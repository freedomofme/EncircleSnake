package com.xiegeixiong.guide;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


import android.basic.lesson48.Coordinate;
import android.basic.lesson48.Node;
import android.basic.lesson48.SnakeView;
import android.view.ViewDebug.FlagToString;

//A*算法，其实就是深度优先和广度优先的合体
public class SnakeGuideFaster {

//	private ArrayList<Node> openList = new ArrayList<Node>();
//	private ArrayList<Coordinate> closeList = new ArrayList<Coordinate>();
	private HashMap<Node, Boolean >  openHash=  new HashMap<Node, Boolean>();
	private HashMap<Coordinate, Boolean >  closeHash=  new HashMap<Coordinate, Boolean>();
	
	public Node doAPuls(Coordinate targetApple, ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> mSnakeTrail) {
		Node parent = null;
//		openList.clear();
//		closeList.clear();
		ArrayList<Node> neighborNodes = null;
		boolean flag = false;
		Node currentNode;
		openHash.clear();
		closeHash.clear();
		
		for(Coordinate holdCoordinate : enemySnakeTrail )
			closeHash.put(holdCoordinate, null);
		for(Coordinate holdCoordinate : mSnakeTrail )
			closeHash.put(holdCoordinate, null);
		
//		closeList.addAll(enemySnakeTrail);
//		System.out.println("%%%%%%%%%%%%%");
		Node startNode = new Node(enemySnakeTrail.get(0).x, enemySnakeTrail.get(0).y);
		Node endNode = new Node(targetApple.x, targetApple.y);
//		System.out.println("苹果：" + endNode);

		// 把起点加入 open list
		openHash.put(startNode, null);
		
		while (openHash.size() > 0)  {
			// 遍历 open list ，查找 F值最小的节点，把它作为当前要处理的节点
			currentNode = findMinFNodeInOpneList();
			// 从open list中移除
			openHash.remove(currentNode);
			
//			Coordinate aa = new Coordinate(currentNode.x, currentNode.y);
//			// 把这个节点移到 close list
//			System.out.println("closeHash.size()" + closeHash.size());
//			closeHash.put(aa,null);
//			System.out.println("closeHash.size()" + closeHash.size());
//			System.out.println("aa.getClass()" + aa.getClass());
			closeHash.put(currentNode, null);
//			System.out.println("closeHash.size()" + closeHash.size());
//			System.out.println("currentNode.getClass()" + currentNode.getClass());
			neighborNodes = findNeighborNodes(currentNode);
			for (Node node : neighborNodes) {
				if (exists(openHash, node)) {
					;
				} else {
					notFoundPoint(currentNode, endNode, node);
					
				}
			}
			
			//不能直接返回endNode，因为endNode里parent == null
			if (openHash.containsKey(endNode)) {
				flag = true;
				break;
			}

		}		
		if(flag == false)
			return null;
		for( Node n: neighborNodes) {
			if(n.equals(endNode))
				parent = n;
		}
//		
//		if(parent == null) {
//			System.out.println(" 没有路");
//			return null;
//		}
		ArrayList<Node> pathArrayList = new ArrayList<Node>();
		pathArrayList.add(endNode);
		while (parent.parent.parent != null) {
			pathArrayList.add(parent);
			parent = parent.parent;
			
		}
//		System.out.println("parent:" + parent);
		
		return parent;

	}

//	private void foundPoint(Node tempStart, Node node) {
//		if (tempStart.g < node.g)  {
//		System.out.println("绝对不会到这里。");
//		}
//	}

	private void notFoundPoint(Node tempStart, Node end, Node node) {
		node.parent = tempStart;
		node.g = calcG(tempStart, node);
		node.h = calcH(end, node);
		node.CalF();
		openHash.put(node, null);
	}

	public  Node find(HashMap<Node, Boolean > map, Node point) {
		if (map.containsKey(point))
			return point;
		return null;
	}
	
	private int calcG(Node start, Node node) {
		int parentG = node.parent != null ? node.parent.g : 0;
		return 1 + parentG;
	}

	private int calcH(Node end, Node node) {
		int step = Math.abs(node.x - end.x) + Math.abs(node.y - end.y);
		return step;
	}

//	public Node findMinFNodeInOpneList() {
//		Node tempNode = openList.get(0);
//		for (Node node : openList) {
//			if (node.f < tempNode.f) {
//				tempNode = node;
//			}
//		}
//		return tempNode;
//	}
	public Node findMinFNodeInOpneList() {
		Set<Node> set = openHash.keySet();
		Iterator<Node> iter= set.iterator();
		Node tempNode = iter.next();
		
		while(iter.hasNext()) {
			Node holdNode = iter.next();
			if(holdNode.f < tempNode.f)
				tempNode = holdNode;
		}
		
		return tempNode;
	}
	
