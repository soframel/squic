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

import org.soframel.squic.quiz.media.SoundFile;

public class SpeechResultAction extends ResultAction {


    private SoundFile speechFile;

    public SpeechResultAction(){

    }

	public SoundFile getSpeechFile() {
		return speechFile;
	}

	public void setSpeechFile(SoundFile speechFile) {
		this.speechFile = speechFile;
	}
	
	public String toString(){
        if(speechFile==null)
            return "Speech result action, no speech file";
        else
		    return "Speech result action, file="+speechFile.getFile();
	}


}
