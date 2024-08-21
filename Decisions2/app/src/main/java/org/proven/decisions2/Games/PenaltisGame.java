package org.proven.decisions2.Games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import org.proven.decisions2.R;

import java.util.Random;

public class PenaltisGame extends Activity {

    // Declaration of CardView buttons to touch to play and CountDownTimer to the time
    CardView btDown, btRight, btLeft, btRightUp, btLeftUp;
    CountDownTimer countDownTimer;

    // Declaration of integer variables for keeping score and round count
    int electionPlayer, electionMachine, golPlayer = 0, golMachine = 0, round = 0,round2 = 0, value = 0;

    // Declaration of TextViews for displaying score, timer and game result
    TextView tvTimer, tvGolsPlayer, tvGolsMachine, tvResult;

    // Boolean variables to keep track of player and goalie turn, game finish and win/loss state
    boolean turnPlayer, turnGoalie, finish = false, win = false, lose = false, charge=false;

    // Declaration of ImageViews for penalties points, robot, ball and other game elements
    ImageView p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, robot, ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.penaltis_animation_layout);

        // Delayed post to run code after 6.5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Set content view to the main layout
                setContentView(R.layout.penaltis_layout);
                charge=true;

                // Initialize UI elements
                instantiateElements();

                // Set initial scores
                tvGolsMachine.setText(""+golMachine);
                tvGolsPlayer.setText(""+golPlayer);

                // Initialize turn and timer
                initTurn();
                initCrono();

                // Set up button listeners
                btDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        electionPlayer=3;
                        tiradaMaquina();
                        checkTirada();
                        checkWinDelayed();
                    }
                });

                btRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        electionPlayer=5;
                        tiradaMaquina();
                        checkTirada();
                        checkWinDelayed();
                    }
                });
                btLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        electionPlayer=2;
                        tiradaMaquina();
                        checkTirada();
                        checkWinDelayed();
                    }
                });

                btRightUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        electionPlayer=4;
                        tiradaMaquina();
                        checkTirada();
                        checkWinDelayed();
                    }
                });

                btLeftUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        electionPlayer=1;
                        tiradaMaquina();
                        checkTirada();
                        checkWinDelayed();
                    }
                });
            }
        }, 6500); // 6500 milliseconds = 6.5 seconds delay
    }

    // This method initializes the turn of the game
    private void initTurn(){
        // Creating a new Random object
        Random random = new Random();
        // Generating a random number between 0 and 1
        int randomNumber = random.nextInt(2);

        // If the random number is 0, it's the player's turn
        if (randomNumber == 0) {
            turnPlayer = true;
            // Setting the text of a TextView to a string resource with the message "You are player"
            tvResult.setText(R.string.you_are_player);
        }
        // If the random number is 1, it's the goalkeeper's turn
        else {
            turnGoalie = true;
            // Setting the text of a TextView to a string resource with the message "You are goalkeeper"
            tvResult.setText(R.string.you_are_goalkeeper);
        }
    }

    // This method is responsible for initializing all the elements of the UI.
    private void instantiateElements() {
        // Finding the views with their respective IDs and assigning them to their corresponding variables.
        btDown = findViewById(R.id.down);
        btRight = findViewById(R.id.right);
        btLeft = findViewById(R.id.left);
        btRightUp = findViewById(R.id.right_up);
        btLeftUp = findViewById(R.id.left_up);
        tvTimer = findViewById(R.id.timer);
        tvGolsPlayer = findViewById(R.id.gols1);
        tvGolsMachine = findViewById(R.id.gols2);
        tvResult = findViewById(R.id.result);

        // Finding the views of the penalties points, the robot, and the ball and assigning them to their corresponding variables.
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.p5);
        p6 = findViewById(R.id.p6);
        p7 = findViewById(R.id.p7);
        p8 = findViewById(R.id.p8);
        p9 = findViewById(R.id.p9);
        p10 = findViewById(R.id.p10);
        robot = findViewById(R.id.robot);
        ball = findViewById(R.id.ball);
    }

    // Method to initialize the countdown timer
    private void initCrono(){
        // Create a new CountDownTimer object with a duration of 15 seconds and a tick interval of 1 second
        countDownTimer = new CountDownTimer(15000, 1000){

            @Override
            // Method to be called on each tick of the countdown timer
            public void onTick(long time) {
                // Calculate the remaining time in seconds
                long segPendiente=time/1000;
                // Set the text of the tvTimer TextView to show the remaining time in seconds
                tvTimer.setText(getString(R.string.time)+": "+segPendiente);
            }

            @Override
            // Method to be called when the countdown timer finishes
            public void onFinish() {
                // Create a new Random object
                Random rand = new Random();
                // Randomly select an element for the rival
                electionPlayer = rand.nextInt(5) + 1;
                // Call the tiradaMaquina() method
                tiradaMaquina();
                // Call the checkTirada() method
                checkTirada();
                // Call the checkWinDelayed() method
                checkWinDelayed();
            }
        }.start();

    }

    // Method to randomly select an element for the machine player
    public void tiradaMaquina(){
        // Create a new Random object
        Random rand = new Random();
        // Randomly select an element for the machine player
        electionMachine = rand.nextInt(5) + 1;
    }

    // Method to check the outcome of the player's move
    public void checkTirada(){
        // Disable all buttons
        btDown.setEnabled(false);
        btRight.setEnabled(false);
        btLeft.setEnabled(false);
        btRightUp.setEnabled(false);
        btLeftUp.setEnabled(false);
        // Cancel the countdown timer
        countDownTimer.cancel();
        // Hide the ball ImageView
        ball.setVisibility(View.INVISIBLE);
        // Check if it's the player's turn
        if(turnPlayer){
            // Increase the round counter
            round++;
            // Set the robot ImageView to show the player's chosen element
            setRobot();
            // Set the ball ImageView to the player's position
            setBall();
            // Check if the player's element is different from the machine player's element
            if(electionPlayer != electionMachine){
                // Increase the player's score
                golPlayer++;
                // Set the text of the tvGolsPlayer TextView to show the player's score
                tvGolsPlayer.setText(""+golPlayer);
                // Set the gol ImageView to show the player's goal
                setGol(round);
            }else{
                // Set the error ImageView to show the player's error
                setError(round);
            }
        }else if(turnGoalie){
            // Increase the round counter
            round2++;
            // Set the robot ImageView to show the player's chosen element
            setRobot();
            // Set the ball ImageView to the player's position
            setBall();
            // Check if the player's element is different from the machine player's element
            if (electionPlayer != electionMachine){
                // Increase the machine player's score
                golMachine++;
                // Set the text of the tvGolsMachine TextView to show the machine player's score
                tvGolsMachine.setText(Integer.toString(golMachine));
                // Set the gol ImageView to show the machine player's goal
                setGol(round2);
            }else{
                // Set the error ImageView to show the machine player's error
                setError(round2);
            }
        }
    }

    // This method uses a Handler to delay the execution of checkwin() by 1000 milliseconds
    public void checkWinDelayed(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkwin();
            }
        }, 1000); // delay in milliseconds
    }

    // This method sets the background resource of the corresponding ImageView based on the value of the round and turnPlayer variables
    public void setGol(int round){
        if(round==1 && turnPlayer){
            p1.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==2 && turnPlayer){
            p2.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==3 && turnPlayer) {
            p3.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==4 && turnPlayer){
            p4.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==5 && turnPlayer) {
            p5.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }
        if(round==1 && turnGoalie){
            p6.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==2 && turnGoalie){
            p7.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==3 && turnGoalie) {
            p8.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==4 && turnGoalie){
            p9.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==5 && turnGoalie) {
            p10.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }
    }

    // This method sets an error in the UI for a given round and player turn.
    public void setError(int round){
        // If it's the player's turn, set the appropriate background resource based on the round number.
        if(round==1 && turnPlayer){
            p1.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==2 && turnPlayer){
            p2.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==3 && turnPlayer) {
            p3.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==4 && turnPlayer){
            p4.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==5 && turnPlayer) {
            p5.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }
        // If it's the goalie's turn, set the appropriate background resource based on the round number.
        if(round==1 && turnGoalie){
            p6.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==2 && turnGoalie){
            p7.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==3 && turnGoalie) {
            p8.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==4 && turnGoalie){
            p9.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==5 && turnGoalie) {
            p10.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }
    }

    // This method sets the image resource of the robot based on the selected machine or player and turn
    public void setRobot() {
        if (electionMachine == 1 && turnPlayer) {
            // Set the image resource to the robot facing left and up
            robot.setImageResource(R.drawable.robot_left_up);
        } else if (electionMachine == 2 && turnPlayer) {
            // Set the image resource to the robot facing left
            robot.setImageResource(R.drawable.robot_left);
        } else if (electionMachine == 3 && turnPlayer) {
            // Set the image resource to the robot facing up
            robot.setImageResource(R.drawable.robot_up);
        } else if (electionMachine == 4 && turnPlayer) {
            // Set the image resource to the robot facing right and up
            robot.setImageResource(R.drawable.robot_right_up);
        } else if (electionMachine == 5 && turnPlayer) {
            // Set the image resource to the robot facing right
            robot.setImageResource(R.drawable.robot_right);
        }
        if (electionPlayer == 1 && turnGoalie) {
            // Set the image resource to the robot facing left and up
            robot.setImageResource(R.drawable.robot_left_up);
        } else if (electionPlayer == 2 && turnGoalie) {
            // Set the image resource to the robot facing left
            robot.setImageResource(R.drawable.robot_left);
        } else if (electionPlayer == 3 && turnGoalie) {
            // Set the image resource to the robot facing up
            robot.setImageResource(R.drawable.robot_up);
        } else if (electionPlayer == 4 && turnGoalie) {
            // Set the image resource to the robot facing right and up
            robot.setImageResource(R.drawable.robot_right_up);
        } else if (electionPlayer == 5 && turnGoalie) {
            // Set the image resource to the robot facing right
            robot.setImageResource(R.drawable.robot_right);
        }
    }

    // This method sets the ball on the selected button based on the chosen player or machine and the current turn.
    public void setBall(){
        // If the chosen machine is 1 and it's the goalie's turn, set the ball on the top-left button.
        if (electionMachine==1 && turnGoalie){
            btLeftUp.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen machine is 2 and it's the goalie's turn, set the ball on the left button.
        else if (electionMachine==2 && turnGoalie){
            btLeft.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen machine is 3 and it's the goalie's turn, set the ball on the bottom button.
        else if (electionMachine==3 && turnGoalie){
            btDown.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen machine is 4 and it's the goalie's turn, set the ball on the top-right button.
        else if (electionMachine==4 && turnGoalie){
            btRightUp.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen machine is 5 and it's the goalie's turn, set the ball on the right button.
        else if (electionMachine==5 && turnGoalie){
            btRight.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 1 and it's the player's turn, set the ball on the top-left button.
        if (electionPlayer==1 && turnPlayer){
            btLeftUp.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 2 and it's the player's turn, set the ball on the left button.
        else if (electionPlayer==2 && turnPlayer){
            btLeft.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 3 and it's the player's turn, set the ball on the bottom button.
        else if (electionPlayer==3 && turnPlayer){
            btDown.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 4 and it's the player's turn, set the ball on the top-right button.
        else if (electionPlayer==4 && turnPlayer){
            btRightUp.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 5 and it's the player's turn, set the ball on the right button.
        else if (electionPlayer==5 && turnPlayer){
            btRight.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
    }


    // This method checks if the player wins or loses the penalty shootout game
    private void checkwin() {
        // Reset the game state
        reset();

        // Check if the player wins or loses by 4 goals or more difference
        if (golPlayer - golMachine >= 4) {
            finish = true;
            tvResult.setText(R.string.you_win);
            win = true;
        } else if(golMachine - golPlayer >= 4){
            finish = true;
            tvResult.setText(R.string.you_lost);
            lose = true;
        }
        // Check if the player wins or loses by 3 goals or more difference
        if (golPlayer - golMachine >= 3) {
            finish = true;
            tvResult.setText(R.string.you_win);
            win = true;
        } else if(golMachine - golPlayer >= 3){
            finish = true;
            tvResult.setText(R.string.you_lost);
            lose = true;
        }
        // Check if the player wins or loses at the end of the 5 rounds
        if (round==5 || round2==5){
            if (golPlayer == 5 && golMachine < 4){
                tvResult.setText(R.string.you_win);
                finish = true;
                win = true;
            }else if (golMachine == 5 && golPlayer < 4){
                tvResult.setText(R.string.you_lost);
                finish = true;
                lose = true;
            }
            if (golPlayer - golMachine >= 2) {
                finish = true;
                tvResult.setText(R.string.you_win);
                win = true;
            } else if(golMachine - golPlayer >= 2){
                finish = true;
                tvResult.setText(R.string.you_lost);
                lose = true;
            }
            if (golPlayer - golMachine >= 3) {
                finish = true;
                tvResult.setText(R.string.you_win);
                win = true;
            } else if(golMachine - golPlayer >= 3){
                finish = true;
                tvResult.setText(R.string.you_lost);
                lose = true;
            }
            if (round==5 && round2==5){
                if (golPlayer - golMachine >= 1) {
                    finish = true;
                    tvResult.setText(R.string.you_win);
                    win = true;
                } else if(golMachine - golPlayer >= 1){
                    finish = true;
                    tvResult.setText(R.string.you_lost);
                    lose = true;
                }
            }
        }

        // Check if the player wins or loses after 5 rounds
        if (round > 5 || round2 > 5) {
            if (golPlayer - golMachine >= 1) {
                finish = true;
                tvResult.setText(R.string.you_win);
                win = true;
            } else if(golMachine - golPlayer >= 1){
                finish = true;
                tvResult.setText(R.string.you_lost);
                lose = true;
            }
        }

        // Disable all buttons and stop the countdown timer if the game is finished
        if (finish){
            btDown.setEnabled(false);
            btRight.setEnabled(false);
            btLeft.setEnabled(false);
            btRightUp.setEnabled(false);
            btLeftUp.setEnabled(false);
            countDownTimer.cancel();
            tvTimer.setVisibility(View.GONE);
        }

        // Launch the result activity after 2 seconds delay if the player wins or loses
        if (win){
            value = 0;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = getIntent();
                    String textoDecision1 = intent.getStringExtra("decision1");
                    String textoDecision2 = intent.getStringExtra("decision2");
                    System.out.println("PenaltisGame "+textoDecision1);
                    System.out.println("PenaltisGame "+textoDecision2);
                    Intent intent2 = new Intent(PenaltisGame.this, ResultGame.class);
                    intent2.putExtra("result", value);
                    intent2.putExtra("decision1", textoDecision1);
                    intent2.putExtra("decision2", textoDecision2);
                    startActivity(intent2);
                    finish();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }else if (lose) {
            value = 2;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = getIntent();
                    String textoDecision1 = intent.getStringExtra("decision1");
                    String textoDecision2 = intent.getStringExtra("decision2");
                    System.out.println("PenaltisGame "+textoDecision1);
                    System.out.println("PenaltisGame "+textoDecision2);
                    Intent intent2 = new Intent(PenaltisGame.this, ResultGame.class);
                    intent2.putExtra("result", value);
                    intent2.putExtra("decision1", textoDecision1);
                    intent2.putExtra("decision2", textoDecision2);
                    startActivity(intent2);
                    finish();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }
    }

    // This method is used to reset the game and start a new round.
    public void reset(){
        // Start the countdown timer for the new round.
        countDownTimer.start();

        // Enable all the buttons for the player to control the robot.
        btDown.setEnabled(true);
        btRight.setEnabled(true);
        btLeft.setEnabled(true);
        btRightUp.setEnabled(true);
        btLeftUp.setEnabled(true);

        // Set the robot image to the default "down" direction.
        robot.setImageResource(R.drawable.robot_down);

        // Set the foreground images of the buttons to their default values.
        btLeftUp.setForeground(ContextCompat.getDrawable(this, R.drawable.left_up));
        btLeft.setForeground(ContextCompat.getDrawable(this, R.drawable.left));
        btDown.setForeground(ContextCompat.getDrawable(this, R.drawable.stay));
        btRightUp.setForeground(ContextCompat.getDrawable(this, R.drawable.right_up));
        btRight.setForeground(ContextCompat.getDrawable(this, R.drawable.right));

        // Make the ball visible again.
        ball.setVisibility(View.VISIBLE);

        // Switch the turn between the player and the goalie.
        if (turnPlayer){
            turnPlayer = false;
            turnGoalie = true;
            tvResult.setText(R.string.you_are_goalkeeper);
        }else if (turnGoalie){
            turnPlayer = true;
            turnGoalie = false;
            tvResult.setText(R.string.you_are_player);
        }
    }

    // This method overrides the default behavior of the back button press in the activity
    @Override
    public void onBackPressed() {
        if (charge){
            // Cancels the countdown timer associated with the activity
            countDownTimer.cancel();
            // Calls the parent class method to handle the back button press
            super.onBackPressed();
        }
    }
}
