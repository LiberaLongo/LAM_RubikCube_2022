package rubikcube.cube;

import android.graphics.Canvas;

public class Rhombus extends Polygon{

	public int color;
	public int angle;
	public Point[] points;

	/**
	 * Rhombus builder
	 *
	 * @param color  Integer representing a fill color (see http://developer.android.com/reference/android/graphics/Color.html)
	 * @param side   Is the side of the Rhombus
	 * @param angle  Angle of rotation
	 * @param x      A particular point of the rhombus
	 * @param y      A particular point of the rhombus
	 * //I want use (x, y) as it is the center of the Cube that uses various rhombuses
	 * //that mean that from angle i calculate the point of translations for the center of the rhombus.
	 */
	Rhombus(int color, float side, int angle, float x, float y) {
		super();
		this.angle = angle;
		this.color = color;

		//in order to generate my points i need to call the super after
		Point[] points = new Point[4];

		//rotation angles
		final int FRONT = 240, RIGHT = 120, UP = 0;
		final int BACK = 60, LEFT = 300, DOWN = 180;

		float center_x = x, center_y = y;

		//I need the height of the right triangle having
		//the side parameter as hypotenuse and the side/2 parameter as a base
		float h = side / 2;
		float w = h * (float) Math.sqrt(3) ;
		//now i want to get the center from the (x, y) specified according to the angle.
		switch (angle) {
			case UP: center_y = y - h; break;
			case DOWN: center_y = y + h; break;
			case FRONT: h = h/2; w = w/2; center_x = x - w; center_y = y + h; break;
			case BACK: h = h/2; w = w/2; center_x = x - w; center_y = y - h; break;
			case RIGHT: h = h/2; w = w/2; center_x = x + w; center_y = y + h; break;
			case LEFT: h = h/2; w = w/2; center_x = x + w; center_y = y - h; break;
			default:
				break;
		}
		//now thx to the "angle" i wanna drive 3 types of rhombus ... (see the ASCII arts)
		if(angle == UP || angle == DOWN) {
			points[0] = new Point( center_x , center_y - h ); //A   /\A
			points[1] = new Point( center_x - w , center_y ); //B B/  \
			points[2] = new Point( center_x , center_y + h ); //C  \  /D
			points[3] = new Point( center_x + w , center_y ); //D  C\/ up
		}
		else if(angle == FRONT || angle == LEFT) {
			points[0] = new Point(center_x - w, center_y - h * 3); //A B|\
			points[1] = new Point(center_x + w, center_y - h * 1); //B  | \C front
			points[2] = new Point(center_x + w, center_y + h * 3); //C A\ | (left)
			points[3] = new Point(center_x - w, center_y + h * 1); //D   \|D
		}
		else if(angle == BACK || angle == RIGHT) {
			points[0] = new Point( center_x + w, center_y - h*3 ); //A    /|B
			points[1] = new Point( center_x - w, center_y - h*1 ); //B C / | right
			points[2] = new Point( center_x - w, center_y + h*3 ); //C   | / A (back)
			points[3] = new Point( center_x + w, center_y + h*1 ); //D  D|/
		} else {
			System.out.println("ANGLE ERROR!");
		}
		this.points = points;
	}
	public void draw(Canvas canvas) {
		this.draw(canvas, this.color, this.points);
	}
}
