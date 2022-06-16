package rubikcube.cube;

import androidx.annotation.NonNull;

import java.util.Scanner;

public class Face {	//NOTA BENE: java do not use pointers so i have to returns always an array if i want to change something.

    private final int DIM = 4; //cube of Rubik 3x3
    int colour; //fixed colour of central card
	private int[] vertices = new int[DIM]; //N W S E
	private int[] edges = new int[DIM]; //NW NE SE SW

	public Face() {
		this.colour = 0;
		this.Reset();
	}

	public Face(int colour) {
		this.colour = colour;
		this.Reset();
	}

	public void Reset()	{
		//every card get the colour again
		for (int i = 0; i < DIM; i++) {
			this.edges[i] = this.colour;
			this.vertices[i] = this.colour;
		}
	}
	public int[] rotateClockwise(int[] input) {
		return new int[]{input[3], input[0], input[1], input[2]};
	}
	public int[] rotateAntiClockwise(int[] input) {
		return new int[]{input[1], input[2], input[3], input[0]};
	}
	public void move(boolean clockwise) {
		//System.out.println(this.name);
		if(clockwise) {
			this.edges = rotateClockwise(this.edges);
			this.vertices = rotateClockwise(this.vertices);
		} else {
			this.edges = rotateAntiClockwise(this.edges);
			this.vertices = rotateAntiClockwise(this.vertices);
		}
	}
	public int[] moveAndReturn(boolean clockwise) {
		//initialize new arrays
		int[] output = new int[DIM + DIM + 1];
		int[] vertices;
		int[] edges;
		//check boolean and do the rotation
		if (clockwise) {
			vertices = rotateClockwise(this.vertices);
			edges = rotateClockwise(this.edges);
		} else{
			vertices = rotateAntiClockwise(this.vertices);
			edges = rotateAntiClockwise(this.edges);
		}
		//populate the output array
		for (int i = 0; i < DIM * 2; i += 2) {
			output[i] = vertices[i / 2];
			output[i + 1] = edges[i / 2];
		}
		output[8] = this.colour;
		//and return it
		return output;
	}
	public int[] up_side_down() {
		return new int[] {
			vertices[2], edges[2], vertices[3], edges[3], //0, 1, 2, 3
			vertices[0], edges[0], vertices[1], edges[1], //4, 5, 6, 7
			colour
		};
	}

	//the caller need to know 0 north, 1 east, 2 south, 3 west
    public int[] getSide(int n) {
		int[] side = new int[DIM-1]; //i am using a 3x3 Cube then i have a vector length 3
		side[0] = this.vertices[n % 4];
		side[1] = this.edges   [n % 4];
		side[2] = this.vertices[(n + 1) % 4];
		return side;
	}

	//the caller need to know 0 north, 1 east, 2 south, 3 west
	public void setSide(int n, int[] input) {
		this.vertices[n % 4] = input[0];
		this.edges[n % 4] = input[1];
		this.vertices[(n+1)%4] = input[2];
	}

    public int[][] getMatrix() {
		return new int[][] {
				{vertices[0], edges[0], vertices[1]},
				{edges[3], colour, edges[1]},
				{vertices[3], edges[2], vertices[2]}
		};
	}

    @NonNull
	public String toString() {
		int[] vector = this.onSave();
        StringBuilder str = new StringBuilder("[ ");
        for(int index = 0 ; index < 3*3 ; index ++) {
        		str.append(vector[index]).append(" ");
        }
        str.append("]");
        return str.toString();
    }
    public static int[] read_from_string(String str) {
		int[] vector = new int[3*3];
		Scanner scanner = new Scanner(str);
		//String brace =
				scanner.next(); //i have to find the [ after the name
		//if(brace.equals("[")) System.out.println("yes");
		//now i should scan the array of integers
		if(str != null) {
			for(int index = 0; index < 3*3; index ++) {
				int readingInt = scanner.nextInt();
				//System.out.print("  I read " + readingInt);
				vector[index] = readingInt;
			}
		}
		return vector;
	}

	//stuff for onSaveInstanceState, onRestoreInstanceState (and onCreate) of the caller
	public int[] onSave() {
		int[] vector = new int[DIM+DIM+1];
		for(int i = 0; i < DIM*2; i+=2) {
			vector[i] = this.vertices[i/2];
			vector[i+1] = this.edges[i/2];
		}
		vector[8] = this.colour;
		return vector;
	}
	public int onRestore(int[] vector) {
		for(int i = 0; i < DIM*2; i+=2) {
			this.vertices[i/2] = vector[i] ;
			this.edges[i/2] = vector[i+1] ;
		}
		this.colour = vector[8];
		return this.colour;
	}

}
