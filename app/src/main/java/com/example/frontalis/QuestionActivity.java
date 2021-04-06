package com.example.frontalis;

import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.frontalis.Adapter.AnswerSheetAdapter;
import com.example.frontalis.Adapter.QuestionFragmentAdapter;
import com.example.frontalis.Common.Common;
import com.example.frontalis.DBHelper.DBHelper;
import com.example.frontalis.Model.CurrentQuestion;
import com.example.frontalis.Model.Question;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    int time_play = Common.TOTAL_TIME;
    TextView txt_right_answer, txt_timer;
    @Override
    protected void onDestroy() {
        if (Common.countDownTimer != null)
            Common.countDownTimer.cancel();
        super.onDestroy();
    }
    private CountDownTimer mCountDownTimer = Common.countDownTimer;

    private boolean mTimerRunning;
    ViewPager viewPager;
    TabLayout tabLayout;
    private long mTimeLeftInMillis = time_play;
    private long mEndTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.selectedCategory.getName());
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        takeQuestion();

        if (Common.questionList.size() > 0){

            txt_timer = findViewById(R.id.txt_timer);

            txt_timer.setVisibility(View.VISIBLE);
            txt_right_answer = findViewById(R.id.txt_question_right);
            txt_right_answer.setVisibility(View.VISIBLE);
            txt_right_answer.setText("1/" + Common.questionList.size());
            startTimer();

            viewPager = findViewById(R.id.viewpager);
            tabLayout = findViewById(R.id.sliding_tabs);

            genFragmentList();

            QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(), this,
                    Common.fragmentList);
            viewPager.setAdapter(questionFragmentAdapter);
            tabLayout.setupWithViewPager(viewPager);


             viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                int SCROLLING_RIGHT = 0;
                int SCROLLING_LEFT = 1;
                int SCROLLING_UNDETERMINED = 2;

                int currentScrollDirection = 2;

                private void setScrollDirection(float positionOffset){
                    if ((1 - positionOffset) >= 0.5)
                        this.currentScrollDirection = SCROLLING_RIGHT;
                    else if ((1 - positionOffset) <= 0.5)
                        this.currentScrollDirection = SCROLLING_LEFT;
                }
                private boolean isScrollDirectionUndetermined(){
                    return currentScrollDirection == SCROLLING_UNDETERMINED;
                }
                private boolean isScrollRight(){
                    return currentScrollDirection == SCROLLING_RIGHT;
                }
                private boolean isScrollLeft(){
                    return currentScrollDirection == SCROLLING_LEFT;
                }
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (isScrollDirectionUndetermined())
                        setScrollDirection(positionOffset);
                }

                @Override
                public void onPageSelected(int i) {
                    QuestionFragment questionFragment;

                    int position = 0;
                    if (i > 0){
                         if (isScrollRight()){
                            questionFragment = Common.fragmentList.get(i - 1);
                            position = i - 1;
                        }
                        else if (isScrollLeft()){
                            questionFragment = Common.fragmentList.get(i + 1);
                            position = i + 1;
                        }
                        else{
                            questionFragment = Common.fragmentList.get(position);
                        }
                    }else{
                        questionFragment = Common.fragmentList.get(0);
                        position = 0;
                    }
                    CurrentQuestion question_state = questionFragment.getSelectedAnswer();
                    Common.answerSheetList.set(position, question_state);
                    countCorrectAnswer();
                    Log.d("MAIN", String.valueOf(Common.right_answer_count));
                    txt_right_answer.setText(new StringBuilder(String.format(Locale.getDefault(), "%d", Common.right_answer_count))
                        .append("/")
                        .append(String.format(Locale.getDefault(), "%d", Common.questionList.size())).toString());

                    //txt_right_answer.setText(Common.questionList.get(position).getCorrectAnswer());
                    //Toast.makeText(QuestionActivity.this, Common.questionList.get(position).getCorrectAnswer(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE)
                        this.currentScrollDirection = SCROLLING_UNDETERMINED;
                }
            });

        }
    }
    private void countCorrectAnswer(){
        Common.right_answer_count = Common.wrong_answer_count = 0;
        for (CurrentQuestion item: Common.answerSheetList) {
            if (item.getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
                Common.right_answer_count++;
            else if (item.getType() == Common.ANSWER_TYPE.WRONG_ANSWER)
                Common.wrong_answer_count++;
        }
    }
    private void genFragmentList(){
        Common.fragmentList.clear();
        for (int i = 0; i < Common.questionList.size(); i++){
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            QuestionFragment fragment = new QuestionFragment();
            fragment.setArguments(bundle);
            Common.fragmentList.add(fragment);
        }
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                finish();
            }
        }.start();

        mTimerRunning = true;
    }
    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        txt_timer.setText(timeLeftFormatted);
    }
    private void takeQuestion(){
        Common.questionList = DBHelper.getInstance(this).getQuestionByCategory(Common.selectedCategory.getId());
        if (Common.questionList.size() == 0){
            new MaterialStyledDialog.Builder(this)
                    .setTitle("Opps !")
                    .setIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                    .setDescription("We don't have any questions in this " + Common.selectedCategory.getName() + " category")
                    .setPositiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }else{

            if (Common.answerSheetList.size() > 0)
                Common.answerSheetList.clear();

            for (int i = 0; i < Common.questionList.size(); i++){
                Common.answerSheetList.add(new CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER));
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_finish){
            Toast.makeText(this, String.valueOf(Common.right_answer_count), Toast.LENGTH_SHORT).show();
            new MaterialStyledDialog.Builder(this)
                    .setTitle("Finish?")
                    .setIcon(R.drawable.ic_mood_black_24dp)
                    .setDescription("Are you sure?")
                    .setNegativeText("No")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveText("Yes")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finishTest();
                            Log.d("Main", String.valueOf(Common.right_answer_count));
                        }
                    }).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void finishTest() {
        int position = viewPager.getCurrentItem();
        QuestionFragment questionFragment = Common.fragmentList.get(position);
        CurrentQuestion question_state = questionFragment.getSelectedAnswer();
        Common.answerSheetList.set(position, question_state);

        countCorrectAnswer();

        txt_right_answer.setText((position + 1) + "/" + Common.questionList.size());
    }
}
