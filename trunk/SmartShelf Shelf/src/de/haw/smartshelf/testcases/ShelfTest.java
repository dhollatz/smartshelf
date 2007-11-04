package de.haw.smartshelf.testcases;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import de.haw.smartshelf.facade.Shelf;

public class ShelfTest {

	private static Shelf shelf = new Shelf();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getAllItems() {
		assertEquals(shelf.getAllItems().size(), 3);
	}

}
