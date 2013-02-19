package org.soframel.android.squic.quiz.automatic;

import java.util.List;

import org.soframel.android.squic.quiz.question.MultipleChoiceQuestion;
import org.soframel.android.squic.quiz.question.Question;

public abstract class AutomaticQuestions {
	int nbQuestions;

	public AutomaticQuestions(int nbQuestions){
		this.nbQuestions=nbQuestions;
	}
	
	public int getNbQuestions() {
		return nbQuestions;
	}

	public void setNbQuestions(int nbQuestions) {
		this.nbQuestions = nbQuestions;
	}
	
	abstract public List<Question> initializeQuestions();
}
