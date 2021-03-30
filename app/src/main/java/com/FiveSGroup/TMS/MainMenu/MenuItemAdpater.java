package com.FiveSGroup.TMS.MainMenu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.AddBarcode.ListProductBarcodeActivity;
import com.FiveSGroup.TMS.Inventory.InventoryHome;
import com.FiveSGroup.TMS.LPN.LPNActivity;
import com.FiveSGroup.TMS.LetDown.LetDownSuggestionsActivity;
import com.FiveSGroup.TMS.MasterPick.Home_Master_Pick;
import com.FiveSGroup.TMS.PickList.NewWareHouseActivity;
import com.FiveSGroup.TMS.PutAway.Qrcode_PutAway;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.RemoveFromLPN.Qrcode_Remove_LPN;
import com.FiveSGroup.TMS.ReturnWareHouse.Home_Return_WareHouse;
import com.FiveSGroup.TMS.StockOut.Home_Stockout;
import com.FiveSGroup.TMS.StockTransfer.Qrcode_StockTransfer;
import com.FiveSGroup.TMS.Warehouse.HomeQRActivity;
import com.FiveSGroup.TMS.Warehouse_Adjustment.Warehouse_Adjustment;

import java.util.ArrayList;

public class MenuItemAdpater extends RecyclerView.Adapter<MenuItemAdpater.ViewHolder> {

    private Context context;
    ArrayList<MenuItemObject> arrItem;
    // private FCustomerAddNewEdit fCustomerAddNewEdit;
    private ImageView imgDisplay, imgClose;
    private LinearLayout layout;
    private FrameLayout frameLayout;

    public MenuItemAdpater(Context context, ArrayList<MenuItemObject> arrItem ){
        this.context = context;
        this.arrItem = arrItem;
    }

    @NonNull
    @Override
    public MenuItemAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_item_category,null,false);
        return new MenuItemAdpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemAdpater.ViewHolder holder, final int position) {

        final MenuItemObject object = arrItem.get(position);
        holder.tvNameItem.setText(object.getNameItem());
        holder.imageItem.setImageResource(object.getImageItem());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (object.getNameItem()){
                    case "LPN":
                        Intent intentLPN = new Intent(context, LPNActivity.class);
                        context.startActivity(intentLPN);
                        break;
                    case "Nhập Kho":
                        Intent intent = new Intent(context, HomeQRActivity.class);
                        context.startActivity(intent);
                        break;
                    case "Put Away":
                        Intent intentt = new Intent(context, Qrcode_PutAway.class);
                        context.startActivity(intentt);
                        break;
                    case "Let Down":
                        Intent intent1 = new Intent(context, LetDownSuggestionsActivity.class);
                        context.startActivity(intent1);
                        break;
                    case "Chuyển Vị Trí":
                        Intent intent3 = new Intent(context, Qrcode_StockTransfer.class);
                        context.startActivity(intent3);
                        break;
                    case "Master Pick":
                        Intent intent_master = new Intent(context, Home_Master_Pick.class);
                        context.startActivity(intent_master);
                        break;
                    case "PickList":
                        Intent intent4 = new Intent(context, NewWareHouseActivity.class);
                        context.startActivity(intent4);
                        break;
                    case "Xuất Kho":
                        Intent intent2 = new Intent(context, Home_Stockout.class);
                        context.startActivity(intent2);
                        break;
                    case "Kiểm Tồn":
                        Intent intent_ivt = new Intent(context, InventoryHome.class);
                        context.startActivity(intent_ivt);
                        break;
                    case "Chỉnh Kho":
                        Intent intent_ajm = new Intent(context, Warehouse_Adjustment.class);
                        context.startActivity(intent_ajm);
                        break;
                    case "Gỡ Sản Phẩm":
                        Intent intent_remove = new Intent(context, Qrcode_Remove_LPN.class);
                        context.startActivity(intent_remove);
                        break;
                    case "Trả Hàng":
                        Intent intent_return = new Intent(context, Home_Return_WareHouse.class);
                        context.startActivity(intent_return);
                        break;
//                    case "Thêm Mã Barcode":
//                        Toast.makeText(context, "Tính năng đang phát triển trong thời gian tới", Toast.LENGTH_LONG).show();
////                        Intent intent_add_barcode = new Intent(context, ListProductBarcodeActivity.class);
////                        context.startActivity(intent_add_barcode);
//                        break;

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
