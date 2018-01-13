package com.basic.Snakes;

import java.util.ArrayList;

import com.basic.strategy.ChooseApple;
import com.xiegeixiong.guide.SnakeGuide;

import android.basic.lesson48.Coordinate;
import android.basic.lesson48.SnakeView;

public class BasicSnake implements SurviveType, SearchPath {
	public SnakeGuide sGuide = new SnakeGuide();
	public int steps = 50;
	public int speed = 50;
	public int delay = 50;
	public boolean likeTrun = false;
	public ChooseApple chooseApple = null;
	private ArrayList<Coordinate> savedArrayList = null;
	public BasicSnake(int steps, int speed, int delay, boolean likeTrun, ChooseApple ChooseApple) {
		this.steps = steps;
		this.speed = speed;
		this.likeTrun = likeTrun;
		this.chooseApple = ChooseApple;
		this.delay = delay;
		sGuide.LikeTurn = likeTrun;
		SnakeView.DELAY = delay;
		sGuide.setSteps(steps);
		System.out.println("sGuide.STEPS!!!!!" + sGuide.STEPS);
	}
	

	public ChooseApple getChooseAppleStrategy() {
		return chooseApple;
	}
	
	@Override
	public Coordinate nextCoordinate(Coordinate miniApple,
			ArrayList<Coordinate> enemySnakeTrail,  ArrayList<Coordinate> mSnakeTrail) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	protected boolean isSafe(ArrayList<Coordinate> holdArrayList) {
		return (holdArrayList.size() > 0 && (holdArrayList.get(holdArrayList.size() - 1).h == 0 ));
	}



	@Override
	public Coordinate searchCoordinate(Coordinate aCoordinate,
			ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> apples,ArrayList<Coordinate> mSnakeTrail) {

		Coordinate newEnemyHead = new Coordinate(enemySnakeTrail.get(0).x, enemySnakeTrail.get(0).y + 1);
		
		// 开始深度优先寻找路径
		ArrayList<Coordinate> enemyPath = sGuide.DeepFirst(aCoordinate,enemySnakeTrail , mSnakeTrail);
//		if(enemyPath.size() < 0 ) {
//			enemyPath = SnakeGuide.DeepFirst(new Coordinate(1, 1),enemySnakeTrail);
//			if(enemyPath.size() < 0) {
//				enemyPath = SnakeGuide.DeepFirst(new Coordinate(SnakeView.mXTileCount - 2, SnakeView.mYTileCount - 2),enemySnakeTrail);
//				if(enemyPath.size() < 0) {
//					enemyPath = SnakeGuide.DeepFirst(new Coordinate(1, SnakeView.mYTileCount - 2),enemySnakeTrail);
//					if(enemyPath.size() < 0)
//						enemyPath = SnakeGuide.DeepFirst(new Coordinate(SnakeView.mXTileCount - 2, 1),enemySnakeTrail);
//				}
//			}
//		}
		

//		System.out.println("Basic ,enemyPath.size()" + enemyPath.size());
//		System.out.println("Basic ,enemyPath" + enemyPath);
		if(enemyPath.size() > 0 ) {
			newEnemyHead = new Coordinate(enemyPath.get(0).x, enemyPath.get(0).y);
//			System.out.println("AAAAAAA");
		}
		else if(enemyPath.size() == 0 )  {
			Coordinate h = 	sGuide.getSurround(enemySnakeTrail, mSnakeTrail) ;
			if (h != null)
				newEnemyHead = h;
//			System.out.println("BBBBBBBB");
		}
//		else 
//			System.out.println("CCCCCCCCC");
		
	
		return newEnemyHead;
	}

}
