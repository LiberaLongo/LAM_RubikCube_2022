package rubik_cube.cube;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public abstract class myFilesManager {

	public static final String CUBE_FILENAME = "cube.txt"; //chosen by user
	public static final String WRITE_FILENAME = "write.txt"; //algorithm
	public static final String INTENT_FILENAME = "intent.txt"; //auto-saved
	public static final String CHALLENGE_FILENAME = "challenge.txt"; //received from a message

	// https://stackoverflow.com/questions/14376807/read-write-string-from-to-a-file-in-android

	//FM09_DataManagement.pdf slide 20.
	//FUNCTION TO WRITE something IN A FILE
	public static void WRITE(String filename, Context context, String to_write) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					context.openFileOutput(filename, Context.MODE_PRIVATE));
			outputStreamWriter.write(to_write);
			outputStreamWriter.close();
			System.out.println(to_write);
		}
		catch (IOException e) {
			Log.e("Exception", "File write failed: " + e);
		}
	}


	/**
	 * //FM09_DataManagement.pdf slide 21.
	 * //FUNCTION to READ the content of a FILE in INTERNAL STORAGE
	 *
	 * @param filename the file name i want to read
	 * @param context the context the filename is saved
	 *				* (most of times requireActivity() is called from a fragment)
	 * @return the content of the file as a String
	 */
	public static String READ(String filename, Context context) {

		String ret = null;
		if(!is_saved(context, filename))
			Toast.makeText(context, "file isn't saved then i can't load it", Toast.LENGTH_SHORT).show();
		try {
			InputStream inputStream;
			inputStream = context.openFileInput(filename);

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString;
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString).append("\n");
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		}
		catch (FileNotFoundException e) {
			Log.e("FILE_MANAGER", "File not found: " + e);
		} catch (IOException e) {
			Log.e("FILE_MANAGER", "Can not read file: " + e);
		}
		System.out.println("ret = " + ret);
		return ret;
	}

	/**
	 * I check if it is saved
	 * @return if saved true else false
	 */
	public static boolean is_saved(Context context, String filename) {
		File dir = context.getFilesDir();
		File file = new File(dir, filename);
		return file.exists();
	}
	/**
	 * I save the model stuff NOW in the INTENT_FILENAME
	 */
	public static void save_cube_backup(Context context) {
		//i save with model function
		CubeViewModel model = new ViewModelProvider((ViewModelStoreOwner) context).get(CubeViewModel.class);
		model.SAVE_CUBE(context, myFilesManager.INTENT_FILENAME, null);
	}
}
