package rubikcube.cube;

import android.graphics.Canvas;

/* auxiliary class used for draw the rubik cube
 * it separate the Cube moves functions to their draw stuffs
 * so the Cube will pass a vector for each of them faces
 * and call the draw of a DrawCube*/
public class DrawCube {
	//this is needed so if i want to test a special Draw with set parameter
	//cube do not put up the draw of a untested stuff if it is in a upper position of DrawCube arrays
	private boolean doDraw = true; //i tested it? yes -> doDraw = true, else false
	private float x = 200, y = 200;
	//coordinates
	private final float cardSide;
	//stuff for understood what to draw exactly
	private final boolean _2d; //draw: true => cross, false => 3d
	private boolean up = false; //draw: true => FRU, false => BLD, default is false.
	private boolean cut = false; //draw: true => u see inside, false => u see outside, default is false.

	public DrawCube(float cardSide, boolean _2d) {
		this.cardSide = cardSide;
		this._2d = _2d;
	}

	public DrawCube(float cardSide, boolean _2d, boolean up) {
		this.cardSide = cardSide;
		this._2d = _2d;
		this.up = up;
	}
	public DrawCube(float cardSide, boolean _2d, boolean up, boolean cut) {
		this.cardSide = cardSide;
		this._2d = _2d;
		this.up = up;
		this.cut = cut;
	}
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setDoDraw(boolean doDraw) {
		this.doDraw = doDraw;
	}

