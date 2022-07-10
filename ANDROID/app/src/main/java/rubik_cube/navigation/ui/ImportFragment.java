package rubik_cube.navigation.ui;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;

import rubik_cube.cube.myWebLinks;
import rubik_cube.navigation.R;
import rubik_cube.navigation.databinding.FragmentImportBinding;

/**
 * Where the user write his algorithm to show in the visualizeFragment
 */
public class ImportFragment extends Fragment {

	//navigation stuff
	private FragmentImportBinding binding;

	//web stuff
	private WebView webView;
	private long download_id;
	private DLReceiver downloadReceiver;
	private DownloadManager downloadManager;

	//default links
	//page wiki-how that explain the method
	private final String webURI = myWebLinks.webURI;
	//page where u can download a pdf
	private final String pdfURI = myWebLinks.pdfURI;
	//them explain the same method i think it is best than one only.

	//actual links
	private String my_URI;

	/* DOWNLOAD */
	private class DLReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				ParcelFileDescriptor f = downloadManager.openDownloadedFile(download_id);
				Log.d("FILE", "OPENED!!!" + f);
				TextView tv = binding.textView;
				tv.setText(getString(R.string.download_correct));
			} catch (FileNotFoundException e) {
				Log.d("FILE", "FAILURE");
				e.printStackTrace();
			}
		}
	}

	public ImportFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//navigation stuff...
		binding = FragmentImportBinding.inflate(inflater, container, false);

		//verify internet permissions
		if(requireActivity().checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			requireActivity().requestPermissions(new String[] {Manifest.permission.INTERNET}, 0);
		}
		if(requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
				PackageManager.PERMISSION_GRANTED) {
			requireActivity().requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
					101);
		}

		//links model (get and set only)
		myWebLinks links = new ViewModelProvider(requireActivity()).get(myWebLinks.class);
		this.my_URI = links.get_my_URI();
		//System.out.println("MY URI IS THIS NOW: " + my_URI);

		//my views
		TextView tv = binding.textView;
		tv.setText(links.get_my_URI());
		EditText edit = binding.writeUri;

		/* WEB VIEW*/
		this.webView = binding.webView;
		myWebLinks.search(this.my_URI, webView);
		Button btnRefresh = binding.btnRefresh;
		btnRefresh.setOnClickListener(v -> this.webView.reload());

		//fab behaviour
		FloatingActionButton fab = binding.fabURI;
		fab.setOnClickListener(view -> {
			//get the string of the uri
			Editable editable = edit.getText();
			//and save it in private strings
			String search_string = String.valueOf(editable);
			if(!search_string.equals("")) {
				this.my_URI = search_string;
				links.set_my_URI(search_string);
			}
			//then update the web view
			myWebLinks.search(this.my_URI, webView);
			//then update the text view
			tv.setText(search_string);
		});

		/* DOWNLOAD */
		downloadReceiver = new DLReceiver();
		Button btnDownload = binding.buttonDownload;
		btnDownload.setOnClickListener(v -> {

			downloadManager = (DownloadManager)
					requireActivity().getSystemService(DOWNLOAD_SERVICE);
			String URI = this.my_URI;
			if(!this.my_URI.endsWith(".pdf")) URI = pdfURI;
			download_id = downloadManager.enqueue(new DownloadManager.Request(Uri.parse(URI))
					.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
							DownloadManager.Request.NETWORK_MOBILE)
					.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
							"algorithm.pdf"));
			requireActivity().registerReceiver(downloadReceiver,
					new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		});

		return binding.getRoot();
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		// Do something that differs the Activity's menu here
		inflater.inflate(R.menu.import_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//links model (get and set only)
		myWebLinks links = new ViewModelProvider(requireActivity()).get(myWebLinks.class);

		String tv_text;
		boolean extern_app = false;

		//array of colours for the cube
		switch (item.getItemId()) {
			//case R.id.activity_menu_item:
			//not implemented here
			//return false;
			case R.id.google:
				this.my_URI = myWebLinks.google;
				tv_text = getResources().getString(R.string.google);
				//update the links
				links.set_my_URI(myWebLinks.google);
				break;
			case R.id.default_web:
				this.my_URI = this.webURI;
				tv_text = getResources().getString(R.string.default_web);
				//update the links
				links.set_my_URI(this.webURI);
				break;
			case R.id.default_pdf:
				this.my_URI = this.pdfURI;
				tv_text = getResources().getString(R.string.default_pdf);
				//update the links
				links.set_my_URI(this.pdfURI);
				break;
			case R.id.import_new:
				this.my_URI = links.get_my_URI();
				tv_text = getResources().getString(R.string.import_new);
				break;
			case R.id.view_pdf:
				this.my_URI = links.get_my_URI();
				extern_app = true;
				tv_text = getResources().getString(R.string.view_pdf);
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		//update the text view
		TextView tv = binding.textView;
		tv_text += "\n" + links.get_my_URI();
		tv.setText(tv_text);

		//update the web view
		myWebLinks.search(this.my_URI, webView);
		//view the pdf from an extern activity
		if(extern_app && this.my_URI.endsWith(".pdf")) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(this.my_URI), "application/pdf");
			startActivity(intent);
		}

		return true;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		binding = null;
	}
}
