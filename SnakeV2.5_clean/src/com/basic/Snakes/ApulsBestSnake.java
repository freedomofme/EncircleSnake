package com.basic.Snakes;

import java.util.ArrayList;

import android.basic.lesson48.Coordinate;
import android.basic.lesson48.Node;

import com.basic.strategy.ChooseApple;
import com.xiegeixiong.guide.SnakeGuideFaster;

public class ApulsBestSnake extends BasicSnake implements SurviveType,
		SearchPath {
	SnakeGuideFaster aFasterGuide = new SnakeGuideFaster();

	public ApulsBestSnake(int steps, int speed, int delay, boolean likeTrun,
			ChooseApple ChooseApple) {
		super(steps, speed, delay, likeTrun, ChooseApple);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Coordinate searchCoordinate(Coordinate aCoordinate,
			ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> apples ,ArrayList<Coordinate> mSnakeTrail) {
		Coordinate newEnemyHead = new Coordinate(enemySnakeTrail.get(0).x,
				enemySnakeTrail.get(0).y + 1);
		Coordinate enemyNode = null;
		// 开始A*算法寻找路径
		ArrayList<Node> path = aFasterGuide.doAPuls2(aCoordinate,
				enemySnakeTrail ,mSnakeTrail);
		Coordinate miniNode = null;
		Coordinate miniNode2 = null;
		if (path != null) {
			enemyNode = path.get(path.size() - 1);

			miniNode = creatMiniSnake(enemySnakeTrail, path,
					mSnakeTrail);
//			System.out.println("A点可达:");
//			if (miniNode != null) {
//				System.out.println("mini蛇安全");
//			}
//			else {
//				System.out.println("mini蛇bububu安全");
//			}
		}
//		else{
//			System.out.println("A点不可达");
//		}
		
		if (enemyNode != null && miniNode != null) {
			//往一个苹果走
			newEnemyHead = new Coordinate(enemyNode.x, enemyNode.y);
			
		} else {
			//一个没路，往另一个走
			Coordinate hold = null;
			for (Coordinate theother : apples)
				if (!theother.equals(aCoordinate))
					hold = theother;
			path = aFasterGuide.doAPuls2(hold, enemySnakeTrail, mSnakeTrail);
			if(path != null) {
//				System.out.println("B点可达");
				enemyNode = path.get(path.size() - 1);
				miniNode2 = creatMiniSnake(enemySnakeTrail, path,
						mSnakeTrail);
//				if (miniNode2 != null) {
//					System.out.println("第二次选择mini蛇安全");
//				}else {
//					System.out.println("第二次选择minibububu蛇安全");
//				}
				
			}
//			else {
//				System.out.println("B点不可达");
//			}
			
			if (enemyNode != null && miniNode2 != null) {
				newEnemyHead = new Coordinate(enemyNode.x, enemyNode.y);
			} else {
				//都没路，随便走
				Coordinate h = sGuide.getSurround(enemySnakeTrail, mSnakeTrail);
				if (h != null)
					newEnemyHead = h;
//				System.out.println("方案3");
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

	private Coordinate creatMiniSnake(ArrayList<Coordinate> enemySnakeTrail, ArrayList<Node> path, ArrayList<Coordinate> mSnakeTrail) {
		ArrayList<Coordinate> miniSnakeArrayList = new ArrayList<Coordinate>();
		int enmeySize = enemySnakeTrail.size();
		int i = 0;
		for (; i < enmeySize; i++) {
			if(i < path.size()) {
				miniSnakeArrayList.add(new Coordinate(path.get(i).x, path.get(i).y));
			} else {
				miniSnakeArrayList.add(new Coordinate(enemySnakeTrail.get(i - path.size()).x, enemySnakeTrail.get(i - path.size()).y));
			}
		}
		
		Coordinate miniApple = null;
		if (i < path.size()) {
			 miniApple = new Coordinate(path.get(path.size() - 1 - i).x,
					path.get(path.size() - 1 - i).y);
		} else {
			 miniApple = new Coordinate(enemySnakeTrail.get(i - path.size()).x,
					enemySnakeTrail.get(i - path.size()).y);
		}
		
		
		Coordinate miniNode = aFasterGuide.doAPuls(miniApple, miniSnakeArrayList, mSnakeTrail);
		return miniNode;
	}
}
