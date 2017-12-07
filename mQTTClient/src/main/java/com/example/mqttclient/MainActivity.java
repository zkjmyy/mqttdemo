package com.example.mqttclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends Activity {

    private EditText editText;
    private TextView textView;
    private Button button;
    private String userName = "zhangkj";
    private String passWord = "zhangkj";
    private MqttConnectOptions options;
    public static Handler handler;
    private MqttAsyncClient mqttAsyncClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.eidt_text);
        textView = (TextView) findViewById(R.id.text_view);
        button = (Button) findViewById(R.id.send_button);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                textView.setText(textView.getText().toString() + "\n"
                        + "我接受到得   " + "：" + msg.obj);

                Toast.makeText(getBaseContext(), "" + msg.obj, Toast.LENGTH_SHORT).show();
            }
        };


        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String msg = editText.getText().toString();
                if (msg == null || msg.equals("")) {
                    Toast.makeText(MainActivity.this, "输入内容不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessage(msg);
            }
        });
        MQTTUtils.startBlackIceService(this);
    }


    /**
     * 发送消息
     *
     * @param msg
     */
    private void sendMessage(final String msg) {
        try {
            if (mqttAsyncClient == null) {
                mqttAsyncClient = new MqttAsyncClient(Constant.MQTT_SERVERURL,
                        MqttClient.generateClientId(),
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
                mqttAsyncClient.connect(options, this, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
//                    Log.e(TAG, "onSuccess: " + iMqttToken.getException().toString());
                        try {
                            mqttAsyncClient.publish(Constant.SERVER_TOPIC, msg.getBytes(), 0, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(textView.getText().toString() + "\n"
                                        + "===>本地增加---" + "我发送得  " + "：" + msg);
                                editText.setText("");
                            }
                        });
//                    MqttMessage message = new MqttMessage(msg.getBytes());
//                    try {
//                        mqttAsyncClient.publish(Constant.SERVER_TOPIC, message, getBaseContext(), new IMqttActionListener() {
//                            @Override
//                            public void onSuccess(IMqttToken iMqttToken) {
////                                Log.e(TAG, "onSuccess: " + iMqttToken.getTopics().toString());
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        textView.setText(textView.getText().toString() + "\n"
//                                                + "===>本地增加---" + "我发送得  " + "：" + msg);
//                                        editText.setText("");
//                                    }
//                                });
//
//                            }
//
//                            @Override
//                            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
//                                sendMessage(msg);
////                                Log.e(TAG, "onSuccess: " + iMqttToken.getException().toString());
//                            }
//                        });
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    }

                    }

                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
//                    Log.e(TAG, "onSuccess: " + iMqttToken.getException().toString());
                    }
                });
            } else {
                if (mqttAsyncClient.isConnected()) {
                    try {
                        mqttAsyncClient.publish(Constant.SERVER_TOPIC, msg.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(textView.getText().toString() + "\n"
                                    + "===>本地增加---" + "我发送得  " + "：" + msg);
                            editText.setText("");
                        }
                    });
                } else {
                    mqttAsyncClient.reconnect();
                    try {
                        mqttAsyncClient.publish(Constant.SERVER_TOPIC, msg.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(textView.getText().toString() + "\n"
                                    + "===>本地增加---" + "我发送得  " + "：" + msg);
                            editText.setText("");
                        }
                    });
                }
            }

//			} else {
//				MqttMessage message = new MqttMessage(msg.getBytes());
//				mqttAsyncClient.publish(Constant.SERVER_TOPIC, message, getBaseContext(), new IMqttActionListener() {
//					@Override
//					public void onSuccess(IMqttToken iMqttToken) {
//						Log.e(TAG, "onSuccess: " + iMqttToken.toString());
//					}
//
//					@Override
//					public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
//						sendMessage(msg);
//						Log.e(TAG, "onFailure: " + iMqttToken.toString() );
//					}
//				});
//			}

//
//					MqttClient client = new MqttClient(Constant.MQTT_SERVERURL,
//							MqttClient.generateClientId(),
//							new MemoryPersistence());
//
//					//MQTT的连接设置
//					options = new MqttConnectOptions();
//					//设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
//					options.setCleanSession(true);
//					//设置连接的用户名
//					options.setUserName(userName);
//					//设置连接的密码
//					options.setPassword(passWord.toCharArray());
//
//					client.connect(options);
//					MqttTopic temperatureTopic = client
//							.getTopic(Constant.SERVER_TOPIC);
//					MqttMessage message = new MqttMessage(msg.getBytes());
//					temperatureTopic.publish(message);
//					client.disconnect();
//					textView.setText(textView.getText().toString() + "\n"
//							+ "===>本地增加---" + "我发送得" + "：" + msg);
//					editText.setText("");
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (mqttAsyncClient != null) {
//            try {
//                mqttAsyncClient.disconnect(0);
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
