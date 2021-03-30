package com.FiveSGroup.TMS;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.StockTransfer.ListStockTransfer;

import java.util.ArrayList;

public class InitIntent {

    private static  InitIntent Init;

    public static InitIntent getInstance() {
        if (Init != null) {
            return Init;
        } else {
            return new InitIntent();
        }
    }

    public static void ReturnPosition(Context context, Class A, String barcode, String position, String ea_unit_position, String product_cd,
                                      String stock, String detectToFinish, String stockinDate, String expPosition, ArrayList<Ea_Unit_Tam> ea_unit_tams ){

        Intent intent = new Intent(context, A);
        intent.putExtra("btn1", barcode);
        intent.putExtra("returnposition", position);
        intent.putExtra("return_ea_unit_position", ea_unit_position);
        intent.putExtra("returnCD", product_cd);
        intent.putExtra("returnStock", stock);
        intent.putExtra("detect_to_finish", detectToFinish);
        if (stockinDate == null) {
            intent.putExtra("stockin_date", stockinDate);

        } else {
            intent.putExtra("stockin_date", stockinDate);
        }
        if(ea_unit_tams.size() > 0){
            intent.putExtra("ea_unit", ea_unit_tams.get(0).getEA_UNIT_TAM());

        }else{
            intent.putExtra("ea_unit", " ");
        }

        // truyền qua để xử lí from - to
        intent.putExtra("expdate", expPosition);


        context.startActivity(intent);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        //editor.commit();
    }
}
