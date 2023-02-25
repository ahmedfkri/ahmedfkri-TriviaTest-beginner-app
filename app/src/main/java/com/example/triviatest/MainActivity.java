package com.example.triviatest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import data.AnswerListAsyncResponse;
import data.QuestionBank;
import model.Question;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private int currentQuestionIndex =0;
    private int currentScore=0;
    private int highestScore;
    private  Button btnFalse;
   private Button btnTrue;
   private ImageButton btnPrev;
   private ImageButton btnNext;
  private TextView txtQuestion;

   private TextView txtQuestionCount;
   private TextView txtScore;
   private TextView txtHighestScore;

   private List<Question> questionList;

   CardView cardView;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        btnFalse=findViewById(R.id.btn_false);
        btnTrue=findViewById(R.id.btn_true);
        btnNext=findViewById(R.id.btn_next);
        btnPrev=findViewById(R.id.btn_prev);
        cardView=findViewById(R.id.cardView);
        txtQuestion=findViewById(R.id.txt_question);
        txtQuestionCount=findViewById(R.id.txt_count);
        txtScore=findViewById(R.id.txt_score);
        txtHighestScore=findViewById(R.id.txt_highest_score);

        btnTrue.setOnClickListener(this);
        btnFalse.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

       sharedPreferences= getSharedPreferences("data",MODE_PRIVATE);
       currentQuestionIndex=sharedPreferences.getInt("state",0);

        txtScore.setText(String.valueOf(currentScore));
        txtHighestScore.setText(String.valueOf(getHighestScore()));


       questionList= new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
           @Override
           public void processFinished(ArrayList<Question> questionArrayList) {
               Log.d("MAIN", "processFinished: "+questionArrayList);

               txtQuestionCount.setText(currentQuestionIndex+1 +" / "+questionList.size());
               txtQuestion.setText(questionList.get(currentQuestionIndex).getAnswer());

           }
       });
    }

    private int getHighestScore() {

        if(sharedPreferences==null){
            return 0;
        }else{
            highestScore=sharedPreferences.getInt("highest",0);
            return highestScore;
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_prev:
                if(currentQuestionIndex >0) currentQuestionIndex =(currentQuestionIndex -1)% questionList.size();
                updateQuestionAndScore();
                break;

            case R.id.btn_true:
                checkAnswer(true);
                updateQuestionAndScore();
                break;

            case R.id.btn_false:
                checkAnswer(false);
                updateQuestionAndScore();
                break;

            case R.id.btn_next:
                currentQuestionIndex =(currentQuestionIndex +1)%questionList.size();
                updateQuestionAndScore();
                break;


        }

    }

    private void checkAnswer(boolean userAnswer) {

        btnFalse.setClickable(false);
        btnTrue.setClickable(false);
        boolean modelAnswer= questionList.get(currentQuestionIndex).isAnswerTure();

        int toastMessageId=0;

        if(userAnswer== modelAnswer){
            currentScore++;
            toastMessageId=R.string.correct_answer;
            fadeView();

        }else{
            currentScore--;
            toastMessageId=R.string.wrong_answer;
            shakeAnimation();
        }



        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();
        //btnNext.callOnClick();

    }

    private void fadeView() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(2);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                cardView.setCardBackgroundColor(Color.WHITE);
                btnNext.callOnClick();
                btnFalse.setClickable(true);
                btnTrue.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake_animation);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                btnNext.callOnClick();
                btnFalse.setClickable(true);
                btnTrue.setClickable(true);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    private void updateQuestionAndScore() {
        txtQuestion.setText(questionList.get(currentQuestionIndex).getAnswer());
        txtQuestionCount.setText(currentQuestionIndex+1 +" / "+questionList.size());
        txtScore.setText(String.valueOf(currentScore));
        txtHighestScore.setText(String.valueOf(sharedPreferences.getInt("highest",0)));
        checkHighestScore();


    }

    @Override
    protected void onPause() {

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        sharedPreferences.edit().putInt("highest", highestScore)
                .putInt("state",currentQuestionIndex).apply();
        super.onPause();
    }

    private void checkHighestScore() {

        if(currentScore>highestScore){
            highestScore=currentScore;
            txtHighestScore.setText(String.valueOf(highestScore));

        }
    }


}