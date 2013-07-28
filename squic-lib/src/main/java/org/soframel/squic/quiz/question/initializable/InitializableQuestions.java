package org.soframel.squic.quiz.question.initializable;

import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.quiz.response.Response;

import java.util.List;

/**
 * Set of questions that have to be initialized before usage
 * User: sophie.ramel
 * Date: 22/4/13
 */
public abstract class InitializableQuestions {

    public abstract List<Question> initialize() throws Exception;
    public abstract List<MultipleChoiceResponse> getResponses() throws Exception;

    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
