package de.haw.smartshelf.testcases;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.mock.ICodeReaderMock;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class MockTest {
	private static ShelfReader reader = new ICodeReaderMock();

	@Before
	public void resetReader() {
		// reader.reset();
	}

	@Test
	public void gatherTags() {
		for (RFIDTag tag : reader.gatherTags()) {
			assert tag.getId() == "12345";
		}
	}

	@Ignore("TODO")
	@Test
	public void search() {
	}
}