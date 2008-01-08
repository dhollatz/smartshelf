/***************************************************************************\
*                                                                           *
* File            SL2test.c   (C-Source)                                    *
*                                                                           *
* Project-Files   SL2test.c   (Source File: Main Module)                    *
*                 SL2ser.c    (Source File: Serial Library)                 *
*                                                                           *
* Project-Output  SL2test.exe (Executable Program File)                     *
*                                                                           *
* Compiler        Microsoft Visual C/C++ V6.0                               *
*                                                                           *
*****************************************************************************
*                                                                           *
* X1.00/2000-02-14  (C)  Philips Semiconductors Gratkorn GmbH               *
*                                                                           *
* Test Program for I·CODE Long Range Reader Module                          *
*                                                                           *
*---------------------------------------------------------------------------*
*                                                                           *
* X4.01/2000-03-09/WZ I·CODE1 and I·CODE2-PPP (CICODEV0)                    *
*     :                                                                     *
* X4.04/2001-02-13/WZ I·CODE SLI (CICODES)                                  *
*     :                                                                     *
* X4.10/2001-09-28/WZ I·CODE SLI V3 (CICODE_V3)                             *
*     :                                                                     *
* X5.00/2002-08-21/WZ I·CODE OTP (EPC): [a], [i], [k], [o], [p], [q], [s],  *
*                                       [t], [u], [v], [w], [x], [y], [z],  *
*                                       [C], [X], [Z], [0]                  *
*                                                                           *
* X5.01/2002-10-25/WZ I·CODE OTP (EPC): [J] Initialize Label                *
* X5.02/2002-12-17/WZ I·CODE OTP (EPC): [r] Change CRC8 Preset,             *
                                        default Hashvalue = 13              *
* X5.03/2003-04-25/WZ I·CODE OTP (EPC): [q], [s] and [x] removed,           *
*                                       COM-Port (default): COM2 --> COM1   *
* X5.04/2003-04-29/WZ I·CODE OTP (EPC): [l] Log-File (Filename/ON/OFF)      *
* X5.05/2003-07-11/WZ I·CODE OTP (EPC): [e] CHECK PASSWORD,                 *
*                                       [m] Set MUX,                        *
*                                       [x] Change MUX Value,               *
*                                       [A] Read ALL,                       *
*                                       [E] Change ECNTRL2,                 *
*                                       [H] IDDQ Test,                      *
*                                       [T] Change Type (EPC/UID),          *
*                                       [V] Change Hashvalue Mode at [a],   *
*                                       [Y] Write ALL                       *
* X5.06/2003-08-22/WZ I·CODE OTP (EPC): default Hashvalue -> LSB of EPC,    *
*                                       show number of labels at [a]        *
* X5.07/2003-11-10/WZ I·CODE OTP (EPC): new definition for 'UID CRC16' of   *
*                                       variant UID2 (UID: bytes 14-18)     *
* X5.08/2004-09-24/WZ I·CODE SLI:                                           *
*                     Add. RxDlyOffset (incl. config/get_info) T = 18.88 us *
*                     0: -4*T = -75.52 us, 1: -3*T = -56.64 us,             *
*                     2: -2*T = -37.76 us, 3: -1*T = -18.88 us,             *
*                     4: +-0 us (default), 5: +1*T = +18.88 us,             *
*                     6: +2*T = +37.76 us, ..., 255: +251*T = +4738.64 us   *
* X5.09/2004-11-11/BT I·CODE OTP (EPC): [s] Poll for I·CODE SLI and EPC     *
*                                                                           *
* X6.00/2005-01-17/WZ I·CODE SLI XS (cSLIXS0T1..5): [n]/[x]/[y] -> max. 128 *
* X6.00/2005-02-15/WZ I·CODE SLI XS: [n] -> max. 63     [x]/[y] -> max. 255 *
* X6.00/2005-03-03/WZ I·CODE SLI XS: [J] Initialize SLI XS (if [n] > 32)    *
* X6.00/2005-03-04/WZ I·CODE SLI XS: [n] -> max. 256                        *
* X6.00/2005-04-20/WZ I·CODE SLI XS: [m], [X] consider Option Flag,         *
*                                    [W] considers Write Block Address y    *
* X6.00/2005-06-24/WZ I·CODE SLI-S:  [g] Read ALL (ECNTRL2!),               *
*                                    default EE_control = 0x03 -> 0x00      *
* X6.00/2005-10-04/WZ [h]: 'Continuously' issue fixed                       *
* X6.01/2006-02-23/WZ I·CODE SLI-S (cSLIS0T7/T8/T8HC): [J] Initialize SLI-S *
* X6.02/2006-06-13/WZ I·CODE SLI-S (cSL2S53/54V0AT):                        *
*                     SLI-Mode: [g] Read ALL (ECNTRL2!)                     *
*                               [9] Change Nr. of 'Read ALL' Blocks         *
*                               [)] Get Random Number                       *
*                               [=] Set Password                            *
*                     EPC-Mode: [e] -> [j] CHECK PASSWORD                   *
*                               [e] Long Range Command: Read EAS            *
*                               [f] Long Range Command: Read UID            *
*                               [g] Long Range Command: Read EPC            *
* X6.03/2006-10-24/WZ I·CODE SLI-S (cSL2S53/54V0A/B): WriteData[] =         *
*                     {0x00, 0x08, 0xF4, 0xC8} => {0x00, 0x00, 0xF4, 0xC8}  *
*                                                                           *
\***************************************************************************/
#define  PRG_VER                "X6.03"
/***************************************************************************\
                                Includes
\***************************************************************************/
#include <stdio.h>
#include <conio.h>
#include <dos.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#pragma warning(disable:4115)   // avoids warning "'_RPC_ASYNC_STATE' : 
#include <windows.h>            // named type definition in parentheses" 
                                // (caused by incorrect <rpcasync.h>)
#include "SL2ser.h"

/***************************************************************************\
                                Common Defines
\***************************************************************************/
//#define USE_DEBUG_OUTPUT

#define BPL                     16    // Number of bytes per line to show
#define VRF_LSB                 5.1f
#define C590                    (8.0f/13.56f)    // 0.59
#define C1888                   (256.0f/13.56f)  // 18.88

/***************************************************************************\
                                Defines for I·CODE1 (Timeslot Exponents)
\***************************************************************************/
#define MAX_NoBl_ICODE1         16
#define MAX_BlAd_ICODE1         MAX_NoBl_ICODE1 - 1

#define TSE_1                   0
#define TSE_4                   1
#define TSE_8                   2
#define TSE_16                  3
#define TSE_32                  4
#define TSE_64                  5
#define TSE_128                 6
#define TSE_256                 7

/***************************************************************************\
                                Defines for I·CODE SLI-S/XS
\***************************************************************************/
#define MAX_NoBl_SLI            32    // I·CODE SLI Testmode: 32, Usermode: 28
#define MAX_BlAd_SLI            MAX_NoBl_SLI - 1

#define MAX_NoBl_SLI_XS0T       63    // I·CODE SLI-XS (CSLIXS0T1..T5)
#define MAX_NoBl_SLI_S          64    // I·CODE SLI-S  (cSL2S53/54V0)

#define MAX_NoBl_SLI_XS         256
#define MAX_BlAd_SLI_XS         255

#define MAX_BlAd                MAX_BlAd_SLI_XS
#define MAX_NoBl                MAX_NoBl_SLI_XS

#define LR_CMD                  0x40

/***************************************************************************\
                                Defines for I·CODE SLI
\***************************************************************************/
#define MAX_ISO_SLOTS           16    // Max. number of timeslots for Inventory

#define STAT                    1     // Number of bytes for 'Status'
#define FLAGS                   1     // Number of bytes for 'FLAGS'
#define CMD                     1     // Number of bytes for 'COMMAND_CODE'
#define UID                     8     // Number of bytes for 'UID'
#define DSFID                   1     // Number of bytes for 'DSFID'
#define INFO_FLAGS              1     // Number of bytes for 'INFO_FLAGS'
#define CRC16                   2     // Number of bytes for 'CRC16'
#define EAS                     32    // Number of EAS response bytes

#define CRC16_POLYNOM_LSB       0x8408  // x^16 + x^12 + x^5 + 1 (LSB first)
#define CRC16_PRESET_LSB        0xFFFF
#define CRC16_CHECK_LSB         0xF0B8

/***************************************************************************\
                                Defines for I·CODE OTP (EPC)
\***************************************************************************/
#define OTP_WRITE               0x01
#define OTP_DESTROY             0x02
#define OTP_BEGIN_ROUND         0x30
#define OTP_CHECK_PWD           0xE0
#define OTP_READ_ALL            0xE8
#define OTP_WRITE_ALL           0xEA
#define OTP_IDDQ                0xEB
#define OTP_SETMUX              0xEC

#define MAX_EPC_SLOTS           512     // Max. number of timeslots
#define TSE_512                 8

#define MAX_OTP_BYTES           32      // Max. number of memory bytes 
#define CRC8                    1       // Number of bytes for 'CRC8'

#define TYPE_EPC                0
#define TYPE_UID1               1
#define TYPE_UID2               2
#define TYPE_UID1A              3
#define MAX_EPC_TYPE            3
#define TYPE_EAS                4       // LR EAS
#define TYPE_UID                5       // LR UID
#define BYTES_EPC               12      // Number of bytes for Type 'EPC'
#define BYTES_UID1              12      // Number of bytes for Type 'UID1'
#define BYTES_UID2              19      // Number of bytes for Type 'UID2'
#define BYTES_UID2_UID          5       // Number of bytes for UID of Type 'UID2'
#define BYTES_UID1A             16      // Number of bytes for Type 'UID1A'

#define CRC8_POLYNOM_MSB        0x1D    // x^8 + x^4 + x^3 + x^2 + 1 (MSB first)
#define CRC8_PRESET_EPC         0xFF
#define CRC8_PRESET_UID1_1A     0xFE
#define CRC8_PRESET_UID2        0xFD

#define CRC16_POLYNOM_MSB       0x1021  // x^16 + x^12 + x^5 + 1 (MSB first)
#define CRC16_PRESET_MSB        0xFFFF
#define CRC16_CHECK_MSB         0x1D0F

/***************************************************************************\
                                Locals
\***************************************************************************/
static char helpstr[] =
"                                                                    \n"
"SL2test   "PRG_VER"   ("__DATE__")                                  \n"
"                                                                    \n"
"Usage:    SL2test [<ComPort> [<BaudRate>]]                          \n"
"                                                                    \n"
"ComPort:  COM1 (default), COM2, COM3, COM4                          \n"
"BaudRate: 9600, 14400, 19200, 28800, 38400, 57600, 115200 (default) \n"
"                                                                    \n"
"Example:  SL2test COM2 57600                                        \n"
"                                                                    \n";

static char menustr[] =
"SL2test: "PRG_VER" ("__DATE__") %4s %6lu Baud %3d TS              IúCODE1 Mode \n"
"Reader:  %s\n"
"\n"
"[a] .... ANTICOLLISION/SELECT             [A] .... Change Application ID       \n"
"[b] .... Pulse RF-Power OFF/ON (10 ms)    [B] .... Get VRF and Modulation Index\n"
"[c] .... Init Noise Thresholds            [C] .... Change Family Code          \n"
"[d] .... Set default Parameters                                                \n"
"[e] .... EAS (IúCODE1 Mode)                                                    \n"
"[f] .... Fast Mode ON/OFF                 [F] .... RF-Power OFF/ON             \n"
"                                          [G] .... Get Input Port (æC)         \n"
"[h] .... HALT                             [H] .... Set Halt Mode               \n"
"[i] .... Get Info: æC-Firmware and System [I] .... ISO/IEC 15693 Mode          \n"
"[j] .... Change FAST START Pulse Offset   [J] .... Change START Pulse Offset   \n"
"[k] .... Change FAST START Pulse Length   [K] .... Store current Modulation I. \n"
"[l] .... Change EAS Alarm Pulse Length    [L][+-*/]Change Modul. without store \n"
"[m] .... Change EAS Alarm Level           [M][+-]. Change Modulation Index     \n"
"[n][n+-] Change Number of Blocks (1..16)  [N] .... Set Noise Level             \n"
"[o] .... Change Pulse Offset              [O][+-]. Change RF-Pwr. without store\n"
"[p] .... Change Pulse Length              [P][+-]. Change RF-Power             \n"
"[q] .... RESET QUIET BIT                  [Q] .... Set Bit Threshold Factor    \n"
"[r] .... READ (Read Block, Blocks)        [R] .... Reset Noise Level Mode      \n"
"[s] .... Read SNRs from Labels            [S] .... Set Output Port (æC)        \n"
"[t][t+-] Change Timeslots (1, 4, 8..256)  [T] .... Clear Timeslot Table        \n"
"[u] .... UNSELECTED READ (Blocks)         [U] .... Store current RF-Power      \n"
"[v][v+-] Change Hashvalue (0..31)                                              \n"
"[w] .... WRITE (Write Data, Write Block)  [W] .... Ignore WEAK COLLISION       \n"
"[x][x+-] Change Read  Block (0..15)                                            \n"
"[y][y+-] Change Write Block (0..15)                                            \n"
"[z][z+-] Change Write Data                                                     \n"
"\n"
"[ ]or[,] Repeat last Command continuously                                      \n"
"\n"
"other .. This Help Screen                 [Esc] .. EXIT                        \n"
"\n";

static char iso_menustr[] =
"SL2test: "PRG_VER" ("__DATE__") %4s %6lu Baud %3d TS  ISO/IEC 15693 (SLI) Mode \n"
"Reader:  %s\n"
"\n"
"[a] .... INVENTORY                        [A] .... Addressed/Selected/AFI Mode \n"
"[b] .... Pulse RF-Power OFF/ON (10 ms)    [B] .... Get VRF and Modulation Index\n"
"[c] .... Init Noise Thresholds            [C] .... Read EPC                    \n"
"[d] .... Set Default Parameters           [D] .... DECODER Test                \n"
"[e] .... EAS (ISO/IEC 15693 Mode)         [E] .... Change ECNTRL2              \n"
"[f] .... Fast Mode (1 out of 4) OFF/ON    [F] .... RF-Power OFF/ON             \n"
"[g] .... Read ALL (ECNTRL2!)              [G] .... Get Input Port (æC)         \n"
"[h] .... Sub-Carrier/Data Rate            [H] .... LOCK Block y                \n"
"[i] .... Get Reader Information (æC-FW)   [I] .... IúCODE1 Mode                \n"
"[j] .... Get System Information (Label)   [J] .... Initialize Label (UID, EAS) \n"
"[k] .... Change Inventory Mask Length     [K] .... Store current Modulation I. \n"
"[l] .... Change Alarm Pulse Length        [L][+-*/]Change Modul. without store \n"
"[m] .... Set MUX                          [M][+-]. Change Modulation Index     \n"
"[n][n+-] Change Number of Blocks (1..256) [N] .... Set Noise Level             \n"
"[o] .... Change Pulse Offset              [O][+-]. Change RF-Pwr. without store\n"
"[p] .... Change Pulse Length              [P][+-]. Change RF-Power             \n"
"[q] .... Stay QUIET                       [Q] .... Set Bit Threshold Factor    \n"
"[r] .... READ Block x                     [R] .... Reset Noise Level Mode      \n"
"[s] .... SELECT                           [S] .... Set Output Port (æC)        \n"
"[t][t+-] Change Number of Timesl. (1..16) [T] .... Change RX Offset (0..4..255)\n"
"[u][u+-] Change UID/Mask Data             [U] .... Store current RF-Power      \n"
"[v] .... Vreg OFF (Short Voltage Regul.)  [V] .... Change AFI/DSFID/PwdID Value\n"
"[w] .... WRITE Block y                    [W] .... Write ALL (ECNTRL2!)        \n"
"[x][x+-] Change Read  Block (0..255)      [X] .... Change MUX Value            \n"
"[y][y+-] Change Write Block (0..255)      [Y] .... RESET to READY              \n"
"[z][z+-] Change Write Data                [Z] .... IDDQ Test (ECNTRL2!)        \n"
"[1] .... Set   EAS                        [!] .... Lock EAS                    \n"
"[2] .... Reset EAS                        [\"] .... Read EAS Data               \n"
"[3] .... Inventory Read                   [@] .... Fast Inventory Read         \n"
"[4] .... Read ALL (MARGIN Test)           [$] .... Read ALL (Iref/VCG Test)    \n"
"[5] .... Short Timeslot Mode OFF/ON       [%%] .... 100 %% Modulation ON/OFF    \n"
"[6] .... Write AFI                        [&] .... Lock AFI                    \n"
"[7] .... Write DSFID                      [{] .... Lock DSFID                  \n"
"[8] .... Get Block Security Status        [(] .... Option Flag ON/OFF          \n"
"[9][9+-] Change Nr. of 'Read ALL' Blocks  [)] .... Get Random Number           \n"
"                                          [=] .... Set   Password V (Value z)  \n"
"                                          [^P].... Write Password V (Value z)  \n"
"                                          [^L].... Lock  Password V (Value z)  \n"
"                                          [^O].... 64 Bit Password Protection  \n"
"\n"
"[ ]or[,] Repeat last Command continuously [0] .... IúCODE OTP (EPC) Menu       \n"
"\n"
"other .. This Help Screen                 [Esc] .. EXIT                        \n"
"\n";

static char epc_menustr[] =
"SL2test: "PRG_VER" ("__DATE__") %4s %6lu Baud %3d TS     IúCODE OTP (EPC) Mode \n"
"Reader:  %s\n"
"\n"
"[a] .... BEGIN ROUND                      [A] .... Read ALL                    \n"
"[b] .... Pulse RF-Power OFF/ON (10 ms)    [B] .... Get VRF and Modulation Index\n"
"[c] .... Init Noise Thresholds            [C][*+-] Calculate CRC16 of EPC (u/k)\n"
"[d] .... Set Default Parameters           [D] .... Read ALL (ECNTRL2!)         \n"
"[e] .... Long Range Command: Read EAS     [E] .... Change ECNTRL2              \n"
"[f] .... Long Range Command: Read UID     [F] .... RF-Power OFF/ON             \n"
"[g] .... Long Range Command: Read EPC     [G] .... Get Input Port (æC)         \n"
"                                          [H] .... IDDQ Test (y, z, EE. Ctrl.!)\n"
"[i] .... Get Reader Information (æC-FW)   [I] .... ISO/IEC 15693 Mode          \n"
"[j] .... CHECK PASSWORD (Password z)      [J] .... Initialize (EPC u, Passwd z)\n"
"[k][k+-] Change Mask Length (0..96..152)  [K] .... Store current Modulation I. \n"
"[l][l+-] Logging of [a] (Filename/ON/OFF) [L][+-*/]Change Modul. without store \n"
"[m] .... Set MUX (MUX Value x)            [M][+-]. Change Modulation Index     \n"
"                                          [N] .... Set Noise Level             \n"
"[o] .... Change Pulse Offset              [O][+-]. Change RF-Pwr. without store\n"
"[p] .... Change Pulse Length              [P][+-]. Change RF-Power             \n"
"                                          [Q] .... Set Bit Threshold Factor    \n"
"[r][r+-] Change CRC8 Preset               [R] .... Reset Noise Level Mode      \n"
"[s] .... Poll for IúCODE SLI/EPC Label    [S] .... Set Output Port (æC)        \n"
"[t][t+-] Change Timeslots (1, 4, 8..512)  [T][T+-] Change Type (EPC/UID1/2/1A) \n"
"[u][u+-] Change EPC/Mask Data Bytes       [U] .... Store current RF-Power      \n"
"[v][v+-] Change Hashvalue (0..13..17..20) [V] .... Change Hashvalue Mode at [a]\n"
"[w] .... WRITE Data Byte z to Address y   [W] .... Ignore WEAK COLLISION       \n"
"[x] .... Change MUX Value                 [X] .... Change EPC Testmode         \n"
"[y][y+-] Change Write Address (0..31)     [Y] .... Write ALL (y, z, EE. Ctrl.!)\n"
"[z][z+-] Change Write/Password Data Bytes [Z] .... DESTROY LABEL (EPC u, PW z) \n"
"\n"
"[ ]or[,] Repeat last Command continuously [0] .... IúCODE OTP (EPC) Menu OFF   \n"
"\n"
"other .. This Help Screen                 [Esc] .. EXIT                        \n"
"\n";

static uchar EAS_Resp[EAS] = {0x2F, 0xB3, 0x62, 0x70, 0xD5, 0xA7, 0x90, 0x7F,
                              0xE8, 0xB1, 0x80, 0x38, 0xD2, 0x81, 0x49, 0x76,
                              0x82, 0xDA, 0x9A, 0x86, 0x6F, 0xAF, 0x8B, 0xB0,
                              0xF1, 0x9C, 0xD1, 0x12, 0xA5, 0x72, 0x37, 0xEF};
static uchar Data[256 * 65];
static uchar Tag[256][8];

static char  Measure[80][80];

static ulong Cnt          = 1L;
static int   Quit         = FALSE;
static int   Cont         = FALSE;
static int   Console_x    = 80;
static int   Console_y    = 50;
static int   Lines        = 50;
static int   Logging      = FALSE;
FILE        *Lfp          = NULL;
static int   ePC_Testmode = 0;
static int   ePC_Type     = TYPE_EPC;
static int   ePC_Bytes[]  = {BYTES_EPC, BYTES_UID1, BYTES_UID2, BYTES_UID1A, EAS, UID};
static char *ePC_Types[]  = {"EPC", "UID1", "UID2", "UID1A", "EAS", "UID"};

static uchar LR_Slots[]   = {0x10, 0x20, 0x40, 0x80, 0x00, 0x01, 0x02, 0x04, 0x08, 0};

HANDLE ICODE_Event;


static void HL_anticoll_select  (uchar hash, uchar tse,  uchar *resp);
static void HL_write            (uchar hash, uchar blnr, uchar *data,
                                 uchar *tsl, uchar *resp);
static void HL_read_unselected  (uchar hash, uchar tse,   uchar blnr,
                                 uchar nobl, uchar *resp);
static int  HL_ePC_cmd          (int cmd_code, int parlen, int rxlen,
                                 uchar *epc_mask, uchar *par, uchar *rxdata);
static int  HL_ISO_cmd          (int cmd_code, int flags, int parlen, int rxlen,
                                 uchar *uid, uchar *par, uchar *rxdata);
static int  HL_ISO_inventory    (int inventory_cmd, int flags, int nr_of_slots,
                                 int afi, int mask_len, int blk_ad, int nr_of_blocks,
                                 uchar *mask, uchar *rxdata);
