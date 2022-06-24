package rubik_cube.cube;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Square{

	public int color;
	public float side, x, y;

	Square(int color, float side, float x, float y) {
		this.color = color;
		this.side = side;
		this.x = x;
		this.y = y;
	}
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(x, y, x+side, y+side, paint);
	}
}
