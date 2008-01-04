package de.haw.smartshelf.reader.test.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface RockerWrapper extends Library {

	RockerWrapper INSTANCE = (RockerWrapper) Native.loadLibrary("SmartShelfRockerWrapper", RockerWrapper.class);

	void HelloWorld();
	int returnBool(int bool);
}
