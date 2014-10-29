package com.example.startservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

public class RemoteService extends Service{

	final RemoteCallbackList<IRemoteServiceCallback> mCallbacks
		= new RemoteCallbackList<IRemoteServiceCallback>();
	
	int mValue = 0;
	NotificationManager mNM;
	
	public void onCreate() {
		
		Log.e("start_service","onCreate");
		
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		sendBroadcast(new Intent("com.aaa"));
		
		//showNotification();
		
		//mHandler.sendEmptyMessage(REPORT_MSG);
	}
	
	public void onDestroy() {
		mNM.cancel(R.string.remote_service_started);
		
		Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
	
		mCallbacks.kill();
		
		mHandler.removeMessages(REPORT_MSG);
	}
	
	public IBinder onBind(Intent intent) {
		
		Log.e("start_service","onBind");
		
		if(IRemoteService.class.getName().equals(intent.getAction())) {
			return mBinder;
		}
		
		return null;
	}
	
	private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
		
		@Override
		public void unregisterCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(cb != null) mCallbacks.unregister(cb);
			
		}
		
		@Override
		public void registerCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(cb != null) mCallbacks.register(cb);
			
		}
	};
	
	private final ISecondary.Stub mSecondaryBinder = new ISecondary.Stub() {
		
		@Override
		public int getPid() throws RemoteException {
			// TODO Auto-generated method stub
			return Process.myPid();
		}
		
		@Override
		public void basicTypes(int anInt, long aLong, boolean aBoolean,
				float aFloat, double aDouble, String aString)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void onTaskRemoved(Intent rootIntent) {
		Toast.makeText(this, "Task removed: " + rootIntent, Toast.LENGTH_LONG).show();
	}
	
	private static final int REPORT_MSG =1;
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.e("handleMessage","handleMessage");
			final int mcall = mCallbacks.beginBroadcast();
			
			Log.e("mCallback","mCallback"+ mcall);
			
			switch (msg.what) {
			case REPORT_MSG: {
				int value = ++mValue;
				
				final int N = mCallbacks.beginBroadcast();
				
				
				for (int i=0; i<N; i++) {
					try {
						mCallbacks.getBroadcastItem(i).valueChanged(value);
					} catch (RemoteException e) {
						
					}
				}
					mCallbacks.finishBroadcast();
					
					sendMessageDelayed(obtainMessage(REPORT_MSG), 1*1000);
			} break;
			
			default:
				super.handleMessage(msg);
		 }
		}
	};
	
	private void showNotification() {
		CharSequence text = getText(R.string.remote_service_started);
		Intent intent = new Intent(this, MainActivity.class);
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.stat_sample)
				.setContentTitle(text)
				.setTicker(getText(R.string.remote_service_label));
		
		PendingIntent contentIntent = PendingIntent.getActivity(this
																		, 0
																		, intent 
																		, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		builder.setContentIntent(contentIntent);
		mNM.notify(R.string.remote_service_started, builder.build());
	}
	
}
