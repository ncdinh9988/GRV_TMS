package com.FiveSGroup.TMS.AddCustomerFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class FragAddCustomer extends androidx.fragment.app.Fragment {

    private RecyclerView rvCustomer;

    private static final int ACTIVITY_RESULT_ADD_NEW = 1;

    private Button btnEdit, btnAddNew, btnDeleteAll;

    private CustomerAdapter adapter;
    ArrayList<CCustomer> arrCustomer;
    RelativeLayout rlAddNew;
    private EditText edSearch;
    private CharSequence valueFilter = "";
    private ArrayList<CCustomer> arrCustomerFilter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_add_customer, container, false);
        arrCustomer = new ArrayList<>();
        btnAddNew = (Button) view.findViewById(R.id.btnAddNew);
        rvCustomer = view.findViewById(R.id.rvCustomer);
        rlAddNew = view.findViewById(R.id.rlAddNew);
        edSearch = view.findViewById(R.id.edSearch);
        arrCustomerFilter = new ArrayList<>();

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CmnFns.isNetworkAvailable()) {
                    transfer();
                } else {
                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
                    final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                    InsetDrawable inset = new InsetDrawable(back, 64);
                    dialog.getWindow().setBackgroundDrawable(inset);
                    dialog.setView(layout_cus);

                    Button btnClose = layout_cus.findViewById(R.id.btnHuy);
                    TextView textView = layout_cus.findViewById(R.id.tvText);


                    textView.setText("Vui lòng bật kết nối mạng");
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                }
            }
        });
        if (CmnFns.isNetworkAvailable()) {
            prepareData();

            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                valueFilter = charSequence;
//                if (adapter != null) {
//                    adapter.getFilter().filter(charSequence); // truyền vào nội dung tìm
//
//                    // kiếm
//                }
                    filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {


                }
            });

            //  init();
        } else {
            LayoutInflater factory = LayoutInflater.from(getActivity());
            View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
            final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 64);
            dialog.getWindow().setBackgroundDrawable(inset);
            dialog.setView(layout_cus);

            Button btnClose = layout_cus.findViewById(R.id.btnHuy);
            TextView textView = layout_cus.findViewById(R.id.tvText);


            textView.setText("Vui lòng bật kết nối mạng");
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });
            dialog.show();
        }
        return view;

    }

    public void transfer() {
        Intent intentNew = new Intent(getActivity(), FCustomerAddNewEdit.class);
        intentNew.putExtra("AddNew", "1");
        startActivityForResult(intentNew, ACTIVITY_RESULT_ADD_NEW);
    }

    private void prepareData() {

        arrCustomer = DatabaseHelper.getInstance().getAllCustomerNew();
        if (arrCustomer.size() > 0) {
            adapter = new CustomerAdapter(getActivity(), arrCustomer);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            rvCustomer.setLayoutManager(layoutManager);
            rvCustomer.scrollToPosition(arrCustomer.size() - 1);
            rvCustomer.setAdapter(adapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        arrCustomer.clear();
        prepareData();

    }

    private void filter(CharSequence charSequence) {
        //new array list that will hold the filtered data
        arrCustomerFilter = new ArrayList<>();

        if (arrCustomer.size() > 0) {
            //looping through existing elements
            for (int i = 0; i < arrCustomer.size(); i++) {
                //if the existing elements contains the search input
                if (arrCustomer.get(i).getCustomerName().contains(charSequence.toString())
                        || arrCustomer.get(i).getCustomerCode().contains(charSequence.toString())) {
                    CCustomer customer = arrCustomer.get(i);
                    arrCustomerFilter.add(customer);
                    //adding the element to filtered list
                }
            }
            //calling a method of the adapter class and passing the filtered list
            adapter.filterList(arrCustomerFilter);
            // adapter.notifyDataSetChanged();
        }
    }
}
