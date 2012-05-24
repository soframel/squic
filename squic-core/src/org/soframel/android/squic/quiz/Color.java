package org.soframel.android.squic.quiz;

import java.io.Serializable;

public class Color implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2414442899564535271L;
	protected String colorCode;

    /**
     * Gets the value of the colorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * Sets the value of the colorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColorCode(String value) {
        this.colorCode = value;
    }

}
