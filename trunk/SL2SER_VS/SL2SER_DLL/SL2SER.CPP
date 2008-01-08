/***************************************************************************\
*                                                                           *
* File            SL2ser.cpp (C-Source)                                     *
*                                                                           *
* Compiler        Microsoft Visual C/C++ V6.0                               *
*                                                                           *
*****************************************************************************
*                                                                           *
* X1.00/2000-02-15  (C)  Philips Semiconductors Gratkorn GmbH               *
*                                                                           *
* Serial Library for I·CODE Long Range Reader Module (32 bit DLL)           *
*                                                                           *
*---------------------------------------------------------------------------*
*                                                                           *
* X1.01/2000-02-24/WZ  I·CODE1, I·CODE2-PPP and I·CODE SLI                  *
*     :                                                                     *
* X5.00/2002-07-23/WZ  I·CODE OTP (ePC)                                     *
*     :                                                                     *
* X6.02/2006-06-09/WZ I·CODE SLI-S/XS:  CRM_cmd()                           *
*                                                                           *
\***************************************************************************/

/***************************************************************************\
										  Includes
\***************************************************************************/
#include <windows.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <process.h>

#define  VERSION "SL2ser.dll for Windows 95/98/NT4/2000 (32 bit)"

#define  CRM_SW32_DLL_EXPORT
#include "SL2ser.h"

/***************************************************************************\
										  Defines for CRM Commands
\***************************************************************************/
//CRM Commands
#define CMD_CONFIG              0x0C
#define CMD_GET_INFO            0x0D
#define CMD_SET_PORT            0x0E
#define CMD_GET_PORT            0x0F
//ICODE Commands
#define CMD_ANTICOLL_SELECT     0x10
#define CMD_READ                0x11
#define CMD_READ_UNSELECTED     0x12
#define CMD_WRITE               0x13
#define CMD_HALT                0x14
#define CMD_RESET_QUIET_BIT     0x15
#define CMD_EAS                 0x16

/***************************************************************************\
                                Defines for Serial Communication
\***************************************************************************/
//Possible Baudrates
#define CRMBAUD_57600           57600L
#define CRMBAUD_115200          115200L
//Special Communication Bytes
#define SOH                     0x01
#define STX                     0x02
#define ETX                     0x03
#define ENQ                     0x05
#define ACK                     0x06
#define DLE                     0x10
#define NAK                     0x15
//Protocol specific length definitions
#define HEADER                  4
#define MAXDATA                 256 * (16 * 4 + 1)
#define CHKBYTES                2
//CRC Defines
//CCITT-CRC16 (x^16 + x^12 + x^5 + 1)
#define CRC_POLYNOM             0x8408
#define CRC_PRESET              0xFFFF
//Size of the RBuffer
#define MAXBUF                  (HEADER + MAXDATA + CHKBYTES + 1) //16640
//possible States of the 3964 Statemachine
#define CMD_SEND_STX            0
#define CMD_RECV_DLE            1
#define CMD_SEND_DATA           2
#define CMD_RECV_DLE_NAK        3
#define RESP_RECV_STX           4
#define RESP_SEND_DLE           5
#define RESP_RECV_DATA          6
#define RESP_RECV_DATA_DLE      7
#define RESP_SEND_DLE_NAK       8
#define RESP_END                9
//# of repeats if error
#define MAXATTEMPT              3
//Timeouts for Serial Communication
#define WRITE_TIMEOUT   500     //500 ms write timeout
#define READ_TIMEOUT    500     //500 ms normal read timeout
#define RESP_TIMEOUT    7000    //7 s timeout between command and response
//(needed for detecting a broken line)

/***************************************************************************\
                                Local Variables
\***************************************************************************/
static uchar           SBuffer[270];    //SendBuffer
static uchar           RBuffer[MAXBUF]; //ReceiveBuffer = 16640 bytes long
static uchar           SBufferLen = 0;  //length of sendBuffer
static uint            RPtr       = 0;  //# of receveived Bytes
static uchar           *UPtr      = NULL;//Pointer with Response (internal use only)

static short state; //state of the 3964 Statemachine

//shared memory for indicating dlluse
//#pragma comment(linker, "/SECTION:.shr,RWS")
#pragma data_seg(".shr")    // data in shared memory
BOOL inuse=FALSE;
#pragma data_seg()

OVERLAPPED osReader={0};        //Structure for Read Event, gets signalled, 
                                //when a character is received
OVERLAPPED osWriter={0};        //Structure for Write Event, gets signalled,
                                //when a character is sent
OVERLAPPED osCRM_CmdReady={0};
OVERLAPPED osThreadStartWork={0};
CRITICAL_SECTION cs_inuse;
//structure for workerthread initialisation
typedef struct
{
  uchar cmd;
  ushort len;
  BOOL kill;
}PARAMS, *PPARAMS;

static HANDLE hComHandle = INVALID_HANDLE_VALUE;    //HANDLE for ComPort
static ulong dwLastError;     // holds errorcode on an API error on serial interface
static PARAMS params;
static  uchar OpenComCounter=0;

//Status Variables not exported in DLL anymore, access only through corresp. functs.
uchar CRM_Eot;  //command ready
uchar CRM_Err;  //error variable
uchar CRM_Status;
uchar CRM_CmdValid;
uchar CRM_TxSeq;
uchar CRM_RxSeq;
uint  CRM_Check;
uint  CRM_DataLen;
ulong CRM_Baud;
uint  CRM_TimeSlots;

