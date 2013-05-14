package org.soframel.squic.quiz.question.initializable;

import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.question.WritingQuestion;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sophie.ramel
 * Date: 22/4/13
 */
public class WritingQuestions extends WordQuestions{

    private String questionPrefix;
    private String questionSuffix;

    public WritingQuestions() {
    }

    @Override
    public List<Question> createQuestions() {
        List<Question> questions=new ArrayList<Question>(this.getDictionaryLines().size());
        for(DictionaryLine line: this.getDictionaryLines()){
            //question
            WritingQuestion question=new WritingQuestion();
            questions.add(question);
            question.setResponse(line.getName());
            String questionText=questionPrefix+line.getName()+questionSuffix;
            question.setText(questionText);
        }
        return questions;
    }

    @Override
    public List<MultipleChoiceResponse> getResponses() throws Exception {
        //Not a MCQ
        return null;
    }


    public String getQuestionPrefix() {
        return questionPrefix;
    }

    public void setQuestionPrefix(String questionPrefix) {
        this.questionPrefix = questionPrefix;
    }

    public String getQuestionSuffix() {
        return questionSuffix;
    }

    public void setQuestionSuffix(String questionSuffix) {
        this.questionSuffix = questionSuffix;
    }

}
