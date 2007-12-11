package de.haw.smartshelf.reader.test;

public class ICodeReaderTest {
	static {
		System.loadLibrary("SL2SER");
	}

	public static native int CRM_init(String comStr, String baudStr);
	
	public static void main(String args[]){
		CRM_init("COM1", "9600"); // just guessed ;)
	}
}
