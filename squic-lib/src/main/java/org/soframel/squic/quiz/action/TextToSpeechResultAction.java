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
package org.soframel.squic.quiz.action;

public class TextToSpeechResultAction extends ResultAction {
	private String text;

    public TextToSpeechResultAction() {
    }

    public String toString(){
		return "TextToSpeech result action, text="+text;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}
}
