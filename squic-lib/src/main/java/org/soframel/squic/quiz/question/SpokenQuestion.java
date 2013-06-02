package org.soframel.squic.quiz.question;

import org.soframel.squic.quiz.media.SoundFile;

public interface SpokenQuestion extends Question{
	public SoundFile getSpeechFile();
	public void setSpeechFile(SoundFile value);
}
