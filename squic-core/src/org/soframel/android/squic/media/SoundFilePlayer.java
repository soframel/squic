/*******************************************************************************
 * Copyright (c) 2012 soframel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     soframel - initial API and implementation
 ******************************************************************************/
package org.soframel.android.squic.media;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

public class SoundFilePlayer {
	
	private static final String TAG = "SoundFilePlayer";
	
	private Activity activity;
	private SoundPool soundPool;
	private Map<Integer,Boolean> loadedIds;
	
	public SoundFilePlayer(Activity activity){
		this.activity=activity;
		// Set the hardware buttons to control the music
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Load the sound
		loadedIds=new HashMap<Integer,Boolean>();
		//soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loadedIds.put(sampleId, Boolean.TRUE);
				Log.d(TAG, "loaded sound file "+sampleId+": status: "+status);
			}
		});		
	}
	
	/**
	 * load a music resource
	 * @param resourceName
	 * @return
	 */
	public int loadFile(String resourceName){
		//find internal android identifier
		int id=activity.getResources().getIdentifier(resourceName, "raw",activity.getApplicationInfo().packageName);
		Log.d(TAG, "Loading resource "+resourceName+" from id "+id);
		return soundPool.load(activity, id, 1);
	}
	
	public void playFile(int id){
		Log.d(TAG, "Playing sound from id "+id);
		// Getting the user sound settings
		AudioManager audioManager = (AudioManager) activity.getSystemService(Activity.AUDIO_SERVICE);
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		// Is the sound loaded already?
		int nbRetries=10;
		boolean loaded=isLoaded(id);
		while(!loaded && nbRetries>0){
			try {
				nbRetries--;
				Log.d(TAG, "Waiting for sound to be loaded");
				Thread.sleep(200);
				loaded=isLoaded(id);
			} catch (InterruptedException e1) {
			}			
		}
		int r=soundPool.play(id, volume, volume, 1, 0, 1f);
		Log.d(TAG, "Played sound: "+r);
	}
	
	public boolean isLoaded(int id){
		Boolean loadedB=this.loadedIds.get(id);
		if(loadedB==null)
			return false;
		else 
			return loadedB.booleanValue();
	}
	
	public void release(){
		soundPool.release();
	}
}
