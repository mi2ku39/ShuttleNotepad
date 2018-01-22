package com.example.denpa.ghostshuttle;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //変数宣言
    FloatingActionButton fab ;
    ListView listview;
    TextView debugMes;
    int itemcount = 0,context_potision = 0;
    String context_title;

    CoordinatorLayout cl;

    MemoDBHelper DBHelper = new MemoDBHelper(this);

    //画面遷移で使うやつ
    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getResources().getString(R.string.app_name));

        //findViewByIdをする関数
        findid();
        //setOnClickListenerをする関数
        setlistener();

        listview = findViewById(R.id.listview);
        //ListViewのアイテムがタップされたときの処理
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //変数宣言
                ShuttleListItem item = (ShuttleListItem)listview.getItemAtPosition(position);
                String title = item.getmTitle();

                //データベースの取得・クエリ実行
                SQLiteDatabase read_db = DBHelper.getReadableDatabase();
                Cursor cursor = read_db.query("memo",new String[] {"filepath","_id","notifi_enabled"},"title like '" + title + "'",null,null,null,null);

                cursor.moveToFirst();

                //EditActivityへ値を渡す処理
                Intent editer = new Intent(getApplicationContext(),EditActivity.class);
                editer.putExtra("TITLE", title);
                editer.putExtra("MEMO", readFile(cursor.getString(0) + ".gs"));
                editer.putExtra("_ID", cursor.getInt(1));
                editer.putExtra("flag",true);
                editer.putExtra("Notifi",cursor.getInt(2));

                //カーソルのクローズ
                cursor.close();
                read_db.close();

                startActivity(editer);

            }
        });

        listview.setEmptyView(findViewById(R.id.EmptyText));
        listview.setChoiceMode(ListView.CHOICE_MODE_NONE);
        registerForContextMenu(listview);

        SyncList();

        if(getIntent().getBooleanExtra("FLAG",false)){
            int id = getIntent().getIntExtra("ID",-1);

            //データベースの取得・クエリ実行
            SQLiteDatabase read_db = DBHelper.getReadableDatabase();
            Cursor cursor = read_db.query("memo",new String[] {"filepath","title","notifi_enabled"},"_id = '" + id + "'",null,null,null,null);

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                //EditActivityへ値を渡す処理
                Intent editer = new Intent(getApplicationContext(), EditActivity.class);
                editer.putExtra("TITLE", cursor.getString(1));
                editer.putExtra("MEMO", readFile(cursor.getString(0) + ".gs"));
                editer.putExtra("_ID", id);
                editer.putExtra("flag", true);
                editer.putExtra("Notifi", cursor.getInt(2));

                //カーソルのクローズ
                cursor.close();
                read_db.close();

                startActivity(editer);
                overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_under);

            }else
                Toast.makeText(this, getResources().getString(R.string.toast_delete), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo Info) {
        super.onCreateContextMenu(menu, v, Info);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) Info;

        ShuttleListItem item = (ShuttleListItem)listview.getItemAtPosition(info.position);
        context_potision = info.position;
        context_title = item.getmTitle();
        String title = item.getmTitle();
        menu.setHeaderTitle(title);

        getMenuInflater().inflate(R.menu.context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.delete_memo:

                //ダイアログの表示
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(MainActivity.this);
                alertDlg.setTitle(context_title);
                alertDlg.setMessage(getResources().getString(R.string.delete_question));
                alertDlg.setPositiveButton(
                        "いいよ！",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // OK ボタンクリック処理
                                ShuttleListItem item = (ShuttleListItem) listview.getItemAtPosition(context_potision);
                                String title = item.getmTitle();

                                //データベースの取得・クエリ実行
                                SQLiteDatabase write_db = DBHelper.getReadableDatabase();
                                Cursor cursor = write_db.query("memo", new String[]{"filepath", "_id"}, "title = '" + title + "'", null, null, null, null);

                                //データベースからの情報を格納する変数のゼロクリア
                                cursor.moveToFirst();
                                int db_id = cursor.getInt(1);
                                String path = cursor.getString(0);

                                write_db.delete("memo", "_id = " + db_id, null);
                                write_db.delete("NOTIFICATION", "_id = " + db_id, null);

                                deleteFile(path + ".gs");

                                cursor.close();
                                write_db.close();

                                SyncList();
                            }
                        });
                alertDlg.setNegativeButton(
                        "ダメです",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel ボタンクリック処理
                            }
                        });

                // 表示
                alertDlg.create().show();
                break;

            case R.id.change_icon:
                ShuttleListItem list_item = (ShuttleListItem) listview.getItemAtPosition(context_potision);

                Log.d("test",String.valueOf(list_item.getId()));

                Intent Icon = new Intent(getApplicationContext(), iconActivity.class);
                Icon.putExtra("memo_id",list_item.getId());
                Icon.putExtra("memo_title",list_item.getmTitle());

                Log.d("TEST",String.valueOf(list_item.getId()));

                SQLiteDatabase read_db = DBHelper.getReadableDatabase();
                Cursor cursor = read_db.query("memo",new String[] {"icon_img","icon_color"},"_id = '" + list_item.getId() + "'",null,null,null,null );
                cursor.moveToFirst();
                Icon.putExtra("icon_img",cursor.getString(0));
                Icon.putExtra("icon_color",cursor.getString(1));

                cursor.close();
                read_db.close();

                startActivity(Icon);
                overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_under);

                break;


        }
        return false;
    }


    @Override
    public void onResume(){
        super.onResume();
        SyncList();
    }

    private void findid(){

        fab=findViewById(R.id.fab);
        listview=findViewById(R.id.listview);
        cl= findViewById(R.id.coordinatorLayout);

        debugMes=findViewById(R.id.debugMes);


    }

    private void setlistener(){

        fab.setOnClickListener(this);

    }

    private void SyncList(){

        SQLiteDatabase read_db = DBHelper.getReadableDatabase();

        Cursor cursor = read_db.query("memo",new String[] {"title","data_modified","icon_img","icon_color","_id"},null,null,null,null,"data_modified desc" );
        cursor.moveToFirst();

        itemcount = cursor.getCount();
        debugMes.setText("アイテム数 : " + cursor.getCount());

        ArrayList<ShuttleListItem> listItems = new ArrayList<>();
        for(int i=0;i<cursor.getCount();i++){
            int icon = getResources().getIdentifier(cursor.getString(2), "drawable",getPackageName());
            ShuttleListItem item = new ShuttleListItem(icon,cursor.getString(0),"作成日時(UTC) : " + cursor.getString(1),cursor.getString(3),cursor.getInt(4));
            listItems.add(item);
            cursor.moveToNext();
        }

        ShuttleListAdapter adapter = new ShuttleListAdapter(this, R.layout.shuttle_listitem, listItems);
        listview.setAdapter(adapter);


        cursor.close();
        read_db.close();

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.fab:
                //FABが押されたときの動作
                Intent intent = new Intent(this,EditActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ANOTHER_CALC_1);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.detail:
                Intent intent = new Intent(this,AppDetail.class);
                startActivityForResult(intent,REQUEST_CODE_ANOTHER_CALC_1);
                break;

            case R.id.mode:
                if(itemcount != 0) {
                    Intent delete_intent = new Intent(this, DeleteActivity.class);
                    delete_intent.putExtra("item",itemcount);
                    startActivityForResult(delete_intent, REQUEST_CODE_ANOTHER_CALC_1);
                }else{
                    Snackbar.make(cl, "削除する項目がありません。", Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.settings:
                    Intent setting_intent = new Intent(this,SettingActivity.class);
                    startActivity(setting_intent);
                break;

            default:
                break;
        }
        return true;
    }

    public String readFile(String file){
        String str = "";
        String tmp;
        try{
            FileInputStream in = openFileInput( file );
            BufferedReader reader = new BufferedReader( new InputStreamReader( in , "UTF-8") );
            while( (tmp = reader.readLine()) != null ){
                str = str + tmp + "\n";
            }
            reader.close();
        }catch( IOException e ){
            e.printStackTrace();
        }

        return str;
    }

}
