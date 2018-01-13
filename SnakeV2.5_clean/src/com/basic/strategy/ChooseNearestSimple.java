package com.basic.strategy;

import java.util.ArrayList;

import android.basic.lesson48.Coordinate;

public class ChooseNearestSimple implements ChooseApple {

	@Override
	public Coordinate choose(ArrayList<Coordinate> mAppleArrayList,
			Coordinate mHead, Coordinate enemyHead) {
		// TODO Auto-generated method stub
		Coordinate apple1 = mAppleArrayList.get(0);
		Coordinate apple2 = mAppleArrayList.get(1);
		int distance1 = Math.abs(apple1.x - enemyHead.x) + Math.abs(apple1.y - enemyHead.y); 
		int distance2 = Math.abs(apple2.x - enemyHead.x) + Math.abs(apple2.y - enemyHead.y);
		
		return distance1 < distance2 ? apple1 : apple2;
	}

}
