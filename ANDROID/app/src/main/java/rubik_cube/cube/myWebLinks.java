package rubik_cube.cube;

import android.annotation.SuppressLint;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.lifecycle.ViewModel;

public class myWebLinks extends ViewModel {
	//default links
	//page wiki-how that explain the method
	public static final String webURI = "https://www.wikihow.it/Risolvere-il-Cubo-di-Rubik";
	//page where u can download a pdf
	public static final String pdfURI = "https://www.speedcubing.it/file/metodo_a_strati.pdf";
	//them explain the same method i think it is best than one only.

	//google for search a uri if u don't know one...
	public static final String google = "https://www.google.com";

	/**
	 * uri for all my pdf purposes
	 */
	private String my_uri = webURI;

	public void set_my_URI(String new_uri) {
		this.my_uri = new_uri;
	}

	public String get_my_URI() {
		return this.my_uri;
	}

	@SuppressLint("SetJavaScriptEnabled")
	public static void search(String uri, WebView webView) {
		String url = "";
		if(uri.endsWith(".pdf"))
			url += "https://drive.google.com/viewerng/viewer?embedded=true&url=";
		url += uri;
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient());
	}

}
