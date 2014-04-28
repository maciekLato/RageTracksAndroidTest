package com.macieklato.ragetracks.test;

import junit.framework.TestCase;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.macieklato.ragetracks.controller.ApplicationController;
import com.macieklato.ragetracks.controller.SongController;
import com.macieklato.ragetracks.model.Song;
import com.macieklato.ragetracks.util.Network;

public class ApplicationControllerBroadcastTest extends TestCase {

	int songLoadingCount = 0;
	int songFinishLoadingCount = 0;
	int songErrorLoadingCount = 0;
	int waveformLoadingCount = 0;
	int waveformFinishLoadingCount = 0;
	int waveformErrorLoadingCount = 0;
	int finishCount = 0;
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
		songLoadingCount = 0;
		songFinishLoadingCount = 0;
		songErrorLoadingCount = 0;
		waveformLoadingCount = 0;
		waveformFinishLoadingCount = 0;
		waveformErrorLoadingCount = 0;
		finishCount = 0;
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
						ApplicationController.EXTRA_UPDATE, -1);
				switch (action) {
				case ApplicationController.UPDATE_LOADING_SONGS:
					songLoadingCount++;
					break;
				case ApplicationController.UPDATE_FINISH_LOADING_SONGS:
					songFinishLoadingCount++;
					break;
				case ApplicationController.UPDATE_ERROR_LOADING_SONGS:
					songErrorLoadingCount++;
					break;
				case ApplicationController.UPDATE_LOADING_WAVEFORMS:
					waveformLoadingCount++;
					break;
				case ApplicationController.UPDATE_FINISH_LOADING_WAVEFORMS:
					waveformFinishLoadingCount++;
					break;
				case ApplicationController.UPDATE_ERROR_LOADING_WAVEFORMS:
					waveformErrorLoadingCount++;
					break;
				case ApplicationController.UPDATE_FINISH:
					finishCount++;
					break;
				default:
					otherCount++;
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(ApplicationController.ACTION_UPDATE);
		ac.registerReceiver(br, filter);
	}

	public void after() {
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

	public void testLoadSongs() {
		before();
		setWifi(true);

		assertEquals(0, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		ac.loadSongs(1);
		pause();

		assertEquals(1, songLoadingCount);
		assertEquals(1, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(1, waveformLoadingCount);
		assertEquals(1, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		after();
	}

	public void testNextLoadsMore() {
		before();
		setWifi(true);

		assertEquals(0, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		sc.addSong(s1);
		sc.addSong(s2);
		sc.nextSong();
		pause();

		assertEquals(0, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		sc.nextSong();
		pause();

		assertEquals(1, songLoadingCount);
		assertEquals(1, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(1, waveformLoadingCount);
		assertEquals(1, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		after();
	}

	public void testPoorWifiLoad() {
		before();
		setWifi(false);

		assertEquals(0, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		ac.loadSongs(1);
		pause();

		assertEquals(1, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(1, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		after();
	}

	public void testPoorWifiNext() {
		before();
		setWifi(false);
		assertEquals(0, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		sc.addSong(s1);
		sc.addSong(s2);
		sc.nextSong();
		pause();

		assertEquals(0, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		sc.nextSong();
		pause();

		assertEquals(1, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(1, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		after();
	}

	public void testDestroy() {
		before();

		assertEquals(0, songLoadingCount);
		assertEquals(0, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(0, finishCount);
		assertEquals(0, otherCount);

		ac.destroy();
		pause();

		assertEquals(0, songLoadingCount);
		assertEquals(1, songFinishLoadingCount);
		assertEquals(0, songErrorLoadingCount);
		assertEquals(0, waveformLoadingCount);
		assertEquals(0, waveformFinishLoadingCount);
		assertEquals(0, waveformErrorLoadingCount);
		assertEquals(1, finishCount);
		assertEquals(0, otherCount);

		after();
	}
}
