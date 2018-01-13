package com.xiegeixiong.guide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import android.R.bool;
import android.R.dimen;
import android.R.integer;
import android.basic.lesson48.Coordinate;
import android.basic.lesson48.SnakeView;

public class SnakeGuide {
	public int STEPS = 99;
	private int ADD = 1;
	private static boolean IsGoUP = true; 
	public  boolean LikeTurn = true;
	private static boolean isDone = false;
//	private static  ArrayList<Coordinate> isViewdArrayList = new ArrayList<Coordinate>();
	public   ArrayList<Coordinate> DeepFirst(Coordinate targetApple, ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> mSnakeTrail) {
		//这个地方的注释去掉，这个游戏就会卡住。
			isDone = false;
		//将玩家的蛇加入到isViewed中，防止与玩家蛇触碰。	
		ArrayList<Coordinate> isViewdArrayList = new ArrayList<Coordinate>(mSnakeTrail);
		
		ArrayList<Coordinate> pathArrayList = new ArrayList<Coordinate>();
		Coordinate head = enemySnakeTrail.get(0);
//		DoDeepFirst(head, pathArrayList, mAppleList.get(new Random().nextInt(mAppleList.size())));
		
		DoDeepFirst(head, pathArrayList, targetApple, enemySnakeTrail, isViewdArrayList);
//		System.out.println("STEPS" + STEPS);
		
		return pathArrayList;
	}
	
	private  void DoDeepFirst(Coordinate head, ArrayList<Coordinate> pathArrayList, Coordinate apple, ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> isViewdArrayList) {
	
		if(head.x == apple.x && head.y == apple.y) {
			isDone = true;
			return;
		}
		//超过n步就不要算了，怕你内存不够
		if(pathArrayList.size() > STEPS) {
//			System.out.println("steps步数到了");
			isDone = true;
			return;
		}
			
		ArrayList<Coordinate> candidator = new ArrayList<Coordinate>();
		candidator = getSurround(head, apple, pathArrayList, enemySnakeTrail, isViewdArrayList);
//		//走到胡同里了就返回null,再考虑下，不一定直接设置为true
		if(candidator.size() == 0) {
//			isDone = true;
			return;
		}
		
		for (int i = 0; i < candidator.size(); i++) {
			if (LikeTurn) {
				// 喜欢转弯
				if (i + 1 < candidator.size()
						&& candidator.get(i).h == candidator.get(i + 1).h) {
					i = i + ADD;
					if (ADD == 0) {
						ADD = 1;
					} else
						ADD = 0;
				}
			}
			pathArrayList.add(candidator.get(i));
			isViewdArrayList.add(candidator.get(i));
			DoDeepFirst(candidator.get(i), pathArrayList, apple, enemySnakeTrail, isViewdArrayList);
			if (isDone) return;
			pathArrayList.remove(pathArrayList.size() - 1);
			}
		
	}
	
	private  ArrayList<Coordinate> getSurround(Coordinate head, Coordinate apple, ArrayList<Coordinate> pathArrayList
			, ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> isViewdArrayList) {
		ArrayList<Coordinate> candidator = new ArrayList<Coordinate>();
		int x = 0;
		int y = 0;
		boolean flag = false;
		for (int i = 0; i < 4; i++) {
			if(i == 0) {
				x = 1;
				y = 0;
			} else if(i == 1) {
				x = 0;
				y = 1;
			} else if(i == 2) {
				x = -1;
				y = 0;
			} else {
				x = 0;
				y = -1;
			}
			Coordinate newHead = new Coordinate(head.x + x, head.y + y);
			
			if ((newHead.x < 0) || (newHead.y < 0)
					|| (newHead.x > SnakeView.mXTileCount - 1)
					|| (newHead.y > SnakeView.mYTileCount - 1))
				continue;
			//碰到自己continue
			for (Coordinate i1 : enemySnakeTrail) {
				if (i1.equals(newHead))
					flag = true;
			}

			if(flag) {
				flag = false;
				continue;
			}
		//	碰到自己走过来的路
			for (Coordinate i2 : pathArrayList) {
				if (i2.equals(newHead))
					flag = true;
			}
			if(flag) {
				flag = false;
				continue;
			}
			// 碰到自己扫描过的路
			for (Coordinate i2 : isViewdArrayList) {
				if (i2.equals(newHead))
					flag = true;
			}
			if (flag) {
				flag = false;
				continue;
			}
			
			if (i == 0) {
				if (apple.y == newHead.y && apple.x < newHead.x) {
					newHead.setH(getH(newHead, apple) + 1);
//					System.out.println("i:" + i);
				} else
					newHead.setH(getH(newHead, apple));
			}
			if (i == 1) {
				if (apple.x == newHead.x && apple.y < newHead.y) {
					newHead.setH(getH(newHead, apple) + 1);
//					System.out.println("i:" + i);
				} else
					newHead.setH(getH(newHead, apple));
			}
			if (i == 2) {
				if (apple.y == newHead.y && apple.x > newHead.x) {
					newHead.setH(getH(newHead, apple) + 1);
//					System.out.println("i:" + i);
				} else
					newHead.setH(getH(newHead, apple));
			}
			if (i == 3) {
				if (apple.x == newHead.x && apple.y > newHead.y) {
					newHead.setH(getH(newHead, apple) + 1);
//					System.out.println("i:" + i);
				} else
					newHead.setH(getH(newHead, apple));
			}
			
//			newHead.setH(getH(newHead, apple));
			candidator.add(newHead);
			//System.out.println("here:" + candidator);
		}
		if(candidator != null) {
			if(IsGoUP) {
				System.out.println("candidator.size(): " + candidator);
				java.util.Collections.sort(candidator,new Comparator<Coordinate>() {
					@Override
					public int compare(Coordinate lhs, Coordinate rhs) {
						// TODO Auto-generated method stub
						return lhs.h - rhs.h;
					}
				});
			}
			else  {
				java.util.Collections.sort(candidator, new Comparator<Coordinate>() {
					@Override
					public int compare(Coordinate lhs, Coordinate rhs) {
						// TODO Auto-generated method stub
						return rhs.h - lhs.h;
					}
				});
			}
			return candidator;
		} else {
			System.out.println("here:~~~~~~~~~~~~~~~~");
			return null;
		}
	
	}
	
