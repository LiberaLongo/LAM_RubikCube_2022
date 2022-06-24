package rubik_cube.cube;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.Scanner;

public class Cube implements ICube {

	//const
	public static final int FACES_IN_A_CUBE = 6;

	//variables
	private int lastMove; //last move done by the user
	private final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;
	private final Face F, R, U, L, B, D;
	private DrawCube[] drawCubes;

	//colors
	int[] colors = null;

	//it gives 0 as colour of all cards of all faces...
	//the caller would probably onRestore after!
	public Cube() {
		this.F = new Face();
		this.R = new Face();
		this.U = new Face();
		this.B = new Face();
		this.L = new Face();
		this.D = new Face();
	}

	public Cube(int[] colors)
	{
		this.colors = colors;
		//Faces
		this.F = new Face(colors[0]);
		this.R = new Face(colors[1]);
		this.U = new Face(colors[2]);
		this.B = new Face(colors[3]);
		this.L = new Face(colors[4]);
		this.D = new Face(colors[5]);
		//Faces
		this.Reset();
	}

	//colors stuff
	public int[] get_colors() {
		return this.colors;
	}
	public int get_color_index(int index) {
		return this.colors[index];
	}
	public void set_colors(int[] colors) {
		this.colors = colors;
	}
	public void set_color_index(int index, int color) {
		this.colors[index] = color;
	}
	//CUBE STUFF

	//reset
	public Cube Reset()
	{
		this.lastMove = -1;
		this.F.Reset();
		this.R.Reset();
		this.U.Reset();
		this.B.Reset();
		this.L.Reset();
		this.D.Reset();
		return this;
	}

	public int getLastMove() {
		return this.lastMove;
	}
	public void setLastMove(int m) { this.lastMove = m; }
	//do a move
	public Cube MOVE(int m, boolean clockwise) {
		//i need to update the last done move (one return is available)
		this.lastMove = m;
		switch (m) {
			case 0: this.Front(clockwise); break;
			case 1: this.Right(clockwise); break;
			case 2: this.Up   (clockwise); break;
			case 3: this.Back (clockwise); break;
			case 4: this.Left (clockwise); break;
			case 5: this.Down (clockwise); break;
			default: break;
		}
		return this;
	}
	public Cube MOVE(int m) {
		//i understood if m is clockwise (0-5) or anticlockwise (6-11)
		boolean clockwise = (m < FACES_IN_A_CUBE);
		return this.MOVE(m % FACES_IN_A_CUBE, clockwise);
	}
	//randomize the cube
	public Cube Random(int n) {
		Random rand = new Random();
		for(int i = 0; i < n ; i++) {
			//generate one of the 12 possible moves
			int move = rand.nextInt();
			this.MOVE(move);
		}
		this.lastMove = -1;
		return this;
	}

	//implementation single moves

	//clockwise mean:
	//before: a, b, c, d.
	//temp = a; a = d; d = c; c = b; b = temp;
	//after: d, c, b, a.

	//AntiClockwise mean:
	//before: a, b, c, d.
	//temp = a; a = b; b = c; c = d; d = temp;
	//after: b, c, d, a.

