package com.FiveSGroup.TMS.AddCustomerFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    ArrayList<Bitmap> arrImage;
   // private FCustomerAddNewEdit fCustomerAddNewEdit;
    private ImageView imgDisplay, imgClose;
    private LinearLayout layout;
    private FrameLayout frameLayout;

    public ImageAdapter(Context context, ArrayList<Bitmap> arrImage, FrameLayout frameLayout, LinearLayout layout, ImageView imgDisplay, ImageView imgClose){
        this.context = context;
        this.arrImage = arrImage;
        this.imgDisplay = imgDisplay;
        this.layout = layout;
        this.frameLayout = frameLayout;
        this.imgClose = imgClose;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_item_image,null,false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, final int position) {

        final Bitmap bitmap = arrImage.get(position);
        holder.imageView.setImageBitmap(bitmap);
        //final String image = BitMapToString(bitmap);
        //final String imageView [] = {image};
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                imgDisplay.setVisibility(View.VISIBLE);
                imgClose.setVisibility(View.VISIBLE);
                imgDisplay.setImageBitmap(bitmap);
                imgDisplay.setScaleType(ImageView.ScaleType.FIT_XY);
               imgClose.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       frameLayout.setVisibility(View.GONE);
                       layout.setVisibility(View.VISIBLE);
                   }
               });
               // fCustomerAddNewEdit.showPicker(position, imageView);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrImage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView tvInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgAnh);
          //  tvInfo = itemView.findViewById(R.id.tvInfo);
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