	private  int getH(Coordinate head, Coordinate apple) {
		return Math.abs(apple.x - head.x) + Math.abs(apple.y - head.y);
	}

	public   ArrayList<Coordinate> WalkFurther(Coordinate miniApple, ArrayList<Coordinate> miniSnakeTrail, ArrayList<Coordinate> mSnakeTrail) {
		Random random = new Random();
		IsGoUP = false;
		ArrayList<Coordinate> miniPathArrayList = DeepFirst(miniApple, miniSnakeTrail, mSnakeTrail);
		IsGoUP = true;
		return miniPathArrayList;
		// TODO Auto-generated method stub
		
	}
	private  Coordinate GetFarestPosition(Coordinate apple) {
		int x = 1;
		int y = 1;
		if(apple.x < SnakeView.mXTileCount / 2)
			x = SnakeView.mXTileCount - 2;
		if(apple.y < SnakeView.mYTileCount / 2)
			y = SnakeView.mYTileCount - 2;
		
		return new Coordinate(x, y);
		
	}
	public   ArrayList<Coordinate> WalkRandom(ArrayList<Coordinate> miniSnakeTrail, ArrayList<Coordinate> mSnakeTrail) {
		Random random = new Random();
		int x = SnakeView.mXTileCount - miniSnakeTrail.get(0).x;
		int y = SnakeView.mYTileCount - miniSnakeTrail.get(0).y;
		 ArrayList<Coordinate> miniPathArrayList = DeepFirst(new Coordinate(random.nextInt(SnakeView.mXTileCount - 3) / 2 + x /2 , 
				random.nextInt(SnakeView.mYTileCount - 3) / 2 + y / 2),
				miniSnakeTrail,
				mSnakeTrail);
		return miniPathArrayList;
		// TODO Auto-generated method stub
		
	}

	public  ArrayList<Coordinate> WalkFarest(Coordinate holdCoordinate,
			ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> mSnakeTrail) {
		// TODO Auto-generated method stub
		ArrayList<Coordinate> miniPathArrayList = DeepFirst(GetFarestPosition(holdCoordinate), enemySnakeTrail, mSnakeTrail);
		return miniPathArrayList;
	}
	
	public  Coordinate getSurround(ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> mSnakeTrail) {
		Coordinate head = enemySnakeTrail.get(0);
		int x = 0;
		int y = 0;
		boolean flag = false;
		for (int i = 0; i < 4; i++) {
			if(i == 0) {
				x = 1;
				y = 0;
			} else if(i == 1) {
				x = 0;
				y = 1;
			} else if(i == 2) {
				x = -1;
				y = 0;
			} else {
				x = 0;
				y = -1;
			}
			
			
			Coordinate newHead = new Coordinate(head.x + x, head.y + y);
			//碰到边界continue
			if ((newHead.x < 0) || (newHead.y < 0)
					|| (newHead.x > SnakeView.mXTileCount - 1)
					|| (newHead.y > SnakeView.mYTileCount - 1))
				continue;
			//碰到mSnakeTrail,continue
			for(Coordinate c:mSnakeTrail) {
				if(c.equals(newHead)) {
					flag = true;
				}
			}
			if(flag) {
				flag = false;
				continue;
			}
			
			//碰到自己continue
			for (Coordinate i1 : enemySnakeTrail)
				if (i1.equals(newHead))
					flag = true;

			if(flag) {
				flag = false;
				continue;
			}
		
			return newHead;
			//System.out.println("here:" + candidator);
		}
		return null;
	}
	
	public boolean canReach(int x, int y) {
		if (x >= 0 && x <= SnakeView.mXTileCount - 1 && y >= 0
				&& y <= SnakeView.mYTileCount - 1) {
			return true;
		}
		return false;
	}
	
	public void setSteps(int steps) {
		this.STEPS = steps;
	}

}