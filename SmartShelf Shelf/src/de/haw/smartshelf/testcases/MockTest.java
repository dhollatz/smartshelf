package de.haw.smartshelf.testcases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.mock.ICodeReaderMock;

public class MockTest {

	private static ShelfReader reader = new ICodeReaderMock();

	@Before
	public void setUp() throws Exception {
		// reader.reset();
	}

	@Test
	public void gatherTags() {
		assertEquals(reader.gatherTags().size(), 3);

	}

	@Test
	public void tagInRange() {
		assert reader.isTagInRange("12345");
	}

	@Test
	public void tagNotInRange() {
		assertFalse(reader.isTagInRange("4711"));
	}

	@Ignore("TODO")
	public void readerCrash() {
	}

}
