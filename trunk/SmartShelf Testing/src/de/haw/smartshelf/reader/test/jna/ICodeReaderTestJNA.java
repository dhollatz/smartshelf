package de.haw.smartshelf.reader.test.jna;

public class ICodeReaderTestJNA {

	// VM Arguments:
	// -Djna.library.path=./lib

	public static final short TXLEN = 3;
	public static final short RXLEN = 10;
	public static final byte CRM_GET_VERSION = 1;
	public static final byte CRM_GET_ISO_15693 = 8;

	public static SL2SER lib = SL2SER.INSTANCE;

	static {
		String path = System.getProperty("jna.library.path");
		System.out.println(path);
		// System.loadLibrary("SL2SER");
		// System.loadLibrary("reader");
	}

	public static void main(String args[]) {

		if (isInit()) {
			System.out.println("Init OK...");
			getInfo();
			// getPort();
		} else {
			System.out.println("In your face...");
		}

	}

	public static boolean isInit() {
		int result = lib.CRM_init("COM1", "115200");
		return result == 0;
	}

	public static void getInfo() {
		System.out.println("ICodeReaderTestJNA.getInfo()");
		byte[] info = new byte[256 * 65];
		lib.CRM_get_info(CRM_GET_ISO_15693, info);
		for (byte b : info) {
			if (b != 0)
				System.out.println("|" + b);
		}
	}

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

	public static void cmd() {
		// byte[] tx = new byte[14];
		// tx[0] = 0x06;
		// tx[1] = 0x01;
		// tx[2] = 0x00;
		// byte[] rx = new byte[256 * 65];
		// lib.CRM_cmd(SL2SER.CMD_CMD, TXLEN, RXLEN, tx, rx);
		// for (byte b : rx) {
		// if (b != 0)
		// System.out.println("|" + b);
		// }
		// System.out.println('|');

		// tx[0] = 6;
		// tx[1] = 1;
		// tx[3] = 0;
		// lib.CRM_cmd((byte) 252, TXLEN, RXLEN, tx, rx);
		// System.out.println(rx);
	}

}
