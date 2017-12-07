/**   
 * Copyright © 2014 All rights reserved.
 * 
 * @Title: MQTTUtils.java 
 * @Prject: CabletechAppStore
 * @Package: com.cabletech.appstore.utils 
 * @Description: TODO
 * @author: raot  719055805@qq.com
 * @date: 2014年9月12日 下午4:32:08 
 * @version: V1.0   
 */
package com.example.mqttclient;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
/**
 * 
 * @author zkj create
 * @time 2017/12/7 10:27
 */
public class MQTTUtils {
	
	public static boolean serviceIsRunning(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.cabletech.appstore.service.MQTTService"
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static void startBlackIceService(Context context) {
		if (!serviceIsRunning(context)) {
			final Intent intent = new Intent(context, MQTTService.class);
			context.startService(intent);
		}
	}

	public static void stopBlackIceService(Context context) {
		if (serviceIsRunning(context)) {
			final Intent intent = new Intent(context, MQTTService.class);
			context.stopService(intent);
		}
	}
}
