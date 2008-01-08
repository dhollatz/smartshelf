/***************************************************************************\
*                                                                           *
* File            SL2ser.h   (C-Header for SL2ser.cpp)                      *
*                                                                           *
* Compiler        Microsoft Visual C/C++ V6.0                               *
*                                                                           *
*****************************************************************************
*                                                                           *
* X1.00/2000-02-14  (C)  Philips Semiconductors Gratkorn GmbH               *
*                                                                           *
* Serial Library for I·CODE Long Range Reader Module (32 bit DLL)           *
*                                                                           *
*---------------------------------------------------------------------------*
*                                                                           *
* X1.01/2000-03-06/WZ I·CODE1, I·CODE2-PPP and I·CODE SLI                   *
*     :                                                                     *
* X5.00/2002-07-23/WZ I·CODE OTP (ePC)                                      *
* X5.02/2002-12-13/WZ I·CODE OTP (ePC): ..._ePC_CRC8_PRESET                 *
* X5.05/2003-07-11/WZ I·CODE OTP (ePC): ..._ePC_BYTES                       *
* X5.08/2004-09-24/WZ I·CODE1/SLI/OTP:  ..._RX_DLY_OFFS                     *
*     :                                                                     *
* X6.02/2006-06-09/WZ I·CODE SLI-S/XS:  CRM_cmd()                           *
*                                                                           *
\***************************************************************************/

/***************************************************************************\
                                Defines
\***************************************************************************/

#define uchar                           unsigned char
#define uint                            unsigned int    // 32 bit @ win32
#define ushort                          unsigned short  // 16 bit @ win32
#define ulong                           unsigned long

/***************************************************************************\
                                CRM Command Codes
\***************************************************************************/

#define CMD_EPC                         0xF0
#define CMD_TST                         0xF8
#define CMD_CMD                         0xFC
#define CMD_ISO                         0xFF

/***************************************************************************\
                                CRM Status Codes
\***************************************************************************/

#define OK                              0

// Label Communication Status

#define CRM_NO_TAG                      1
#define CRM_CRC_ERR                     2
#define CRM_COLLISION                   3
#define CRM_WRONG_SNR                   4
#define CRM_WEAK_COLLISION              8
#define CRM_NO_WRITE_OK                 16
#define CRM_NO_HALT_OK                  32

// Serial Communication Status

#define CRM_WRONG_PARAMETER             98
#define CRM_CMD_INTERRUPTED             99
#define CRM_WRONG_CMD                   100
#define CRM_CMD_NY_IMPLEMENTED          101

#define CRM_NO_ERR_TEXT_AVAIL           199
#define CRM_BREAK                       200

#define CRM_NO_DLE_REC                  222
#define CRM_CREATE_EVENT_ERR            230
#define CRM_INIT_ERR                    251
#define CRM_COM_STRING_ERR              252
#define CRM_FIFO_ERR                    253
#define CRM_EXIT_ERR                    254
#define CRM_SERCOM_ERR                  255

/***************************************************************************\
                                CRM Config Codes
\***************************************************************************/

#define CFG_RF_PAUSE_INIT               0
#define CFG_INIT                        1
#define CFG_DEFAULT                     2
#define CFG_EAS_LEVEL                   3
#define CFG_EAS_LENGTH                  4
#define CFG_PULSE_OFFSET                5
#define CFG_PULSE_LENGTH                6
#define CFG_RF_OFF_ON                   7
#define CFG_FAST_MODE                   8
#define CFG_FAMILY_CODE                 9
#define CFG_APPLICATION_ID              10
#define CFG_MOD_DEPTH                   11
#define CFG_RF_POWER                    12
#define CFG_FAST_OFFSET                 13
#define CFG_FAST_LENGTH                 14
#define CFG_START_OFFSET                15
#define CFG_BIT_THRESHOLD               16
#define CFG_INIT_TS_TABLE               17
#define CFG_HALT_MODE                   18
#define CFG_NOISELEVEL_MODE             19
#define CFG_SET_NOISE_MIN               20
#define CFG_SET_NOISE_AVG               21
#define CFG_SET_NOISE_MAX               22
#define CFG_MOD_DEPTH_NOSTORE           23
#define CFG_RF_POWER_NOSTORE            24
#define CFG_IGNORE_WEAK_COLLISION       25
#define CFG_ISO_15693                   26
#define CFG_PULSE_OFFSET_ISO            27
#define CFG_PULSE_LENGTH_ISO            28
#define CFG_RESP_OFFS_ISO_LOCK          29
#define CFG_RESP_OFFS_ISO_WRITE         30
#define CFG_PULSE_OFFSET_ePC            31
#define CFG_PULSE_LENGTH_ePC            32
#define CFG_ePC_TESTMODE                33
#define CFG_ePC_CRC8_PRESET             34
#define CFG_ePC_BYTES                   35
#define CFG_RX_DLY_OFFS                 36

/***************************************************************************\
                                CFG Status Codes
\***************************************************************************/

// CFG_BIT_THRESHOLD

#define BIT_THRESHOLD_FACTOR            3

// CFG_HALT_MODE

#define TIMESLOTS_AUTO_OFF              0
#define TIMESLOTS_AUTO_FREE             1


