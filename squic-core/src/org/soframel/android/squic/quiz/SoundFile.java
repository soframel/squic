package org.soframel.android.squic.quiz;

import java.io.Serializable;

public class SoundFile  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3824904355178539748L;

	protected String file;

    protected SoundType type;

    /**
     * Gets the value of the file property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the value of the file property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFile(String value) {
        this.file = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link SoundType }
     *     
     */
    public SoundType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoundType }
     *     
     */
    public void setType(SoundType value) {
        this.type = value;
    }

}
