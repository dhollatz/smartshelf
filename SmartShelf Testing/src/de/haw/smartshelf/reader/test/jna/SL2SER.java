package de.haw.smartshelf.reader.test.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface SL2SER extends Library {

	SL2SER INSTANCE = (SL2SER) Native
			.loadLibrary("SL2SERWrapper", SL2SER.class);

	public static final byte CMD_CMD = (byte) 0xFC;

	int init(String ComStr, String BaudStr);

	int inventory(byte[] data);

	void enableDebug();

}
