package com.example.yizu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yizu.R;
import com.example.yizu.RecordActivity;
import com.example.yizu.bean.Record;

import java.util.List;
/**
 * Created by q on 2017/7/20
 */
public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder>{
    private Context mContext;
    private List<Record> mBusinessList;
    static class ViewHolder  extends RecyclerView.ViewHolder{
        TextView goodsname;
        TextView TIME;
        TextView CODE;
        TextView RENTMONEY;
        TextView OTHERMONEY;
        TextView STATE;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsname=(TextView) itemView.findViewById(R.id.goodsName);
            TIME=(TextView)itemView.findViewById(R.id.time);
            CODE=(TextView)itemView.findViewById(R.id.code);
            RENTMONEY=(TextView)itemView.findViewById(R.id.RentMoney);
            OTHERMONEY=(TextView)itemView.findViewById(R.id.OtherMoney);
            STATE=(TextView)itemView.findViewById(R.id.state);
        }
    }

    public BusinessAdapter(List<Record> BusinessList){
        mBusinessList=BusinessList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext==null)
        {
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.business_item,parent,false);
        Button button;
        button =(Button)view.findViewById(R.id.recordactivity);
        final ViewHolder holder = new ViewHolder(view);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Record record = mBusinessList.get(position);
                Intent intent=new Intent(mContext,RecordActivity.class);
                intent.putExtra("record",record);;
                mContext.startActivity(intent);
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        Record record=mBusinessList.get(position);
        holder.goodsname.setText(record.getMake().getGoodsName());
        holder.TIME.setText(record.getCreatedAt());
        holder.OTHERMONEY.setText(record.getDeposit().toString());
        holder.CODE.setText(record.getObjectId());
        holder.RENTMONEY.setText(record.getMoney().toString());
        holder.STATE.setText(record.getState());
    }

    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }
}
