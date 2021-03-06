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
package org.soframel.squic.quiz.question;

import java.io.Serializable;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.soframel.squic.quiz.Level;
import org.soframel.squic.quiz.Quiz;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;

public class MultipleChoiceQuestion extends Question implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2304542277667305632L;

    protected List<MultipleChoiceResponse> possibleResponses;
    protected int nbRandomResponses=-1;
	protected List<String> correctIds;

    public MultipleChoiceQuestion() {
    }

    /**
	 * return the total number of possible responses for this question
	 * @return
	 */
	public int getNumberOfResponses(){
		if(nbRandomResponses>0)
			return possibleResponses.size()+nbRandomResponses;
		else
			return possibleResponses.size();
	}
	

    public List<String> getCorrectIds() {
		return correctIds;
	}

	public void setCorrectIds(List<String> correctIds) {
		this.correctIds = correctIds;
	}

	/**
     * Gets the value of the goodResponseId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public List<MultipleChoiceResponse> getPossibleResponses() {
        return possibleResponses;
    }
    
    /**
     * return a list of responses usable for this question: 
     * the listed possible responses, + random responses chosen from other responses
     * @return
     */
    public List<MultipleChoiceResponse> getResponsesWithRandom(Quiz quiz){
    	List<MultipleChoiceResponse> resps=new ArrayList<MultipleChoiceResponse>(possibleResponses);
    	
    	if(nbRandomResponses>0){    		
    		List<MultipleChoiceResponse> shuffledResponses=quiz.getResponses();
    		Collections.shuffle(shuffledResponses);
    		
    		Iterator<MultipleChoiceResponse> it=shuffledResponses.iterator();
    		int nbLeft=nbRandomResponses;
    		while(it.hasNext() && nbLeft>0){
    			MultipleChoiceResponse resp=it.next();
    			if(!resps.contains(resp)){
    				resps.add(resp);
    				nbLeft--;
    			}
    		}
    	}
    	return resps;
    }

    public List<MultipleChoiceResponse> findGoodResponses(){
    	List<MultipleChoiceResponse> goodRs=new ArrayList<MultipleChoiceResponse>();
    	for(MultipleChoiceResponse resp: this.getPossibleResponses()){
    		if(getCorrectIds().contains(resp.getId()))
    			goodRs.add(resp);
    	}
    	
    	return goodRs;
    }
    
    /**
     * Sets the value of the goodResponseId property.
     *
     *     
     */
    public void setPossibleResponses(List<MultipleChoiceResponse> responses) {
        this.possibleResponses=responses;
    }


    
    public int getNbRandomResponses() {
		return nbRandomResponses;
	}

	public void setNbRandomResponses(int nbRandomResponses) {
		this.nbRandomResponses = nbRandomResponses;
	}

    
    @Override
    public String toString(){
    	String s="Question "+id+", level="+level+", possible responses:\n";
    	for(MultipleChoiceResponse r: possibleResponses){
    		s=s+"possible response: "+r.toString()+"\n";
    	}
    	s=s+"good responses: ";
    	for(String respId: correctIds){
    		s=s+respId+", ";
    	}
    	return s;
    }

}
