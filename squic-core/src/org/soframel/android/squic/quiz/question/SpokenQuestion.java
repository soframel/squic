package org.soframel.android.squic.quiz.question;

import org.soframel.android.squic.quiz.media.SoundFile;

public interface SpokenQuestion {
	public SoundFile getSpeechFile();
	public void setSpeechFile(SoundFile value);
}
