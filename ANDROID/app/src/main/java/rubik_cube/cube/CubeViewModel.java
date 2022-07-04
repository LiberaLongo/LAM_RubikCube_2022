package rubik_cube.cube;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.IOError;
import java.util.Objects;

//https://developer.android.com/topic/libraries/architecture/viewmodel#implement

public class CubeViewModel extends ViewModel {

	//constants
	private final float SIZE = 15;

	//mutable live data
	private MutableLiveData<Cube> mld_cube;

	//stuff always recreated
	private boolean clockwise;
	private SwapCube swapper;
	private String[] letters; //letters of the moves, stored in the queue
	private final Queue movesQueue;

	//builder (only Android may call it when it want)
	public CubeViewModel() {
		this.letters = null;
		this.mld_cube = null;
		this.clockwise = true;
		this.swapper = new SwapCube(50, 200, 200);
		this.movesQueue = new Queue(26);
	}

	//OBSERVABLE STUFF

	//the cube
	public void observeCube(@NonNull LifecycleOwner owner,
							@NonNull Observer<Cube> observer) {
		getLiveDataCube().observe(owner, observer);
	}
	public LiveData<Cube> getLiveDataCube() {
		//if it return null i probably need to call the createCube method with
		//colors i can't get from the View Model!
		return mld_cube;
	}
	public Cube getCube() {
		return Objects.requireNonNull(mld_cube.getValue());
	}


	//CUBE STUFF

	/**
	 * @param color array of colour you want i create the cube
	 * @param refresh if i have to check if mld_cube is null or i have to re-create anyway
	 */
	public void createCube(int[] color, boolean refresh) {
		if(mld_cube == null || refresh) {
			mld_cube = new MutableLiveData<>();
			//if(color.length == 6) idk if i have to check this...
			Cube cube = new Cube(color);
			cube.setLastMove(-1);
			cube.setDefaultDrawers(SIZE);
			mld_cube.setValue(cube);
		}
		//return mld_cube;
	}
	//stuff i want the cube can do
	public void Reset() {
		mld_cube.setValue(getCube().Reset());
		this.movesQueue.clear();
	}
	public void Random() {
		mld_cube.setValue(getCube().Random(10));
		this.movesQueue.clear();
	}
	public void MOVE(int move) {
		//update the queue
		String text = "";
		if (this.clockwise) text += " ";
		else text += "'";
		this.add_move_in_queue(this.getLetter(move) + text);
		//update the cube and notify observers
		mld_cube.setValue(getCube().MOVE(move, this.clockwise));
		//NOTE: if i update the cube first than the queue
		// observers can't let the user seen the last move in the "history"
	}
	//clockwise
	public boolean isClockwise() {
		return this.clockwise;
	}
	public void changeClockwise() {
		this.clockwise = ! this.clockwise;
	}
	//the swapper
	public void swapUpsideDown() {
		this.swapper.swapUpsideDown();
	}
	public void swapLeft() {
		this.swapper.swapLeft();
	}
	public void swapRight() {
		this.swapper.swapRight();
	}
	public SwapCube getSwapper() {
		return this.swapper;
	}
	//the letters
	private String getLetter(int index) {
		String letter;
		if(this.letters == null) //in case i haven't set them
			//i choice the english letters "hardcoding" them.
			this.letters = new String[] {"F", "R", "U", "B", "L", "D"};
		letter = letters[index];
		return letter;
	}

	/**
	 * String[] letters = getResources().getStringArray(R.array.faceLetter_array);
	 * @param letters array of string in order Front, Right, Up, Back, Left, Down.
	 */
	public void setLetters(String[] letters) {
		this.letters = letters;
	}
	//the queue
	private void add_move_in_queue(String str) {
		this.movesQueue.enqueue(str);
	}
	public String getQueueText(String default_text) {
		if(this.movesQueue.isEmpty()) return default_text;
		return this.movesQueue.toString();
	}

	//FILES MANAGING

	/** load a cube from the string of the cube
	 * @return array of colors of center of the cube
	 */
	public int[] LOAD_CUBE(Context context, String filename) {
		int[] colors = null;
		this.movesQueue.clear();
		// Do an asynchronous operation to fetch cube.
		try {
			int[][] matrix;
			String str = myFilesManager.READ(filename, context);
			if(str != null) {
				matrix = Cube.read_from_file(str);
				Cube cube = new Cube();
				colors = cube.onRestore(matrix);
				cube.setDefaultDrawers(SIZE);
				cube.setLastMove(-1);
				if(mld_cube == null)
					mld_cube = new MutableLiveData<>();
				this.mld_cube.setValue(cube);
			}
		} catch(IOError e) {
			Log.e("Exception", "ERROR LOADING CUBE FROM FILE: " + e);
		}
		return colors;
	}

	/**
	 * @param context context of the file
	 * @param filename where i have to write
	 * @param string_cube null if
	 */
	public void SAVE_CUBE(Context context, String filename, String string_cube) {
		this.movesQueue.clear();
		//if i am sure i saved the cube from a cube, and not a "received message"
		if(string_cube == null) {
			Cube cube = getCube();
			string_cube = cube.toString();
			//set last move to -1 so i can show "Save" and after updateUI() function.
			cube.setLastMove(-1);
			mld_cube.setValue(cube);
		} else {
			String msg = "Warning! i'm not sure i 'received' a cube, the app may crash during load";
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
		//write the to_string into file
		myFilesManager.WRITE(filename, context, string_cube);
	}

	public void setSwapCube_SizeXY(float size, float x, float y) {
		this.swapper = new SwapCube(size, x, y);
	}
}
