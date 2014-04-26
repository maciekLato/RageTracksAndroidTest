package com.macieklato.ragetracks.test;

import org.json.JSONArray;
import org.json.JSONObject;

import junit.framework.TestCase;

import com.macieklato.ragetracks.util.JSONUtil;

public class JSONUtilTest extends TestCase {

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
		assertEquals(artist.trim(),
				JSONUtil.parseArtist(artist + JSONUtil.DIVIDER + title));
		assertEquals(title.trim(),
				JSONUtil.parseTitle(artist + JSONUtil.DIVIDER + title));
	}

	public void testParseMultipleDividersArtistTitle() {
		String artist = "this " + JSONUtil.DIVIDER + "is" + JSONUtil.DIVIDER
				+ " a test" + JSONUtil.DIVIDER;
		String title = "this should be just the title";
		assertEquals(artist.trim(),
				JSONUtil.parseArtist(artist + JSONUtil.DIVIDER + title));
		assertEquals(title.trim(),
				JSONUtil.parseTitle(artist + JSONUtil.DIVIDER + title));
	}

	public void testParseRandomArtistTitle() {
		String artist = "this2387%@#$ is aASn ASDFH TEAs98";
		String title = "ASHiuashetjer #$*@ sakldfj #@$#$@$F s9";
		assertEquals(artist.trim(),
				JSONUtil.parseArtist(artist + JSONUtil.DIVIDER + title));
		assertEquals(title.trim(),
				JSONUtil.parseTitle(artist + JSONUtil.DIVIDER + title));
	}

	public void testParseNullSongUrl() {
		assertEquals(null, JSONUtil.parseTrack(null));
	}

	public void testParseNoSongUrl() {
		assertEquals(null, JSONUtil.parseTrack("there/is/not/a/tRakc/here"));
	}

	public void testParseSimpleSongUrl() {
		int track = 440352;
		String test = songStart + track + songEnd;
		String expected = ""+track;
		assertEquals(expected, JSONUtil.parseTrack(test));
	}

	public void testParseNoEndSongUrl() {
		int track = 440352;
		String test = songStart + track;
		assertEquals(null, JSONUtil.parseTrack(test));
	}

	public void testParseNoStartSongUrl() {
		int track = 49871342;
		String test = "aseiuaspfs/as/" + track + songEnd + "as;dfas/asdkfj";
		assertEquals(null, JSONUtil.parseTrack(test));
	}

	public void testParseLongSongUrl() {
		int track = 49871342;
		String test = "aseiuaspfs/as/" + songStart + track + songEnd
				+ "$$%&&as;dfas/asdkfj";
		String expected = "" + track;
		assertEquals(expected, JSONUtil.parseTrack(test));
	}

	public void testParseNullThumbnail() {
		assertEquals(null, JSONUtil.parseAttachments(null));
	}

	public void testParseEmptyArrayThumbnail() {
		JSONArray arr = new JSONArray();
		assertEquals(null, JSONUtil.parseAttachments(arr));
	}

	public void testParseNoImageObjectThumbnail() {
		JSONArray arr = new JSONArray();
		arr.put(new JSONObject());
		assertEquals(null, JSONUtil.parseAttachments(arr));
	}

	public void testParseNoThubmnailObjectThumbnail() {
		try {
			JSONArray arr = new JSONArray();
			JSONObject idx0 = new JSONObject();
			arr.put(0, idx0);
			idx0.put("images", new JSONObject());
			assertEquals(null, JSONUtil.parseAttachments(arr));
		} catch (Exception e) {
			fail();
		}
	}

	public void testParseNoUrlStringThumbnail() {
		try {
			JSONArray arr = new JSONArray();
			JSONObject idx0 = new JSONObject();
			arr.put(0, idx0);
			JSONObject images = new JSONObject();
			idx0.put("images", images);
			images.put("thumbnail", new JSONObject());
			assertEquals(null, JSONUtil.parseAttachments(arr));
		} catch (Exception e) {
			fail();
		}
	}

	public void testParseGoodUrlThumbnail() {
		try {
			JSONArray arr = new JSONArray();
			JSONObject idx0 = new JSONObject();
			arr.put(0, idx0);
			JSONObject images = new JSONObject();
			idx0.put("images", images);
			JSONObject thumbnail = new JSONObject();
			images.put("large", thumbnail);
			String url = "testURL";
			thumbnail.put("url", url);
			assertEquals(url, JSONUtil.parseAttachments(arr));
		} catch (Exception e) {
			fail();
		}
	}

	public void testParseGoodUrlFromStringThumbnail() {
		try {
			String url = "http://test.test";
			JSONArray arr = new JSONArray("[{images:{large:{url:\"" + url
					+ "\"}}}]");
			assertEquals(url, JSONUtil.parseAttachments(arr));
		} catch (Exception e) {
			fail();
		}
	}
}
