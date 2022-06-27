package rubik_cube.navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.snackbar.Snackbar;
import rubik_cube.navigation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

	private AppBarConfiguration mAppBarConfiguration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//navigation stuff
		ActivityMainBinding binding =
				ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		setSupportActionBar(binding.appBarMain.toolbar);
		/* //no longer used here
		* binding.appBarMain.fab.setOnClickListener(view -> {
		*	//"Replace with your own action"
		*	Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
		*			.setAction("Action", null).show();
		* });
		* */
		DrawerLayout drawer = binding.drawerLayout;
		NavigationView navigationView = binding.navView;
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		mAppBarConfiguration = new AppBarConfiguration.Builder(
				R.id.nav_home)
				.setOpenableLayout(drawer)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cube_menu, menu);
		return true;
	}

	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		String msg = "";
		switch (item.getItemId()) {
			//case R.id.activity_menu_item:
				// Do Activity menu item stuff here
				//return true;

			//all this cases under are not implemented here!
			//(i do only a log to see i passed from here)
			case R.id.reset:  msg = "Reset"; break;
			case R.id.random: msg = "Random"; break;
			case R.id.save:   msg = "Save"; break;
			case R.id.load:   msg = "Load"; break;
			default:
				break;
		}
		Log.d("SELECTED", msg);
		return false;
	}

	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration)
				|| super.onSupportNavigateUp();
	}
}