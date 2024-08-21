package org.proven.decisions2.Games;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.proven.decisions2.R;
import org.proven.decisions2.Settings.AppCompat;

import java.util.ArrayList;
import java.util.Random;

public class QuestionQuizGame extends AppCompat {

    // The variables "value", "questionNumber", and "lifes" are declared as integers and initialized to default values of 0 and 3, respectively.
    private int value, questionNumber;
    private int lifes = 3;

    // The CardView variables "option1Button", "option2Button", "option3Button", and "option4Button" are declared to store the 4 answer options for each question.
    private CardView option1Button, option2Button, option3Button, option4Button;

    // The TextView variables "tv1", "tv2", "tv3", "tv4", "tvQuestion", and "tvCategory" are declared to display various elements of the quiz interface.
    private TextView tv1, tv2, tv3, tv4, tvQuestion, tvCategory;

    // The ImageView variables "life1", "life2", and "life3" are declared to display the remaining number of lives for the user.
    ImageView life1, life2, life3;

    // The remaining variables are declared to store various other data such as the total number of questions, the current question index, the selected answer, the count down timer, and the list of shown questions.
    int totalQuestions;
    int currentQuestionIndex = 0;
    int selectedAnswer = 0;
    CountDownTimer countDownTimer;
    ProgressBar pb;

    // Lastly, the variables "finish", "win", "lose", and "charge" are declared to track the game state, whether the user has finished the quiz, won, lost, or the game is still in progress.
    boolean finish = false, win = false, lose = false, charge=false;
    private ArrayList<Integer> shownQuestions = new ArrayList<Integer>();

