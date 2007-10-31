package de.haw.smartshelf.testcases;

import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.mock.ICodeReaderMock;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class MockTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ShelfReader reader = new ICodeReaderMock();
		for (RFIDTag tag:reader.gatherTags()){
			System.out.println(tag);
		}
	}

}
