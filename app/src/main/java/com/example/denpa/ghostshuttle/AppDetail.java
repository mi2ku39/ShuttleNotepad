package com.example.denpa.ghostshuttle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AppDetail extends AppCompatActivity implements View.OnClickListener{

    Button web,fed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);

        //画面上部の「戻るボタン」設定
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("このアプリについて");

        web = (Button)findViewById(R.id.web);
        web.setOnClickListener(this);

        fed = (Button)findViewById(R.id.button);
        fed.setOnClickListener(this);

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
                Uri uri = Uri.parse("http://ghostserver.xyz/page/works/ghostshuttle.html");
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
                break;

            case R.id.button:
                Uri uri2 = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSemQXtfQoEv20pcXQXdINKmqdi1eBCElYawatEM6vaOKQvnrw/viewform?usp=sf_link");
                Intent i2 = new Intent(Intent.ACTION_VIEW,uri2);
                startActivity(i2);
                break;
        }
    }

}
