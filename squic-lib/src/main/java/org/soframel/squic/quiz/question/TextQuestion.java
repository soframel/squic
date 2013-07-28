package org.soframel.squic.quiz.question;

public interface TextQuestion extends PlayableQuestion {

	public String getText();

	public void setText(String text);
}