/***************************************************************************\
                                Local Prototypes
\***************************************************************************/
int  setComTimeouts(ulong dwReadTimeOut, ulong dwWriteTimeOut);
void send_cmd(uchar cmd, ushort len);
int  exec_3964_command (uchar slength, uchar *send);
int  exec_3964_response (ushort *rlength, uchar *receive);
void ThreadProc(PVOID pPARAM);
int  create_Events();
void delete_Events();
void seterr(int error_nr);

/****************************************************************************\
*
* Name      DLLMain
*
\****************************************************************************/
#pragma  warning(disable:4100)  // 'lpReserved', 'hModule': unreferenced formal
										  //                          parameter
//BOOL APIENTRY DllEntryPoint(HANDLE hModule, ulong ul_reason_for_call, LPVOID lpReserved)
BOOL APIENTRY DllMain(HANDLE hModule, ulong ul_reason_for_call, LPVOID lpReserved)
{static HANDLE hThreadProc;

  switch (ul_reason_for_call)
  {
  case DLL_PROCESS_ATTACH:
	 if(inuse)
	 {
		MessageBox(NULL,"DLL is already used by another process!",
		VERSION,MB_ICONEXCLAMATION);
		if (create_Events())
		return FALSE;
		return TRUE;
	 }
	 else
	 {
		inuse=TRUE;
		if (create_Events())
		return FALSE;
		hThreadProc = (HANDLE)_beginthread(ThreadProc,0,&params); //start thread
		break;
	 }
  case DLL_THREAD_ATTACH:
	 break;
  case DLL_THREAD_DETACH:
	 break;
  case DLL_PROCESS_DETACH:
	 if(inuse==TRUE) inuse=FALSE;
	 params.kill=TRUE;
	 //SetEvent(osThreadStartWork.hEvent);
	 TerminateThread(hThreadProc, 0);
	 delete_Events();
	 break;
  }
  return TRUE;
}
#pragma  warning(default:4100)  // unreferenced formal parameter

/***************************************************************************\
                                Exported Commands
\***************************************************************************/

/***************************************************************************\
*
* CRM_init
*
* Prepares interrupt vectors for serial communication.
*
* Inputs    char *ComStr, char *BaudStr, char *IrqStr(deprecated)
*                                        IrqStr: not used anymore. it's not possible
*                                                to set manually the IRQ for COM
* Outputs   -
*
* Return    int OK, CRM_COM_STRING_ERR, CRM_INIT_ERROR, CRM_SERCOM_ERR
*
\***************************************************************************/
int CALL_CONV CRM_init(char *ComStr, char *BaudStr)
{

  DWORD dwRes;                  // result from WaitForSingleObject
  ulong baud=0;
  int           com;
  char portnumber[5]="COM";
  DCB ComPort;
  DWORD dwStoredFlags;
  dwStoredFlags = EV_BREAK;

  //if other process or other thread wants to call Crm_init while in use
  if(hComHandle != INVALID_HANDLE_VALUE)
  {
    return 232;
  }
  CRM_Baud  = 0L;
  CRM_Check = 0;
  CRM_TimeSlots = 0;

  if (ComStr != NULL) //evaluate COMPORT
  {
    if (strlen(ComStr) == 4 && (!strnicmp(ComStr, "COM",3)))
    {
		com = atoi(&ComStr[3]);
      if(com <1 || com>9)
      {
        return (CRM_COM_STRING_ERR);
      }
      strcat(portnumber,&ComStr[3]);
    }
    if (BaudStr != NULL) //evaluate BAUDRATE
    {
      switch (baud = atol(BaudStr))
      {
      case CRMBAUD_57600:
        break;
		default:
		  baud = CRMBAUD_115200;
        break;
      }
    }
    CRM_Baud=baud;
    //setup CommPort                                    //access    //sharemode
    if ((hComHandle= CreateFile( portnumber, GENERIC_READ | GENERIC_WRITE, 0, NULL,
    OPEN_EXISTING, FILE_FLAG_OVERLAPPED, NULL ))==  INVALID_HANDLE_VALUE)
    {
      dwLastError= GetLastError();
      return(CRM_SERCOM_ERR);
    }
    //setup comm, define size of inbuffer and outbuffer
    SetupComm( hComHandle, 32767, 1024 );

    //abort all outstandig r/w operations
    PurgeComm( hComHandle, PURGE_TXABORT | PURGE_RXABORT | 
                           PURGE_TXCLEAR | PURGE_RXCLEAR );
    // Set Com State
    ComPort.DCBlength= sizeof( DCB );
    GetCommState( hComHandle, &ComPort );
    ComPort.BaudRate= baud;//baud;
    ComPort.ByteSize= 8;    //8 Databits
    ComPort.Parity  = NOPARITY; //no Parity
    ComPort.StopBits= ONESTOPBIT;    //1 Stopbit
    ComPort.fBinary = TRUE; //has to be true under NT (see API)
    ComPort.fParity=FALSE;
    ComPort.fOutxDsrFlow = FALSE;      // DSR output flow control
	 ComPort.fDsrSensitivity = FALSE;   // DSR sensitivity
	 ComPort.fOutX= FALSE;
    ComPort.fInX = FALSE;
    ComPort.fDtrControl = FALSE;       // DTR flow control type
    ComPort.fRtsControl= FALSE;       // RTS flow control
	 ComPort.fOutxCtsFlow = FALSE;      // CTS output flow control
	 ComPort.fErrorChar = FALSE;       // enable error replacement
	 ComPort.fNull = FALSE;            // enable null stripping
	 ComPort.fAbortOnError = FALSE;

	 if( !SetCommState( hComHandle, &ComPort ) )
	 {
		dwLastError= GetLastError();

		CloseHandle (hComHandle);
		return(CRM_SERCOM_ERR);
	 }

	 if (!SetCommMask(hComHandle, dwStoredFlags))
	 {
		dwLastError= GetLastError();
		CloseHandle (hComHandle);
		return(CRM_SERCOM_ERR);
	 }

	 if(setComTimeouts(READ_TIMEOUT, WRITE_TIMEOUT))
	 {
		return(CRM_SERCOM_ERR);
	 }

	 //get timeslots
	 CRM_get_info(CRM_GET_TIMESLOTS, RBuffer);
	 dwRes = WaitForSingleObject(osCRM_CmdReady.hEvent, INFINITE);

	 switch(dwRes)
	 {
		// Overlapped event has been signaled.
	 case WAIT_OBJECT_0:
		break;
	 default:
		return (CRM_SERCOM_ERR);
		break;
	 }

	 if (CRM_Status == OK)
	 {
		CRM_TimeSlots = *(int *)RBuffer;
		return (OK);
	 }
	 return (CRM_SERCOM_ERR);
  }
  return (CRM_COM_STRING_ERR);
}

