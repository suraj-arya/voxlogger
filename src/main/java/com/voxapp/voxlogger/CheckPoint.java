package com.voxapp.voxlogger;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by surya on 1/2/15.
 * this class encapsulates a list of
 * String checkpoints
 */
public class CheckPoint {
    //list of string checkPoints
    private List<String> checkPoints = new ArrayList<String>();
    private int currIndex = -1;

    public void add(String checkPointName){
        checkPoints.add(checkPointName);
    }

    public void add(String... checkPoints){
        for (String checkPoint : checkPoints){
            this.checkPoints.add(checkPoint);
        }
    }

    public void remove(String checkPointName){
        checkPoints.remove(checkPointName);
    }

    public List<String> getAll(){
        return checkPoints;
    }

    public String next(){
        currIndex++;
        if (currIndex == checkPoints.size()) {
            currIndex = 0;
        }
        return checkPoints.get(currIndex);
    }

    public String getCurrent(){
        return checkPoints.get(currIndex);
    }

    public String get(int idx){
        return checkPoints.get(idx);
    }
}