	public ArrayList<Node> findNeighborNodes(Node currentNode) {
		ArrayList<Node> arrayList = new ArrayList<Node>();
		// 只考虑上下左右，不考虑斜对角
		int topX = currentNode.x;
		int topY = currentNode.y - 1;
		boolean can = false;
		Node tempNode = new Node(topX, topY);
		//排除不可达情况
		if (currentNode.x >= 1 && currentNode.x <= SnakeView.mXTileCount - 2 && currentNode.y >= 1
				&& currentNode.y <= SnakeView.mYTileCount - 2) {
			can =  true;
		}
		
		if ((can ||canReach(topX, topY)) && !exists2(closeHash, tempNode) ) {
			arrayList.add(tempNode);
		}
		
		int bottomX = currentNode.x;
		int bottomY = currentNode.y + 1;
		tempNode = new Node(bottomX, bottomY);
		if ((can || canReach(bottomX, bottomY)) && !exists2(closeHash, tempNode)) {
			arrayList.add(new Node(bottomX, bottomY));
		}
		int leftX = currentNode.x - 1;
		int leftY = currentNode.y;
		tempNode = new Node(leftX, leftY);
		if ((can || canReach(leftX, leftY)) && !exists2(closeHash, tempNode) ) {
			arrayList.add(tempNode);
		}
		int rightX = currentNode.x + 1;
		int rightY = currentNode.y;
		tempNode = new Node(rightX, rightY);
		if ((can || canReach(rightX, rightY)) && !exists2(closeHash, tempNode)) {
			arrayList.add(tempNode);
		}
		return arrayList;
	}

	public boolean canReach(int x, int y) {
		if (x >= 0 && x <= SnakeView.mXTileCount - 1 && y >= 0
				&& y <= SnakeView.mYTileCount - 1) {
			return true;
		}
		return false;
	}

	public static boolean exists(List<Coordinate> nodes, int x, int y) {
		for (Coordinate n : nodes) {
			if ((n.x == x) && (n.y == y)) {
				return true;
			}
		}
		return false;
	}
	

	public static boolean exists(List<Node> nodes, Node node) {
		for (Node n : nodes) {
			if ((n.x == node.x) && (n.y == node.y)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean exists(Map<Node, Boolean> nodes, Node node) {
		if(nodes.containsKey(node))
			return true;
		return false;
	}
	public static boolean exists2(Map<Coordinate, Boolean> coordinates, Node node) {
		if(coordinates.containsKey(node))
			return true;
		return false;
	}
	
	public ArrayList<Node> doAPuls2(Coordinate targetApple, ArrayList<Coordinate> enemySnakeTrail, ArrayList<Coordinate> mSnakeTrail) {
		Node parent = null;
//		openList.clear();
//		closeList.clear();
		ArrayList<Node> neighborNodes = null;
		boolean flag = false;
		Node currentNode;
		openHash.clear();
		closeHash.clear();
		
		for(Coordinate holdCoordinate : enemySnakeTrail )
			closeHash.put(holdCoordinate, null);
		for(Coordinate holdCoordinate : mSnakeTrail )
			closeHash.put(holdCoordinate, null);
		
//		closeList.addAll(enemySnakeTrail);
//		System.out.println("%%%%%%%%%%%%%");
		Node startNode = new Node(enemySnakeTrail.get(0).x, enemySnakeTrail.get(0).y);
		Node endNode = new Node(targetApple.x, targetApple.y);
//		System.out.println("苹果：" + endNode);

		// 把起点加入 open list
		openHash.put(startNode, null);
		
		while (openHash.size() > 0)  {
			// 遍历 open list ，查找 F值最小的节点，把它作为当前要处理的节点
			currentNode = findMinFNodeInOpneList();
			// 从open list中移除
			openHash.remove(currentNode);
			closeHash.put(currentNode, null);
			neighborNodes = findNeighborNodes(currentNode);
			for (Node node : neighborNodes) {
				if (exists(openHash, node)) {
					;
				} else {
					notFoundPoint(currentNode, endNode, node);
					
				}
			}
			
			//不能直接返回endNode，因为endNode里parent == null
			if (openHash.containsKey(endNode)) {
				flag = true;
				break;
			}

		}		
		if(flag == false)
			return null;
		for( Node n: neighborNodes) {
			if(n.equals(endNode)) {
				parent = n;
			}
		}
		
		if(parent == null) {
//			System.out.println(" 没有路");
			return null;
		}
		ArrayList<Node> pathArrayList = new ArrayList<Node>();
		pathArrayList.add(endNode);
		while (parent.parent.parent != null) {
			parent = parent.parent;
			pathArrayList.add(parent);
			
		}
//		System.out.println("parent:" + parent);
		
		return pathArrayList;

	}
	
	
}
