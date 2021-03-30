package com.FiveSGroup.TMS.AddCustomerFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>  {

    private Context context;
    ArrayList<CCustomer> arrCustomer;
    ArrayList<CCustomer> arrCustomerFilter;

   // private CCustomerFilter vfilter;


    public CustomerAdapter(Context context, ArrayList<CCustomer> arrCustomer) {
        this.context = context;
        this.arrCustomer = arrCustomer;
       // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_item_customer, null, false);
        return new CustomerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomerAdapter.ViewHolder holder, final int position) {

        final CCustomer cCustomer = arrCustomer.get(position);
        if (cCustomer != null) {
            holder.tvCodeCustomer.setText(cCustomer.getCustomerCode());
            holder.tvNameCustomer.setText(cCustomer.getCustomerName());

            try {
                holder.tvPhoneNumber.setText(cCustomer.getCustomerPhoneNumber());

            } catch (Exception e) {

            }
            holder.tvCreateDate.setText(cCustomer.getCustomerCreatedDate());


            holder.tvAddress.setText(cCustomer.getCustomerAddress());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();

                    Intent intent = new Intent(context, FCustomerAddNewEdit.class);
                    EventBus.getDefault().postSticky(new CustomerEventbus(cCustomer));
                    context.startActivity(intent);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                        LayoutInflater factory = LayoutInflater.from(context);
                        View layout_cus = factory.inflate(R.layout.layout_cus, null);
                        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                        InsetDrawable inset = new InsetDrawable(back, 64);
                        dialog.getWindow().setBackgroundDrawable(inset);
                        dialog.setView(layout_cus);
                        Button btnConfirm = layout_cus.findViewById(R.id.btnDongY);
                        Button btnClose = layout_cus.findViewById(R.id.btnHuy);

                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();

                            }
                        });

                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                arrCustomer.remove(cCustomer);
//                                notifyDataSetChanged();
                                removeItem(position);
                                notifyDataSetChanged();
                                DatabaseHelper.getInstance().deleteCustomerAddNew(cCustomer.getCustomerCode());
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    return true;
                }
            });

        }


    }



    @Override
    public int getItemCount() {
        return arrCustomer.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCodeCustomer, tvNameCustomer, tvPhoneNumber, tvAddress, tvCreateDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodeCustomer = itemView.findViewById(R.id.tvCodeCustomer);
            tvNameCustomer = itemView.findViewById(R.id.tvNameCustomer);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneCCustomer);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCreateDate = itemView.findViewById(R.id.tvCreateDate);

        }
    }
    private void removeItem(int position){

        arrCustomer.remove(position);
        notifyItemRemoved(position);
    }
//    @Override
//    public Filter getFilter() {
//        if(vfilter == null){
//            vfilter = new CCustomerFilter();
//
//        }
//        return vfilter;
//    }
//
//    public class CCustomerFilter extends Filter{
//
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            String charString = charSequence.toString();
//            FilterResults result = new FilterResults();
//            if(charString.isEmpty()){
//                arrCustomerFilter = arrCustomer;
//            }else{
//                ArrayList<CCustomer> filterList = new ArrayList<>();
//                for(CCustomer cCustomer : arrCustomer){
//                    if(cCustomer.autoFilter().toLowerCase().contains(charString)){
//                        filterList.add(cCustomer);
//
//                    }
//                }
//                arrCustomerFilter = filterList;
//            }
//            result.values = arrCustomerFilter;
//            result.count = arrCustomerFilter.size();
//            return result;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            arrCustomerFilter = (ArrayList<CCustomer>) filterResults.values;
//            notifyDataSetChanged();
//        }
//    }

    public void filterList(ArrayList<CCustomer> cFilter) {
        this.arrCustomer = cFilter;
        notifyDataSetChanged();
    }
}