/***************************************************************************\
*
* CRM_exit
*
* Closes the Comport
*
* Inputs    -
* Outputs   -
*
* Return    - int OK
*
\***************************************************************************/
int CALL_CONV CRM_exit (void)
{
  if ( hComHandle != INVALID_HANDLE_VALUE )
  {
	 CloseHandle(hComHandle);
	 hComHandle=INVALID_HANDLE_VALUE;
  }
  return (OK);
}

/***************************************************************************\
										  CRM Commands
\***************************************************************************/

/****************************************************************************\
*
* Name      CRM_config
*
*           Change some CRM parameters
*
* Inputs    mode        ... defines the mode (what to change)
*           confbyte    ... parameter byte for mode
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_config(uchar mode, uchar confbyte)
{
  SBuffer[4] = mode;
  SBuffer[5] = confbyte;
  send_cmd(CMD_CONFIG, 2);
}

/****************************************************************************\
*
* Name      CRM_get_info
*
*           Supplies information of current configuration of the CRM
*
* Inputs    mode        ... defines the mode (which information to get)
*           info        ... response, providing the info
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_get_info(uchar mode, uchar *info)
{
  SBuffer[4] = mode;
  UPtr = info;
  send_cmd(CMD_GET_INFO, 1);
}

/****************************************************************************\
*
* Name      CRM_set_port
*
*           Modifies the output port bits of the CRM
*
* Inputs    portbyte    ... byte in which the bits are corresponding to the
*                           output port bits
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_set_port(uchar portbyte)
{
  SBuffer[4] = portbyte;
  send_cmd(CMD_SET_PORT, 2);
}

/****************************************************************************\
*
* Name      CRM_get_port
*
*           Reads the input port bits of the CRM
*
* Inputs    *portbyte   ... byte in which the bits are corresponding to the
*                           input port bits
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_get_port(uchar *portbyte)
{
  UPtr = portbyte;
  send_cmd(CMD_GET_PORT, 0);
}

/***************************************************************************\
                                I-CODE Commands
\***************************************************************************/
void CALL_CONV CRM_test(uchar cmd, uchar txlen, uchar rxlen, uchar *tx, uchar *rx)
{
  SBuffer[4] = txlen;
  SBuffer[5] = rxlen;
  memcpy(&SBuffer[6], tx, txlen);
  UPtr = rx;
  send_cmd(cmd, (ushort)(txlen + 2));
}

