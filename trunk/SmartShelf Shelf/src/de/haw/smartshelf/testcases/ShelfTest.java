package de.haw.smartshelf.testcases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.haw.smartshelf.shelf.Shelf;

public class ShelfTest {

	private static Shelf shelf = new Shelf();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getAllItems() {
		// assertTrue(shelf.getAllItems().size() >= 3);
	}

	@Test
	public void isTagInShelf() {
		assertFalse(shelf.containsTag("FF:00:FF:00:FF:00:FF:00"));
		assertTrue(shelf.containsTag("E2:9C:52:00:00:03:04:E0"));
	}

}
