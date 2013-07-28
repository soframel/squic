package org.soframel.squic.quiz.question;

import org.soframel.squic.quiz.Level;

public class Question {
    protected String id;
    protected Level level;

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

    /**
     * Gets the value of the level property.
     *
     * @return
     *     possible object is
     *     {@link Level }
     *
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     *
     * @param value
     *     allowed object is
     *     {@link Level }
     *
     */
    public void setLevel(Level value) {
        this.level = value;
    }
}
