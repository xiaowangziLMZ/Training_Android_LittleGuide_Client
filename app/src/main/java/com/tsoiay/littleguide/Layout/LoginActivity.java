package com.tsoiay.littleguide.Layout;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsoiay.littleguide.Fragment.UserFragment;
import com.tsoiay.littleguide.R;
import com.tsoiay.littleguide.Web.WebServiceGet;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private ProgressDialog dialog;
    private String infoString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        back = findViewById(R.id.img_back);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.goregister);
        InitTitleBar();

        back.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.goregister:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                if(username.getText().toString().length() == 0 || password.getText().toString().length() == 0){
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setTitle("正在登陆");
                    dialog.setMessage("请稍后");
                    dialog.setCancelable(true);//设置可以通过back键取消
                    dialog.show();
                    //设置子线程，分别进行Get和Post传输数据
                    new Thread(new MyThread()).start();
                }
                break;
            case R.id.img_back:
                back();
                break;
        }
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            infoString = WebServiceGet.executeHttpGet(username.getText().toString(),password.getText().toString(),"LogLet");//获取服务器返回的数据
            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {
                if(response.equals("false")){
                    Toast.makeText(LoginActivity.this,"用户名或密码错误", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(LoginActivity.this,UserFragment.class);
                    startActivity(intent);
                }
                dialog.dismiss();
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
