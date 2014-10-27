package com.example.servicetest;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.os.Process;

@SuppressLint("NewApi")
public class MainActivity extends Service {
	private NotificationManager mNM;
	private Intent mInvokeIntent;
	private volatile Looper mServiceLooper;
	private volatile ServiceHandler mServiceHandler;
	
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		
		public void handleMessage(Message msg) {
			Bundle arguments = (Bundle)msg.obj;
			
			String txt = arguments.getString("name");
			
			Log.i("ServiceStartArguments", "Message: " + msg + ", "
					+ arguments.getString("name"));
			
			if ((msg.arg2 &Service.START_FLAG_REDELIVERY) ==0 ){
				txt = "New cmd #" + msg.arg1 + " : " + txt;
			} else {
				txt = "Re-delivered #" + msg.arg1 + ": " + txt;
			}
			
			showNotification(txt);
			
			long endTime = System.currentTimeMillis() + 5*1000;
			while(System.currentTimeMillis() < endTime) {
				synchronized (this) {
					try {
						wait(endTime - System.currentTimeMillis());
					} catch (Exception e) {
						
					}
				}
			}
			
			hideNotification();
			
			Log.i("ServiceStartArguments", "Done with #" + msg.arg1);
			stopSelfResult(msg.arg1);
		}
	}
	
	public void onCreate() {
		HandlerThread thread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}
	
	
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("ServiceStartArguments",
				"Starting #" + startId + ": " + intent.getExtras());
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.arg2 = flags;
		msg.obj = intent.getExtras();
		mServiceHandler.sendMessage(msg);
		Log.i("ServiceStartArguments", "Sending: " + msg);
		
		if(intent.getBooleanExtra("fail", false)) {
			if((flags & START_FLAG_RETRY) == 0) {
				
				Process.killProcess(Process.myPid());
			}
		}
		
		return intent.getBooleanExtra("redeliver", false)
				? START_REDELIVER_INTENT : START_NOT_STICKY;
	}
	
	public void onDestroy() {
		mServiceLooper.quit();
		
		hideNotification();
		
		 Toast.makeText(MainActivity.this, R.string.service_destroyed,
	                Toast.LENGTH_SHORT).show();
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void showNotification(String text) {
		Intent intent = new Intent(this,Controller.class);
		
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		NotificationCompat.Builder builder = 
				new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.stat_sample)
				.setContentTitle(getText(R.string.service_start_arguments_label))
				.setContentText(text);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this 
																		, 0
																		, intent
																		, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		builder.setContentIntent(contentIntent);
		mNM.notify(R.string.service_created,builder.build());		
		
	}
	
	private void hideNotification() {
		mNM.cancel(R.string.service_created);
	}
	/*
	public static class Controller extends Activity {
		
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
	*/
}
