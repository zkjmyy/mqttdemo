/**
 * Copyright © 2014 All rights reserved.
 *
 * @Title: MQTTService.java
 * @Prject: CabletechAppStore
 * @Package: com.cabletech.appstore.service
 * @Description: TODO
 * @author: raot  719055805@qq.com
 * @date: 2014�?�?1�?下午4:28:27
 * @version: V1.0
 */
package com.example.mqttclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 
 * @author zkj create
 * @time 2017/12/7 10:24
 */
public class MQTTService extends Service {


    private MqttClient mqttClient;
    //消息服务器的账户和密码
    private String userName = "XXX";
    private String passWord = "XXX";
    private MqttConnectOptions options;
    private MqttAsyncClient mqttAsyncClient;
    private IMqttActionListener iMqttActionListener;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub


//        iMqttActionListener = new IMqttActionListener() {
//            @Override
//            public void onSuccess(IMqttToken iMqttToken) {
//
//            }
//
//            @Override
//            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
//                conectsever();
//            }
//        };


//        ThreadManager.getSinglePool().execute(new Runnable() {
//            @Override
//            public void run() {
                conectsever();
//            }
//        });
    }

    /**
     * 接收消息
     */
    private void conectsever() {
        try {
            mqttAsyncClient = new MqttAsyncClient(Constant.MQTT_SERVERURL, Constant.CLIENT_ID,
                    new MemoryPersistence());

            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 链接时长
            options.setConnectionTimeout(10);
            // 维持心跳次数
            options.setKeepAliveInterval(20);

            mqttAsyncClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
//                        conectsever();
                    try {
                        mqttAsyncClient.reconnect();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    // TODO Auto-generated method stub
                    String str = new String(mqttMessage.getPayload());
                    if (str != null && !str.equals("")) {
                        if (MainActivity.handler != null) {
                            Message msg = new Message();
                            msg.obj = str;
                            msg.what = 0;
                            MainActivity.handler.sendMessage(msg);
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

            mqttAsyncClient.connect(options, this, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    try {
                        mqttAsyncClient.subscribe(Constant.CLIENT_TOPIC, 0, this, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken iMqttToken) {
                                System.out.println("===");
                            }

                            @Override
                            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                                System.out.println("====");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                        try {
                            mqttAsyncClient.reconnect();
                        } catch (MqttException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
//                    Log.e(TAG, "onSuccess: " + iMqttToken.getException().toString() );
                    System.out.println("====");
                    try {
                        mqttAsyncClient.reconnect();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


//        try {
//            mqttClient = new MqttClient(Constant.MQTT_SERVERURL, "test1",
//                    new MemoryPersistence());
//
//            //MQTT的连接设置
//            options = new MqttConnectOptions();
//            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
//            options.setCleanSession(true);
//            //设置连接的用户名
//            options.setUserName(userName);
//            //设置连接的密码
//            options.setPassword(passWord.toCharArray());
//
//            mqttClient.setCallback(new MqttCallback() {
//
//                @Override
//                public void messageArrived(String topicName, MqttMessage message)
//                        throws Exception {
//                    // TODO Auto-generated method stub
//                    String str = new String(message.getPayload());
//                    if (str != null && !str.equals("")) {
//                        if (MainActivity.handler != null) {
//                            Message msg = new Message();
//                            msg.obj = str;
//                            msg.what = 0;
//                            MainActivity.handler.sendMessage(msg);
//                        }
//                    }
//                }
//
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken token) {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @Override
//                public void connectionLost(Throwable cause) {
//                    // TODO Auto-generated method stub
//                    conectsever();
//                }
//            });
//            mqttClient.connect(options);
//            mqttClient.subscribe(Constant.CLIENT_TOPIC);
//        } catch (MqttException e) {
//            // TODO Auto-generated catch block
//            Log.i("Log", e.getMessage());
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            Log.i("Log", e.getMessage());
            e.printStackTrace();
        }
    }
}
