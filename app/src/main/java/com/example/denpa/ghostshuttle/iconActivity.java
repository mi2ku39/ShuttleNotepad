package com.example.denpa.ghostshuttle;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class iconActivity extends AppCompatActivity {

    GridView icon_grid,bgc_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("アイコンの変更");

        icon_grid = findViewById(R.id.grid_icon);
        icon_grid.setAdapter(new GridAdapter(this));

        bgc_grid = findViewById(R.id.grid_bgc);
        bgc_grid.setAdapter(new ColorAdapter(this));
    }

    //ActionBarのメニューを設定
    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.icon_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        boolean result = true;
        switch(id){

            //「戻るボタン」のクリックイベント
            case android.R.id.home:

                finish();
                break;

            case R.id.ok:

            default:

                result = super.onOptionsItemSelected(item);

        }

        return result;

    }

    //戻るキーのクリックイベント
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                finish();

                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
