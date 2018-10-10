package jp.ghostserver.ghostshuttle.MainActivityRepository;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.AppDetail;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDBHelper;
import jp.ghostserver.ghostshuttle.DeleteActivity;
import jp.ghostserver.ghostshuttle.EditActivityRepository.EditActivity;
import jp.ghostserver.ghostshuttle.ListViewClasses.EnhancedListView.ShuttleListItem;
import jp.ghostserver.ghostshuttle.ListViewClasses.SimpleListView.SimpleListItem;
import jp.ghostserver.ghostshuttle.SettingActivity;
import jp.ghostserver.ghostshuttle.iconActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //変数宣言
    public FloatingActionButton fab ;
    public ListView listView;
    public CoordinatorLayout cl;

    private int contextPosition = 0;
    private String context_title;
    private Boolean list_style;

    //画面遷移で使うやつ
    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        SetViews.setPreferenceScreen(this);
        SetViews.setToolbar(this);
        SetViews.findIDs(this);
        SetViews.initListView(this);

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
                                MemoDBHelper Helper = new MemoDBHelper(MainActivity.this);
                                SQLiteDatabase write_db = Helper.getWritableDatabase();
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

                                SetViews.syncList(MainActivity.this);
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

                    MemoDBHelper Helper = new MemoDBHelper(this);
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

                    MemoDBHelper Helper = new MemoDBHelper(this);
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
        SetViews.syncList(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.fab:
                //FABが押されたときの動作
                Intent intent = new Intent(this, EditActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ANOTHER_CALC_1);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.detail:
                Intent intent = new Intent(this, AppDetail.class);
                startActivityForResult(intent,REQUEST_CODE_ANOTHER_CALC_1);
                break;

            case R.id.mode:
                if(listView.getCount() != 0) {
                    Intent delete_intent = new Intent(this, DeleteActivity.class);
                    delete_intent.putExtra("item", listView.getCount());
                    startActivityForResult(delete_intent, REQUEST_CODE_ANOTHER_CALC_1);
                }else{
                    Snackbar.make(cl, getResources().getString(R.string.error_d), Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.settings:
                Intent setting_intent = new Intent(this, SettingActivity.class);
                    startActivity(setting_intent);
                break;

            default:
                break;
        }
        return true;
    }



}
