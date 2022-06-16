package rubikcube.stuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rubikcubeandroid.R;

import rubik_cube.Cube;

public class myButtonFragment extends Fragment implements Interface_my_Fragment{

	private Button[] btnMoves;
	//private MaterialButtonToggleGroup.OnButtonCheckedListener listener;

	public myButtonFragment () {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.button_fragment, container, false);
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
		Button btnBack = requireView().findViewById(R.id.buttonBack);
		Button btnLeft = requireView().findViewById(R.id.buttonLeft);
		Button btnDown = requireView().findViewById(R.id.buttonDown);

		this.btnMoves = new Button[] {btnFront, btnRight, btnUp, btnBack, btnLeft, btnDown};

		myViewModel model = new ViewModelProvider(requireActivity()).get(myViewModel.class);

		final String clockwiseText = (String) getResources().getText(R.string.clockwiseText);
		final String anticlockwiseText = (String) getResources().getText(R.string.anticlockwiseText);

		//clockwise or anticlockwise switch
		@SuppressLint("UseSwitchCompatOrMaterialCode")
		Switch clockwiseButton = requireView().findViewById(R.id.clockwiseSwitch);
		clockwiseButton.setOnClickListener( v -> {
			model.changeClockwise();
			if(model.isClockwise())
				clockwiseButton.setText(clockwiseText);
			else
				clockwiseButton.setText(anticlockwiseText);
		});

		if(model.isClockwise())
			clockwiseButton.setText(clockwiseText);
		else
			clockwiseButton.setText(anticlockwiseText);

		for (int i = 0; i < Cube.FACES_IN_A_CUBE; i++) {
			int finalI = i;
			btnMoves[i].setOnClickListener(v -> {
				model.MOVE(finalI);
			});
		}
	}
	@Override
	public void updateButtons(int[] default_colors) {
		if(btnMoves != null)
			for(int i = 0; i < Cube.FACES_IN_A_CUBE; i++) {
				int finalI = i;
				btnMoves[i].setBackgroundColor(default_colors[finalI]);
			}
		else
			Log.d("ERROR", "button of moves not initialized");
	}
	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);

		/*try{
			//model
			myViewModel model = new ViewModelProvider(this).get(myViewModel.class);
			updateButtons(model.get_Cube_Colors());
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement colors for buttons well");
		}*/
	}
}
