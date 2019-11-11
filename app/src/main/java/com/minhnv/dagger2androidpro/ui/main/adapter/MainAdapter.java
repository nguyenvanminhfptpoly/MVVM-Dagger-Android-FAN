package com.minhnv.dagger2androidpro.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.widget.ANImageView;
import com.minhnv.dagger2androidpro.R;
import com.minhnv.dagger2androidpro.data.model.HomeStay;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<HomeStay> homeStays;
    private Context context;
    private onClick onClick;

    public MainAdapter(List<HomeStay> homeStays, Context context,onClick onClick) {
        this.homeStays = homeStays;
        this.context = context;
        this.onClick = onClick;
    }

    public interface onClick{
        void onClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(homeStays.get(position));
        holder.imgPicture.setOnClickListener(view -> onClick.onClick(position));
    }

    @Override
    public int getItemCount() {
        return homeStays == null ? 0 : homeStays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
    //replace Picasso loadding Image from Url
        private ANImageView imgPicture;
        private TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPicture = itemView.findViewById(R.id.imgPicture);
            tvName = itemView.findViewById(R.id.tvName);
        }
        public void bind(HomeStay homeStay){
            //default image
            imgPicture.setDefaultImageResId(R.drawable.ic_launcher_background);
            //image error
            imgPicture.setErrorImageResId(R.drawable.ic_launcher_background);
            //load url
            imgPicture.setImageUrl(homeStay.getImage());
            tvName.setText(homeStay.getTitle());
        }
    }
}
