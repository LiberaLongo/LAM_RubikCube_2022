package rubik_cube.cube;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import rubik_cube.navigation.R;

public class Cube_Fragment extends Fragment{

	private static final String IS_SAVED = "IS_SAVED";
	private boolean draw_fix_cube = true;
	private Context context;
	private int[] default_colors;

	public Cube_Fragment () {
	}
	public void setDrawFixCube(boolean draw_fix_cube) {this.draw_fix_cube = draw_fix_cube; }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = requireActivity();
		return inflater.inflate(R.layout.cube_fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//cube stuff
		this.default_colors = getResources().getIntArray(R.array.color_array);
		//text Views
		String[] letters = getResources().getStringArray(R.array.faceLetter_array);
		//the image where i have to draw the cube
		ImageView image = requireView().findViewById(R.id.Image);

		//MODELS
		CubeViewModel cube_model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);

		//clockwise or anticlockwise SWITCH
		final String clockwiseText = (String) getResources().getText(R.string.clockwiseText);
		final String anticlockwiseText = (String) getResources().getText(R.string.anticlockwiseText);
		@SuppressLint("UseSwitchCompatOrMaterialCode")
		Switch clockwiseButton = requireView().findViewById(R.id.clockwiseSwitch);
		clockwiseButton.setOnClickListener( v -> {
			cube_model.changeClockwise();
			if(cube_model.isClockwise())
				clockwiseButton.setText(clockwiseText);
			else
				clockwiseButton.setText(anticlockwiseText);
		});
		if(cube_model.isClockwise())
			clockwiseButton.setText(clockwiseText);
		else
			clockwiseButton.setText(anticlockwiseText);

		//i check if i have to create a NEW CUBE OR RELOAD it.
		this.checkCubeBackup();

		//and now OBSERVE cube
		cube_model.observeCube(this, cube -> {
			int lastMove = cube.getLastMove();
			Log.d("CUBE_OBSERVING", "i observed last move is " + lastMove);
			if(lastMove >= 0) {
				String text = "";
				if (cube_model.isClockwise()) text += " ";
				else text += "'";
				cube_model.add_move_in_queue(letters[lastMove % 6] + text);
			}
			updateUI(image);
		});

		//in order to swap the image
		// in order to swipe and maybe fix this errors check this link:
		// https://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
		image.setOnTouchListener(new OnSwipeTouchListener(requireActivity()) {
			//top and down have the same output
			@Override
			public void onSwipeBottom() {
				cube_model.swapUpsideDown();
				updateUI(image);
			}
			@Override
			public void onSwipeTop() {
				cube_model.swapUpsideDown();
				updateUI(image);
			}
			//left or right
			@Override
			public void onSwipeLeft() {
				cube_model.swapLeft();
				updateUI(image);
			}
			@Override
			public void onSwipeRight() {
				cube_model.swapRight();
				updateUI(image);
			}
		});
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
	}

	//i load the cube and restore colors and buttons colors
	private void load(String filename) {
		//MODELS
		CubeViewModel cube_model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);
		ColorViewModel colour_model = new ViewModelProvider(requireActivity()).get(ColorViewModel.class);
		//restore the cube
		int[] colors = cube_model.LOAD_CUBE(context, filename);
		//restore the colors
		colour_model.set_Colors(colors);
		//update buttons colors
		FragmentManager fm = getChildFragmentManager();
		buttons_FRU frag_FRU = (buttons_FRU) fm.findFragmentById(R.id.leftFragment);
		buttons_BLD frag_BLD = (buttons_BLD) fm.findFragmentById(R.id.rightFragment);
		if (frag_FRU != null) {
			frag_FRU.updateButtons(colors);
		} else {
			Log.d("FRAGMENT", "FRU fragment error");
		}
		if (frag_BLD != null) {
			frag_BLD.updateButtons(colors);
		} else {
			Log.d("FRAGMENT", "BLD fragment error");
		}
	}

	//i check if i have to create a NEW CUBE OR RELOAD it.
	private void checkCubeBackup() {
		//MODELS
		CubeViewModel cube_model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);
		ColorViewModel colour_model = new ViewModelProvider(requireActivity()).get(ColorViewModel.class);
		//i check if i have to create a NEW CUBE OR RELOAD it.
		boolean isSaved = myFilesManager.is_saved(context);
		if(isSaved) {
			//i ask to reload the cube from the INTENT_FILENAME file.
			Log.d(IS_SAVED, "loaded");
			this.load(myFilesManager.INTENT_FILENAME);
			Toast.makeText(requireActivity(), "loaded", Toast.LENGTH_SHORT).show();
		} else {
			//i ask the model to set the default colors according to the resources colours.
			cube_model.createCube(this.default_colors, false);
			colour_model.set_Default_Colors(this.default_colors);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		this.checkCubeBackup();
	}

	@Override
	public void onStop() {
		super.onStop();

		myFilesManager.save_cube_backup(context);

	}

	void updateUI(ImageView image) { //upgrade the user interface

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
			String text = getString(R.string.moveText) + " ";
			text += names[lastMove % 6]; //6 faces in a cube
			TextView tvResult = requireView().findViewById(R.id.textViewResult);
			tvResult.setText(text);
		}
		//update history queue
		TextView tvHistory = requireView().findViewById(R.id.textViewHistory);
		String history = getString(R.string.history);
		tvHistory.setText(model.getQueueText(history));

		//draw
		Drawer.draw(image, cube, model.getSwapper(), this.draw_fix_cube, getResources().getConfiguration());
	}

	//menu stuff
	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		// Do something that differs the Activity's menu here
		inflater.inflate(R.menu.cube_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		TextView tvResult = requireView().findViewById(R.id.textViewResult);
		ImageView image = requireView().findViewById(R.id.Image);

		//I save the model stuff NOW int the INTENT_FILENAME
		CubeViewModel model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);

		//myFilesManager.save_cube_backup(requireActivity());

		//array of colours for the cube
		switch (item.getItemId()) {
			//case R.id.activity_menu_item:
			//not implemented here
			//return false;
			case R.id.random:
				tvResult.setText(getString(R.string.cube_random));
				model.Random();
				updateUI(image);
				return true;
			case R.id.reset:
				tvResult.setText(getString(R.string.cube_reset));
				model.Reset();
				updateUI(image);
				return true;
			case R.id.cube_save:
				tvResult.setText(getString(R.string.cube_save));
				model.SAVE_CUBE(context, myFilesManager.CUBE_FILENAME);
				updateUI(image);
				return true;
			case R.id.cube_load:
				tvResult.setText(getString(R.string.cube_load));
				this.load(myFilesManager.CUBE_FILENAME);
				updateUI(image);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
