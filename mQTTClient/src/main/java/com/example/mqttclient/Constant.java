/**   
 * Copyright © 2014 All rights reserved.
 * 
 * @Title: Constant.java 
 * @Prject: MQTTClient
 * @Package: com.example.mqttclient 
 * @Description: TODO
 * @author: raot  719055805@qq.com
 * @date: 2014年9月15日 上午11:33:33 
 * @version: V1.0   
 */
package com.example.mqttclient;

/**
 *
 * @author zkj create
 * @time 2017/12/7 10:24
 */
public class Constant {

	public static String MQTT_SERVERURL = "tcp://172.16.100.75:61613";
	
	/***************************用户二安装的时候注释掉************************************/
//	public static String CLIENT_ID = "test1";
//	public static String CLIENT_TOPIC = "test2";
//
//	public static String SERVER_ID = "test2";
//	public static String SERVER_TOPIC = "test1";
	
	/***************************用户一安装的时候注释掉***********************************/
//	public static String CLIENT_ID = "用户二";
	public static String CLIENT_TOPIC = "user2";

//	public static String SERVER_ID = "用户一";
	public static String SERVER_TOPIC = "user1";


	public static String CLIENT_ID = "two";
	
}
