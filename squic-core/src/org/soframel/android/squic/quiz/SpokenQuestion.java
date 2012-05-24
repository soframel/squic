package org.soframel.android.squic.quiz;

public class SpokenQuestion
    extends Question
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
