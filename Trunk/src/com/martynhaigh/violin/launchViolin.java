package com.martynhaigh.violin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class LaunchViolin extends Activity  {
	
    private static final int MENU_ABOUT = Menu.FIRST;
    private static final int MENU_QUIT = MENU_ABOUT + 1;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int acceptedCodeVersion = appPreferences.getInt("acceptedVersion", 0);
        Log.i("start","Accepted Version" + acceptedCodeVersion);
        if(acceptedCodeVersion == 0 || acceptedCodeVersion != getAppVersionCode(getBaseContext())) {

    		String versionNumber = "";
    		
    		try {
    			versionNumber = getBaseContext().getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
    		} catch (NameNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		String whatsNew = "";
    		String headerText = getResources().getString(R.string.whatsNewText) + " " + versionNumber;
    		String[] arrayContents = getResources()
    				.getStringArray(R.array.whatsNew);
    		for (String s : arrayContents) {
    			whatsNew += "* " + s + "\n";
    		}       	
        	 new AlertDialog.Builder(this)
             .setIcon(android.R.drawable.ic_dialog_alert)
             .setTitle(headerText)
             .setMessage(whatsNew)
             .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                	 dialog.dismiss();
                 }
             })
             .show();        	
			SharedPreferences.Editor editor = appPreferences.edit();
	        editor.putInt("acceptedVersion",  getAppVersionCode(getBaseContext()));
	        editor.commit(); // Very important
        }
     
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

	public int getAppVersionCode(Context context) {
	    if ( context!= null )
	    {
	        try {
	            return context.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
	        } catch (NameNotFoundException e) {
	            // App not installed!
	        }
	    }
	    return -1;
	}

	public void quitApplication() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
				.setTitle(R.string.app_name)
				.setCancelable(false).setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								LaunchViolin.this.finish();
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