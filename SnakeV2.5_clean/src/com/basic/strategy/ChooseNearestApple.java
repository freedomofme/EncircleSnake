package com.basic.strategy;

import java.util.ArrayList;

import javax.security.auth.x500.X500Principal;

import android.R.bool;
import android.R.integer;
import android.basic.lesson48.Coordinate;


public class ChooseNearestApple implements ChooseApple{
//	public static int X = -1;
//	public static int Y = -1;
//	public static int X2 = -1;
//	public static int Y2 = -1;
	@Override
	public Coordinate choose(ArrayList<Coordinate> mAppleArrayList, Coordinate mHead,
			Coordinate enemyHead) {
		// 选择更远的苹果
//		for(Coordinate coordinate : mAppleArrayList) {
//			if(coordinate.x == X && coordinate.y == Y) {
//				System.out.println("选择之前的苹果");
//				return coordinate;
//			}
//			if(coordinate.x == X2 && coordinate.y == Y2) {
//				System.out.println("选择之前的苹果");
//				return coordinate;
//			}
//		}
			
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
		
		if( enemyIndex == mIndex && enemyMin > mMin && mMin < 10) {
//			X = mAppleArrayList.get((enemyIndex == 1? 0:1)).x;
//			Y = mAppleArrayList.get((enemyIndex == 1? 0:1)).y;
			return mAppleArrayList.get((enemyIndex == 1? 0:1));
		}
//		X2 = mAppleArrayList.get(enemyIndex).x;
//		Y2 = mAppleArrayList.get(enemyIndex).y;
		return mAppleArrayList.get(enemyIndex);
	}
	
	private int distance(Coordinate A, Coordinate B) {
		return Math.abs(A.x - B.x) + Math.abs(A.y - B.y);
	}
	
}