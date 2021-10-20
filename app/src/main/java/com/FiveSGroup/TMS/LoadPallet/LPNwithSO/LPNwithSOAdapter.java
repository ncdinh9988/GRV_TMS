package com.FiveSGroup.TMS.LoadPallet.LPNwithSO;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.LPN.ItemLPNAdapter;
import com.FiveSGroup.TMS.LPN.LPN;
import com.FiveSGroup.TMS.LPN.LPNProductActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;

public class LPNwithSOAdapter extends RecyclerView.Adapter<LPNwithSOAdapter.ViewHolder> {

    final ArrayList<LPN> listLPN;
    Context context;

    View view;

    public LPNwithSOAdapter(Context context, ArrayList<LPN> listLPN) {
        this.context = context;
        this.listLPN = listLPN;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public LPNwithSOAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_item_lpnwithso, null, false);
        return new LPNwithSOAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LPNwithSOAdapter.ViewHolder holder, final int position) {
        final LPN lpn = listLPN.get(position);
        holder.tvCode.setText(lpn.getLPN_CODE());
        holder.tvDate.setText(lpn.getLPN_DATE());
        holder.tvSO.setText(lpn.getORDER_CODE());
        holder.tvSTT.setText(lpn.getLPN_NUMBER());
        holder.tvuser_create.setText(lpn.getUSER_CREATE());
        holder.tvstorage.setText(lpn.getSTORAGE());
//        holder.layout_item.setBackgroundColor(context.getResources().getColor(R.color.white));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // holder.layout_item.setBackgroundColor(context.getResources().getColor(R.color.grey04));
//                Toast.makeText(context, ""+lpn.getLPN_NUMBER()+" - "+lpn.getLPN_CODE()+" - "+lpn.getLPN_DATE(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LPNwithSOInfo.class);
                intent.putExtra("LPN_CODE", String.valueOf(lpn.getLPN_CODE()));
                global.setLpn_code(String.valueOf(lpn.getLPN_CODE()));
                intent.putExtra("order_code", String.valueOf(lpn.getORDER_CODE()));
                global.setOrder_code(String.valueOf(lpn.getORDER_CODE()));
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

        TextView tvCode, tvDate, tvSTT , tvuser_create , tvstorage ,tvSO;
        LinearLayout layout_item;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvSO = itemView.findViewById(R.id.tvSO);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvuser_create = itemView.findViewById(R.id.tvuser_create);
            tvstorage = itemView.findViewById(R.id.tvstorage);
            tvSTT = itemView.findViewById(R.id.tvSTT);
            layout_item = itemView.findViewById(R.id.layout_item);
        }
    }


}
