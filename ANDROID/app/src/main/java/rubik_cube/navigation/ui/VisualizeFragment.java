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
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import rubik_cube.cube.CubeViewModel;
import rubik_cube.cube.Cube_Fragment;
import rubik_cube.cube.myFilesManager;
import rubik_cube.cube.myWebLinks;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentVisualizeBinding;

/**
 * Where i draw the Rubik Cube and its fixed stuff
 */
public class VisualizeFragment extends Fragment {

	private FragmentVisualizeBinding binding;

	public VisualizeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//navigation stuff...
		binding = FragmentVisualizeBinding.inflate(inflater, container, false);

		//web view invisible
		WebView webView = binding.webView;
		webView.setVisibility(View.INVISIBLE);

		//CUBE
		CubeViewModel cube_model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);
		//i want to draw the swapCube at the center of the image
		cube_model.setSwapCube_SizeXY(60, 200, 200);
		//i want the cube_fragment do NOT DRAW the fix drawers parts
		FragmentManager fm = getChildFragmentManager();
		Cube_Fragment fragment = (Cube_Fragment) fm.findFragmentById(R.id.cube_frag);
		if (fragment != null) {
			fragment.setDrawFixCube(false);
		} else {
			Log.d("FRAGMENT", "visualize error");
		}

		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		myFilesManager.save_cube_backup(requireActivity());
		binding = null;
	}

	//menu stuff
	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		// Do something that differs the Activity's menu here
		inflater.inflate(R.menu.visualize_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {

		//algorithm views
		TextView tv = binding.textView;
		WebView webView = binding.webView;

		//model for links
		myWebLinks links = new ViewModelProvider(requireActivity()).get(myWebLinks.class);

		switch (item.getItemId()) {
			//case R.id.activity_menu_item:
			//not implemented here
			//return false;
			case R.id.visualize_txt:
				//get content from file
				String str = myFilesManager.READ(myFilesManager.WRITE_FILENAME, requireActivity());
				//preparation of view visibility
				webView.setVisibility(View.INVISIBLE);
				tv.setVisibility(View.VISIBLE);
				//update the view
				tv.setText(str);
				return true;
			case R.id.visualize_pdf:
				//preparation of view visibility
				tv.setVisibility(View.INVISIBLE);
				webView.setVisibility(View.VISIBLE);
				//update the view
				myWebLinks.search(links.get_my_URI(), webView);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
