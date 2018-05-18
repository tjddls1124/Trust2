package com.example.tjddl.trust;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int maxTerm = 10;
    static final int cc = 0;
    static final int cb = 1;
    static final int bc = 2;
    static final int bb = 3;

    Spinner spinnerLeft, spinnerRight;
    ImageView ivPeepLeft,ivPeepRight,ivMachine;
    TextView tv_scoreLeft , tv_scoreRight;

    ArrayList<String> teamNameList= new ArrayList<>();
    ArrayList<Team> teamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


        makeTeam("Random","Peace","TitForTat"); // 팀 명 추가할 것


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
        int left = spinnerLeft.getSelectedItemPosition();
        int right = spinnerRight.getSelectedItemPosition();
        Team A = teamList.get(left);
        Team B = teamList.get(right);

        for(int i = 0 ; i < maxTerm ; i++){
            A.addToResultInArr(B,i);
            B.addToResultInArr(A,i);
            int result = getResult(A,B,i);
            switch (result){ // Animation Play, 비프 음 mp3 play 구현할 것
                case cc:
                    break;
                case bc:
                    break;
                case cb:
                    break;
                case bb:
                    break;
            }
            SystemClock.sleep(100);
            tv_scoreLeft.setText(""+A.result + i);
            tv_scoreRight.setText(""+B.result + i);
            SystemClock.sleep(100);

        }
        //게임이 끝나면 초기화
        A.arr = new ArrayList<>();
        B.arr = new ArrayList<>();
        A.result = 10;
        B.result = 10;


    }
    void init(){
        spinnerLeft = (Spinner)findViewById(R.id.spinner_left);
        spinnerRight = (Spinner)findViewById(R.id.spinner_right);
        ivPeepLeft = (ImageView)findViewById(R.id.imageView_leftPeep);
        ivPeepRight = (ImageView)findViewById(R.id.imageView_rightPeep);
        ivMachine = (ImageView)findViewById(R.id.imageView_machine);
        tv_scoreLeft = (TextView)findViewById(R.id.tv_scoreLeft);
        tv_scoreRight =(TextView)findViewById(R.id.tv_scoreRight);
    }
}