	public void Front(boolean clockwise)
	{
		this.F.move(clockwise);
		//in C++ i needed: SOUTH, WEST, NORTH, EAST, a=U, b=R, c=D, d=L
		int[] temp = this.U.getSide(SOUTH);
		if(clockwise) {
			//a = d; d = c; c = b; b = temp;
			this.U.setSide(SOUTH, this.L.getSide(EAST));
			this.L.setSide(EAST, this.D.getSide(NORTH));
			this.D.setSide(NORTH, this.R.getSide(WEST));
			this.R.setSide(WEST, temp);
		} else {
			//a = b; b = c; c = d; d = temp;
			this.U.setSide(SOUTH, this.R.getSide(WEST));
			this.R.setSide(WEST, this.D.getSide(NORTH));
			this.D.setSide(NORTH, this.L.getSide(EAST));
			this.L.setSide(EAST, temp);
		}
	}
	public void Right(boolean clockwise)
	{
		R.move(clockwise);
		//EAST, EAST, EAST, WEST, a=U, b=B, c=D, d=F //!check it
		int[] temp = this.U.getSide(EAST);
		if(clockwise) {
			//a = d; d = c; c = b; b = temp;
			this.U.setSide(EAST, this.F.getSide(EAST));
			this.F.setSide(EAST, this.D.getSide(EAST));
			this.D.setSide(EAST, this.B.getSide(WEST));
			this.B.setSide(WEST, temp);
		} else {
			//a = b; b = c; c = d; d = temp;
			this.U.setSide(EAST, this.B.getSide(WEST));
			this.B.setSide(WEST, this.D.getSide(EAST));
			this.D.setSide(EAST, this.F.getSide(EAST));
			this.F.setSide(EAST, temp);
		}
	}
	public void Up(boolean clockwise)
	{
		U.move(clockwise);
		//NORTH, NORTH, NORTH, NORTH, a=B, b=R, c=F, d=L
		int[] temp = this.B.getSide(NORTH);
		if(clockwise) {
			//a = d; d = c; c = b; b = temp;
			this.B.setSide(NORTH, this.L.getSide(NORTH));
			this.L.setSide(NORTH, this.F.getSide(NORTH));
			this.F.setSide(NORTH, this.R.getSide(NORTH));
			this.R.setSide(NORTH, temp);
		} else {
			//a = b; b = c; c = d; d = temp;
			this.B.setSide(NORTH, this.R.getSide(NORTH));
			this.R.setSide(NORTH, this.F.getSide(NORTH));
			this.F.setSide(NORTH, this.L.getSide(NORTH));
			this.L.setSide(NORTH, temp);
		}
	}
	public void Back(boolean clockwise)
	{
		B.move(clockwise);
		//NORTH, WEST, SOUTH, EAST, a=U, b=L, c=D, d=R
		int[] temp = this.U.getSide(NORTH);
		if(clockwise) {
			//a = d; d = c; c = b; b = temp;
			this.U.setSide(NORTH, this.R.getSide(EAST));
			this.R.setSide(EAST, this.D.getSide(SOUTH));
			this.D.setSide(SOUTH, this.L.getSide(WEST));
			this.L.setSide(WEST, temp);
		} else {
			//a = b; b = c; c = d; d = temp;
			this.U.setSide(NORTH, this.L.getSide(WEST));
			this.L.setSide(WEST, this.D.getSide(SOUTH));
			this.D.setSide(SOUTH, this.R.getSide(EAST));
			this.R.setSide(EAST, temp);
		}
	}
	public void Left(boolean clockwise)
	{
		L.move(clockwise);
		//WEST, WEST, WEST, EAST, U, F, D, B
		int[] temp = this.U.getSide(WEST);
		if(clockwise) {
			//a = d; d = c; c = b; b = temp;
			this.U.setSide(WEST, this.B.getSide(EAST));
			this.B.setSide(EAST, this.D.getSide(WEST));
			this.D.setSide(WEST, this.F.getSide(WEST));
			this.F.setSide(WEST, temp);
		} else {
			//a = b; b = c; c = d; d = temp;
			this.U.setSide(WEST, this.F.getSide(WEST));
			this.F.setSide(WEST, this.D.getSide(WEST));
			this.D.setSide(WEST, this.B.getSide(EAST));
			this.B.setSide(EAST, temp);
		}
	}
	public void Down(boolean clockwise)
	{
		D.move(clockwise);
		//SOUTH, SOUTH, SOUTH, SOUTH, F, R, B, L
		int[] temp = this.F.getSide(SOUTH);
		if(clockwise) {
			//a = d; d = c; c = b; b = temp;
			this.F.setSide(SOUTH, this.L.getSide(SOUTH));
			this.L.setSide(SOUTH, this.B.getSide(SOUTH));
			this.B.setSide(SOUTH, this.R.getSide(SOUTH));
			this.R.setSide(SOUTH, temp);

		} else {
			//a = b; b = c; c = d; d = temp;
			this.F.setSide(SOUTH, this.R.getSide(SOUTH));
			this.R.setSide(SOUTH, this.B.getSide(SOUTH));
			this.B.setSide(SOUTH, this.L.getSide(SOUTH));
			this.L.setSide(SOUTH, temp);
		}
	}

	//getters without setters for the faces
	public Face getFront() { return this.F; }
	public Face getRight() { return this.R; }
	public Face getUp()    { return this.U; }
	public Face getBack()  { return this.B; }
	public Face getLeft()  { return this.L; }
	public Face getDown()  { return this.D; }

	public void ChangingFaces( String name, int[] saved) {
		switch (name) {
			case "Front": this.F.onRestore(saved); break;
			case "Right": this.R.onRestore(saved); break;
			case "Up"   : this.U.onRestore(saved); break;
			case "Back" : this.B.onRestore(saved); break;
			case "Left" : this.L.onRestore(saved); break;
			case "Down" : this.D.onRestore(saved); break;
			default: break;
		}
	}
	@NonNull
	public Cube clone() {
		Cube my_copy = new Cube();
		my_copy.onRestore(this.onSave());
		return my_copy;
	}

