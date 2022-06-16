package rubikcube.cube;

import android.graphics.Canvas;

public class SwapCube {
	private boolean up_side_down = false; //false => everything is ok, true => up side down
	private int rotation = 0; //always in the range (0, 4).
	private final DrawCube drawer;

	public SwapCube(float size, float x, float y) {
		this.drawer = new DrawCube(size, false, true);
		this.drawer.setXY(x, y);
	}
	//public boolean getUpsideDown() { return this.up_side_down; }
	public void swapUpsideDown() {
		this.up_side_down = !this.up_side_down;
	}
	public void swapLeft() {
		this.rotation = (this.rotation + 1) % 4;
	}
	public void swapRight() {
		this.rotation = (4 + this.rotation - 1) % 4;
	}
	private Cube swapUpsideDown(Cube cube) {
		Cube my_draw = cube.clone();
		//front and left
		my_draw.ChangingFaces("Front", cube.getLeft().up_side_down());
		my_draw.ChangingFaces("Left", cube.getFront().up_side_down());
		//right and back
		my_draw.ChangingFaces("Right", cube.getBack().up_side_down());
		my_draw.ChangingFaces("Back", cube.getRight().up_side_down());
		//up and down
		my_draw.ChangingFaces("Up", cube.getDown().moveAndReturn(false));
		my_draw.ChangingFaces("Down", cube.getUp().onSave());
		return my_draw;
	}
	private Cube swapLeft(Cube cube) {
		Cube my_draw = cube.clone();
		//easy one
		my_draw.ChangingFaces("Front", cube.getRight().onSave());
		my_draw.ChangingFaces("Right", cube.getBack().onSave());
		my_draw.ChangingFaces("Back", cube.getLeft().onSave());
		my_draw.ChangingFaces("Left", cube.getFront().onSave());
		//rotation too
		my_draw.ChangingFaces("Up", cube.getUp().moveAndReturn(!up_side_down));
		my_draw.ChangingFaces("Down", cube.getDown().moveAndReturn(!up_side_down));
		return my_draw;
	}
	private Cube swapRight(Cube cube) {
		Cube my_draw = cube.clone();
		//easy one
		my_draw.ChangingFaces("Front", cube.getLeft().onSave());
		my_draw.ChangingFaces("Right", cube.getFront().onSave());
		my_draw.ChangingFaces("Back", cube.getRight().onSave());
		my_draw.ChangingFaces("Left", cube.getBack().onSave());
		//rotation too
		my_draw.ChangingFaces("Up", cube.getUp().moveAndReturn(up_side_down));
		my_draw.ChangingFaces("Down", cube.getDown().moveAndReturn(up_side_down));
		return my_draw;
	}
	public void draw(Canvas canvas, Cube cube) {
		Cube my_draw = cube.clone();
		//now i understood how to swap
		switch(rotation) {
			case 1:
				my_draw = swapLeft(cube);
				break;
			case 2:
				my_draw = swapLeft(swapLeft(cube));
				break;
			case 3:
				my_draw = swapRight(cube);
				break;
		}
		if(up_side_down) my_draw = swapUpsideDown(my_draw);
		//now i draw
		int[][] front = my_draw.getFront().getMatrix();
		int[][] right = my_draw.getRight().getMatrix();
		int[][] up    = my_draw.getUp().getMatrix();
		int[][] back  = my_draw.getBack().getMatrix();
		int[][] left  = my_draw.getLeft().getMatrix();
		int[][] down  = my_draw.getDown().getMatrix();
		this.drawer.draw(canvas, front, right, up, back, left, down);
	}
}
