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
                int rand = random.nextInt(2);
                return rand;
            case "Peace":
                return 0;
            case "TitForTat" :
                if(term==0) return 0;
                else if(term == 10) return 1;
                else return other.arr.get(term-1);

            case "전설의 타이거" :
                Random rand3 = new Random();
                int num = rand3.nextInt(2);
                if (term == 0)
                    return 1;
                if (other.arr.get(term-1) == 1 )
                    return num;
                else
                    return 1;


            case "크로스팀" :
                if (term == 0 )
                    return 1;
                int zero = 0;
                int one = 0;
                int cnt = 0;
                while (cnt < term) {
                    if (other.arr.get(cnt) == 0)
                        zero++;
                    else if (other.arr.get(cnt) == 1)
                        one++;
                    cnt++;
                }
                if (one > zero)
                    return 1;
                else
                    return 0;

            case "Wind" :
                Random rand5 = new Random();
                int choise4  = rand5.nextInt(2);

                  switch (term) {
                    case 0:
                        return 0;
                    case 1:
                        return 0;
                    case 2:
                        return 0;
                    case 3:
                        return 0;
                    case 4:
                        return 1;
                    case 5:
                        return choise4;
                    case 6:
                        return 1;
                    case 7:
                        return 1;
                    case 8:
                        if(other.arr.get(term-1) ==0){
                            return 1;
                        }
                        else {
                            return 0;
                        }
                    case 9:
                        return choise4;

                }
        }
        return 0;
    }
    public int addToResultInArr(Team other,int term){
        int res = resulting(other,term);
        this.arr.add(res);
        return res;
    }

}
