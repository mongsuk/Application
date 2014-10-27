package com.example.servicetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Controller extends Activity {

	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.service_start_arguments_controller);
		
		Button button = (Button)findViewById(R.id.start1);
		button.setOnClickListener(mStart1Listener);
		button = (Button)findViewById(R.id.start2);
		button.setOnClickListener(mStart2Listener);
		button = (Button)findViewById(R.id.start3);
		button.setOnClickListener(mStart3Listener);
		button = (Button)findViewById(R.id.startfail);
		button.setOnClickListener(mStartFailListener);
		button = (Button)findViewById(R.id.kill);
		button.setOnClickListener(mKillListener);
		
	}
	
	private OnClickListener mStart1Listener = new OnClickListener() {
		public void onClick(View v) {
			startService(new Intent(Controller.this,
					MainActivity.class)
							.putExtra("name", "One"));
		}
	};
	
	private OnClickListener mStart2Listener = new OnClickListener() {
		public void onClick(View v) {
			startService(new Intent(Controller.this,
					MainActivity.class)
						.putExtra("name", "Two"));	
		}
	};
	
	private OnClickListener mStart3Listener = new OnClickListener() {
		public void onClick(View v) {
			startService(new Intent(Controller.this,
					MainActivity.class)
						.putExtra("name", "Three"));
		}
	};
	
	private OnClickListener mStartFailListener = new OnClickListener() {
		public void onClick(View v) {
			startService(new Intent(Controller.this,
					MainActivity.class)
							.putExtra("name", "Failure"));
		}
	};
	
	private OnClickListener mKillListener = new OnClickListener() {
		public void onClick(View v) {
			Process.killProcess(Process.myPid());
			
		}
	};		
}
