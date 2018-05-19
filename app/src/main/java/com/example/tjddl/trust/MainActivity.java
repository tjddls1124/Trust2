package com.example.tjddl.trust;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Boolean isPlaying = false;
    Team A,B;
    static final int maxTerm = 10;
    static final int cc = 0;
    static final int cb = 1;
    static final int bc = 2;
    static final int bb = 3;

    private static MediaPlayer mp;

    Spinner spinnerLeft, spinnerRight;
    ImageView ivPeepLeft,ivPeepRight,ivMachine,ivPayoff,ivCoinLeft,ivCoinRight;
    TextView tv_scoreLeft , tv_scoreRight;
    Animation left_translate,right_translate,insertCoinLeft,insertCoinRight,goBackLeft,goBackRight,trans_coin_left,trans_coin_right,init_LeftCoin,init_rightCoin;
    SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
    int soundId;

    ArrayList<String> teamNameList= new ArrayList<>();
    ArrayList<Team> teamList = new ArrayList<>();

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mp = MediaPlayer.create(this, R.raw.bg_music);
        mp.setLooping(true);
        mp.start();

        soundId = sound.load(this, R.raw.coin_get, 1);

        makeTeam("Random","Peace","TitForTat"); // 팀 명 추가

        setAnimation();

        ivPeepLeft.startAnimation(left_translate);
        ivPeepRight.startAnimation(right_translate);

        //Adapter 선언
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,teamNameList);


        //UI
        ivPeepRight.setScaleX((float)-1);
        ivPeepRight.setScaleY((float)1);
        spinnerRight.setAdapter(adapter);
        spinnerLeft.setAdapter(adapter);

    }

    int getResult(Team thisTeam, Team otherTeam, int term){ //0 은 협력, 1은 배반
        if( thisTeam.arr.get(term) == 0 && otherTeam.arr.get(term) == 0){ // 상호협력
            thisTeam.result += 2;
            otherTeam.result +=2;
            return cc;
        }
        if( thisTeam.arr.get(term) == 0 && otherTeam.arr.get(term) == 1){ // 협력,배반
            thisTeam.result -= 1;
            otherTeam.result += 3;
            return cb;
        }
        if( thisTeam.arr.get(term) == 1 && otherTeam.arr.get(term) == 0){ //배반, 협력
            thisTeam.result += 3;
            otherTeam.result -=1;
            return bc;
        }
        if( thisTeam.arr.get(term) == 1 && otherTeam.arr.get(term) == 1){ // 상호배반
            thisTeam.result += 0;
            otherTeam.result +=0;
            return bb;
        }
        return 0;
    }





    void makeTeam(String... team){
        for(String name : team){
            ArrayList<Integer> arr = new ArrayList<>();
            Team t = new Team(name);
            teamList.add(t);
            teamNameList.add(name);
        }
    }
    void onClick(View v){
        if(isPlaying) return; // until end

        int left = spinnerLeft.getSelectedItemPosition();
        int right = spinnerRight.getSelectedItemPosition();
        A = teamList.get(left);
        B = teamList.get(right);
       if(A.equals(B)) B = new Team(teamNameList.get(right));
        MyTask myTask = new MyTask();
        myTask.execute();
        tv_scoreLeft.setText("10");
        tv_scoreRight.setText("10");

    }

    /**
     * set Animation
     */
    void setAnimation(){

        left_translate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_left_to_right);
        right_translate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_right_to_left);
        insertCoinLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.insert_coin_left);
        insertCoinRight = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.insert_coin_right);
        goBackLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.goback_left);
        goBackRight = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.goback_right);
        trans_coin_left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_coin_left_to_right);
        trans_coin_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_coin_right_to_left);
        init_LeftCoin = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.init_left_coin);
        init_rightCoin = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.init_right_coin);
    }

    /**
     * initiating
     */
    void init(){
        spinnerLeft = (Spinner)findViewById(R.id.spinner_left);
        spinnerRight = (Spinner)findViewById(R.id.spinner_right);
        ivPeepLeft = (ImageView)findViewById(R.id.imageView_leftPeep);
        ivPeepRight = (ImageView)findViewById(R.id.imageView_rightPeep);
        ivMachine = (ImageView)findViewById(R.id.imageView_machine);
        tv_scoreLeft = (TextView)findViewById(R.id.tv_scoreLeft);
        tv_scoreRight =(TextView)findViewById(R.id.tv_scoreRight);
        ivPayoff = (ImageView)findViewById(R.id.imageView_payoff);
        ivCoinLeft = (ImageView)findViewById(R.id.imageView_coinLeft);
        ivCoinRight = (ImageView)findViewById(R.id.imageView_coinRight);
    }


    //resulting Animation
    //결과 보여주기
    public void playResultAnimation(final int payoff,final int res){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Change & Announcing Score
                ivPayoff.setImageResource(payoff);
                tv_scoreLeft.setText(""+A.result);
                tv_scoreRight.setText(""+B.result);
                int streamId = sound.play(soundId, 1.0F, 1.0F,  1,  0,  1.0F);

                switch (res){
                    case cc :
                        ivCoinLeft.setVisibility(View.INVISIBLE);
                        ivCoinRight.setVisibility(View.INVISIBLE);
                        ivPeepLeft.setImageResource(R.drawable.cc_peep);
                        ivPeepRight.setImageResource(R.drawable.cc_peep);
                        break;
                    case cb :
                        ivCoinLeft.setVisibility(View.INVISIBLE);
                        ivCoinRight.setVisibility(View.VISIBLE);
                        ivPeepLeft.setImageResource(R.drawable.c_peep);
                        ivPeepRight.setImageResource(R.drawable.b_peep);
                        break;
                    case bc:
                        ivCoinLeft.setVisibility(View.VISIBLE);
                        ivCoinRight.setVisibility(View.INVISIBLE);
                        ivPeepLeft.setImageResource(R.drawable.b_peep);
                        ivPeepRight.setImageResource(R.drawable.c_peep);
                        break;
                    case bb:
                        ivCoinLeft.setVisibility(View.INVISIBLE);
                        ivCoinRight.setVisibility(View.INVISIBLE);
                        ivPeepLeft.setImageResource(R.drawable.bb_peep);
                        ivPeepRight.setImageResource(R.drawable.bb_peep);
                        break;
                }
            }
        },3000);
    }

    @Override
    public void onDestroy() {
        mp.stop();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        mp.start();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        mp.stop();
        super.onBackPressed();
    }

    /**
     Animation after Inserting
     */
    public void afterInsertAnimation(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //Peep return
                ivPeepLeft.setImageResource(R.drawable.splash_peep);
                ivPeepRight.setImageResource(R.drawable.splash_peep);

                //go back
                ivPeepLeft.startAnimation(goBackLeft);
                ivPeepRight.startAnimation(goBackRight);

                ivCoinRight.setVisibility(View.INVISIBLE);
                ivCoinLeft.setVisibility(View.INVISIBLE);



            }
        }, 4500);
    }

    /**
     * UI Thread _ AsyncTask
     * Animation working
     */
    class MyTask extends AsyncTask<Void,Integer,Void>{
        @Override
        protected void onPreExecute() {
            isPlaying = true; // 실행중
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(isCancelled()) return null;

            for (int i = 0; i <maxTerm; i++) {
                try {
                    publishProgress(i);
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            A.addToResultInArr(B,values[0]);
            B.addToResultInArr(A,values[0]);
            int result = getResult(A,B,values[0]);

            //init Coin
            ivCoinRight.setVisibility(View.VISIBLE);
            ivCoinLeft.setVisibility(View.VISIBLE);
            ivCoinLeft.startAnimation(init_LeftCoin);
            ivCoinRight.startAnimation(init_rightCoin);

            //Move Peep&Coin
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ivPeepLeft.startAnimation(insertCoinRight);
                    ivPeepRight.startAnimation(insertCoinLeft);
                    ivCoinLeft.startAnimation(trans_coin_left);
                    ivCoinRight.startAnimation(trans_coin_right);
                }
            },500);




            switch (result) { // Animation Play, 비프 음 mp3 play 구현할 것
                case cc:
                    afterInsertAnimation();
                    playResultAnimation(R.drawable.iterated_payoffs_cc,cc);
                    break;
                case bc:
                    afterInsertAnimation();
                    playResultAnimation(R.drawable.iterated_payoffs_bc,bc);
                    break;
                case cb:
                    afterInsertAnimation();
                    playResultAnimation(R.drawable.iterated_payoffs_cb,cb);
                    break;
                case bb:
                    afterInsertAnimation();
                    playResultAnimation(R.drawable.iterated_payoffs_bb,bb);
                    break;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //게임이 끝나면 초기화
            A.arr = new ArrayList<>();
            B.arr = new ArrayList<>();
            A.result = 10;
            B.result = 10;
            ivPayoff.setImageResource(R.drawable.iterated_payoffs);
            isPlaying = false; // make Playing non-simulating
        }
    }
}

