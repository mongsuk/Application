package com.example.startservice;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	static public class Receive extends BroadcastReceiver {
	 @Override
	 public void onReceive(Context context, Intent intent) {
         
	      String name = intent.getAction();
	         
	      if(name.equals("com.aaa")){
	        	
	        	Log.e("test","test");
	           
	        	Toast.makeText
	            (context, "정상적으로 값을 받았습니다.", Toast.LENGTH_SHORT).show();
	        }
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_service_controller);
		
		Button button= (Button)findViewById(R.id.start);
		button.setOnClickListener(mStartListener);
		button = (Button)findViewById(R.id.stop);
		button.setOnClickListener(mStopListener);
		
	}
	
	private OnClickListener mStartListener = new OnClickListener() {
		public void onClick(View v) {
			Log.e("start_service","start_service");
			startService(new Intent(
					"com.example.startservice.REMOTE_SERVICE"));
		}
	};
	
	private OnClickListener mStopListener = new OnClickListener() {
		public void onClick(View v) {
			stopService(new Intent(
					"com.example.startservice.REMOTE_SERVICE"));
		}
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
