package com.ratwareid.sertronik.adapter;

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
import com.ratwareid.sertronik.activity.admin.AktivasiMitraActivity;
import com.ratwareid.sertronik.activity.home.HomeActivity;
import com.ratwareid.sertronik.activity.user.order.SelectMitraActivity;
import com.ratwareid.sertronik.activity.user.order.pickup.DetailPickupActivity;
import com.ratwareid.sertronik.helper.UniversalHelper;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Mitradata;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class MitraAdapter extends RecyclerView.Adapter<MitraAdapter.ViewHolder> {


    private ArrayList<Mitradata> mitradataArrayList;
    private HomeActivity activity;


    public MitraAdapter(ArrayList<Mitradata> mitradataArrayList, HomeActivity activity) {
        this.mitradataArrayList = mitradataArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MitraAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_mitra_unverif, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MitraAdapter.ViewHolder holder, int position) {
        final Mitradata mitradata = mitradataArrayList.get(position);

        holder.imageThumbnail.setImageDrawable(TextDrawable.builder().buildRect(StringUtils.upperCase(UniversalHelper.textAvatar(mitradata.getNamaToko())), activity.getResources().getColor(R.color.colorPrimaryDark)));
        holder.textMitraName.setText(mitradata.getNamaToko());
        holder.textPhoneNumber.setText(mitradata.getNoTlp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(
                        new Intent(activity, AktivasiMitraActivity.class)
                                .putExtra("mitraID", mitradata.getMitraID())
                                .putExtra("mitraName" , mitradata.getNamaToko())
                                .putExtra("mitraLocation", mitradata.getAlamatToko())
                                .putExtra("mitraPhoneNumber", mitradata.getNoTlp())
                                .putExtra("mitraSpecialist", mitradata.getSpecialist())
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mitradataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageThumbnail;
        TextView textMitraName, textPhoneNumber, textRating;

        public ViewHolder(@NonNull View view) {
            super(view);

            imageThumbnail = view.findViewById(R.id.imageThumbnail);
            textMitraName = view.findViewById(R.id.textMitraName);
            textPhoneNumber = view.findViewById(R.id.textPhoneNumber);
            textRating = view.findViewById(R.id.textRating);
        }
    }
}
