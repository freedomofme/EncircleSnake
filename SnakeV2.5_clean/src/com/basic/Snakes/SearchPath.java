package com.basic.Snakes;

import java.util.ArrayList;

import android.basic.lesson48.Coordinate;

public interface SearchPath {
	public Coordinate searchCoordinate(Coordinate aCoordinate, ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> apples,
			ArrayList<Coordinate> mSnakeTrail);
}
