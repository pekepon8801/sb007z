package jp.or.juice.pugly.SB007z;

import java.io.BufferedReader;
import java.io.StringReader;
import android.util.Log;

public class RouterStatus {
	public boolean pppStatus = false;
	public boolean loginInfo = false;
	public String lucknum = "";
	
	public RouterStatus(String statusString) {
		String ps = getRouterStatusString(statusString, "ppp_status");
		this.pppStatus = ps.equals("ppp_connected") || ps.equals("ppp_connecting");
		String li = getRouterStatusString(statusString, "login_info");
		this.loginInfo = li.equals("ok");
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
