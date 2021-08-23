package com.FiveSGroup.TMS.TransferQR.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.MainMenu.MenuItemObject;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.TransferQR.ChuyenMa.Home_ChuyenMa;
import com.FiveSGroup.TMS.TransferQR.TransferPosting.Home_TransferPosting;
import com.FiveSGroup.TMS.Warehouse.HomeQRActivity;

import java.util.ArrayList;

public class Arr_Adapter_Transfer_Posting extends RecyclerView.Adapter<Arr_Adapter_Transfer_Posting.ViewHolder> {

    private Context context;
    ArrayList<MenuItemObject> arrItem;
    // private FCustomerAddNewEdit fCustomerAddNewEdit;
    private ImageView imgDisplay, imgClose;
    private LinearLayout layout;
    private FrameLayout frameLayout;

    public Arr_Adapter_Transfer_Posting(Context context, ArrayList<MenuItemObject> arrItem) {
        this.context = context;
        this.arrItem = arrItem;
    }

    @NonNull
    @Override
    public Arr_Adapter_Transfer_Posting.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_item_category, null, false);
        return new Arr_Adapter_Transfer_Posting.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Arr_Adapter_Transfer_Posting.ViewHolder holder, final int position) {

        final MenuItemObject object = arrItem.get(position);
        holder.tvNameItem.setText(object.getNameItem());
        holder.imageItem.setImageResource(object.getImageItem());

//        final int block_Warehouse = new CmnFns().Block_Function_By_Warehouse();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (object.getNameItem()) {
                    case "Lấy Hàng":
                        Intent intentLPN = new Intent(context, Home_TransferPosting.class);
                        context.startActivity(intentLPN);
                        break;
                    case "Phân Loại":
                        Intent intent = new Intent(context, Home_ChuyenMa.class);
                        context.startActivity(intent);
                        break;


                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return arrItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageItem;
        private TextView tvNameItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.imageItem);
            tvNameItem = itemView.findViewById(R.id.tvNameItem);
        }
    }

}
