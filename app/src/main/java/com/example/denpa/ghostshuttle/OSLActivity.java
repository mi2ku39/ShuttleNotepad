package com.example.denpa.ghostshuttle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class OSLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osl);

        //画面上部の「戻るボタン」設定
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //画面上部のタイトル設定
        setTitle(getResources().getString(R.string.osl));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //「戻るボタン」のクリックイベント
            case android.R.id.home:

                finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
