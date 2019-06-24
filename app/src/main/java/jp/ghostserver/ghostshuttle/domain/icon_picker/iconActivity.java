package jp.ghostserver.ghostshuttle.domain.icon_picker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.entities.memo.MemoDBHelper;
import jp.ghostserver.ghostshuttle.domain.comopnents.ColorGridView.ColorAdapter;
import jp.ghostserver.ghostshuttle.domain.comopnents.ColorGridView.ColorGridItem;
import jp.ghostserver.ghostshuttle.domain.comopnents.IconGridItem.GridAdapter;
import jp.ghostserver.ghostshuttle.domain.comopnents.IconGridItem.IconGridItem;

import java.util.ArrayList;

public class iconActivity extends AppCompatActivity implements View.OnClickListener{

    GridView icon_grid,bgc_grid;
    String now_color="#ffffff",memo_title = null,now_icon = "paper";
    int memo_id;
    FloatingActionButton fab;

    MemoDBHelper DBHelper = new MemoDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        Intent ei = getIntent();
        memo_id = ei.getIntExtra("memo_id",-1);
        memo_title = ei.getStringExtra("memo_title");
        now_icon = ei.getStringExtra("icon_img");
        now_color = ei.getStringExtra("icon_color");
        Log.d("test",now_icon +" "+now_color);

        setTitle(getResources().getString(R.string.icon_change));

        icon_grid = findViewById(R.id.grid_icon);
        bgc_grid = findViewById(R.id.grid_bgc);

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
                now_icon = item.getIcon_name();
                SyncGrid();
            }
        });

        SyncGrid();

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.fab:
                Log.d("test",now_icon);
                Log.d("test",now_color);

                SQLiteDatabase memo_db = DBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("icon_img",now_icon);
                values.put("icon_color",now_color);
                memo_db.update("memo", values, "_id = " + memo_id , null);

                memo_db.close();
                finish();
                break;

            default:
                break;
        }
    }

    private void SyncGrid(){
        String color[] = {"#ffffff","#E0E0E0","#9E9E9E","#ef9a9a","#90CAF9","#A5D6A7","#FFF59D","#FFAB91","#ffcdd2","#BBDEFB","#C8E6C9","#FFF9C4","#FFCCBC","#CFD8DC"};
        Log.d("test",now_icon +" "+now_color);
        boolean match_color;
        ArrayList<ColorGridItem> listItems = new ArrayList<>();

        for(int i=0;i<color.length;i++){
            if(color[i].equals(now_color)){
                match_color = true;
            }else{
                match_color = false;
            }
            ColorGridItem item = new ColorGridItem(color[i],match_color);
            listItems.add(item);
        }

        ColorAdapter adapter = new ColorAdapter(this, R.layout.icon_item, listItems);
        bgc_grid.setAdapter(adapter);

        int icon_Array[] = {R.drawable.eraser,R.drawable.heart,R.drawable.paper,R.drawable.pencil,R.drawable.rice};
        String icon_name[] = {"eraser","heart","paper","pencil","rice"};
        ArrayList<IconGridItem> listItems_icon = new ArrayList<>();
        boolean match_icon;
        for(int i=0;i<icon_Array.length;i++){
            if(icon_name[i].equals(now_icon)){
                match_icon = true;
            }else{
                match_icon = false;
            }
            IconGridItem item = new IconGridItem(icon_Array[i],icon_name[i],now_color,match_icon);
            listItems_icon.add(item);
        }
        GridAdapter adapter_icon = new GridAdapter(this,R.layout.icon_item,listItems_icon);
        icon_grid.setAdapter(adapter_icon);
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
