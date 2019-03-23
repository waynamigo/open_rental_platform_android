package com.example.yizu.adapter;

/**
 * Created by yikuai on 2017/7/24.
 */

import android.app.DownloadManager;
import android.content.Context;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yizu.R;
import com.example.yizu.bean.Goods;
import com.example.yizu.tool.PictureTool;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

public class ImageAdapter extends PagerAdapter {//上栏
    //private Context context;

    private ArrayList<View> mImageViewList;
    //item的个数
    private Context context;
    private BmobFile[] files;
    public ImageAdapter( ArrayList<View> mImageViewList,Context context,BmobFile[] files){

        this.mImageViewList=mImageViewList;
        this.context = context;
        this.files = files;
    }



   public int getCount() {
        return mImageViewList.size();
    }

   @Override
    public boolean isViewFromObject(View view, Object object) {
       return view == object;

    }

//    //初始化item布局
    @Override
   public Object instantiateItem(ViewGroup container, int position) {
        View view = mImageViewList.get(position);
        ImageView goodsPic = (ImageView)view.findViewById(R.id.imageView);
        downImage(files[position],goodsPic);
        container.addView(view);
        return view;
    }

   //销毁item
   @Override
   public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
   }
    void downImage(BmobFile bmobfile, final ImageView view){
        File saveFile = new File(context.getExternalFilesDir(null), bmobfile.getFilename());
        if(bmobfile!= null) {
            bmobfile.download(saveFile,new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.d("debug1",s);
                        view.setImageBitmap(PictureTool.showImage(s));
                    }else {
                        Toast.makeText(context,e.toString(), Toast.LENGTH_LONG).show();
                        Log.d("debug1",e.getMessage()+" "+e.getErrorCode()+" "+e.toString());
                    }

                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
    }
}