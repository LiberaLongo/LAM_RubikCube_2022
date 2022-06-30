package rubik_cube.navigation.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import rubik_cube.cube.ColorViewModel;
import rubik_cube.cube.Cube;
import rubik_cube.cube.CubeViewModel;
import rubik_cube.cube.Drawer;
import rubik_cube.cube.OnSwipeTouchListener;
import rubik_cube.cube.myFilesManager;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentHomeBinding;

/**
 * Where i draw the Rubik Cube and its fixed stuff
 */
public class HomeFragment extends Fragment {

	private static final String IS_SAVED = "isSaved";

	private FragmentHomeBinding binding;

	@SuppressLint("ClickableViewAccessibility")
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		//navigation stuff...
		binding = FragmentHomeBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		/*
		 * //stuff i not need but it is for "blank" fragment
		 * final TextView textView = binding.textHome;
		 * HomeViewModel homeViewModel =
		 * 		new ViewModelProvider(this).get(HomeViewModel.class);
		 * homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
		 */

		//CUBE STUFF!
		//array of colors used by the Cube builder
		//cube stuff
		int[] default_colors = getResources().getIntArray(R.array.color_array);
		//text Views
		String[] letters = getResources().getStringArray(R.array.faceLetter_array);

		ImageView image = binding.Image;

		//MODELS
		CubeViewModel cube_model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);
		ColorViewModel colour_model = new ViewModelProvider(requireActivity()).get(ColorViewModel.class);

		//i check the bundle to understood if i have to create a new cube or reload it.
		boolean isSaved = myFilesManager.is_saved(requireActivity());
		if(isSaved) {
			//i ask to reload the cube from the INTENT_FILENAME file.
			Log.d(IS_SAVED, "loaded");
			int[] colors = cube_model.LOAD_CUBE(requireActivity(), myFilesManager.INTENT_FILENAME);
			colour_model.set_Colors(colors);
			Toast.makeText(requireActivity(), "loaded", Toast.LENGTH_SHORT).show();
		} else {
			//i ask the model to set the default colors according to the resources colours.
			cube_model.createCube(default_colors, false);
			colour_model.set_Default_Colors(default_colors);
		}

		//and now observe cube
		cube_model.observeCube(this, cube -> {
			int lastMove = cube.getLastMove();
			Log.d("CUBE_OBSERVING", "i observed last move is " + lastMove);
			if(lastMove >= 0) {
				String text = "";
				if (cube_model.isClockwise()) text += " ";
				else text += "'";
				cube_model.add_move_in_queue(letters[lastMove % 6] + text);
			}
			updateUI(image, binding);
		});
		cube_model.setSwapCube_SizeXY(50, 200, 250);

		//in order to swap the image
		// in order to swipe and maybe fix this errors check this link:
		// https://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
		image.setOnTouchListener(new OnSwipeTouchListener(requireActivity()) {
			//top and down have the same output except for the toast
			@Override
			public void onSwipeBottom() {
				cube_model.swapUpsideDown();
				updateUI(image, binding);
			}
			@Override
			public void onSwipeTop() {
				cube_model.swapUpsideDown();
				updateUI(image, binding);
			}

			@Override
			public void onSwipeLeft() {
				cube_model.swapLeft();
				updateUI(image, binding);
			}

			@Override
			public void onSwipeRight() {
				cube_model.swapRight();
				updateUI(image, binding);
			}
		});

		//... navigation stuff
		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		myFilesManager.save_cube_backup(requireActivity());

		binding = null;
	}

	void updateUI(ImageView image, FragmentHomeBinding binding) { //upgrade the user interface

		//name of the faces as strings
		String[] names = getResources().getStringArray(R.array.faceName_array);

		//model
		CubeViewModel model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);

		//cube
		Cube cube = model.getCube();

		//last move
		int lastMove = cube.getLastMove();
		if(lastMove >= 0) {
			//reset test result
			String text = getString(R.string.moveText);
			text += names[lastMove % 6]; //6 faces in a cube
			TextView tvResult = binding.textViewResult;
			tvResult.setText(text);
		}
		//update history queue
		TextView tvHistory = binding.textViewHistory;
		String history = getString(R.string.history);
		tvHistory.setText(model.getQueueText(history));

		//draw
		Drawer.draw(image, cube, model.getSwapper(), true, getResources().getConfiguration());
	}

	//menu stuff
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		// Do something that differs the Activity's menu here
		inflater.inflate(R.menu.cube_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		TextView tvResult = binding.textViewResult;
		ImageView image = binding.Image;

		//I save the model stuff NOW int the INTENT_FILENAME
		CubeViewModel model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);
		myFilesManager.save_cube_backup(requireActivity());

		//array of colours for the cube
		switch (item.getItemId()) {
			//case R.id.activity_menu_item:
			//not implemented here
			//return false;
			case R.id.random:
				tvResult.setText(getString(R.string.cube_random));
				model.Random();
				updateUI(image, binding);
				return true;
			case R.id.reset:
				tvResult.setText(getString(R.string.cube_reset));
				model.Reset();
				updateUI(image, binding);
				return true;
			case R.id.cube_save:
				tvResult.setText(getString(R.string.cube_save));
				model.SAVE_CUBE(requireActivity(), myFilesManager.CUBE_FILENAME);
				updateUI(image, binding);
				return true;
			case R.id.cube_load:
				tvResult.setText(getString(R.string.cube_load));
				model.LOAD_CUBE(requireActivity(), myFilesManager.CUBE_FILENAME);
				updateUI(image, binding);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
