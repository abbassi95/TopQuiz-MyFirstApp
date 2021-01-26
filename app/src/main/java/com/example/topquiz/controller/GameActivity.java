package com.example.topquiz.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import static java.lang.System.out;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquiz.R;
import com.example.topquiz.model.Question;
import com.example.topquiz.model.QuestionBank;


import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mQuestion;
    private Button  mAnswer_1;
    private Button  mAnswer_2;
    private Button  mAnswer_3;
    private Button  mAnswer_4;
    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;
    private int mNumberOfQuestions;
    private int mScore;
    private boolean mEnableTouchEvents = true;
    public static final String BUNDLE_EXTRA_SCORE =
            GameActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_SCORE");
    public static final String BUNDLE_STATE_SCORE =
            GameActivity.class.getCanonicalName().concat("currentScore");
    public static final String BUNDLE_STATE_QUESTION =
            GameActivity.class.getCanonicalName().concat("currentQuestion");

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE,mScore);
        outState.putInt(BUNDLE_STATE_QUESTION,mNumberOfQuestions);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        out.println("GameActivity::onStart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("GameActivity::onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        out.println("GameActivity::onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("GameActivity::onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        out.println("GameActivity::onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        out.println("GameActivity::onCreate");
        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            int a = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mNumberOfQuestions = 4;
        }
        setContentView(R.layout.activity_game);
        mQuestionBank= this.generateQuestions();
        mQuestion = (TextView) findViewById(R.id.activity_game_question_text);
        mAnswer_1 = (Button) findViewById(R.id.activity_game_answer1_btn);
        mAnswer_2 = (Button) findViewById(R.id.activity_game_answer2_btn);
        mAnswer_3 = (Button) findViewById(R.id.activity_game_answer3_btn);
        mAnswer_4 = (Button) findViewById(R.id.activity_game_answer4_btn);

        // Use the same listener for all four buttons.
        mAnswer_1.setOnClickListener(this);
        mAnswer_2.setOnClickListener(this);
        mAnswer_3.setOnClickListener(this);
        mAnswer_4.setOnClickListener(this);
//
        // Use the tag property to 'name' the buttons
        mAnswer_1.setTag(0);
        mAnswer_2.setTag(1);
        mAnswer_3.setTag(2);
        mAnswer_4.setTag(3);

//        mNumberOfQuestions = 4;
//        mScore = 0;
//
        for (View button : new View[]{mAnswer_1,mAnswer_2,mAnswer_3,mAnswer_4} ){
            button.setOnClickListener(GameActivity.this);
        }
//
        mCurrentQuestion = mQuestionBank.getQuestion();
        displayQuestion(mCurrentQuestion);


    }
    private void displayQuestion(final Question question) {
        // Set the text for the question text view and the four buttons
        mQuestion.setText(question.getQuestion());
        mQuestion.append("(Question n° "+mNumberOfQuestions+")");
        mAnswer_1.setText(question.getChoiceList().get(0));
        mAnswer_2.setText(question.getChoiceList().get(1));
        mAnswer_3.setText(question.getChoiceList().get(2));
        mAnswer_4.setText(question.getChoiceList().get(3));
    }

    private void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!")
                .setMessage("Your score is " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

//    @Override
//    public void finish(){
//        Intent mainActivityIntent = new Intent(GameActivity.this, MainActivity.class);
//        startActivity(mainActivityIntent);
//
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();
        if (responseIndex==mCurrentQuestion.getAnswerIndex()){
            Toast.makeText(GameActivity.this,"correct!",Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            Toast.makeText(GameActivity.this,"False!",Toast.LENGTH_SHORT).show();
        }
        // ... onClick(), after verifying the user's answer
        mEnableTouchEvents = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // If this is the last question, ends the game.
                // Else, display the next question.
                if (--mNumberOfQuestions == 0) {
                    // No questions remaining, end the game
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
                mEnableTouchEvents = true;
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long


// onClick()...

    }
    private QuestionBank generateQuestions() {
        Question question1 = new Question("What is the name of the current french president?",
                Arrays.asList("François Hollande", "Emmanuel Macron", "Jacques Chirac", "François Mitterand"),
                1);

        Question question2 = new Question("How many countries are there in the European Union?",
                Arrays.asList("15", "24", "28", "32"),
                2);

        Question question3 = new Question("Who is the creator of the Android operating system?",
                Arrays.asList("Andy Rubin", "Steve Wozniak", "Jake Wharton", "Paul Smith"),
                0);

        Question question4 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958", "1962", "1967", "1969"),
                3);

        Question question5 = new Question("What is the capital of Romania?",
                Arrays.asList("Bucarest", "Warsaw", "Budapest", "Berlin"),
                0);

        Question question6 = new Question("Who did the Mona Lisa paint?",
                Arrays.asList("Michelangelo", "Leonardo Da Vinci", "Raphael", "Carravagio"),
                1);

        Question question7 = new Question("In which city is the composer Frédéric Chopin buried?",
                Arrays.asList("Strasbourg", "Warsaw", "Paris", "Moscow"),
                2);

        Question question8 = new Question("What is the country top-level domain of Belgium?",
                Arrays.asList(".bg", ".bm", ".bl", ".be"),
                3);

        Question question9 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42", "101", "666", "742"),
                3);
        Question question10 = new Question("What is Abderrahim`s favourite food?",
                Arrays.asList("Couscous", "Pizza frutti di Mari", "Grilled fish", "salad"),
                5);

        return new QuestionBank(Arrays.asList(question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9,
                question10));
    }
}