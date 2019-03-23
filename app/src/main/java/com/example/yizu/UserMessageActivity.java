package com.example.yizu;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yizu.bean.User;
import com.example.yizu.control.WaveView;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.PictureTool;
import com.example.yizu.tool.ShareStorage;
import com.wingsofts.threedlayout.ThreeDLayout;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessageActivity extends AppCompatActivity {
    User user;
    TextView NAME;
    TextView GRADE;
    TextView GENDER;
    CircleImageView USERIMAGE;
    RelativeLayout GoName;
    RelativeLayout GoGender;
    Button button;
    TextView PHONENUM;
    WaveView waveView;

    public static final int TAKE_PHOTO = 11;
    public static final int CHOOSE_PHOTO = 12;
    private Uri imageUri;
    private String UriPath;//图片路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        ActivityCollecter.addActivty(this);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.userToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        NAME=(TextView)findViewById(R.id.name);
        GRADE=(TextView)findViewById(R.id.grade);
        GENDER=(TextView)findViewById(R.id.gender);
        USERIMAGE=(CircleImageView)findViewById(R.id.userimage);
        waveView = (WaveView)findViewById(R.id.wave);
        GoGender = (RelativeLayout) findViewById(R.id.goGender);
        PHONENUM = (TextView)findViewById(R.id.phoneNum);
        GoName = (RelativeLayout) findViewById(R.id.goName);
        button= (Button)findViewById(R.id.exit_user_message);
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2,-2);
        lp.gravity = Gravity.BOTTOM|Gravity.CENTER;
        waveView.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {
                lp.setMargins(0,0,0,(int)y+20);
                USERIMAGE.setLayoutParams(lp);
            }
        });
        GoName.setOnClickListener(new View.OnClickListener() {//启动修改名字
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserMessageActivity.this,ChangeNameActivity.class);
                startActivityForResult(intent1, 1);
            }
        });
        GoGender.setOnClickListener(new View.OnClickListener() {//修改男女
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserMessageActivity.this)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[] {"男","女"}, -1,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0){
                                            GENDER.setText("男");
                                            user.setGender("男");
                                        }else{
                                            GENDER.setText("女");
                                            user.setGender("女");
                                        }
                                        SaveUserMessage();
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {//注销登陆
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(UserMessageActivity.this,LoginActivity.class);
                startActivity(intent3);
                ActivityCollecter.finishALL();
            }
        });
        USERIMAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("mime");
        UriPath = intent.getStringExtra("path");
        show();
        if(!UriPath.equals("null"))
        USERIMAGE.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(UriPath,300,300));
    }

    private void show(){
        if(user.getName()!=null) NAME.setText(user.getName());
        if(user.getGrade()!=null) GRADE.setText(user.getGrade().toString());
        if(user.getGender()!=null)GENDER.setText(user.getGender());
        PHONENUM.setText(user.getPhoneNumber());
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void SaveUserMessage(){//上传到服务器
        user.update(user.getObjectId(), new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(UserMessageActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserMessageActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }
    //申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {//申请权限
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//处理结果
        switch (requestCode) {
            case 1:
                if(resultCode==RESULT_OK){
                    String newName = data.getStringExtra("ReName");
                    NAME.setText(newName);
                    user.setName(newName);
                    SaveUserMessage();
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                       // Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        USERIMAGE.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(UriPath,300,300));
                        SaveFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    PictureTool tool = new PictureTool();
                    UriPath = tool.getPicFromUri(this,data.getData());
                    USERIMAGE.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(UriPath,300,300));
                    SaveFile();
                }
                break;
            default:
                break;
        }
    }

    public void showDialog() {//弹出对话框
        final View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        //初始化控件
        Button picture = (Button) view.findViewById(R.id.Picture_Map);
        Button camera = (Button) view.findViewById(R.id.Picture_Camera);
        Button cancel = (Button) view.findViewById(R.id.Picture_Cancel);
        Button bigPic = (Button)view.findViewById(R.id.lookBigPic);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
                dialog.dismiss();

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getExternalCacheDir(), "TouXinag.jpg");
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UriPath = file.getPath();
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(UserMessageActivity.this, "com.example.yizu", file);
                } else {
                    imageUri = Uri.fromFile(file);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                dialog.dismiss();
            }
        });
        bigPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhotoActivity(USERIMAGE);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    void SaveFile(){
        File file = new File(UriPath);
        final BmobFile bmobFile = new BmobFile(file);
      //  SaveUserMessage();
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    user.setTouXiang(bmobFile);
                    SaveUserMessage();
                }else{
                    Toast.makeText(UserMessageActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
    public  void startPhotoActivity(ImageView imageView) {
        Intent intent = new Intent(this, DragPhotoActivity.class);
        int location[] = new int[2];
        imageView.getLocationOnScreen(location);
        intent.putExtra("left", location[0]);
        intent.putExtra("top", location[1]);
        intent.putExtra("height", imageView.getHeight());
        intent.putExtra("width", imageView.getWidth());
        Bundle b=new Bundle();
        String [] p = new String[1];
        p[0] = UriPath;
        b.putStringArray("PicPath", p);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}

