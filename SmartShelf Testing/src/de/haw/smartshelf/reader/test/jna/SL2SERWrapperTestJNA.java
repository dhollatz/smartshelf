package de.haw.smartshelf.reader.test.jna;

public class SL2SERWrapperTestJNA {

	// VM Arguments:
	// -Djna.library.path=./lib

	public static SL2SERWrapper lib = SL2SERWrapper.INSTANCE;

	static {
		String path = System.getProperty("jna.library.path");
		System.out.println(path);
	}

	public static void main(String args[]) {
		System.out.println(lib.returnBool(5));
		lib.init("COM1", "115200");
	}
}
