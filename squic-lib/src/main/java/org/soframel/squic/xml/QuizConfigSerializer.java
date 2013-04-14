package org.soframel.squic.xml;

import java.io.OutputStream;

import org.soframel.squic.quiz.Quiz;

public interface QuizConfigSerializer {
	public String serializeQuizConfig(Quiz quiz);
}
