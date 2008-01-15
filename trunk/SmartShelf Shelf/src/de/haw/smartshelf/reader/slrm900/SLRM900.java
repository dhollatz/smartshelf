package de.haw.smartshelf.reader.slrm900;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import de.haw.smartshelf.reader.AbstractReader;
import de.haw.smartshelf.reader.tags.ICodeTag;
import de.haw.smartshelf.reader.tags.RFIDTag;

// VM Arguments:
// -Djna.library.path=./lib

public class SLRM900 extends AbstractReader {

	private static final String BAUD = "115200";

	private static final Logger LOG = Logger.getLogger(SLRM900.class);

	public static SL2SERWrapper lib = SL2SERWrapper.INSTANCE;

	static {
		String path = System.getProperty("jna.library.path");
		System.out.println(path);
	}

	public static void main(String args[]) {
		SLRM900 slrm900 = new SLRM900();
		slrm900.setPort("COM1");
		lib.enableDebug();
		if (slrm900.init()) {
			LOG.debug("JNA: " + SL2SERWrapper.LIBNAME + " initialized");
			slrm900.inventory();
		} else {
			LOG.error("SLRM900 initialization failed - check connection");
		}
	}

	public void initialize() {
		init();
	}

	public Collection<RFIDTag> gatherTags() {
		LOG.debug("gathering tags...");
		Collection<RFIDTag> tags = new ArrayList<RFIDTag>();
		lib.enableDebug();
		if (isInit()) {
			LOG.debug("JNA: " + SL2SERWrapper.LIBNAME + " initialized");
			inventory();
		} else {
			LOG.error("SLRM900 initialization failed - check connection");
		}
		return tags;
	}

	public boolean isTagInRange(String id) {

		return false;
	}

	private Collection<ICodeTag> inventory() {
		// ICODE SLI-S Structure
		// |STATUS|STAT1|DSFID|UID0|UID1|UID2|UID3|UID4|UID5|UID6|UID7|
		byte[] data = new byte[11];
		int len = lib.inventory(data);
		LOG.debug("Length of data: " + len);
		String result = "";
		String id = "";
		Collection<ICodeTag> tags = new ArrayList<ICodeTag>();
		ICodeTag aTag = null;
		for (int i = 0; i < len; i++) {
			switch (i % len) {
			case 0:
				LOG.trace("Tag found...");
				LOG.trace("STATUS: " + (data[i] & 0xFF));
				aTag = new ICodeTag();
				break;
			case 1:
				LOG.trace("STAT1: " + (data[i] & 0xFF));
				aTag.setStat1(data[i] & 0xFF);
				break;
			case 2:
				LOG.trace("DSFID: " + (data[i] & 0xFF));
				aTag.setDsfid(data[i] & 0xFF);
				break;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				id += (data[i] & 0xFF); // unsigned byte to int by using "&
				// 0xFF"
				if (LOG.isTraceEnabled()) {
					result += (data[i] & 0xFF) + ":";
				}
				if (i % len == 10) {
					LOG.trace(result);
					aTag.setId(id);
					tags.add(aTag);
				}
				break;
			}
		}
		return tags;
	}

	private boolean init() {
		LOG.debug("Initializing reader...");
		return (isInit = lib.init(port, BAUD) == 0);
	}

}
