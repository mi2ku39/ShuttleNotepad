package com.example.denpa.ghostshuttle;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

public class AppDetail extends AppCompatActivity implements View.OnClickListener{

    Button web,fed,osl_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);

        //画面上部の「戻るボタン」設定
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(getResources().getString(R.string.app_details));

        web = findViewById(R.id.web);
        web.setOnClickListener(this);

        fed = findViewById(R.id.button);
        fed.setOnClickListener(this);

        osl_bt = findViewById(R.id.osl_bt);
        osl_bt.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.web:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://ghostserver.xyz/page/works/ghostshuttle.html")));
                break;

            case R.id.button:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSemQXtfQoEv20pcXQXdINKmqdi1eBCElYawatEM6vaOKQvnrw/viewform?usp=sf_link")));
                break;

            case R.id.osl_bt:

                // カスタムビューを設定
                LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.riv_osl, (ScrollView) findViewById(R.id.riv));

                // アラーとダイアログ を生成
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.osl));
                builder.setView(layout);
                builder.setPositiveButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });

                // 表示
                builder.create().show();

                //Intent intent = new Intent(getApplicationContext(),OSLActivity.class);
                //startActivity(intent);

                break;
        }
    }

}
