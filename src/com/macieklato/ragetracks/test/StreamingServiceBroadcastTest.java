package com.macieklato.ragetracks.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.macieklato.ragetracks.controller.ApplicationController;
import com.macieklato.ragetracks.controller.SongController;
import com.macieklato.ragetracks.model.Song;
import com.macieklato.ragetracks.service.StreamingBackgroundService;
import com.macieklato.ragetracks.util.Network;

import junit.framework.TestCase;

public class StreamingServiceBroadcastTest extends TestCase {

	int songLoadCount = 0;
	int songPlayCount = 0;
	int songPauseCount = 0;
	int songStopCount = 0;
	int songErrorCount = 0;
	int songPositionCount = 0;
	int otherCount = 0;

	ApplicationController ac;
	SongController sc;
	Song s1;
	Song s2;
	BroadcastReceiver br;

	public void setWifi(boolean val) {
		ac = ApplicationController.getInstance();
		WifiManager wifiManager = (WifiManager) ac.getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(val);
		if (val)
			pause();
	}

	public void before() {
		songLoadCount = 0;
		songPlayCount = 0;
		songPauseCount = 0;
		songStopCount = 0;
		songErrorCount = 0;
		songPositionCount = 0;
		otherCount = 0;

		ac = ApplicationController.getInstance();
		ac.reset();
		ac.cancelAll();

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

		br = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				int action = intent.getIntExtra(
						StreamingBackgroundService.EXTRA_UPDATE, -1);

				switch (action) {
				case StreamingBackgroundService.UPDATE_PLAY:
					songPlayCount++;
					break;
				case StreamingBackgroundService.UPDATE_LOADING:
					songLoadCount++;
					break;
				case StreamingBackgroundService.UPDATE_PAUSE:
					songPauseCount++;
					break;
				case StreamingBackgroundService.UPDATE_STOP:
					songStopCount++;
					break;
				case StreamingBackgroundService.UPDATE_ERROR:
					songErrorCount++;
					break;
				case StreamingBackgroundService.UPDATE_POSITION:
					songPositionCount++;
					break;
				default:
					otherCount++;
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(StreamingBackgroundService.ACTION_UPDATE);
		ac.registerReceiver(br, filter);
	}

	public void after() {
		ac.sendCommand(StreamingBackgroundService.ACTION_KILL);
		pause();
		ac.unregisterReceiver(br);
		ac.reset();
		ac.cancelAll();
		sc.reset();
	}

	public void pause() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
	public void testLoad() {
		before();
		setWifi(true);
		
		assertEquals(0, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);

		sc.addSong(s1);
		ac.sendCommand(StreamingBackgroundService.ACTION_LOAD);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		
		after();
	}
	
	public void testStop() {
		before();
		
		assertEquals(0, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);

		sc.addSong(s1);
		ac.sendCommand(StreamingBackgroundService.ACTION_LOAD);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		ac.sendCommand(StreamingBackgroundService.ACTION_STOP);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(1, songStopCount);
		assertEquals(0, songErrorCount);
		
		
		after();
	}
	
	public void testNext() {
		before();
		
		assertEquals(0, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);

		sc.addSong(s1);
		ac.sendCommand(StreamingBackgroundService.ACTION_LOAD);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		sc.addSong(s2);
		ac.sendCommand(StreamingBackgroundService.ACTION_NEXT);
		pause();
		
		assertEquals(2, songLoadCount);
		assertEquals(2, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(1, songStopCount);
		assertEquals(0, songErrorCount);
		
		assertEquals(s2, sc.getSong());
		
		after();
	}
	
	public void testPrevious() {
		before();
		
		assertEquals(0, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);

		sc.addSong(s1);
		sc.addSong(s2);
		sc.setActiveSong(s2);
		ac.sendCommand(StreamingBackgroundService.ACTION_LOAD);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		ac.sendCommand(StreamingBackgroundService.ACTION_PREVIOUS);
		pause();
		
		assertEquals(2, songLoadCount);
		assertEquals(2, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(1, songStopCount);
		assertEquals(0, songErrorCount);
		
		assertEquals(s1, sc.getSong());
		
		after();
	}
	
	public void testPause() {
		before();
		
		assertEquals(0, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);

		sc.addSong(s1);
		ac.sendCommand(StreamingBackgroundService.ACTION_LOAD);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		ac.sendCommand(StreamingBackgroundService.ACTION_PAUSE);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(1, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		after();
	}
	
	public void testPlay() {
		before();
		
		assertEquals(0, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);

		sc.addSong(s1);
		ac.sendCommand(StreamingBackgroundService.ACTION_LOAD);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		ac.sendCommand(StreamingBackgroundService.ACTION_PAUSE);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(1, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		ac.sendCommand(StreamingBackgroundService.ACTION_PLAY);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(2, songPlayCount);
		assertEquals(1, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		after();
	}
	
	public void testTogglePlayback() {
		before();
		
		assertEquals(0, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);

		sc.addSong(s1);
		ac.sendCommand(StreamingBackgroundService.ACTION_LOAD);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		ac.sendCommand(StreamingBackgroundService.ACTION_TOGGLE_PLAYBACK);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(1, songPlayCount);
		assertEquals(1, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		ac.sendCommand(StreamingBackgroundService.ACTION_TOGGLE_PLAYBACK);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(2, songPlayCount);
		assertEquals(1, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);
		
		after();
	}
	/*
	public void testError() {
		before();
		setWifi(false);
		
		assertEquals(0, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(0, songErrorCount);

		sc.addSong(s1);
		ac.sendCommand(StreamingBackgroundService.ACTION_LOAD);
		pause();
		
		assertEquals(1, songLoadCount);
		assertEquals(0, songPlayCount);
		assertEquals(0, songPauseCount);
		assertEquals(0, songStopCount);
		assertEquals(1, songErrorCount);
		
		after();
	}
	*/
}
