/*******************************************************************************
 * Copyright (c) 2012 soframel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     soframel - initial API and implementation
 ******************************************************************************/
package org.soframel.android.squic.quiz;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.soframel.android.squic.quiz.automatic.AutomaticQuestions;

public class Quiz implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3394775723105410446L;
	
	protected List<Question> questions;
	protected AutomaticQuestions automaticQuestions;
    protected List<Response> responses;
    protected String name;
    protected String id;
    protected Locale language;
    protected float widthToHeightResponsesRatio=1;
	protected ResultAction goodResultAction;
    protected ResultAction badResultAction;
    protected ResultAction quizFinishedAction;
    protected String icon;
    protected int nbQuestions=0;
    protected GameMode gameMode;

    public String getResPrefix(){
    	return "id"+id;
    }
    
    public int getNbQuestions() {
		return nbQuestions;
	}

	public void setNbQuestions(int nbQuestions) {
		this.nbQuestions = nbQuestions;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}
    
	/**
     * Gets the value of the questions property.
     * 
     * @return
     *     possible object is
     *     {@link Quiz.Questions }
     *     
     */
    public List<Question> getQuestions() {
    	if(automaticQuestions!=null){
    		return automaticQuestions.initializeQuestions();
    	}
    	else
    		return questions;
    }

    /**
     * Sets the value of the questions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Quiz.Questions }
     *     
     */
    public void setQuestions(List<Question> value) {
        this.questions = value;
    }

    /**
     * Gets the value of the responses property.
     * 
     * @return
     *     possible object is
     *     {@link Quiz.Responses }
     *     
     */
    public List<Response> getResponses() {
        return responses;
    }

    /**
     * Sets the value of the responses property.
     * 
     * @param value
     *     allowed object is
     *     {@link Quiz.Responses }
     *     
     */
    public void setResponses(List<Response> value) {
        this.responses = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }


    public ResultAction getGoodResultAction() {
		return goodResultAction;
	}

	public void setGoodResultAction(ResultAction goodResultAction) {
		this.goodResultAction = goodResultAction;
	}

	public ResultAction getBadResultAction() {
		return badResultAction;
	}

	public void setBadResultAction(ResultAction badResultAction) {
		this.badResultAction = badResultAction;
	}
	

    public ResultAction getQuizFinishedAction() {
		return quizFinishedAction;
	}

	public void setQuizFinishedAction(ResultAction quizFinishedAction) {
		this.quizFinishedAction = quizFinishedAction;
	}
	public float getWidthToHeightResponsesRatio() {
		return widthToHeightResponsesRatio;
	}

	public void setWidthToHeightResponsesRatio(float widthToHeightResponsesRatio) {
		this.widthToHeightResponsesRatio = widthToHeightResponsesRatio;
	}


    
    @Override
    public String toString(){
    	String s="Quiz "+name+" (id="+id+"):\n";
    	s=s+"Good result: "+goodResultAction+"\n";
    	s=s+"Bad result: "+badResultAction+"\n";
    	if(questions!=null){
	    	s=s+"Questions:\n";
	    	for(Question q: questions){
	    		s=s+q.toString()+"\n";
	    	}
    	}
    	if(responses!=null){
	    	s=s+"Responses:\n";
	    	for(Response r: responses){
	    		s=s+r.toString()+"\n";
	    	}
    	}
    	if(automaticQuestions!=null)
    		s=s+"Automatic questions: "+automaticQuestions.toString()+"\n";
    	return s;
    }

	public AutomaticQuestions getAutomaticQuestions() {
		return automaticQuestions;
	}

	public void setAutomaticQuestions(AutomaticQuestions automaticQuestions) {
		this.automaticQuestions = automaticQuestions;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode mode) {
		this.gameMode = mode;
	}

}
