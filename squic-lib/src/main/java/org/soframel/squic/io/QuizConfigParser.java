package org.soframel.squic.io;

import java.io.InputStream;

import org.soframel.squic.quiz.Quiz;

public interface QuizConfigParser {
	public Quiz parseQuizConfig(InputStream in);
}