/****************************************************************************\
*
* Name      CRM_anticoll_select
*
*           Selects the labels (anticollision routine)
*
* Inputs    hash    ... hash value
*           tse     ... timeslotexponent, defines the number of timeslots 
*                       n = 2^(tse+1)   (n = 1 if tse = 0)
*
* Outputs   *resp   ... response from reader
*
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_anticoll_select(uchar hash, uchar tse, uchar *resp)
{
  CRM_TimeSlots = ((tse & 0x07) == 0)? 1 : 2 << (tse & 0x07);
  SBuffer[4] = (uchar)((tse << 5) | (hash & 0x1F));
  UPtr = resp;
  send_cmd(CMD_ANTICOLL_SELECT, 1);
}

/****************************************************************************\
*
* Name      CRM_read
*
*           Reads nobl blocks(4 bytes) beginning at blnr of selected labels only
*
* Inputs    blnr    ... startblock from which to be read
*           nobl    ... number of of blocks to read
*
* Outputs   *resp   ... data
*
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_read(uchar blnr, uchar nobl, uchar *resp)
{
  SBuffer[4] = blnr;
  SBuffer[5] = nobl;
  UPtr = resp;
  send_cmd(CMD_READ, 2);
}

/****************************************************************************\
*
* Name      CRM_read_unselected
*
*           Reads nobl blocks(4 bytes) beginning at blnr of unselected labels
*
* Inputs    hash    ... hash value
*           tse     ... timeslotexponent, defines the number of timeslots 
*                       n = 2^(tse+1)   (n = 1 if tse = 0)
*           blnr    ... startblock from which to be read
*           nobl    ... number of of blocks to read
*
* Outputs   *resp   ... data
*
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_read_unselected(uchar hash, uchar tse, uchar  blnr,
											  uchar nobl, uchar *resp)
{
  SBuffer[4] = (uchar)((tse << 5) | (hash & 0x1F));
  SBuffer[5] = blnr;
  SBuffer[6] = nobl;
  UPtr = resp;
  send_cmd(CMD_READ_UNSELECTED, 3);
}

/****************************************************************************\
*
* Name      CRM_write
*
*           Writes one block of 4 bytes to selected labels
*
* Inputs    hash    ... hash value
*           blnr    ... block to which data should be written
*           *data   ... data to write (4 bytes)
*           *tsl    ... timeslotvector, if bit set, data is written to label 
*                       in this timeslot
*
* Outputs   *resp   ... response from reader
*
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_write(uchar hash, uchar blnr, uchar *data,
                         uchar *tsl, uchar *resp)
{
  SBuffer[4] = hash;
  SBuffer[5] = blnr;
  memcpy(&SBuffer[6], data, 4);
  memcpy(&SBuffer[10], tsl, (uchar)((CRM_TimeSlots + 7)/8));
  UPtr = resp;
  send_cmd(CMD_WRITE, (ushort)(((CRM_TimeSlots + 7)/8) + 6));
}

/****************************************************************************\
*
* Name      CRM_halt
*
*           sets selected labels into halt mode depending on corresponding bit 
*           in timeslotvector
*
* Inputs    hash    ... hash value
*           *tsl    ... timeslotvector, if bit set, data is written to label 
*                       in this timeslot
*
* Outputs   *resp   ... response from reader
*
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_halt(uchar hash, uchar *tsl, uchar *resp)
{
  SBuffer[4] = hash;
  memcpy(&SBuffer[5], tsl, (uchar)((CRM_TimeSlots + 7)/8));
  UPtr = resp;
  send_cmd(CMD_HALT, (uchar)((CRM_TimeSlots + 7)/8) + 1);
}

/****************************************************************************\
*
* Name      CRM_reset_QUIET_bit
*
*           clears the QUIET bit of all labels
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_reset_QUIET_bit(void)
{
  UPtr = NULL;
  send_cmd(CMD_RESET_QUIET_BIT, 0);
}

/****************************************************************************\
*
* Name      CRM_eas
*
*           each label responds with a pattern of 256 bit to the eas command,
*           if it's EAS bits are set. works in selected and unselected state
*           and is not restricted to timeslots
*
* Outputs   resp ... response
*
* Return    Status
*
\****************************************************************************/
void CALL_CONV CRM_eas(uchar *resp)
{
  UPtr = resp;
  send_cmd(CMD_EAS, 0);
}

/***************************************************************************\
              General Commands exporting various state variables
\***************************************************************************/

/****************************************************************************\
*
* Name      CRM_get_eot
*
*           returns the current value of CRM_Eot
*
* Return    CRM_Eot
*
\****************************************************************************/
uchar CALL_CONV CRM_get_eot(void)
{
  return (CRM_Eot);
}

/****************************************************************************\
*
* Name      CRM_get_err
*
*           returns the current value of CRM_Err
*
* Return    CRM_Err
*
\****************************************************************************/
uchar CALL_CONV CRM_get_err(void)
{
  return (CRM_Err);
}

/****************************************************************************\
*
* Name      CRM_get_status
*
*           returns the current value of CRM_Status
*
* Return    CRM_Status
*
\****************************************************************************/
uchar CALL_CONV CRM_get_status(void)
{
  return (CRM_Status);
}

/****************************************************************************\
*
* Name      CRM_get_cmdvalid
*
*           returns the current value of CRM_CmdValid
*
* Return    CRM_CmdValid
*
\****************************************************************************/
uchar CALL_CONV CRM_get_cmdvalid(void)
{
  return (CRM_CmdValid);
}

/****************************************************************************\
*
* Name      CRM_get_txseq
*
*           returns the current value of CRM_TxSeq
*
* Return    CRM_TxSeq
*
\****************************************************************************/
uchar CALL_CONV CRM_get_txseq(void)
{
  return (CRM_TxSeq);
}

/****************************************************************************\
*
* Name      CRM_get_rxseq
*
*           returns the current value of CRM_RxSeq
*
* Return    CRM_RxSeq
*
\****************************************************************************/
uchar CALL_CONV CRM_get_rxseq(void)
{
  return (CRM_RxSeq);
}

/****************************************************************************\
*
* Name      CRM_get_check
*
*           returns the current value of CRM_Check (crc check sequence)
*
* Return    CRM_Check
*
\****************************************************************************/
uint CALL_CONV CRM_get_check(void)
{
  return (CRM_Check);
}

/****************************************************************************\
*
* Name      CRM_get_datalen
*
*           returns the current value of CRM_DataLen
*
* Return    CRM_DataLen
*
\****************************************************************************/
uint CALL_CONV CRM_get_datalen(void)
{
  return (CRM_DataLen);
}

/****************************************************************************\
*
* Name      CRM_get_getbaud
*
*           returns the current Baudrate
*
* Return    CRM_Baud
*
\****************************************************************************/
ulong CALL_CONV CRM_get_baud(void)
{
  return (CRM_Baud);
}
/****************************************************************************\
*
* Name      CRM_get_timeslots
*
*           returns the current timeslots
*
* Return    CRM_TimeSlots
*
\****************************************************************************/
uint CALL_CONV CRM_get_timeslots(void)
{
  return (CRM_TimeSlots);
}