    // The RelativeLayout and LinearLayout variables are used to arrange the elements of the quiz interface.
    RelativeLayout relativeLayout;
    LinearLayout cardLifes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_animation_layout);

        // Using a Handler to delay the execution of the code inside the Runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Setting the content view to the quiz layout
                setContentView(R.layout.quiz_layout);
                // Setting a boolean variable to true
                charge=true;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                    // Finding the quiz layout in the activity
                    relativeLayout = findViewById(R.id.quizLayout);

                    // Creating an animation drawable from the layout's background and setting fade durations
                    AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
                    animationDrawable.setEnterFadeDuration(2500);
                    animationDrawable.setExitFadeDuration(5000);
                    animationDrawable.start();
                } else {
                    // Setting the background color of the quiz layout
                    relativeLayout = findViewById(R.id.quizLayout);
                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                }

                // Initializing the elements in the quiz layout
                initElements();

                // Generating a random set of questions
                numRandomTotalQuestions();

                // Displaying the questions on the layout
                showQuestions();

                // Initializing a countdown timer
                initCrono();
            }
        }, 6500); // 6500 milliseconds = 6.5 seconds delay
    }


    // This is a method in Java that initializes elements for a quiz application
    // It uses findViewById to get references to buttons, text views, and other views in the layout
    private void initElements() {
        // Get references to the option buttons
        option1Button = findViewById(R.id.btAnswer1);
        option2Button = findViewById(R.id.btAnswer2);
        option3Button = findViewById(R.id.btAnswer3);
        option4Button = findViewById(R.id.btAnswer4);

        // Get references to the text views for the answer options
        tv1 = findViewById(R.id.tvAnswer1);
        tv2 = findViewById(R.id.tvAnswer2);
        tv3 = findViewById(R.id.tvAnswer3);
        tv4 = findViewById(R.id.tvAnswer4);

        // Get references to the text views for the question and category
        tvQuestion = findViewById(R.id.tvQuestion);
        tvCategory = findViewById(R.id.tvCategory);

        // Get references to the life views and card view for displaying remaining lives
        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        life3 = findViewById(R.id.life3);
        cardLifes = findViewById(R.id.cardLifes);

        // Get a reference to the progress bar
        pb = findViewById(R.id.pb);
    }


    // This method generates a random number between 5 and 10, which is used to set the total number of questions in the game.
    // If the generated number is greater than the total number of questions available, then the total number of questions is set to the maximum available.
    private void numRandomTotalQuestions(){
        Random random = new Random();
        totalQuestions = random.nextInt(6) + 5;
        if (totalQuestions>Question.question.length){
            totalQuestions=Question.question.length;
        }
    }


    // This method loads random questions for the game
    private void loadRandomQuestions() {
        // If the user has answered all questions correctly and still has lives left, the user wins.
        if (questionNumber == totalQuestions && lifes >= 1) {
            tvQuestion.setText(getString(R.string.you_win));
            finish = true;
            win = true;
        }
        // If the user has run out of lives, the user loses.
        else if (lifes == 0) {
            tvQuestion.setText(getString(R.string.you_lost));
            finish = true;
            lose = true;
        }
        // If the game is not finished yet, load a new question randomly.
        else if (!finish) {
             // Create a new random number generator.
            Random random = new Random();
            int index;
            // If all questions have been shown, choose a question from the list of previously shown questions.
            if (shownQuestions.size() >= Question.question.length) {
                index = shownQuestions.get(random.nextInt(shownQuestions.size()));
            }
            // Otherwise, choose a new question at random that hasn't been shown yet.
            else {
                do {
                    index = random.nextInt(Question.question.length);
                } while (shownQuestions.contains(index));
                shownQuestions.add(index);
            }
            // Update the current question index and the text for the TextViews on the screen.
            currentQuestionIndex = index;
            tvQuestion.setText(Question.question[currentQuestionIndex]);
            tv1.setText(Question.answers[currentQuestionIndex][0]);
            tv2.setText(Question.answers[currentQuestionIndex][1]);
            tv3.setText(Question.answers[currentQuestionIndex][2]);
            tv4.setText(Question.answers[currentQuestionIndex][3]);
            tvCategory.setText(Question.category[currentQuestionIndex]);
        }
    }

    // This method initializes a countdown timer with a progress bar that has an initial value of 30
    private void initCrono(){
        pb.setProgress(30);
        // A new CountDownTimer object is created with a duration of 31 seconds and an interval of 1 second
        countDownTimer = new CountDownTimer(31000, 1000){

            // This method is called every second during the countdown, it calculates the remaining seconds and sets the progress bar accordingly
            @Override
            public void onTick(long time) {
                long segPendiente=time/1000;
                pb.setProgress((int) segPendiente);
            }

            // This method is called when the timer finishes, it selects a random answer for the rival and sets the background color of the buttons to red before checking the answer after a delay
            @Override
            public void onFinish() {
                selectedAnswer = 5;
                option1Button.setCardBackgroundColor(Color.RED);
                option2Button.setCardBackgroundColor(Color.RED);
                option3Button.setCardBackgroundColor(Color.RED);
                option4Button.setCardBackgroundColor(Color.RED);
                checkAnswerDelayed();
            }
        }.start(); // The countdown timer starts running
    }


    // This method displays the quiz questions on the screen and sets up the buttons for each answer option
    private void showQuestions() {
        resetButtons(); // Reset all buttons to their default appearance

        // Update the current question number TextView
        TextView questionNumberTextView = findViewById(R.id.tvQuestionNumber);
        questionNumberTextView.setText(questionNumber+"/"+ totalQuestions);

        loadRandomQuestions(); // Load a random set of questions

        // Set up the OnClickListener for each answer option button
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(); // Stop the countdown timer
                selectedAnswer=0; // Set the selected answer to option 1
                changeCardColor(); // Change the color of the answer card to indicate selection
                checkAnswerDelayed(); // Check the answer after a short delay
            }
        });

        // Set up the OnClickListener for the second answer option button
        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(); // Stop the countdown timer
                selectedAnswer=1; // Set the selected answer to option 2
                changeCardColor(); // Change the color of the answer card to indicate selection
                checkAnswerDelayed(); // Check the answer after a short delay
            }
        });

        // Set up the OnClickListener for the third answer option button
        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(); // Stop the countdown timer
                selectedAnswer=2; // Set the selected answer to option 3
                changeCardColor(); // Change the color of the answer card to indicate selection
                checkAnswerDelayed(); // Check the answer after a short delay
            }
        });

        // Set up the OnClickListener for the fourth answer option button
        option4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(); // Stop the countdown timer
                selectedAnswer=3; // Set the selected answer to option 4
                changeCardColor(); // Change the color of the answer card to indicate selection
                checkAnswerDelayed(); // Check the answer after a short delay
            }
        });

    }

    // This method delays the execution of the checkAnswer method for 1000 milliseconds (1 second)
    public void checkAnswerDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAnswer();
            }
        }, 1000); // delay in milliseconds
    }

    // This method checks the user's answer and updates the game state accordingly
    private void checkAnswer() {

        // Show the next question if the answer is correct and the game has not finished yet
        if (selectedAnswer == Question.correctAnswer[currentQuestionIndex] && !finish) {
            questionNumber++;
            currentQuestionIndex++;
            showQuestions();
            countDownTimer.start();
        }
        // If the answer is incorrect, update the number of lives and show the next question
        else if(selectedAnswer != Question.correctAnswer[currentQuestionIndex]){
            updatelifes();
            System.out.println("Lifes: "+ lifes);
            currentQuestionIndex++;
            showQuestions();
            countDownTimer.start();
        }

        System.out.println(questionNumber+""+totalQuestions);

        // If the game has finished, disable all buttons and stop the countdown timer
        if (finish){
            option1Button.setEnabled(false);
            option2Button.setEnabled(false);
            option3Button.setEnabled(false);
            option4Button.setEnabled(false);
            countDownTimer.cancel();
        }

        // If the player wins the game, show the result screen after a 2-second delay
        if (win){
            value = 0;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = getIntent();
                    String textoDecision1 = intent.getStringExtra("decision1");
                    String textoDecision2 = intent.getStringExtra("decision2");
                    System.out.println("QuestionQuizGame "+textoDecision1);
                    System.out.println("QuestionQuizGame "+textoDecision2);
                    Intent intent2 = new Intent(QuestionQuizGame.this, ResultGame.class);
                    intent2.putExtra("result", value);
                    intent2.putExtra("decision1", textoDecision1);
                    intent2.putExtra("decision2", textoDecision2);
                    startActivity(intent2);
                    finish();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }
        // If the player loses the game, show the result screen after a 2-second delay and hide the life card view
        else if (lose) {
            cardLifes.setVisibility(View.GONE);
            value = 2;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(QuestionQuizGame.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finish();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }
    }

    // Decrements the value of the variable lifes by 1
    private void updatelifes(){
        lifes--;
        // If lifes equals 1, hide life3 and life2
        if (lifes==1){
            life3.setVisibility(View.GONE);
            life2.setVisibility(View.GONE);
        }
        // If lifes equals 2, hide life3
        else if (lifes==2){
            life3.setVisibility(View.GONE);
        }
        // If lifes equals 0, hide all life views and cardLifes
        else if(lifes==0){
            life3.setVisibility(View.GONE);
            life2.setVisibility(View.GONE);
            life1.setVisibility(View.GONE);
            cardLifes.setVisibility(View.GONE);
        }
    }

    // Changes the background color of the selected CardView based on whether the selected answer is correct or not
    private void changeCardColor(){
        CardView selectedCardView = null;
        // Find the CardView based on the value of selectedAnswer
        if (selectedAnswer == 0) {
            selectedCardView = findViewById(R.id.btAnswer1);
        } else if (selectedAnswer == 1) {
            selectedCardView = findViewById(R.id.btAnswer2);
        } else if (selectedAnswer == 2) {
            selectedCardView = findViewById(R.id.btAnswer3);
        } else if (selectedAnswer == 3){
            selectedCardView = findViewById(R.id.btAnswer4);
        }

        // Check if the selected answer is correct
        boolean isCorrect = (selectedAnswer == Question.correctAnswer[currentQuestionIndex]);

        // Change the CardView's background color based on whether the answer is correct or not
        int color = isCorrect ? Color.GREEN : Color.RED;
        selectedCardView.setCardBackgroundColor(color);
    }

    // Stops the game by canceling the countDownTimer and disabling all option buttons
    public void stop(){
        countDownTimer.cancel();
        option1Button.setEnabled(false);
        option2Button.setEnabled(false);
        option3Button.setEnabled(false);
        option4Button.setEnabled(false);
    }

    // Resets the option buttons to their default state by enabling them and setting their background color to white
    public void resetButtons(){
        option1Button.setEnabled(true);
        option2Button.setEnabled(true);
        option3Button.setEnabled(true);
        option4Button.setEnabled(true);

        option1Button.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        option2Button.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        option3Button.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        option4Button.setCardBackgroundColor(getResources().getColor(android.R.color.white));
    }

    // Overrides the default behavior of the back button press in the activity
    @Override
    public void onBackPressed() {
        if (charge){
            // Cancels the countDownTimer associated with the activity
            countDownTimer.cancel();
            // Calls the parent class method to handle the back button press
            super.onBackPressed();
        }
    }
}
