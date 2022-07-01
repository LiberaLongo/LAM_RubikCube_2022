package rubik_cube.navigation.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import rubik_cube.cube.ColorViewModel;
import rubik_cube.cube.CubeViewModel;
import rubik_cube.cube.myFilesManager;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentColorBinding;

/**
 * Where the user write his algorithm to show in the visualizeFragment
 */
public class ColorFragment extends Fragment {

	private FragmentColorBinding binding;

	public ColorFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//navigation stuff...
		binding = FragmentColorBinding.inflate(inflater, container, false);

		//array of faces names
		String[] faces = getResources().getStringArray(R.array.faceName_array);
		String[] color_name = getResources().getStringArray(R.array.colorName_array);
		int[] default_colors = getResources().getIntArray(R.array.color_array);
		//BUTTONS
		//default button
		Button btn_default_colors = binding.buttonDefault;

		//faces buttons
		Button btnFront = binding.buttonFront;
		Button btnRight = binding.buttonRight;
		Button btnUp = binding.buttonUp;
		Button btnBack = binding.buttonBack;
		Button btnLeft = binding.buttonLeft;
		Button btnDown = binding.buttonDown;

		Button[] btnFaces = new Button[] {btnFront, btnRight, btnUp, btnBack, btnLeft, btnDown};

		//confirm button
		Button btn_Confirm = binding.buttonConfirm;

		//MODEL for the COLOR
		CubeViewModel cube_model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);
		ColorViewModel colour_model = new ViewModelProvider(requireActivity()).get(ColorViewModel.class);

		colour_model.set_Default_Colors(default_colors);

		//BEHAVIOURS
		//default color button behaviour
		btn_default_colors.setOnClickListener(v -> {
			//a message to the user to say i'm doing what him asked
			String msg = requireActivity().getString(R.string.default_colors);
			Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
			//set "current" colors to default ones
			colour_model.resetColors();
		});

		//confirm button behaviour
		btn_Confirm.setOnClickListener(v -> {
			//a message to the user to say i'm doing what him asked
			String msg = requireActivity().getString(R.string.confirm);
			Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
			//create a new cube with "current" colors
			cube_model.createCube(colour_model.getColors(), true);
			//and save into a backup
			myFilesManager.save_cube_backup(requireActivity());
		});

		//faces buttons behaviour
		int DIM = 6;
		for (int i = 0; i < DIM; i++) {
			int face_choice = i;
			btnFaces[i].setOnClickListener(v -> {
				Toast.makeText(requireActivity(),
						"you selected the " + face_choice + " button",
						Toast.LENGTH_SHORT).show();

				//FM12_navigation_architecture.pdf (slide 13)
				//Dialog: AlertDialog with a list
				final CharSequence[] items = getResources().getStringArray(R.array.colorName_array);
				AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
				builder.setTitle("Pick a color");
				builder.setSingleChoiceItems(items, -1, (dialog, color_index) -> {
					String msg = faces[face_choice] + " becomes " + color_name[color_index] ;
					Log.d("COLOR", "item = " + color_index +
							", face_choice = " + face_choice + " (" + msg + ")");
					Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
					colour_model.set_Color_Index(face_choice, color_index);
					btnFaces[face_choice].setBackgroundColor(default_colors[color_index]);
				});
				AlertDialog alert = builder.create();
				alert.show();
			});
		}

		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		binding = null;
	}
}
