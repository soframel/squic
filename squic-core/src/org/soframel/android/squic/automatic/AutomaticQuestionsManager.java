package org.soframel.android.squic.automatic;

import java.util.List;

import org.soframel.squic.quiz.automatic.AutomaticQuestions;
import org.soframel.squic.quiz.question.Question;

public abstract class AutomaticQuestionsManager {
	AutomaticQuestions automaticQuestions;

	abstract public List<Question> initializeQuestions();

	public AutomaticQuestions getAutomaticQuestions() {
		return automaticQuestions;
	}

	public void setAutomaticQuestions(AutomaticQuestions automaticQuestions) {
		this.automaticQuestions = automaticQuestions;
	}
	
	
}
