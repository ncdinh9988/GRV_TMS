package com.FiveSGroup.TMS.LPN;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.Warehouse.Product_Qrcode;
import com.FiveSGroup.TMS.Warehouse.Qrcode;

import java.util.ArrayList;

public class ItemLPNAdapter extends RecyclerView.Adapter<ItemLPNAdapter.ViewHolder> {

    final ArrayList<LPN> listLPN;
    Context context;

    View view;

    public ItemLPNAdapter(Context context, ArrayList<LPN> listLPN) {
        this.context = context;
        this.listLPN = listLPN;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public ItemLPNAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_item_lpn, null, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemLPNAdapter.ViewHolder holder, final int position) {
        final LPN lpn = listLPN.get(position);
        holder.tvCode.setText(lpn.getLPN_CODE());
        holder.tvDate.setText(lpn.getLPN_DATE());
        holder.tvSTT.setText(lpn.getLPN_NUMBER());
        holder.tvuser_create.setText(lpn.getUSER_CREATE());
        holder.tvstorage.setText(lpn.getSTORAGE());
//        holder.layout_item.setBackgroundColor(context.getResources().getColor(R.color.white));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // holder.layout_item.setBackgroundColor(context.getResources().getColor(R.color.grey04));
//                Toast.makeText(context, ""+lpn.getLPN_NUMBER()+" - "+lpn.getLPN_CODE()+" - "+lpn.getLPN_DATE(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LPNProductActivity.class);
                intent.putExtra("LPN_CODE", String.valueOf(lpn.getLPN_CODE()));
                context.startActivity(intent);
            }
        });
    }

    private boolean hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        return true;
    }

    @Override
    public int getItemCount() {
        return listLPN.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCode, tvDate, tvSTT , tvuser_create , tvstorage;
        LinearLayout layout_item;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvuser_create = itemView.findViewById(R.id.tvuser_create);
            tvstorage = itemView.findViewById(R.id.tvstorage);
            tvSTT = itemView.findViewById(R.id.tvSTT);
            layout_item = itemView.findViewById(R.id.layout_item);


        }
    }


}
