package com.martynhaigh.violin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class launchViolin extends Activity {
	
    private static final int MENU_ABOUT = Menu.FIRST;
    private static final int MENU_QUIT = MENU_ABOUT + 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_ABOUT, 0, "About").setIcon(android.R.drawable.ic_menu_help);	    
	    menu.add(0, MENU_QUIT, 0, "Quit").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
	    return true;
	}	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_ABOUT:

			new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(
					R.string.about_text).setPositiveButton("OK",
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).setCancelable(false).create().show();
	    	
	    	
	    	return true;
	    case MENU_QUIT:
	    	quitApplication();
	        return true;
	    }
	    return false;
	}		

	public void quitApplication() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
				.setTitle(R.string.app_name)
				.setCancelable(false).setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								launchViolin.this.finish();
							}
						}).setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}