/****************************************************************************\
*
* Name      CRM_get_Win32_error
*
*           returns the current error of the Comport (from API)
*
* Inputs    showMessage ...  if !=0, a MessageBox with the formatted win32 
*                            Errorstring is created, returns only the ErrorCode
*
* Return    dwLastError
*
\****************************************************************************/
ulong CALL_CONV CRM_get_win32_error(uchar *errtext, uint  *tlength,
                                    uchar showMessage)
{
  LPVOID lpMsgBuf;
  char *MsgBuf;
  char header[]="Serial Communication Error!\n\n";
  uint length;

  length=FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM |
                       FORMAT_MESSAGE_IGNORE_INSERTS, NULL,dwLastError,
                       MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
                       (LPTSTR) &lpMsgBuf,0,NULL);
  *tlength=length;

  MsgBuf= (char*)malloc(strlen(header)*sizeof(char) + 
               strlen((char*)lpMsgBuf)*sizeof(char));

  strcpy(MsgBuf,"COM port already used!\n\n");

  if(dwLastError!=0)    strcat(MsgBuf,(char*)lpMsgBuf);

  if (length>255)       length = 255;
  if(dwLastError!=0)
	 memcpy(errtext,(uchar*)lpMsgBuf,length);
  else
  {
	 *tlength = strlen(header);
	 memcpy(errtext,(uchar*)header,*tlength);
  }
  if(showMessage)
  {
    // Display the string.
	 MessageBox( NULL, (LPCTSTR)MsgBuf, VERSION, MB_OK);
  }
  // Free the buffer.
  LocalFree(lpMsgBuf);
  LocalFree(MsgBuf);

  return dwLastError;
}

/****************************************************************************\
*
* Name      CRM_get_error_text
*
*           returns a textual description of error_number
*
* Inputs    int error_number  ... the number of the error
*
* Outputs   char** error_text ... textual description of the error
*
*
\****************************************************************************/
void CALL_CONV CRM_get_error_text(int error_number, char **error_text)
{
  char *text[] =
  {
	 #include "SL2err.h"
  };

  if (error_number < 0 || error_number > 255)
  {
    *error_text = text[CRM_NO_ERR_TEXT_AVAIL];

    CRM_Status = CRM_NO_ERR_TEXT_AVAIL;
  }
  else
  {
    *error_text = text[error_number];
    CRM_Status  = OK;
  }

  CRM_DataLen = strlen(*error_text);
  CRM_Eot     = TRUE;
}

/****************************************************************************\
*
* Name      CRM_cmd
*
* Inputs    cmd   ... command code
*           txlen ... number of bytes to transmit
*           rxlen ... number of expected bytes in response
*           *tx   ... bytes to transmit
*
* Outputs   *rx   ... response from reader
*
\****************************************************************************/
void CALL_CONV CRM_cmd(uchar cmd, ushort txlen, ushort rxlen, uchar *tx, uchar *rx)
{
  memcpy(&SBuffer[4], &txlen, 2);
  memcpy(&SBuffer[6], &rxlen, 2);
  memcpy(&SBuffer[8], tx, txlen);
  UPtr = rx;
  send_cmd(cmd, (ushort)(txlen + 4));
}

/****************************************************************************\
*
* name      create_Events
*
*           creates a ReadEvent and a WriteEvent for synchronized
*           serial communication
*
* Inputs    -
*
* Outputs   -
*
* Return    OK, CRM_SERCOM_ERR
*
\****************************************************************************/
int create_Events()
{
  InitializeCriticalSection(&cs_inuse);

  osThreadStartWork.hEvent=CreateEvent(NULL, FALSE, FALSE, NULL);
  if (osThreadStartWork.hEvent == NULL)
  {   // Error creating overlapped event; abort.
    return CRM_SERCOM_ERR;
  }


  //create ReadEvent
  osReader.hEvent = CreateEvent(NULL, TRUE, FALSE, NULL);
  if (osReader.hEvent == NULL)
  {   // Error creating overlapped event; abort.
    return CRM_SERCOM_ERR;
  }
  //create WriteEvent
  osWriter.hEvent = CreateEvent(NULL, TRUE, FALSE, NULL);
  if (osWriter.hEvent == NULL)
  {  // error creating overlapped event handle
    return CRM_SERCOM_ERR;
  }

  osCRM_CmdReady.hEvent = CreateEvent(NULL, FALSE, FALSE, ICODEEVENT);
                                                       // EVENT_RESP_DONE);
  if (osCRM_CmdReady.hEvent == NULL)
  {  // error creating overlapped event handle
    return CRM_SERCOM_ERR;
  }
  return OK;
}

/****************************************************************************\
*
* name      delete_Events
*
*           deletes all created Communication Events
*
* Inputs
*
* Outputs
*
* Return
*
\****************************************************************************/
void delete_Events()
{
  DeleteCriticalSection(&cs_inuse);
  CloseHandle(osThreadStartWork.hEvent);
  CloseHandle(osReader.hEvent);
  CloseHandle(osWriter.hEvent);
  CloseHandle(osCRM_CmdReady.hEvent);
  return;
}

