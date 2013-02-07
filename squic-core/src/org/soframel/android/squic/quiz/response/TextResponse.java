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
package org.soframel.android.squic.quiz.response;

public class TextResponse extends TouchResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1738001428212904219L;
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	 @Override
	    public String toString(){
	    	String s=super.toString();
	    	return s+", text="+text;
	    }
}
