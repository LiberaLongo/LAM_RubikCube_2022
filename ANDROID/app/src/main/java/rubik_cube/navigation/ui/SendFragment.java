package rubik_cube.navigation.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
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

	private final File sendPath = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DOCUMENTS);
	private final File receivePath = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DOWNLOADS);

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

		//ask permission if not granted yet
		ActivityCompat.requestPermissions(requireActivity(),
				new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
				PackageManager.PERMISSION_GRANTED);

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
			String message = myFilesManager.READ(this.cube_filename, requireActivity());
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
			String message = myFilesManager.READ(this.txt_filename, requireActivity());
			text +=	this.sendMessage(message, this.txt_filename, txt);
			tv.setText(text);
		});
		receive_txt.setOnClickListener(v -> {
			//a message for the user
			String received = this.receive(this.txt_filename, null);
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
			String received = this.receive(this.pdf_filename, null);
			String msg = requireActivity().getString(R.string.receive_pdf) + "\n" + received;
			tv.setText(msg);
		});

		return binding.getRoot();
	}

	/**
	 * emulates a receive by reading the files from internal memory
	 * @param filenameExtern filename extern i should read
	 * @param filenameIntern filename where i should save into, null if equals to extern filename.
	 * @return a string to be seen by the user
	 */
	private String receive(String filenameExtern, String filenameIntern) {
		//result is true if ok a string otherwise
		String result = "false";

		if(filenameIntern == null) filenameIntern = filenameExtern;

		//File file = new File(this.requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + filenameExtern);
		File file = new File(this.receivePath, filenameExtern);
		if(file.exists()) {
			try {
				//read the extern file
				String content = myFilesManager.READ(filenameExtern, requireActivity());
				//now write in the internal file
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
	 * @param message the message (got from internal files)
	 * @param filename the filename where i have to write the message
	 */
	private String sendMessage(String message, String filename, String type) {
		File file = new File(this.sendPath, filename);
		if (type.equals(txt))
			//write the message in the external file
			try {
				FileOutputStream outputStream = new FileOutputStream(file);
				outputStream.write(message.getBytes());
				outputStream.flush();
				outputStream.close();
				Log.d("FILE", file.toString());
			} catch (Exception e) {
				Log.d("FILE", e.getMessage());
			}

		//now create the send intent
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		//if i had success in creating file
		if (file.exists()) {
			//uri stuff
			Uri fileURI = FileProvider.getUriForFile(requireActivity(),
					requireActivity().getApplicationContext().getPackageName() + ".provider",
					file);
			sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// grant all three uri permissions!List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

			requireActivity().grantUriPermission(
					String.valueOf(this.sendPath), fileURI,
					Intent.FLAG_GRANT_READ_URI_PERMISSION);

			sendIntent.putExtra(Intent.EXTRA_STREAM, fileURI);
			if (type.equals(txt))
				sendIntent.setType("text/plain");
			else if (type.equals(pdf))
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
