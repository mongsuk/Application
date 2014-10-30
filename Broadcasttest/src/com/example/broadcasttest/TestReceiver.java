package com.example.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TestReceiver {
	
	public TestReceiver(){
		
	}

	static public class Test extends BroadcastReceiver {

		public Test(){
			
		}
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String name = intent.getAction();
			
			if(name.equals("com.aaa")) {
				Toast.makeText(context,"정상",Toast.LENGTH_SHORT).show();
			}
			
		}
	}
}
