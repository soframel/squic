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

import java.io.Serializable;

public class MultipleChoiceResponse  implements Response, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4653846341139938106L;
	protected String id;

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
    
    

    @Override
	public boolean equals(Object o) {
		if(o!=null && o instanceof MultipleChoiceResponse){
			MultipleChoiceResponse r=(MultipleChoiceResponse)o;
			return id.equals(r.getId());
		}
		else
			return false;
	}

	@Override
    public String toString(){
    	return "Response "+id;
    }
}
