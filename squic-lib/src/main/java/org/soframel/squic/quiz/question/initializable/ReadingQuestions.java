package org.soframel.squic.quiz.question.initializable;

import org.soframel.squic.quiz.question.MultipleChoiceTextToSpeechQuestion;
import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.quiz.response.MultipleChoiceResponse;
import org.soframel.squic.quiz.response.TextResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: sophie.ramel
 * Date: 22/4/13
 */
public class ReadingQuestions extends WordQuestions {

    private String questionPrefix;
    private String questionSuffix;
    private int nbRandom;
    List<MultipleChoiceResponse> responses;

    public ReadingQuestions() {
    }

    @Override
    public List<Question> createQuestions() {
        List<Question> questions=new ArrayList<Question>(this.getDictionaryLines().size());
        Map<String, String> genres=new HashMap<String, String>();
        int i=0;
        responses=new ArrayList<MultipleChoiceResponse>(this.getDictionaryLines().size());
        for(DictionaryLine line: this.getDictionaryLines()){
            //question
            String questionS=questionPrefix+" "+line.getGenre() +" "+line.getName()+" "+questionSuffix;
            MultipleChoiceTextToSpeechQuestion question=new MultipleChoiceTextToSpeechQuestion();
            questions.add(question);
            question.setText(questionS);
            question.setId((Integer.valueOf(i)).toString());

            //response
            TextResponse response=new TextResponse();
            response.setText(line.getName());
            response.setId((Integer.valueOf(i)).toString());
            responses.add(response);

            ArrayList<MultipleChoiceResponse> possibleResps=new ArrayList<MultipleChoiceResponse>(1);
            possibleResps.add(response);
            question.setPossibleResponses(possibleResps);
            ArrayList<String> correctIds=new ArrayList<String>(1);
            correctIds.add(response.getId());
            question.setCorrectIds(correctIds);

            //also load random responses
            if(nbRandom>0)
                question.setNbRandomResponses(nbRandom);

            i++;
        }
        return questions;
    }

    @Override
    public List<MultipleChoiceResponse> getResponses() throws Exception {
       return responses;
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

    public int getNbRandom() {
        return nbRandom;
    }

    public void setNbRandom(int nbRandom) {
        this.nbRandom = nbRandom;
    }
}
