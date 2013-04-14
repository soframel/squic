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

import org.soframel.squic.quiz.media.SoundFile;

public class MultipleChoiceSpokenQuestion
    extends MultipleChoiceQuestion implements SpokenQuestion
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8828719245294773520L;
	protected SoundFile speechFile;

    /**
     * Gets the value of the speechFile property.
     * 
     * @return
     *     possible object is
     *     {@link SoundFile }
     *     
     */
    public SoundFile getSpeechFile() {
        return speechFile;
    }

    /**
     * Sets the value of the speechFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoundFile }
     *     
     */
    public void setSpeechFile(SoundFile value) {
        this.speechFile = value;
    }

    @Override
    public String toString(){
    	String s=super.toString();
    	s=s+", speechFile="+speechFile.getFile();
    	return s;
    }
}
