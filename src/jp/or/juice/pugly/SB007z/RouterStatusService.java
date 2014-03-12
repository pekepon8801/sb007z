package jp.or.juice.pugly.SB007z;

import java.io.IOException;

import jp.or.juice.pugly.SB007z.R;


import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;

public class RouterStatusService extends IntentService {

	private String lucknum = "";
	
	public RouterStatusService() {
		super("RouterStatusService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("eiichi", "onHandleIntent");

		SB007z sb007z = new SB007z();
		
		try {
			RouterStatus rs = sb007z.doGetRouterStatus();
			if (rs.loginInfo) {
				this.lucknum = rs.lucknum;
			}
			if (!rs.pppStatus) {
				if (!rs.loginInfo) {
					sb007z.doLoginToRouter();
					rs = sb007z.doGetRouterStatus();
					if (rs.loginInfo) {
						this.lucknum = rs.lucknum;
					}
				}
				sb007z.doConnectRouterPPP(this.lucknum);
				notifyConnect();
			}
		} catch (IOException ioex) {
			Log.w("eiichi", ioex);
			ioex.printStackTrace();
		}
	}
    
    private void notifyConnect() {
    	NotificationManager mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    	Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
    	PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
    	 
    	NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplicationContext());
    	nb.setContentIntent(pi);
    	nb.setTicker(getResources().getString(R.string.notify_ticker));
    	nb.setSmallIcon(R.drawable.ic_launcher);
    	nb.setContentTitle(getResources().getString(R.string.app_name));
    	nb.setContentText(getResources().getString(R.string.notify_content_text));
    	//nb.setLargeIcon(icon);
    	nb.setWhen(System.currentTimeMillis());
    	nb.setDefaults(Notification.DEFAULT_SOUND);
    	nb.setAutoCancel(true);
    	
    	mManager.notify(1, nb.build());
    }
}