static void show_inventory_data (int cmd, int ts, int bytes_per_ts, uchar *data);
static void show_read_data      (int ts,  int nobl, uchar *data);
static void show_data           (int len, uchar *data);
static int  get_status          (int mode, char *cmd);
static void wait_Eot            (int showlifesign);
static int  _clrscr              (int *dwSize_x, int *dwSize_y);
static int  _gotoxy              (int x,  int y);

/***************************************************************************/

int main(int argc, char **argv)
{
  int   i, j, k, max;
  char  *ErrTxt;

  char  ComX[]   = "COM1";
  char  Baud[]   = "115200";

  char  Ch       = 0;
  char  Oldch    = 0;

  uchar Outbyte  = 1;

  uchar MaxEas   = 0;
  uchar EasLevel = 64;

  WORD  MinMinNL = 4095;
  WORD  MaxMinNL = 0;
  WORD  MinAvgNL = 4095;
  WORD  MaxAvgNL = 0;
  WORD  MinMaxNL = 4095;
  WORD  MaxMaxNL = 0;

  uchar WrAd = 0;
  uchar WriteData[] = {0x00, 0x00, 0xF4, 0xC8};
  uchar AllOnes[]   = {0xFF, 0xFF, 0xFF, 0xFF};
  uchar AllZeros[]  = {0x00, 0x00, 0x00, 0x00};
  uchar EnableEAS[] = {0x80, 0x01, 0xAF, 0xDF};

  uchar UID_Mask[]  = {0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x04, 0xE0};
  uchar UID_Mask2[] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x04, 0xE0};

  uchar RdAd = 0;
  uchar NoBl       = MAX_NoBl_ICODE1;
  int   NoBl_XS    = MAX_NoBl_SLI_S;
  int   NoBl_RdAll = MAX_NoBl_SLI_S;

  uchar Hash = 0;

  int   RF_off   = FALSE;
  int   FastMode = FALSE;

  char  InputStr[96];
  int   In[32];

  float Vrfmax, Vrfmin, Vrfoff;

  char  CRM_Reader_Version[256];

  uchar Tse         = TSE_1;
  uchar tmpTse      = TSE_1;

  int   ISO_reader  = FALSE;
  int   ISO_mode    = FALSE;
  int   Mod100      = FALSE;
  int   ShortTsMode = FALSE;

  int   Slots       = 1;      // Inventory Timeslots
  int   Masklen     = 0;      // Inventory Mask Length

  uchar DataRate    = 0x02;   // Single Sub Carrier/High Data Rate (26.5 kBaud)
  uchar AdSelAFI    = 0x00;   // Non-addressed Mode (AFI at Inventory is OFF)
  uchar AFI_DSFID   = 0x00;   // AFI/DSFID Value
  uchar Option      = 0x00;   // Option Flag

  uchar MUX_value   = 0x82;   // SLI SETMUX: Test Out = Demod10
  uchar EE_control  = 0x00;   // Enable all rows = 0x03

  int   ePC_reader  = FALSE;
  int   ePC_mode    = FALSE;
  uint  crc16       = 0;
  int   parlen, rxlen, msklen, bitmask, start;
  uchar ePC_Mask[]  = {0x12, 0x11, 0x10, 0x09, 0x08, 0x07, 0x06, 0x05, 
							  0x04, 0x03, 0x02, 0x01, 0x13, 0x14, 0x15, 0x16,
                       0x17, 0x18, 0x19,                          // UID/EPC Bytes
                       0x00, 0x00,                                // 2 Bytes CRC16
                       0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}; // Dummy
  uchar UID2_Data[] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                       0x00, 0x00, 0x00, 0x00, 0x00, 0x00,        // User Data
                       0x7B, 0x06,                                // UD CRC16
                       0x40, 0x00, 0x00, 0x00, 0x00};             // UID

  uchar  RndNr[2];            // Random Number
  ushort RndNrMax = 0x0000;
  ushort RndNrMin = 0xFFFF;
  ulong  RndNrCnt = 1L;
  double RndNrSum = 0.0;
  double RndNrAvg;

  char  DateBuf[20];
  char  TimeBuf[20];
  char  LogFile[80] = "SL2test.log";

  uchar ePC_MUX_value   = 0x04; // OTP SETMUX: Test Out = Ana_Demod
  uchar ePC_CRC8_preset = 0;
  int   ePC_type_tmp    = 0;
  int   ePC_Hashmode    = 0;


  ICODE_Event = OpenEvent(SYNCHRONIZE, FALSE, ICODEEVENT);

  printf(helpstr);

  if (argc > 1)     strncpy(ComX, argv[1], 4);
  if (argc > 2)     strncpy(Baud, argv[2], 6);

  printf("Used Parameters:  %s %s Baud\n", ComX, Baud);

  i = CRM_init(ComX, Baud);
  if (i == OK)
  {
    CRM_get_info(CRM_GET_VERSION, (uchar *)CRM_Reader_Version);
    wait_Eot(0);
    if (get_status(-1, "") != OK)
    {
      printf("\n\nPress any key ..."); getch(); printf("\n");
      CRM_exit();
      exit(1);
    }

    _clrscr(&Console_x, &Console_y);
    printf("\nConsole: x = %d columns, y = %d rows (buffer size)\n\n",
                           Console_x,      Console_y);
    if (Console_x <= 80)  Lines = Console_x/2 + Console_x/8;
    else                  Lines = Console_x/4 + Console_x/8 + Console_x/35;

    Slots = CRM_get_timeslots();
    if      (Slots <=   1)    Tse = TSE_1;
    else if (Slots <=   4)    Tse = TSE_4;
    else if (Slots <=   8)    Tse = TSE_8;  
    else if (Slots <=  16)    Tse = TSE_16; 
    else if (Slots <=  32)    Tse = TSE_32; 
    else if (Slots <=  64)    Tse = TSE_64; 
    else if (Slots <= 128)    Tse = TSE_128;
    else if (Slots <= 256)    Tse = TSE_256;
    else                      Tse = TSE_512;

    if (strncmp(&CRM_Reader_Version[24], "ISO", 3) == OK)
    {
      ISO_reader = TRUE;
      CRM_get_info(CRM_GET_ISO_15693, Data);
      wait_Eot(0);
      if (get_status(0, "GI8") == OK)
      {
        ISO_mode    = (Data[0] & 0x01)? TRUE : FALSE;
        Mod100      = (Data[0] & 0x02)? TRUE : FALSE;
        ShortTsMode = (Data[0] & 0x04)? TRUE : FALSE;

        CRM_get_info(CRM_GET_ePC_TESTMODE, Data);
        wait_Eot(0);
        if (get_status(-2, "G10") == OK)
        {
          ePC_reader = TRUE;
          ePC_Testmode = Data[0];

          CRM_get_info(CRM_GET_ePC_CRC8_PRESET, Data);
          wait_Eot(0);
          if (get_status(-2, "G11") == OK)
          {
            ePC_CRC8_preset = Data[0];
          }
          CRM_get_info(CRM_GET_ePC_BYTES, Data);
          wait_Eot(0);
          if (get_status(-2, "G12") == OK)
          {
            ePC_Hashmode = 1;
            if      (Data[0] == BYTES_UID2)       ePC_Type = TYPE_UID2;
            else if (Data[0] == BYTES_UID1A)      ePC_Type = TYPE_UID1A;
            else
            {
              ePC_Hashmode = 0;                   ePC_Type = TYPE_EPC;
            }
          }
        }
        if (ISO_mode)
        {
          if (ePC_reader && Slots > MAX_ISO_SLOTS)
          {
            ePC_mode = TRUE;
            Hash = (uchar)(ePC_Bytes[ePC_Type] - 1);
            printf(epc_menustr, ComX, CRM_get_baud(), Slots, CRM_Reader_Version);
          }
          else
          {
            printf(iso_menustr, ComX, CRM_get_baud(), Slots, CRM_Reader_Version);
          }
        }
        else
        {
          printf(menustr, ComX, CRM_get_baud(), Slots, CRM_Reader_Version);
          if (Tse > TSE_256)
          {
            Tse = TSE_256;
          }
        }
      }
    }
    else
    {
      printf(menustr, ComX, CRM_get_baud(), Slots, CRM_Reader_Version);
    }
    tmpTse = Tse;
  }
  else
  {
    CRM_get_error_text(i, &ErrTxt);
    printf("\n%s!\n", ErrTxt);
    printf("\n\nPress any key ..."); getch(); printf("\n");
    CRM_exit();
    exit(1);
  }

  for (i = 0; i < 80; i++)
  {
    for (j = 0; j < 79; j++)
    {
      Measure[i][j] = (char)((j < i)? 'Û' : ' ');
    }
    Measure[i][79] = '\0';
  }

