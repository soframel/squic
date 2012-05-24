package org.soframel.android.squic.quiz;

public class SpeechResultAction extends ResultAction {
	private SoundFile speechFile;

	public SoundFile getSpeechFile() {
		return speechFile;
	}

	public void setSpeechFile(SoundFile speechFile) {
		this.speechFile = speechFile;
	}
	
	public String toString(){
		return "Speech result action, file="+speechFile.getFile();
	}
}
