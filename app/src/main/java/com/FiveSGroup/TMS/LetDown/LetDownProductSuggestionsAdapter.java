package com.FiveSGroup.TMS.LetDown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.FiveSGroup.TMS.R;

import java.util.List;

public class LetDownProductSuggestionsAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<LetDownProductSuggest> letDownProductSuggests;

    public LetDownProductSuggestionsAdapter(Context context, int layout, List<LetDownProductSuggest> letDownProductSuggests) {
        this.context = context;
        this.layout = layout;
        this.letDownProductSuggests = letDownProductSuggests;
    }

    @Override
    public int getCount() {
        return letDownProductSuggests.size();
    }

    @Override
    public Object getItem(int position) {
        return letDownProductSuggests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        private TextView tvProductSuggestionSTT,
                tvProductSuggestionName,
                tvProductSuggestionAmount,
                tvProductSuggestionUnit,
                tvProductSuggestionPositionFrom,
                tvProductSuggestionPositionTo,
                tvProductSuggestUnit,
                tvCodeProductSuggest,
                tvProductSuggestionExpDate,
                tvProductSuggestionStockDate,
                tvProductSuggestionVTGY;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.tvProductSuggestionName= convertView.findViewById(R.id.tvProductSuggestName);
            viewHolder.tvCodeProductSuggest = convertView.findViewById(R.id.tvCodeProductSuggest);
            viewHolder.tvProductSuggestUnit = convertView.findViewById(R.id.tvProductSuggestUnit);
            viewHolder.tvProductSuggestionAmount = convertView.findViewById(R.id.tvProductSuggestAmount);
            viewHolder.tvProductSuggestionPositionFrom = convertView.findViewById(R.id.tvProductSuggestPositionFrom);
            viewHolder.tvProductSuggestionPositionTo = convertView.findViewById(R.id.tvProductSuggestPositionTo);
            viewHolder.tvProductSuggestionExpDate = convertView.findViewById(R.id.tvProductSuggestExpDate);
            viewHolder.tvProductSuggestionStockDate = convertView.findViewById(R.id.tvProductSuggestStockDate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LetDownProductSuggest productSuggest = (LetDownProductSuggest) getItem(position);
        viewHolder.tvProductSuggestionName.setText(productSuggest.getProductName());
        viewHolder.tvCodeProductSuggest.setText(productSuggest.getProductCode());
        viewHolder.tvProductSuggestionAmount.setText(productSuggest.getProductAmount());
        viewHolder.tvProductSuggestUnit.setText(productSuggest.getProductUnit());
        viewHolder.tvProductSuggestionPositionFrom.setText(productSuggest.getProductPositionFrom());
        viewHolder.tvProductSuggestionPositionTo.setText(productSuggest.getProductPositionTo());
        viewHolder.tvProductSuggestionExpDate.setText(productSuggest.getProductExpDate());
        viewHolder.tvProductSuggestionStockDate.setText(productSuggest.getProductStockDate());

        return convertView;
    }
}
