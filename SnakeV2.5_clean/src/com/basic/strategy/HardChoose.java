package com.basic.strategy;

import java.util.ArrayList;


import android.R.bool;
import android.R.integer;
import android.basic.lesson48.Coordinate;


public class HardChoose implements ChooseApple{

	@Override
	public Coordinate choose(ArrayList<Coordinate> mAppleArrayList, Coordinate mHead,
			Coordinate enemyHead) {
			
		int mMin = 0xffffff;
		int mIndex = 0;
		int enemyMin = 0xffffff;
		int enemyIndex = 0;
		int hold;
		for(int i = 0; i < mAppleArrayList.size(); i++) {
			hold = distance(mHead, mAppleArrayList.get(i));
			if(hold < mMin) { 
				mMin = hold; 
				mIndex = i;
			}
			hold = distance(enemyHead, mAppleArrayList.get(i));
			if(hold < enemyMin) {
				enemyMin = hold;
				enemyIndex = i;
			}
		}
		
		if( enemyIndex == mIndex && enemyMin > mMin && mMin < 150) {
			return mAppleArrayList.get((enemyIndex == 1? 0:1));
		}

		return mAppleArrayList.get(enemyIndex);
	}
	
	private int distance(Coordinate A, Coordinate B) {
		return  (int) Math.pow((A.x - B.x) * (A.x - B.x) + (A.y - B.y) * (A.x - B.x), 0.5);
	}
	
}