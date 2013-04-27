package org.soframel.squic.io;

import org.soframel.squic.quiz.Quiz;

public interface QuizConfigSerializer {
	public String serializeQuizConfig(Quiz quiz);
}
