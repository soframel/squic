package org.soframel.squic.quiz.automatic;


public abstract class AutomaticQuestions {
	int nbQuestions;

    String id;

    public AutomaticQuestions(){

    }

	public AutomaticQuestions(int nbQuestions){
		this.nbQuestions=nbQuestions;
	}
	
	public int getNbQuestions() {
		return nbQuestions;
	}

	public void setNbQuestions(int nbQuestions) {
		this.nbQuestions = nbQuestions;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
