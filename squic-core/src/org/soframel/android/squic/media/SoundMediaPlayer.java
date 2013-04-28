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

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

public class SoundMediaPlayer implements ISoundFilePlayer {
	
	private static final String TAG = "Squic_SoundMediaPlayer";
	
	private Activity activity;
	private HashMap<Integer,MediaPlayer> players;
	//private List<MediaPlayer> players;
	//private Map<Integer,Boolean> loadedIds;
	
	public SoundMediaPlayer(Activity activity){
		this.activity=activity;
		// Set the hardware buttons to control the music
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Load the sound
		//loadedIds=new HashMap<Integer,Boolean>();
		
		//players=new ArrayList<MediaPlayer>();
		players=new HashMap<Integer, MediaPlayer>();
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
		try {			
			MediaPlayer player = MediaPlayer.create(activity, id);
			players.put(id, player);
			
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "IllegalArgumentException", e);
		} catch (SecurityException e) {
			Log.e(TAG, "SecurityException", e);
		} catch (IllegalStateException e) {
			Log.e(TAG, "IllegalStateException", e);
		}		
		
		//int is indice of player in list
		//return players.size()-1;
		
		return id;
	}
	
	public void playFile(int id){
		Log.d(TAG, "Playing sound from id "+id);		
		MediaPlayer player=players.get(id);
		player.setOnCompletionListener(null);
		player.start();
	}
	
	public void playFileSynchronous(int id, Object listener){
		Log.d(TAG, "Playing sound from id "+id);		
		MediaPlayer player=players.get(id);
				
		player.setOnCompletionListener((MediaPlayer.OnCompletionListener) listener);	
		player.start();
	}	
		
	public void release(){
		for(MediaPlayer player: players.values()){
			player.release();
		}
	}
	
	public MediaPlayer getMediaPlayer(int id){
		return players.get(id);
	}
}
