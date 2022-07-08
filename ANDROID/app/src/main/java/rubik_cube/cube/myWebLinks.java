package rubik_cube.cube;

import androidx.lifecycle.ViewModel;

public class myWebLinks extends ViewModel {
	//default links
	//page wiki-how that explain the method
	public static final String webURI = "https://www.wikihow.it/Risolvere-il-Cubo-di-Rubik";
	//page where u can download a pdf
	public static final String pdfURI = "https://www.speedcubing.it/file/metodo_a_strati.pdf";
	//them explain the same method i think it is best than one only.

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

}
