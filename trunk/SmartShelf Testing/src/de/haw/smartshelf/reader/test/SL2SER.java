/**
 * 
 */
package de.haw.smartshelf.reader.test;

/**
 * @author hollat_d
 *
 */
public class SL2SER {
	//TODO Use generated wrapper
	static {
		System.out.println(System.getProperty("java.library.path"));
//		System.loadLibrary("SL2SER");
		System.loadLibrary("reader");
		
	}
	
	public static native int CRM_init(String comStr, String baudStr);
	
	
}
