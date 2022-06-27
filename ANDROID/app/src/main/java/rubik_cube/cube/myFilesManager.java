package rubik_cube.cube;

import android.content.Context;
import android.util.Log;

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

	// https://stackoverflow.com/questions/14376807/read-write-string-from-to-a-file-in-android

	//FM09_DataManagement.pdf slide 20.
	//FUNCTION TO WRITE A CUBE IN A FILE
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


	//FM09_DataManagement.pdf slide 21.
	//FUNCTION TO READ A CUBE FROM A FILE
	public static String READ(String filename, Context context) {

		String ret = "";

		try {
			InputStream inputStream = context.openFileInput(filename);

			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString;
				StringBuilder stringBuilder = new StringBuilder();

				while ( (receiveString = bufferedReader.readLine()) != null ) {
					stringBuilder.append(receiveString).append("\n");
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		}
		catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e);
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e);
		}

		System.out.println("ret = " + ret);
		return ret;
	}

	/**
	 * I check if it is saved
	 * @return if saved true else false
	 */
	public static boolean is_saved(Context context) {
		File dir = context.getFilesDir();
		File file = new File(dir, INTENT_FILENAME);
		return file.exists();
	}
	/**
	 * I save the model stuff NOW in the INTENT_FILENAME
	 */
	public static void save_cube_backup(Context context) {
		//i save with model function
		CubeViewModel model = new ViewModelProvider((ViewModelStoreOwner) context).get(CubeViewModel.class);
		model.SAVE_CUBE(context, myFilesManager.INTENT_FILENAME);
	}
	/**
	 * I save the model stuff NOW in the CUBE_FILENAME
	 */
	public static void save_cube_user(Context context) {
		//i save with model function
		CubeViewModel model = new ViewModelProvider((ViewModelStoreOwner) context).get(CubeViewModel.class);
		model.SAVE_CUBE(context, myFilesManager.INTENT_FILENAME);
	}
}
