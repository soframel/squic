package org.soframel.android.squic.quiz;

import java.io.Serializable;

public class Response  implements Serializable{

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
		if(o!=null && o instanceof Response){
			Response r=(Response)o;
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
