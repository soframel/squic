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
public interface InitializableQuestions {

    public List<Question> initialize() throws Exception;
    public List<MultipleChoiceResponse> getResponses() throws Exception;
}