// CFG_NOISELEVEL_MODE

#define SET_NOISE_INSTR_FETCH           0
#define SET_NOISE_BY_ORDER              1
#define SET_NOISE_BY_APPL               2

/***************************************************************************\
                                    Bit Masks
\***************************************************************************/

// CRM_get_info

#define CRM_GET_TIMESLOTS               0
#define CRM_GET_VERSION                 1
#define CRM_GET_NOISE_LEVEL             2
#define CRM_GET_MOD_LEVEL               3
#define CRM_GET_SYS_VARS                4
#define CRM_GET_HALT_MODE               5
#define CRM_GET_IGNORE_WEAK_COLLISION   7
#define CRM_GET_ISO_15693               8
#define CRM_GET_RESP_OFFS_ISO           9
#define CRM_GET_ePC_TESTMODE            10
#define CRM_GET_ePC_CRC8_PRESET         11
#define CRM_GET_ePC_BYTES               12
#define CRM_GET_RX_DLY_OFFS             13

// CRM_config

#define CRM_CFG_MOD_EEPOT               0
#define CRM_CFG_PWR_EEPOT               1
#define CRM_CFG_EEPOT_NOSTORE           0x80

#define CRM_CFG_EEPOT_UP                0x80
#define CRM_CFG_EEPOT_DOWN              0x00


#define ICODEEVENT                      "EVENT_RESP_DONE"

/***************************************************************************\
                                Global Variables
\***************************************************************************/

#if 1
#ifdef CRM_SW32_DLL_EXPORT
 #define DLLEXP_IMP                      __declspec(dllexport)
#else
 #define DLLEXP_IMP                      __declspec(dllimport)
#endif

#else
#ifdef CRM_SW32_DLL_EXPORT
 #define DLLEXP_IMP                      __export
#else
 #define DLLEXP_IMP                      
#endif

#endif

//#define CALL_CONV                       __stdcall
#define CALL_CONV                         __cdecl

#ifdef __cplusplus
extern "C" {
#endif

/***************************************************************************\
                                Global Prototypes
\***************************************************************************/

// Serial Communication

int DLLEXP_IMP CALL_CONV CRM_init              (char  *ComStr, char *BaudStr);
int DLLEXP_IMP CALL_CONV CRM_exit              (void);

// Reader Configuration

void DLLEXP_IMP CALL_CONV CRM_config           (uchar  mode, uchar  confbyte);
void DLLEXP_IMP CALL_CONV CRM_get_info         (uchar  mode, uchar *info);

void DLLEXP_IMP CALL_CONV CRM_set_port         (uchar  portbyte);
void DLLEXP_IMP CALL_CONV CRM_get_port         (uchar *portbyte);

// Test Command

void DLLEXP_IMP CALL_CONV CRM_test             (uchar cmd,   uchar txlen,
																uchar rxlen, uchar *tx,
																uchar *rx);
// I-CODE1 Commands

void DLLEXP_IMP CALL_CONV CRM_anticoll_select  (uchar  hash, uchar  tse,
																uchar *resp);
void DLLEXP_IMP CALL_CONV CRM_read             (uchar  blnr, uchar  nobl,
																uchar *resp);
void DLLEXP_IMP CALL_CONV CRM_read_unselected  (uchar  hash, uchar  tse,
																uchar  blnr, uchar  nobl,
																uchar *resp);
void DLLEXP_IMP CALL_CONV CRM_write            (uchar  hash, uchar  blnr,
																uchar *data, uchar *timeslots,
																uchar *resp);
void DLLEXP_IMP CALL_CONV CRM_halt             (uchar  hash, uchar *timeslots,
																uchar *resp);
void DLLEXP_IMP CALL_CONV CRM_reset_QUIET_bit  (void);

void DLLEXP_IMP CALL_CONV CRM_eas              (uchar *resp);


// Global Variables for Serial Communication

uchar DLLEXP_IMP CALL_CONV CRM_get_eot         (void);
uchar DLLEXP_IMP CALL_CONV CRM_get_err         (void);
uchar DLLEXP_IMP CALL_CONV CRM_get_status      (void);
uchar DLLEXP_IMP CALL_CONV CRM_get_cmdvalid    (void);
uchar DLLEXP_IMP CALL_CONV CRM_get_txseq       (void);
uchar DLLEXP_IMP CALL_CONV CRM_get_rxseq       (void);
uint  DLLEXP_IMP CALL_CONV CRM_get_check       (void);
uint DLLEXP_IMP  CALL_CONV CRM_get_datalen     (void);
ulong DLLEXP_IMP CALL_CONV CRM_get_baud        (void);
uint DLLEXP_IMP  CALL_CONV CRM_get_timeslots   (void);
void DLLEXP_IMP  CALL_CONV CRM_get_error_text  (int    error_number,
																char **error_text);
ulong DLLEXP_IMP CALL_CONV CRM_get_win32_error (uchar *errtext, uint *length,
																uchar  showMessage);

// Extended Transparent Command

void DLLEXP_IMP CALL_CONV CRM_cmd              (uchar cmd, ushort txlen, ushort rxlen,
																uchar *tx, uchar *rx);
#ifdef __cplusplus
}
#endif

/***************************************************************************\
                                End of the File
\***************************************************************************/
