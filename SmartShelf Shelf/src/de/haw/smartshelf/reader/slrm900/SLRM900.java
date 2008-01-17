package de.haw.smartshelf.reader.slrm900;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.sun.jna.ptr.IntByReference;

import de.haw.smartshelf.reader.AbstractReader;
import de.haw.smartshelf.reader.tags.ICodeTag;
import de.haw.smartshelf.reader.tags.RFIDTag;

// VM Arguments:
// -Djna.library.path=./lib

public class SLRM900 extends AbstractReader {

	private static final String BAUD = "115200";
	private static final int UID_LENGTH = 8;

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
			tags.addAll(inventory());
		} else {
			LOG.error("SLRM900 initialization failed - check connection");
		}
		return tags;
	}

	private Collection<ICodeTag> inventory() {
		// ICODE SLI-S Structure
		// |STATUS|STAT1|DSFID|UID0|UID1|UID2|UID3|UID4|UID5|UID6|UID7|
		// if a status is "1" then no tag was found
		// -> the next byte contains the status for the next timeslot
		// -> e.g. |1|1|0|... represents 2 timeslots where no tag was found
		// followed by a timeslot with tag data

		// unsigned byte to int by using "& 0xFF"
		// byte[] data = new byte[11];
		byte[] data = new byte[256 * 65];
		IntByReference timeSlotLengthRef = new IntByReference();
		int len = lib.inventory(data, timeSlotLengthRef);
		int timeSlotLength = timeSlotLengthRef.getValue();
		LOG.debug("Length of data: " + len);
		LOG.debug("Length of one timeslot: " + timeSlotLength);
		Collection<ICodeTag> tags = new ArrayList<ICodeTag>();
		int offset = 0;
		int tsNum = 1;
		for (int i = 0; i < len; i = i + offset + 1) {
			if (data[i] == 0x00) {
				LOG.trace("Timeslot " + tsNum + ": Tag found...");
				offset = timeSlotLength;
				// timeslot data corrupt?
				if ((i + timeSlotLength) < len) {
					ICodeTag aTag = new ICodeTag();
					String result = "";
					String id = "";
					LOG.trace("STATUS: " + (data[i] & 0xFF));
					LOG.trace("STAT1: " + (data[i + 1] & 0xFF));
					aTag.setStat1(data[i + 1] & 0xFF);
					LOG.trace("DSFID: " + (data[i + 2] & 0xFF));
					aTag.setDsfid(data[i + 2] & 0xFF);
					// index i + 3 to i + 3 + UID_LENGTH are UID bytes
					for (int uidIndex = i + 3; uidIndex < i + 3 + UID_LENGTH; uidIndex++) {
						// TODO wie bereiten wir die ID auf?
						id += (data[uidIndex] & 0xFF);
						if (LOG.isTraceEnabled()) {
							result += String.format("%02X",
									data[uidIndex] & 0xFF)
									+ ":";
						}
					}

					LOG.trace("UDI: "
							+ result.substring(0, result.length() - 1));
					aTag.setId(result.substring(0, result.length() - 1));
					tags.add(aTag);

				} else {
					LOG.error("Timeslot " + tsNum + ": Data corrupt");
				}
			} else {
				LOG.trace("Timeslot " + tsNum + ": No tag found...");
				offset = 0;
			}
			tsNum++;
		}
		return tags;
	}

	private boolean init() {
		LOG.debug("Initializing reader...");
		return (isInit = lib.init(port, BAUD) == 0);
	}

}
