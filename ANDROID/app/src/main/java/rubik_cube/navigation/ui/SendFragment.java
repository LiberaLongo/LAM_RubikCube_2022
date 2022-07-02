package rubik_cube.navigation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentSendBinding;

/**
 * Where the user can send and receive cube and algorithm in txt, pdf.
 */
public class SendFragment extends Fragment {

	private FragmentSendBinding binding;

	public SendFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//navigation stuff...
		binding = FragmentSendBinding.inflate(inflater, container, false);

		//where i show what user clicked
		TextView tv = binding.textView;
		//SEND BUTTONS
		Button send_cube = binding.btnSendCube;
		Button send_txt = binding.btnSendTxt;
		Button send_pdf = binding.btnSendPdf;
		//RECEIVE BUTTONS
		Button receive_cube = binding.btnReceiveCube;
		Button receive_txt = binding.btnReceiveTxt;
		Button receive_pdf = binding.btnReceivePdf;

		//CUBE
		send_cube.setOnClickListener(v -> {
			//a message for the user
			tv.setText(requireActivity().getString(R.string.send_cube));
		});
		receive_cube.setOnClickListener(v -> {
			//a message for the user
			tv.setText(requireActivity().getString(R.string.receive_cube));
		});
		//TXT
		send_txt.setOnClickListener(v -> {
			//a message for the user
			tv.setText(requireActivity().getString(R.string.send_txt));
		});
		receive_txt.setOnClickListener(v -> {
			//a message for the user
			tv.setText(requireActivity().getString(R.string.receive_txt));
		});
		//PDF
		send_pdf.setOnClickListener(v -> {
			//a message for the user
			tv.setText(requireActivity().getString(R.string.send_pdf));
		});
		receive_pdf.setOnClickListener(v -> {
			//a message for the user
			tv.setText(requireActivity().getString(R.string.receive_pdf));
		});

		return binding.getRoot();
	}

	private void send(String filename) {

	}
	private String receive(String filename) {
		return "";
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		binding = null;
	}
}
