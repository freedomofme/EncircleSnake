package com.basic.Snakes;
import java.util.ArrayList;

import android.basic.lesson48.*;

public interface SurviveType {
	public Coordinate nextCoordinate(Coordinate miniApple, ArrayList<Coordinate> enemySnakeTrail,  ArrayList<Coordinate> mSnakeTrail);
}
