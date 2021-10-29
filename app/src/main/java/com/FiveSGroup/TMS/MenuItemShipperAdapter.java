package com.FiveSGroup.TMS;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.AddCustomerFragment.FragAddCustomer;
import com.FiveSGroup.TMS.CancelGood.Home_CancelGood;
import com.FiveSGroup.TMS.Inventory.InventoryHome;
import com.FiveSGroup.TMS.LPN.LPNActivity;
import com.FiveSGroup.TMS.LetDown.LetDownSuggestionsActivity;
import com.FiveSGroup.TMS.MainMenu.MenuItemAdpater;
import com.FiveSGroup.TMS.MainMenu.MenuItemObject;
import com.FiveSGroup.TMS.MasterPick.Home_Master_Pick;
import com.FiveSGroup.TMS.PickList.NewWareHouseActivity;
import com.FiveSGroup.TMS.PoReturn.Home_PoReturn;
import com.FiveSGroup.TMS.PutAway.Qrcode_PutAway;
import com.FiveSGroup.TMS.QA.Home.Home_Show_QA;
import com.FiveSGroup.TMS.RemoveFromLPN.Qrcode_Remove_LPN;
import com.FiveSGroup.TMS.StockOut.Home_Stockout;
import com.FiveSGroup.TMS.StockTransfer.Qrcode_StockTransfer;
import com.FiveSGroup.TMS.TowingContainers.Towing_Containers;
import com.FiveSGroup.TMS.TransferQR.Home.Home_PhanloaiHH;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitQrcode;
import com.FiveSGroup.TMS.Warehouse.HomeQRActivity;
import com.FiveSGroup.TMS.Warehouse_Adjustment.Warehouse_Adjustment;

import java.util.ArrayList;

public class MenuItemShipperAdapter extends RecyclerView.Adapter<MenuItemShipperAdapter.ViewHolder> {

    private Context context;
    ArrayList<MenuItemObject> arrItem;
    // private FCustomerAddNewEdit fCustomerAddNewEdit;
    private ImageView imgDisplay, imgClose;
    private LinearLayout layout;
    private FrameLayout frameLayout;

    public MenuItemShipperAdapter(Context context, ArrayList<MenuItemObject> arrItem) {
        this.context = context;
        this.arrItem = arrItem;
    }

    @NonNull
    @Override
    public MenuItemShipperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_item_category, null, false);
        return new MenuItemShipperAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemShipperAdapter.ViewHolder holder, final int position) {

        final MenuItemObject object = arrItem.get(position);
        holder.tvNameItem.setText(object.getNameItem());
        holder.imageItem.setImageResource(object.getImageItem());

//        final int block_Warehouse = new CmnFns().Block_Function_By_Warehouse();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (object.getNameItem()) {
                    case "Kéo Công":
                        Intent intentLPN = new Intent(context, Towing_Containers.class);
                        context.startActivity(intentLPN);
                        break;
                    case "Giao Hàng":
                        Intent intent = new Intent(context, HomeActivity.class);
                        context.startActivity(intent);
                        break;

                    case "Thêm Khách Hàng":
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Fragment fragment = new FragAddCustomer();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.addnewcustomer, fragment).addToBackStack(null).commit();
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