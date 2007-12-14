package de.haw.smartshelf.reader.test;

/**
 * 
 * Note: SWIG Commands for wrapper generation.
 * <code>
 * > swig.exe -java reader.i
 * > gcc.exe -c reader_wrap.c -IC:\Java\jdk1.5.0\include -IC:\Java\jdk1.5.0\include\win32
 * > gcc.exe -shared reader_wrap.o -mno-cygwin -Wl,--add-stdcall-alias  -o reader.dll
 * </code>
 *  
 * @author hollat_d
 *
 */
public class ICodeReaderTest {

	//TODO Use generated wrapper
	static {
		String path = System.getProperty("java.library.path");
		System.out.println(path);
//		System.loadLibrary("SL2SER");
		System.loadLibrary("reader");
		
	}

	public static native int CRM_init(String comStr, String baudStr);

	public static void main(String args[]){
		CRM_init("COM1", "115200"); // just guessed ;)
	}
}
