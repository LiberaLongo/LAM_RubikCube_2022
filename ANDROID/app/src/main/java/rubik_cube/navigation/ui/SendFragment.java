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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import rubik_cube.cube.Cube;
import rubik_cube.cube.CubeViewModel;
import rubik_cube.cube.myFilesManager;
import rubik_cube.cube.myWebLinks;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentSendBinding;
import rubik_cube.navigation.myPermissionsManagement;

/**
 * Where the user can send and receive cube and algorithm in txt, pdf.
 */
public class SendFragment extends Fragment {

	//navigation stuff
	private FragmentSendBinding binding;

	//type of files i wish to send and receive

	//filenames i wish to send and receive
	private final String cube_filename = myFilesManager.CUBE_FILENAME; //txt with a cube
	private final String txt_filename = myFilesManager.WRITE_FILENAME; //txt with an algorithm

	private final String folder_explanation = "\n(*) usually in:" +
			"\narchive -> internal memory\n-> ";

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

		CubeViewModel cube_model = new ViewModelProvider(requireActivity()).get(CubeViewModel.class);

		//CUBE
		send_cube.setOnClickListener(v -> {
			//a message for the user
			String text = requireActivity().getString(R.string.send_cube) + "\n";
			//first i see if user saved something
			String message = myFilesManager.READ(this.cube_filename, requireActivity());
			if(message == null) {
				//if the user didn't save i get the cube message directly from model, the "ACTUAL" configuration.
				Cube cube = cube_model.getNullableCube();
				//cube can be null now if cube wasn't created yet
				if(cube != null)
					message = cube.toString();
				else {
					message = "";
					Toast.makeText(requireActivity(), "no cube created yet", Toast.LENGTH_LONG).show();
				}
			}
			if(!message.equals(""))
				text += this.sendMessage(message, this.cube_filename, "cube");
			tv.setText(text);
		});
		receive_cube.setOnClickListener(v -> {
			String received = this.receive(this.cube_filename, myFilesManager.CHALLENGE_FILENAME, "cube");
			String msg = requireActivity().getString(R.string.receive_cube) + "\n" + received;
			//a message for the user
			tv.setText(msg);
		});
		//TXT
		send_txt.setOnClickListener(v -> {
			//a message for the user
			String text = requireActivity().getString(R.string.send_txt) + "\n";
			String message = myFilesManager.READ(this.txt_filename, requireActivity());
			text +=	this.sendMessage(message, this.txt_filename, "algorithm TXT written by you");
			tv.setText(text);
		});
		receive_txt.setOnClickListener(v -> {
			//a message for the user
			String received = this.receive(this.txt_filename, null, "algorithm TXT written by you");
			String msg = requireActivity().getString(R.string.receive_txt) + "\n" + received;
			tv.setText(msg);
		});
		//PDF

		//links model (get and set only)
		myWebLinks links = new ViewModelProvider(requireActivity()).get(myWebLinks.class);

		send_pdf.setOnClickListener(v -> {
			//a message for the user
			String text = requireActivity().getString(R.string.send_pdf) + "\n";
			text +=	this.sendPDF(links.get_my_URI());
			tv.setText(text);
		});

