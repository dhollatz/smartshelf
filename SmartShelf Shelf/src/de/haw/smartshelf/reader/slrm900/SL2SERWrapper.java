package de.haw.smartshelf.reader.slrm900;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface SL2SERWrapper extends Library {

	public static final String LIBNAME = "SL2SERWrapper";

	public static final byte CMD_CMD = (byte) 0xFC;

	SL2SERWrapper INSTANCE = (SL2SERWrapper) Native.loadLibrary(LIBNAME,
			SL2SERWrapper.class);

	int init(String ComStr, String BaudStr);

	int inventory(byte[] data);

	void enableDebug();

}
