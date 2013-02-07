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
package org.soframel.android.squic.quiz.question;


public class TextToSpeechQuestion
    extends MultipleChoiceQuestion
    implements TextQuestion{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2100200709780474983L;

	protected String text;
	
	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	@Override
    public String toString(){
    	String s=super.toString();
    	s=s+", text="+text;
    	return s;
    }
}
