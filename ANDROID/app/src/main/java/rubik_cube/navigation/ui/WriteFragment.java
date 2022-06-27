package rubik_cube.navigation.ui;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//navigation stuff...
		binding = FragmentWriteBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		//my stuff here

		TextView tv = binding.textView;
		EditText edit = binding.editText;

		Button load = binding.load;
		load.setOnClickListener(l -> {
			String str = myFilesManager.READ(myFilesManager.WRITE_FILENAME, requireActivity());
			edit.setText(str);
			tv.setText(getString(R.string.load));
		});

		Button save = binding.save;
		save.setOnClickListener(l -> {
			Editable str = edit.getText();
			myFilesManager.WRITE(myFilesManager.WRITE_FILENAME, requireActivity(), String.valueOf(str));
			tv.setText(getString(R.string.save));
		});

		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		binding = null;
	}
}