/***************************************************************************\
*
* set_com_time_outs
*
* se ts the read and write timeouts for serial communication
*
* Inputs    u.long dwReadTimeOut : timeout to wait for first char in msec
*           u.long dwWriteTimeOut: timeout to wait for write complete in msec
* Outputs   -
*
* Return    - int OK , int CRM_SERCOM_ERR
*
\***************************************************************************/
int setComTimeouts( ulong dwReadTimeOut, ulong dwWriteTimeOut )
{
  COMMTIMEOUTS xCommTimeOuts;

  if( hComHandle == INVALID_HANDLE_VALUE )              return CRM_SERCOM_ERR;
  //set readtimeouts
  xCommTimeOuts.ReadIntervalTimeout = MAXDWORD;
  xCommTimeOuts.ReadTotalTimeoutMultiplier = MAXDWORD;
  xCommTimeOuts.ReadTotalTimeoutConstant = dwReadTimeOut;
  //set writetimeouts
  xCommTimeOuts.WriteTotalTimeoutMultiplier = 0;
  xCommTimeOuts.WriteTotalTimeoutConstant = dwWriteTimeOut; // longer than 500 ms
  if( !SetCommTimeouts( hComHandle, &xCommTimeOuts ))   return CRM_SERCOM_ERR;
  else                                                  return OK;
}

/****************************************************************************\
*
* Name      ThreadProc(PVOID pPARAM)
*
*           Threadroutine handling the 3964 protocol
*
* Inputs    PVOID pPARAM - pointer to struct param
*
*           pParam.cmd ... command (hex)
*           pParam.len ... length of INFO
*
* Return    -
*
\****************************************************************************/
void ThreadProc(PVOID pPARAM)
{
  static uint  i,j;
  static ushort  crc;
  static ushort lrec;
  static int rstate;
  static uchar receive[MAXBUF];
  static PPARAMS pparams;

  pparams = (PPARAMS) pPARAM;
  SetPriorityClass(GetCurrentProcess(),HIGH_PRIORITY_CLASS);
  //  SetThreadPriority(GetCurrentThread(),THREAD_PRIORITY_HIGHEST);

  #pragma  warning(disable:4127)  // conditional expression is constant

  while(1)
  {
	 //wait until something to do

	 WaitForSingleObject(osThreadStartWork.hEvent, INFINITE);

	 ResetEvent(osCRM_CmdReady.hEvent);
	 ResetEvent(osReader.hEvent);
	 ResetEvent(osWriter.hEvent);
	 //abort all outstandig r/w operations
	 PurgeComm( hComHandle, PURGE_TXABORT | PURGE_RXABORT |
									PURGE_TXCLEAR | PURGE_RXCLEAR );
	 if(pparams->kill==TRUE)
	 {
		break;
	 }
	 CRM_Eot =FALSE;
	 SBuffer[0] = ++CRM_TxSeq;                       //Sequence Number
	 SBuffer[1] = pparams->cmd;                      //command
	 *((ushort *)&SBuffer[2]) = pparams->len;        //length (only par. of command)
	 pparams->len += HEADER;                         //add header length
	 SBufferLen = (uchar)(pparams->len + CHKBYTES);  //length of complete command
	 //crc clculation
	 crc = CRC_PRESET;

	 for (i = 0; i < pparams->len; i++)
	 {
		crc ^= (ushort)SBuffer[i];

		for (j = 0; j < 8; j++)
		{
		  if (crc & 0x0001)
		  {
			 crc = (ushort)((crc >> 1) ^ CRC_POLYNOM);
		  }
		  else
		  {
			 crc = (ushort)(crc >> 1);
		  }
		}
	 }
	 *((ushort *)&SBuffer[pparams->len]) = crc;      //add crc to sendbuffer

	 // perform 3964 protocol
	 if ((rstate = exec_3964_command (SBufferLen, SBuffer)) != OK)
	 {
		EnterCriticalSection(&cs_inuse);
		CRM_Status = (uchar)rstate;
		LeaveCriticalSection(&cs_inuse);
	 }
	 else
	 {
		if((rstate = exec_3964_response (&lrec, receive)) != OK)
		{
		  CRM_Status = (uchar)rstate;
		}
		else
		{
		  EnterCriticalSection(&cs_inuse);
		  memcpy (UPtr, &receive[4], lrec);    // copy DATA
		  CRM_CmdValid=TRUE;
		  CRM_Eot=TRUE;
		  CRM_Status = (receive[1]);       // return Statusbyte
		  LeaveCriticalSection(&cs_inuse);
		}
	 }
	 SetEvent(osCRM_CmdReady.hEvent);
  }

  _endthread();
}

/****************************************************************************\
*
* Name      send_cmd(uchar cmd, ushort len)
*
*           dispatch routine, starts the thread
*
*
* Inputs    cmd ... command (hex)
*           len ... length of INFO
*
* Return    -
*
\****************************************************************************/
void send_cmd(uchar cmd, ushort len)
{
  params.cmd = cmd;         //command
  params.len = len;
  params.kill = FALSE;
  CRM_Eot = FALSE;
  SetEvent(osThreadStartWork.hEvent);

}

