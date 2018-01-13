package com.basic.strategy;

import java.util.ArrayList;

import android.basic.lesson48.Coordinate;

public class ChooseFirst implements ChooseApple {

	@Override
	public Coordinate choose(ArrayList<Coordinate> mAppleArrayList,
			Coordinate mHead, Coordinate enemyHead) {
		// TODO Auto-generated method stub
		
		return mAppleArrayList.get(0);
	}

}
