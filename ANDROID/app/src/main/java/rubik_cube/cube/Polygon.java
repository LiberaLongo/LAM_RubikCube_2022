package rubik_cube.cube;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Polygon {

	Polygon() {}

	/**
	 * Rhombus builder
	 *
	 * @param canvas The Canvas i want to draw into my Polygon
	 * @param color  Integer representing a fill color (see http://developer.android.com/reference/android/graphics/Color.html)
	 * @param points the points of my polygon
	 * NOTE: i can't have points in the constructor because maybe u want to extend this class with
	 * custom shape and u want choice points coordinates in ur builder
	 * and super() must be the FIRST CALL of ur builder!
	 * so the builder(Point[] points) is probably never used when u want ONLY A CUSTOM polygon.
	 * - PLACE this up this comment if u want a POLYGON object (and no subclasses).
	 * //builder(Point[] points) {this.points = points}
	 */
	public void draw(Canvas canvas, int color, Point[] points) {

		if(points.length < 3) return; //if not enough points i can't draw

		// paint
		Paint polyPaint = new Paint();
		polyPaint.setColor(color);
		polyPaint.setStyle(Paint.Style.FILL);

		// path
		Path polyPath = new Path();
		polyPath.moveTo(points[0].x, points[0].y);
		int i, len = points.length; //len = 4
		for (i = 0; i < len; i++) {
			polyPath.lineTo(points[i].x, points[i].y);
		}
		polyPath.close();

		// draw
		canvas.drawPath(polyPath, polyPaint);
	}
}
