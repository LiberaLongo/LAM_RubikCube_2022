package rubik_cube.cube;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.ImageView;

public abstract class Drawer {

	/**
	 * Draw all cube and swappable
	 * @param view where to draw?
	 * @param cube what is the fixed position of the cube?
	 * @param mySwapperCube what i can swap to see the cube differently
	 * @param fix_draw i have to draw the cube? yes -> true, no -> false
	 */
	public static void draw(ImageView view, Cube cube, SwapCube mySwapperCube, boolean fix_draw, Configuration conf) {

		//Bitmap preparation
		Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		//instantiate Canvas based on the Bitmap
		Canvas canvas = new Canvas(bitmap);

		//assignment of the background color black to the Drawable
		int currentNightMode = conf.uiMode & Configuration.UI_MODE_NIGHT_MASK;
		switch(currentNightMode) {
			case Configuration.UI_MODE_NIGHT_NO:
				//Day mode (night not active now on device)
				//if day (or user choice day) i want white
				canvas.drawColor(Color.WHITE);
				break;
			case Configuration.UI_MODE_NIGHT_YES:
				//Night mode
				//if dark/night (or user choice dark) i want black
				canvas.drawColor(Color.BLACK);
				break;
			default:
				break;
		}
		//draw my stuff i want to draw in the Canvas
		if(fix_draw)
			cube.draw(canvas);
		mySwapperCube.draw(canvas, cube);

		//and after this set the bitmap to the view
		view.setImageBitmap(bitmap);
	}
}
