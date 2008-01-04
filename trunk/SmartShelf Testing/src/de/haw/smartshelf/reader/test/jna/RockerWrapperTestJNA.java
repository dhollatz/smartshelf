package de.haw.smartshelf.reader.test.jna;

public class RockerWrapperTestJNA {

	// VM Arguments:
	// -Djna.library.path=./lib

	public static RockerWrapper lib = RockerWrapper.INSTANCE;

	public static void main(String args[]) {
		lib.HelloWorld();
		System.out.println(lib.returnBool(5));
	}
}
