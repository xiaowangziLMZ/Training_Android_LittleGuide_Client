package com.tsoiay.littleguide.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tsoiay.littleguide.GlideImageLoader;
import com.tsoiay.littleguide.News;
import com.tsoiay.littleguide.NewsAdapter;
import com.tsoiay.littleguide.NewsDisplayActivity;
import com.tsoiay.littleguide.R;
import com.youth.banner.Banner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class IndexFragment extends Fragment {
    private Context mContext;
    private Button btn_back = null;
    private Button btn_edit = null;
    private TextView tv_title = null;
    private List<News> newsList;
    private NewsAdapter adapter;
    private Handler handler;
    private Banner banner;
    private ListView lv;
    private View  view;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.index_fragment,container,false);
        btn_back = (Button)view.findViewById(R.id.btnBack);
        btn_edit = (Button)view.findViewById(R.id.btnEdit);
        tv_title = (TextView)view.findViewById(R.id.tv_title);
        InitTitleBar();
        /*
         * 代
         * 码
         * 区
         * */
        newsList = new ArrayList<>();
        lv =view.findViewById(android.R.id.list);
        Banner banner =view.findViewById( R.id.banner );
        banner.setImageLoader( new GlideImageLoader () );

        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add("https://image2.thepaper.cn/image/8/478/779.jpg");
        list_path.add("https://image2.thepaper.cn/image/8/474/216.jpg");
        list_path.add("https://image2.thepaper.cn/image/8/480/171.jpg");
        list_path.add("https://image2.thepaper.cn/image/8/480/78.jpg");

        list_title.add("");
        list_title.add("");
        list_title.add("");
        list_title.add("");
        banner.setImages(list_path);
        banner.start();
        getNews();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    adapter = new NewsAdapter(mContext,newsList);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News news = newsList.get(position);
                            Intent intent = new Intent();
                            intent.setClass( getActivity(),NewsDisplayActivity.class );
                            intent.putExtra("news_url",news.getNewsUrl());
                            startActivity(intent);
                        }
                    });


                }
            }
        };
        return view;
    }
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
    }

    private void InitTitleBar(){
        btn_back.setVisibility(View.INVISIBLE);
        btn_edit.setVisibility(View.INVISIBLE);
        tv_title.setText("小导航");
    }
    private void getNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i = 1;i<=2;i++) {
                        Document doc = Jsoup.connect( "http://news.qq.com/" ).get ();
                        Elements titleLinks = doc.select( "div,wrap" ).select( "div,container" ).select( "div.main" ).select( "div.head" ).select( "div.Q-tpList" ).select( "div.Q-tpWrap" ).select( "div.text" );    //解析来获取每条新闻的标题与链接地址
                        Elements imgLinks = doc.select( "div,wrap" ).select( "div,container" ).select( "div.main" ).select( "div.head" ).select( "div.Q-tpList" ).select( "div.Q-tpWrap" ); //解析获取的图片url
                        for(int j = 0;j < titleLinks.size();j++) {
                            String title
                                    = titleLinks.get ( j ).select ( "em" ).select ( "a" ).text ();
                            String uri
                                    = titleLinks.get ( j).select ( "em" ).select ( "a" ).attr ( "href" );
                            String img
                                    = imgLinks.get ( j ).select ( "a" ).select ( "img" ).first ().absUrl ( "src" );
                            Uri uri1 = Uri.parse ( (String) img );
                            String time = "腾讯新闻";
                            Bitmap bitmap = null;
                            try {
                                URL myFileUrl = new URL ( img );
                                HttpURLConnection conn = (HttpURLConnection) myFileUrl
                                        .openConnection ();
                                conn.setDoInput ( true );
                                conn.connect ();
                                InputStream is = conn.getInputStream ();
                                bitmap = BitmapFactory.decodeStream ( is );
                                is.close ();

                                Log.v ( TAG, "image download finished." + img );
                            } catch (OutOfMemoryError e) {
                                e.printStackTrace ();
                                bitmap = null;
                            } catch (IOException e) {
                                e.printStackTrace ();
                                Log.v ( TAG, "getbitmap bmp fail---" );
                                bitmap = null;

                            }
                            News news = new News ( title, uri, time, bitmap );
                            newsList.add ( news );


                        }
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    Log.e( "nnn", e.toString() );
                    e.printStackTrace();
                }
            }
        }).start();             }

}
