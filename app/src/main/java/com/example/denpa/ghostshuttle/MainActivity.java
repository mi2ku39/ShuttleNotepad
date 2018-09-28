package com.example.denpa.ghostshuttle;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.denpa.ghostshuttle.MainActivityFunctions.setViews;
import com.example.denpa.ghostshuttle.memofileaccessor.MemoFileManager;
import jp.ghostserver.ghostshuttle.ViewerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //変数宣言
    public FloatingActionButton fab ;
    public ListView listView;
    private int itemCount = 0, contextPosition = 0;
    private String context_title;
    private Boolean list_style;

    public CoordinatorLayout cl;

    private MemoDBHelper Helper = new MemoDBHelper(this);

    //画面遷移で使うやつ
    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        setViews.setPreferenceScreen(this);
        setViews.setToolbar(this);
        setViews.findIDs(this);
        setViews.initListView(this);

        //ListViewのアイテムがタップされたときの処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //データベースの取得・クエリ実行
                SQLiteDatabase read_db = Helper.getReadableDatabase();
                String title;

                if(list_style){
                    //変数宣言
                    ShuttleListItem item = (ShuttleListItem) listView.getItemAtPosition(position);
                    title = item.getmTitle();
                }else{
                    SimpleListItem item = (SimpleListItem) listView.getItemAtPosition(position);
                    title = item.getTitle();
                }

                    Cursor cursor = read_db.query("memo", new String[]{"filepath", "_id", "notifi_enabled"}, "title like '" + title + "'", null, null, null, null);
                    cursor.moveToFirst();

                if(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean("viewer_used",false)) {

                    //ViewerActivityへ値を渡す処理
                    Intent viewer = new Intent(getApplicationContext(), ViewerActivity.class);
                    viewer.putExtra("TITLE", title);
                    viewer.putExtra("MEMO", cursor.getString(0) + ".gs");
                    viewer.putExtra("_ID", cursor.getInt(1));
                    viewer.putExtra("Notifi", cursor.getInt(2));

                    //カーソルのクローズ
                    cursor.close();
                    read_db.close();

                    //Activity開始
                    startActivity(viewer);

                }else{

                    //EditActivityへ値を渡す処理
                    Intent editor = new Intent(getApplicationContext(), EditActivity.class);
                    editor.putExtra("TITLE", title);
                    editor.putExtra("MEMO", MemoFileManager.readFile(cursor.getString(0) + ".gs", MainActivity.this));
                    editor.putExtra("_ID", cursor.getInt(1));
                    editor.putExtra("flag", true);
                    editor.putExtra("Notifi", cursor.getInt(2));

                    //カーソルのクローズ
                    cursor.close();
                    read_db.close();

                    startActivity(editor);

                }
            }
        });

        listView.setEmptyView(findViewById(R.id.EmptyText));
        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        registerForContextMenu(listView);

        if(getIntent().getBooleanExtra("FLAG",false)){
            int id = getIntent().getIntExtra("ID",-1);

            //データベースの取得・クエリ実行
            SQLiteDatabase read_db = Helper.getReadableDatabase();
            Cursor cursor = read_db.query("memo",new String[] {"filepath","title","notifi_enabled"},"_id = '" + id + "'",null,null,null,null);

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                //EditActivityへ値を渡す処理
                Intent editer = new Intent(getApplicationContext(), EditActivity.class);
                editer.putExtra("TITLE", cursor.getString(1));
                editer.putExtra("MEMO", MemoFileManager.readFile(cursor.getString(0) + ".gs", this));
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

        if(list_style){
            ShuttleListItem item = (ShuttleListItem) listView.getItemAtPosition(info.position);
            contextPosition = info.position;
            context_title = item.getmTitle();
            String title = item.getmTitle();
            menu.setHeaderTitle(title);
        }else{
            SimpleListItem item = (SimpleListItem) listView.getItemAtPosition(info.position);
            contextPosition = info.position;
            context_title = item.getTitle();
            String title = item.getTitle();
            menu.setHeaderTitle(title);
        }

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
                alertDlg.setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // OK ボタンクリック処理
                                String title;

                                if(list_style) {
                                    ShuttleListItem item = (ShuttleListItem) listView.getItemAtPosition(contextPosition);
                                    title = item.getmTitle();
                                }else{
                                    SimpleListItem item = (SimpleListItem) listView.getItemAtPosition(contextPosition);
                                    title = item.getTitle();
                                }

                                //データベースの取得・クエリ実行
                                SQLiteDatabase write_db = Helper.getReadableDatabase();
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
                        getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel ボタンクリック処理
                            }
                        });

                // 表示
                alertDlg.create().show();
                break;

            case R.id.change_icon:
                if(list_style) {
                    ShuttleListItem list_item = (ShuttleListItem) listView.getItemAtPosition(contextPosition);

                    Intent Icon = new Intent(getApplicationContext(), iconActivity.class);
                    Icon.putExtra("memo_id",list_item.getId());
                    Icon.putExtra("memo_title",list_item.getmTitle());

                    Log.d("TEST",String.valueOf(list_item.getId()));

                    SQLiteDatabase read_db = Helper.getReadableDatabase();
                    Cursor cursor = read_db.query("memo",new String[] {"icon_img","icon_color"},"_id = '" + list_item.getId() + "'",null,null,null,null );
                    cursor.moveToFirst();
                    Icon.putExtra("icon_img",cursor.getString(0));
                    Icon.putExtra("icon_color",cursor.getString(1));

                    cursor.close();
                    read_db.close();

                    startActivity(Icon);
                    overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_under);
                }else{
                    SimpleListItem list_item = (SimpleListItem) listView.getItemAtPosition(contextPosition);

                    Log.d("test",String.valueOf(list_item.getId()));

                    Intent Icon = new Intent(getApplicationContext(), iconActivity.class);
                    Icon.putExtra("memo_id",list_item.getId());
                    Icon.putExtra("memo_title",list_item.getTitle());

                    Log.d("TEST",String.valueOf(list_item.getId()));

                    SQLiteDatabase read_db = Helper.getReadableDatabase();
                    Cursor cursor = read_db.query("memo",new String[] {"icon_img","icon_color"},"_id = '" + list_item.getId() + "'",null,null,null,null );
                    cursor.moveToFirst();
                    Icon.putExtra("icon_img",cursor.getString(0));
                    Icon.putExtra("icon_color",cursor.getString(1));

                    cursor.close();
                    read_db.close();

                    startActivity(Icon);
                    overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_under);
                }

                break;


        }
        return false;
    }


    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        list_style = pref.getBoolean("list_style",false);
        SyncList();
    }

    private void findid(){

        fab=findViewById(R.id.fab);
        listView =findViewById(R.id.listview);
        cl= findViewById(R.id.coordinatorLayout);

    }

    private void SyncList(){

        SQLiteDatabase read_db = Helper.getReadableDatabase();

        Cursor cursor = read_db.query("memo",new String[] {"title","data_modified","icon_img","icon_color","_id"},null,null,null,null,"data_modified desc" );
        cursor.moveToFirst();

        itemCount = cursor.getCount();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        if(pref.getBoolean("list_style",false)){
            ArrayList<ShuttleListItem> listItems = new ArrayList<>();
            for(int i=0;i<cursor.getCount();i++){
                int icon = getResources().getIdentifier(cursor.getString(2), "drawable",getPackageName());
                ShuttleListItem item = new ShuttleListItem(icon,cursor.getString(0),getResources().getString(R.string.cd) + cursor.getString(1),cursor.getString(3),cursor.getInt(4));
                listItems.add(item);
                cursor.moveToNext();
            }

            ShuttleListAdapter adapter = new ShuttleListAdapter(this, R.layout.shuttle_listitem, listItems);
            listView.setAdapter(adapter);
        }else{
            ArrayList<SimpleListItem> listItems = new ArrayList<>();
            for(int i=0;i<cursor.getCount();i++){
                SimpleListItem item = new SimpleListItem(cursor.getString(0),cursor.getInt(4));
                listItems.add(item);
                cursor.moveToNext();
            }

            SimpleListAdapter adapter = new SimpleListAdapter(this,R.layout.simple_listitem,listItems);
            listView.setAdapter(adapter);
        }

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
                if(itemCount != 0) {
                    Intent delete_intent = new Intent(this, DeleteActivity.class);
                    delete_intent.putExtra("item", itemCount);
                    startActivityForResult(delete_intent, REQUEST_CODE_ANOTHER_CALC_1);
                }else{
                    Snackbar.make(cl, getResources().getString(R.string.error_d), Snackbar.LENGTH_SHORT).show();
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



}
