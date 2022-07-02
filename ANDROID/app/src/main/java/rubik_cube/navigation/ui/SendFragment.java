package rubik_cube.navigation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import rubik_cube.cube.myFilesManager;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentSendBinding;

/**
 * Where the user can send and receive cube and algorithm in txt, pdf.
 */
public class SendFragment extends Fragment {

	private static final String txt = "text/plain";
	private static final String pdf = "application/pdf";

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
			//send the cube "saved from the user"
			String message = myFilesManager.READ(myFilesManager.CUBE_FILENAME, requireActivity());
			this.sendMessage(message, txt);
		});
		receive_cube.setOnClickListener(v -> {
			//a message for the user
			boolean received = this.receive(myFilesManager.CHALLENGE_FILENAME, txt);
			String msg = requireActivity().getString(R.string.receive_cube) + "\nreceived " + received;
			tv.setText(msg);
		});
		//TXT
		send_txt.setOnClickListener(v -> {
			//a message for the user
			tv.setText(requireActivity().getString(R.string.send_txt));
			//send the algorithm "written by me" or "imported"
			String message = myFilesManager.READ(myFilesManager.WRITE_FILENAME, requireActivity());
			this.sendMessage(message, txt);
		});
		receive_txt.setOnClickListener(v -> {
			//a message for the user
			boolean received = this.receive(myFilesManager.WRITE_FILENAME, txt);
			String msg = requireActivity().getString(R.string.receive_txt) + "\nreceived " + received;
			tv.setText(msg);
		});
		//PDF
		send_pdf.setOnClickListener(v -> {
			//a message for the user
			tv.setText(requireActivity().getString(R.string.send_pdf));
			this.sendMessage(myFilesManager.PDF_FILENAME, pdf);
		});
		receive_pdf.setOnClickListener(v -> {
			//a message for the user
			boolean received = this.receive(myFilesManager.PDF_FILENAME, pdf);
			String msg = requireActivity().getString(R.string.receive_pdf) + "\nreceived " + received;
			tv.setText(msg);

		});

		return binding.getRoot();
	}

	/**
	 * Emulates the receive of a file.
	 * but i haven't successfully understood how to send a FILE txt
	 * then i ask the user to copy it into the EditText view...
	 *
	 * @return string to save into file
	 */
	private boolean receive(String filename, String type) {
		return false;
	}

	/**
	* @param message message txt you want to send.
	* @param type tell if u are trying to send a txt or a pdf
	*
	* Note: i tried to send a FILE txt but i didn't understood how to do it!
	* and i haven't handled pdf yet
	*/
	private void sendMessage(String message, String type) {
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		binding = null;
	}
}
