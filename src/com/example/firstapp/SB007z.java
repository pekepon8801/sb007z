package com.example.firstapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class SB007z {

	public RouterStatus doGetRouterStatus() throws IOException {
		Log.w("eiichi", "doGetRouterStatus");
		String[] param = new String[1];
    	param[0] = "http://192.168.3.1/login_data.asp";
    	return new RouterStatus(requestTo007z(param));
	}
	
	public String doLoginToRouter() throws IOException {
		Log.w("eiichi", "doLoginToRouter");
		String[] param = new String[2];
    	param[0] = "http://192.168.3.1/goform/goform_process";
    	param[1] = "goformId=LOGIN&lucknum=673627&systemDate=&save_login=0&languageSelect=japanese&user=admin&psw=admin";
    	return requestTo007z(param);
	}
	
	public String doConnectRouterPPP(String lucknum) throws IOException {
		Log.w("eiichi", "doConnectRouterPPP");
    	String[] param = new String[2];
    	param[0] = "http://192.168.3.1/goform/goform_process";
    	param[1] = "goformId=NET_CONNECT&dial_mode=auto_dial&action=connect&Submit_linkset=%E9%81%A9%E7%94%A8&lucknum_NET_CONNECT=" + lucknum;
    	return requestTo007z(param);
	}
	
    private String requestTo007z(String[] requestParam) throws IOException {
        BufferedReader input = null;
        PrintStream ps = null;
        String data = "";
    	try {
    		Log.w("eiichi", requestParam[0]);
    		URL url = new URL(requestParam[0]);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            if (requestParam.length == 2) {
        		Log.w("eiichi", requestParam[1]);
	            connection.setDoOutput(true);
	            ps = new PrintStream(connection.getOutputStream());
	            ps.print(requestParam[1]);
	            ps.flush();
            }
            connection.setConnectTimeout(1000);
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String tmp = "";
            while ((tmp = input.readLine()) != null) {
                data += tmp;
            }
    	} catch (IOException ioex) {
    		throw ioex;
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	} finally {
    		try {
    			if (input != null) {
    				input.close();
    			}
    			if (ps != null) {
    				ps.close();
    			}
    		} catch (Exception ex) {
    		}
    	}
//    	Log.w("eiichi", data);
    	return data;
    }
}