	//for let me DRAW
	public void draw(DrawCube drawCube, Canvas canvas) {
		int[][] front = this.F.getMatrix();
		int[][] right = this.R.getMatrix();
		int[][] up    = this.U.getMatrix();
		int[][] back  = this.B.getMatrix();
		int[][] left  = this.L.getMatrix();
		int[][] down  = this.D.getMatrix();
		drawCube.draw(canvas, front, right, up, back, left, down);
	}
	public void draw(Canvas canvas) {

		for (DrawCube drawCube : this.drawCubes) {
			this.draw(drawCube, canvas);
		}
	}
	//main can choice mine drawers ...
	public void setDrawers(DrawCube[] drawCubes) {
		this.drawCubes = drawCubes;
	}
	//... or ask me to use the default ones, otherwise no Drawers and i can't draw myself
	public void setDefaultDrawers(float size) {
		//creates the drawers
		DrawCube draw_2D_cross = new DrawCube(size, true);
		DrawCube draw_3D_FRU = new DrawCube(size*3/2, false, true);
		//DrawCube draw_3D_BLD = new DrawCube(size, false, false);
		DrawCube draw_3D_BLD_cut = new DrawCube(size, false, false, true);
		//choice the center of where draw the cube for his drawer.
		draw_2D_cross.setXY(0, 0);
		draw_3D_BLD_cut.setXY(225, 50);
		draw_3D_FRU.setXY(330, 70);
		//array of drawers
		DrawCube[] drawCubes = {draw_2D_cross, draw_3D_FRU, draw_3D_BLD_cut};

		//apply drawers to the CUBE
		this.setDrawers(drawCubes);
	}

	@NonNull
	public String toString() {
		//i'm a 3x3 cube but need also one position for other stuff
		String str = "CUBE :";
		str += "\nFRONT\n" + F.toString();
		str += "\nRIGHT\n" + R.toString();
		str += "\nUP   \n" + U.toString();
		str += "\nBACK \n" + B.toString();
		str += "\nLEFT \n" + L.toString();
		str += "\nDOWN \n" + D.toString();
		return "\n" + str;
	}
	public static int[][] read_from_file(String str) {
		//line i read
		String line;
		//set of names of faces i expect to read
		String[] names = {"FRONT", "RIGHT", "UP   ", "BACK ", "LEFT ", "DOWN "};
		//a vector to save the face
		int[] vector;
		//a matrix to save all the faces vectors.
		int[][] matrix = new int[FACES_IN_A_CUBE][9];
		//scanner to read
		Scanner scanner = new Scanner(str);
		//for some reason in the file there is one \n i don't understand why exactly...
		scanner.nextLine();
		//i want to read the "CUBE :" that is the first line
		if(scanner.hasNext()) {
			line = scanner.nextLine();
			if(line.equals("CUBE :")) System.out.println("cube ok");
			else System.out.println("cube NOT ok" + line + "end");
		}

		//for each face
		for (int index = 0; index < names.length; index ++) {
			//i want to read the name
			if (scanner.hasNextLine()) {
				line = scanner.nextLine();
				if (line.equals(names[index]))
					System.out.println("I read " + line);
				else
					System.out.println("\nSomething is wrong in reading cube");
			}
			//i want to read the vector for the face
			if (scanner.hasNextLine()) {
				line = scanner.nextLine();
				//System.out.print(line);
				vector = Face.read_from_string(line);
				matrix[index] = vector;
			}
		}
		return matrix;
	}
	/*public void print() { System.out.println(this); }*/

	//stuff for onSaveInstanceState, onRestoreInstanceState (and onCreate) of the caller
	public int[][] onSave() {
		int[][] savedCube = new int[FACES_IN_A_CUBE][9]; //6 faces with 9 cards each
		savedCube[0] = this.F.onSave();
		savedCube[1] = this.R.onSave();
		savedCube[2] = this.U.onSave();
		savedCube[3] = this.B.onSave();
		savedCube[4] = this.L.onSave();
		savedCube[5] = this.D.onSave();
		return savedCube;
	}
	public void onRestore(int[][] matrix) {
		int[] my_colors = new int[FACES_IN_A_CUBE];
		my_colors[0] = this.F.onRestore(matrix[0]);
		my_colors[1] = this.R.onRestore(matrix[1]);
		my_colors[2] = this.U.onRestore(matrix[2]);
		my_colors[3] = this.B.onRestore(matrix[3]);
		my_colors[4] = this.L.onRestore(matrix[4]);
		my_colors[5] = this.D.onRestore(matrix[5]);
		this.colors = my_colors;
	}
}
