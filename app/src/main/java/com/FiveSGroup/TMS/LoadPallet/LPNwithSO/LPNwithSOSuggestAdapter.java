package com.FiveSGroup.TMS.LoadPallet.LPNwithSO;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.LPN.LPN;
import com.FiveSGroup.TMS.LPN.LPNProductActivity;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class LPNwithSOSuggestAdapter extends RecyclerView.Adapter<LPNwithSOSuggestAdapter.ViewHolder> {

    final ArrayList<ProductLpnWithSo> listLPN;
    Context context;

    View view;

    public LPNwithSOSuggestAdapter(Context context, ArrayList<ProductLpnWithSo> listLPN) {
        this.context = context;
        this.listLPN = listLPN;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public LPNwithSOSuggestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_item_lpnwithsosuggest, null, false);
        return new LPNwithSOSuggestAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final LPNwithSOSuggestAdapter.ViewHolder holder, final int position) {
        final ProductLpnWithSo lpn = listLPN.get(position);
        holder.tvname.setText(lpn.getPRODUCT_NAME());
        holder.tvSO.setText(lpn.getPRODUCT_CODE());
        String unit = lpn.getSO_QTY();
        unit = unit.replace(",","\n");
        holder.tvunit.setText(unit);
        String suggest = lpn.getPOSITION_CODE();
        suggest = suggest.replace("</br> ","\n");
        holder.tvsuggest.setText(suggest);
//        holder.layout_item.setBackgroundColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return listLPN.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvname, tvSO , tvunit , tvsuggest ;
        LinearLayout layout_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tvname);
            tvSO = itemView.findViewById(R.id.tvSO);
            tvunit = itemView.findViewById(R.id.tvunit);
            tvsuggest = itemView.findViewById(R.id.tvsuggest);
            layout_item = itemView.findViewById(R.id.layout_item);


        }
    }


}
