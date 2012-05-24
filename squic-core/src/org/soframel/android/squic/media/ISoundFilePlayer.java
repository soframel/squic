package org.soframel.android.squic.media;

public interface ISoundFilePlayer {
	public int loadFile(String resourceName);
	public void playFile(int id);
	public void playFileSynchronous(int id, Object listener);
	public void release();
}
