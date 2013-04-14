package org.soframel.squic.quiz.automatic;


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
	
}
