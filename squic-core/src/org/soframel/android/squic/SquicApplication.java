package org.soframel.android.squic;

import java.util.Map;

import android.app.Application;

import org.soframel.android.squic.quiz.Quiz;

public class SquicApplication extends Application {
	private Map<String,Quiz> quizzes;

	public Map<String,Quiz> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(Map<String,Quiz> quizzes) {
		this.quizzes = quizzes;
	}
}
