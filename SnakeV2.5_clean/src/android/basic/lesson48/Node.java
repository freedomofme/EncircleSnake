package android.basic.lesson48;

public class Node extends Coordinate {
	
	public int g;
	public int f;
	public Node parent;
	public Node(int newX, int newY) {
		super(newX, newY);
		// TODO Auto-generated constructor stub
	}
	
	public void setG(int g) {
		this.g = g;
	}
	public void setF(int f) {
		this.f = f;
	}
	public void CalF(){
		this.f = this.g + this.h;
	}
//
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return super.toString() + "parent--> " + parent;
//	}
	

}