/****************************************************************************\
*
* name      read_unblocked
*
*           receives one character via API
*           if no byte can be read immediately, the main thread is suspended
*           until event "osReader" occurs, or until timeout occours.
*
* Inputs    timeout ... timeout [ms]
*
* Outputs   ch       ... received character
*
* Return    OK, CRM_SERCOM_ERR
*
\****************************************************************************/
int read_unblocked(uchar *ch,DWORD readTimeout)
{
  static char RecBuf[2];
  static ulong dwBytesRead = 0;
  static DWORD      dwRes;           // result from WaitForSingleObject

  //set timeout - default 500ms. changed only when waiting for response
  if(setComTimeouts(readTimeout, WRITE_TIMEOUT))
  return CRM_SERCOM_ERR;
  // Issue read operation

  if (!ReadFile(hComHandle, RecBuf, 1, &dwBytesRead, &osReader))
  {
	 if (GetLastError() != ERROR_IO_PENDING)     // read not delayed?
	 {// Error in communications; report it.
		dwLastError = GetLastError();
		return CRM_SERCOM_ERR;
	 }
	 else  //wait for completion of read
	 {
		dwRes = WaitForSingleObject(osReader.hEvent,readTimeout);
		switch(dwRes)
      {
        // Read completed.
      case WAIT_OBJECT_0:
        if (!GetOverlappedResult(hComHandle, &osReader, &dwBytesRead, FALSE))
        {
          dwLastError = GetLastError();
          return CRM_SERCOM_ERR;
        }
        else
        {   //read successfull
          if(dwBytesRead != 1)
          {
            return CRM_SERCOM_ERR;
          }
          RecBuf[dwBytesRead]= '\0';
          *ch= *RecBuf;
          return OK;
        }
      case WAIT_TIMEOUT: //timeout occourred
        CRM_Err=211; //timeout at receiving a response
        dwLastError = GetLastError();
        return CRM_SERCOM_ERR;
      default:
        // Error in the WaitForSingleObject; abort.
		  // This indicates a problem with the OVERLAPPED structure's event handle.
        dwLastError = GetLastError();
        return CRM_SERCOM_ERR;
      }
	 }
  }
  else
  {
    if(dwBytesRead != 1)
    {
      return CRM_SERCOM_ERR;
    }
    RecBuf[dwBytesRead]= '\0';
    *ch= *RecBuf;
    return OK;
  }
}

/****************************************************************************\
*
* Name      send_unblocked
*
*           transmits one character via API
*           if byte cannot be written immediately, the main thread is suspended
*           until event osWriter occurs, or until timeout occours.
*
* Inputs    ch ... character to transmit
*
* Return    OK, CRM_SERCOM_ERR
*
\****************************************************************************/
int send_unblocked (uchar ch)
{
  DWORD dwRes;           // result from WaitForSingleObject
  ulong dwBytesWritten;
  //issue write operation
  if (!WriteFile(hComHandle, &ch, 1,&dwBytesWritten, &osWriter))
  {
    if (GetLastError() != ERROR_IO_PENDING)
    {
      // WriteFile failed, but isn't delayed. Report error and abort.
      dwLastError = GetLastError();
      return CRM_SERCOM_ERR;
    }
    else
    {
      // Write is pending.
      dwRes = WaitForSingleObject(osWriter.hEvent, WRITE_TIMEOUT);
      //dwRes = WaitForSingleObject(osWriter.hEvent, INFINITE);
      switch(dwRes)
      {
        // OVERLAPPED structure's event has been signaled.
      case WAIT_OBJECT_0:
        if (!GetOverlappedResult(hComHandle, &osWriter, &dwBytesWritten, FALSE))
        {
          dwLastError = GetLastError();
          return CRM_SERCOM_ERR;
        }
		  else
        {
          // Write operation completed successfully.
          return OK;
		  }
      default:
        // An error has occurred in WaitForSingleObject.
        // This usually indicates a problem with the
        // OVERLAPPED structure's event handle.

        dwLastError = GetLastError();
        return CRM_SERCOM_ERR;
      }
    }
  }
  else
  {
    // WriteFile completed immediately.
    return OK;
  }
}

/****************************************************************************\
*
* Name      exec_3964_command
*
*           1st part of 3964, manages the host's part - send command
*
* Outputs   ushort slength ... length of the command to send (incl. header, crc)
*           uchar *send ... pointer to the command to send
*
* Return    Status (OK, CRM_SERCOM_ERR)
*
\****************************************************************************/
int exec_3964_command(uchar slength, uchar *send)
{
  state = CMD_SEND_STX;
  int rstate,i;
  uchar ch='!';
  int attempt =MAXATTEMPT;
  int repeat = MAXATTEMPT;

  CRM_CmdValid= FALSE;
  CRM_Err=OK;
  CRM_Status=OK;

  while(state != RESP_RECV_STX)
  {
    switch(state)
    {
    case CMD_SEND_STX:      //send STX, receive DLE
      if((rstate=send_unblocked( STX)) != OK)
      {
        seterr(CRM_SERCOM_ERR);
        return rstate;
      }
      if (((rstate=read_unblocked (&ch,READ_TIMEOUT)) ==0) && (ch==DLE))
      {
		  state = CMD_SEND_DATA;
      }
      else
      {
		  if (--attempt <1)//3 trials
        {
          seterr(222);
          return CRM_SERCOM_ERR;
        }
      }
      break;
    case CMD_SEND_DATA:         //send datablock +DLE+ETX
      for ( i= 0;i<slength; i++)
      {
        if ((rstate = send_unblocked(send[i])) != OK)
        {
          seterr(CRM_SERCOM_ERR);
          return rstate;
        }
        if(send[i]==DLE)
        {
          if ((rstate = send_unblocked(DLE)) != OK) //send DLE twice
          {
            seterr(CRM_SERCOM_ERR);
            return rstate;
          }
        }
      }
      if ((rstate = send_unblocked(DLE)) != OK)  //send DLE
		{
        seterr(CRM_SERCOM_ERR);
        return rstate;
      }
		if ((rstate = send_unblocked(ETX)) != OK)  //+ETX
      {
        seterr(CRM_SERCOM_ERR);
        return rstate;
      }
      state  = CMD_RECV_DLE_NAK;
      break;
    case CMD_RECV_DLE_NAK:          //receive DLE or NAK
      if (((rstate=read_unblocked (&ch,READ_TIMEOUT)) ==0) && (ch==DLE))
      {
        state = RESP_RECV_STX;
        CRM_CmdValid=TRUE;
      }
      else
      {
        if (--repeat <1)    //if no DLE reveiced, repeat up to 3 times
        {
          seterr(233);
          return CRM_SERCOM_ERR;
        }
        attempt=MAXATTEMPT;
        state = CMD_SEND_STX;
      }
      break;
    }
  }
  return (OK);
}

