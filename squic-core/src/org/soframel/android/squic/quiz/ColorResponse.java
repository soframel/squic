
package org.soframel.android.squic.quiz;

public class ColorResponse
    extends TouchResponse
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6772272059818596486L;
	protected Color color;

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
    	String s=super.toString();
    	return s+", color="+color.getColorCode();
    }
}
