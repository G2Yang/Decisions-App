package org.proven.decisions2.Settings;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeScroll;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

public class GuideActivity extends Activity {

    TextView tvDetailsElements, tvDetailsPenaltis, tvDetailsQuiz;
    LinearLayout layoutExpand, layoutImgElements, layoutExpandPenaltis, layoutImgPenaltis, layoutExpandQuiz, layoutImgQuiz;
    Button btHome, btSettings, btFriends;
    ImageView waterImg, fireImg, iceImg, robot, ball;

    /**
     * The onCreate() method in the GuideActivity class initializes the activity and sets up the click listeners for the buttons. Here's a breakdown of the code:
     *
     * The method starts by calling the superclass's onCreate() method and passing the savedInstanceState bundle.
     * It sets the content view of the activity to the layout file "guide_layout" using the setContentView() method.
     * The initializeElements() method is called to initialize the elements (not shown in the provided code).
     * The btHome button's click listener is set to start a new activity SocialInterface and remove the current activity from the task stack using startActivity() and finishAndRemoveTask() methods, respectively.
     * Similarly, the btSettings button's click listener starts the SettingsActivity and removes the current activity from the task stack.
     * The btFriends button's click listener starts the FriendsActivity and removes the current activity from the task stack.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guide_layout);
        //Initialize the elements
        initializeElements();

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, SocialInterface.class));
                finishAndRemoveTask();
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, SettingsActivity.class));
                finishAndRemoveTask();
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, FriendsActivity.class));
                finishAndRemoveTask();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);

        tvDetailsElements = findViewById(R.id.tvDetails);
        waterImg = findViewById(R.id.waterImg);
        fireImg = findViewById(R.id.fireImg);
        iceImg = findViewById(R.id.iceImg);
        layoutExpand = findViewById(R.id.layoutExpand);
        layoutImgElements = findViewById(R.id.layoutImg);
        layoutExpand.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        layoutImgElements.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);

        tvDetailsPenaltis = findViewById(R.id.tvDetailsPenaltis);
        robot = findViewById(R.id.robot);
        ball = findViewById(R.id.ball);
        layoutExpandPenaltis = findViewById(R.id.layoutExpandPenaltis);
        layoutImgPenaltis = findViewById(R.id.layoutImgPenaltis);
        layoutExpandPenaltis.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        layoutImgPenaltis.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);

        tvDetailsQuiz = findViewById(R.id.tvDetailsQuiz);
        layoutExpandQuiz = findViewById(R.id.layoutExpandQuiz);
        layoutImgQuiz = findViewById(R.id.layoutImgQuiz);
        layoutExpandQuiz.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        layoutImgQuiz.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
    }

    /**
     * The expandElements() method is used to toggle the visibility of certain elements in response to a user action. Here's a breakdown of the code:
     *
     * The method is defined with a parameter view representing the view that triggered the action (e.g., a button click).
     *
     * The visibility of tvDetailsElements (presumably a TextView) is checked using tvDetailsElements.getVisibility().
     *
     * If tvDetailsElements is currently set to View.GONE, it means it is hidden, so the value of v is set to View.VISIBLE to make it visible. Otherwise,
     * if it is visible (View.VISIBLE), the value of v is set to View.GONE to hide it.
     *
     * A TransitionManager is used to animate the transition between the visibility states.
     * In this case, a ChangeScroll transition is applied to smoothly scroll the content if necessary.
     *
     * The visibility of tvDetailsElements is set to the value of v using tvDetailsElements.setVisibility(v), which makes it either visible or hidden based on the previous step.
     *
     * Similarly, the visibility of layoutImgElements (presumably a layout containing images) is checked and toggled using the same logic as above.
     *
     * The TransitionManager is again used to animate the visibility change of layoutImgElements with a ChangeScroll transition.
     *
     * The visibility of layoutImgElements is set to the value of l, making it either visible or hidden.
     * @param view
     */
    public void expandElements(View view) {
        int v = (tvDetailsElements.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
        TransitionManager.beginDelayedTransition(layoutExpand, new ChangeScroll() {
        });
        tvDetailsElements.setVisibility(v);

        int l = (layoutImgElements.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layoutImgElements, new ChangeScroll());
        layoutImgElements.setVisibility(l);

    }

    /**
     * The expandPenaltis() method is similar to the expandElements() method you provided earlier. It allows you to toggle the visibility of certain elements related to penalties. Here's a breakdown of the code:
     *
     * The method is defined with a parameter view representing the view that triggered the action.
     *
     * The visibility of tvDetailsPenaltis (presumably a TextView) is checked using tvDetailsPenaltis.getVisibility().
     *
     * If tvDetailsPenaltis is currently set to View.GONE, it means it is hidden, so the value of v is set to View.VISIBLE to make it visible.
     * Otherwise, if it is visible (View.VISIBLE), the value of v is set to View.GONE to hide it.
     *
     * A TransitionManager is used to animate the transition between the visibility states.
     * In this case, a ChangeScroll transition is applied to smoothly scroll the content if necessary.
     *
     * The visibility of tvDetailsPenaltis is set to the value of v using tvDetailsPenaltis.setVisibility(v), which makes it either visible or hidden based on the previous step.
     *
     * Similarly, the visibility of layoutImgPenaltis (presumably a layout containing images) is checked and toggled using the same logic as above.
     *
     * The TransitionManager is again used to animate the visibility change of layoutImgPenaltis with a ChangeScroll transition.
     *
     * The visibility of layoutImgPenaltis is set to the value of l, making it either visible or hidden.
     * @param view
     */
    public void expandPenaltis(View view) {
        int v = (tvDetailsPenaltis.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
        TransitionManager.beginDelayedTransition(layoutExpandPenaltis, new ChangeScroll() {
        });
        tvDetailsPenaltis.setVisibility(v);

        int l = (layoutImgPenaltis.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layoutImgPenaltis, new ChangeScroll());
        layoutImgPenaltis.setVisibility(l);

    }

    /**
     * The expandQuiz() method is similar to the previous methods you provided (expandElements() and expandPenaltis()). It allows you to toggle the visibility of certain elements related to a quiz. Here's a breakdown of the code:
     *
     * The method is defined with a parameter view representing the view that triggered the action.
     *
     * The visibility of tvDetailsQuiz (presumably a TextView) is checked using tvDetailsQuiz.getVisibility().
     *
     * If tvDetailsQuiz is currently set to View.GONE, it means it is hidden, so the value of v is set to View.VISIBLE to make it visible.
     * Otherwise, if it is visible (View.VISIBLE), the value of v is set to View.GONE to hide it.
     *
     * A TransitionManager is used to animate the transition between the visibility states. In this case,
     * a ChangeScroll transition is applied to smoothly scroll the content if necessary.
     *
     * The visibility of tvDetailsQuiz is set to the value of v using tvDetailsQuiz.setVisibility(v), which makes it either visible or hidden based on the previous step.
     *
     * Similarly, the visibility of layoutImgQuiz (presumably a layout containing images) is checked and toggled using the same logic as above.
     *
     * The TransitionManager is again used to animate the visibility change of layoutImgQuiz with a ChangeScroll transition.
     *
     * The visibility of layoutImgQuiz is set to the value of l, making it either visible or hidden.
     * @param view
     */
    public void expandQuiz(View view) {
        int v = (tvDetailsQuiz.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
        TransitionManager.beginDelayedTransition(layoutExpandQuiz, new ChangeScroll() {
        });
        tvDetailsQuiz.setVisibility(v);

        int l = (layoutImgQuiz.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layoutImgQuiz, new ChangeScroll());
        layoutImgQuiz.setVisibility(l);

    }
}