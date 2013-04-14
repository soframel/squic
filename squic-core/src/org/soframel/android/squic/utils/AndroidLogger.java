package org.soframel.android.squic.utils;

import org.soframel.squic.utils.SquicLogger;

import android.util.Log;

public class AndroidLogger implements SquicLogger {

	private String tag;
	public AndroidLogger(String tag){
		this.tag=tag;
	}
	
	@Override
	public void debug(String text) {
		Log.d(tag, text);
	}

	@Override
	public void info(String text) {
		Log.i(tag, text);
	}

	@Override
	public void warn(String text) {
		Log.w(tag, text);
	}

	@Override
	public void warn(String text, Throwable t) {
		Log.d(tag, text, t);
	}

	@Override
	public void error(String text) {
		Log.d(tag, text);
	}

	@Override
	public void error(String text, Throwable t) {
		Log.d(tag, text, t);
	}

}
