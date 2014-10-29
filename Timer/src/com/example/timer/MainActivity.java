package com.example.timer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	volatile	int value=0;
	volatile	int value2=0;
	TextView mText;
	TextView mText2;
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			value++;
			mText.setText("Value = "+value);
			mHandler.sendEmptyMessageDelayed(0,1000);
		}
	};
	
	Handler cmHandler = new Handler() {
		public void handleMessage(Message msg) {
			value2++;
			mText2.setText("Value = "+value);
			cmHandler.sendEmptyMessageDelayed(0,3000);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mText = (TextView)findViewById(R.id.hello_world);
		mText2 = (TextView)findViewById(R.id.hello_world2);
		
		mHandler.sendEmptyMessage(0);
		cmHandler.sendEmptyMessage(0);
	}
	
	
/**/
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
