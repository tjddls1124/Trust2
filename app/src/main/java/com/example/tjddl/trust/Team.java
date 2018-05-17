package com.example.tjddl.trust;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tjddl on 2018-05-16.
 */

public class Team { // Team 별로 관리
    String teamName;
    ArrayList<Integer> arr;
    int result;
    public Team(String name) {
        this.teamName = name;
        this.arr = new ArrayList<>();
        result = 10;
    }
    public Team(ArrayList<Integer> arr, int result) {
        this.arr = arr;
        this.result = result;
    }

    public int resulting(Team other, int term){ // Team 별 결과도출 알고리즘
        switch (this.teamName){
            case "Random":
                Random random = new Random();
                return random.nextInt(2);
            case "Peace":
                return 1;
            case "TitForTat" :
                if(term==0) return 0;
                else if(term == 10) return 1;
                else return other.arr.get(term-1);
        }
        return 0;
    }

}
