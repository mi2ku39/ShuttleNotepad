package jp.ghostserver.ghostshuttle.domain.selected_memo_delete;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDBHelper;

import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {

    ListView listview;
    static ArrayAdapter<String> adapter;
    MemoDBHelper DBHelper = new MemoDBHelper(this);
    TextView debug;
    int itemcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //コメント
        setTitle(getResources().getString(R.string.choose_delete));

        ArrayList<String> arraylist = new ArrayList<>();
        listview = findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice,arraylist);
        listview.setAdapter(adapter);

        Intent ei = getIntent();
        itemcount = ei.getIntExtra("item",0);

        debug = findViewById(R.id.textView6);

        SyncList();

    }

    //ActionBarのメニューを設定
    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void SyncList(){

        adapter.clear();

        SQLiteDatabase read_db = DBHelper.getReadableDatabase();

        Cursor cursor = read_db.query("memo",new String[] {"title","data_modified"},null,null,null,null,"data_modified asc" );
        cursor.moveToFirst();

        for(int i=0;i<cursor.getCount();i++){

            //ListViewに追加（1レコードのみ）
            adapter.insert(cursor.getString(0),0);

            //カーソルを次のレコードへ進める
            cursor.moveToNext();
        }

        cursor.close();
        read_db.close();

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

            case R.id.done:

                String Selected = "";
                int counter = 0;

                SparseBooleanArray checked = listview.getCheckedItemPositions();
                for(int i=0; i <= itemcount;i++){

                    if(checked.get(i) == true && counter < 4){
                        Selected = Selected + listview.getItemAtPosition(i) + "\n";
                    }

                    if(checked.get(i) == true)
                        counter++;

                }

                if(counter > 4)
                    Selected = Selected + getResources().getString(R.string.etc);

                //ダイアログの表示
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(DeleteActivity.this);
                alertDlg.setTitle(getResources().getString(R.string.choose_delete));
                alertDlg.setMessage(Selected + "\n" + getResources().getString(R.string.select_erase));
                alertDlg.setPositiveButton(
                        getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // OK ボタンクリック処理

                                //マップの情報を取得する
                                SparseBooleanArray checked = listview.getCheckedItemPositions();

                                for(int i=0; i <= itemcount;i++){
                                    if(checked.get(i) == true){
                                        String query_id ="SELECT _id FROM memo where title='"+ listview.getItemAtPosition(i) +"'";

                                        //データベースの取得・クエリ実行
                                        SQLiteDatabase write_db = DBHelper.getWritableDatabase();
                                        Cursor cursor_id = write_db.rawQuery(query_id,null);

                                        //データベースからの情報を格納する変数のゼロクリア
                                        int db_id = 0;

                                        //データベースから取得したデータを変数へ格納
                                        //db_id=データベース上の主キー
                                        if(cursor_id.moveToFirst()){
                                            db_id = cursor_id.getInt(0);
                                        }

                                        write_db.delete("memo", "_id = " + db_id, null);
                                        write_db.delete("NOTIFICATION","_id = " + db_id,null);
                                        deleteFile( listview.getItemAtPosition(i) + ".gs" );
                                    }
                                }

                                finish();
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
