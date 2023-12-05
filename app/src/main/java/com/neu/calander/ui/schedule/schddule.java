package com.neu.calander.ui.schedule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;
import com.neu.calander.Service.SQL_SERVICE;
import com.neu.calander.Pojo.NEUSchedule;
import com.neu.calander.Pojo.User;
import com.neu.calander.R;
import com.neu.calander.Util.tool;

import java.util.ArrayList;
import java.util.List;

public class schddule extends Fragment {
    private static int time = 0;
    private Boolean isClassTablePage = false;
    private Boolean isClassFinalPage = false;
    private SchdduleViewModel mViewModel;
    private WebView webview = null;
    private HorizontalScrollView hsv = null;
    private Boolean isClassLoadSuccess = false;
    private String classDataString = null; //用于存储课程数据字符串，可以将其存储到文件，无需转换
    private static List<NEUSchedule> classArray = null; //课程数组
    private boolean is_using_vpn;
    private Context ct = null;
    private String user_id;
    private int sx;
    private int sy;
    private String user_pw;
    private User user = User.getSingle_instance();
    private TextView textView;
    private boolean has_class = false;
    private static boolean has_load = false;
    private Activity activity;
    int i = 0;
    private  int scrollx;
    String url = null;
    String id = null;
    private  int screenWidth;
    private int screenHeigh;
    private  LinearLayout linearLayout;
    private List<GridLayout> gridLayoutList = new ArrayList();
    private View root;
    @Override
    public void onStart() {
        tool.getSingle_instance().setShow(2);
        super.onStart();
        scrollx=0;
        if(classArray!=null){
            if(has_load){
                add_schddule();
            }
        }else{
                init();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user_id = user.getSchool_id();
        user_pw = user.getSchool_pw();
        tool.getSingle_instance().setShow(2);
        DisplayMetrics dm = new DisplayMetrics();
//获取屏幕信息
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;

        mViewModel = new ViewModelProvider(this).get(SchdduleViewModel.class);
        //設置toobar 格式
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView toolbar_textview = getActivity().findViewById(R.id.toolbar_title);
        toolbar_textview.setVisibility(View.VISIBLE);
        Button button = getActivity().findViewById(R.id.tButton);
        button.setVisibility(View.GONE);

        root = inflater.inflate(R.layout.schddule_fragment, container, false);
        webview = root.findViewById(R.id.Web);
        webview.setVisibility(View.GONE);
        textView = root.findViewById(R.id.textView8);

        hsv = root.findViewById(R.id.scrollview);
        ct = root.getContext();
        activity = getActivity();
        if(!has_load) {
            textView.setText("Loading");
            toolbar_textview.setText("Loading");
            if (!has_class) {
                if (user_id != null && User.getSingle_instance().getName()!=null) {
                    if (!has_load) {
                        init();
                    }
                    has_load = true;
                } else {
                    Toast.makeText(ct, "Please login at first", Toast.LENGTH_SHORT).show();
                }
            }else{

            }
        }

        return root;
    }

    private void init() {
        SQL_SERVICE it = new SQL_SERVICE();
        new Thread() {
            @Override
            public void run() {
                if (it.Is_Use_school_Net()) {
                    is_using_vpn = false;
                    url = "http://219.216.96.4/eams/courseTableForStd.action";
                } else {
                    is_using_vpn = true;
                    url = "https://webvpn.neu.edu.cn/http/77726476706e69737468656265737421a2a618d275613e1e275ec7f8/eams/courseTableForStd.action";
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CookieManager.getInstance().removeAllCookies(value -> {
                        });
                        String ID;
                        final String get_id = "(function (){return window.document.body.outerHTML.match(/(?<=bg\\.form\\.addInput\\(form,\\\"ids\\\",\\\").*?(?=\\\")/)[0]})()";
                        webview.loadUrl(url);
                        webview.getSettings().setJavaScriptEnabled(true);
                        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
                        webview.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                view.loadUrl(request.getUrl().toString());
                                return super.shouldOverrideUrlLoading(view, request);
                            }

                            @Override
                            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                super.onReceivedSslError(view, handler, error);
                                handler.proceed();
                            }

                            @SuppressLint("WrongConstant")
                            @Override
                            public void onPageFinished(WebView view, String surl) {
                                String userinput = null;
                                Log.e("loadPage", surl);
                                String test_login_page = surl.substring(surl.length() - 5, surl.length());
                                Log.e("Page_substring", test_login_page);
                                if (test_login_page.equals("login") || is_using_vpn == false) {
                                    isClassTablePage = true;
                                }
                                ;
                                if (test_login_page.contains("std")) {
                                    i++;
                                    Log.e("Page_i", String.valueOf(i));
                                }
                                if (test_login_page.contains("true") || i == 1) {
                                    if (user_id != null) {
                                        userinput = "(function() {var user = document.getElementById(\"un\");user.value =\""
                                                + user_id + "\";var pw = document.getElementById(\"pd\");pw.value =\""
                                                + user_pw + "\";var button = document.getElementById(\"index_login_btn\");button.click();}());";
                                        Log.e("page_js", userinput);
                                        webview.evaluateJavascript(userinput, null);
                                    }
                                }
                                if (isClassTablePage) {
                                    if (i == 3) {
                                        Log.e("loadPage", "True");
                                        if (id == null) {
                                            webview.evaluateJavascript(get_id, value -> {
                                                String str = value.substring(1, value.length() - 1);
                                                id = str;
                                                Log.e("evaluateJavascript", str);
                                                String str2 = "ignoreHead=1&showPrintAndExport=1&setting.kind=std&startWeek=&semester.id=57&ids=" + str;
                                                if (is_using_vpn) {
                                                    url = "https://webvpn.neu.edu.cn/http/77726476706e69737468656265737421a2a618d275613e1e275ec7f8/eams/courseTableForStd!courseTable.action?" + str2;
                                                } else {
                                                    url = "http://219.216.96.4/eams/courseTableForStd!courseTable.action" + "?" + str2;
                                                }

                                                webview.loadUrl(url);
                                            });
                                            return;
                                        }
                                        isClassTablePage = false;
                                        isClassFinalPage = true;
                                    }
                                }
                                if (!isClassTablePage && isClassFinalPage) {
                                    final String javaScript = "(function scheduleHtmlParser(){let t=document.getE" +
                                            "lementsByTagName(\"html\")[0].innerHTML,e=[];var i=/(?<=var[ \\t]" +
                                            "*actTeachers[ \\t]*=.*name:[ \\t]*\\\").*?(?=\\\")/,n=/(?<=activi" +
                                            "ty[\\t ]*=[ \\t]*new[\\t ]*TaskActivity\\(.*\\\"[0-9A-Za-z]{4,10}" +
                                            "\\([0-9A-Za-z]{4,10}\\)\\\"[\\t ]*,[\\t ]*\\\").*?(?=\\\")/,a=/(?" +
                                            "<=activity[\\t ]*=[\\t ]*new[\\t ]*TaskActivity\\(([^\"]*?\\\"){7" +
                                            "})[^\"]*?(?=\\\")/,c=/(?<=\\\")[0-1]{53}(?=\\\")/,r=/index[\\t ]" +
                                            "*=[\\t ]*[0-6][\\t ]*\\*[\\t ]*unitCount[\\t ]*\\+[\\t ]*[0-9][0" +
                                            "-1]?[\\t ]*;/g,s=/(?<=index[\\t ]*=[\\t ]*)[0-6](?=[\\t ]*\\*)/,h=" +
                                            "/(?<=index[\\t ]*=[\\t ]*.*?unitCount[\\t ]*\\+[\\t ]*)[0-9][0-1" +
                                            "]?/,m=String(t).match(/var[ \\t]*teachers[\\t ]*=[\\t ]*[\\s\\S]*" +
                                            "?(?=table0\\.activities\\[index\\]\\[table0\\.activities\\[index\\" +
                                            "]\\.length\\][ \\t]*=[\\t ]*activity;[\\r\\n\\t ]*[vt])/g);for(l" +
                                            "et t=0;t<m.length;t++){const o=m[t];let u={name:String,position:" +
                                            "String,teacher:String,weeks:[],day:Number,sections:[]};u.name=o.m" +
                                            "atch(n)[0],u.position=o.match(a)[0],u.teacher=o.match(i)[0],u.da" +
                                            "y=Number(o.match(s)[0])+1;let g=o.match(c)[0];for(let t=0;t<g.le" +
                                            "ngth;t++)\"1\"==g[t]&&u.weeks.push(t);var l=o.match(r);for(let t" +
                                            "=0;t<l.length;t++){const e=l[t];u.sections.push(Number(e.match(h)" +
                                            "[0])+1)}e.push(u)}return JSON.stringify(e)})()";

                                    webview.evaluateJavascript(javaScript, value -> {
                                        classDataString = value.substring(1, value.length() - 1).replace("\\", "");
                                        try {
                                            //Log.e("NO_ERROR", classDataString);
                                            classArray = JSONObject.parseArray(classDataString, NEUSchedule.class);
                                        } catch (Exception e) {
                                            Log.e("ERROR_from_CLASS", e.getLocalizedMessage());
                                            //Log.e("ERROR_FROM_CLASS", classDataString);
                                            has_load = false;
                                            textView.setText("找不到課表");
                                            return;
                                        }
                                        if (classArray != null) {
                                            isClassLoadSuccess = true;
                                            add_schddule();
                                            for(NEUSchedule neuSchedule:classArray){
                                                Log.e("NEUS",neuSchedule.toString() );
                                            }
                                        } else {
                                            isClassLoadSuccess = false;
                                            Toast.makeText(getActivity(), "我找不到課表啊2", Toast.LENGTH_SHORT).show();
                                            has_load = false;
                                            return;
                                        }
                                    });
                                    //----------
                                    i = 0;
                                    isClassTablePage = false;
                                    isClassFinalPage = false;


                                }
                            }
                        });

                    }
                });
            }
        }.start();
    }
    private void add_schddule(){
        if(tool.getSingle_instance().getShow()!=2){
            return;
        }
        hsv.removeAllViews();
        String[] week = {"","周一", "周二", "周三", "周四", "周五", "周六", "周日"};;
        String[] count = {"一","二","三","四","五","六","七","八"};
        String[] curson_time = {"08:30~09:20","09:30~10:20","10:40~11:30","11:40~12:30","14:00~14:50","15:00~15:50","16:10~17:00","17:10~18:00"};
        linearLayout = new LinearLayout(ct);
        hsv.addView(linearLayout);
        ViewGroup.LayoutParams lpout = linearLayout.getLayoutParams();
        lpout.width = screenWidth;
        lpout.height = screenHeigh;
        hsv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        sx = (int) motionEvent.getRawX();
                        sy = (int) motionEvent.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        int x = (int)motionEvent.getRawX();
                        int y = (int)motionEvent.getRawY();
                        if(scrollx<screenWidth*20) {
                            //向右
                            if (sx - x >= screenWidth / 10) {
                                if(scrollx<screenWidth*19) {
                                    scrollx += screenWidth;
                                    hsv.smoothScrollTo(scrollx, 0);
                                    textView.setText("第" + String.valueOf((scrollx / screenWidth) + 1) + "周");
                                    break;
                                }
                            }
                            if (x - sx >= screenWidth / 10) {
                                //向左滑
                                if(scrollx>0) {
                                    scrollx -= screenWidth;
                                    hsv.smoothScrollTo(scrollx, 0);
                                    textView.setText("第" + String.valueOf((scrollx / screenWidth)+1) + "周");
                                    break;
                                }
                            }
                        }

                    }
                }
                return true;
            }
        });

        for (int i = 0; i < 20; i++) {

            GridLayout gridLayout = new GridLayout(ct);
            linearLayout.addView(gridLayout);
            ViewGroup.LayoutParams gridLayoutLayoutParams = gridLayout.getLayoutParams();
            gridLayoutLayoutParams.width = screenWidth-20;
            gridLayoutLayoutParams.height = (int) Math.ceil(screenHeigh*0.7);
            gridLayout.setBackgroundColor(Color.parseColor("#EEEEEE"));
            LinearLayout.LayoutParams gra = (LinearLayout.LayoutParams) gridLayout.getLayoutParams();
            gra.setMargins(10,0,10,0);
            gridLayout.setColumnCount(8);
            gridLayout.setRowCount(9);
        for(int f = 0;f<week.length;f++){
                TextView textView = new TextView(ct);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height =0;
                params.rowSpec =  GridLayout.spec(0,1,1f);
                params.columnSpec = GridLayout.spec(f,1,1f);
                textView.setGravity(Gravity.CENTER);
                    textView.setText(week[f]);
                textView.setBackgroundResource(R.drawable.schd_week_time);
                 gridLayout.addView(textView,params);

        }

        for(int j = 0;j<8;j++){
            boolean flag= false;
            for(int k = 1;k<9;k++){
                if(flag){flag=false;continue;}
                if(j==0){
                    TextView textView = new TextView(ct);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 0;
                    params.height =0;
                    params.rowSpec =  GridLayout.spec(k,1,1f);
                    params.columnSpec = GridLayout.spec(j,1,1f);
                    textView.setText(count[k-1]);
                    textView.setGravity(Gravity.CENTER);
                    gridLayout.addView(textView,params);
                    textView.setBackgroundResource(R.drawable.schd_week_time);
                }else {
                        for(NEUSchedule scd: classArray){
                            for(int wk:scd.getWeeks()){
                                if(wk==i+1){
                                    if(scd.getDay()==j){
                                        int column = scd.getSections().get(0);
                                        if(column==k) {
                                            TextView textView = new TextView(ct);
                                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                            params.width = 0;
                                            params.height = 0;
                                            params.rowSpec = GridLayout.spec(k, 2, 1f);
                                            params.columnSpec = GridLayout.spec(j, 1, 1f);
                                            String text = scd.getName();
                                            String[] token = text.split("\\u0028");
                                            String position = scd.getPosition();
                                            String[] token2 = position.split("\\u0028");
                                            if(!position.equals("")) {
                                                String settext = token[0] + "\n(" + token2[0] + ")";
                                                textView.setText(settext);
                                            }else{
                                                textView.setText(token[0]);
                                            }
                                            textView.setGravity(Gravity.CENTER);
                                            gridLayout.addView(textView, params);
                                            textView.setBackgroundResource(R.drawable.class_schddule);
                                            flag = true;
                                        }
                                    }
                                }
                                if(flag == true){
                                    break;
                                }
                            }
                            if(flag==true){
                                break;
                            }
                        }
                        if(flag){continue;}
                    TextView textView = new TextView(ct);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 0;
                    params.height = 0;
                    params.rowSpec = GridLayout.spec(k, 1, 1f);
                    params.columnSpec = GridLayout.spec(j, 1, 1f);
                    textView.setText("");
                    textView.setGravity(Gravity.CENTER);
                    gridLayout.addView(textView, params);
                    textView.setBackgroundResource(R.drawable.schd_week_time);
                }
            }
        }
        gridLayoutList.add(gridLayout);
        }
        textView.setText("第1周");
        TextView toolbar_textview = getActivity().findViewById(R.id.toolbar_title);
        toolbar_textview.setText("");
        if(time==0){
            time++;
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.nav_host_fragment,new schddule()).commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}