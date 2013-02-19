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
public class ImageResponse
    extends MultipleChoiceResponse
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2939354465752538239L;
	protected String imageFile;

    /**
     * Gets the value of the imageFile property.
     * 
     */
    public String getImageFile() {
        return imageFile;
    }

    /**
     * Sets the value of the imageFile property.
     */
    public void setImageFile(String value) {
        this.imageFile = value;
    }

    @Override
    public String toString(){
    	String s=super.toString();
    	return s+", image="+imageFile;
    }
}
