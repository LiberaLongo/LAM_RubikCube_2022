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

public class buttons_FRU extends Fragment implements Interface_my_Fragment{

	private Button[] btnMoves;
	private final int DIM = 3;

	public buttons_FRU () {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.button_fru_layout, container, false);
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
		Button btnFront = requireView().findViewById(R.id.buttonFront);
		Button btnRight = requireView().findViewById(R.id.buttonRight);
		Button btnUp = requireView().findViewById(R.id.buttonUp);

		this.btnMoves = new Button[] {btnFront, btnRight, btnUp};

		CubeViewModel model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);

		for (int i = 0; i < this.DIM; i++) {
			int finalI = i;
			btnMoves[i].setOnClickListener(v -> model.MOVE(finalI));
		}
	}
	@Override
	public void updateButtons(int[] default_colors) {
		if(btnMoves != null)
			for(int i = 0; i < this.DIM; i++) {
				btnMoves[i].setBackgroundColor(default_colors[i]);
			}
		else
			Log.d("ERROR", "button of moves not initialized");
	}
	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
	}
}
