package com.example.yizu;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.adapter.RecommendAdapter;
import com.example.yizu.bean.Goods;
import com.example.yizu.bean.ShareModel;
import com.example.yizu.bean.User;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.yizu.control.ChangeAddressPopwindow;
import com.example.yizu.db.HistoryRecord;
import com.example.yizu.service.StateChangeService;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.PictureTool;
import com.example.yizu.tool.ShareStorage;
import com.example.yizu.tool.ShowDialog;
import com.example.yizu.tool.ToastShow;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaosu.DataSetAdapter;
import com.xiaosu.VerticalRollingTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import de.hdodenhof.circleimageview.CircleImageView;

import cn.sharesdk.framework.PlatformActionListener;



import android.os.Handler.Callback;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.zackratos.ultimatebar.UltimateBar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,PlatformActionListener,Callback {

    private static  final String text = "这是YI租的下载地址";
    private static  final String imageurl = "http://139.199.224.112/logo.png";
    private static  final String title = "江苏华漫";
    private static  final String url = "http://139.199.224.112/YI.apk";
    public static final String SHARE_APP_KEY = "21b0f35691b8";
    private SharePopupWindow share;

    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;
    private CircleImageView user_picture;
    private LinearLayout title1;
    public LocationClient mLocationClient;
    private TextView positionText;
    private VerticalRollingTextView scroll;
    List<String> textArray = new ArrayList<String>();
    static  String c_sheng,c_shi,c_qu;
    private User user;
    TextView myNum,myName;
    DrawerLayout drawer;
    private String Path=null;
    private long exitTime;
    private TwinklingRefreshLayout twinklingRefreshLayout;
    private TextView tools[] = new TextView[8];
    private String classification[] = {"工具类","数码类","家居类","学习类","服饰类","交通类","场地类","服务类"};
    private List<Goods> recommendList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecommendAdapter recommendAdapter;
    private int limit = 6;
    private int skip = 0;
    BmobRealTimeData rtd = new BmobRealTimeData();//数据监听
    private ToastShow toastShow = new ToastShow(this);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBar();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);
        ActivityCollecter.addActivty(this);
        //

        positionText = (TextView) findViewById(R.id.position);
        requestLocation();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        right = (CoordinatorLayout) findViewById(R.id.right1);
        left = (NavigationView) findViewById(R.id.nav_view);
        title1 = (LinearLayout) findViewById(R.id.MianTitle);

        tools[0]=(TextView)findViewById(R.id.skill);
        tools[1]=(TextView)findViewById(R.id.electronic);
        tools[2]=(TextView)findViewById(R.id.home);
        tools[3]=(TextView)findViewById(R.id.study);
        tools[4]=(TextView)findViewById(R.id.clothes);
        tools[5]=(TextView)findViewById(R.id.transportation);
        tools[6]=(TextView)findViewById(R.id.place);
        tools[7]=(TextView)findViewById(R.id.service);
        recyclerView=(RecyclerView)findViewById(R.id.recommendRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        twinklingRefreshLayout = (TwinklingRefreshLayout)findViewById(R.id.recommendRefresh);
        twinklingRefreshLayout.setEnableLoadmore(true);
        twinklingRefreshLayout.setOverScrollBottomShow(false);
        twinklingRefreshLayout.setEnableRefresh(false);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recommendAdapter = new RecommendAdapter(recommendList);
        recyclerView.setAdapter(recommendAdapter);
        scroll = (VerticalRollingTextView)findViewById(R.id.scrollText);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        user_picture = (CircleImageView) headerView.findViewById(R.id.roundImageView);
        myName = (TextView) headerView.findViewById(R.id.myName);
        myNum = (TextView) headerView.findViewById(R.id.myNum);
        ShareSDK.initSDK(this);
        String ObjectId = ShareStorage.getShareString(this,"ObjectId");
        queryUser(ObjectId);
        monitorState();
        queryGoods();
        initList();
        scroll.setDataSetAdapter(new DataSetAdapter<String>(textArray) {
            @Override
            protected String text(String s) {
                return s;
            }
        });
        scroll.run();
        for(int i = 0;i<8;i++){
            final int finalI = i;
            tools[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,ArticlesActivity.class);//跳转到技能分类的页面
                    intent.putExtra("SNTSearch",classification[finalI]);
                    intent.putExtra("SearchFlag","1");
                    startActivity(intent);

                }
            });
        }
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryGoods();
                        refreshLayout.finishLoadmore();
                    }
                },1000);
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isDrawer){
                    return left.dispatchTouchEvent(motionEvent);
                }else{
                    return false;
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.icon_location:
                        ChangeAddressPopwindow mChangeAddressPopwindow = new ChangeAddressPopwindow(MainActivity.this);
                        mChangeAddressPopwindow.setAddress("广东", "深圳", "福田区");
                        mChangeAddressPopwindow.showAtLocation(positionText, Gravity.BOTTOM, 0, 0);
                        mChangeAddressPopwindow.setAddresskListener(new ChangeAddressPopwindow.OnAddressCListener() {
                            @Override
                            public void onClick(String province, String city, String area) {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "您选择了:"+province + "-" + city + "-" + area, Toast.LENGTH_LONG).show();
                                positionText.setText(area);
                                ShareStorage.setShareString(MainActivity.this,"mainShi",city+"市","mainQu",area);
                            }
                            @Override
                            public void onClick() {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "当前定位:"+c_sheng +"-" + c_shi + "-" + c_qu, Toast.LENGTH_LONG).show();
                                ShareStorage.setShareString(MainActivity.this,"mainShi",c_shi,"mainQu",c_qu);
                                positionText.setText(c_qu);
                            }

                        });
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        user_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserMessageActivity.class);
                intent.putExtra("mime",user);
                if(Path==null)intent.putExtra("path","null");
                else intent.putExtra("path",Path);
                startActivity(intent);
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left 左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                right.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
            }
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer=false;
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        setMonitorObject();
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this,OrderActivity.class);
            intent.putExtra("flag","1");
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this,OrderActivity.class);
            intent.putExtra("flag","2");
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this,UserGoodsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this,SetActivity.class);
            startActivity(intent);

        }else if(id ==R.id.nav_release){
            Intent intent = new Intent(this,CreateMessageActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share) {
            share = new SharePopupWindow(MainActivity.this);
            share.setPlatformActionListener(MainActivity.this);
            ShareModel model = new ShareModel();
            model.setImageUrl(imageurl);
            model.setText(text);
            model.setTitle(title);
            model.setUrl(url);
            share.initShareParams(model);
            share.showShareWindow();
            // 显示窗口 (设置layout在PopupWindow中显示的位置)
            share.showAtLocation(
                    MainActivity.this.findViewById(R.id.drawer_layout),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    public void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    public void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            final String  sheng=location.getProvince().toString();
            final String  shi=location.getCity().toString();
            final String qu=location.getDistrict().toString();
            c_sheng=sheng;
            c_shi=shi;
            c_qu=qu;
            pop(sheng,shi,qu);
            ShareStorage.setShareString(MainActivity.this,"mainShi",c_shi,"mainQu",c_qu);
            ShareStorage.setShareString(MainActivity.this,"Shi",c_shi,"Qu",c_qu);
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

    }
    public void pop(String x1,String x2,final String x3)
    {
        String [] pos = ShareStorage.getShareString(this,"mainShi","mainQu");
        if(!pos[0].equals(x2) || !pos[1].equals(x3)){
            Toast.makeText(this,"当前定位为:"+x1+"-"+x2+"-"+x3, Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    positionText.setText(x3);
                }
            });
        }

    }
    private void queryUser(String ObjectID){//根据id查询用户
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(ObjectID, new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    user = object;
                    myNum.setText(user.getPhoneNumber());
                    myName.setText(user.getName());
                    BmobFile bmobfile = user.getTouXiang();
                    if(bmobfile!= null){
                        bmobfile.download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                show();
                                if(e==null) {
                                    user_picture.setImageBitmap(PictureTool.showImage(s));
                                    Path = s;
                                }
                                else  Toast.makeText(MainActivity.this,e.getErrorCode(),Toast.LENGTH_LONG);
                            }
                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                    }
                }else{
                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG);
                }
            }

        });
    }
    private void show(){
        if(user.getName()!=null) myName.setText(user.getName());
        myNum.setText(user.getPhoneNumber());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String ObjectId = ShareStorage.getShareString(this,"ObjectId");
        queryUser(ObjectId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
        ShareSDK.stopSDK(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return true;
        }
        else{
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
            {

                if((System.currentTimeMillis()-exitTime) > 2000) {
                    toastShow.toastShow ("再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                }
                else {
                    moveTaskToBack(false);
                }

                return true;
            }
            return super.onKeyDown(keyCode, event);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //获取样式中的属性值
            TypedValue typedValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
            int[] attribute = new int[]{android.R.attr.colorPrimary};
            TypedArray array = this.obtainStyledAttributes(typedValue.resourceId, attribute);
            int color = array.getColor(0, Color.TRANSPARENT);
            array.recycle();
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (share != null) {
            share.dismiss();
        }
    }



    @Override
    public void onCancel(Platform arg0, int arg1) {
        Message msg = new Message();
        msg.what = 0;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        Message msg = new Message();
        msg.what = 1;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        if (what == 1) {
            Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
        }
        if (share != null) {
            share.dismiss();
        }
        return false;
    }
    public  void queryGoods() {
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        BmobQuery<Goods> q1 = new BmobQuery<Goods>();
        User me = new User();
        me.setObjectId(ShareStorage.getShareString(this,"ObjectId"));
        q1.addWhereNotEqualTo("user",me);
        BmobQuery<Goods> q2 = new BmobQuery<Goods>();
        q2.order("-StarRating");
        q2.addWhereEqualTo("state","可租用");
        BmobQuery<Goods> q3 = new BmobQuery<Goods>();
        String pos = ShareStorage.getShareString(this,"mainShi");
        q3.addWhereEqualTo("Positioning", pos);//市定位
        List<BmobQuery<Goods>> queries = new ArrayList<BmobQuery<Goods>>();
        queries.add(q1);
        queries.add(q2);
        queries.add(q3);
        query.and(queries);
        query.setSkip(skip).setLimit(limit);
        skip+=6;
        query.findObjects(new FindListener<Goods>()
        {
            @Override
            public void done(List<Goods> goodsList, BmobException e) {
                if (goodsList == null) {
                    Toast.makeText(MainActivity.this, "无此物品的相关信息！", Toast.LENGTH_SHORT).show();
                } else {
                    recommendList.addAll(goodsList);
                    recommendAdapter.notifyDataSetChanged();
                }
            }
        });

    }
    //监视我的物品的状态变化
    private void  monitorState(){
        rtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                Intent intent = new Intent(MainActivity.this, StateChangeService.class);
                startService(intent);
            }

            @Override
            public void onConnectCompleted(Exception ex) {
                Log.d("debug1", "连接成功:"+rtd.isConnected());

            }
        });
    }
    //给用户的每一个物品设置监听
    private void setMonitorObject(){
        String id = ShareStorage.getShareString(this,"ObjectId");
        User me = new User();
        me.setObjectId(id);
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addWhereEqualTo("user",me);
        query.findObjects(new FindListener<Goods>()
        {
            @Override
            public void done(List<Goods> goodsList, BmobException e) {
                if (rtd.isConnected()){
                    for(Goods g: goodsList){
                        rtd.subRowUpdate("Goods", g.getObjectId());
                    }
                }
            }
        });

    }
    //初始化滚动textview
    private void initList(){
        textArray.add("欢迎观临YI租在线");
        textArray.add("点我搜索");
    }
//    private void statusBar(){
//        if(cb)
//    }
}
