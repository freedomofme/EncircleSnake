package com.basic.Snakes;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.basic.strategy.ChooseApple;
import com.xiegeixiong.guide.SnakeGuide;

import android.basic.lesson48.Coordinate;

public class BestSnake extends BasicSnake implements SurviveType, SearchPath{
	private int nowCount = 0;
	private ArrayList<Coordinate> savedArrayList = null;
	public BestSnake(int steps, int speed, int delay, boolean likeTrun, ChooseApple chooseApple) {
		super(steps, speed, delay, likeTrun, chooseApple);
	}

	@Override
	public Coordinate nextCoordinate(Coordinate miniApple, ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> mSnakeTrail ) {
		ArrayList<Coordinate> holdArrayList = sGuide.WalkFarest(miniApple, enemySnakeTrail, mSnakeTrail);
		ArrayList<Coordinate> holdArrayList2;
		ArrayList<Coordinate> holdArrayList3;
		Coordinate holdCoordinate = null;
		//至少走5步这样的路线
		nowCount = 1;
		if (isSafe(holdArrayList)) {
			holdCoordinate = holdArrayList.get(0);
			savedArrayList = holdArrayList;
//			System.out.println("44444");
		}
//		else {
//			holdArrayList2 = sGuide.WalkFurther(miniApple,
//					enemySnakeTrail);
//			if (isSafe(holdArrayList2)) {
//				holdCoordinate = holdArrayList2.get(0);
//				savedArrayList = holdArrayList2;
//				System.out.println("55555");
//			}
			else {
				holdArrayList3 = sGuide.WalkRandom(enemySnakeTrail, mSnakeTrail);
				if (isSafe(holdArrayList3)) {
					holdCoordinate = holdArrayList3.get(0);
					savedArrayList = holdArrayList3;
//					System.out.println("66666");
				}
				else {
					nowCount = 0;
					Coordinate h = sGuide.getSurround(enemySnakeTrail, mSnakeTrail);
					if(h != null)
						holdCoordinate = h;
//					System.out.println("88888");
				}
//			}
		}
		return holdCoordinate;
	}

	@Override
	public Coordinate searchCoordinate(Coordinate aCoordinate, ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> apples, ArrayList<Coordinate> mSnakeTrail) {
		Coordinate newEnemyHead = new Coordinate(1, 1);
		if(nowCount != 0 && savedArrayList.size() > 1) {
			savedArrayList.remove(0);
			nowCount--;
//			System.out.println("nowCount" + nowCount);
			return new Coordinate(savedArrayList.get(0).x, savedArrayList.get(0).y);
		}
		// 试探的蛇的数组
		ArrayList<Coordinate> miniSnakeTrailArrayList;
		// 试探的蛇的苹果
		Coordinate miniApple = new Coordinate(1, 1);
		// 开始深度优先寻找路径
		ArrayList<Coordinate> enemyPath = sGuide.DeepFirst(aCoordinate,enemySnakeTrail, mSnakeTrail);

		// 找到路径，而且这个路径在step步里能到终点
		if (isSafe(enemyPath)) {
//			System.out.println("主蛇安全！");
			newEnemyHead = new Coordinate(enemyPath.get(0).x,
					enemyPath.get(0).y);
			// 模拟一条蛇
			Coordinate miniSnakeHead = enemyPath.get(enemyPath.size() - 1);
			// 敌人的蛇头已经探测到到了苹果
			if (miniSnakeHead.h == 0) {
				int size = enemyPath.size();
				miniSnakeTrailArrayList = new ArrayList<Coordinate>();
				int j = 0;
				for (j = 0; j < enemySnakeTrail.size(); j++) {
					if (j < size) {
						miniSnakeTrailArrayList.add(new Coordinate(enemyPath
								.get(size - 1 - j).x, enemyPath.get(size - 1
								- j).y));
					} else {
						miniSnakeTrailArrayList.add(new Coordinate(
								enemySnakeTrail.get(j - size).x,
								enemySnakeTrail.get(j - size).y));
					}
				}
				// 设点mini蛇尾巴后面一个的位置为他的目标苹果，能吃到就事安全的。

				if (j < size) {
					miniApple = new Coordinate(enemyPath.get(size - 1 - j).x,
							enemyPath.get(size - 1 - j).y);
				} else {
					miniApple = new Coordinate(enemySnakeTrail.get(j - size).x,
							enemySnakeTrail.get(j - size).y);
				}
				// mini蛇从苹果到蛇尾的路径
				ArrayList<Coordinate> miniPath = sGuide.DeepFirst(
						miniApple, miniSnakeTrailArrayList, mSnakeTrail);
				if (isSafe(miniPath)) {
//					System.out.println("试探蛇安全");
					newEnemyHead = new Coordinate(enemyPath.get(0).x,enemyPath.get(0).y);
				} else {
					System.out.println("试探蛇被围住了");
					Coordinate holdCoordinate = new Coordinate(enemyPath.get(0).x,enemyPath.get(0).y);
					Coordinate h = nextCoordinate(miniApple, enemySnakeTrail, mSnakeTrail);
					if(h != null) {
						holdCoordinate = h;
						newEnemyHead = new Coordinate(holdCoordinate.x,
								holdCoordinate.y);
					} else {
//						System.out.println("不科学啊, null！");
					}
				}
				
			}
		}
		// 到不了终点
		else {
//			System.out.println("蛇被自己围住了");
//			System.out.println("当前路径长度： " + enemyPath.size());
			Coordinate holdCoordinate = new Coordinate(enemySnakeTrail.get(0).x, enemySnakeTrail.get(0).y + 1);
			
			Coordinate h = sGuide.getSurround(enemySnakeTrail, mSnakeTrail);
			if(h != null)
				holdCoordinate = h;

			newEnemyHead = new Coordinate(holdCoordinate.x,holdCoordinate.y);
//			System.out.println("...................Best:最后尝试enemySnakeTrail"+enemySnakeTrail);

		}
		return newEnemyHead;
	}
	
}
