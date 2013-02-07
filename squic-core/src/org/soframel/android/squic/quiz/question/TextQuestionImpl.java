package org.soframel.android.squic.quiz.question;

public class TextQuestionImpl extends MultipleChoiceQuestion implements TextQuestion {

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
