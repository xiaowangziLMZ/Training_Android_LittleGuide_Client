package com.tsoiay.littleguide;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.tsoiay.littleguide.db.City;
import com.tsoiay.littleguide.db.County;
import com.tsoiay.littleguide.db.Province;
import com.tsoiay.littleguide.util.HttpUtil;
import com.tsoiay.littleguide.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;


    private ProgressDialog progressDialog;

    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String>adapter;
    private List<String> dataList =new ArrayList <> (  );
    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City>cityList;
    /**
     * 县列表
     */
    private List<County>countyList;
    /**
     * 选中的省份
     */
    private Province selecedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     *
     * 当前选中的级别
     */
    private int currentLevel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate ( R.layout.choose_area,container,false );
        titleText=(TextView)view.findViewById ( R.id.title_text );
        listView=(ListView)view.findViewById ( R.id.list_view );
        backButton=(Button)view.findViewById ( R.id.back_back_button );
        adapter= new ArrayAdapter <> (getContext (),android.R.layout.simple_list_item_1 ,dataList);
        listView.setAdapter ( adapter );
        return  view;
    }
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated ( saveInstanceState );
        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selecedProvince=provinceList.get(position);
                    queryCites ();
                }else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get ( position );
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY){
                    String weatherId= countyList.get ( position ).getWeatherId ();
                    if(getActivity () instanceof newWeather ){
                        Intent intent = new Intent ( getActivity (), WeatherActivity.class );
                        intent.putExtra ( "weather_id", weatherId );
                        startActivity ( intent );
                        getActivity ().finish ();
                    } else if (getActivity () instanceof WeatherActivity) {
                        WeatherActivity activity =(WeatherActivity)getActivity();
                        activity.drawerLayout.closeDrawers ();
                        activity.swipeRefresh.setRefreshing ( true );
                        activity.requestWeather ( weatherId );

                    }
                    }
                }

        } );
        backButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCites ();
                }else if(currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        } );
        queryProvinces();
    }
    /**
     * 查询全国所有省，优先在数据库中查询，如果没有再去服务器中查询
     */
    private  void queryProvinces(){
        titleText.setText ( "中国" );
        backButton.setVisibility ( View.GONE );
        provinceList= DataSupport.findAll ( Province.class );
        if(provinceList.size ()>0){
            dataList.clear ();
            for (Province province:provinceList){
                dataList.add ( province.getProvinceName () );
            }
            adapter.notifyDataSetChanged ();
            listView.setSelection ( 0 );
            currentLevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }
    /**
     *查询选中的省内所有的市，优先从数据库查询，如果没有查询到再去服务器查询
     */
    private void queryCites() {
        titleText.setText ( selecedProvince.getProvinceName () );
        backButton.setVisibility ( View.VISIBLE );
        cityList = DataSupport.where ( "provinceid = ?", String.valueOf ( selecedProvince.getId () ) ).find ( City.class );
        if (cityList.size () > 0) {
            dataList.clear ();
            for (City city : cityList) {
                dataList.add ( city.getCityName () );
            }
            adapter.notifyDataSetChanged ();
            listView.setSelection ( 0 );
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selecedProvince.getProvinceCode ();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer ( address, "city" );
        }
        /**
         * 查询选中的市内所有的县，优先从数据库查询，如果没有查询到再去服务器查询
         *
         */
    }
       private void queryCounties(){
           titleText.setText ( selectedCity.getCityName() );
           backButton.setVisibility ( View.VISIBLE );
           countyList= DataSupport.where ( "cityid = ?",String.valueOf(selectedCity.getId ()) ).find( County.class);
           if (countyList.size ()>0){
               dataList.clear ();
               for (County county:countyList){
                   dataList.add ( county.getCountyName () );
               }
               adapter.notifyDataSetChanged ();
               listView.setSelection ( 0 );
               currentLevel=LEVEL_COUNTY;
           }else {
               int provinceCode=selecedProvince.getProvinceCode ();
               int cityCode=selectedCity.getCityCode ();
               String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
               queryFromServer(address,"county");
           }

        }
    /**
     * 根据传入的地址类型从服务器上查询省市县数据
     */
    private void queryFromServer(String address,final  String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest ( address, new Callback () {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity ().runOnUiThread ( new Runnable () {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText ( getContext(),"加载失败",Toast.LENGTH_SHORT).show ();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            String responseText=response.body ().string ();
            boolean result=false;
            if ("province".equals ( type )){
                result= Utility.handleProvinceResponse ( responseText );
            }else if("city".equals ( type )){
                result=Utility.handleCityResponse ( responseText,selecedProvince.getId () );

            }else if("county".equals ( type )){
                result=Utility.handleCountyResponse (responseText,selectedCity.getId ());
            }
            if(result){
                getActivity ().runOnUiThread ( new Runnable () {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if("province".equals ( type )){
                            queryProvinces ();
                        }else if("city".equals ( type )){
                            queryCites ();
                        }else if("county".equals ( type )){
                            queryCounties ();
                        }

                    }
                } );
            }
            }

        } );

    }

    /**
     * 打开对话框
     *
     */
    private void showProgressDialog(){
        if(progressDialog ==null){
            progressDialog=new ProgressDialog ( getActivity () );
            progressDialog.setMessage ( "正在加载。。。" );
            progressDialog.setCanceledOnTouchOutside ( false );
        }
        progressDialog.show ();
    }

/**
 *
 * 关闭对话框
 */
    private void closeProgressDialog(){
        if(progressDialog !=null){
            progressDialog.dismiss ();
        }
    }


}

