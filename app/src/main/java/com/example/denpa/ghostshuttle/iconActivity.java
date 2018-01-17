package com.example.denpa.ghostshuttle;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import static com.example.denpa.ghostshuttle.DeleteActivity.adapter;

public class iconActivity extends AppCompatActivity {

    GridView icon_grid,bgc_grid;
    String now_color = "#ffffff",memo_title = null;
    long memo_id;

    MemoDBHelper DBHelper = new MemoDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent ei = getIntent();
        memo_id = ei.getLongExtra("memo_id",-1);
        memo_title = ei.getStringExtra("memo_title");

        setTitle(memo_title + "のアイコンの変更");

        icon_grid = findViewById(R.id.grid_icon);
        bgc_grid = findViewById(R.id.grid_bgc);
        String color[] = {"#ffffff","#E0E0E0","#9E9E9E","#ef9a9a","#90CAF9","#A5D6A7","#FFF59D","#FFAB91"};
        ArrayList<ColorGridItem> listItems = new ArrayList<>();

        for(int i=0;i<color.length;i++){
            ColorGridItem item = new ColorGridItem(color[i]);
            listItems.add(item);
        }

        SyncGrid();

       ColorAdapter adapter = new ColorAdapter(this, R.layout.icon_item, listItems);
        bgc_grid.setAdapter(adapter);

        bgc_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ColorGridItem item = (ColorGridItem)bgc_grid.getItemAtPosition(position);
               now_color = item.getColor();
               SyncGrid();
            }
        });

        icon_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconGridItem item = (IconGridItem)icon_grid.getItemAtPosition(position);

                Log.d("test",item.getIcon_name());
                Log.d("test",now_color);

                SQLiteDatabase memo_db = DBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("icon_img",item.getIcon_name());
                values.put("icon_color",now_color);
                memo_db.update("memo", values, "_id = " + memo_id , null);

                memo_db.close();
                finish();
            }
        });

    }

    private void SyncGrid(){
        int icon_Array[] = {R.drawable.eraser,R.drawable.heart,R.drawable.paper,R.drawable.pencil,R.drawable.rice};
        String icon_name[] = {"eraser","heart","paper","pencil","rice"};
        ArrayList<IconGridItem> listItems_icon = new ArrayList<>();
        for(int i=0;i<icon_Array.length;i++){
            IconGridItem item = new IconGridItem(icon_Array[i],icon_name[i],now_color);
            listItems_icon.add(item);
        }
        GridAdapter adapter_icon = new GridAdapter(this,R.layout.icon_item,listItems_icon);
        icon_grid.setAdapter(adapter_icon);
    }

    //ActionBarのメニューを設定
    /*
    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.icon_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    */

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
