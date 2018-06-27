package com.tsoiay.littleguide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tsoiay.littleguide.Fragment.IndexFragment;
import com.tsoiay.littleguide.Fragment.SocialFragment;
import com.tsoiay.littleguide.Fragment.TravelFragment;
import com.tsoiay.littleguide.Fragment.UserFragment;

public class MainActivity extends AppCompatActivity {
    private android.support.v4.app.FragmentManager manager;
    private ImageView icon_index;
    private ImageView icon_social;
    private ImageView icon_travel;
    private ImageView icon_user;

    private Fragment indexFragment;
    private Fragment socialFragment;
    private Fragment userFragment;
    private Fragment tavelFragment;

    private Fragment currentFragment = new Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        getViews();

        InitTitleBar();

        OnFocusChangeListenerImpl listener = new OnFocusChangeListenerImpl();

        icon_index.setOnFocusChangeListener(listener);
        icon_social.setOnFocusChangeListener(listener);
        icon_travel.setOnFocusChangeListener(listener);
        icon_user.setOnFocusChangeListener(listener);

        indexFragment = new IndexFragment();
        socialFragment = new SocialFragment();
        userFragment = new UserFragment();
        tavelFragment = new TravelFragment();


        /*
         * 代
         * 码
         * 区
         * */

    }

    private void getViews(){
        icon_index = findViewById(R.id.index_ico);
        icon_social = findViewById(R.id.social_ico);
        icon_travel = findViewById(R.id.travel_ico);
        icon_user = findViewById(R.id.user_ico);
    }

    private void changeFragment(android.support.v4.app.Fragment fragment){
        //借助于FragmentManger和FragmentTransaction
        //首先判断FragmentManager是否为空
        if(null == manager){
            manager = getSupportFragmentManager();
        }
        //切换页面,每次切换页面，进行一次页面切换事务
        if(currentFragment != fragment){//如果当前显示的页面和目标要显示的页面不同
            //创建事务
            FragmentTransaction transaction = manager.beginTransaction();
            //隐藏当前页面
            transaction.hide(currentFragment);
            //判断待显示的页面是否已经添加过
            if(!fragment.isAdded()){//待显示的页面没有被添加过
                transaction.add(R.id.fl_content, fragment);
            }
            //显示目标页面
            transaction.show(fragment);
            //提交事务
            transaction.commit();
            //更改当前页面
            currentFragment = fragment;
        }
    }

    private class OnFocusChangeListenerImpl implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                case R.id.index_ico:
                    changeFragment(indexFragment);
                    break;
                case R.id.social_ico:
                    changeFragment(socialFragment);
                    break;
                case R.id.travel_ico:
                    changeFragment(tavelFragment);
                    break;
                case R.id.user_ico:
                    changeFragment(userFragment);
                    break;
            }
        }
    }

    private void InitTitleBar(){
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
    }

}
