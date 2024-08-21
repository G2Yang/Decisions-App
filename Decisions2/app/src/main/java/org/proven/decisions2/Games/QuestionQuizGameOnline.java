package org.proven.decisions2.Games;

import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.proven.decisions2.R;
import org.proven.decisions2.Settings.AppCompat;

import java.util.ArrayList;
import java.util.Random;

public class QuestionQuizGameOnline extends AppCompat {

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

    FirebaseDatabase database;
    DatabaseReference winerRef;
    DatabaseReference numQuestRef;

    String playerName = "";
    String roomName = "";
    String role = "";


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

                initFire();

                // Generating a random set of questions
                numRandomTotalQuestions();

            }
        }, 6500); // 6500 milliseconds = 6.5 seconds delay
    }

    /**
     * This code snippet is part of a method called initFire(). Here's a breakdown of what it does:
     *
     * It initializes a SharedPreferences object to retrieve stored preferences with the name "PREFS".
     * It retrieves the value of "playerName" from the shared preferences. If no value is found, an empty string is assigned to the playerName variable.
     * It gets the extras from the intent that started the current activity.
     * It checks if the extras bundle is not null.
     * If the extras bundle is not null, it retrieves the value of "roomName" from the extras.
     * It checks if the value of roomName is equal to the playerName obtained from shared preferences.
     * If the roomName is equal to the playerName, it sets the value of the role variable to "host".
     * If the roomName is not equal to the playerName, it sets the value of the role variable to "guest".
     * It retrieves a database reference using the roomName obtained.
     * It calls the addWinRoomListener() method, which adds a listener to the winerRef reference (previously obtained).
     * It sets the value of the winerRef reference to "pending" in the database.
     */
    private void initFire(){
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName = preferences.getString("playerName","");

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            roomName = extras.getString("roomName");

            if(roomName.equals(playerName)){
                role="host";
            }else{
                role="guest";
            }
        }

        winerRef = database.getReference("rooms/"+roomName+"/winner");
        addWinRoomListener();
        winerRef.setValue("pending");
    }

    /**
     * This code snippet defines a method called setWiner() which takes a winer parameter. Here's what the code does:
     *
     * It is a commented line of code that retrieves a database reference using the playerName variable and the path "rooms/playerName/winner".
     * It seems to be a potential alternative to the current reference winerRef.
     *
     * It sets the value of the winerRef reference (previously obtained) to "winner:" concatenated with the value of the winer parameter.
     */
    private void setWiner(String winer){
        winerRef.setValue("winner:"+winer);
    }

    /**
     * This code snippet defines a method called numRandomTotalQuestions(). Here's a breakdown of what it does:
     *
     * It creates a new Random object to generate random numbers.
     * It checks if the value of the role variable is "host".
     * If the role is "host", it generates a random number between 5 and 10 (inclusive) using nextInt(6) + 5 and assigns it to the totalQuestions variable.
     * It retrieves a database reference using the roomName variable and the path "rooms/roomName/questions" and assigns it to the numQuestRef variable.
     * It calls the getNumPreguntas() method, which is not shown in the code snippet.
     * It sets the value of the numQuestRef reference in the database to the value of totalQuestions.
     * If the role is "guest", it retrieves the database reference using the roomName variable and the path "rooms/roomName/questions" and assigns it to the numQuestRef variable.
     * It calls the getNumPreguntas() method, which is not shown in the code snippet.
     * It checks if the value of totalQuestions is greater than the length of the Question.question array.
     * If the condition is true, it sets the value of totalQuestions to the length of the Question.question array.
     */
    private void numRandomTotalQuestions(){
        Random random = new Random();

        if(role.equals("host")){
            totalQuestions = random.nextInt(6) + 5;
            numQuestRef = database.getReference("rooms/"+roomName+"/questions");
            getNumPreguntas();
            numQuestRef.setValue(totalQuestions);

        }else if(role.equals("guest")){
            numQuestRef = database.getReference("rooms/"+roomName+"/questions");
            getNumPreguntas();
        }

        if (totalQuestions>Question.question.length){
            totalQuestions=Question.question.length;
        }
    }

    /**
     * This code snippet defines a method called getNumPreguntas() which adds a ValueEventListener to the numQuestRef reference. Here's what the code does:
     *
     * It adds a ValueEventListener to the numQuestRef reference (previously obtained).
     *
     * The ValueEventListener defines two main methods:
     *
     * a. onDataChange(): This method is invoked when the data at the numQuestRef reference changes. Within this method, the following steps are performed:
     *
     * It checks if the value obtained from snapshot is not null and is of type Long.
     *
     * If the role is "guest", it retrieves the value from the snapshot as a Long and converts it to an integer using intValue(). The result is assigned to the totalQuestions variable.
     *
     * It calls the showQuestions() method, which is not shown in the code snippet. It seems to be responsible for displaying the questions on the layout.
     *
     * It calls the initCrono() method, which is not shown in the code snippet. It seems to be responsible for initializing a countdown timer.
     *
     * If the role is "host", it calls the showQuestions() method to display the questions on the layout.
     *
     * It calls the initCrono() method to initialize a countdown timer.
     *
     * b. onCancelled(): This method is invoked when a database error occurs or the operation is canceled. In this case, the method doesn't perform any specific action.
     */
    private void getNumPreguntas(){
        numQuestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Long.class) != null){
                    if(role.equals("guest")){
                        totalQuestions = snapshot.getValue(Long.class).intValue();
                        // Displaying the questions on the layout
                        showQuestions();

                        // Initializing a countdown timer
                        initCrono();
                    }else if(role.equals("host")){
                        // Displaying the questions on the layout
                        showQuestions();

                        // Initializing a countdown timer
                        initCrono();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * This code snippet defines a method called initElements(). Here's a breakdown of what it does:
     *
     * It gets references to the option buttons by calling the findViewById() method and passing the corresponding IDs: R.id.btAnswer1,
     * R.id.btAnswer2, R.id.btAnswer3, and R.id.btAnswer4. The references are assigned to option1Button, option2Button, option3Button, and option4Button variables, respectively.
     *
     * It gets references to the text views for the answer options by calling the findViewById() method and passing the corresponding IDs:
     * R.id.tvAnswer1, R.id.tvAnswer2, R.id.tvAnswer3, and R.id.tvAnswer4. The references are assigned to tv1, tv2, tv3, and tv4 variables, respectively.
     *
     * It gets references to the text views for the question and category by calling the findViewById() method and passing the corresponding IDs:
     * R.id.tvQuestion and R.id.tvCategory. The references are assigned to tvQuestion and tvCategory variables, respectively.
     *
     * It gets references to the life views and card view for displaying remaining lives by calling the findViewById() method and passing the corresponding IDs:
     * R.id.life1, R.id.life2, R.id.life3, and R.id.cardLifes. The references are assigned to life1, life2, life3, and cardLifes variables, respectively.
     *
     * It gets a reference to the progress bar by calling the findViewById() method and passing the ID R.id.pb. The reference is assigned to the pb variable.
     *
     * It gets a reference to the FirebaseDatabase instance by calling FirebaseDatabase.getInstance() and assigns it to the database variable.
     */
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

        database = FirebaseDatabase.getInstance();
    }


    /**
     * This code snippet defines a method called loadRandomQuestions(). Here's a breakdown of what it does:
     *
     * It checks if the questionNumber variable is equal to totalQuestions and the lifes variable is greater than or equal to 1.
     * If both conditions are true, it means the user has answered all questions correctly and still has lives left.
     * In this case, it sets the text of the tvQuestion TextView to "You win", sets the finish variable to true, and sets the win variable to true.
     *
     * If the lifes variable is equal to 0, it means the user has run out of lives.
     * In this case, it sets the text of the tvQuestion TextView to "You lost", sets the finish variable to true, and sets the lose variable to true.
     *
     * If neither of the above conditions is true, it means the game is not finished yet. It proceeds to load a new question randomly.
     *
     * It creates a new Random object to generate random numbers.
     *
     * It declares an index variable.
     *
     * If the number of questions shown (shownQuestions) is equal to or greater than the total number of questions available (Question.question.length),
     * it means all questions have been shown. In this case, it chooses a question from the list of previously shown questions (shownQuestions) using a
     * random index from 0 to the size of shownQuestions. The selected index is assigned to the index variable.
     *
     * If the number of questions shown is less than the total number of questions available, it means there are still unshown questions.
     * It enters a loop to choose a new question randomly that hasn't been shown yet. It generates a random index from 0 to the length of Question.question.length
     * using the nextInt() method of the Random object. It continues to generate a new index until an index is found that is not present in shownQuestions.
     * The selected index is assigned to the index variable, and the loop continues until a unique index is found.
     *
     * After obtaining the index for the new question, it adds the index to the shownQuestions list to keep track of shown questions.
     *
     * It updates the currentQuestionIndex with the selected index.
     *
     * It updates the text of the tvQuestion TextView with the question text from Question.question array at the currentQuestionIndex position.
     *
     * It updates the text of the option TextViews (tv1, tv2, tv3, tv4) with the answer options from Question.answers array at the currentQuestionIndex position.
     *
     * It updates the text of the tvCategory TextView with the category text from Question.category array at the currentQuestionIndex position.
     */
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

    /**
     * This code snippet defines a method called initCrono(). Here's a breakdown of what it does:
     *
     * It sets the progress of the pb progress bar to 30. The progress bar is presumably a visual representation of the countdown timer.
     *
     * It creates a new CountDownTimer object with a duration of 31 seconds (31,000 milliseconds) and an interval of 1 second (1,000 milliseconds).
     * The countdown timer will run for 31 seconds and update every second.
     *
     * The CountDownTimer object overrides two methods:
     *
     * a. onTick(long time): This method is called every second during the countdown. It receives the remaining time in milliseconds as the time parameter.
     * Inside this method, the remaining time is divided by 1000 to obtain the remaining seconds (segPendiente),
     * which is then cast to an integer and used to set the progress of the pb progress bar.
     *
     * b. onFinish(): This method is called when the timer finishes, i.e., when the countdown reaches 0. Inside this method, the following steps are performed:
     *
     * The selectedAnswer variable is set to 5. The purpose of this variable is not shown in the code snippet.
     * The background color of the option buttons (option1Button, option2Button, option3Button, option4Button) is set to red using the setCardBackgroundColor() method of the CardView class and the Color.RED constant.
     * The checkAnswerDelayed() method is called. The purpose of this method is not shown in the code snippet.
     * The CountDownTimer object is started by calling the start() method. This initiates the countdown.
     */
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


    /**
     * This code snippet defines a method called showQuestions(). Here's a breakdown of what it does:
     *
     * It calls the resetButtons() method, which resets all buttons to their default appearance. The purpose of this method is not shown in the code snippet.
     *
     * It retrieves a reference to the tvQuestionNumber TextView and sets its text to the current question number (questionNumber) concatenated with the total number of
     * questions (totalQuestions).
     *
     * It calls the loadRandomQuestions() method to load a random set of questions. The purpose of this method is not shown in the code snippet.
     *
     * It sets up an OnClickListener for each answer option button (option1Button, option2Button, option3Button, option4Button).
     * When a button is clicked, the corresponding onClick() method is triggered.
     *
     * Inside each onClick() method, the following steps are performed:
     *
     * The stop() method is called to stop the countdown timer. The purpose of this method is not shown in the code snippet.
     *
     * The selectedAnswer variable is set to a specific value based on the clicked button. For example, if option1Button is clicked, selectedAnswer is set to 0.
     * The purpose of this variable is not shown in the code snippet.
     *
     * The changeCardColor() method is called to change the color of the answer card to indicate the selection. The purpose of this method is not shown in the code snippet.
     *
     * The checkAnswerDelayed() method is called to check the answer after a short delay. The purpose of this method is not shown in the code snippet.
     */
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

    /**
     * This code snippet defines a method called checkAnswer(). Here's a breakdown of what it does:
     *
     * If the selected answer (selectedAnswer) matches the correct answer for the current question (Question.correctAnswer[currentQuestionIndex])
     * and the game has not finished yet (!finish), the following steps are performed:
     *
     * Increment the question number (questionNumber) by 1.
     * Increment the current question index (currentQuestionIndex) by 1.
     * Call the showQuestions() method to display the next question.
     * Restart the countdown timer (countDownTimer.start()).
     * If the selected answer does not match the correct answer (selectedAnswer != Question.correctAnswer[currentQuestionIndex]), the following steps are performed:
     *
     * Call the updatelifes() method to update the number of lives.
     * Increment the current question index.
     * Call the showQuestions() method to display the next question.
     * Restart the countdown timer.
     * If the game has finished (finish is true), the following steps are performed:
     *
     * Disable all buttons (option1Button, option2Button, option3Button, option4Button).
     * Stop the countdown timer (countDownTimer.cancel()).
     * If the player wins the game (win is true), the following steps are performed:
     *
     * Hide the life card view (cardLifes.setVisibility(View.GONE)).
     * Set the winner (setWiner()) based on the player's role (role). The purpose of this method is not shown in the code snippet.
     * If the player loses the game (lose is true), the following steps are performed:
     *
     * Hide the life card view.
     * Set the winner based on the player's role.
     */
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
            currentQuestionIndex++;
            showQuestions();
            countDownTimer.start();
        }

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
            cardLifes.setVisibility(View.GONE);

            if(role.equals("guest")){
                setWiner("guest");
            }else if(role.equals("host")){
                setWiner("host");
            }
        }
        // If the player loses the game, show the result screen after a 2-second delay and hide the life card view
        else if (lose) {
            cardLifes.setVisibility(View.GONE);

            if(role.equals("guest")){
                setWiner("host");
            }else if(role.equals("host")){
                setWiner("guest");
            }

        }
    }

    /**
     *This code documents a method called addWinRoomListener() that adds a ValueEventListener to a
     * winerRef reference. The winerRef reference appears to be related to a database or data storage service.
     *
     * The ValueEventListener defines two main methods:
     *
     * 1- onDataChange(): This method is invoked when the data at the winerRef
     * reference changes. Within this method, the following steps are performed:
     *
     * a. It checks if the value obtained from snapshot is not null and is of type String.
     *
     * b. If the value contains the string "winner:guest", it checks if the value of role is "guest". If it is,
     * it creates a new intent to start the ResultGame activity with an extra "result" set to 0. Then it starts
     * the activity and finishes the current activity (QuestionQuizGameOnline).
     *
     * c. If the value contains the string "winner:host", it checks if the value of role is "host".
     * If it is, it performs the same process as in the previous step, creating an intent with an extra "result" set to 0.
     *
     * 2- onCancelled(): This method is invoked when a database error occurs or the operation is canceled. In this case, the method doesn't perform any specific action.
     */
    public void addWinRoomListener(){
        winerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class) != null){
                    if(snapshot.getValue(String.class).replace("winner:","").equals("guest")){
                        if(role.equals("guest")){
                            Intent intent = getIntent();
                            String textoDecision1 = intent.getStringExtra("decision1");
                            System.out.println("QuestionQuizGameOnline "+textoDecision1);
                            Intent intent2 = new Intent(QuestionQuizGameOnline.this, ResultGame.class);
                            intent2.putExtra("decision1", textoDecision1);
                            intent.putExtra("result", 0);
                            startActivity(intent2);
                            finish();
                            deleteRoom(roomName);
                        }else{
                            Intent intent = new Intent(QuestionQuizGameOnline.this, ResultGame.class);
                            intent.putExtra("result", 1);
                            startActivity(intent);
                            finish();
                            deleteRoom(roomName);
                        }
                    } else if(snapshot.getValue(String.class).replace("winner:","").equals("host")){
                        if(role.equals("host")){
                            Intent intent = getIntent();
                            String textoDecision1 = intent.getStringExtra("decision1");
                            System.out.println("QuestionQuizGameOnline "+textoDecision1);
                            Intent intent2 = new Intent(QuestionQuizGameOnline.this, ResultGame.class);
                            intent2.putExtra("decision1", textoDecision1);
                            intent.putExtra("result", 0);
                            startActivity(intent2);
                            finish();
                            deleteRoom(roomName);
                        }else{
                            Intent intent = new Intent(QuestionQuizGameOnline.this, ResultGame.class);
                            intent.putExtra("result", 1);
                            startActivity(intent);
                            finish();
                            deleteRoom(roomName);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * The updatelifes() method is responsible for updating the number of remaining lives in the game. Here's a breakdown of what it does:
     *
     * Decrement the lifes variable by 1.
     *
     * Depending on the value of lifes, perform the following actions:
     *
     * If lifes equals 1, hide the life3 and life2 views by setting their visibility to View.GONE.
     * If lifes equals 2, hide the life3 view by setting its visibility to View.GONE.
     * If lifes equals 0, hide all life views (life3, life2, life1) and the cardLifes view by setting their visibility to View.GONE.
     */
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

    /**
     * The changeCardColor() method is responsible for changing the background color of the selected answer option CardView. Here's an explanation of what it does:
     *
     * Declare a variable selectedCardView to store the reference to the CardView that corresponds to the selected answer.
     *
     * Based on the value of selectedAnswer, assign the appropriate CardView reference to the selectedCardView variable.
     * The CardView is identified by its ID, which is determined by the value of selectedAnswer.
     *
     * Determine if the selected answer is correct by comparing selectedAnswer with the correct answer for the current question (Question.correctAnswer[currentQuestionIndex]).
     *
     * Set the background color of the selectedCardView based on whether the answer is correct or not. If the answer is correct, the background color is set to Color.GREEN;
     * otherwise, it is set to Color.RED.
     */
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

    /**
     * The stop() method is responsible for stopping the countdown timer and disabling the answer option buttons. Here's a breakdown of what it does:
     *
     * Cancel the countdown timer by calling the cancel() method on the countDownTimer object.
     *
     * Disable the answer option buttons by setting their enabled state to false. This is done by calling the setEnabled(false) method on each of the option1Button,
     * option2Button, option3Button, and option4Button.
     */
    public void stop(){
        countDownTimer.cancel();
        option1Button.setEnabled(false);
        option2Button.setEnabled(false);
        option3Button.setEnabled(false);
        option4Button.setEnabled(false);
    }

    /**
     * The resetButtons() method is responsible for resetting the answer option buttons to their default appearance and enabling them for interaction.
     * Here's a breakdown of what it does:
     *
     * Enable the answer option buttons by setting their enabled state to true.
     * This is done by calling the setEnabled(true) method on each of the option1Button, option2Button, option3Button, and option4Button.
     *
     * Reset the background color of the answer option buttons to the default color.
     * This is done by calling the setCardBackgroundColor() method on each button and passing the color obtained from getResources().getColor(android.R.color.white).
     */
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

    /**
     * The updated onBackPressed() method overrides the default behavior of the back button press event. Here's what it does:
     *
     * It checks if the charge variable is true. This variable indicates that a countdown timer is active and running.
     *
     * If charge is true, it calls the super.onBackPressed() method, which is the default implementation of the back button press behavior in the parent class. This allows the activity to navigate back using the default behavior when the back button is pressed.
     *
     * After calling super.onBackPressed(), it assigns a value of 5 to the value variable.
     *
     * It cancels the countDownTimer associated with the activity by calling the cancel() method on the countDownTimer object.
     *
     * It creates an Intent to start the ResultGame activity.
     *
     * It adds the value as an extra to the Intent using the key "result".
     *
     * It starts the ResultGame activity by calling startActivity(intent).
     *
     * It finishes the current activity by calling finish().
     *
     * It calls the deleteRoom() method to delete the room with the specified roomName.
     */
    @Override
    public void onBackPressed() {
        if (charge){
            super.onBackPressed();
            // Cancels the countdown timer associated with the activity
            countDownTimer.cancel();
            finish();
            deleteRoom(roomName);
        }
    }


    /**
     * The updated code overrides the onDestroy() method of the activity. Here's what it does:
     *
     * It calls the super.onDestroy() method to ensure that the parent class's onDestroy() method is executed.
     *
     * It checks if the countDownTimer object is not null.
     *
     * If the countDownTimer is not null, it cancels the timer by calling the cancel() method on the countDownTimer object.
     *
     * It calls the deleteRoom() method with the specified roomName to delete the associated room from the database.
     *
     * The deleteRoom() method checks if the database object is not null.
     *
     * If the database is not null, it gets a reference to the "rooms" node in the database using the getReference() method.
     *
     * It removes the value associated with the specified roomName using the removeValue() method.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        deleteRoom(roomName);
    }
    private void deleteRoom(String roomName) {
        if (database != null) {
            database.getReference("rooms/" + roomName).removeValue();
        }
    }
}
