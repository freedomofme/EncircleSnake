package android.basic.lesson48;

// 定义坐标类
public class Coordinate implements Comparable<Coordinate>{

		public int x;
		public int y;
		public int h;
		// 构造函数
		public Coordinate(int newX, int newY) {
			x = newX;
			y = newY;
		}

		// 重写equals
		public boolean equals(Coordinate other) {
			if (x == other.x && y == other.y) {
				return true;
			}
			return false;
		}
		
		

		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			
			if(this == o) return true;
			if(o == null) return false;
			if(! (o instanceof Coordinate)) return false;
			Coordinate that = (Coordinate) o;
			if(this.x != that.x) return false;
			if(this.y != that.y) return false;
			return true;
		}


		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			int hash = 7;
			hash = hash * 31 + this.x;
			hash = hash * 31 + this.y;
			return hash;
			
		}

		// 重写toString
		@Override
		public String toString() {
			return "Coordinate: [" + x + "," + y + "," + h + "]";
		}
		public void setH(int h) {
			this.h = h;
		}
		

		@Override
		public int compareTo(Coordinate another) {
			// TODO Auto-generated method stub
			return this.h - another.h;
		}
	}