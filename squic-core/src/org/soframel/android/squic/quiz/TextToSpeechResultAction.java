package org.soframel.android.squic.quiz;

public class TextToSpeechResultAction extends ResultAction {
	private String text;

	
	
	public String toString(){
		return "TextToSpeech result action, text="+text;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}
}
