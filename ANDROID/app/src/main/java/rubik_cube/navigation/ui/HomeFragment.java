package rubik_cube.navigation.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import rubik_cube.cube.CubeViewModel;
import rubik_cube.cube.Cube_Fragment;
import rubik_cube.cube.myFilesManager;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentHomeBinding;

/**
 * Where i draw the Rubik Cube and its fixed stuff
 */
public class HomeFragment extends Fragment {

	private FragmentHomeBinding binding;

	public HomeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//navigation stuff...
		binding = FragmentHomeBinding.inflate(inflater, container, false);
		//MODELS
		CubeViewModel cube_model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);
		//i want to draw the swapCube under the fix cube drawers...
		cube_model.setSwapCube_SizeXY(50, 200, 250);
		//i want the cube_fragment do DRAW the fix drawers parts
		FragmentManager fm = getChildFragmentManager();
		Cube_Fragment fragment = (Cube_Fragment) fm.findFragmentById(R.id.cube_frag);
		if (fragment != null) {
			fragment.setDrawFixCube(true);
		} else {
			Log.d("FRAGMENT", "home error");
		}

		return binding.getRoot();
	}

	//menu stuff
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		myFilesManager.save_cube_backup(requireActivity());
		binding = null;
	}
}
