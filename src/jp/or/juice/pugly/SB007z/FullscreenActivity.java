package jp.or.juice.pugly.SB007z;

import java.io.IOException;

import jp.or.juice.pugly.SB007z.R;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity implements OnClickListener{

	private TextView pppStatusView;
	private Button serviceScheduleButton;
	private int serviceTimer = 60;
	private SharedPreferences prefs;
	private MenuItem refreshMenuItem;
	private MenuItem executingMenuItem;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_fullscreen);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        serviceScheduleButton = (Button)findViewById(R.id.button_service_schedule);
        serviceScheduleButton.setOnClickListener(this);

        this.pppStatusView = (TextView)findViewById(R.id.ppp_status);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();

    	//new RouterTask(this).execute("");

    	boolean f = isScheduleService();
    	this.serviceScheduleButton.setText(f ? getResources().getString(R.string.service_scheduled) : getResources().getString(R.string.service_not_scheduled));

    	int v = Integer.parseInt(this.prefs.getString("serviceTimer", "60"));
    	Log.w("eiichi", this.prefs.getString("serviceTimer", "Null"));
    	if (this.serviceTimer != v) {
    		this.serviceTimer = v;
    		if (f) {
	    		cancelService();
	    		scheduleService();
    		}
    	}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.prefs, menu);
		LinearLayout fl = new LinearLayout(this);
		LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(64, -2, 0x11);
		fl.setLayoutParams(flp);
		
		this.refreshMenuItem = menu.add(0, 1, Menu.NONE, getResources().getString(R.string.actionbar_refresh));
		this.refreshMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		String idString = "@*android:drawable/ic_menu_refresh";
		int id = getResources().getIdentifier(idString, null, null);
		this.refreshMenuItem.setIcon(id);

		this.executingMenuItem = menu.add(0, 0, Menu.NONE, getResources().getString(R.string.actionbar_refresh));
		this.executingMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(40, 40);
		ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyle);
		pb.setLayoutParams(lp);
		fl.addView(pb);
		this.executingMenuItem.setActionView(fl);
		this.executingMenuItem.setVisible(false);
		
		MenuItem mi = menu.add(0, 2, Menu.NONE, getResources().getString(R.string.actionbar_setting));
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		mi.setIcon(android.R.drawable.ic_menu_preferences);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			this.refreshMenuItem.setVisible(false);
			this.executingMenuItem.setVisible(true);
			//this.setProgressBarIndeterminateVisibility(true);
			new RouterTask(this).execute("");
			break;
		case 2:
			Intent intent = new Intent(this, PrefsActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	public void onClick(View v) {
		if (v.getId() == R.id.button_service_schedule) {
        	if (isScheduleService()) {
            	cancelService();
        		this.serviceScheduleButton.setText(getResources().getString(R.string.service_not_scheduled));
        	} else {
            	scheduleService();
        		this.serviceScheduleButton.setText(getResources().getString(R.string.service_scheduled));
        	}
        }
    }

    private void scheduleService(){
    	Log.w("eiichi", "scheduleService()");
    	if (!isScheduleService()) {
        	Context context = getBaseContext();
        	Intent intent = new Intent(context, RouterStatusService.class);
    		PendingIntent pendingIntent = PendingIntent.getService(
    				context, -1, intent, 
    				PendingIntent.FLAG_UPDATE_CURRENT);
    		AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
    		alarmManager.setInexactRepeating(AlarmManager.RTC, 
    				System.currentTimeMillis(),
    				this.serviceTimer * 1000, pendingIntent);
    	} else {
    		Log.w("eiichi", "already scheduled");
    	}
    }
      
    private void cancelService(){
    	Log.w("eiichi", "cancelService()");
    	Context context = getBaseContext();
    	Intent intent = new Intent(context, RouterStatusService.class);
    	PendingIntent pendingIntent = PendingIntent.getService(
    			context, -1, intent, 
    			PendingIntent.FLAG_UPDATE_CURRENT);
    	AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
    	alarmManager.cancel(pendingIntent);
    	pendingIntent.cancel();
    }

    private boolean isScheduleService() {
    	Context context = getBaseContext();
    	Intent intent = new Intent(context, RouterStatusService.class);
    	PendingIntent pendingIntent = PendingIntent.getService(
	    		context, -1, intent, 
	    		PendingIntent.FLAG_NO_CREATE);
    	return (pendingIntent != null);
    }

	private void dispRouterStatus(RouterStatus rs) {
		((TextView)findViewById(R.id.signalbar_value)).setText(rs.signalbar);
		((TextView)findViewById(R.id.network_type_value)).setText(rs.network_type);
		((TextView)findViewById(R.id.network_provider_value)).setText(rs.network_provider);
		((TextView)findViewById(R.id.ppp_status_value)).setText(rs.ppp_status);
		((TextView)findViewById(R.id.modem_main_state_value)).setText(rs.modem_main_state);
		((TextView)findViewById(R.id.sms_unread_count_value)).setText(rs.sms_unread_count);
		((TextView)findViewById(R.id.wifi_status_value)).setText(rs.wifi_status);
		((TextView)findViewById(R.id.wifi_access_count_value)).setText(rs.wifi_access_count);
		((TextView)findViewById(R.id.battery_charging_value)).setText(rs.battery_charging);
		((TextView)findViewById(R.id.battery_value_value)).setText(rs.battery_value);
		((TextView)findViewById(R.id.new_message_value)).setText(rs.new_message);
		((TextView)findViewById(R.id.message_status_value)).setText(rs.message_status);
		((TextView)findViewById(R.id.login_info_value)).setText(rs.login_info);
		((TextView)findViewById(R.id.host_phone_value)).setText(rs.host_phone);
		((TextView)findViewById(R.id.wan_ipaddr_value)).setText(rs.wan_ipaddr);
		((TextView)findViewById(R.id.dns_mode_value)).setText(rs.dns_mode);
		((TextView)findViewById(R.id.prefer_dns_manual_value)).setText(rs.prefer_dns_manual);
		((TextView)findViewById(R.id.standby_dns_manual_value)).setText(rs.standby_dns_manual);
		((TextView)findViewById(R.id.prefer_dns_auto_value)).setText(rs.prefer_dns_auto);
		((TextView)findViewById(R.id.standby_dns_auto_value)).setText(rs.standby_dns_auto);
		((TextView)findViewById(R.id.SSID1_value)).setText(rs.SSID1);
		((TextView)findViewById(R.id.Channel_value)).setText(rs.Channel);
		((TextView)findViewById(R.id.EncrypType_value)).setText(rs.EncrypType);
		((TextView)findViewById(R.id.AuthMode_value)).setText(rs.AuthMode);
		((TextView)findViewById(R.id.WscModeOption_value)).setText(rs.WscModeOption);
		((TextView)findViewById(R.id.AuthMode_tmp_value)).setText(rs.AuthMode_tmp);
		((TextView)findViewById(R.id.lan_ipaddr_value)).setText(rs.lan_ipaddr);
		((TextView)findViewById(R.id.lan_netmask_value)).setText(rs.lan_netmask);
		((TextView)findViewById(R.id.dhcpEnabled_value)).setText(rs.dhcpEnabled);
		((TextView)findViewById(R.id.sw_outer_version_value)).setText(rs.sw_outer_version);
		((TextView)findViewById(R.id.hardware_version_value)).setText(rs.hardware_version);
		((TextView)findViewById(R.id.pin_status_value)).setText(rs.pin_status);
		((TextView)findViewById(R.id.lucknum_value)).setText(rs.lucknum);
}
    
    public class RouterTask extends AsyncTask<String, Integer, RouterStatus>{
    	
    	private FullscreenActivity activity;
    	
    	public RouterTask(FullscreenActivity activity) {
    		this.activity = activity;
    	}
    	
    	@Override
    	protected RouterStatus doInBackground(String... params) {
    		SB007z sb007z = new SB007z();
			RouterStatus rs = null;
    		try {
				rs = sb007z.doGetRouterStatus();
			} catch (IOException ioex) {
				return null;
			}
    		return rs;
    	}

    	@Override
    	protected void onPostExecute(RouterStatus rs) {
			if (rs != null) {
				String s = rs.pppStatus ? getResources().getString(R.string.text_ppp_connected) : getResources().getString(R.string.text_ppp_disconnected);
				this.activity.pppStatusView.setText(s);
				this.activity.dispRouterStatus(rs);
			}
			//this.activity.setProgressBarIndeterminateVisibility(false);
  			this.activity.refreshMenuItem.setVisible(true);
			this.activity.executingMenuItem.setVisible(false);
		}
    }
}
