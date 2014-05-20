package jp.or.juice.pugly.SB007z;

import java.io.BufferedReader;
import java.io.StringReader;
import android.util.Log;

public class RouterStatus {
	public boolean pppStatus = false;
	public boolean loginInfo = false;
	public String signalbar = "";
	public String network_type = "";
	public String network_provider = "";
	public String ppp_status = "";
	public String modem_main_state = "";
	public String sms_unread_count = "";
	public String wifi_status = "";
	public String wifi_access_count = "";
	public String battery_charging = "";
	public String battery_value = "";
	public String new_message = "";
	public String message_status = "";
	public String login_info = "";
	public String host_phone = "";
	public String wan_ipaddr = "";
	public String dns_mode = "";
	public String prefer_dns_manual = "";
	public String standby_dns_manual = "";
	public String prefer_dns_auto = "";
	public String standby_dns_auto = "";
	public String SSID1 = "";
	public String Channel = "";
	public String EncrypType = "";
	public String AuthMode = "";
	public String WscModeOption = "";
	public String AuthMode_tmp = "";
	public String lan_ipaddr = "";
	public String lan_netmask = "";
	public String dhcpEnabled = "";
	public String sw_outer_version = "";
	public String hardware_version = "";
	public String pin_status = "";
	public String lucknum = "";
	
	public RouterStatus(String statusString) {
		String ps = getRouterStatusString(statusString, "ppp_status");
		this.pppStatus = ps.equals("ppp_connected") || ps.equals("ppp_connecting");
		String li = getRouterStatusString(statusString, "login_info");
		this.loginInfo = li.equals("ok");


		this.signalbar = getRouterStatusString(statusString, "signalbar");
		this.network_type = getRouterStatusString(statusString, "network_type");
		this.network_provider = getRouterStatusString(statusString, "network_provider");
		this.ppp_status = getRouterStatusString(statusString, "ppp_status");
		this.modem_main_state = getRouterStatusString(statusString, "modem_main_state");
		this.sms_unread_count = getRouterStatusString(statusString, "sms_unread_count");
		this.wifi_status = getRouterStatusString(statusString, "wifi_status");
		this.wifi_access_count = getRouterStatusString(statusString, "wifi_access_count");
		this.battery_charging = getRouterStatusString(statusString, "battery_charging");
		this.battery_value = getRouterStatusString(statusString, "battery_value");
		this.new_message = getRouterStatusString(statusString, "new_message");
		this.message_status = getRouterStatusString(statusString, "message_status");
		this.login_info = getRouterStatusString(statusString, "login_info");
		this.host_phone = getRouterStatusString(statusString, "host_phone");
		this.wan_ipaddr = getRouterStatusString(statusString, "wan_ipaddr");
		this.dns_mode = getRouterStatusString(statusString, "dns_mode");
		this.prefer_dns_manual = getRouterStatusString(statusString, "prefer_dns_manual");
		this.standby_dns_manual = getRouterStatusString(statusString, "standby_dns_manual");
		this.prefer_dns_auto = getRouterStatusString(statusString, "prefer_dns_auto");
		this.standby_dns_auto = getRouterStatusString(statusString, "standby_dns_auto");
		this.SSID1 = getRouterStatusString(statusString, "SSID1");
		this.Channel = getRouterStatusString(statusString, "Channel");
		this.EncrypType = getRouterStatusString(statusString, "EncrypType");
		this.AuthMode = getRouterStatusString(statusString, "AuthMode");
		this.WscModeOption = getRouterStatusString(statusString, "WscModeOption");
		this.AuthMode_tmp = getRouterStatusString(statusString, "AuthMode_tmp");
		this.lan_ipaddr = getRouterStatusString(statusString, "lan_ipaddr");
		this.lan_netmask = getRouterStatusString(statusString, "lan_netmask");
		this.dhcpEnabled = getRouterStatusString(statusString, "dhcpEnabled");
		this.sw_outer_version = getRouterStatusString(statusString, "sw_outer_version");
		this.hardware_version = getRouterStatusString(statusString, "hardware_version");
		this.pin_status = getRouterStatusString(statusString, "pin_status");
		this.lucknum = getRouterStatusString(statusString, "lucknum");
		
		Log.w("eiichi", "ppp_status:" + this.pppStatus + " login_info:" + this.loginInfo + " lucknum:" + this.lucknum);
	}

    private String getRouterStatusString(String statusString, String paramName) {
		BufferedReader br = null;
		String ret = "";
    	try {
    		br = new BufferedReader(new StringReader(statusString));
    		boolean f = true;
    		do {
    			String line = br.readLine();
    			if (line == null) {
    				break;
    			} else {
    				String[] param = line.split(",");
    				for (int i = 0; i < param.length; i ++) {
	    				String[] p = param[i].split(" ");
	    				if (p[0].equals(paramName + ":")) {
	    					ret = p[1].replaceAll("'", "").replaceAll("\\}", "");
	    					f = false;
	    					break;
	    				}
    				}
    			}
    		} while (f);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	} finally {
    		if (br != null) {
    			try {
    				br.close();
    			} catch (Exception ex) {
    			}
    		}
    	}
    	
    	return ret;
    }
}
