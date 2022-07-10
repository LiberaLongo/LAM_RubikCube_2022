package rubik_cube.navigation;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/** PERMISSIONS */
public class myPermissionsManagement {

	private static final String LOG = "PERMISSIONS";
	// look at https://developer.android.com/training/permissions/requesting

	// In an educational UI, explain to the user why your app requires this
	// permission for a specific feature to behave as expected. In this UI,
	// include a "cancel" or "no thanks" button that allows the user to
	// continue using your app without granting the permission.
	private static boolean showInContextUI(Activity context, String to_show) {
		final boolean[] permissionGranted = {false};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(to_show).setCancelable(false);
		builder.setPositiveButton("Yes, allow", (dialog, id) -> {
			//permissionGrantedNow
			Log.d(LOG, "permission dialog user answered YES");
			permissionGranted[0] = true;
			//and exit dialog
			dialog.cancel();
		});
		builder.setNegativeButton("No, tanks", (dialog, id) -> {
			//permissionStillDenied
			Log.d(LOG, "permission dialog user answered NO");
			permissionGranted[0] = false;
			//and exit dialog
			dialog.cancel();
		});
		AlertDialog alert = builder.create();
		alert.show();
		return permissionGranted[0];
	}

	public static boolean ask_permission(Activity activity, String permission, int location, String why_app_need_it) {
		boolean permissionGranted = false;
		//i basically need only manage, read, write in extern file for my app
		//then i check if permission is read or write and handle the "manage" permission for android >= 11
		//if i need read and write i need also "manage"
		if (permission.equals(READ_EXTERNAL_STORAGE) || permission.equals(WRITE_EXTERNAL_STORAGE)) {
			Log.d(LOG, "my sdk_int is " + SDK_INT);
			if (SDK_INT >= 30)
				if (!Environment.isExternalStorageManager()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setMessage("Permission needed for managing files!\n\n" + why_app_need_it).setCancelable(false);
					final boolean[] manages_files = {false};
					builder.setPositiveButton("Settings", (dialog, id) -> {
						//permissionGrantedNow
						Log.d(LOG, "permission MANAGE dialog user answered YES");
						try {
							Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
							Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
							activity.startActivity(intent);
						} catch (Exception ex) {
							Intent intent = new Intent();
							intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
							activity.startActivity(intent);
						}
						manages_files[0] = true;
						dialog.cancel();
					});
					builder.setNegativeButton("No, tanks", (dialog, id) -> {
						//permissionStillDenied
						Log.d(LOG, "permission MANAGE dialog user answered NO");
						manages_files[0] = false;
						dialog.cancel();
					});
					AlertDialog alert = builder.create();
					alert.show();

					//now if i can't manage files exit, else continue to see others permissions (read or write probably)
					if(!manages_files[0]) return false;
				}
		}
		//manage the remaining read or write permission
		if (ContextCompat.checkSelfPermission(activity, permission) ==
				PackageManager.PERMISSION_GRANTED) {
			// You can use the API that requires the permission.
			Log.d(LOG, "permission already granted");
			permissionGranted = true;
		} else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
			// In an educational UI, explain to the user why your app requires this
			// permission for a specific feature to behave as expected. In this UI,
			// include a "cancel" or "no thanks" button that allows the user to
			// continue using your app without granting the permission.
			Log.d(LOG, "shouldShow returned true");
			permissionGranted = showInContextUI(activity, why_app_need_it);
		} else {
			Log.d(LOG, "shouldShow returned false");
			// You can directly ask for the permission.
			ActivityCompat.requestPermissions(activity, new String[] {
					permission }, location);
		}
		return permissionGranted;
	}
}
