package de.haw.smartshelf.reader.test.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface SL2SERWrapper extends Library {

	SL2SERWrapper INSTANCE = (SL2SERWrapper) Native.loadLibrary("SL2SERWrapper", SL2SERWrapper.class);

	int returnBool(int bool);
	
	int init(String comStr, String baudStr);
}
