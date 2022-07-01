package rubik_cube.cube;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import rubik_cube.navigation.R;

public class buttons_BLD extends Fragment implements I_button_frag{

	private final int DIM = 3;
	private Button[] btnMoves;

	public buttons_BLD () {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.button_bld_layout, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//MOVEs buttons
		Button btnBack = requireView().findViewById(R.id.buttonBack);
		Button btnLeft = requireView().findViewById(R.id.buttonLeft);
		Button btnDown = requireView().findViewById(R.id.buttonDown);

		this.btnMoves = new Button[]{btnBack, btnLeft, btnDown};

		//MODELS
		CubeViewModel model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);
		ColorViewModel colour_model = new ViewModelProvider(requireActivity()).get(ColorViewModel.class);

		//BEHAVIOUR
		for (int i = 0; i < this.DIM; i++) {
			int finalI = i;
			//color
			btnMoves[i].setBackgroundColor(colour_model.get_color_index(DIM + i));
			//behaviour
			btnMoves[i].setOnClickListener(v -> model.MOVE(DIM + finalI));
		}
	}
	@Override
	public void updateButtons(int[] colors) {
		if(btnMoves != null)
			for(int i = 0; i < this.DIM; i++) {
				btnMoves[i].setBackgroundColor(colors[DIM + i]);
			}
		else
			Log.d("ERROR", "button of moves not initialized");
	}
	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
	}
}
