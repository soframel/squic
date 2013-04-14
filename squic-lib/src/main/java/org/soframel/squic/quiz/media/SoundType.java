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
package org.soframel.squic.quiz.media;
public enum SoundType {


    WAV("wav");
    private final String value;

    SoundType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SoundType fromValue(String v) {
        for (SoundType c: SoundType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
