package com.macieklato.ragetracks.test;

import com.macieklato.ragetracks.model.Song;
import com.macieklato.ragetracks.model.SongController;
import com.macieklato.ragetracks.util.Network;

import junit.framework.TestCase;

public class SongControllerTest extends TestCase {

	SongController sc = SongController.getInstance();
	Song song = new Song();
	Song song2 = new Song();

	
	public void test1UninitializedState() {
		assertEquals(SongController.UNINITIALIZED, sc.getState());
	}
	
	public void test2MustLoad() {
		assertEquals(SongController.UNINITIALIZED, sc.getState());
		sc.toggle();
		assertEquals(SongController.UNINITIALIZED, sc.getState());
	}
	
	public void test3Load() {
		assertEquals(SongController.UNINITIALIZED, sc.getState());
		song.setTrack("144680470");
		song.setStreamUrl("https://api.soundcloud.com/tracks/144680470/stream?client_id="+Network.CLIENT_ID);
		sc.toggle(song);
		assertEquals(SongController.LOADING, sc.getState());
	}
	
	public void test4Song() {
		song2.setTrack("14460470");
		song2.setStreamUrl("https://api.soundcloud.com/tracks/144680470/stream?client_id="+Network.CLIENT_ID);
		assertFalse(song2.equals(sc.getSong()));
		assertEquals(SongController.LOADING, sc.getState());
		sc.toggle(song2);
		assertFalse(song2.equals(sc.getSong()));
	}
	
}
