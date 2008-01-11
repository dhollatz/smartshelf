package de.haw.smartshelf.reader.test.jna;

public class ICodeReaderTestJNA {

	// VM Arguments:
	// -Djna.library.path=./lib

	public static final short TXLEN = 0x03;
	public static final short RXLEN = 0x0A; // bzw. 10
	public static final byte CRM_GET_VERSION = 1;
	public static final byte CRM_GET_ISO_15693 = 8;

	public static SL2SER lib = SL2SER.INSTANCE;

	public static void main(String args[]) {

		if (isInit()) {
			System.out.println("Init OK...");
			// getInfo();
			// getPort();
			inventory();
		} else {
			System.out.println("In your face...");
		}
	}

	public static void inventory() {
		byte[] data = new byte[11];
		int len = lib.inventory(data);
		System.out.println("Length of data: " + len);
		String result = "";
		for (int i = 0; i < len; i++) {
			result += data[i] + ":";
		}
		System.out.println(result);
	}

	public static boolean isInit() {
		int result = lib.init("COM1", "115200");
		// int result = lib.CRM_init("COM1", "115200");
		return result == 0;
	}

	// public static void getInfo() {
	// System.out.println("ICodeReaderTestJNA.getInfo()");
	// byte[] info = new byte[256 * 65];
	// lib.CRM_get_info(CRM_GET_ISO_15693, info);
	// for (byte b : info) {
	// if (b != 0)
	// System.out.println("|" + b);
	// }
	// }

	public static void getPort() {
		// byte[] port = new byte[256 * 65];
		// lib.CRM_get_port(port);
		// for (byte b : port) {
		// if (b != 0)
		// System.out.println("|" + b);
		// }
		// ByteByReference byteRef = new ByteByReference();
		// System.out.println(byteRef.getValue());
		// lib.CRM_get_port(byteRef);
		// System.out.println(byteRef.getValue());
	}

	// public static void cmd() {
	// byte[] tx = new byte[3];
	// tx[0] = 0x26;
	// tx[1] = 0x01;
	// tx[2] = 0x00;
	// //byte[] rx = new byte[256 * 65];
	// byte[] rx = new byte[11];
	// lib.CRM_cmd(SL2SER.CMD_CMD, TXLEN, RXLEN, tx, rx);
	//		 
	//		 
	// for (byte b : rx) {
	// if (b != 0)
	// System.out.println("|" + b);
	// }
	// System.out.println('|');
	//
	// // tx[0] = 6;
	// // tx[1] = 1;
	// // tx[3] = 0;
	// // lib.CRM_cmd((byte) 252, TXLEN, RXLEN, tx, rx);
	// // System.out.println(rx);
	// }

}
