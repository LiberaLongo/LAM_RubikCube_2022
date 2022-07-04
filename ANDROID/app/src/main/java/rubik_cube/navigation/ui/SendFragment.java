package rubik_cube.navigation.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;

import rubik_cube.cube.myFilesManager;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentSendBinding;

/**
 * Where the user can send and receive cube and algorithm in txt, pdf.
 */
public class SendFragment extends Fragment {

	//navigation stuff
	private FragmentSendBinding binding;

	//type of files i wish to send and receive
	private static final String txt = ".txt";
	private static final String pdf = ".pdf";

	//filenames i wish to send and receive
	private final String cube_filename = myFilesManager.CUBE_FILENAME; //txt with a cube
	private final String txt_filename = myFilesManager.WRITE_FILENAME; //txt with an algorithm
	private final String pdf_filename = myFilesManager.PDF_FILENAME;   //pdf

	public SendFragment() {
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
			String text = requireActivity().getString(R.string.send_cube) + "\n";
			String message = myFilesManager.READ(this.cube_filename, requireActivity(), null);
			text +=	this.sendMessage(message, this.cube_filename, txt);
			tv.setText(text);
		});
		receive_cube.setOnClickListener(v -> {
			String received = this.receive(this.cube_filename, myFilesManager.CHALLENGE_FILENAME);
			String msg = requireActivity().getString(R.string.receive_cube) + "\n" + received;
			//a message for the user
			tv.setText(msg);
		});
		//TXT
		send_txt.setOnClickListener(v -> {
			//a message for the user
			String text = requireActivity().getString(R.string.send_txt) + "\n";
			String message = myFilesManager.READ(this.txt_filename, requireActivity(), null);
			text +=	this.sendMessage(message, this.txt_filename, txt);
			tv.setText(text);
		});
		receive_txt.setOnClickListener(v -> {
			//a message for the user
			String received = this.receive(this.txt_filename, this.txt_filename);
			String msg = requireActivity().getString(R.string.receive_txt) + "\n" + received;
			tv.setText(msg);
		});
		//PDF
		send_pdf.setOnClickListener(v -> {
			//a message for the user
			String text = requireActivity().getString(R.string.send_pdf) + "\n";
			text +=	this.sendMessage("", this.pdf_filename, pdf);
			tv.setText(text);
		});
		receive_pdf.setOnClickListener(v -> {
			//a message for the user
			String received = this.receive(this.pdf_filename, this.pdf_filename);
			String msg = requireActivity().getString(R.string.receive_pdf) + "\n" + received;
			tv.setText(msg);

		});

		return binding.getRoot();
	}

	/**
	 * if i received something i want to "copy" from extern to intern file
	 *
	 * @return string to save into file
	 */
	private String receive(String filenameExtern, String filenameIntern) {
		//ask permission for external storage if not granted yet
		if (ActivityCompat.checkSelfPermission
				(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
				PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(requireActivity(),
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
		} else {
			Toast.makeText(requireContext(), "Has Permissions to read" , Toast.LENGTH_LONG).show();
		}
		//result is true if ok a string otherwise
		String result = "false";
		//File file = new File(this.requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + filenameExtern);
		File file = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOCUMENTS), filenameExtern);
		//		Environment.DIRECTORY_DOWNLOADS), filenameExtern);
		if(file.exists()) {
			try {
				String content = myFilesManager.READ(filenameExtern, requireActivity(), file);
				myFilesManager.WRITE(filenameIntern, requireContext(), content);
				result = "\n" + file + "\n" + content;
			} catch (Exception e) {
				Log.d("FILE", e.getMessage());
			}
		} else {
			result = "\nyou should save the file received in " + file;
			result += "\nand it should be named " + filenameExtern + " in DOCUMENT directory";
			result += "\nand retry pressing 'receive button' u pressed now";
		}
		return result;
	}

	/**
	 * @param message the message inside the file
	 * @param filename the filename where i have to write the message
	 */
	private String sendMessage(String message, String filename, String type) {
		//ask permission for external storage if not granted yet
		if (ActivityCompat.checkSelfPermission
				(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
				PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(requireActivity(),
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
		} else {
			Toast.makeText(requireContext(), "Has Permissions to write" , Toast.LENGTH_LONG).show();
		}
		//file declaration
		//it is saved in the Document directory as filename.txt
		//then probably:
		// .../Document/cube.txt
		// or .../Document/write.txt
		// or .../Document/algorithm.pdf
		File file = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOCUMENTS), filename);
		//file copy the message into the extern file
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(message.getBytes());
			outputStream.close();
			Log.d("FILE", file.toString());
		} catch (Exception e) {
			Log.d("FILE", e.getMessage());
		}
		//now create the send intent
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		//if i had success in creating file
		if(file.exists()) {
			sendIntent.putExtra(Intent.EXTRA_STREAM, file);
			if(type.equals(txt))
				sendIntent.setType("text/*");
			else if(type.equals(pdf))
				sendIntent.setType("application/pdf");
		} else { //if i haven't success i try to send the plain text only
			String extra_text = "I tried to send you a file called " + filename;
			extra_text += "\nbut writing extern file failed ...\n" + message;
			sendIntent.putExtra(Intent.EXTRA_TEXT, extra_text);
			sendIntent.setType("text/plain");
		}
		// ... and start the intent
		startActivity(Intent.createChooser(sendIntent,
				getResources().getText(R.string.send_to)));
		//note: for some reason in my phone it is "Unsupported content"
		String result = "\nIf u see a 'toast' under with \n' Unsupported content ' ";
		result += "\nyou should see if there is a file called " + filename ;
		result += "\nin the DOCUMENTS folder (*) of your telephone and send it";
		result += "\nhis path should be:\n" + file;
		result += "\n\n(*) from messaging app (in my test phone it is):";
		result += "\n attach -> file -> internal archive -> archive -> internal memory";
		result += " -> Documents -> " + filename + " -> done ";
		return result;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		binding = null;
	}
}
