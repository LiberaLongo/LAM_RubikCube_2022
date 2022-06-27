package rubik_cube.cube;

import android.graphics.Canvas;

public interface ICube {
	//reset
	Cube Reset();

	//do n times MOVE(m) with m in range(0, 12).
	Cube Random(int n);

	//choice which move depending on a integer in range(0, 12).
	Cube MOVE(int m);

	//get the last move done.
	int getLastMove();

	//moves
	void Front(boolean clockwise);
	void Right(boolean clockwise);
	void Up   (boolean clockwise);
	void Back (boolean clockwise);
	void Left (boolean clockwise);
	void Down (boolean clockwise);

	//for draw
	void setDrawers(DrawCube[] drawCubes);
	void setDefaultDrawers(float size);
	void draw(Canvas canvas);

	//for save
	int[][] onSave();
	int[] onRestore(int[][] matrix);
}
