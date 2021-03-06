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
package org.soframel.squic.quiz.response;

import org.soframel.squic.quiz.media.Color;

public class ColorResponse
    extends MultipleChoiceResponse
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6772272059818596486L;
	protected Color color;

    public ColorResponse() {
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link Color }
     *     
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link Color }
     *     
     */
    public void setColor(Color value) {
        this.color = value;
    }

    @Override
    public String toString(){
        if(this.getColor()!=null)
            return "color "+this.getColor().getColorCode();
        else
            return "color null";
    }
}
