package com.FiveSGroup.TMS.LPN;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.FiveSGroup.TMS.R;

import java.util.List;

public class LPNProductAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<LPNProduct> lpnProducts;

    public LPNProductAdapter(Context context, int layout, List<LPNProduct> lpnProducts) {
        this.context = context;
        this.layout = layout;
        this.lpnProducts = lpnProducts;
    }

    @Override
    public int getCount() {
        return lpnProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return lpnProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        private TextView tvProductLPNCode,
                tvProductLPNName,
                tvProductLPNExpDate,
                tvProductLPNStockinDate,
                tvProductLPNAmount,
                tvProductLPNUnit,
                tvProductLPNCurrentPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.tvProductLPNCode = convertView.findViewById(R.id.tvProductLPNCode);
            viewHolder.tvProductLPNName = convertView.findViewById(R.id.tvProductLPNName);
            viewHolder.tvProductLPNExpDate = convertView.findViewById(R.id.tvProductLPNExpDate);
            viewHolder.tvProductLPNStockinDate = convertView.findViewById(R.id.tvProductLPNStockinDate);
            viewHolder.tvProductLPNAmount = convertView.findViewById(R.id.tvProductLPNAmount);
            viewHolder.tvProductLPNUnit = convertView.findViewById(R.id.tvProductLPNUnit);
            viewHolder.tvProductLPNCurrentPosition = convertView.findViewById(R.id.tvProductLPNCurrentPosition);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LPNProduct lpnProduct = (LPNProduct) getItem(position);

        viewHolder.tvProductLPNCode.setText(lpnProduct.getProductCode());
        viewHolder.tvProductLPNName.setText(lpnProduct.getProductName());
        viewHolder.tvProductLPNExpDate.setText(lpnProduct.getProductExpDate());
        viewHolder.tvProductLPNStockinDate.setText(lpnProduct.getProductStockDate());
        viewHolder.tvProductLPNAmount.setText(lpnProduct.getProductAmount());
        viewHolder.tvProductLPNUnit.setText(lpnProduct.getProductUnit());
        viewHolder.tvProductLPNCurrentPosition.setText(lpnProduct.getProductCurrentPosition());
        return convertView;
    }
}
