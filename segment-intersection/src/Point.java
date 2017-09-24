import java.util.Objects;

public class Point {

	public final Integer x;
	public final Integer y;

	public Point(Integer left, Integer right) {
		this.x = left;
		this.y = right;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Point)) {
			return false;
		}
		Point other_ = (Point) other;
		return Objects.equals(other_.x, this.x) && Objects.equals(other_.y, this.y);
	}

	public boolean lessThan(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Point)) {
			return false;
		}
		Point other_ = (Point) other;
		if (this.x < other_.x && this.y < other_.y) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 13;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}
}