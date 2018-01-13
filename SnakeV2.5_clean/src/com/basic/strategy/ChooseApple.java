package com.basic.strategy;

import java.util.ArrayList;

import android.basic.lesson48.Coordinate;

public interface ChooseApple {
	public Coordinate choose(ArrayList<Coordinate> mAppleArrayList, Coordinate mHead, Coordinate enemyHead);
}