#pragma warning(disable:4127) // avoids warning "conditional expression is constant"

  while (TRUE)
  {
    if (Cont)
    {
      if (Quit || CRM_get_err() != OK)
      {
        Cont = FALSE;
        continue;
      }
      else
      {
        _gotoxy(1, 1);  printf("%lu ...\n", Cnt++);
      }
    }
    else
    {
      if (Console_x <= 80)  printf("\r%79s",  " ");
      else                  printf("\r%249s", " ");
      printf("\rCmd %lu> ", Cnt++);
      if (Ch != '+' && Ch != '-' && Ch != '/' && Ch != '*')  Oldch = Ch;
      i = getch();
      if (i == 0x00 || i == 0xE0) // F1 ... F12
      {
        getch();
        Ch = 0xFF;
      }
      else
      {
        Ch = (char)i;
      }
      printf("%c\n", Ch);
      Quit = FALSE;
    }

////          Esc
    if (Ch == 0x1B)
    {
      break;                            //  Break main loop (=> exit program)
    }

////
    if (Ch == '1' && ISO_mode && !ePC_mode)
    {
      printf("\nSet EAS ...\n\n");

      HL_ISO_cmd(0xA2, DataRate | AdSelAFI | Option, 0, FLAGS, UID_Mask, NULL, Data);
      continue;
    }

////
    if (Ch == '!' && ISO_mode && !ePC_mode)
    {
      printf("\nLock EAS ...\n\n");

      HL_ISO_cmd(0xA4, DataRate | AdSelAFI | Option, 0, FLAGS, UID_Mask, NULL, Data);
      continue;
    }

////
    if (Ch == '2' && ISO_mode && !ePC_mode)
    {
      printf("\nReset EAS ...\n\n");

      HL_ISO_cmd(0xA3, DataRate | AdSelAFI | Option, 0, FLAGS, UID_Mask, NULL, Data);
      continue;
    }

////
    if (Ch == '"' && ISO_mode && !ePC_mode)
    {
      printf("\nRead EAS (EAS ID z @ Option Flag is ON) ...\n\n");

      if (Option)
      {
        Data[0] = WriteData[0];   // EAS ID Mask Length
        Data[1] = WriteData[1];   // EAS ID Value (LSB)
        Data[2] = WriteData[2];   // EAS ID Value (MSB)
        if      (Data[0] == 0)  i = 1;
        else if (Data[0] <= 8)  i = 2;
        else                    i = 3;
        j = (Data[0] == 0)? FLAGS + 2 : FLAGS + EAS;
      }
      else
      {
        i = 0;
        j = FLAGS + EAS;
      }
      j = HL_ISO_cmd(0xA5, DataRate | AdSelAFI | Option, i, j, UID_Mask, Data, Data);
      if (j <= FLAGS) continue;

      if (i == 1)
      {
        printf("\nEAS ID: %02X%02X hex\n\n", Data[FLAGS + STAT + 1], Data[FLAGS + STAT]);
      }
      else
      {
        printf("\n");
        for (i = 0; i < (j - FLAGS) && i < EAS; i++)
        {
          if (Data[i + STAT + FLAGS] != EAS_Resp[i]) break;
        }
        printf("Matching EAS Response Bytes: %3d -> %5.1f %%   ", i, i * 100.0/EAS);
        if (i >= EasLevel/8)
        {
          printf("%24s\r\n", "===>  EAS  Alarm  <===");
          Beep(8000, 1);
        }
        else
        {
          printf("%24s\n", "No Label or EAS disabled");
        }
      }
      continue;
    }

////
    if (Ch == '3' && ISO_mode && !ePC_mode)
    {
      printf("\nInventory Read ...\n\n");

      if (Slots > MAX_ISO_SLOTS)  Slots = MAX_ISO_SLOTS;

      HL_ISO_inventory(0xB0, DataRate | AdSelAFI | Option, Slots,
                             AFI_DSFID, Masklen, RdAd, NoBl, UID_Mask, Data);
      continue;
    }

////
    if (Ch == '@' && ISO_mode && !ePC_mode)
    {
      printf("\nFast Inventory Read ...\n\n");

      if (Slots > MAX_ISO_SLOTS)  Slots = MAX_ISO_SLOTS;

      HL_ISO_inventory(0xB1, DataRate | AdSelAFI | Option, Slots,
                             AFI_DSFID, Masklen, RdAd, NoBl, UID_Mask, Data);
      continue;
    }

////
    if (Ch == '4' && ISO_mode && !ePC_mode)
    {
      printf("\nRead ALL (Margin Test, ECNTRL2: 18 hex, Address: %d, NoBl_RdALL: %d) ...\n\n",
                                                                 RdAd, NoBl_RdAll);
      Data[0] = RdAd; // Addr
      Data[1] = 0x18; // ECNTRL2: Margin (Erased/Written) Test

      HL_ISO_cmd(0xF0, DataRate | AdSelAFI, 2, (FLAGS + 4 * NoBl_RdAll),
                       UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == '$' && ISO_mode && !ePC_mode)
    {
      printf("\nRead ALL (Iref/VCG Test, ECNTRL2: 08 hex, Address: %d, NoBl_RdALL: %d) ...\n\n",
                                                                   RdAd, NoBl_RdAll);
      Data[0] = RdAd; // Addr
      Data[1] = 0x08; // ECNTRL2: Iref/VCG Test

      HL_ISO_cmd(0xF0, DataRate | AdSelAFI, 2, (FLAGS + 4 * NoBl_RdAll),
                       UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == '5' && ISO_mode && !ePC_mode)
    {
      Cont = FALSE;
      CRM_get_info(CRM_GET_ISO_15693, Data);
      wait_Eot(0);
      if (get_status(0, "GI8") != OK)  continue;

      if (Oldch != '5')
      {
        j = (Data[0] & 0x04)? TRUE : FALSE;
      }
      else
      {
        j = (Data[0] & 0x04)? FALSE : TRUE;
        i = (Data[0] & 0xFB);
        if (j)                                   i |= 0x04;
        if (Slots > 1 && Slots < MAX_ISO_SLOTS)  i |= (MAX_ISO_SLOTS - Slots) << 4;

        CRM_config(CFG_ISO_15693, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C26") != OK)  continue;
      }
      ShortTsMode = j;
      printf("Short Timeslot Mode is %s!\n", (ShortTsMode)? "ON" : "OFF");
      continue;
    }

////
    if (Ch == '%' && ISO_mode && !ePC_mode)
    {
      Cont = FALSE;
      CRM_get_info(CRM_GET_ISO_15693, Data);
      wait_Eot(0);
      if (get_status(0, "GI8") != OK)  continue;

      if (Oldch != '%')
      {
        j = (Data[0] & 0x02)? TRUE : FALSE;
      }
      else
      {
        j = (Data[0] & 0x02)? FALSE : TRUE;
        i = (Data[0] & 0xFD);
        if (j)                                   i |= 0x02;
        if (Slots > 1 && Slots < MAX_ISO_SLOTS)  i |= (MAX_ISO_SLOTS - Slots) << 4;

        CRM_config(CFG_ISO_15693, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C26") != OK)  continue;
      }
      Mod100 = j;
      printf("100 %% Modulation is %s!\n", (Mod100)? "ON" : "OFF");
      continue;
    }

////
    if (Ch == '6' && ISO_mode && !ePC_mode)
    {
      printf("\nWrite AFI ...\n\n");

      Data[0] = AFI_DSFID;

      HL_ISO_cmd(0x27, DataRate | AdSelAFI | Option, 1, FLAGS, UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == '&' && ISO_mode && !ePC_mode)
    {
      printf("\nLock AFI ...\n\n");

      HL_ISO_cmd(0x28, DataRate | AdSelAFI | Option, 0, FLAGS, UID_Mask, NULL, Data);
      continue;
    }

////
    if (Ch == '7' && ISO_mode && !ePC_mode)
    {
      printf("\nWrite DSFID ...\n\n");

      Data[0] = AFI_DSFID;

      HL_ISO_cmd(0x29, DataRate | AdSelAFI | Option, 1, FLAGS, UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == '{' && ISO_mode && !ePC_mode)
    {
      printf("\nLock DSFID ...\n\n");

      HL_ISO_cmd(0x2A, DataRate | AdSelAFI | Option, 0, FLAGS, UID_Mask, NULL, Data);
      continue;
    }

////
    if (Ch == '8' && ISO_mode && !ePC_mode)
    {
      printf("\nGet Multiple Block Security Status ...\n\n");

      Data[0] = RdAd;
      Data[1] = (uchar)(NoBl - 1);

      HL_ISO_cmd(0x2C, DataRate | AdSelAFI, 2, FLAGS + NoBl, UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == '(' && ISO_mode && !ePC_mode)
    {
      Cont = FALSE;
      if (Oldch == '(')
      {
        if (Option == 0x00)  Option = 0x40;
        else                 Option = 0x00;
      }
      printf("\nOption Flag is %s!\n\n", (Option)? "ON" : "OFF");
      continue;
    }

////
    if ((Ch == '9' || Oldch == '9' && (Ch == '+' || Ch == '-')) && ISO_mode && !ePC_mode)
    {
      max = (ISO_mode)? MAX_NoBl : MAX_NoBl_ICODE1;
      Cont = FALSE;
      if (Oldch == '9' && Ch != '+' && Ch != '-')
      {
        printf("\nInput Value (1..%d) for Nr. of 'Read ALL' Blocks (%d): ", max, NoBl_RdAll);
        gets(InputStr);
        if (sscanf(InputStr, "%3d", &i) == EOF)  continue;

        if      (i > max)     NoBl_RdAll = max;
        else if (i < 1)       NoBl_RdAll = 1;
        else                  NoBl_RdAll = i;
      }
      else if (Ch == '+')
      {
        if (NoBl_RdAll < max) NoBl_RdAll *= 2;
        else                  NoBl_RdAll  = 1;
      }
      else if (Ch == '-')
      {
        if (NoBl_RdAll > 1)   NoBl_RdAll /= 2;
        else                  NoBl_RdAll  = max;
      }
      printf("Number of 'Read ALL' Blocks: NoBl_RdAll = %d\n", NoBl_RdAll);
      continue;
    }

////
    if (Ch == ')' && ISO_mode && !ePC_mode)
    {
      printf("\nGet Random Number ...\n\n");

      i = HL_ISO_cmd(0xB2, DataRate | AdSelAFI, 0, FLAGS + 2, UID_Mask2, Data, Data);
      if (i == FLAGS + 2)
      {
        RndNr[0] = Data[STAT + FLAGS];
        RndNr[1] = Data[STAT + FLAGS + 1];
        if (*(ushort *)RndNr > RndNrMax)  RndNrMax = *(ushort *)RndNr;
        if (*(ushort *)RndNr < RndNrMin)  RndNrMin = *(ushort *)RndNr;
        RndNrSum += (double)(*(ushort *)RndNr);
        RndNrAvg =  (double)(RndNrSum/RndNrCnt);
        printf("\n%lu. Random Number: %5d (Average: %9.3lf, Max: %lu, Min: %lu)     \n\n",
                  RndNrCnt, *(ushort *)RndNr, RndNrAvg, RndNrMax, RndNrMin);
        RndNrCnt++;
      }
      continue;
    }

////
    if (Ch == '0' && ISO_mode)
    {
      Cont = FALSE;
      _clrscr(&Console_x, &Console_y);
      if (ePC_reader && !ePC_mode)
      {
        ePC_mode = TRUE;
        Hash = (uchar)(ePC_Bytes[ePC_Type] - 1);
        printf(epc_menustr, ComX, CRM_get_baud(), (tmpTse == TSE_1)? 1 : 2 << tmpTse,
               CRM_Reader_Version);
      }
      else
      {
        ePC_mode = FALSE;
        printf(iso_menustr, ComX, CRM_get_baud(), Slots, CRM_Reader_Version);
      }
      continue;
    }

////
    if (Ch == '=' && ISO_mode && !ePC_mode)
    {
      printf("\nSet Password: Identifier V, Value z (choose A: addr./selected) ...\n\n");

      Data[0] = AFI_DSFID;
      Data[1] = WriteData[0] ^ RndNr[0];
      Data[2] = WriteData[1] ^ RndNr[1];
      Data[3] = WriteData[2] ^ RndNr[0];
      Data[4] = WriteData[3] ^ RndNr[1];

      HL_ISO_cmd(0xB3, DataRate | AdSelAFI, 5, FLAGS, UID_Mask2, Data, Data);
      continue;
    }

////       Ctrl-P
    if (Ch == 16 && ISO_mode && !ePC_mode)
    {
      printf("\nWrite Password: Identifier V, Value z (choose A: addr./selected) ...\n\n");

      Data[0] = AFI_DSFID;
      Data[1] = WriteData[0];
      Data[2] = WriteData[1];
      Data[3] = WriteData[2];
      Data[4] = WriteData[3];

      HL_ISO_cmd(0xB4, DataRate | AdSelAFI, 5, FLAGS, UID_Mask2, Data, Data);
      continue;
    }

////       Ctrl-L
    if (Ch == 12 && ISO_mode && !ePC_mode)
    {
      printf("\nLock Password: Identifier V (choose A: addr./selected) ...\n\n");

      Data[0] = AFI_DSFID;

      HL_ISO_cmd(0xB5, DataRate | AdSelAFI, 1, FLAGS, UID_Mask2, Data, Data);
      continue;
    }

////       Ctrl-O
    if (Ch == 15 && ISO_mode && !ePC_mode)
    {
      printf("\nSet 64 Bit Password Protection ...\n\n");

      HL_ISO_cmd(0xBB, DataRate | AdSelAFI, 0, FLAGS, UID_Mask2, Data, Data);
      continue;
    }

////
    if (Ch == 'a')
    {
      if (ISO_mode)
      {
        if (ePC_mode)
        {
          printf("BEGIN ROUND "
                 "(using %d most significant Bits of EPC-Mask u) ...\n", Masklen);

          Data[0] = (uchar)Masklen;
          Data[1] = (uchar)((tmpTse)? (1 << tmpTse) - 1 : 0);
          Data[2] = Hash;
          parlen = (ePC_Hashmode)? 2 : 3;
          rxlen = ePC_Bytes[ePC_Type] - Masklen/8 + CRC16;
          if (rxlen < CRC16)
          {
            printf("\nMasklen (%d) larger than number of EPC bits (%d: %s)!\n",
                             Masklen, 8 * ePC_Bytes[ePC_Type], ePC_Types[ePC_Type]);
            printf("(Change Masklen [k] or IúCODE OTP Type [T])!\n\n");
            continue;
          }
          HL_ePC_cmd(OTP_BEGIN_ROUND, parlen, rxlen, ePC_Mask, Data, Data);
          continue;
        }
        printf("\nInventory ...\n\n");

        if (Slots > MAX_ISO_SLOTS)  Slots = MAX_ISO_SLOTS;

        HL_ISO_inventory(0x01, DataRate | AdSelAFI | Option, Slots,
                               AFI_DSFID, Masklen, 0, 0, UID_Mask, Data);
      }
      else
      {
        printf("\nAnticollision/Select ... \n");

        if (tmpTse > TSE_256)  tmpTse = TSE_256;
        Tse = tmpTse;

        HL_anticoll_select(Hash, Tse, Data);
      }
      continue;
    }

////
    if (Ch == 'A')
    {
      if (ISO_mode)
      {
        if (ePC_mode)
        {
          printf("\nRead ALL ...\n\n");

          Data[0] = 0; // Dummy Address
          Data[1] = 0; // ECNTRL2
          parlen = 2;
          rxlen  = MAX_OTP_BYTES + CRC8;

          HL_ePC_cmd(OTP_READ_ALL, parlen, rxlen, ePC_Mask, Data, Data);
          continue;
        }
        Cont = FALSE;
        if (Oldch == 'A')
        {
          AdSelAFI += 0x10;
          if (AdSelAFI == 0x30) AdSelAFI = 0x00;
        }
        switch (AdSelAFI)
        {
          case 0x00:
            printf("\nNon-addressed Mode (AFI at Inventory is OFF)!\n\n");
            break;
          case 0x10:
            printf("\nSelected  Mode     (AFI at Inventory is ON)!\n\n");
            break;
          case 0x20:
            printf("\nAddressed Mode     (AFI at Inventory is OFF)!\n\n");
            break;
        }
      }
      else
      {
        Cont = FALSE;
        CRM_get_info(CRM_GET_SYS_VARS, Data);
        wait_Eot(0);
        if (get_status(0, "GI4") != OK)  continue;

        printf("\nInput Value (0..FF hex) for Application ID (%02X hex): ", Data[2]);
        gets(InputStr);
        if (sscanf(InputStr, "%2x", &i) == EOF)  continue;

        CRM_config(CFG_APPLICATION_ID, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C10") != OK)  continue;

        printf("New Application ID: %02X hex\n", i);
      }
      continue;
    }

////
    if (Ch == 'b')
    {
      CRM_config(CFG_RF_PAUSE_INIT, 0);
      wait_Eot(0);
      if (get_status(0, "CF0") != OK)  continue;

      if (!ISO_mode)
      {
        for (i = 0; i < (int)CRM_get_timeslots(); i++)
        {
          memcpy(Tag[i], "\0\0\0\0\0\0\0\0", 8);
        }
      }
      CRM_get_info(CRM_GET_NOISE_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI2") != OK)  continue;

      printf("Noise Levels:   Min. %4u mV\n", *((WORD *)&Data[0]));
      printf("                Avg. %4u mV\n", *((WORD *)&Data[2]));
      printf("                Max. %4u mV\n", *((WORD *)&Data[4]));
      printf("%79s\n", Measure[*((WORD *)&Data[4])/52]);
      printf("Bit Threshold Factor:%4u\n", *((WORD *)&Data[6]));
      printf("Noise Level Mode:    %4u\n", Data[8]);
      continue;
    }

////
    if (Ch == 'B')
    {
      Sleep(150);
      CRM_get_info(CRM_GET_MOD_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI3") != OK)  continue;

      Vrfmax = (float)(*((WORD *)&Data[0])) * VRF_LSB;
      Vrfmin = (float)(*((WORD *)&Data[2])) * VRF_LSB;

      CRM_config(CFG_RF_OFF_ON, 1);
      wait_Eot(0);
      if (get_status(0, "CF7") != OK)  continue;

      Sleep(1);
      CRM_get_info(CRM_GET_MOD_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI3") != OK)  continue;

      Vrfoff = (float)(*((WORD *)&Data[0])) * VRF_LSB;
      printf("RF-Level:         %4.0f mV    \n", Vrfmax);
      printf("Modulated VRF:    %4.0f mV    \n", Vrfmin);
      printf("RF switched off:  %4.0f mV    \n", Vrfoff);

      Vrfmax -= Vrfoff;
      Vrfmin -= Vrfoff;

      if ((Vrfmax + Vrfmin) != 0)
      {
        printf("Modulation Index: %4.1f %%   \n", (Vrfmax - Vrfmin)/
               (Vrfmax + Vrfmin) * 85.0);
      }
      continue;
    }

////
    if (Ch == 'c')
    {
      CRM_config(CFG_INIT, 0);
      wait_Eot(0);
      if (get_status(0, "CF1") != OK)  continue;

      CRM_get_info(CRM_GET_NOISE_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI2") != OK)  continue;

      if (*((WORD *)&Data[0]) < MinMinNL)
      {
        MinMinNL = *((WORD *)&Data[0]);
      }
      if (*((WORD *)&Data[0]) > MaxMinNL)
      {
        MaxMinNL = *((WORD *)&Data[0]);
      }
      if (*((WORD *)&Data[2]) < MinAvgNL)
      {
        MinAvgNL = *((WORD *)&Data[2]);
      }
      if (*((WORD *)&Data[2]) > MaxAvgNL)
      {
        MaxAvgNL = *((WORD *)&Data[2]);
      }
      if (*((WORD *)&Data[4]) < MinMaxNL)
      {
        MinMaxNL = *((WORD *)&Data[4]);
      }
      if (*((WORD *)&Data[4]) > MaxMaxNL)
      {
        MaxMaxNL = *((WORD *)&Data[4]);
      }
      printf("Noise Levels:   Minimum %4u mV (min. %4u mV, max. %4u mV)\n",
             *((WORD *)&Data[0]), MinMinNL, MaxMinNL);
      printf("                Average %4u mV (min. %4u mV, max. %4u mV)\n",
             *((WORD *)&Data[2]), MinAvgNL, MaxAvgNL);
      printf("                Maximum %4u mV (min. %4u mV, max. %4u mV)\n",
             *((WORD *)&Data[4]), MinMaxNL, MaxMaxNL);
      printf("%79s\n", Measure[*((WORD *)&Data[4])/52]);
      printf("Bit Threshold Factor:%4u\n", *((WORD *)&Data[6]));
      printf("Noise Level Mode:    %4u\n", Data[8]);
      continue;
    }

////
    if (Ch == 'C' || (Oldch == 'C' && (Ch == '+' || Ch == '-' || Ch == '*')))
    {
      if (ISO_mode && ePC_mode)
      {
        if      (Ch == '+')   (*(uint *)&ePC_Mask[ePC_Bytes[ePC_Type] - 2])++;
        else if (Ch == '-')   (*(uint *)&ePC_Mask[ePC_Bytes[ePC_Type] - 2])--;
        else if (Ch == '*')
        {
          (*(uint *)&ePC_Mask[ePC_Bytes[ePC_Type] - 2])++;
          if (!Cont)
          {
            _clrscr(&Console_x, &Console_y);
            printf("\n");
            Cont = TRUE;
          }
        }
        msklen = (Masklen == 0)? 8 * ePC_Bytes[ePC_Type] : Masklen;
        printf("\nCalculate CRC16 of EPC-Mask u "
               "by using Mask Length k = %d bit(s) ...\n", msklen);
        printf("\n(Change Mask Length by pressing [k][k+-]!)\n\n");
        // LSB first
        printf("\nEPC-Mask (LSB1st): ");
        j = (msklen + 7)/8;
        for (i = 0; i < j; i++)
        {
          if (i == (j - 1) && msklen % 8)
          {
            printf("%02X ", ePC_Mask[i] & (uchar)(0xFF >> (8 - (msklen % 8))));
          }
          else
          {
            printf("%02X ", ePC_Mask[i]);
          }
        }
        printf("hex\n");

        bitmask = 0x01;
        crc16 = CRC16_PRESET_LSB;
        j = 0;
        for (i = 0; i < msklen && i < (8 * ePC_Bytes[ePC_Type]); i++)
        {
          if (ePC_Mask[j] & bitmask)
          {
            crc16 = (crc16 & 0x0001)? crc16 >> 1 : (crc16 >> 1) ^ CRC16_POLYNOM_LSB;
          }
          else
          {
            crc16 = (crc16 & 0x0001)? (crc16 >> 1) ^ CRC16_POLYNOM_LSB : crc16 >> 1;
          }
          if (bitmask != 0x80)
          {
            bitmask <<= 1;
          }
          else
          {
            bitmask = 0x01;
            j++;
          }
        }
        printf("\nCRC16:    %02X %02X hex (LSB MSB)     LSB-first algorithm!",
               (uchar)(crc16), (uchar)(crc16 >> 8));
        printf("\nCRC16inv: %02X %02X hex (LSB MSB)"
               " ==> inverted (used for IúCODE SLI)\n\n",
               (uchar)(~crc16), (uchar)(~crc16 >> 8));
        // MSB first
        printf("\nEPC-Mask (MSB1st): ");
        j = ePC_Bytes[ePC_Type] - (msklen + 7)/8;
        for (i = ePC_Bytes[ePC_Type] - 1; i >= j; i--)
        {
          if (i == j && msklen % 8)
          {
            printf("%02X ", ePC_Mask[i] & (uchar)(0xFF << (8 - (msklen % 8))));
          }
          else
          {
            printf("%02X ", ePC_Mask[i]);
          }
        }
        printf("hex\n");

        bitmask = 0x80;
        crc16 = CRC16_PRESET_MSB;
        j = ePC_Bytes[ePC_Type] - 1;
        for (i = 0; i < msklen && i < (8 * ePC_Bytes[ePC_Type]); i++)
        {
          if (ePC_Mask[j] & bitmask)
          {
            crc16 = (crc16 & 0x8000)? crc16 << 1 : (crc16 << 1) ^ CRC16_POLYNOM_MSB;
          }
          else
          {
            crc16 = (crc16 & 0x8000)? (crc16 << 1) ^ CRC16_POLYNOM_MSB : crc16 << 1;
          }
          if (bitmask != 0x01)
          {
            bitmask >>= 1;
          }
          else
          {
            bitmask = 0x80;
            j--;
          }
        }
        printf("\nCRC16:    %02X %02X hex (MSB LSB)     MSB-first algorithm!",
               (uchar)(crc16 >> 8), (uchar)crc16);
        printf("\nCRC16inv: %02X %02X hex (MSB LSB)"
               " ==> inverted (used for IúCODE OTP/EPC)\n\n",
               (uchar)(~crc16 >> 8), (uchar)(~crc16));

        if ((uchar)(~crc16)      == WriteData[0] &&
            (uchar)(~crc16 >> 8) == WriteData[1] && Cont)
        {
          Beep(3000, 1000);
          Cont = FALSE;
        }
        else if (kbhit())
        {
          i = getch();
          if (i == 0x00 || i == 0xE0)  getch();
          Cont = FALSE;
        }
        continue;
      }
      else if (ISO_mode)
      {
        printf("\nRead EPC ...\n\n");

        HL_ISO_cmd(0xA8, DataRate | AdSelAFI, 0, FLAGS + BYTES_EPC, UID_Mask, Data, Data);
        continue;
      }
      else
      {
        Cont = FALSE;
        CRM_get_info(CRM_GET_SYS_VARS, Data);
        wait_Eot(0);
        if (get_status(0, "GI4") != OK)  continue;

        printf("\nInput Value (0..FF hex) for Family Code (%02X hex): ", Data[1]);
        gets(InputStr);
        if (sscanf(InputStr, "%2x", &i) == EOF)  continue;

        CRM_config(CFG_FAMILY_CODE, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "CF9") != OK)  continue;

        printf("New Family Code: %02X hex\n", i);
        continue;
      }
    }

////
    if (Ch == 'd')
    {
      printf("\nSet Default Parameters ...\n\n");

      CRM_config(CFG_DEFAULT, 0);
      wait_Eot(0);
      if (get_status(0, "CF2") != OK)  continue;
      Ch = 'i';
    }

////
    if (Ch == 'D')
    {
      if (ISO_mode && ePC_mode)
      {
        printf("\nRead ALL (Dummy Address, ECNTRL2 E) ...\n\n");

        Data[0] = 0; // Dummy Address
        Data[1] = EE_control;
        parlen = 2;
        rxlen  = MAX_OTP_BYTES + CRC8;

        HL_ePC_cmd(OTP_READ_ALL, parlen, rxlen, ePC_Mask, Data, Data);
        continue;
      }
      printf("\nDecoder Test (ECNTRL2: 04 hex, NoBl_RdAll: %d, EEPROM must be erased) ...\n",
                                                           NoBl_RdAll);
      printf("\n");
      Data[0] = 0x00; // Addr
      Data[1] = 0x04; // ECNTRL2: Decoder Test

      HL_ISO_cmd(0xF0, DataRate | AdSelAFI, 2, (FLAGS + 4 * NoBl_RdAll),
                       UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == 'e')
    {
      if (ISO_mode && ePC_mode)
      {
        printf("\nLong Range Command: Read EAS ...\n\n");

        Data[0] = 0x00;                       // Data Selector:   EAS
        Data[1] = 0x00;                       // Number of Slots: ignored
        Data[2] = LR_CMD ^ Data[0] ^ Data[1]; // XOR (Checkbyte)
        parlen  = 3;
        rxlen   = EAS + CRC16;

        HL_ePC_cmd(LR_CMD, parlen, rxlen, NULL, Data, Data);
        continue;
      }
      CRM_eas(Data);
      wait_Eot(0);
      if (get_status(0, "EAS") != OK)  continue;

      if (Data[0] > MaxEas)
      {
        MaxEas = Data[0];
      }
      printf("EAS-Response:   %3d (peak %3d) -> %5.1f %%   ",
                              Data[0],  MaxEas, Data[0]/2.55);

      if (Data[0] >= EasLevel)
      {
        printf("%24s\r\n", "===>  EAS  Alarm  <===");
        Beep(1000 + 16*Data[0], 2);
      }
      else
      {
        printf("%24s\n", "No Label or EAS disabled");
      }
      printf("%79s\n", Measure[(int)(0.31 * (Data[0] + 3))]);
      continue;
    }

////
    if (Ch == 'E' && ISO_mode)
    {
      Cont = FALSE;
      printf("\nInput Value (0..FF hex) for ECNTRL2 (%02X hex): ",EE_control);
      gets(InputStr);
      if (sscanf(InputStr, "%02X", &i) == EOF)  continue;

      EE_control = (uchar)(i & 0xFF);
      printf("New ECNTRL2: %02X hex\n", EE_control);
      continue;
    }

////
    if (Ch == 'f')
    {
      if (ISO_mode && ePC_mode)
      {
        printf("\nLong Range Command: Read UID ...\n\n");

        Data[0] = 0x01;                       // Data Selector: UID
        Data[1] = LR_Slots[tmpTse];           // Number of Slots
        Data[2] = LR_CMD ^ Data[0] ^ Data[1]; // XOR (Checkbyte)
        parlen  = 3;
        rxlen   = UID + CRC16;

        HL_ePC_cmd(LR_CMD, parlen, rxlen, NULL, Data, Data);
        continue;
      }
      Cont = FALSE;
      CRM_get_info(CRM_GET_SYS_VARS, Data);
      wait_Eot(0);
      if (get_status(0, "GI4") != OK)  continue;

      if (Oldch != 'f')
      {
        i = (Data[0])? TRUE : FALSE;
      }
      else
      {
        i = (Data[0])? FALSE : TRUE;
        CRM_config(CFG_FAST_MODE, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "CF8") != OK)  continue;
      }
      FastMode = i;
      if (ISO_mode)
      {
        printf("Fast Mode is %s!\n", (FastMode)? "ON  (=> 1 out of 4)" :
                                                 "OFF (=> 1 out of 256)");
      }
      else
      {
        printf("Fast Mode is %s!\n", (FastMode)? "ON" : "OFF (=> 1 out of 256)");
      }
      continue;
    }

////
    if (Ch == 'F')
    {
      if (Oldch != 'F')
      {
        i = RF_off;
      }
      else
      {
        i = (RF_off)? FALSE : TRUE;
        CRM_config(CFG_RF_OFF_ON, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "CF7") != OK)  continue;
      }
      RF_off = i;
      printf("RF-Power is switched %s!\n", (RF_off)? "OFF" : "ON");
      if (Cont)
      {
        Sleep(100);
      }
      continue;
    }

////
    if (Ch == 'g' && ISO_mode)
    {
      if (ePC_mode)
      {
        printf("\nLong Range Command: Read EPC ...\n\n");

        Data[0] = 0x02;                       // Data Selector: EPC
        Data[1] = LR_Slots[tmpTse];           // Number of Slots
        Data[2] = LR_CMD ^ Data[0] ^ Data[1]; // XOR (Checkbyte)
        parlen  = 3;
        rxlen   = BYTES_EPC + CRC16;

        HL_ePC_cmd(LR_CMD, parlen, rxlen, NULL, Data, Data);
        continue;
      }
      printf("\nRead ALL (ECNTRL2: %02x hex, Address: %d, NoBl_RdALL: %d) ...\n\n",
                                   EE_control, RdAd, NoBl_RdAll);

      Data[0] = RdAd;       // Addr
      Data[1] = EE_control; // Standard Read: ECNTRL2 = 0x00

      HL_ISO_cmd(0xF0, DataRate | AdSelAFI, 2, (FLAGS + 4 * NoBl_RdAll),
                       UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == 'G')
    {
      CRM_get_port(Data);
      wait_Eot(0);
      if (get_status(0, "GIP") != OK)  continue;

      printf("Value of Input Port: %02X hex\n", Data[0]);

      continue;
    }

////
    if (Ch == 'h' && !ePC_mode)
    {
      if (ISO_mode)
      {
        Cont = FALSE;
        if (Oldch == 'h')
        {
          DataRate = (uchar)((++DataRate) & 0x03);
        }
        printf("\n%d Sub-Carrier(s), ", (DataRate & 0x01) + 1);
        printf("%s Data Rate at Back-Modulation!\n\n",
               (DataRate & 0x02)? "High" : "Low");
      }
      else
      {
        for (i = 0; i < 32; i++)
        {
          Data[i] = 0xFF;
        }
        printf("\nAll selected Labels will be set to 'Halt' Mode ...\n");

        CRM_halt(Hash, Data, Data);
        wait_Eot(0);
        i = CRM_get_datalen();
        if (get_status(0, "HLT") != OK)  continue;

        show_data(i, Data);
      }
      continue;
    }

////
    if (Ch == 'H')
    {
      if (ISO_mode)
      {
        if (ePC_mode)
        {
          printf("\nIDDQ Test (Write Address y, Data z, ECNTRL2 E) ...");
          printf("\n(The EEPROM should be initialized with FF hex before!)\n\n");
          Data[0] = WrAd;
          Data[1] = WriteData[0]; // LSB
          Data[2] = WriteData[1]; //  :
          Data[3] = WriteData[2]; //  :
          Data[4] = WriteData[3]; // MSB
          Data[5] = (uchar)(EE_control | 0xC0);
          parlen = 6;
          rxlen  = 1;

          HL_ePC_cmd(OTP_IDDQ, parlen, rxlen, ePC_Mask, Data, Data);
          continue;
        }
        printf("\nLock Block y ...\n\n");

        Data[0] = WrAd;

        HL_ISO_cmd(0x22, DataRate | AdSelAFI | Option, 1, FLAGS,
                         UID_Mask, Data, Data);
      }
      else
      {
        Cont = FALSE;
        CRM_get_info(CRM_GET_HALT_MODE, Data);
        wait_Eot(0);
        if (get_status(0, "GI5") != OK)  continue;

        printf("\nInput Value (0/1) for Halt Mode (%d)\n", Data[0]);
        printf("(0: Release timeslots only if HALT status is OK,\n");
        printf(" 1: Release timeslots independent of HALT status): ");
        gets(InputStr);
        if (sscanf(InputStr, "%d", &i) == EOF)  continue;

        CRM_config(CFG_HALT_MODE, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C18") != OK)  continue;

        printf("New Halt Mode: %d\n", i);
      }
      continue;
    }

////
    if (Ch == 'i')
    {
      CRM_get_info(CRM_GET_VERSION, (uchar *)CRM_Reader_Version);
      wait_Eot(0);
      if (get_status(0, "GI1") != OK)  continue;

      if (ISO_reader)
      {
        CRM_get_info(CRM_GET_ISO_15693, Data);
        wait_Eot(0);
        if (get_status(0, "GI8") != OK)  continue;

        ISO_mode    = (Data[0] & 0x01)? TRUE : FALSE;
        Mod100      = (Data[0] & 0x02)? TRUE : FALSE;
        ShortTsMode = (Data[0] & 0x04)? TRUE : FALSE;
        printf("æC-Firmware Version:   %45s ", CRM_Reader_Version);
        if (ISO_mode)
        {
          if (ePC_mode)
          {
            printf("IúCODE OTP\n");
          }
          else
          {
            printf(" ISO 15693\n");
            printf("ISO/IEC 15693 Mode:   %2X", Data[0]);
            if (Mod100)
            {
              printf(" (100 %% Modulation is ON!)");
            }
            if (ShortTsMode)
            {
              printf(" (Short Timeslot Mode is ON!)");
            }
            printf("\n");
          }
        }
        else
        {
          printf("   IúCODE1\n");
        }
      }
      else
      {
        printf("æC-Firmware Version:   %45s IúCODE1-Rd\n", CRM_Reader_Version);
      }

      CRM_get_info(CRM_GET_TIMESLOTS, Data);
      wait_Eot(0);
      if (get_status(0, "GI0") != OK)  continue;

      printf("Number of Timeslots:   %d\n", *(WORD *)Data);

      CRM_exit();
      i = CRM_init(ComX, Baud);
      if (i != OK)
      {
        CRM_get_error_text(i, &ErrTxt);
        printf("\n%s!\n", ErrTxt);
        printf("\n\nPress any key ..."); getch(); printf("\n");
        CRM_exit();
        exit(1);
      }
      switch (CRM_get_timeslots())
      {
        case 1:   Tse = TSE_1;   break;
        case 4:   Tse = TSE_4;   break;
        case 8:   Tse = TSE_8;   break;
        case 16:  Tse = TSE_16;  break;
        case 32:  Tse = TSE_32;  break;
        case 64:  Tse = TSE_64;  break;
        case 128: Tse = TSE_128; break;
        case 256: Tse = TSE_256; break;
        default:  Tse = TSE_1;   break;
      }

      CRM_get_info(CRM_GET_NOISE_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI2") != OK)  continue;

      printf("Noise Levels:         %4u %4u %4u mV (Min Avg Max)\n",
             *((WORD *)&Data[0]), *((WORD *)&Data[2]), *((WORD *)&Data[4]));
      printf("NL Mode:               %u\n", Data[8]);
      printf("Bit Threshold Factor:  %u\n", *((WORD *)&Data[6]));

      CRM_get_info(CRM_GET_IGNORE_WEAK_COLLISION, Data);
      wait_Eot(0);
      if (get_status(0, "GI7") != OK)  continue;

      printf("Ignore WEAK COLLISION: %u\n", Data[0]);

      CRM_get_info(CRM_GET_SYS_VARS, Data);
      wait_Eot(0);
      if (get_status(0, "GI4") != OK)  continue;

      if (ISO_mode)
      {
        if (ePC_mode)
        {
          printf("PULSE Offset OTP:      %d \t %7.2f æs\n", Data[12], Data[12]*C590);
          printf("PULSE Length OTP:      %d \t %7.2f æs\n", Data[13], Data[13]*C590);

          CRM_get_info(CRM_GET_ePC_TESTMODE, Data);
          wait_Eot(0);
          if (get_status(-2, "G10") == OK)
          {
            printf("EPC Testmode:          %02X hex\n", Data[0]);
          }
          CRM_get_info(CRM_GET_ePC_CRC8_PRESET, Data);
          wait_Eot(0);
          if (get_status(-2, "G11") == OK)
          {
            ePC_CRC8_preset = Data[0];
            printf("CRC8 Preset:           %02X hex\n", ePC_CRC8_preset);
          }
          CRM_get_info(CRM_GET_ePC_BYTES, Data);
          wait_Eot(0);
          if (get_status(-2, "G12") == OK)
          {
            if      (Data[0] == BYTES_UID2)     ePC_Type = TYPE_UID2;
            else if (Data[0] == BYTES_UID1A)    ePC_Type = TYPE_UID1A;
            else if (ePC_Type != TYPE_UID1)     ePC_Type = TYPE_EPC;
            printf("Number of EPC-Bytes:   %-2d (IúCODE OTP Type: %s)\n",
                                        Data[0], ePC_Types[ePC_Type]);
          }
        }
        else
        {
          printf("Fastmode (1 out of 4): %d\n",             Data[0]);
          printf("PULSE Offset:          %d \t %7.2f æs\n", Data[10], Data[10]*C590);
          printf("PULSE Length:          %d \t %7.2f æs\n", Data[11], Data[11]*C590);
        }
        CRM_get_info(CRM_GET_RX_DLY_OFFS, Data);
        wait_Eot(0);
        if (get_status(-2, "G13") == OK)
        {
          printf("RX Delay Offset:       %d \t %7.2f æs\n", Data[0], (Data[0] - 4)*C1888);
        }
      }
      else
      {
        printf("Fastmode:              %d\n",                 Data[0]);
        printf("Family Code:           %02X hex\t %3d dec\n", Data[1], Data[1]);
        printf("Application ID:        %02X hex\t %3d dec\n", Data[2], Data[2]);
        printf("START Offset:          %d \t %6.2f æs\n",     Data[3], Data[3]*C590);
        printf("PULSE Offset:          %d \t %6.2f æs\n",     Data[4], Data[4]*C590);
        printf("PULSE Length:          %d \t %6.2f æs\n",     Data[5], Data[5]*C590);
        printf("FAST  Offset:          %d \t %6.2f æs\n",     Data[6], Data[6]*C590);
        printf("FAST  Length:          %d \t %6.2f æs\n",     Data[7], Data[7]*C590);
        printf("EAS Level:             %d \t %3.0f %%\n",     Data[8], Data[8]/2.55);
        printf("EAS Length:            %d \t %3u ms\n",       Data[9], Data[9]*10);

        CRM_get_info(CRM_GET_HALT_MODE, Data);
        wait_Eot(0);
        if (get_status(0, "GI5") != OK)  continue;

        printf("HALT Mode:             %d\n", Data[0]);
      }
      continue;
    }

////
    if (ISO_reader)
    {
      if (Ch == 'I')
      {
        Cont = FALSE;
        if (ePC_mode)
        {
          _clrscr(&Console_x, &Console_y);
          ePC_mode = FALSE;
          if (Slots > MAX_ISO_SLOTS)
          {
            Slots = MAX_ISO_SLOTS;
          }
          printf(iso_menustr, ComX, CRM_get_baud(), Slots, CRM_Reader_Version);
          continue;
        }
        CRM_get_info(CRM_GET_ISO_15693, Data);
        wait_Eot(0);
        if (get_status(0, "GI8") != OK)  continue;

        if (Oldch != 'I')
        {
          ISO_mode = (Data[0] & 0x01)? TRUE : FALSE;
        }
        else
        {
          ISO_mode = (Data[0] & 0x01)? FALSE : TRUE;
          i = Data[0] & 0xFE;
          if (ISO_mode)                            i |= 0x01;
          if (Slots > 1 && Slots < MAX_ISO_SLOTS)  i |= (MAX_ISO_SLOTS - Slots) << 4;

          CRM_config(CFG_ISO_15693, (uchar)i);
          wait_Eot(0);
          if (get_status(0, "C26") != OK)  continue;
        }
        printf("ISO/IEC 15693 Mode is switched %s!\n",
               (ISO_mode)? "ON" : "OFF (=> IúCODE1 Mode enabled)");
        continue;
      }
    }

////
    if (Ch == 'j')
    {
      if (ISO_mode)
      {
        if (ePC_mode)
        {
          printf("\nCHECK PASSWORD (using 3-Byte-Password z) ...\n\n");

          Data[0] = 0x04;         // Manufacturer Code (Philips)
          Data[1] = WriteData[0]; // 24 bit password (LSB)
          Data[2] = WriteData[1]; //                   : 
          Data[3] = WriteData[2]; //       -"-       (MSB)
          parlen  = 4;
          rxlen   = 1;

          HL_ePC_cmd(OTP_CHECK_PWD, parlen, rxlen, ePC_Mask, Data, Data);
          continue;
        }
        printf("\nGet System Information ...\n\n");

        HL_ISO_cmd(0x2B, DataRate | AdSelAFI, 0, FLAGS + INFO_FLAGS + UID,
                         UID_Mask, NULL, Data);
      }
      else
      {
        Cont = FALSE;
        printf("\nInput Value (2..32, e.g. 28) for FAST Pulse Offset: ");
        gets(InputStr);
        if (sscanf(InputStr, "%d", &i) == EOF)  continue;

        CRM_config(CFG_FAST_OFFSET, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C13") != OK)  continue;

        printf("New FAST Pulse Offset: %d\n", i);
      }
      continue;
    }

////
    if (Ch == 'J')
    {
      if (ePC_mode)
      {
        printf("\nInitialize EPC Label ...\n\n");
        printf("New EPC (MSB..LSB): ");
        for (i = ePC_Bytes[WriteData[3] & 0x03] - 1; i >= 0; i--)
        {
          printf("%02X ", ePC_Mask[i]);
        }
        printf("hex\n\n");
        printf("Put one Label into the Antenna-Field ...\n");

        CRM_get_info(CRM_GET_ePC_TESTMODE, Data);
        wait_Eot(0);
        if (get_status(0, "G10") != OK)  continue;

        ePC_Testmode = Data[0];

        CRM_get_info(CRM_GET_ePC_CRC8_PRESET, Data);
        wait_Eot(0);
        if (get_status(0, "G11") != OK)  continue;

        ePC_CRC8_preset = Data[0];

        CRM_config(CFG_ePC_TESTMODE, 0x01);
        wait_Eot(0);
        if (get_status(0, "C33") != OK)  continue;

        rxlen = ePC_Bytes[ePC_Type] + CRC16;
        parlen = 4;                               // Hashvalue mode: external

        for (i = 0, j = 0; i < 15; i++)
        {
          Data[0] = OTP_BEGIN_ROUND;
          Data[1] = 0;                            // Mask Length
          Data[2] = 0;                            // Number of Slots
          Data[3] = 0;                            // Hashvalue

          CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
          wait_Eot(1);
          if (get_status(0, "OBR") != OK)  break;

          j += CRM_get_datalen();
        }
        if (j != (15 * (STAT + rxlen + 1)))
        {
          parlen = 3;                             // Hashvalue mode: internal

          for (i = 0, j = 0; i < 15; i++)
          {
            Data[0] = OTP_BEGIN_ROUND;
            Data[1] = 0;                          // Mask Length
            Data[2] = 0;                          // Number of Slots

            CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
            wait_Eot(1);
            if (get_status(0, "OBR") != OK)  break;

            j += CRM_get_datalen();
          }
          if (j != (15 * (STAT + rxlen + 1)))
          {
            CRM_config(CFG_ePC_TESTMODE, (uchar)ePC_Testmode);
            wait_Eot(0);
            get_status(0, "G10");
            continue;
          }
        }
        for (i = 0, j = TRUE; i < rxlen - CRC16; i++)
        {
          if (Data[STAT + rxlen - CRC16 - 1 + i] != ePC_Mask[i])
          {
            j = FALSE;
            break;
          }
        }
        if (j)
        {
          CRM_config(CFG_ePC_TESTMODE, (uchar)ePC_Testmode);
          wait_Eot(0);
          get_status(0, "G10");
          continue;
        }
        ePC_type_tmp = ePC_Type;
        ePC_Type = WriteData[3] & 0x03;
        WriteData[3] |= 0xC8;

        printf("\nCalculate CRC16 of EPC (MSB LSB): ");

        bitmask = 0x80;
        crc16 = CRC16_PRESET_MSB;
        start = (ePC_Type == TYPE_UID2)? BYTES_UID2 - BYTES_UID2_UID : 0;

        for (i = 8 * start, j = ePC_Bytes[ePC_Type] - 1 - start;
             i < (8 * ePC_Bytes[ePC_Type]); i++)
        {
          if (ePC_Mask[j] & bitmask)
          {
            crc16 = (crc16 & 0x8000)? crc16 << 1 : (crc16 << 1) ^ CRC16_POLYNOM_MSB;
          }
          else
          {
            crc16 = (crc16 & 0x8000)? (crc16 << 1) ^ CRC16_POLYNOM_MSB : crc16 << 1;
          }
          if (bitmask != 0x01)
          {
            bitmask >>= 1;
          }
          else
          {
            bitmask = 0x80;
            j--;
          }
        }
        crc16 = ~crc16;
        printf("%02X %02X hex\n\n", (uchar)(crc16 >> 8), (uchar)(crc16));

        parlen  = 3;
        rxlen   = 0;
        if (ePC_Type == TYPE_EPC)
        {
          i = 0x20;                                 // 0x20 = 50 % Duty Cycle (Response)
          ePC_Hashmode = 0;                         // + External Hashvalue Calculation
        }
        else
        {
          i = 0x28;                                 // 0x28 = 50 % Duty Cycle (Response)
          ePC_Hashmode = 1;                         // + Internal Hashvalue Calculation
        }
        printf("Byte %2d -> ", MAX_OTP_BYTES - 1);
        Data[0] = OTP_WRITE;
        Data[1] = MAX_OTP_BYTES - 1;                // Addr for Config Byte 1
        Data[2] = (uchar)i;                         // Data for Config Byte 1

        CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
        wait_Eot(1);
        if (get_status(0, "OWR") != OK)  continue;

        printf("Data = %02X hex (Config Byte 1)     = 50 %% Duty Cycle,\n", (uchar)i);
        printf("%46s %s Hashvalue Calculation\n", " ", 
               (ePC_Hashmode)? "internal" : "external");

        printf("Byte %2d -> ", MAX_OTP_BYTES - 2);
        Data[0] = OTP_WRITE;
        Data[1] = MAX_OTP_BYTES - 2;                // Addr for Config Byte 0
        Data[2] = WriteData[3];                     // Data for Config Byte 0

        CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
        wait_Eot(1);
        if (get_status(0, "OWR") != OK)  continue;

        printf("Data = %02X hex (Config Byte 0)     = WriteData[3] ==> Type: %s\n\n", 
                       WriteData[3], ePC_Types[ePC_Type]);

        for (j = ePC_Bytes[ePC_Type] + CRC16 + 2; j >= ePC_Bytes[ePC_Type] + CRC16; j--)
        {
          printf("Byte %2d -> ", j);
          Data[0] = OTP_WRITE;
          Data[1] = (uchar)j;                       // Addr for Password
          Data[2] = WriteData[ePC_Bytes[ePC_Type] + CRC16 + 2 - j];

          CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
          wait_Eot(1);
          if (get_status(0, "OWR") != OK)  break;

          printf("Data = %02X hex (Password LSB..MSB) = WriteData[%d]\n", 
                 WriteData[ePC_Bytes[ePC_Type] + CRC16 + 2 - j],
                 ePC_Bytes[ePC_Type] + CRC16 + 2 - j);
        }
        printf("\n");

        printf("Byte %2d -> ", ePC_Bytes[ePC_Type] + 1);
        Data[0] = OTP_WRITE;
        Data[1] = (uchar)(ePC_Bytes[ePC_Type] + 1); // Addr for CRC16 (LSB)
        Data[2] = (uchar)crc16;

        CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
        wait_Eot(1);
        if (get_status(0, "OWR") != OK)  continue;

        printf("Data = %02X hex (CRC16 LSB)\n", (uchar)(crc16));

        printf("Byte %2d -> ", ePC_Bytes[ePC_Type]);
        Data[0] = OTP_WRITE;
        Data[1] = (uchar)ePC_Bytes[ePC_Type];       // Addr for CRC16 (MSB)
        Data[2] = (uchar)(crc16 >> 8);

        CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
        wait_Eot(1);
        if (get_status(0, "OWR") != OK)  continue;

        printf("Data = %02X hex (CRC16 MSB)\n\n", (uchar)(crc16 >> 8));

        for (j = ePC_Bytes[ePC_Type] - 1; j >= 0 ; j--)
        {
          printf("Byte %2d -> ", j);
          Data[0] = OTP_WRITE;
          Data[1] = (uchar)j;                       // Addr for ePC Bytes
          Data[2] = ePC_Mask[ePC_Bytes[ePC_Type] - j - 1];

          CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
          wait_Eot(1);
          if (get_status(0, "OWR") != OK)  break;

          printf("Data = %02X hex (EPC LSB..MSB)\n",
                 ePC_Mask[ePC_Bytes[ePC_Type] - j - 1]);
        }
        printf("\n");

        for (j = MAX_OTP_BYTES - 3; j >= MAX_OTP_BYTES - 8; j--)
        {
          printf("Byte %2d -> ", j);
          Data[0] = OTP_WRITE;
          Data[1] = (uchar)j;                       // Addr for unused Bytes & Lockbits
          Data[2] = 0x00;                           // Data

          CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
          wait_Eot(1);
          if (get_status(0, "OWR") != OK)  break;

          printf("Data = 00 hex (Lock Bits)\n");
        }
        printf("\n");
        rxlen = ePC_Bytes[ePC_Type] + CRC16;

        CRM_config(CFG_RF_PAUSE_INIT, 0);
        wait_Eot(0);
        if (get_status(0, "CF0") != OK)  continue;

        switch (ePC_Type)
        {
          case TYPE_UID1:
          case TYPE_UID1A:
            i = CRC8_PRESET_UID1_1A;
            break;
          case TYPE_UID2:
            i = CRC8_PRESET_UID2;
            break;
          default:
            i = CRC8_PRESET_EPC;
        }
        CRM_config(CFG_ePC_CRC8_PRESET, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C34") != OK)  continue;

        CRM_config(CFG_ePC_BYTES, (uchar)ePC_Bytes[ePC_Type]);
        wait_Eot(0);
        if (get_status(0, "C35") != OK)  continue;

        parlen = 2;                               // Hashvalue mode: internal
        Data[0] = 0;                              // Mask Length    
        Data[1] = 0;                              // Number of Slots

        i = HL_ePC_cmd(OTP_BEGIN_ROUND, parlen, rxlen, ePC_Mask, Data, Data);

        if (i != rxlen)
        {
          parlen = 3;                             // Hashvalue mode: external
          Data[0] = 0;                            // Mask Length    
          Data[1] = 0;                            // Number of Slots
          Data[2] = 0;                            // Hashvalue      

          i = HL_ePC_cmd(OTP_BEGIN_ROUND, parlen, rxlen, ePC_Mask, Data, Data);
        }
        j = FALSE;
        if (i != rxlen)                                           j = TRUE;
        if (Data[STAT + rxlen - 1] != (uchar)(crc16))             j = TRUE;
        if (Data[STAT + rxlen - 2] != (uchar)(crc16 >> 8))        j = TRUE;
        for (i = 0; i < rxlen - CRC16; i++)
        {
          if (Data[STAT + rxlen - CRC16 - 1 - i] != ePC_Mask[i])  j = TRUE;
        }
        if (j)
        {
          Beep(500, 500);
          Cont = FALSE;
          printf("\nErroneous EPC + CRC16: ");
          if (Data[0] == CRM_NO_TAG)
          {
            printf("No Response!                                   \n");
          }
          else
          {
            for (i = 1; i < STAT + rxlen; i++)
            {
              printf("%02X ", Data[i]);
            }
            printf("hex   \n");
          }
          printf("\n(Correct  EPC + CRC16: ");
          for (i = ePC_Bytes[ePC_Type] - 1; i >= 0; i--)  printf("%02X ", ePC_Mask[i]);
          printf("%02X %02X hex)   \n", (uchar)(crc16 >> 8), (uchar)(crc16));

          ePC_Type = ePC_type_tmp;

          CRM_config(CFG_ePC_TESTMODE, (uchar)ePC_Testmode);
          wait_Eot(0);
          if (get_status(0, "C33") != OK)  continue;

          CRM_config(CFG_ePC_CRC8_PRESET, (uchar)ePC_CRC8_preset);
          wait_Eot(0);
          get_status(0, "C34");
          continue;
        }
        Beep(5000, 200);
        (*(ulong *)ePC_Mask)++;
        printf("\nNext EPC (MSB..LSB):   ");
        for (i = ePC_Bytes[ePC_Type] - 1; i >= 0; i--)  printf("%02X ", ePC_Mask[i]);
        printf("(%Lu)\n", *(ulong *)ePC_Mask);

        if (Cont)
        {
          printf("\nRemove the Label ...\n");
          rxlen = ePC_Bytes[ePC_Type] + CRC16;

          parlen = 4;                             // Hashvalue mode: external
          do
          {
            Data[0] = OTP_BEGIN_ROUND;
            Data[1] = 0;                          // Mask Length
            Data[2] = 0;                          // Number of Slots
            Data[3] = 0;                          // Hashvalue

            CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
            wait_Eot(1);
          } while (Cont && CRM_get_datalen() == (uchar)(STAT + rxlen + 1));

          parlen = 3;                             // Hashvalue mode: internal
          do
          {
            Data[0] = OTP_BEGIN_ROUND;
            Data[1] = 0;                          // Mask Length
            Data[2] = 0;                          // Number of Slots

            CRM_test(CMD_EPC, (uchar)parlen, (uchar)rxlen, Data, Data);
            wait_Eot(1);
          } while (Cont && CRM_get_datalen() == (uchar)(STAT + rxlen + 1));
        }
        ePC_Type = ePC_type_tmp;

        CRM_config(CFG_ePC_TESTMODE, (uchar)ePC_Testmode);
        wait_Eot(0);
        if (get_status(0, "C33") != OK)  continue;

        CRM_config(CFG_ePC_CRC8_PRESET, (uchar)ePC_CRC8_preset);
        wait_Eot(0);
        if (get_status(0, "C34") != OK)  continue;

        printf("=====> OK <=====\n");
        continue;
      }
      // ISO_mode
      Cont = FALSE;
      if (ISO_mode)
      {
        if (NoBl_XS > MAX_NoBl_SLI)
        {
          printf("\nInitialize SLI-S Label (%d Blocks = %d Bytes = %d Bits) ...\n",
                                             NoBl_XS, 4 * NoBl_XS, 32 * NoBl_XS);
          printf("\nNew UID: ");
          for (i = 0; i < UID; i++)     printf("%02X ", UID_Mask2[i]);
          printf("(%Lu)\n\n", *(ulong *)UID_Mask2);

          if (NoBl_XS != MAX_NoBl_SLI_XS0T)
          {                             // Write ALL
            Data[0] = 0;                // Addr
            Data[1] = 0x03;             // ECNTRL2: Enable all rows
            memcpy(&Data[2], AllZeros, 4);
            i = HL_ISO_cmd(0xF2, 0x02, 6, FLAGS, NULL, Data, Data);
            if (i != FLAGS)  continue;

            Data[0] = 12;               // Write PPC (Page Protection Conditions R/W)
            memcpy(&Data[1], AllOnes, 4);
            i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
            if (i != FLAGS)  continue;
          }
          else
          {
            for (j = 0; j < NoBl_XS; j++)
            {
              Data[0] = 0x02;           // Flags (DataRate)
              Data[1] = 0x21;           // Write Single Block
              Data[2] = (uchar)j;       // Addr
              if (j == 12)              // PPC (Page Protection Conditions R/W)
              {
                memcpy(&Data[3], AllOnes, 4);
              }
              else
              {
                memcpy(&Data[3], AllZeros, 4);
              }
              CRM_cmd(CMD_CMD, FLAGS + CMD + 5, FLAGS, Data, Data);
              wait_Eot(1);
              i = CRM_get_datalen();
              if (get_status(0, "SL2") != OK)  break;
              if (i < STAT + FLAGS)            break;
            }
            if (i < STAT + FLAGS)
            {
              Beep(500, 500);
              printf("\n==> Error at Initializing of Block %d! <==\n\n", j);
              continue;
            }
          }
          Data[0] = 0;                  // Write Single Block SNR0
          memcpy(&Data[1], &UID_Mask2[0], 4);
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;

          Data[0] = 1;                  // Write Single Block SNR1
          memcpy(&Data[1], &UID_Mask2[4], 4);
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;

          Data[0] = 2;                  // Write Single Block AFI/DSFID
          Data[1] = 0x00; Data[2] = 0x00; Data[3] = 0xAF; Data[4] = 0xDF;
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;
                                          
          Data[0] = 3;                  // Write Single Block Config
          memcpy(&Data[1], WriteData, 4);
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;

          Data[0] = 4;                  // Write Single Block BootLock/LockBits
          Data[1] = 0x13; Data[2] = 0x00; Data[3] = 0x00; Data[4] = 0x00; 
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;

          Data[0] = 16;                 // Write Single Block EPC (MSB ...)
          Data[1] = 0x80; Data[2] = 0x40; Data[3] = 0xC0; Data[4] = 0x20; 
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;

          Data[0] = 17;                 // Write Single Block EPC (  ...  )
          Data[1] = 0xA0; Data[2] = 0x60; Data[3] = 0xE0; Data[4] = 0x10; 
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;

          Data[0] = 18;                 // Write Single Block EPC (... LSB)
          Data[1] = 0x90; Data[2] = 0x08; Data[3] = 0x88; Data[4] = 0x48; 
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;

          Data[0] = 19;                 // Write Single Block EPC (CRC16)
          Data[1] = 0x26; Data[2] = 0x4C; Data[3] = 0x00; Data[4] = 0x00; 
          i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
          if (i != FLAGS)  continue;
                                        // Set EAS
          i = HL_ISO_cmd(0xA2, 0x02, 0, FLAGS, NULL, NULL, Data);
          if (i < FLAGS)   continue;
                                        // Read ALL
          Data[0] = 0;                  // Addr
          Data[1] = 0x00;               // Standard Read: ECNTRL2 = 0x00
          i = HL_ISO_cmd(0xF0, 0x02, 2, (FLAGS + 4 * NoBl_XS), NULL, Data, Data);
          if (i != (FLAGS + 4 * NoBl_XS))   continue;
/*
          Data[0] = 0;                  // Read Multiple Blocks
          j = (NoBl_XS > MAX_NoBl_SLI_XS0T)? MAX_NoBl_SLI_XS0T - 1 : NoBl_XS - 1;
          Data[1] = (uchar)(j - 1);
          i = HL_ISO_cmd(0x23, 0x02, 2, FLAGS + 4*j, NULL, Data, Data);
          if (i != (FLAGS + 4*j))          continue;
*/
          CRM_eas(Data);                // Check EAS
          wait_Eot(0);
          if (get_status(0, "EAS") != OK)   continue;

          printf("EAS-Response:   %3d => %5.1f %% ", Data[0], Data[0]/2.55);
          if (Data[0] >= EasLevel)
          {
            printf("=> EAS OK!\n");
            Beep(1000 + 16*Data[0], 300);

            (*(ulong *)UID_Mask2)++;
            printf("\nNext UID:        ");
            for (i = 0; i < UID; i++)     printf("%02X ", UID_Mask2[i]);
            printf("(%Lu)\n", *(ulong *)UID_Mask2);
          }
          printf("\n");
          continue;
        }
        printf("\nInitialize SLI Label ...\n\n");
                                        // Write ALL Ones
        Data[0] = 0;                    // Addr
        Data[1] = 0x03;                 // Enable all rows
        memcpy(&Data[2], AllOnes, 4);

        i = HL_ISO_cmd(0xF2, 0x02, 6, FLAGS, NULL, Data, Data);
        if (i != FLAGS)                 // No response or no longer in Testmode
        {
          for (j = 0; j < MAX_NoBl_SLI - 4; j++) // Init User Blocks
          {
            Data[0] = (uchar)j;         // Addr
            memcpy(&Data[1], WriteData, 4);

            i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
            if (i < FLAGS)  break;
          }
          if (i < FLAGS)  continue;     // Set EAS

          i = HL_ISO_cmd(0xA2, 0x02, 0, FLAGS, NULL, NULL, Data);
          if (i < FLAGS)  continue;

          Data[0] = 0;                  // Addr
          Data[1] = MAX_NoBl_SLI - 5;

          i = HL_ISO_cmd(0x23, 0x02, 2, FLAGS + 4 * (MAX_NoBl_SLI - 4), NULL, Data, Data);
          if (i == (FLAGS + 4 * (MAX_NoBl_SLI - 4))) Beep(3000, 300);
          continue;
        }                               // Write ALL Zeros
        Data[0] = 0;                    // Addr
        Data[1] = 0x03;                 // Enable all rows
        memcpy(&Data[2], AllZeros, 4);

        i = HL_ISO_cmd(0xF2, 0x02, 6, FLAGS, NULL, Data, Data);
        if (i != FLAGS)  continue;
                                        // Write Single Block SNR0
        Data[0] = 0;                    // SNR0
        memcpy(&Data[1], &UID_Mask[0], 4);

        i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
        if (i != FLAGS)  continue;
                                        // Write Single Block SNR1
        Data[0] = 1;                    // SNR1
        memcpy(&Data[1], &UID_Mask[4], 4);

        i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
        if (i != FLAGS)  continue;
                                        // Write Single Block EAS
        Data[0] = 2;                    // EAS
        memcpy(&Data[1], EnableEAS, 4);

        i = HL_ISO_cmd(0x21, 0x02, 5, FLAGS, NULL, Data, Data);
        if (i != FLAGS)  continue;
                                        // Read Multiple Blocks
        Data[0] = 0;                    // Addr
        Data[1] = MAX_NoBl_SLI - 1;

        i = HL_ISO_cmd(0x23, 0x02, 2, FLAGS + 4 * MAX_NoBl_SLI, NULL, Data, Data);
        if (i != (FLAGS + 4 * MAX_NoBl_SLI))  continue;
                                        // EAS
        i = HL_ISO_cmd(0xA5, 0x02, 0, FLAGS + EAS, NULL, NULL, Data);
        if (i != (FLAGS + EAS))  continue;

        if (*(ulong *)&Data[STAT + FLAGS] == *(ulong *)EAS_Resp)
        {
          (*(ulong *)UID_Mask)++;
          printf("\nNext UID: ");
          for (i = 0; i < UID; i++)     printf("%02X ", UID_Mask[i]);
          printf("(%Lu)\n", *(ulong *)UID_Mask);
          Beep(5000, 200);
        }
      }
      else
      {
        printf("\nInput Value (2..27, e.g. 15) for START Pulse Offset: ");
        gets(InputStr);
        if (sscanf(InputStr, "%d", &i) == EOF)  continue;

        CRM_config(CFG_START_OFFSET, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C15") != OK)  continue;

        printf("New START Pulse Offset: %d\n", i);
      }
      continue;
    }

////
    if (Ch == 'k' || Oldch == 'k' && (Ch == '+' || Ch == '-'))
    {
      Cont = FALSE;
      if (ISO_mode)
      {
        if (ePC_mode)        max = 8 * ePC_Bytes[ePC_Type];
        else if (Slots == 1) max = 64;
        else                 max = 60;
        if (Oldch == 'k' && Ch != '+' && Ch != '-')
        {
          printf("\nInput Value (0..%d) for Mask Length (%d): ", max, Masklen);
          gets(InputStr);
          if (sscanf(InputStr, "%3d", &i) == EOF)  continue;

          if (i <= max)      Masklen = (uchar)i;
          else               Masklen = (uchar)max;
        }
        else if (Ch == '+')
        {
          if (Masklen < max) Masklen++;
          else               Masklen = 0;
        }
        else if (Ch == '-')
        {
          if (Masklen > 0)   Masklen--;
          else               Masklen = (uchar)max;
        }
        printf("Mask Length: %d\n", Masklen);
      }
      else
      {
        printf("\nInput Value (2..32, e.g. 29) for FAST Pulse Length: ");
        gets(InputStr);
        if (sscanf(InputStr, "%d", &i) == EOF)  continue;

        CRM_config(CFG_FAST_LENGTH, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C14") != OK)  continue;

        printf("New FAST Pulse Length: %d\n", i);
      }
      continue;
    }

////
    if (Ch == 'K')
    {
      Cont = FALSE;
      printf("\nStore current Modulation Index ... \n\n");
      CRM_config(CFG_MOD_DEPTH, 0);
      wait_Eot(0);
      if (get_status(0, "C11") != OK)  continue;

      printf("OK\n");
      continue;
    }

////
    if (Ch == 'l' || Oldch == 'l' && (Ch == '+' || Ch == '-'))
    {
      Cont = FALSE;
      if (ePC_mode)
      {
        printf("\nLogging of BEGIN ROUND commands "
               "(press [+] or [-] to switch logging ON or OFF)\n");

        if (Ch == '+')
        {
          if (Lfp != NULL)
          {
            fclose(Lfp);
            Lfp = NULL;
          }
          if ((Lfp = fopen(LogFile, "a")) == NULL)
          {
            printf("\nCan't open logfile '%s' for output!\n", LogFile);
            Logging = FALSE;
            continue;
          }
          _strdate(DateBuf);
          _strtime(TimeBuf);
          fprintf(Lfp, "Start logging of BEGIN ROUND (%s %s) ...\n", DateBuf,TimeBuf);
          Logging = TRUE;
        }
        else if (Ch == '-')
        {
          if (Lfp != NULL)
          {
            _strdate(DateBuf);
            _strtime(TimeBuf);
            fprintf(Lfp, " Stop logging of BEGIN ROUND (%s %s) ...\n", DateBuf,TimeBuf);
            fclose(Lfp);
            Lfp = NULL;
          }
          Logging = FALSE;
        }
        else if (Ch == 'l' && Oldch == 'l')
        {
          printf("\nInput new filename (currently '%s'): ", LogFile);
          gets(InputStr);
          if (strlen(InputStr) > 0)
          {
            strncpy(LogFile, InputStr, 79);
          }
        }
        printf("Current state is %s (filename: '%s')!\n",
                     (Logging)? "ON" : "OFF", LogFile);
        continue;
      }
      printf("\nInput Value (0..255, 1: 10 ms) for EAS Alarm Length: ");
      gets(InputStr);
      if (sscanf(InputStr, "%3d", &i) == EOF)  continue;

      if (i > 255) i = 255;

      CRM_config(CFG_EAS_LENGTH, (uchar)i);
      wait_Eot(0);
      if (get_status(0, "CF4") != OK)  continue;

      printf("New EAS Alarm Length: %d\n", i);
      continue;
    }

////
    if (Ch == 'L' || Oldch == 'L' && (Ch == '+' || Ch == '-' ||
                                      Ch == '/' || Ch == '*'))
    {
      Cont = FALSE;
      if (Ch == '+')
      {
        printf("\nIncrease Modulation Index without store (1 step: ~ 0.5 %%) ...\n");
        CRM_config(CFG_MOD_DEPTH_NOSTORE, CRM_CFG_EEPOT_DOWN + 1);
        wait_Eot(0);
        if (get_status(0, "C23") != OK)  continue;
      }
      else if (Ch == '-')
      {
        printf("\nDecrease Modulation Index without store (1 step: ~ 0.5 %%) ...\n");
        CRM_config(CFG_MOD_DEPTH_NOSTORE, CRM_CFG_EEPOT_UP + 1);
        wait_Eot(0);
        if (get_status(0, "C23") != OK)  continue;
      }
      else if (Ch == '*')
      {
        printf("\nIncrease Modulation Index without store (1 step: ~ 5 %%) ...\n");
        CRM_config(CFG_MOD_DEPTH_NOSTORE, CRM_CFG_EEPOT_DOWN + 10);
        wait_Eot(0);
        if (get_status(0, "C23") != OK)  continue;
      }
      else if (Ch == '/')
      {
        printf("\nDecrease Modulation Index without store (1 step: ~ 5 %%) ...\n");
        CRM_config(CFG_MOD_DEPTH_NOSTORE, CRM_CFG_EEPOT_UP + 10);
        wait_Eot(0);
        if (get_status(0, "C23") != OK)  continue;
      }
      else if (Ch == 'L')
      {
        printf("\nChange Modulation Index without store "
                 "(Press [+][-]: 0.5 %%, [*][/]: 5 %%) ...\n");
      }
      CRM_eas(Data);
      wait_Eot(0);
      if (get_status(0, "EAS") != OK)  continue;

      printf("\nEAS-Response:     %3d -> %5.1f %%\n", Data[0], Data[0]/2.55);

      Sleep(150);
      CRM_get_info(CRM_GET_MOD_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI3") != OK)  continue;

      Vrfmax = (float)(*((WORD *)&Data[0])) * VRF_LSB;
      Vrfmin = (float)(*((WORD *)&Data[2])) * VRF_LSB;

      CRM_config(CFG_RF_OFF_ON, TRUE);
      wait_Eot(0);
      if (get_status(0, "CF7") != OK)  continue;

      Sleep(1);
      CRM_get_info(CRM_GET_MOD_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI3") != OK)  continue;

      Vrfoff = (float)(*((WORD *)&Data[0])) * VRF_LSB;

      printf("RF-Level:         %4.0f mV\n", Vrfmax);
      printf("Modulated VRF:    %4.0f mV\n", Vrfmin);
      printf("RF switched off:  %4.0f mV\n", Vrfoff);

      Vrfmax -= Vrfoff;
      Vrfmin -= Vrfoff;

      if ((Vrfmax + Vrfmin) != 0)
      {
        printf("Modulation Index: %4.1f %%\n", (Vrfmax - Vrfmin)/
               (Vrfmax + Vrfmin) * 85.0);
      }
      continue;
    }

////
    if (Ch == 'm')
    {
      if (ISO_mode)
      {
        printf("\nSet MUX ...\n\n");

        if (ePC_mode)
        {
          Data[0] = ePC_MUX_value;
          parlen = 1;
          rxlen  = 1;

          HL_ePC_cmd(OTP_SETMUX, parlen, rxlen, ePC_Mask, Data, Data);
          continue;
        }
        Data[0] = MUX_value;

        HL_ISO_cmd(0xF3, DataRate | AdSelAFI | Option, 1, FLAGS, UID_Mask, Data, Data);
        continue;
      }
      else
      {
        Cont = FALSE;
        printf("\nInput Value (0..255, e.g. 64) for EAS Alarm Level (%d): ",
                                                                     EasLevel);
        gets(InputStr);
        if (sscanf(InputStr, "%3d", &i) == EOF)  continue;

        if (i > 255) EasLevel = 255;
        else         EasLevel = (uchar)i;

        CRM_config(CFG_EAS_LEVEL, EasLevel);
        wait_Eot(0);
        if (get_status(0, "CF3") != OK)  continue;

        printf("New EAS Alarm Level: %d\n", EasLevel);
      }
      continue;
    }

////
    if (Ch == 'M' || Oldch == 'M' && (Ch == '+' || Ch == '-'))
    {
      Cont = FALSE;
      if (Ch == '+')
      {
        printf("\nIncrease Modulation Index ... \n");
        CRM_config(CFG_MOD_DEPTH, CRM_CFG_EEPOT_DOWN + 1);
        wait_Eot(0);
        if (get_status(0, "C11") != OK)  continue;
      }
      else if (Ch == '-')
      {
        printf("\nDecrease Modulation Index ... \n");
        CRM_config(CFG_MOD_DEPTH, CRM_CFG_EEPOT_UP + 1);
        wait_Eot(0);
        if (get_status(0, "C11") != OK)  continue;
      }
      else if (Ch == 'M')
      {
        printf("\nChange Modulation Index (Press [+] or [-]) ...\n");
      }
      CRM_eas(Data);
      wait_Eot(0);
      if (get_status(0, "EAS") != OK)  continue;

      printf("\nEAS-Response:     %3d -> %5.1f %%\n", Data[0], Data[0]/2.55);

      Sleep(150);
      CRM_get_info(CRM_GET_MOD_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI3") != OK)  continue;

      Vrfmax = (float)(*((WORD *)&Data[0])) * VRF_LSB;
      Vrfmin = (float)(*((WORD *)&Data[2])) * VRF_LSB;

      CRM_config(CFG_RF_OFF_ON, TRUE);
      wait_Eot(0);
      if (get_status(0, "CF7") != OK)  continue;

      Sleep(1);
      CRM_get_info(CRM_GET_MOD_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI3") != OK)  continue;

      Vrfoff = (float)(*((WORD *)&Data[0])) * VRF_LSB;

      printf("RF-Level:         %4.0f mV\n", Vrfmax);
      printf("Modulated VRF:    %4.0f mV\n", Vrfmin);
      printf("RF switched off:  %4.0f mV\n", Vrfoff);

      Vrfmax -= Vrfoff;
      Vrfmin -= Vrfoff;

      if ((Vrfmax + Vrfmin) != 0)
      {
        printf("Modulation Index: %4.1f %%\n", (Vrfmax - Vrfmin)/
               (Vrfmax + Vrfmin) * 85.0);
      }
      continue;
    }

////
    if ((Ch == 'n' || Oldch == 'n' && (Ch == '+' || Ch == '-')) && !ePC_mode)
    {
      max = (ISO_mode)? MAX_NoBl : MAX_NoBl_ICODE1;
      Cont = FALSE;
      if (Oldch == 'n' && Ch != '+' && Ch != '-')
      {
        printf("\nInput Value (1..%d) for Number of Blocks: ", max);
        gets(InputStr);
        if (sscanf(InputStr, "%3d", &i) == EOF)  continue;

        if      (i > max)   NoBl_XS = max;
        else if (i < 1)     NoBl_XS = 1;
        else                NoBl_XS = i;
      }
      else if (Ch == '+')
      {
        if (NoBl_XS < max)  NoBl_XS++;
        else                NoBl_XS = 1;
      }
      else if (Ch == '-')
      {
        if (NoBl_XS > 1)    NoBl_XS--;
        else                NoBl_XS = max;
      }
      if (NoBl_XS < 256)    NoBl = (uchar)NoBl_XS;
      printf("Number of Blocks: NoBl = %d, NoBl_XS = %d\n", NoBl, NoBl_XS);
      continue;
    }

////
    if (Ch == 'N')
    {
      Cont = FALSE;
      CRM_get_info(CRM_GET_NOISE_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI2") != OK)  continue;

      In[0] = *((WORD *)&Data[0]);
      In[1] = *((WORD *)&Data[2]);
      In[2] = *((WORD *)&Data[4]);
      printf("\nInput 3 Values (Min Avg Max, < 1024 mV) for NL (%u %u %u): ",
                                                       In[0], In[1], In[2]);
      gets(InputStr);
      if (sscanf(InputStr, "%d %d %d", &In[0], &In[1], &In[2]) == EOF)  continue;

      if (In[0] > 0 && In[0] < 1024)
      {
        CRM_config(CFG_SET_NOISE_MIN, (uchar)(In[0]/4));
        wait_Eot(0);
        if (get_status(0, "C20") != OK)  continue;
      }
      if (In[1] > 0 && In[1] < 1024)
      {
        CRM_config(CFG_SET_NOISE_AVG, (uchar)(In[1]/4));
        wait_Eot(0);
        if (get_status(0, "C21") != OK)  continue;
      }
      if (In[2] > 0 && In[2] < 1024)
      {
        CRM_config(CFG_SET_NOISE_MAX, (uchar)(In[2]/4));
        wait_Eot(0);
        if (get_status(0, "C22") != OK)  continue;
      }

      CRM_get_info(CRM_GET_NOISE_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI2") != OK)  continue;

      printf("\nNew Values for NL (Min Avg Max): %u %u %u mV\n",
             *((WORD *)&Data[0]), *((WORD *)&Data[2]), *((WORD *)&Data[4]));
      continue;
    }

////
    if (Ch == 'o')
    {
      Cont = FALSE;
      if (ISO_mode)
      {
        CRM_get_info(CRM_GET_SYS_VARS, Data);
        wait_Eot(0);
        if (get_status(0, "GI4") != OK)  continue;

        if (ePC_mode)
        {
          printf("\nInput Value (2..%d..%d, e.g. 8) for PAUSE Pulse Offset (%d): ",
                         22 - Data[13], 27 - Data[13],                    Data[12]);
        }
        else
        {
          printf("\nInput Value (2..%d..%d, e.g. 8) for PAUSE Pulse Offset (%d): ",
                         22 - Data[11], 27 - Data[11],                    Data[10]);
        }
      }
      else
      {
        printf("\nInput Value (2..25, e.g. 15) for PAUSE Pulse Offset: ");
      }
      gets(InputStr);
      if (sscanf(InputStr, "%d", &i) == EOF)  continue;

      if (ISO_mode)
      {
        if (ePC_mode)
        {
          CRM_config(CFG_PULSE_OFFSET_ePC, (uchar)i);
          wait_Eot(0);
          if (get_status(0, "C31") != OK)  continue;
        }
        else
        {
          CRM_config(CFG_PULSE_OFFSET_ISO, (uchar)i);
          wait_Eot(0);
          if (get_status(0, "C27") != OK)  continue;
        }
      }
      else
      {
        CRM_config(CFG_PULSE_OFFSET, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "CF5") != OK)  continue;
      }
      printf("New PAUSE Pulse Offset: %d\n", (uchar)i);
      continue;
    }

////
    if (Ch == 'O' || Oldch == 'O' && (Ch == '+' || Ch == '-'))
    {
      Cont = FALSE;
      if (Ch == '+')
      {
        printf("\nIncrease RF-Power without store ... \n");
        CRM_config(CFG_RF_POWER_NOSTORE, CRM_CFG_EEPOT_UP + 1);
        wait_Eot(0);
        if (get_status(0, "C24") != OK)  continue;
      }
      else if (Ch == '-')
      {
        printf("\nDecrease RF-Power without store ... \n");
        CRM_config(CFG_RF_POWER_NOSTORE, CRM_CFG_EEPOT_DOWN + 1);
        wait_Eot(0);
        if (get_status(0, "C24") != OK)  continue;
      }
      else if (Ch == 'O')
      {
        printf("\nChange RF-Power without store (Press [+] or [-]) ... \n");
      }
      Sleep(150);

      CRM_get_info(CRM_GET_MOD_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI3") != OK)  continue;

      Vrfmax = (float)(*((WORD *)&Data[0])) * VRF_LSB;
      printf("RF-Level: %4.0f mV\n", Vrfmax);
      continue;
    }

////
    if (Ch == 'p')
    {
      Cont = FALSE;
      if (ISO_mode)
      {
        CRM_get_info(CRM_GET_SYS_VARS, Data);
        wait_Eot(0);
        if (get_status(0, "GI4") != OK)  continue;

        if (ePC_mode)
        {
          printf("\nInput Value (2..%d..%d, e.g. 14) for PAUSE Pulse Length (%d): ",
                         22 - Data[12], 27 - Data[12],                    Data[13]);
        }
        else
        {
          printf("\nInput Value (2..%d..%d, e.g. 14) for PAUSE Pulse Length (%d): ",
                         22 - Data[10], 27 - Data[10],                    Data[11]);
        }
      }
      else
      {
        printf("\nInput Value (2..27, e.g. 9) for PAUSE Pulse Length: ");
      }
      gets(InputStr);
      if (sscanf(InputStr, "%d", &i) == EOF)  continue;

      if (ISO_mode)
      {
        if (ePC_mode)
        {
          CRM_config(CFG_PULSE_LENGTH_ePC, (uchar)i);
          wait_Eot(0);
          if (get_status(0, "C32") != OK)  continue;
        }
        else
        {
          CRM_config(CFG_PULSE_LENGTH_ISO, (uchar)i);
          wait_Eot(0);
          if (get_status(0, "C28") != OK)  continue;
        }
      }
      else
      {
        CRM_config(CFG_PULSE_LENGTH, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "CF6") != OK)  continue;
      }
      printf("New PAUSE Pulse Length: %d\n", (uchar)i);
      continue;
    }

////
    if (Ch == 'P' || Oldch == 'P' && (Ch == '+' || Ch == '-'))
    {
      Cont = FALSE;
      if (Ch == '+')
      {
        printf("\nIncrease RF-Power ... \n");
        CRM_config(CFG_RF_POWER, CRM_CFG_EEPOT_UP + 1);
        wait_Eot(0);
        if (get_status(0, "C12") != OK)  continue;
      }
      else if (Ch == '-')
      {
        printf("\nDecrease RF-Power ... \n");
        CRM_config(CFG_RF_POWER, CRM_CFG_EEPOT_DOWN + 1);
        wait_Eot(0);
        if (get_status(0, "C12") != OK)  continue;
      }
      else if (Ch == 'P')
      {
        printf("\nChange RF-Power (Press [+] or [-]) ... \n");
      }
      Sleep(150);

      CRM_get_info(CRM_GET_MOD_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI3") != OK)  continue;

      Vrfmax = (float)(*((WORD *)&Data[0])) * VRF_LSB;
      printf("RF-Level: %4.0f mV\n", Vrfmax);
      continue;
    }

////
    if (Ch == 'q' && !ePC_mode)
    {
      if (ISO_mode)
      {
        printf("\nStay Quiet ...\n\n");

        HL_ISO_cmd(0x02, DataRate | 0x20, 0, 0, UID_Mask, NULL, Data);
      }
      else
      {
        printf("\nReset Quiet Bit ...");
        CRM_reset_QUIET_bit();
        wait_Eot(1);
        if (get_status(0, "RQB") != OK)  continue;

        printf(" OK!\n");
      }
      continue;
    }

////
    if (Ch == 'Q')
    {
      Cont = FALSE;
      CRM_get_info(CRM_GET_NOISE_LEVEL, Data);
      wait_Eot(0);
      if (get_status(0, "GI2") != OK)  continue;

      printf("\nInput Value (1..10) for Bit Threshold Factor (%d): ", Data[6]);
      gets(InputStr);
      if (sscanf(InputStr, "%2d", &i) == EOF)  continue;

      if (i > 10)       i = 10;
      else if (i == 0)  i = 1;

      CRM_config(CFG_BIT_THRESHOLD, (uchar)i);
      wait_Eot(0);
      if (get_status(0, "GI2") != OK)  continue;

      printf("New Bit Threshold Factor: %2d\n", i);
      continue;
    }

////
    if (Ch == 'r' || Oldch == 'r' && (Ch == '+' || Ch == '-'))
    {
      if (ISO_mode)
      {
        if (ePC_mode)
        {
          Cont = FALSE;
          CRM_get_info(CRM_GET_ePC_CRC8_PRESET, Data);
          wait_Eot(0);
          if (get_status(0, "G11") != OK)  continue;

          i = Data[0];
          if (Ch == '+')
          {
            if (i < 0xFF)   i++;
            else            i = 0x00;
          }
          else if (Ch == '-')
          {
            if (i > 0x00)   i--;
            else            i = 0xFF;
          }
          else if (Oldch == 'r')
          {
            printf("\nInput Value (0..FF hex) for CRC8 Preset (%02X hex): ", i);
            gets(InputStr);
            if (sscanf(InputStr, "%2x", &i) == EOF)  continue;
          }
          CRM_config(CFG_ePC_CRC8_PRESET, (uchar)i);
          wait_Eot(0);
          if (get_status(0, "C34") != OK)  continue;

          ePC_CRC8_preset = (uchar)i;
          printf("CRC8 Preset for EPC Command: %02X hex\n", ePC_CRC8_preset);
          continue;
        }
        if (NoBl <= 1)
        {
          printf("\nRead Single Block (Address: %d) ...\n\n", RdAd);

          Data[0] = RdAd;
          if (Option) i = FLAGS + 5;
          else        i = FLAGS + 4;

          HL_ISO_cmd(0x20, DataRate | AdSelAFI | Option, 1, i, UID_Mask, Data, Data);
        }
        else
        {
          printf("\nRead Multiple (%d) Blocks (Address: %d) ...\n\n", NoBl, RdAd);

          Data[0] = RdAd;
          Data[1] = (uchar)(NoBl - 1);
          if (Option) i = FLAGS + 5 * NoBl;
          else        i = FLAGS + 4 * NoBl;

          HL_ISO_cmd(0x23, DataRate | AdSelAFI | Option, 2, i, UID_Mask, Data, Data);
        }
      }
      else
      {
        printf("\nSelected Read ... \n");

        CRM_read(RdAd, NoBl, Data);
        wait_Eot(1);
        if (get_status(0, "RD ") != OK)  continue;

        show_read_data(CRM_get_timeslots(), NoBl, Data);
      }
      continue;
    }

////
    if (Ch == 'R')
    {
      printf("\nReset NL Mode ...\n\n");

      CRM_config(CFG_NOISELEVEL_MODE, 0);
      wait_Eot(0);
      if (get_status(0, "C19") != OK)  continue;

      printf("OK\n");
      continue;
    }

////
    if (Ch == 's' && !ePC_mode)
    {
      if (ISO_mode)
      {
        printf("\nSelect ...\n\n");

        HL_ISO_cmd(0x25, DataRate | 0x20, 0, FLAGS, UID_Mask, NULL, Data);
      }
      else
      {
        printf("\nShow Serial Numbers (LSB first) ... \n");

        if (tmpTse > TSE_256)  tmpTse = TSE_256;

        HL_read_unselected(Hash, tmpTse, 0, 2, Data);
      }
      continue;
    }

//////

	if (Ch == 's' && ePC_mode)
    {
      
          printf("BEGIN ROUND "
                 "(using %d most significant Bits of EPC-Mask u) ...\n", Masklen);

          Data[0] = (uchar)Masklen;
          Data[1] = (uchar)((tmpTse)? (1 << tmpTse) - 1 : 0);
          Data[2] = Hash;
          parlen = (ePC_Hashmode)? 2 : 3;
          rxlen = ePC_Bytes[ePC_Type] - Masklen/8 + CRC16;
          if (rxlen < CRC16)
          {
            printf("\nMasklen (%d) larger than number of EPC bits (%d: %s)!\n",
                             Masklen, 8 * ePC_Bytes[ePC_Type], ePC_Types[ePC_Type]);
            printf("(Change Masklen [k] or IúCODE OTP Type [T])!\n\n");
            continue;
          }
		  i = HL_ePC_cmd(OTP_BEGIN_ROUND, parlen, rxlen, ePC_Mask, Data, Data);

          if (tmpTse == 0 && i > 0 && Data[0] == OK)   // Beep only at 1 Timeslot
          {
            Beep(4000, 3);
          }
          
        
        printf("\nInventory ...\n\n");

        if (Slots > MAX_ISO_SLOTS)    Slots = MAX_ISO_SLOTS;

        HL_ISO_inventory(0x01, DataRate | AdSelAFI | Option, Slots,
                               AFI_DSFID, Masklen, 0, 0, UID_Mask, Data);
		continue;
      }
      
      
    
////
////
    if (Ch == 'S')
    {
      printf("Input Value (0..F hex) for Output Port (%02X hex): ", Outbyte);
      gets(InputStr);
      if (sscanf(InputStr, "%x", &i) == EOF)  continue;

      Outbyte = (uchar)i;

      CRM_set_port(Outbyte);
      wait_Eot(0);
      if (get_status(0, "SOP") != OK)  continue;

      printf("New Output Port: %02X hex\n", Outbyte);
      continue;
    }

////
    if (Ch == 't' || Oldch == 't' && (Ch == '+' || Ch == '-'))
    {
      Cont = FALSE;
      if (ISO_mode && !ePC_mode)
      {
        if (Oldch == 't' && Ch == 't')
        {
          if (Slots == 1)             Slots = MAX_ISO_SLOTS;
          else                        Slots = 1;
        }
        else if (Ch == '+')
        {
          if (Slots < MAX_ISO_SLOTS)  Slots++;
          else                        Slots = 1;
        }
        else if (Ch == '-')
        {
          if (Slots > 1)              Slots--;
          else                        Slots = MAX_ISO_SLOTS;
        }
        CRM_get_info(CRM_GET_ISO_15693, Data);
        wait_Eot(0);
        if (get_status(0, "GI8") != OK)  continue;
        i = Data[0] & 0x0F;
        if (Slots > 1 && Slots < MAX_ISO_SLOTS)  i |= (MAX_ISO_SLOTS - Slots) << 4;
        CRM_config(CFG_ISO_15693, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C26") != OK)  continue;

        printf("Inventory Timeslot(s): %d\n", Slots);
      }
      else
      {
        max = (ePC_mode)? TSE_512 : TSE_256;
        if (Ch == '+' || Oldch == 't' && Ch == 't')
        {
          if (tmpTse < max)   tmpTse++;
          else                tmpTse = TSE_1;
        }
        else if (Ch == '-')
        {
          if (tmpTse > TSE_1) tmpTse--;
          else                tmpTse = (uchar)max;
        }
        printf("Timeslots: %3d\n", (tmpTse == TSE_1)? 1 : 2 << tmpTse);
      }
      continue;
    }

////
    if (Ch == 'T' || Oldch == 'T' && (Ch == '+' || Ch == '-'))
    {
      if (ISO_mode)
      {
        Cont = FALSE;
        if (ePC_mode)
        {
          i = ePC_Type;
          j = FALSE;
          if (Ch == 'T' && Oldch == 'T' || Ch == '+')
          {
            ePC_Type++;
            if (ePC_Type > MAX_EPC_TYPE)    ePC_Type = 0;
            if (ePC_Type == TYPE_UID2 && Ch == '+')
            {
              for (k = 0; k < ePC_Bytes[ePC_Type]; k++)
              {
                ePC_Mask[ePC_Bytes[ePC_Type] - 1 - k] = UID2_Data[k];
              }
            }
            j = TRUE;
          }
          else if (Ch == '-')
          {
            ePC_Type--;
            if (ePC_Type < 0)               ePC_Type = MAX_EPC_TYPE;
            j = TRUE;
          }
          CRM_config(CFG_ePC_BYTES, (uchar)ePC_Bytes[ePC_Type]);
          wait_Eot(0);
          if (get_status(0, "C35") != OK)   ePC_Type = i;
    
          printf("\nIúCODE OTP Type: %-6s ==>  ", ePC_Types[ePC_Type]);
    
          switch (ePC_Type)
          {
            case TYPE_UID1:
            case TYPE_UID1A:
              i = CRC8_PRESET_UID1_1A;
              if (j)                        ePC_Hashmode = 1;
              break;
            case TYPE_UID2:
              i = CRC8_PRESET_UID2;
              if (j)                        ePC_Hashmode = 1;
              break;
            default:
              i = CRC8_PRESET_EPC;
              if (j)                        ePC_Hashmode = 0;
          }
          CRM_config(CFG_ePC_CRC8_PRESET, (uchar)i);
          wait_Eot(0);
          if (get_status(0, "C34") != OK)  continue;
    
          ePC_CRC8_preset = (uchar)i;
          printf("CRC8 Preset for EPC Command: %02X hex\n", ePC_CRC8_preset);
    
          printf("\nHashvalue at BEGIN ROUND command will be %s!\n\n",
                 (ePC_Hashmode)? "calculated in the chip" :
                                 "provided as command parameter");
          continue;
        }
        CRM_get_info(CRM_GET_RX_DLY_OFFS, Data);
        wait_Eot(0);
        if (get_status(0, "G13") != OK)  continue;

        printf("\nRX Delay Offset: %d (%.2f æs = [RX Delay Offset - 4] * 18.88 æs)\n\n"
               " 0 => -75.52 æs, 1 => -56.64 æs, 2 => -37.76 æs, 3 => -18.88 æs,\n"
               " 4 =>    +-0 æs, 5 => +18.88 æs, 6 => +37.76 æs, ..., 255 => +4738.64 æs\n",
                                         Data[0], (Data[0] - 4)*C1888);
        printf("\nInput new Value (0..255, e.g. 4) for RX Delay Offset (%d): ", Data[0]);
        gets(InputStr);
        if (sscanf(InputStr, "%d", &i) == EOF)  continue;

        CRM_config(CFG_RX_DLY_OFFS, (uchar)i);
        wait_Eot(0);
        if (get_status(0, "C36") != OK)  continue;

        CRM_get_info(CRM_GET_RX_DLY_OFFS, Data);
        wait_Eot(0);
        if (get_status(0, "G13") != OK)  continue;

        printf("New RX Delay Offset: (%d - 4) * 18.88 æs = %.2f æs\n\n",
                                      Data[0],             (Data[0] - 4)*C1888);
        continue;
      }
      else if (!ISO_mode)
      {
        CRM_config(CFG_INIT_TS_TABLE, 0);
        wait_Eot(0);
        if (get_status(0, "C17") != OK)  continue;

        printf("All timeslots released (TS Table cleared)!\n");
        continue;
      }
    }

////
    if (Ch == 'u' || Oldch == 'u' && (Ch == '+' || Ch == '-' || Ch == '*'))
    {
      if (ISO_mode)
      {
        Cont = FALSE;
        if (ePC_mode)
        {
          if (ePC_Bytes[ePC_Type] > 19)  continue;

          if (Oldch == 'u' && Ch != '+' && Ch != '-' && Ch != '*')
          {
            printf("\nInput EPC/Mask (MSB..LSB, e.g. ");
            for (i = ePC_Bytes[ePC_Type] - 1; i >= 0; i--)          printf("%02X ", i);
            printf("hex):\n                               ");
            gets(InputStr);
            j = sscanf(InputStr, "%2x%2x%2x%2x%2x%2x%2x%2x%2x%2x%2x%2x"
                                 "%2x%2x%2x%2x%2x%2x%2x",
                       &In[18], &In[17], &In[16], &In[15], &In[14], &In[13], &In[12],
                       &In[11], &In[10], &In[9],  &In[8],  &In[7],  &In[6],
                       &In[5],  &In[4],  &In[3],  &In[2],  &In[1],  &In[0]);
            if (j > ePC_Bytes[ePC_Type])   j = ePC_Bytes[ePC_Type];
            for (i = 0; i < j; i++)
            {
              ePC_Mask[ePC_Bytes[ePC_Type] - 1 - i] = (uchar)In[18 - i];
            }
          }
          else if (Ch == '+')
          {
            (*(ulong *)ePC_Mask)++;
          }
          else if (Ch == '-')
          {
            (*(ulong *)ePC_Mask)--;
          }
          printf("\nEPC/Mask Data (MSB..LSB): ");
          for (i = ePC_Bytes[ePC_Type] - 1; i >= 0; i--)  printf("%02X ", ePC_Mask[i]);
          printf("(%Lu)\n", *(ulong *)ePC_Mask);
        }
        else
        {
          if (Oldch == 'u' && Ch != '+' && Ch != '-' && Ch != '*')
          {
            printf("\nInput UID/Mask (LSB..MSB, ");
            for (i = 0; i < UID; i++) printf("%02X ", UID_Mask[i]);
            printf("hex):\n                          ");
            gets(InputStr);
            j = sscanf(InputStr, "%2x%2x%2x%2x%2x%2x%2x%2x",
                     &In[0], &In[1], &In[2], &In[3], &In[4], &In[5], &In[6], &In[7]);
            for (i = 0; i < j && i < UID; i++)
            {
              UID_Mask[i] = UID_Mask2[i] = (uchar)In[i];
            }
          }
          else if (Ch == '+')
          {
            (*(ulong *)UID_Mask)++;
            (*(ulong *)UID_Mask2)++;
          }
          else if (Ch == '-')
          {
            (*(ulong *)UID_Mask)--;
            (*(ulong *)UID_Mask2)--;
          }
          else if (Ch == '*')
          {
            memcpy(UID_Mask, UID_Mask2, 8);
          }
          printf("\nUID/Mask Data (LSB..MSB): ");
          for (i = 0; i < UID; i++)                       printf("%02X ", UID_Mask[i]);
          printf("(%Lu)\n", *(ulong *)UID_Mask);
          printf("UID for SLI-S (LSB..MSB): ");
          for (i = 0; i < UID; i++)                       printf("%02X ", UID_Mask2[i]);
          printf("(%Lu), used only at [J] Init.\n", *(ulong *)UID_Mask2);
        }
      }
      else
      {
        printf("\nUnselected Read ... \n");

        if (tmpTse > TSE_256)  tmpTse = TSE_256;
        CRM_read_unselected(Hash, tmpTse, RdAd, NoBl, Data);
        wait_Eot(1);
        if (get_status(0, "RDU") != OK)  continue;

        show_read_data((tmpTse == TSE_1)? 1 : 2 << tmpTse, NoBl, Data);
      }
      continue;
    }

////
    if (Ch == 'U')
    {
      Cont = FALSE;
      printf("\nStore current RF-Power ... \n");
      CRM_config(CFG_RF_POWER, 0);
      wait_Eot(0);
      if (get_status(0, "C12") != OK)  continue;

      printf("OK\n");
      continue;
    }

////
    if (ISO_mode && !ePC_mode)
    {
      if (Ch == 'v')
      {
        printf("\nVreg OFF ...\n\n");

        HL_ISO_cmd(0xF5, DataRate | AdSelAFI, 0, FLAGS, UID_Mask, NULL, Data);
        continue;
      }
    }
    else
    {
      if (Ch == 'v' || Oldch == 'v' && (Ch == '+' || Ch == '-'))
      {
        Cont = FALSE;
        max = (ePC_mode)? (ePC_Bytes[ePC_Type] + CRC16 - 1) : 31;
        if (Oldch == 'v' && Ch != '+' && Ch != '-')
        {
          printf("\nInput Value (0..%d) for Hashvalue: ", max);
          gets(InputStr);
          if (sscanf(InputStr, "%2d", &i) == EOF)  continue;

          if (i <= max)   Hash = (uchar)i;
          else            Hash = (uchar)max;
        }
        else if (Ch == '+')
        {
          if (Hash < max) Hash++;
          else            Hash = 0;
        }
        else if (Ch == '-')
        {
          if (Hash > 0)   Hash--;
          else            Hash = (uchar)max;
        }
        printf("Hashvalue: %2d\n", Hash);
        continue;
      }
    }

////
    if (Ch == 'V' && ISO_mode)
    {
      Cont = FALSE;
      if (ePC_mode)
      {
        if (Oldch == 'V')
        {
          ePC_Hashmode = (ePC_Hashmode)? 0 : 1;
        }
        printf("\nHashvalue at BEGIN ROUND command will be %s!\n\n",
               (ePC_Hashmode)? "calculated in the chip" :
                               "provided as command parameter");
        continue;
      }
      printf("\nInput Value (0..FF hex) for AFI/DSFID/Password ID (%02X hex): ", AFI_DSFID);
      gets(InputStr);
      if (sscanf(InputStr, "%2x", &i) == EOF)  continue;

      AFI_DSFID = (uchar)i;
      printf("New AFI/DSFID/Password ID Value: %02X hex\n", AFI_DSFID);
      continue;
    }

////
    if (Ch == 'w')
    {
      if (ISO_mode)
      {
        if (ePC_mode)
        {
          printf("\nWrite Byte z to Address y ...\n\n");

          Data[0] = WrAd;
          Data[1] = WriteData[0];
          parlen  = 2;
          rxlen   = 0;

          HL_ePC_cmd(OTP_WRITE, parlen, rxlen, NULL, Data, Data);
          continue;
        }
        printf("\nWrite Single Block ...\n\n");

        Data[0] = WrAd;
        memcpy(&Data[1], WriteData, 4);

        HL_ISO_cmd(0x21, DataRate | AdSelAFI | Option, 5, FLAGS, UID_Mask,Data,Data);
      }
      else
      {
        printf("\nWrite (selected labels) ...\n");

        HL_write(Hash, WrAd, WriteData, Data, Data);
        i = CRM_get_datalen();
        if (get_status(0, "WRT") != OK)  continue;

        show_data(i, Data);
      }
      continue;
    }

////
    if (Ch == 'W')
    {
      if (ISO_mode && !ePC_mode)
      {
        printf("\nWrite ALL (ECNTRL2: %02X hex) ...\n\n", EE_control);

        Data[0] = WrAd;
        Data[1] = EE_control;
        memcpy(&Data[2], WriteData, 4);

        HL_ISO_cmd(0xF2, DataRate | AdSelAFI | Option, 6, FLAGS, UID_Mask,Data,Data);
      }
      else
      {
        Cont = FALSE;
        CRM_get_info(CRM_GET_IGNORE_WEAK_COLLISION, Data);
        wait_Eot(0);
        if (get_status(0, "GI7") != OK)  continue;

        if (Oldch == 'W')
        {
          if (Data[0] == 0) i = 1;
          else              i = 0;
          CRM_config(CFG_IGNORE_WEAK_COLLISION, (uchar)i);
          wait_Eot(0);
          if (get_status(0, "C25") != OK)  continue;
          Data[0] = (uchar)i;
        }
        printf("Ignore WEAK COLLISION Mode is switched %s!\n", (Data[0])? "ON" : "OFF");
      }
      continue;
    }

////
    if (Ch == 'x' || Oldch == 'x' && (Ch == '+' || Ch == '-'))
    {
      Cont = FALSE;
      if (ISO_mode && ePC_mode)
      {
        printf("\nInput Value (0..FF hex) for MUX (%02X hex): ", ePC_MUX_value);
        gets(InputStr);
        if (sscanf(InputStr, "%2x", &i) == EOF)  continue;

        ePC_MUX_value = (uchar)(i & 0xFF);
        printf("New MUX Value:  %02X hex\n\n", ePC_MUX_value);

        Data[0] = ePC_MUX_value;
        parlen = 1;
        rxlen  = 1;

        HL_ePC_cmd(OTP_SETMUX, parlen, rxlen, ePC_Mask, Data, Data);
        continue;
      }
      max = (ISO_mode)? MAX_BlAd : MAX_BlAd_ICODE1;
      if (Oldch == 'x' && Ch != '+' && Ch != '-')
      {
        printf("\nInput Value (0..%d) for Read Address: ", max);
        gets(InputStr);
        if (sscanf(InputStr, "%3d", &i) == EOF)  continue;

        if      (i > max)   RdAd = (uchar)max;
        else if (i < 0)     RdAd = 0;
        else                RdAd = (uchar)i;
      }
      else if (Ch == '+')
      {
        if (RdAd < max)     RdAd++;
        else                RdAd = 0;
      }
      else if (Ch == '-')
      {
        if (RdAd > 0)       RdAd--;
        else                RdAd = (uchar)max;
      }
      printf("Read Block Address: %3d\n", RdAd);
      continue;
    }

////
    if (Ch == 'X' && ISO_mode)
    {
      Cont = FALSE;
      if (ePC_mode)
      {
        CRM_get_info(CRM_GET_ePC_TESTMODE, Data);
        wait_Eot(0);
        if (get_status(0, "G10") != OK)  continue;

        printf("\nInput Value (0..FF hex) for EPC Testmode (%02X hex): ", Data[0]);
        gets(InputStr);
        if (sscanf(InputStr, "%2x", &i) == EOF)  continue;

        ePC_Testmode = (uchar)i;
        CRM_config(CFG_ePC_TESTMODE, (uchar)ePC_Testmode);
        wait_Eot(0);
        if (get_status(0, "C33") != OK)  continue;

        printf("New EPC Testmode: %02X hex\n", (uchar)ePC_Testmode);
        continue;
      }
      printf("\nInput Value (0..FF hex) for MUX (%02X hex): ", MUX_value);
      gets(InputStr);
      if (sscanf(InputStr, "%2x", &i) == EOF)  continue;

      MUX_value = (uchar)(i & 0xFF);
      printf("New MUX Value: %02X hex\n", MUX_value);
              // Set MUX
      Data[0] = (uchar)(MUX_value);

      HL_ISO_cmd(0xF3, DataRate | AdSelAFI | Option, 1, FLAGS, UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == 'y' || Oldch == 'y' && (Ch == '+' || Ch == '-'))
    {
      max = (ISO_mode)? MAX_BlAd : MAX_BlAd_ICODE1;
      Cont = FALSE;
      if (Oldch == 'y' && Ch != '+' && Ch != '-')
      {
        printf("\nInput Value (0..%d) for Write Address (%d): ", max, WrAd);
        gets(InputStr);
        if (sscanf(InputStr, "%3d", &i) == EOF)  continue;

        if      (i > max)   WrAd = (uchar)max;
        else if (i < 0)     WrAd = 0;
        else                WrAd = (uchar)i;
      }
      else if (Ch == '+')
      {
        if (WrAd < max)     WrAd++;
        else                WrAd = 0;
      }
      else if (Ch == '-')
      {
        if (WrAd > 0)       WrAd--;
        else                WrAd = (uchar)max;
      }
      printf("Write Block Address: %3d\n", WrAd);
      continue;
    }

////
    if (Ch == 'Y' && ISO_mode)
    {
      if (ePC_mode)
      {
        printf("\nWrite ALL (Write Address y, Data z, ECNTRL2 E) ...\n\n");

        Data[0] = WrAd;
        Data[1] = WriteData[0]; // LSB
        Data[2] = WriteData[1]; //  :
        Data[3] = WriteData[2]; //  :
        Data[4] = WriteData[3]; // MSB
        Data[5] = EE_control;
        parlen = 6;
        rxlen  = 1;

        HL_ePC_cmd(OTP_WRITE_ALL, parlen, rxlen, ePC_Mask, Data, Data);
        continue;
      }
      printf("\nReset to Ready ...\n\n");

      HL_ISO_cmd(0x26, DataRate | AdSelAFI, 0, FLAGS, UID_Mask, NULL, Data);
      continue;
    }

////
    if (Ch == 'z' || Oldch == 'z' && (Ch == '+' || Ch == '-'))
    {
      Cont = FALSE;
      if (Oldch == 'z' && Ch != '+' && Ch != '-')
      {
        printf("\nInput Write Data (LSB..MSB, e.g. 08 15 47 11 hex): ");
        gets(InputStr);
        j = sscanf(InputStr, "%2x%2x%2x%2x", &In[0], &In[1], &In[2], &In[3]);
        for (i = 0; i < j && i < 4; i++)
        {
          WriteData[i] = (uchar)In[i];
        }
      }
      else if (Ch == '+')
      {
        (*(ulong *)WriteData)++;
      }
      else if (Ch == '-')
      {
        (*(ulong *)WriteData)--;
      }
      printf("\nWrite Data (LSB..MSB): %02X %02X %02X %02X (%Lu)\n",
                WriteData[0], WriteData[1], WriteData[2], WriteData[3],
                *(ulong *)WriteData);
      continue;
    }

////
    if (Ch == 'Z' && ISO_mode)
    {
      if (ePC_mode)
      {
        printf("\nDestroy Label (using 96-Bit-EPC u and 3-Byte-Password z) ...\n\n");

        Data[0] = WriteData[0]; // 24 bit password (LSB)
        Data[1] = WriteData[1]; //                   : 
        Data[2] = WriteData[2]; //       -"-       (MSB)
        parlen  = 3;
        rxlen   = 0;

        HL_ePC_cmd(OTP_DESTROY, parlen, rxlen, ePC_Mask, Data, Data);
        continue;
      }
      printf("\nIDDQ Test (ECNTRL2: %02X hex) ...\n\n", EE_control);

      Data[0] = RdAd;
      Data[1] = EE_control;
      memcpy(&Data[2], WriteData, 4);

      HL_ISO_cmd(0xF4, DataRate | AdSelAFI, 6, 0, UID_Mask, Data, Data);
      continue;
    }

////
    if (Ch == ' ' || Ch == ',')
    {
      Cont = TRUE;
      Ch = Oldch;
      _clrscr(&Console_x, &Console_y);
      _gotoxy(Console_x - 1, 1); putch(Oldch); _gotoxy(1, 1);
      continue;
    }

//// other

    Cont = FALSE;
    _clrscr(&Console_x, &Console_y);
    if (ISO_reader && ISO_mode)
    {
      if (ePC_mode)
      {
        printf(epc_menustr, ComX, CRM_get_baud(), (tmpTse == TSE_1)? 1 : 2 << tmpTse,
               CRM_Reader_Version);
      }
      else
      {
        printf(iso_menustr, ComX, CRM_get_baud(), Slots, CRM_Reader_Version);
      }
    }
    else
    {
      printf(menustr, ComX, CRM_get_baud(), CRM_get_timeslots(), CRM_Reader_Version);
    }
    Cnt      = 1L;
    MaxEas   = 0;
    MinMinNL = 4095;
    MaxMinNL = 0;
    MinAvgNL = 4095;
    MaxAvgNL = 0;
    MinMaxNL = 4095;
    MaxMaxNL = 0;
    RndNrSum = 0.0;
    RndNrCnt = 1L;
    RndNrMax = 0x0000;
    RndNrMin = 0xFFFF;
  }

  CRM_exit();
  
  if (Lfp != NULL)
  {
    _strdate(DateBuf);
    _strtime(TimeBuf);
    fprintf(Lfp, " Stop logging of BEGIN ROUND (%s %s) ...\n", DateBuf,TimeBuf);
    fclose(Lfp);
    Lfp = NULL;
  }

  return(0);
}
#pragma warning(default:4127)  // conditional expression is constant
/***************************************************************************/

static void HL_anticoll_select(uchar hash, uchar tse, uchar *resp)
{
  int  i, j;
  char *errtxt;

  CRM_anticoll_select(hash, tse, resp);
  wait_Eot(1);
  if (get_status(0, "ACS") != OK)  return;

  printf("\n");
  for (i = 0; i < (int)CRM_get_timeslots(); i++)
  {
    memcpy(Tag[i], (resp + 8 * i), 8);

    printf("Timeslot %3d: ", i);

    if (memcmp(&Tag[i][1], "\0\0\0\0\0\0\0", 7) == OK &&
                Tag[i][0] > 0x00 && Tag[i][0] <= 0x0F)
    {
      CRM_get_error_text(Tag[i][0], &errtxt);
      printf("%63s\n", errtxt);
    }
    else
    {
      for (j = 0; j < 8; j++)
      {
        printf("%02X ", Tag[i][j]);
      }
      printf("%15s\n", " ");
    }
    if (!Cont && i % (Lines - 2) == (Lines - 3) && i < (int)CRM_get_timeslots() - 1)
    {
      printf("Continue ...\r");

      switch (getch())
      {
        case 0x1B:  // Esc
          printf("Break!      \n");
          return;

        case 0x00:
          getch();
          break;
      }
    }
  }
}
/***************************************************************************/

static void HL_write(uchar hash, uchar blnr, uchar *data,
                     uchar *tsl, uchar *resp)
{
  int i;

  for (i = 0; i < 32; i++)
  {
    tsl[i] = 0xFF;
  }
  CRM_write(hash, blnr, data, tsl, resp);
  wait_Eot(1);
}
/***************************************************************************/

static void HL_read_unselected(uchar hash, uchar tse, uchar blnr, uchar nobl,
                               uchar *resp)
{
  int i, ts, n, byte1;

  CRM_read_unselected(hash, tse, blnr, nobl, resp);
  wait_Eot(1);
  if (get_status(0, "RDU") != OK)  return;

  printf("\n");
  n = 0;
  ts = (tse == TSE_1)? 1 : 2 << tse;

  for (i = 0; i < ts; i++)
  {
    if (resp[i] == OK || resp[i] == CRM_WEAK_COLLISION)
    {
      n++;

      printf("Timeslot %2d: ", (i + 1));

      for (byte1 = 0; byte1 < 4 * nobl; byte1++)
      {
        printf("%02X ", resp[ts + 8 * i + byte1]);
      }
      printf("Hex \n");
    }
  }
  for (i = 0; i < (ts - n); i++)
  {
    {
      printf("%40s\n", " ");
    }
  }
}
/***************************************************************************/

static int HL_ePC_cmd(int cmd_code, int parlen, int rxlen, uchar *epc_mask,
                      uchar *par, uchar *rxdata)
{
  int i, j, k, txlen, slots, resplen, status, columns;
  int ts_idx, status_idx, byte_idx, epcbyte, LR_type;
  uchar txdata[30], crc8;
  char *errtxt;
  static int masklen = 0;

  if (parlen > 28)
  {
    printf("Max. number of parameter bytes (28) exceeded!\n");
    return (-1);
  }

  printf("Command Code:     %02X hex\n", cmd_code);
  txdata[0] = (uchar)cmd_code;
  txlen = CMD;
  slots = 1;
  LR_type = TYPE_EAS;

  switch (cmd_code)
  {
    case OTP_WRITE:
      printf("Block Number:     %2d \n",     par[0]);
      printf("Data Byte:        %02X hex\n", par[1]);
      txdata[txlen++] = par[0];
      txdata[txlen++] = par[1];
      break;
  
    case OTP_DESTROY:
      printf("EPC:              ");
      for (i = ePC_Bytes[ePC_Type] - 1; i >= 0; i--)
      {
        printf("%02X ",   epc_mask[i]);
        txdata[txlen++] = epc_mask[i];
      }
      printf("hex (MSB..LSB)\n");
      printf("Password:         ");
      for (i = 2; i >= 0; i--)
      {
        printf("%02X ",   par[i]);
        txdata[txlen++] = par[i];
      }
      printf("hex (MSB..LSB)\n");
      break;
  
    case OTP_BEGIN_ROUND:
      masklen = par[0];
      printf("Mask Length:      %2d \n", masklen);
      txdata[txlen++] = (uchar)masklen;
      j = (masklen + 7)/8;
      if (j > 0 && j <= ePC_Bytes[ePC_Type])
      {
        printf("Mask:             ");
        for (i = ePC_Bytes[ePC_Type] - 1; i > ePC_Bytes[ePC_Type] - j; i--)
        {
          printf("%02X ",   epc_mask[i]);
          txdata[txlen++] = epc_mask[i];
        }
        k = masklen % 8;
        k = (k)? 0xFF << (8 - k) : 0xFF;
        txdata[txlen] = (uchar)(epc_mask[i] & k);
        printf("%02X hex ", txdata[txlen]);
        txlen++;
        if (j > 1) printf("(MSB..LSB)");
        printf("\n");
      }
      if (par[1])
      {
        slots = (par[1] + 1) << 1;
      }
      printf("Number of Slots: %3d ('Number of Slots'-Mask: %02X hex)\n", slots, par[1]);
      txdata[txlen++] = par[1];
      if (parlen > 2)
      {
        printf("Hashvalue:        %2d \n", par[2]);
        txdata[txlen++] = par[2];
      }
      printf("Response Length: %3d byte(s) expected (nominal, incl. CRC16)\n", rxlen);
      break;

    case LR_CMD:
      printf("Data Selector:    %02X hex\n", par[0]);
      txdata[txlen++] = par[0];
      if (par[0] != 0x00)  // Data Selector: UID or EPC
      {
        switch (par[1])
        {
//        case 0x10: slots =   1; break;
          case 0x20: slots =   4; break;
          case 0x40: slots =   8; break;
          case 0x80: slots =  16; break;
          case 0x00: slots =  32; break;
          case 0x01: slots =  64; break;
          case 0x02: slots = 128; break;
          case 0x04: slots = 256; break;
          case 0x08: slots = 512; break;
        }
        LR_type = (par[0] == 0x01)? TYPE_UID : TYPE_EPC;
        printf("Number of Slots: %3d ('Number of Slots'-Mask: %02X hex)\n", slots, par[1]);
      }
      else
      {
        LR_type = TYPE_EAS;
        printf("Number of Slots:  ignored ('Number of Slots'-Mask: %02X hex)\n", par[1]);
      }
      txdata[txlen++] = par[1];
      printf("XOR (Checkbyte):  %02X hex\n", par[2]);
      txdata[txlen++] = par[2];
      printf("Response Length: %3d byte(s) expected (nominal, incl. CRC16)\n", rxlen);
      break;
  
    case OTP_CHECK_PWD:
      printf("Manufact. Code:   %02X hex\n", par[0]);
      txdata[txlen++] = par[0];
      printf("Password:         ");
      for (i = 3; i >= 1; i--)
      {
        printf("%02X ",   par[i]);
        txdata[txlen++] = par[i];
      }
      printf("hex (MSB..LSB)\n");
      break;
  
    case OTP_READ_ALL:
      printf("Block Number:     %2d \n",     par[0]);
      txdata[txlen++] = par[0];
      printf("ECNTRL2:          %02X hex\n", par[1]);
      txdata[txlen++] = par[1];
      printf("Response Length: %3d byte(s) expected (nominal)\n", rxlen);
      break;
  
    case OTP_WRITE_ALL:
    case OTP_IDDQ:
      printf("Block Number:     %2d \n",     par[0]);
      txdata[txlen++] = par[0];
      printf("Data Bytes:       ");
      for (i = 4; i >= 1; i--)
      {
        printf("%02X ",   par[i]);
        txdata[txlen++] = par[i];
      }
      printf("hex (MSB..LSB)\n");
      printf("ECNTRL2:          %02X hex\n", par[5]);
      txdata[txlen++] = par[5];
      break;
  
    case OTP_SETMUX:
      printf("MUX Byte:         %02X hex\n", par[0]);
      txdata[txlen++] = par[0];
      break;

    default:
      printf("Parameter Bytes:  ");
      for (i = 0; i < parlen; i++)
      {
        printf("%02X ",   par[i]);
        txdata[txlen++] = par[i];
      }
      printf("hex\n");
      printf("Response Length: %3d byte(s) expected (nominal)\n", rxlen);
  }

  CRM_test(CMD_EPC, (uchar)txlen, (uchar)rxlen, txdata, rxdata);

  wait_Eot(1);
  resplen = CRM_get_datalen();
  if (get_status(0, "EPC") != OK)
  {
    return (-2);
  }

#ifdef USE_DEBUG_OUTPUT
  show_data(resplen, rxdata);
#endif
  
  if (resplen < STAT)
  {
    printf("\n%-60s\n\n", "No Statusbyte from Reader (Wrong Firmware Version)?");
    return (-3);
  }

  if (rxlen == 0)
  {
    return (resplen);
  }

  if (cmd_code == OTP_BEGIN_ROUND ||
      cmd_code == OTP_CHECK_PWD   ||
      cmd_code == OTP_WRITE_ALL   ||
      cmd_code == OTP_IDDQ        ||
      cmd_code == OTP_SETMUX)
  {
    if (resplen <= STAT)
    {
      printf("\n%-60s\n\n", "No Response from Label!");
      return (0);
    }
    resplen--;
    printf("\nStatus of Slot F: %02X hex ", rxdata[resplen]);
    if (rxdata[resplen])  printf("==> Label(s) with Response at Slot F detected <==\n");
    else                  printf("==> No Response at Slot F <==                    \n");
    if (cmd_code != OTP_BEGIN_ROUND)
    {
      return (resplen);
    }
  }
  else
  {
    if (resplen <= STAT)
    {
      printf("\n%-60s\n\n", "No Response from Label!");
      for (i = 0; i < 14; i++)   printf("%70s\n", " ");
      return (0);
    }
    if (cmd_code != LR_CMD)
    {
      resplen--;
      printf("\nResponse Length:%4d byte(s) received   \n", resplen);

      if (cmd_code != OTP_READ_ALL)
      {
        status = Data[0];
        CRM_get_error_text(status, &errtxt);
        printf("\nStatus:           %-14s\n", errtxt);
      }
    }
    else
    {
      if (LR_type == TYPE_EAS)
      {
        resplen--;
        printf("\nResponse Length:%4d byte(s) received   \n", resplen);
        printf("\nResponse Data:    ");
        for (i = STAT; i < (resplen - 1); i++)
        {
          printf("%02X ", Data[i]);
          if (!(i % BPL) && (i < resplen - 2))  printf("hex\n                  ");
        }
        printf("hex%48s\n", " ");
        printf("CRC16 (MSB LSB):  %02X %02X hex\n", Data[resplen - 1], Data[resplen]);
        status = Data[0];
        CRM_get_error_text(status, &errtxt);
        printf("\nStatus:           %-14s\n", errtxt);
        Beep(8000, 1);
      }
      else
      {
        resplen -= slots * STAT;
        printf("\nResponse Length:%4d byte(s) @ %d timeslot(s) received ", resplen, slots);
        if (resplen/rxlen > 1)       printf("(response: %3d slots)", resplen/rxlen);
        else if (resplen == rxlen)   printf("(response:   1 slot) ");
        else                         printf("(no response)        ");
        printf("   \n\n");
      
        if (rxlen >= CRC16 && rxlen <= ePC_Bytes[TYPE_EPC] + CRC16)
        {
          i = 57 + 3*(ePC_Bytes[LR_type] - UID);
          if (Console_x > i && slots > 64) columns = Console_x/i;
          else                             columns = 1;
        
          for (i = 1; i < columns && i < slots; i++)
          {
            printf("SLOT %s%02d.......", ePC_Types[LR_type], ePC_Bytes[LR_type] - 1);
            for (j = 0; j < ePC_Bytes[LR_type] - UID; j++)  printf("...");
            printf("......%s00 CRC16 STATUS   ", ePC_Types[LR_type]);
          }
          printf("SLOT %s%02d.......", ePC_Types[LR_type], ePC_Bytes[LR_type] - 1);
          for (j = 0; j < ePC_Bytes[LR_type] - UID; j++)  printf("...");
          printf("......%s00 CRC16 STATUS\n\n", ePC_Types[LR_type]);
          status_idx = 0;
        
          for (ts_idx = 0; ts_idx < slots; ts_idx++)
          {
            status = rxdata[status_idx];
            printf(" %3d ", ts_idx);
            for (byte_idx = 0; byte_idx < rxlen; byte_idx++)
            {
              epcbyte = *(rxdata + status_idx + STAT + byte_idx);
              if (status == CRM_NO_TAG)       printf("   ");
              else                            printf("%02X ", epcbyte);
            }
            switch (status)
            {
              case OK:
                printf("OK       ");
                break;
              case CRM_NO_TAG:
                printf("No Resp. ");
                break;
              case CRM_CRC_ERR:
                printf("CRC Error");
                break;
              case CRM_COLLISION:
                printf("Collision");
                break;
              case CRM_WEAK_COLLISION:
                printf("Weak Col.");
                break;
              default:
                printf("ERR #%-3d ", status);
            }
            if (ts_idx == (slots - 1) || !((ts_idx + 1) % columns))  printf("\n");
            if (status == CRM_NO_TAG)         status_idx++;
            else                              status_idx += STAT + rxlen;
            if (kbhit())
            {
              k = getch();
              if (!k)
              {
                k = getch();
              }
              Quit = TRUE;
              if (k == 0x1B)  // Esc
              {
                break;
              }
            }
          }
        }
      }
      return (resplen);
    }
    if (cmd_code == OTP_READ_ALL)
    {
      switch (Data[STAT + 30] & 0x03)
      {
        case TYPE_UID1:
        case TYPE_UID1A:
          crc8 = CRC8_PRESET_UID1_1A ^ OTP_READ_ALL;
          break;
        case TYPE_UID2:
          crc8 = CRC8_PRESET_UID2 ^ OTP_READ_ALL;
          break;
        default:
          crc8 = CRC8_PRESET_EPC ^ OTP_READ_ALL;
      }
      for (j = 0; j < 8; j++)
      {
        crc8 = (uchar)((crc8 & 0x80)? (crc8 << 1) ^ CRC8_POLYNOM_MSB : (crc8 << 1));
      }
      printf("\nResponse Data:    ");
      for (i = STAT; i < resplen; i++)
      {
        crc8 = (uchar)(crc8 ^ Data[i]);
        for (j = 0; j < 8; j++)
        {
          crc8 = (uchar)((crc8 & 0x80)? (crc8 << 1) ^ CRC8_POLYNOM_MSB : (crc8 << 1));
        }
        printf("%02X ", Data[i]);
        if (!(i % BPL) && (i < resplen - 1))  printf("hex\n                  ");
      }
      printf("hex%48s\n", " ");
      printf("CRC8 (response):  %02X hex\n", Data[resplen]);
      printf("CRC8 (check):     %02X hex ==> %s <==     \n", crc8, 
               (Data[resplen] == crc8)? "OK" : "Error");
    }
    else
    {
      printf("CRC16 (MSB LSB):  %02X %02X hex\n", Data[resplen - 1], Data[resplen]);
    }
    return (resplen);
  }

  resplen -= slots * STAT;
  printf("\nResponse Length:%4d byte(s) @ %d timeslot(s) received ", resplen, slots);
  if (resplen/rxlen > 1)       printf("(response: %3d slots)", resplen/rxlen);
  else if (resplen == rxlen)   printf("(response:   1 slot) ");
  else                         printf("(no response)        ");
  printf("   \n\n");

  if (rxlen >= CRC16 && rxlen <= ePC_Bytes[ePC_Type] + CRC16)
  {
    i = 57 + 3*(ePC_Bytes[ePC_Type] - BYTES_EPC);
    if (Console_x > i && slots > 64) columns = Console_x/i;
    else                             columns = 1;

    for (i = 1; i < columns && i < slots; i++)
    {
      printf("SLOT EPC%02d (MSB).............", ePC_Bytes[ePC_Type] - 1);
      for (j = 0; j < ePC_Bytes[ePC_Type] - BYTES_EPC; j++)  printf("...");
      printf("(LSB) EPC00 CRC16 STATUS   ");
    }
    printf("SLOT EPC%02d (MSB).............", ePC_Bytes[ePC_Type] - 1);
    for (j = 0; j < ePC_Bytes[ePC_Type] - BYTES_EPC; j++)  printf("...");
    printf("(LSB) EPC00 CRC16 STATUS\n\n");
    status_idx = 0;

    for (ts_idx = 0; ts_idx < slots; ts_idx++)
    {
      status = rxdata[status_idx];
      printf(" %3d ", ts_idx);

      for (byte_idx = ePC_Bytes[ePC_Type] - 1; byte_idx > rxlen - CRC16 - 1; byte_idx--)
      {
        printf("%02X ", epc_mask[byte_idx]);
      }
      for (byte_idx = 0; byte_idx < rxlen; byte_idx++)
      {
        epcbyte = *(rxdata + status_idx + STAT + byte_idx);
        if (byte_idx == 0 && masklen > 0 && (masklen % 8))
        {
          if (status == CRM_NO_TAG)     epcbyte = 0;
          else                          epcbyte &= (0xFF >> (masklen % 8));
          epcbyte |= (0xFF << (8 - (masklen % 8))) & epc_mask[rxlen - CRC16 - 1];
          printf("%02X ", epcbyte);
        }
        else if (status == CRM_NO_TAG)  printf("   ");
        else                            printf("%02X ", epcbyte);
      }
      switch (status)
      {
        case OK:
          printf("OK       ");
          break;
        case CRM_NO_TAG:
          printf("No Resp. ");
          break;
        case CRM_CRC_ERR:
          printf("CRC Error");
          break;
        case CRM_COLLISION:
          printf("Collision");
          break;
        case CRM_WEAK_COLLISION:
          printf("Weak Col.");
          break;
        default:
          printf("ERR #%-3d ", status);
      }
      if (ts_idx == (slots - 1) || !((ts_idx + 1) % columns))  printf("\n");
      if (status == CRM_NO_TAG)         status_idx++;
      else                              status_idx += STAT + rxlen;
      if (kbhit())
      {
        k = getch();
        if (!k)
        {
          k = getch();
        }
        Quit = TRUE;
        if (k == 0x1B)  // Esc
        {
          break;
        }
      }
    }
    if (Logging)
    {
      status_idx = 0;
      fprintf(Lfp, "%8lu Slot F: %02X TS:  0 EPC: ", Cnt - 1L, 
                    rxdata[resplen + slots * STAT]);
      for (ts_idx = 0; ts_idx < slots; ts_idx++)
      {
        if (ts_idx > 0)
        {
          fprintf(Lfp, "                    TS:%3d EPC: ", ts_idx);
        }
        status = rxdata[status_idx];
        for (byte_idx = ePC_Bytes[ePC_Type]-1; byte_idx > rxlen - CRC16 - 1; byte_idx--)
        {
          fprintf(Lfp, "%02X ", epc_mask[byte_idx]);
        }
        for (byte_idx = 0; byte_idx < rxlen; byte_idx++)
        {
          epcbyte = *(rxdata + status_idx + STAT + byte_idx);
          if (byte_idx == 0 && masklen > 0 && (masklen % 8))
          {
            if (status == CRM_NO_TAG)     epcbyte = 0;
            else                          epcbyte &= (0xFF >> (masklen % 8));
            epcbyte |= (0xFF << (8 - (masklen % 8))) & epc_mask[rxlen - CRC16 - 1];
            fprintf(Lfp, "%02X ", epcbyte);
          }
          else if (status == CRM_NO_TAG)  fprintf(Lfp, "   ");
          else                            fprintf(Lfp, "%02X ", epcbyte);
        }
        CRM_get_error_text(status, &errtxt);
        fprintf(Lfp, "%-14s\n",    errtxt);
        if (status == CRM_NO_TAG)         status_idx++;
        else                              status_idx += STAT + rxlen;
      }
    }
  }
  return (resplen);
}
/***************************************************************************/

static int HL_ISO_cmd(int cmd_code, int flags, int parlen, int rxlen,
                      uchar *uid, uchar *par, uchar *rxdata)
{
  int i, bytes_per_line, txlen, resplen, status;
  uchar txdata[30];
  char *errtxt;

  if (parlen > 28)
  {
    printf("Max. number of parameter bytes (28) exceeded!\n");
    return (-1);
  }
  bytes_per_line = ((cmd_code == 0x23) && (flags & 0x40))? BPL - 1 : BPL;

  txdata[0] = (uchar)flags;
  txdata[1] = (uchar)cmd_code;
  printf("Command Flags:   %02X hex\n", flags);
  printf("Command Code:    %02X hex\n", cmd_code);
  txlen = FLAGS + CMD;

  if (cmd_code >= 0xA0 && cmd_code <= 0xDF)
  {
    txdata[txlen++] = 0x04;   // Manufacturer Code: Philips
    printf("Manufact. Code:  04 hex (Philips)\n");
  }
  if (flags & 0x20)           // Addressed Mode
  {
    printf("UID (LSB...MSB): ");
    for (i = 0; i < UID; i++)
    {
      txdata[txlen++] = uid[i];
      printf("%02X ",   uid[i]);
    }
    printf("hex\n");
  }
  if (parlen > 0)
  {
    printf("Parameters:      ");
    for (i = 0; i < parlen; i++)
    {
      txdata[txlen++] = par[i];
      printf("%02X ",   par[i]);
    }
    printf("hex\n");
  }
  printf("Response Length: %2d byte(s) expected (nominal, excl. CRC16)\n", rxlen);

  CRM_cmd(CMD_CMD, (ushort)txlen, (ushort)rxlen, txdata, rxdata);
//CRM_test(CMD_ISO, (uchar)txlen, (uchar)rxlen, txdata, rxdata);

  wait_Eot(1);
  resplen = CRM_get_datalen();
  if (get_status(0, "SL2") != OK)
  {
    return (-2);
  }
#ifdef USE_DEBUG_OUTPUT
  show_data(resplen, rxdata);
#endif
  if (resplen < STAT)
  {
    printf("\n%-40s\n", "No Response!");
    return (0);
  }
  resplen -= STAT;

  printf("\nResponse Length: %2d byte(s) received  \n", resplen);

  if (resplen >= FLAGS)
  {
    printf("Response Flags:  %02X hex %30s\n", rxdata[1], " ");
  }
  if (resplen > FLAGS)
  {
    printf("Data (LSB..MSB): ");
    for (i = STAT + FLAGS; i <= resplen; i++)
    {
      printf("%02X ", rxdata[i]);
      if (bytes_per_line == (BPL - 1) && !((i - 1) % 5)) printf(" ");
      if (!((i - 1) % bytes_per_line))
      {
        if (i < resplen)
        {
          printf("\n                 ");
        }
      }
    }
    printf("              \n");
  }
  status = rxdata[0];
  CRM_get_error_text(status, &errtxt);
  printf("%64s\n", " ");
  printf("Status:          %-16s %30s\n", errtxt, " ");
  printf("%64s\n", " ");
  if (status == CRM_NO_TAG)
  {
    for (i = 0; i <= rxlen/16; i++)
    {
      printf("%70s\n", " ");
    }
  }
  else if ((status & ~CRM_WEAK_COLLISION) == OK)
  {
    if (resplen >= FLAGS && (rxdata[1] & 0x01))
    {
      Beep(1000, 50);
    }
    return (resplen);
  }
  return (-resplen);
}
/***************************************************************************/

static int HL_ISO_inventory(int inventory_cmd, int flags, int nr_of_slots,
									 int afi, int mask_len, int blk_ad, int nr_of_blocks,
									 uchar *mask, uchar *rxdata)
{
  int i, j, txlen, rxlen, resplen;
  uchar txdata[14];

  if (inventory_cmd == 0x01)
  {
	 flags &= 0x13;            // Option Flag not supported at Inventory
  }
  txdata[0] = (uchar)(0x04 | (flags & 0x53));
  if (nr_of_slots == 1)
  {
	 txdata[0] |= 0x20;
  }
  printf("Command Flags:   %02X hex\n", txdata[0]);

  txdata[1] = (uchar)inventory_cmd;
  printf("Command Code:    %02X hex\n", inventory_cmd);
  txlen = FLAGS + CMD;

  if (inventory_cmd == 0xA0 || inventory_cmd == 0xA1 ||
		inventory_cmd == 0xB0 || inventory_cmd == 0xB1)
  {
	 txdata[txlen++] = 0x04;   // Manufacturer Code: Philips
	 printf("Manufact. Code:  04 hex (Philips)\n");
  }

  if (flags & 0x10)           // AFI
  {
	 txdata[txlen++] = (uchar)afi;
	 printf("AFI:             %02X hex\n", afi);
  }

  txdata[txlen++] = (uchar)mask_len;
  printf("Mask Length:     %2d     \n", mask_len);

  j = (mask_len + UID - 1)/UID;
  if (j > 0)
  {
	 printf("Mask:            ");
	 for (i = 0; i < j; i++)
	 {
		txdata[txlen++] = mask[i];
		printf("%02X ", mask[i]);
	 }
	 printf("hex\n");
  }

  if (inventory_cmd == 0x01)  // Inventory
  {
    rxlen = FLAGS + DSFID + UID;
  }
  else                        // Inventory Read, Fast Inventory Read
  {
    txdata[txlen++] = (uchar)blk_ad;
    printf("Block Address:   %2d    \n", blk_ad);

    txdata[txlen++] = (uchar)(nr_of_blocks - 1);
    printf("Number of Blocks:%2d    \n", nr_of_blocks);

    rxlen = FLAGS + 17*nr_of_blocks;

    if (flags & 0x40)         // Option Flag
    {
      rxlen += ((((nr_of_slots == 1)? 64 : 60) - mask_len) + 7)/8;
    }
  }
  printf("Response Length: %2d bytes per timeslot expected (excl. CRC16)\n", rxlen);

  CRM_cmd(CMD_CMD, (ushort)txlen, (ushort)rxlen, txdata, rxdata);
//CRM_test(CMD_ISO, (uchar)txlen, (uchar)rxlen, txdata, rxdata);

  wait_Eot(1);
  resplen = CRM_get_datalen();
  if (get_status(0, "SL2") != OK)
  {
    return (-2);
  }
#ifdef USE_DEBUG_OUTPUT
  show_data(resplen, rxdata);
#endif
  if (resplen < nr_of_slots)
  {
    printf("\n%-40s\n", "No Response!");
    return (0);
  }
  resplen -= nr_of_slots;

  printf("\nResponse Length: %2d bytes @ %d timeslot(s) received (LSB..MSB)\n\n",
                             resplen, nr_of_slots);

  show_inventory_data(inventory_cmd, nr_of_slots, rxlen, rxdata);

  return (resplen);
}
/***************************************************************************/

static void show_inventory_data(int cmd, int ts, int bytes_per_ts, uchar *data)
{
  int  i, ts_idx, flags_idx, data_idx, status_idx, status, databytes, byte_idx;
  int  ts_per_screen, stat1 = 0;
  char *errtxt;

  if (bytes_per_ts < FLAGS)
  {
    return;
  }
  if (cmd == 0x01)            // Inventory
  {
    printf("SLOT STAT1 DSFID UID0 ............. UID7 "
           "STATUS  UID-Bit 1. Strong (Weak) Coll.\n\n");
  }
  else                        // Inventory Read or Fast Inventory Read
  {
    printf("SLOT FLAGS DATA ...\n\n");
  }
  databytes  = bytes_per_ts - FLAGS - 1;
  status_idx = 0;

  for (ts_idx = 0; ts_idx < ts; ts_idx++)
  {
    flags_idx = status_idx + STAT;
    data_idx  = flags_idx + FLAGS;
    status    = data[status_idx];

    if (status != CRM_NO_TAG)
    {
      if (cmd == 0x01)        // Inventory
      {
        stat1 = data[flags_idx];
        printf(" %-2d   %02X    %02X   ", ts_idx, stat1, data[data_idx]);
      }
      else                    // Inventory Read or Fast Inventory Read
      {
        printf(" %-2d   %02X   ", ts_idx, data[flags_idx]);
      }
      status_idx += (STAT + bytes_per_ts);
    }
    else
    {
      if (cmd == 0x01)        // Inventory
      {
        printf(" %-2d              ", ts_idx);
      }
      else                    // Inventory Read or Fast Inventory Read
      {
        printf(" %-2d        ", ts_idx);
      }
      status_idx++;
    }
    if (databytes < 0)
    {
      printf("\n");
      continue;
    }
    i = (cmd == 0x01)? 1 : 0;
    for (byte_idx = i; byte_idx <= databytes; byte_idx++)
    {
      if (status != CRM_NO_TAG)
      {
        printf("%02X ", *(data + data_idx + byte_idx));
      }
      else
      {
        printf("   ");
      }

      if (byte_idx == databytes || (byte_idx % BPL) == (BPL - 1))
      {
        if (byte_idx <  (BPL - 1) && byte_idx == databytes ||
            byte_idx == (BPL - 1))
        {
          if (cmd == 0x01)    // Inventory
          {
            if ((status & CRM_COLLISION) == CRM_COLLISION)
            {
              CRM_get_error_text(CRM_COLLISION, &errtxt);
              if (stat1 & 0x01)
              {
                printf("%-21s       ", errtxt);
              }
              else
              {
                printf("%-21s %-2d    ", errtxt, status >> 2);
              }
            }
            else
            {
              CRM_get_error_text(status, &errtxt);
              printf("%-21s       ", errtxt);
            }
            if (status != CRM_NO_TAG)
            {
              if (stat1 & 0x02)  printf("(%d) ", stat1 >> 2);
              else               printf("    ");
            }
          }
          else
          {
            CRM_get_error_text(status, &errtxt);
            if (strlen(errtxt) > 14)  printf("\n%79s", errtxt);
            else                      printf("%14s  ", errtxt);
          }
        }
        if (byte_idx < databytes)
        {
          printf("\n%11s", " ");
          if (cmd == 0x01)  printf("      ");
        }
        else
        {
          printf("\n");
        }
      }
      else if ((byte_idx % BPL) == (BPL - 1))
      {
        printf("\n");
      }
    }
    if (Quit) return;

    ts_per_screen = (Lines - 2)/((databytes + BPL)/BPL);

    if (!Cont && (ts_idx % ts_per_screen) == (ts_per_screen - 1) &&
                  ts_idx < (ts - 1))
    {
      printf("Continue ...\r");

      switch (getch())
      {
        case 0x1B:  // Esc
          printf("Break!      \n");
          return;

        case 0x00:
          getch();
          break;
      }
    }
  }
}
/***************************************************************************/

static void show_read_data(int ts, int nobl, uchar *data)
{
  int  i, j;
  char *errtxt;

  if (Quit || nobl == 0)
  {
    return;
  }

  printf("\n");
  for (i = 0; i < ts; i++)
  {
    printf("Timeslot %3d: ", i);

    for (j = 0; j < nobl * 4; j++)
    {
      if (data[i] == OK || data[i] == CRM_WEAK_COLLISION)
      {
        printf("%02X ", *(data + ts + 4 * nobl * i + j));
      }

      else
      {
        printf("   ");
      }
      if (j == nobl * 4 - 1 || j % BPL == (BPL - 1))
      {
        if (j == nobl * 4 - 1 && j < (BPL - 1) || j == (BPL - 1))
        {
          CRM_get_error_text(data[i], &errtxt);
          printf("%15s", errtxt);
        }
        if (j < nobl * 4 - 1) printf("\n%14s", " ");
        else                  printf("\n");
      }
      else if (j % BPL == (BPL - 1))
      {
        printf("\n");
      }
    }

    j = (Lines - 2)/((nobl + 3)/4);

    if (Quit) return;

    if (!Cont && i % j == (j - 1) && i < ts - 1)
    {
      printf("Continue ...\r");

      switch (getch())
      {
        case 0x1B:  // Esc
          printf("Break!      \n");
          return;

        case 0x00:
          getch();
          break;
      }
    }
  }
}
/***************************************************************************/

static void show_data(int len, uchar *data)
{
  int i;

  if (Quit || len == 0)
  {
    return;
  }
  printf("\nData (LSB..MSB): ");
  for (i = 0; i < len; i++)
  {
    printf("%02X ", data[i]);
    if (!((i + 1) % BPL) && (i < len - 1))  printf("hex\n                 ");
  }
  printf("hex%48s\n\n", " ");
}
/***************************************************************************/

static int get_status(int mode, char *cmd)
{
  char *text;
  int status = CRM_get_status();

  if (Quit)
  {
    return (CRM_BREAK);
  }
  if (mode == 0 && status == OK)
  {
    return (OK);
  }
  if (mode == -1)
  {
    CRM_get_error_text(status, &text);
    printf("%s!\n", text);
    return (status);
  }
  if (mode == -2)
  {
    return (status);
  }
  printf("\n%s TxS %3d, RxS %3d, Err %3d, Sta %3d, Len %3d, C %X hex -> ",
            cmd, CRM_get_txseq(), CRM_get_rxseq(), CRM_get_err(), status,
            CRM_get_datalen(), CRM_get_check());

  CRM_get_error_text(status, &text);
  if (CRM_get_status() == OK)
  {
    if (CRM_get_datalen() < 21)
    {
      printf("%-19s\n", text);
    }
    else
    {
      printf("\n%79s\n", text);
    }
  }
  return (status);
}
/***************************************************************************/

static void wait_Eot(int showlifesign)
{
  int k, ls_idx = 0;
  DWORD dwRes;
  char lifesign[8] = {'ú', '-', 'ú', '\\', 'ú', '|', 'ú', '/'};

  do
  {
    dwRes = WaitForSingleObject(ICODE_Event, 6);  // Timeout = 6 ms

    switch (dwRes)
    {
      case WAIT_OBJECT_0: // Everything ok
        if (showlifesign) printf(" \010");
        break;

      case WAIT_TIMEOUT:  // Timeout occured while waiting for event WAIT_OBJECT_0
        if (showlifesign)
        {
          printf("%c\010", lifesign[ls_idx]);
          if (++ls_idx >= 8)        ls_idx = 0;
        }
        break;
    }
    if (kbhit() && !Quit)
    {
      Cont = FALSE;
      k = getch();
      if (!k)
      {
        k = getch();
      }
      if (showlifesign)   printf(" \010");
      if (k == 0x1B)  // Esc
      {
        Quit = TRUE;
        _gotoxy(1, Lines - 1);
        if (Console_x <= 80)  printf("%-159s", "Break!");
        else                  printf("%-249s", "Break!");
        _gotoxy(1, Lines); 
      }
    }
  } while (!Quit && dwRes != WAIT_OBJECT_0);

  if (!Quit && dwRes != WAIT_OBJECT_0)
  {
    Quit = TRUE;
    Cont = FALSE;
    printf("\nSerial Communication Error!\n");
  }
}
/***************************************************************************/

static int _clrscr(int *dwSize_x, int *dwSize_y)
{
  DWORD   eDummy;
  HANDLE  hConsole;
  COORD   stStartCoords;
  CONSOLE_SCREEN_BUFFER_INFO  stConsoleInfo;

  //  Get a handle to the current console
  hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
  if (hConsole == INVALID_HANDLE_VALUE)
    return -1;

  //  Get the console's screen buffer size
  if (GetConsoleScreenBufferInfo(hConsole, &stConsoleInfo) == 0)
    return -1;

  //  Fill the console output window with spaces
  stStartCoords.X = 0;
  stStartCoords.Y = 0;
  if (FillConsoleOutputCharacter(hConsole, ' ',
    stConsoleInfo.dwSize.X * stConsoleInfo.dwSize.Y, stStartCoords, &eDummy) == 0)
    return -1;

  *dwSize_x = stConsoleInfo.dwSize.X;
  *dwSize_y = stConsoleInfo.dwSize.Y;

  //  Position the cursor to the top left corner (home position)
  if (SetConsoleCursorPosition(hConsole, stStartCoords) == 0)
    return -1;

  return 0;
}
/***************************************************************************/

static int _gotoxy(int iCursorX, int iCursorY)
{
  HANDLE  hConsole;
  COORD   stCursorCoords;

  //  Get a handle to the current console
  hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
  if (hConsole == INVALID_HANDLE_VALUE)
    return -1;

  //  Position the cursor to the desired place
  stCursorCoords.X = (SHORT)(iCursorX - 1);
  stCursorCoords.Y = (SHORT)(iCursorY - 1);
  if (SetConsoleCursorPosition(hConsole, stCursorCoords) == 0)
    return -1;

  return 0;
}
/***************************************************************************\
                                End of the File
\***************************************************************************/
