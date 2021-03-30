package com.FiveSGroup.TMS.AddBarcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.FiveSGroup.TMS.R;

import java.util.List;

public class ProductBarcodeAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ProductBarcode> productBarcodes;

    public ProductBarcodeAdapter(Context context, int layout, List<ProductBarcode> productBarcodes) {
        this.context = context;
        this.layout = layout;
        this.productBarcodes = productBarcodes;
    }

    @Override
    public int getCount() {
        return productBarcodes.size();
    }

    @Override
    public Object getItem(int position) {
        return productBarcodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView tvCode, tvName, tvBarcode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  layoutInflater.inflate(layout, null);
            viewHolder.tvCode = convertView.findViewById(R.id.tvItemCode);
            viewHolder.tvName = convertView.findViewById(R.id.tvItemName);
            viewHolder.tvBarcode = convertView.findViewById(R.id.tvItemBarcode);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductBarcode productBarcode = (ProductBarcode) getItem(position);
        viewHolder.tvCode.setText(productBarcode.getCode());
        viewHolder.tvName.setText(productBarcode.getName());
        viewHolder.tvBarcode.setText(productBarcode.getBarcode());

        return convertView;
    }
}
