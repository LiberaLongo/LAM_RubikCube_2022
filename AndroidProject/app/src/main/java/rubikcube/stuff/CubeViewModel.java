package rubikcube.stuff;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.IOError;
import java.util.Objects;

import rubikcube.cube.Cube;
import rubikcube.cube.SwapCube;

public class CubeViewModel extends ViewModel {

	//constants
	private final float SIZE = 15;

	//mutable live data
	private MutableLiveData<Cube> mld_cube;

	//stuff always recreated
	private boolean clockwise;
	private SwapCube swapper;
	private final Queue movesQueue;
	private int[] default_colors;

	//builder (only Android may call it when it want)
	public CubeViewModel() {
		this.mld_cube = null;
		this.default_colors = null;
		this.clockwise = true;
		this.swapper = new SwapCube(50, 200, 200);
		this.movesQueue = new Queue(10);
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
		mld_cube.setValue(getCube().MOVE(move, this.clockwise));
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

	//the queue
	public void add_move_in_queue(String str) {
		this.movesQueue.enqueue(str);
	}
	public String getQueueText(String default_text) {
		if(this.movesQueue.isEmpty()) return default_text;
		return this.movesQueue.toString();
	}


	//MY CHANGEABLE COLOURS
	//color array get and set
	public int[] get_Cube_Colors() {
		int[] result = this.getCube().get_colors();
		if(result == null)
			return this.default_colors;
		return result;
	}
	public void set_Default_Colors(int[] default_colors) {
		this.default_colors = default_colors;
		Cube cube = this.getCube();
		cube.set_colors(default_colors);
		this.mld_cube.setValue(cube);
	}

	//color index set
	public void setCubeColorIndex(int index, int color) {
		Cube cube = this.getCube();
		cube.set_color_index(index, color);
		this.mld_cube.setValue(cube);
	}


	//FILES MANAGING

	//https://developer.android.com/topic/libraries/architecture/viewmodel#implement
	public void LOAD_CUBE(Context context, String filename) {
		this.movesQueue.clear();
		// Do an asynchronous operation to fetch cube.
		try {
			String str = myFilesManager.READ(filename, context);
			int[][] matrix;
			if(!str.equals("")) {
				matrix = Cube.read_from_file(str);
				Cube cube = new Cube();
				cube.onRestore(matrix);
				cube.setDefaultDrawers(SIZE);
				cube.setLastMove(-1);
				if(mld_cube == null)
					mld_cube = new MutableLiveData<>();
				this.mld_cube.setValue(cube);
			}
		} catch(IOError e) {
			Log.e("Exception", "ERROR LOADING CUBE FROM FILE: " + e);
		}
	}

	public void SAVE_CUBE(Context context, String filename) {
		this.movesQueue.clear();
		//set last move to -1 so i can show "Save" and after updateUI() function.
		Cube cube = getCube();
		cube.setLastMove(-1);
		myFilesManager.WRITE(filename, context, cube.toString());
		mld_cube.setValue(cube);
	}

	public void setSwapCube_SizeXY(float size, float x, float y) {
		this.swapper = new SwapCube(size, x, y);
	}
}