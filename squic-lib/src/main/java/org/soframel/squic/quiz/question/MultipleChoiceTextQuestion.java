package org.soframel.squic.quiz.question;

public class MultipleChoiceTextQuestion extends MultipleChoiceQuestion implements TextQuestion {

	private String text;

	public MultipleChoiceTextQuestion(String text){
		this.text=text;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
