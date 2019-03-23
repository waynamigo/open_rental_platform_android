package com.example.yizu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yizu.R;
import com.example.yizu.RegisterActivity;
import com.example.yizu.ShowActivity;
import com.example.yizu.bean.Goods;
import com.example.yizu.tool.PictureTool;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by q on 2017/7/20
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context mContext;
    private List<Goods>   mArticle;
    private int max_count = 6;//最大显示数
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView articleImage;
        TextView  articleName;
        TextView  articleDetailed;
        TextView  articleMoney;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            articleImage=(ImageView)view.findViewById(R.id.article_image);
            articleName=(TextView)view.findViewById(R.id.article_name);
            articleMoney=(TextView)view.findViewById(R.id.article_rentmoney);
            articleDetailed=(TextView)view.findViewById(R.id.article_detailed);
        }
    }
    public ArticleAdapter(List<Goods> articleList){
        mArticle=articleList;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.article, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Goods goods = mArticle.get(position);
                Intent intent = new Intent(mContext, ShowActivity.class);
                intent.putExtra("searchGoods",goods);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder,int position){

        Goods article=mArticle.get(position);
        holder.articleName.setText(article.getClassification()+"  "+article.getGoodsName());
        holder.articleDetailed.setText("地址: "+article.getPositioning()+" "+article.getArea());
        holder.articleMoney.setText("￥"+article.getMoneyPer()+"/天");
        downImage(article,holder);
    }
    @Override
    public int getItemCount(){
        return mArticle.size();
    }
    void downImage(final Goods goods, final ViewHolder holder){
        BmobFile bmobfile = goods.getPic1();
        File saveFile = new File(mContext.getExternalFilesDir(null), bmobfile.getFilename());
        if(bmobfile!= null) {
            bmobfile.download(saveFile,new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        goods.setPath(s, 0);
                      //  holder.articleImage.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(goods.getPath(0), 300, 300));
                        holder.articleImage.setImageBitmap(PictureTool.showImage(goods.getPath(0)));
                    } else Toast.makeText(mContext, e.getErrorCode(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
    }
}