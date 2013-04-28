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
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

public class TextToSpeechManager implements OnInitListener {
	private final static String TAG="Squic_TextToSpeechManager";
	
	private Activity activity;
	private TextToSpeech tts;
	private Locale locale;
	private String initialText;
	private boolean initialized=false;
	//private Map<String,String> optionsMap;
	private OnUtteranceCompletedListener completedListener;

	
	public TextToSpeechManager(Activity activity, Locale locale, String initialText, OnUtteranceCompletedListener listener){
		this.activity=activity;
		this.locale=locale;
		this.completedListener=listener;
		if(locale==null)
			this.locale=Locale.FRENCH;
		this.initialText=initialText;
		
		 tts = new TextToSpeech(activity, this);
		 tts.setSpeechRate(0.9f);
		 tts.setPitch(0.8f);	
	}
	

	@Override
	public void onInit(int status) {
		initialized=true;
		Log.d(TAG, "initialized status: "+status);
		if (status == TextToSpeech.SUCCESS) {  			
			 int result = tts.setLanguage(locale);  
	         if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) 
	              Log.e(TAG, "Language is not available: "+locale);  
	         else{
	        	 Log.d(TAG, "Locale set, result="+result);
	        	 
	        	 if(completedListener!=null){
	        		 int listenerSet=tts.setOnUtteranceCompletedListener(completedListener);
	        		 if(listenerSet==TextToSpeech.ERROR)
	        			 Log.e(TAG, "setOnUtteranceCompletedListener not set: "+listenerSet);
	        		 else
	        			 Log.d(TAG, "setOnUtteranceCompletedListener set correctly");
	        	 }
	        	 
	        	 if(initialText!=null && !initialText.equals("")){
	        		 Log.d(TAG, "Playing initial text: "+initialText);
	        		 this.playText(initialText);
	        	 }
	         }
		}
		else{
			Log.e(TAG, "onInit but status is not success: "+status);
		}
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	public void playText(String text){
		this.playText(text, null);
	}
	public void playText(String text, HashMap<String,String> options){		
		Log.d(TAG, "playing text: "+text);		
		if(tts!=null){
			if(!isInitialized()){
				int nbRetries=10;
				while(!isInitialized() && nbRetries>0){
					try {
						nbRetries--;
						Log.d(TAG, "Waiting for tts to be initialized");
						Thread.sleep(100);
					} catch (InterruptedException e1) {
					}			
				}
				if(!isInitialized())
					Log.e(TAG, "TTS still not initialized after waiting");
			}
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, options);
		}
		else
			Log.e(TAG, "tts is null!");
	}
	
	public void playSynchronousText(String text, String textId){
		
		HashMap<String,String> optionsMap=new HashMap<String,String>();
		optionsMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, textId);
		
		this.playText(text, optionsMap);		
	}
	
	public void finish(){
		tts.shutdown();
	}

}
