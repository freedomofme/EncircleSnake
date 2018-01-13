package com.basic.Snakes;

import java.util.ArrayList;

import android.basic.lesson48.Coordinate;

import com.basic.strategy.ChooseApple;
import com.xiegeixiong.guide.SnakeGuideFaster;

public class AplusBasicSnake extends BasicSnake implements SurviveType,
		SearchPath {
	SnakeGuideFaster aFasterGuide = new SnakeGuideFaster();

	public AplusBasicSnake(int steps, int speed, int delay, boolean likeTrun,
			ChooseApple ChooseApple) {
		super(steps, speed, delay, likeTrun, ChooseApple);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Coordinate searchCoordinate(Coordinate aCoordinate,
			ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> apples ,ArrayList<Coordinate> mSnakeTrail) {
		Coordinate newEnemyHead = new Coordinate(enemySnakeTrail.get(0).x,
				enemySnakeTrail.get(0).y + 1);

		// 开始A*算法寻找路径
		Coordinate enemyNode = aFasterGuide.doAPuls(aCoordinate,
				enemySnakeTrail ,mSnakeTrail);

		if (enemyNode != null) {
			//往一个苹果走
			newEnemyHead = new Coordinate(enemyNode.x, enemyNode.y);
//			System.out.println("A方案:");
//			System.out.println("新蛇头:" + newEnemyHead);
		} else {
			//一个没路，往另一个走
			Coordinate hold = null;
			for (Coordinate theother : apples)
				if (!theother.equals(aCoordinate))
					hold = theother;
			enemyNode = aFasterGuide.doAPuls(hold, enemySnakeTrail, mSnakeTrail);
			if (enemyNode != null) {
				newEnemyHead = new Coordinate(enemyNode.x, enemyNode.y);
			} else {
				//都没路，随便走
				Coordinate h = sGuide.getSurround(enemySnakeTrail, mSnakeTrail);
				if (h != null)
					newEnemyHead = h;
//				System.out.println("BBBBBBBB");
			}
		}
		return newEnemyHead;
	}

	@Override
	public Coordinate nextCoordinate(Coordinate miniApple,
			ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> mSNArrayList) {
		// TODO Auto-generated method stub
		return null;
	}

}
