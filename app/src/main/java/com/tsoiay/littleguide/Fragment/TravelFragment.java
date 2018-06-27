package com.tsoiay.littleguide.Fragment;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsoiay.littleguide.R;


public class TravelFragment extends Fragment{
    private Context mContext;
    private Button btn_back = null;
    private Button btn_edit = null;
    private TextView tv_title = null;
    private ImageView baidu;
    private ImageView weibo;
    private ImageView youku;
    private ImageView taobao;
    private ImageView toutiao;
    private ImageView jingdong;
    private ImageView xiecheng;
    private ImageView fenghuang;
    private ImageView tongcheng;
    private ImageView bilibili;
    private ImageView iqiyi;
    private ImageView anjuke;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.travel_fragment,container,false);
        btn_back = (Button)view.findViewById(R.id.btnBack);
        btn_edit = (Button)view.findViewById(R.id.btnEdit);
        tv_title = (TextView)view.findViewById(R.id.tv_title);
        baidu = (ImageView) view.findViewById(R.id.baidu);
        weibo = (ImageView) view.findViewById(R.id.weibo);
        youku = (ImageView) view.findViewById(R.id.youku);
        taobao = (ImageView) view.findViewById(R.id.taobao);
        toutiao = (ImageView) view.findViewById(R.id.toutiao);
        jingdong = (ImageView) view.findViewById(R.id.jingdong);
        xiecheng = (ImageView) view.findViewById(R.id.xiecheng);
        fenghuang = (ImageView) view.findViewById(R.id.fenghuang);
        tongcheng = (ImageView) view.findViewById(R.id.tongcheng);
        bilibili = (ImageView) view.findViewById(R.id.bilibili);
        iqiyi = (ImageView) view.findViewById(R.id.iqiyi);
        anjuke = (ImageView) view.findViewById(R.id.anjuke);

        InitTitleBar();

        baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.weibo.com"));
                startActivity(intent);
            }
        });
        youku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.youku.com"));
                startActivity(intent);
            }
        });
        taobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.taobao.com"));
                startActivity(intent);
            }
        });
        jingdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.jingdong.com"));
                startActivity(intent);
            }
        });
        toutiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.toutiao.com"));
                startActivity(intent);
            }
        });
        xiecheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.xiecheng.com"));
                startActivity(intent);
            }
        });
        fenghuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.ifeng.com"));
                startActivity(intent);
            }
        });
        tongcheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.58.com"));
                startActivity(intent);
            }
        });
        iqiyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.iqiyi.com"));
                startActivity(intent);
            }
        });
        bilibili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.bilibili.com"));
                startActivity(intent);
            }
        });
        anjuke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.anjuke.com"));
                startActivity(intent);
            }
        });

        return view;
    }


    private void InitTitleBar(){
        btn_back.setVisibility(View.INVISIBLE);
        btn_edit.setVisibility(View.INVISIBLE);
        tv_title.setText("小导航");
    }
}
