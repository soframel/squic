package org.soframel.android.squic.quiz;
public class ImageResponse
    extends TouchResponse
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
