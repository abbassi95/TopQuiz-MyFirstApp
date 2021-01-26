package com.example.topquiz.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private List<Question> mQuestionList;
    private static int mNextQuestionIndex=-1;



    public QuestionBank(List<Question> questionList) {
        // Shuffle the question list before storing it
        mQuestionList = questionList;
        Collections.shuffle(mQuestionList);

    }
    public QuestionBank(){

    }


    public Question getQuestion() {
        // Loop over the questions and return a new one at each call
        if (mNextQuestionIndex+1>=mQuestionList.size()){mNextQuestionIndex=0;}
        else {mNextQuestionIndex +=1;}
        return mQuestionList.get(mNextQuestionIndex);
    }


}
