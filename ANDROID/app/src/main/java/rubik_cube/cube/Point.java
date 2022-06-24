package rubik_cube.cube;

import androidx.annotation.NonNull;

//simple points
class Point {

	public float x;
	public float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@NonNull
	public String toString() {
		return "("+this.x+", "+this.y+")";
	}
}
