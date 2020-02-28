package com.ratwareid.sertronik.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.mitra.DetailOrderActivity;
import com.ratwareid.sertronik.helper.UniversalHelper;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Order;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private ArrayList<Order> orderArrayList;
    private Activity activity;

    public OrderAdapter(ArrayList<Order> orderArrayList, Activity activity) {
        this.orderArrayList = orderArrayList;
        this.activity = activity; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_order, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Order order = orderArrayList.get(position);

        holder.imageThumbnail.setImageDrawable(TextDrawable.builder().buildRect(StringUtils.upperCase(UniversalHelper.textAvatar(order.getSenderName())), R.color.colorPrimaryDark));
        holder.textNumberPhone.setText(order.getSenderPhone());
        holder.textName.setText(order.getSenderName());

        if (order.getOrderType() == UniversalKey.CALL_SERVICE){
            holder.textOrderType.setText("Panggilan");
        }else{
            holder.textOrderType.setBackground(activity.getResources().getDrawable(R.drawable.button_call_rounded));
            holder.textOrderType.setText("Jemput Barang");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, DetailOrderActivity.class)
                        .putExtra("mitraID", order.getMitraID())
                        .putExtra("createDate", order.getCreateDate())
                        .putExtra("orderType", order.getOrderType())
                        .putExtra("senderLatitude", order.getSenderLatitude())
                        .putExtra("senderLongitude", order.getSenderLongitude())
                        .putExtra("senderName", order.getSenderName())
                        .putExtra("senderPhone", order.getSenderPhone())
                        .putExtra("itemName", order.getItemName())
                        .putExtra("itemSize", order.getItemSize())
                        .putExtra("itemCrash", order.getItemSymptom())
                        .putExtra("senderLocation", order.getSenderAddress())
                        .putExtra("itemCrash", order.getItemSymptom())
                        .putExtra("itemBrand", order.getItemBrand())
                        .putExtra("key", order.getKey())
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textOrderType, textName, textNumberPhone;
        ImageView imageThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrderType = itemView.findViewById(R.id.textOrderType);
            textName = itemView.findViewById(R.id.textName);
            textNumberPhone = itemView.findViewById(R.id.textPhoneNumber);
            imageThumbnail = itemView.findViewById(R.id.imageThumbnail);

        }
    }
}
