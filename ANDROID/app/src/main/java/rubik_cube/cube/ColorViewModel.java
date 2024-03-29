package rubik_cube.cube;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class ColorViewModel extends ViewModel {

	//mutable live data
	private final MutableLiveData<int[]> mld_colors;

	private int[] default_colors;

	public ColorViewModel() {
		this.mld_colors = new MutableLiveData<>();
		this.default_colors = null;
	}

	//OBSERVABLE STUFF

	//the cube
	public void observeColors(@NonNull LifecycleOwner owner,
							@NonNull Observer<int[]> observer) {
		getLiveDataColors().observe(owner, observer);
	}
	public LiveData<int[]> getLiveDataColors() {
		//if it return null i probably need to call the createCube method with
		//colors i can't get from the View Model!
		return mld_colors;
	}
	public int[] getColors() {
		return mld_colors.getValue();
	}
	public int get_color_index(int index) {
		if(index >= 0 && index < 6) {
			int[] colors = getColors();
			if(colors != null)
				return colors[index];
			else
				Log.d("COLOR ERROR", "returned null cube");
		}
		Log.d("COLOR ERROR", "index = " + index + " is out of bound.");
		return +1;
	}

	/** set the default colour with the cube is created or should return to
	 * @param default_colors colours picked from resources file
	 */
	public void set_Default_Colors(int[] default_colors) {
		this.default_colors = default_colors;
		//this.mld_colors.setValue(default_colors);
	}

	/** set the actual colors for the cube
	 * @param colors actual colors for the cube
	 */
	public void set_Colors(int[] colors) {
		if(colors != null)
			this.mld_colors.setValue(colors);
	}
	/**
	 * reset colors to the default colors
	 */
	public void resetColors() {
		if(default_colors != null) {
			this.set_Colors(this.default_colors);
		}
		Log.d("COLOR ERROR", "default colors is null");
	}

	/** change a colour in the index
	 * @param face_index index of the face where u want to change colour
	 * @param color new colour for the face
	 */
	public void set_Color_Index(int face_index, int color) {
		int[] previous_colors = getColors();
		if((face_index >= 0 && face_index < 6))
			previous_colors[face_index] = color;
		else
			Log.d("COLOR ERROR", "face_index = " + face_index + " out of bound, color = " + color);
		this.mld_colors.setValue(previous_colors);
	}
}