/****************************************************************************\
*
* Name      exec_3964_response
*
*           2nd part of 3964, manages the reader's response
*
* Outputs   ushort rlength ... length of received response (incl. header, crc)
*           uchar *receive ... pointer to the received data
*
* Return    Status (OK, CRM_SERCOM_ERR)
*
\****************************************************************************/
int exec_3964_response (ushort *rlength, uchar *receive)
{

  int rstate,i,rcounter=0;
  uchar ch='!';
  int attempt =MAXATTEMPT;

  CRM_CmdValid= FALSE;
  CRM_Err=OK;
  CRM_Status=OK;

  while(state != RESP_END)
  {
	 switch(state)
	 {
	 case RESP_RECV_STX:   //wait for Response (receive STX)
		if (((rstate=read_unblocked (&ch, RESP_TIMEOUT)) ==0)&&(ch==STX))
		{
		  state = RESP_SEND_DLE;
		  break;
		}
		else
		{
		  seterr(CRM_SERCOM_ERR);
		  return CRM_SERCOM_ERR;
		}
		break;
    case RESP_SEND_DLE:     //send DLE
      if ((rstate = send_unblocked(DLE)) != OK)
      {
        return rstate;
      }
      state = RESP_RECV_DATA;
      rcounter=0;
      break;
    case RESP_RECV_DATA:        //receive databytes of response
      if(rcounter>(HEADER +MAXDATA+CHKBYTES))
      {
        CRM_Err = 244;
        return CRM_SERCOM_ERR;
      }
      if((rstate = read_unblocked(&ch,READ_TIMEOUT)) != OK)
		{
        seterr(CRM_SERCOM_ERR);
        return rstate;
      }
		if(ch==DLE)
      {
        state=RESP_RECV_DATA_DLE;
        break;
      }
      receive[rcounter++]=ch;
      break;
    case RESP_RECV_DATA_DLE:    //DLE or END of response?
      if((rstate = read_unblocked(&ch,READ_TIMEOUT)) != OK)
      {
        seterr(CRM_SERCOM_ERR);
        return rstate;
      }
      if (ch==ETX)  //DLE ETX ->END of Response
      {
        if(rcounter< (HEADER+CHKBYTES))
        {
          CRM_Err=243;
          return CRM_SERCOM_ERR;
        }
        if(rcounter >(HEADER +MAXDATA+CHKBYTES))
        {
          CRM_Err = 244;
          return CRM_SERCOM_ERR;
        }
		  if(( *((ushort*)&receive[2]) +HEADER +CHKBYTES) != rcounter)
        {
          CRM_Err = 245;
          return CRM_SERCOM_ERR;
		  }
        state = RESP_SEND_DLE_NAK;
        break;
      }
      if(ch==DLE)     //DLE DLE Sequence
      {
        receive[rcounter++]=ch;
        state=RESP_RECV_DATA;
      }
      break;
    case RESP_SEND_DLE_NAK:     //responce done,so check CRC, send DLE or NAK
      CRM_Check= CRC_PRESET;
      for (i= 0;i<rcounter;i++)
      {
        CRM_Check ^=(uint) receive[i];
        for (int j=0; j<8;j++)
        {
          if (CRM_Check & 0x0001)
          {
            CRM_Check= (CRM_Check >> 1) ^ CRC_POLYNOM;
          }
          else
          {
            CRM_Check= (CRM_Check >> 1);
          }
		  }
      }
      if (CRM_Check != 0)
      {
		  CRM_Err =246;
        if((rstate = send_unblocked(NAK)) != OK)
          return rstate;
        if((--attempt)>1) state = RESP_RECV_STX;
        else              state = RESP_END;
      }
      if((rstate = send_unblocked(DLE)) != OK)
        return rstate;
      state = RESP_END;
      break;
    }//end switch
  }//end while
  CRM_RxSeq=receive[0];
  if((receive[2] !=0) && UPtr==NULL)
  {
    CRM_Err =247;
    CRM_DataLen=0;
    return CRM_SERCOM_ERR;
  }
  else
  {
    CRM_DataLen = *((ushort *)&receive[2]);
    *rlength = *((ushort *)&receive[2]);
    return (OK);
  }
}

/****************************************************************************\
*
* Name      seterr
*
*           sets Error Code and Status Variables to correct value in case of error
*
* Inputs    error_nr ... corresponding error number
*
* Outputs   -
*
* Return    -
*
\****************************************************************************/
void seterr(int error_nr)
{
  RPtr=0;
  UPtr=NULL;
  if (error_nr < 240 || error_nr == 255)
  CRM_Err=(uchar)error_nr;
  CRM_Status=CRM_SERCOM_ERR;
  CRM_DataLen=0;
  CRM_CmdValid=TRUE;
  CRM_Eot=TRUE;
}

/***************************************************************************\
                                End of the File
\***************************************************************************/
