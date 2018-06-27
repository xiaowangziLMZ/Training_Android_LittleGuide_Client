package com.tsoiay.littleguide.Layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

import com.tsoiay.littleguide.Fragment.UserFragment;
import com.tsoiay.littleguide.R;

public class IdentifyActivity extends AppCompatActivity {
    private Button btn1, btn2;
    private EditText edt1, edt2;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identify_layout);

        btn1 = (Button) findViewById(R.id.send);
        btn2 = (Button) findViewById(R.id.verify);
        edt1 = (EditText) findViewById(R.id.phone);
        edt2 = (EditText) findViewById(R.id.verification);


        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_SHORT).show();
                //SMSSDK.getVerificationCode("1", "4432487596");
                SMSSDK.getVerificationCode("86", edt1.getText().toString());
                phone = edt1.getText().toString();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.submitVerificationCode("86", phone, edt2.getText().toString());
            }
        });
    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result" + event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(IdentifyActivity.this, UserFragment.class));
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //已经验证
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //已经验证
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
//                    textV.setText(data.toString());
                }

            } else {
//              ((Throwable) data).printStackTrace();
//              Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//                  Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(IdentifyActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
        }
    };

//    //将资源的图片保存到本地图片
//    public static void savePic(Context context) {
//        File pic = new File("/sdcard/logo_bluetooth.png");
//        if (!pic.exists()) {
//            try {
//                //把资源文件转成bitmap
//                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//
//                //再转成字节数组
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                int i = 100;
//                bitmap.compress(Bitmap.CompressFormat.PNG, i, out);
//                byte[] array = out.toByteArray();
//
//                //最后通过流在保存
//                FileOutputStream fos = new FileOutputStream(pic);
//                fos.write(array);
//                fos.close();
//            } catch (FileNotFoundException e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
//
//        }
//    }

    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}