	//2D
	private void drawN(Canvas canvas, float size, float x, float y, int[][] colour) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
			{
				float xj = x + j * size;
				float yi = y + i * size;
				Square square = new Square(colour[i][j], size, xj, yi);
				square.draw(canvas);
			}
		}
	}
	private void _2D(Canvas canvas, int[][] front, int[][] right, int[][] up, int[][] back, int[][] left, int[][] down) {
		float side = this.cardSide * 3;
		this.drawN(canvas, this.cardSide, x + 1 * side, y + 1 * side, front);
		this.drawN(canvas, this.cardSide, x + 2 * side, y + 1 * side, right);
		this.drawN(canvas, this.cardSide, x + 1 * side, y + 0 * side, up);
		this.drawN(canvas, this.cardSide, x + 3 * side, y + 1 * side, back);
		this.drawN(canvas, this.cardSide, x + 0 * side, y + 1 * side, left);
		this.drawN(canvas, this.cardSide, x + 1 * side, y + 2 * side, down);
	}

	//FRU
	private void FRU(Canvas canvas, int[][] front, int[][] right, int[][] up) {
		//arrays
		int DIM = 9;
		Rhombus[] rhombusesFront = new Rhombus[DIM];
		Rhombus[] rhombusesRight = new Rhombus[DIM];
		Rhombus[] rhombusesUp       = new Rhombus[DIM];

		//rotation angles
		final int FRONT = 240, RIGHT = 120, UP = 0;
		//length of something
		final float size = this.cardSide, high =  size * (float) Math.sqrt(3) / 2;
		//FRU
		//FRONT
		rhombusesFront[0] = new Rhombus( front[0][0], size, FRONT, x - high * 2, y - size);
		rhombusesFront[1] = new Rhombus( front[0][1], size, FRONT, x - high, y - size / 2);
		rhombusesFront[2] = new Rhombus( front[0][2], size, FRONT, x, y);
		rhombusesFront[3] = new Rhombus( front[1][0], size, FRONT, x - high * 2, y);
		rhombusesFront[4] = new Rhombus( front[1][1], size, FRONT, x - high, y + size / 2);
		rhombusesFront[5] = new Rhombus( front[1][2], size, FRONT, x, y + size);
		rhombusesFront[6] = new Rhombus( front[2][0], size, FRONT, x - high * 2, y + size);
		rhombusesFront[7] = new Rhombus( front[2][1], size, FRONT, x - high, y + size * 3 / 2);
		rhombusesFront[8] = new Rhombus( front[2][2], size, FRONT, x, y + size * 2);
		//RIGHT
		rhombusesRight[0] = new Rhombus( right[0][0], size, RIGHT, x, y);
		rhombusesRight[1] = new Rhombus( right[0][1], size, RIGHT, x + high, y - size / 2);
		rhombusesRight[2] = new Rhombus( right[0][2], size, RIGHT, x + high * 2, y - size);
		rhombusesRight[3] = new Rhombus( right[1][0], size, RIGHT, x, y + size);
		rhombusesRight[4] = new Rhombus( right[1][1], size, RIGHT, x + high, y + size / 2);
		rhombusesRight[5] = new Rhombus( right[1][2], size, RIGHT, x + high * 2, y);
		rhombusesRight[6] = new Rhombus( right[2][0], size, RIGHT, x, y + size * 2);
		rhombusesRight[7] = new Rhombus( right[2][1], size, RIGHT, x + high, y + size * 3 / 2);
		rhombusesRight[8] = new Rhombus( right[2][2], size, RIGHT, x + high * 2, y + size);
		//UP
		rhombusesUp   [0] = new Rhombus( up[0][0], size, UP, x, y - size * 2);
		rhombusesUp   [1] = new Rhombus( up[0][1], size, UP, x + high, y - size * 3 / 2);
		rhombusesUp   [2] = new Rhombus( up[0][2], size, UP, x + high * 2, y - size);
		rhombusesUp   [3] = new Rhombus( up[1][0], size, UP, x - high, y - size * 3 / 2);
		rhombusesUp   [4] = new Rhombus( up[1][1], size, UP, x, y - size);
		rhombusesUp   [5] = new Rhombus( up[1][2], size, UP, x + high, y - size / 2);
		rhombusesUp   [6] = new Rhombus( up[2][0], size, UP, x - high * 2, y - size);
		rhombusesUp   [7] = new Rhombus( up[2][1], size, UP, x - high, y - size / 2);
		rhombusesUp   [8] = new Rhombus( up[2][2], size, UP, x, y);
		//draw
		for (int i = 0; i < DIM; i++) {
			rhombusesFront[i].draw(canvas);
			rhombusesRight[i].draw(canvas);
			rhombusesUp   [i].draw(canvas);
		}
	}

	//BLD
	private void BLD(Canvas canvas, int[][] back, int[][] left, int[][] down) {
		int DIM = 9;
		Rhombus[] rhombusesBack = new Rhombus[DIM];
		Rhombus[] rhombusesLeft = new Rhombus[DIM];
		Rhombus[] rhombusesDown = new Rhombus[DIM];

		//rotation angles
		final int BACK = 60, LEFT = 300, DOWN = 180;
		//length of something
		final float size = this.cardSide, high = size * (float) Math.sqrt(3) / 2;
		//BLD
		//BACK
		rhombusesBack[0] = new Rhombus( back[0][0], size, BACK, x - high * 2, y - size);
		rhombusesBack[1] = new Rhombus( back[0][1], size, BACK, x - high, y - size * 3 / 2);
		rhombusesBack[2] = new Rhombus( back[0][2], size, BACK, x, y - size * 2);
		rhombusesBack[3] = new Rhombus( back[1][0], size, BACK, x - high * 2, y);
		rhombusesBack[4] = new Rhombus( back[1][1], size, BACK, x - high, y - size / 2);
		rhombusesBack[5] = new Rhombus( back[1][2], size, BACK, x, y - size);
		rhombusesBack[6] = new Rhombus( back[2][0], size, BACK, x - high * 2, y + size);
		rhombusesBack[7] = new Rhombus( back[2][1], size, BACK, x - high, y + size / 2);
		rhombusesBack[8] = new Rhombus( back[2][2], size, BACK, x, y);
		//LEFT
		rhombusesLeft[0] = new Rhombus( left[0][0], size, LEFT, x, y - size * 2);
		rhombusesLeft[1] = new Rhombus( left[0][1], size, LEFT, x + high, y - size * 3 / 2);
		rhombusesLeft[2] = new Rhombus( left[0][2], size, LEFT, x + high * 2, y - size);
		rhombusesLeft[3] = new Rhombus( left[1][0], size, LEFT, x, y - size);
		rhombusesLeft[4] = new Rhombus( left[1][1], size, LEFT, x + high, y - size / 2);
		rhombusesLeft[5] = new Rhombus( left[1][2], size, LEFT, x + high * 2, y);
		rhombusesLeft[6] = new Rhombus( left[2][0], size, LEFT, x, y);
		rhombusesLeft[7] = new Rhombus( left[2][1], size, LEFT, x + high, y + size / 2);
		rhombusesLeft[8] = new Rhombus( left[2][2], size, LEFT, x + high * 2, y + size);
		//DOWN
		rhombusesDown[0] = new Rhombus( down[0][0], size, DOWN, x + high * 2, y + size);
		rhombusesDown[1] = new Rhombus( down[0][1], size, DOWN, x + high, y + size * 3 / 2);
		rhombusesDown[2] = new Rhombus( down[0][2], size, DOWN, x, y + size * 2);
		rhombusesDown[3] = new Rhombus( down[1][0], size, DOWN, x + high, y + size / 2);
		rhombusesDown[4] = new Rhombus( down[1][1], size, DOWN, x, y + size);
		rhombusesDown[5] = new Rhombus( down[1][2], size, DOWN, x - high, y + size * 3 / 2);
		rhombusesDown[6] = new Rhombus( down[2][0], size, DOWN, x, y);
		rhombusesDown[7] = new Rhombus( down[2][1], size, DOWN, x - high, y + size / 2);
		rhombusesDown[8] = new Rhombus( down[2][2], size, DOWN, x - high * 2, y + size);
		//draw
		for (int i = 0; i < DIM; i++) {
			rhombusesBack[i].draw(canvas);
			rhombusesLeft[i].draw(canvas);
			rhombusesDown[i].draw(canvas);
		}
	}
	//BLD_CUT
	/*it switch the columns of the matrix 3x3 so:
	* input : [[0, 1, 2]  =>  output: [[2, 1, 0]
	*          [3, 4, 5]               [5, 4, 3]
	*          [6, 7, 8]]              [8, 7, 6]]
	* and i have no idea about there is a particular math matrix that do it so i call it FUNNY!*/
	private int[][] funnyMatrix(int[][] matrix) {
		//if(matrix.length != 3 || matrix[0].length != 3) return null;
		return new int[][]{
			{matrix[0][2], matrix[0][1], matrix[0][0]},
			{matrix[1][2], matrix[1][1], matrix[1][0]},
			{matrix[2][2], matrix[2][1], matrix[2][0]}
		};
	}
	//it let me draw the down face cut symmetric respect to the x? probably...
	private int[][] downyMatrix(int[][] matrix) {
		//if(matrix.length != 3 || matrix[0].length != 3) return null;
		return new int[][]{
			{matrix[2][2], matrix[1][2], matrix[0][2]},
			{matrix[2][1], matrix[1][1], matrix[0][1]},
			{matrix[2][0], matrix[1][0], matrix[0][0]},
		};
	}
	private void BLD_cut(Canvas canvas, int[][] back, int[][] left, int[][] down) {
		//i do "this.BLD.(canvas, back, left, down)" to get the NOT CUT one ... and :
		this.BLD(canvas, funnyMatrix(left), funnyMatrix(back), downyMatrix(down)); //to get the CUT one i NEED.
	}

	// look on internet:
	// https://www.devapp.it/wordpress/android-disegnare-con-i-canvas/

	public void draw(Canvas canvas, int[][] front, int[][] right, int[][] up, int[][] back, int[][] left, int[][] down) {
		if (this.doDraw) {//my draws
			if (this._2d) {
				this._2D(canvas, front, right, up, back, left, down);
			} else if (this.up) {
				//FRU
				this.FRU(canvas, front, right, up);
			} else {//if up = false
				if (this.cut) {
					this.BLD_cut(canvas, back, left, down);
				} else {
					this.BLD(canvas, back, left, down);
				}
			}
		}
	}
}
