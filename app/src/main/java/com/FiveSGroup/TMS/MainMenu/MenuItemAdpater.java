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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CancelGood.Home_CancelGood;
import com.FiveSGroup.TMS.Inventory.InventoryHome;
import com.FiveSGroup.TMS.LPN.LPNActivity;
import com.FiveSGroup.TMS.LetDown.LetDownSuggestionsActivity;
import com.FiveSGroup.TMS.ListOD.HomeOD;
import com.FiveSGroup.TMS.MasterPick.Home_Master_Pick;
import com.FiveSGroup.TMS.PickList.NewWareHouseActivity;
import com.FiveSGroup.TMS.PoReturn.Home_PoReturn;
import com.FiveSGroup.TMS.PutAway.Qrcode_PutAway;
import com.FiveSGroup.TMS.QA.Home.Home_Show_QA;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.RemoveFromLPN.Qrcode_Remove_LPN;
import com.FiveSGroup.TMS.StockOut.Home_Stockout;
import com.FiveSGroup.TMS.StockOut.Home_Stockout_Total;
import com.FiveSGroup.TMS.StockTransfer.Qrcode_StockTransfer;
import com.FiveSGroup.TMS.TowingContainers.Check_Transport;
import com.FiveSGroup.TMS.TransferQR.Home.Home_PhanloaiHH;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitQrcode;
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

    public MenuItemAdpater(Context context, ArrayList<MenuItemObject> arrItem) {
        this.context = context;
        this.arrItem = arrItem;
    }

    @NonNull
    @Override
    public MenuItemAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_item_category, null, false);
        return new MenuItemAdpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemAdpater.ViewHolder holder, final int position) {

        final MenuItemObject object = arrItem.get(position);
        holder.tvNameItem.setText(object.getNameItem());
        holder.imageItem.setImageResource(object.getImageItem());

//        final int block_Warehouse = new CmnFns().Block_Function_By_Warehouse();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (object.getNameItem()) {
                    case "LPN":
                        Intent intentLPN = new Intent(context, LPNActivity.class);
                        context.startActivity(intentLPN);
                        break;
                    case "Nh???p Kho":
                        Intent intent = new Intent(context, HomeQRActivity.class);
                        context.startActivity(intent);
                        break;
                    case "Put Away":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
//                        Intent intentt = new Intent(context, Qrcode_put.class);
                        Intent intentt = new Intent(context, Qrcode_PutAway.class);
                        context.startActivity(intentt);
                        break;
//                        }

                    case "Let Down":

//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        //Items
//                        final String[] items = {"Qu??t M???i", "Qu??t Theo Phi???u"};
//                        AlertDialog.Builder b = new AlertDialog.Builder(context);
////Thi???t l???p title
//                        b.setTitle("Vui L??ng Ch???n H??nh Th???c Letdown");
////Thi???t l???p item
//                        b.setItems(items, new DialogInterface.OnClickListener() {
//                            //X??? l?? s??? ki???n
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                if (which == 0) {
                        Intent intent1 = new Intent(context, LetDownSuggestionsActivity.class);
                        context.startActivity(intent1);

//                                } else {
//
//                                }
//
//                            }
//                        });
////Hi???n th??? dialog
//                        b.show();

                        break;
//                        }

                    case "Chuy???n V??? Tr??":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;lo
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent3 = new Intent(context, Qrcode_StockTransfer.class);
                        context.startActivity(intent3);
                        break;
//                        }

                    case "Master Pick":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_master = new Intent(context, Home_Master_Pick.class);
                        context.startActivity(intent_master);
                        break;
//                        }

                    case "PickList":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent4 = new Intent(context, NewWareHouseActivity.class);
                        context.startActivity(intent4);
                        break;
//                        }

                    case "Xu???t Kho":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent2 = new Intent(context, Home_Stockout_Total.class);
                        context.startActivity(intent2);
                        break;
//                        }

                    case "Ki???m T???n":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_ivt = new Intent(context, InventoryHome.class);
                        context.startActivity(intent_ivt);
                        break;
//                        }

                    case "Ch???nh Kho":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_ajm = new Intent(context, Warehouse_Adjustment.class);
                        context.startActivity(intent_ajm);
                        break;
//                        }

                    case "G??? S???n Ph???m":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_remove = new Intent(context, Qrcode_Remove_LPN.class);
                        context.startActivity(intent_remove);
                        break;
//                        }

//                    case "Tr??? H??ng":
////                        if(block_Warehouse == -29){
////                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
////                            break;
////                        }else if(block_Warehouse == -1){
////                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
////                            break;
////                        }else if(block_Warehouse == 1){
//                        Intent intent_return = new Intent(context, Home_Return_WareHouse.class);
//                        context.startActivity(intent_return);
//                        break;
//                        }
                    case "Xu???t H???y":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_cancel = new Intent(context, Home_CancelGood.class);
                        context.startActivity(intent_cancel);
                        break;
//                        }

                    case "Chuy???n ??VT":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_transfer = new Intent(context, TransferUnitQrcode.class);
                        context.startActivity(intent_transfer);
                        break;

                    case "PO Return":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_po = new Intent(context, Home_PoReturn.class);
                        context.startActivity(intent_po);
                        break;
//                        }
                    case "Ph??n Lo???i HH":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_hh = new Intent(context, Home_PhanloaiHH.class);
                        context.startActivity(intent_hh);
                        break;
//                        }

                    case "Ki???m ?????nh":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_qa = new Intent(context, Home_Show_QA.class);
                        context.startActivity(intent_qa);
                        break;
//                        }

                    case "Ki???m Tra Xe":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_check = new Intent(context, Check_Transport.class);
                        context.startActivity(intent_check);
                        break;

                    case "OD":
//                        if(block_Warehouse == -29){
//                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == -1){
//                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
//                            break;
//                        }else if(block_Warehouse == 1){
                        Intent intent_od = new Intent(context, HomeOD.class);
                        context.startActivity(intent_od);
                        break;

//                    case "Repacking":
////                        if(block_Warehouse == -29){
////                            Toast.makeText(context,"Kho ??ang th???c hi???n ki???m t???n",Toast.LENGTH_LONG).show();
////                            break;
////                        }else if(block_Warehouse == -1){
////                            Toast.makeText(context,"???? x???y ra l???i vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
////                            break;
////                        }else if(block_Warehouse == 1){
//                        Intent intent_repack = new Intent(context, RepackingScanqrcodeVitri.class);
//                        context.startActivity(intent_repack);
//                        break;

//                    case "Th??m M?? Barcode":
//                        Toast.makeText(context, "T??nh n??ng ??ang ph??t tri???n trong th???i gian t???i", Toast.LENGTH_LONG).show();
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
