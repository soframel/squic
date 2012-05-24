package org.soframel.android.squic.quiz;
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
