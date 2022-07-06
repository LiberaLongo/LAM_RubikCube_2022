package rubik_cube.navigation.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import rubik_cube.cube.myFilesManager;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentWriteBinding;

/**
 * Where the user write his algorithm to show in the visualizeFragment
 */
public class WriteFragment extends Fragment {

	private FragmentWriteBinding binding;

	public WriteFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//navigation stuff...
		binding = FragmentWriteBinding.inflate(inflater, container, false);
		return binding.getRoot();
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
		inflater.inflate(R.menu.write_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		TextView tv = binding.textView;
		EditText edit = binding.editText;

		//array of colours for the cube
		switch (item.getItemId()) {
			//case R.id.activity_menu_item:
			//not implemented here
			//return false;
			case R.id.write_save:
				Editable editable = edit.getText();
				myFilesManager.WRITE(myFilesManager.WRITE_FILENAME, requireActivity(), String.valueOf(editable));
				tv.setText(getString(R.string.algorithm_save));
				return true;
			case R.id.write_load:
				String str = myFilesManager.READ(myFilesManager.WRITE_FILENAME, requireActivity());
				edit.setText(str);
				tv.setText(getString(R.string.algorithm_load));
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		binding = null;
	}
}