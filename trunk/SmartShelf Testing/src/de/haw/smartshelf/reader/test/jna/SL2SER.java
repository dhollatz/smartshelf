package de.haw.smartshelf.reader.test.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.ByteByReference;

public interface SL2SER extends Library {

	SL2SER INSTANCE = (SL2SER) Native.loadLibrary("SL2SER", SL2SER.class);

	public static final byte CMD_CMD = (byte) 0xFC;

	int CRM_init(String ComStr, String BaudStr);

	void CRM_cmd(byte cmd, short txlen, short rxlen, byte[] tx, byte[] rx);

	void CRM_get_info(byte mode, byte[] info);

	// void CRM_get_port(byte[] portbyte);
	void CRM_get_port(ByteByReference portbyte);

}
