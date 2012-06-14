package org.soframel.android.squic.quiz;

public class TextQuestionImpl extends Question implements TextQuestion {

	private String text;

	public TextQuestionImpl(String text){
		this.text=text;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
