package org.soframel.squic.quiz.question;

/** 
 * this is not a multiple choice question but a writing question 
 **/
public class WritingQuestion implements TextToSpeechQuestion{
	private String id;
    private String text;
	private String response;

    public WritingQuestion() {
    }

    public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getText() {
		return text;
	}

	public void setText(String questionText) {
		this.text = questionText;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
