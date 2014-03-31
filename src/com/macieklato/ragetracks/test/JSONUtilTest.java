package com.macieklato.ragetracks.test;

import junit.framework.TestCase;

import com.macieklato.ragetracks.util.JSONUtil;

public class JSONUtilTest extends TestCase {
	
	String urlPrepend = "http://api.soundcloud.com/tracks/";
	String urlAppend = "/stream?client_id=7622aa84a50c9f7609e2f7ed8bc85e81";
	String songStart = "tracks/";
	String songEnd = "&";

	public void testParseNullArtistTitle() {
		assertEquals("", JSONUtil.parseArtist(null));
		assertEquals("", JSONUtil.parseTitle(null));
	}
	
	public void testParseNoDividerArtistTitle() {
		String artist = "";
		String title = "simple title";
		assertEquals(artist.trim(), JSONUtil.parseArtist(title));
		assertEquals(title.trim(), JSONUtil.parseTitle(title));
	}
	
	public void testParseOneDividerArtistTitle() {
		String artist = "simple artist";
		String title = "simple title ";
		assertEquals(artist.trim(), JSONUtil.parseArtist(artist+JSONUtil.DIVIDER+title));
		assertEquals(title.trim(), JSONUtil.parseTitle(artist+JSONUtil.DIVIDER+title));
	}
	
	public void testParseMultipleDividersArtistTitle() {
		String artist = "this "+JSONUtil.DIVIDER+"is"
				+ JSONUtil.DIVIDER+" a test"+JSONUtil.DIVIDER;
		String title = "this should be just the title";
		assertEquals(artist.trim(), JSONUtil.parseArtist(artist+JSONUtil.DIVIDER+title));
		assertEquals(title.trim(), JSONUtil.parseTitle(artist+JSONUtil.DIVIDER+title));
	}
	
	public void testParseRandomArtistTitle() {
		String artist = "this2387%@#$ is aASn ASDFH TEAs98";
		String title = "ASHiuashetjer #$*@ sakldfj #@$#$@$F s9";
		assertEquals(artist.trim(), JSONUtil.parseArtist(artist+JSONUtil.DIVIDER+title));
		assertEquals(title.trim(), JSONUtil.parseTitle(artist+JSONUtil.DIVIDER+title));
	}
	
	public void testParseNullSongUrl() {
		assertEquals(null, JSONUtil.parseContent(null));
	}
	
	public void testParseNoSongUrl() {
		assertEquals(null, JSONUtil.parseContent("there/is/not/a/tRakc/here"));
	}
	
	public void testParseSimpleSongUrl() {
		int track = 440352;
		String test = songStart+track+songEnd;
		String expected = urlPrepend+track+urlAppend;
		assertEquals(expected, JSONUtil.parseContent(test));
	}
	
	public void testParseNoEndSongUrl() {
		int track = 440352;
		String test = songStart+track;
		assertEquals(null, JSONUtil.parseContent(test));
	}
	
	public void testParseNoStartSongUrl() {
		int track = 49871342;
		String test = "aseiuaspfs/as/"+track+songEnd+"as;dfas/asdkfj";
		assertEquals(null, JSONUtil.parseContent(test));
	}
	
	public void testParseLongSongUrl() {
		int track = 49871342;
		String test = "aseiuaspfs/as/"+songStart+track+songEnd+"$$%&&as;dfas/asdkfj";
		String expected = urlPrepend+track+urlAppend;
		assertEquals(expected, JSONUtil.parseContent(test));
	}
	
	public void testParseNullThumbnail() {
		assertEquals(null, JSONUtil.parseAttachments(null));
	}
	
}