		return binding.getRoot();
	}

	/* SEND AND RECEIVE */
	 /** emulates a receive by reading the files from internal memory
	 * @param filenameExtern filename extern i should read
	 * @param filenameIntern filename where i should save into, null if equals to extern filename.
	 * @return a string to be seen by the user
	 */
	private String receive(String filenameExtern, String filenameIntern, String reason) {
		//result is true if ok a string otherwise
		String result;

		//permissions locations
		int MY_PERMISSIONS_READ = 0;

		//ask permission if not granted yet
		if(myPermissionsManagement.ask_permission(requireActivity(),
				READ_EXTERNAL_STORAGE, MY_PERMISSIONS_READ,
				getResources().getString(R.string.app_name) +
						" will only read a file called " + filenameExtern +
						" in the Download folder that you placed here that contain the " + reason))
		{	//if permission granted i can perform tasks

			if(filenameIntern == null) filenameIntern = filenameExtern;

			File file = new File(this.receivePath, File.separator + filenameExtern);
			if(file.exists()) {
				try {
					//read the extern file
					String content = readExternalStorage(file);
					System.out.println(content);
					//now write in the internal file
					myFilesManager.WRITE(filenameIntern, requireContext(), content);
					result = "\n" + file + "\n" + content;
				} catch (Exception e) {
					String error = file.getAbsolutePath() +"\n" + e.getMessage();
					result = "FILE ERROR RECEIVING\n" + error;
					Log.d("FILE ERROR RECEIVING", error);
				}
			} else {
				result = "\nbefore receiving you should save the file received in \n" + file.getPath()
						+ "\nand it should be named ''" + filenameExtern + "''\nin DOWNLOAD directory (*)"
						+ "\nand retry pressing 'receive button' u pressed now\n"
						+ folder_explanation + "Download -> " + filenameExtern + " -> DONE";
			}
		} else { result = "permission is denied, i can't receive, i need read an extern file"; }
		return result;
	}

	/**
	 * @param message the message (got from internal files)
	 * @param filename the filename where i have to write the message
	 */
	private String sendMessage(String message, String filename, String reason) {
		String result; //what i should write in the tv
		String text = ""; //what i should send as explanation

		//now create the send intent
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);

		//permission location
		int MY_PERMISSIONS_WRITE = 1;

		//ask permission if not granted yet
		if(myPermissionsManagement.ask_permission(requireActivity(),
				WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_WRITE,
				getResources().getString(R.string.app_name) +
						" will only save a file called " + filename +
						" in the Documents/rubik_cube folder that contain the " + reason))
		{	//if permission granted i can perform tasks

			String folder_name = "rubik_cube";
			File folder = new File(this.sendPath, folder_name);
			if (folder.mkdir()) Log.i("FILE FOLDER", "mkdir true");
			else Log.i("FILE FOLDER", "mkdir false");
			File file = new File(folder, filename);
			//write the message in the external file
			try {
				// Create new file
				// if it does not exist
				if (file.createNewFile())
					System.out.println("File created");
				else
					System.out.println("File already exists");
				FileOutputStream outputStream = new FileOutputStream(file);
				outputStream.write(message.getBytes());
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				String error = file.getAbsolutePath() + "\n" + e.getMessage();
				text += "FILE ERROR WRITING\n" + error;
				Log.d("FILE ERROR WRITING", error);
			}
			//a title
			sendIntent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.app_name));
			//intent explanation how to receive it
			text += "i send you a file called ''" + filename + "''"
					+ "\nto save in Download folder (*)\n\nand then open from \n"
					+ getResources().getString(R.string.app_name)
					+ " app \n-> left side 'menu'\n-> "
					+ getResources().getString(R.string.menu_send)
					+ "\n\n(path = " + receivePath.getPath() + File.separator + filename + ")\n"
					+ folder_explanation + "Download -> " + filename + " -> DONE";

			//if i had success in creating file
			if (file.exists()) {
				//uri stuff
				Uri fileURI = FileProvider.getUriForFile(requireActivity(),
						requireActivity().getApplicationContext().getPackageName() + ".provider",
						file);
				sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// grant all three uri permissions!List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

				requireActivity().grantUriPermission(
						String.valueOf(file), fileURI,
						Intent.FLAG_GRANT_READ_URI_PERMISSION);

				sendIntent.putExtra(Intent.EXTRA_STREAM, fileURI);
			} else {
				result = "file doesn't exists";
				return result;
			}
			sendIntent.putExtra(Intent.EXTRA_TEXT, text);
			sendIntent.setType("text/plain");
			// ... and start the intent
			startActivity(Intent.createChooser(sendIntent,
					getResources().getText(R.string.send_to)));

			//note: for some reason in my phone it is "Unsupported content"
			result = "\nIf u see a 'toast' under with \n' Unsupported content ' "
					+ "\nyou should see if there is a file called ''" + filename + "''"
					+ "\nin the DOCUMENTS folder (*)\n of your telephone and send it"
					+ "\nand in the subdirectory called " + folder_name
					+ "\n\nhis path should be:\n" + file + "\n"
					+ folder_explanation + "Documents -> " + filename + " -> DONE";
		} else {
			//show permission denied
			result = "permission is denied, i can't send, i need write an extern file";
		}
		return result;
	}

	/**
	 * @param uri of pdf
	 */
	private String sendPDF(String uri) {
		String text = "";

		//now create the send intent
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.app_name));
		text += "I tried to send you a link to a PDF algorithm of"
				+ getResources().getString(R.string.app_name)
				+ "\nits URI is:\n\n" + uri
				+ "\n\nand then you can open from \n"
				+ getResources().getString(R.string.app_name)
				+ "app \n-> left side 'menu'\n-> "
				+ getResources().getString(R.string.menu_import);

		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.setType("text/plain");
		// ... and start the intent
		startActivity(Intent.createChooser(sendIntent,
				getResources().getText(R.string.send_to)));

		//in order to update the textView...
		return "I tried to send a PDF with this uri:\t" + uri;
	}

	private String readExternalStorage(File my_file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(my_file);
			int i;
			StringBuilder buffer = new StringBuilder();
			while ((i = fis.read()) != -1) {
				buffer.append((char) i);
			}
			return buffer.toString();
		} catch (Exception e) {
			String error = my_file.getAbsolutePath() ;
			error += "\n error.getMessage() = " + e.getMessage();
			String result = "FILE ERROR READING\n" + error;
			Log.d("FILE ERROR READING", error);
			e.printStackTrace();
			return result;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		binding = null;
	}
}
