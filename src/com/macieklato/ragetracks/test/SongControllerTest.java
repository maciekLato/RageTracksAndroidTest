package com.macieklato.ragetracks.test;

import com.macieklato.ragetracks.controller.SongController;
import com.macieklato.ragetracks.model.Song;
import com.macieklato.ragetracks.util.Network;

import junit.framework.TestCase;

public class SongControllerTest extends TestCase {

	SongController sc;
	Song s1;
	Song s2;

	public void before() {
		sc = SongController.getInstance();
		sc.reset();
		String track = "146496810";
		s1 = new Song(
				31590,
				"http://RageTracks.com/far-too-loud-acid9000-felmax-remix/",
				"Acid9000 (FelMax Remix)",
				"Far Too Loud",
				String.format(
						"http://api.soundcloud.com/tracks/%s/stream?client_id=%s",
						track, Network.CLIENT_ID),
				"http://RageTracks.com/wp-content/uploads/2014/04/artworks-000077601713-pc1pgk-t500x500.jpg",
				track);
		track = "146727164";
		s2 = new Song(
				31587,
				"http://RageTracks.com/romeo-quenn-last-warrior/",
				"Last Warrior",
				"Romeo Quenn",
				String.format(
						"http://api.soundcloud.com/tracks/%s/stream?client_id=%s",
						track, Network.CLIENT_ID),
				"http://RageTracks.com/wp-content/uploads/2014/04/artworks-000077736519-8j462c-t500x500.jpg",
				track);

	}

	public void testAutoPlay() {
		before();
		assertFalse(sc.getAutoPlay());
		sc.nextSong();
		assertTrue(sc.getAutoPlay());
	}
	
	public void testAddSong() {
		before();
		assertEquals(0, sc.getNumSongs());
		sc.addSong(s1);
		assertEquals(1, sc.getNumSongs());
		sc.addSong(s2);
		assertEquals(2, sc.getNumSongs());
		sc.reset();
		assertEquals(0, sc.getNumSongs());
	}
	
	public void testReset() {
		before();
		assertEquals(0, sc.getNumSongs());
		sc.addSong(s1);
		assertEquals(1, sc.getNumSongs());
		
		assertFalse(sc.getAutoPlay());
		sc.nextSong();
		assertTrue(sc.getAutoPlay());
		
		assertEquals(s1, sc.getSong());
		sc.addSong(s2);
		sc.nextSong();
		assertEquals(s2, sc.getSong());
		
		sc.reset();
		assertEquals(0, sc.getNumSongs());
		assertFalse(sc.getAutoPlay());
		assertEquals(null, sc.getSong());
	}
	
	public void testGetSong() {
		before();
		sc.addSong(s1);
		sc.addSong(s2);
		sc.setActiveSong(s1);
		assertEquals(s1, sc.getSong());
		sc.setActiveSong(s2);
		assertEquals(s2, sc.getSong());
		assertFalse(s1.equals(s2));
	}
	
	public void testGetSongAt() {
		before();
		sc.addSong(s1);
		sc.addSong(s2);
		assertEquals(s1, sc.getSongAt(0));
		assertEquals(s2, sc.getSongAt(1));
		assertFalse(sc.getSongAt(0).equals(sc.getSongAt(1)));
		assertEquals(null, sc.getSongAt(-1), sc.getSongAt(2));
	}
	
	public void testGetSongById() {
		before();
		sc.addSong(s1);
		assertEquals(s1, sc.getSongById(s1.getId()));
		assertEquals(null, sc.getSongById(s2.getId()));
		sc.addSong(s2);
		assertEquals(s2, sc.getSongById(s2.getId()));
	}
	
	public void testNext() {
		before();
		sc.addSong(s1);
		sc.addSong(s2);
		assertEquals(s1, sc.getSong());
		sc.nextSong();
		assertEquals(s2, sc.getSong());
		assertFalse(s1.equals(s2));
	}
	
	public void testPrevious() {
		before();
		sc.addSong(s1);
		sc.addSong(s2);
		sc.nextSong();
		assertEquals(s2, sc.getSong());
		sc.previousSong();
		assertEquals(s1, sc.getSong());
		assertFalse(s1.equals(s2));
	}
}
