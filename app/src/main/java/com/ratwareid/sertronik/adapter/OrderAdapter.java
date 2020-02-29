package com.ratwareid.sertronik.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.activity.home.RatingActivity;
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
    private String mode;

    public OrderAdapter(ArrayList<Order> orderArrayList, Activity activity,String mode) {
        this.orderArrayList = orderArrayList;
        this.activity = activity;
        this.mode = mode;
    }

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
        holder.textStatusOrder.setText(UniversalHelper.getOrderStatus(order.getStatus()));
        if (mode.equalsIgnoreCase(UniversalKey.mitraorder)){
            holder.textStatusOrder.setVisibility(View.GONE);
        }

        if (order.getStatus() == UniversalKey.ORDER_FINISH){
            holder.textHapus.setVisibility(View.VISIBLE);
            holder.textRating.setVisibility(View.VISIBLE);
        }else if (order.getStatus() == UniversalKey.ORDER_DECLINED){
            holder.textHapus.setVisibility(View.VISIBLE);
        }else if (order.getStatus() == UniversalKey.WAITING_RESPONSE_ORDER){
            if (mode.equalsIgnoreCase(UniversalKey.useroder)) {
                holder.textBatalkan.setVisibility(View.VISIBLE);
            }
        }

        holder.textHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                if (mode.equalsIgnoreCase(UniversalKey.useroder)){
                    reference.child(UniversalKey.USERDATA_PATH).child(order.getSenderPhone()).child("orderList").child(order.getKey()).child("status").setValue(UniversalKey.ORDER_DELETED);
                }
                if (mode.equalsIgnoreCase(UniversalKey.mitraorder)){
                    reference.child(UniversalKey.MITRADATA_PATH).child(order.getMitraID()).child("listOrder").child(order.getKey()).child("status").setValue(UniversalKey.ORDER_DELETED);
                }
                Toast.makeText(activity, "Berhasil Menghapus Order", Toast.LENGTH_SHORT).show();
            }
        });

        holder.textBatalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                if (mode.equalsIgnoreCase(UniversalKey.useroder)){
                    reference.child(UniversalKey.USERDATA_PATH).child(order.getSenderPhone()).child("orderList").child(order.getKey()).child("status").setValue(UniversalKey.ORDER_CANCELED);
                    reference.child(UniversalKey.MITRADATA_PATH).child(order.getMitraID()).child("listOrder").child(order.getKey()).child("status").setValue(UniversalKey.ORDER_CANCELED);
                }
                Toast.makeText(activity, "Berhasil Membatalkan Order", Toast.LENGTH_SHORT).show();
            }
        });

        holder.textRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO :: Intent dan beri rating lalu ubah status jadi transaksi selesai
                activity.startActivity(new Intent(activity, RatingActivity.class)
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
                    .putExtra("itemStatus", UniversalHelper.getOrderStatus(order.getStatus()))
                    .putExtra("senderLocation", order.getSenderAddress())
                    .putExtra("itemCrash", order.getItemSymptom())
                    .putExtra("itemBrand", order.getItemBrand())
                    .putExtra("status", order.getStatus())
                    .putExtra("key", order.getKey())
                    .putExtra("mode",mode)
                );
            }
        });

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
                        .putExtra("itemStatus", UniversalHelper.getOrderStatus(order.getStatus()))
                        .putExtra("senderLocation", order.getSenderAddress())
                        .putExtra("itemCrash", order.getItemSymptom())
                        .putExtra("itemBrand", order.getItemBrand())
                        .putExtra("status", order.getStatus())
                        .putExtra("key", order.getKey())
                        .putExtra("mode",mode)
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textOrderType, textName, textNumberPhone,textStatusOrder;
        ImageView imageThumbnail;
        TextView textHapus,textBatalkan,textRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrderType = itemView.findViewById(R.id.textOrderType);
            textName = itemView.findViewById(R.id.textName);
            textNumberPhone = itemView.findViewById(R.id.textPhoneNumber);
            imageThumbnail = itemView.findViewById(R.id.imageThumbnail);
            textStatusOrder = itemView.findViewById(R.id.textStatusOrder);
            textHapus = itemView.findViewById(R.id.textHapus);
            textBatalkan = itemView.findViewById(R.id.textBatalkan);
            textRating = itemView.findViewById(R.id.textRating);
        }
    }
}
