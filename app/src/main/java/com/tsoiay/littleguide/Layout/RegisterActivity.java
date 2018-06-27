package com.tsoiay.littleguide.Layout;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;

import com.tsoiay.littleguide.R;
import com.tsoiay.littleguide.Web.WebServicePost;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private EditText regUserName;
    private EditText regPassWord;
    private Button btn_reg;

    ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        regUserName = (EditText)findViewById(R.id.reg_username);
        regPassWord = (EditText)findViewById(R.id.reg_password);
        btn_reg = (Button)findViewById(R.id.register);
        back = findViewById(R.id.img_back);
        InitTitleBar();

        back.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                dialog = new ProgressDialog(RegisterActivity.this);
                dialog.setTitle("正在注册");
                dialog.setMessage("请稍后");
                dialog.show();

                new Thread(new RegThread()).start();
                break;
        }
    }

    public class RegThread implements Runnable{
        @Override
        public void run() {

            //获取服务器返回数据
            String RegRet = WebServicePost.executeHttpPost(regUserName.getText().toString(),regPassWord.getText().toString(),"RegLet");

            //更新UI，界面处理
            showReq(RegRet);
        }
    }

    private void showReq(final String RegRet){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(RegRet.equals("true")){
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("请验证手机号");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(RegisterActivity.this,IdentifyActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }else{
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册失败");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    private void InitTitleBar(){
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
    }

    public void back(){
        new Thread(){
            @Override
            public void run() {
                Instrumentation ins = new Instrumentation();
                ins.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }.start();
    }
}