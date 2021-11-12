package com.FiveSGroup.TMS;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        loadPage task  = new loadPage();
        task.execute();
          // khỏi tạo thư mục chứa hình ảnh
          CmnFns.createFolder(Environment.getExternalStorageDirectory()
                  + File.separator
                  + global.getAppContext().getString(
                  R.string.PathFolderLog));



    }
    class loadPage extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent it = new Intent(MainActivity.this,InputCodeActivity.class);
            startActivity(it);
            finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
