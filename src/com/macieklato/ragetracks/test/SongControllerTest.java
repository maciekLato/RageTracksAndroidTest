package com.macieklato.ragetracks.test;

import com.macieklato.ragetracks.model.SongController;

import junit.framework.TestCase;

public class SongControllerTest extends TestCase {

	SongController sc = SongController.getInstance();
	
	public void testUninitializedState() {
		assertEquals(SongController.UNINITIALIZED, sc.getState());
	}
	
}
