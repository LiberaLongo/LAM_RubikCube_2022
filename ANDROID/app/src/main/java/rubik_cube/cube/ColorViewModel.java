package rubik_cube.cube;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

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
		return Objects.requireNonNull(mld_colors.getValue());
	}

	/** set the default colour with the cube is created or should return to
	 * @param default_colors colours picked from resources file
	 */
	public void set_Default_Colors(int[] default_colors) {
		this.default_colors = default_colors;
		this.mld_colors.setValue(default_colors);
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
	 * @return true if success, false otherwise
	 */
	public boolean resetColors() {
		if(default_colors != null) {
			this.set_Colors(this.default_colors);
			return true;
		}
		return false;
	}

	/** change a colour in the index
	 * @param index index of the face where u want to change colour
	 * @param color new colour for the face
	 */
	public void set_Color_Index(int index, int color) {
		int[] previous_colors = getColors();
		previous_colors[index] = color;
		this.mld_colors.setValue(previous_colors);
	}
}