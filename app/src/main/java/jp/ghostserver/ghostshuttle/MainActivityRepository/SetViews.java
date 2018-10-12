package jp.ghostserver.ghostshuttle.MainActivityRepository;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDBHelper;
import jp.ghostserver.ghostshuttle.EditActivityRepository.EditActivity;
import jp.ghostserver.ghostshuttle.ListViewClasses.EnhancedListView.ShuttleListAdapter;
import jp.ghostserver.ghostshuttle.ListViewClasses.EnhancedListView.ShuttleListItem;
import jp.ghostserver.ghostshuttle.ListViewClasses.SimpleListView.SimpleListAdapter;
import jp.ghostserver.ghostshuttle.ListViewClasses.SimpleListView.SimpleListItem;
import jp.ghostserver.ghostshuttle.ViewerActivity;
import jp.ghostserver.ghostshuttle.memofileaccessor.MemoFileManager;
import jp.ghostserver.ghostshuttle.preferenceaccessor.PreferenceAccessor;

import java.util.ArrayList;
import java.util.List;

public class SetViews {
    public static void findIDs(MainActivity targetActivity){
        targetActivity.fab = targetActivity.findViewById(R.id.fab);
        targetActivity.listView = targetActivity.findViewById(R.id.listview);
        targetActivity.cl = targetActivity.findViewById(R.id.coordinatorLayout);
        targetActivity.fab.setOnClickListener(targetActivity);
    }

    public static void setToolbar(MainActivity activity){
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.setTitle(activity.getResources().getString(R.string.app_name));
    }

    public static void setPreferenceScreen(MainActivity activity){
        PreferenceManager.getDefaultSharedPreferences(activity);
        PreferenceManager.setDefaultValues(activity, R.xml.preference_setting, true);
    }

    public static void initListView(final MainActivity activity){
        //ListViewのアイテムがタップされたときの処理
        activity.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemoDBHelper databaseHelper = new MemoDBHelper(activity);

                //データベースの取得・クエリ実行
                SQLiteDatabase read_db = databaseHelper.getReadableDatabase();
                String title;

                if(PreferenceAccessor.getListStyle(activity)){
                    //変数宣言
                    ShuttleListItem item = (ShuttleListItem) activity.listView.getItemAtPosition(position);
                    title = item.getmTitle();
                }else{
                    SimpleListItem item = (SimpleListItem)activity.listView.getItemAtPosition(position);
                    title = item.getTitle();
                }

                Cursor cursor = read_db.query("memo", new String[]{"filepath", "_id", "notifi_enabled"}, "title like '" + title + "'", null, null, null, null);
                cursor.moveToFirst();

                if(PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("viewer_used",false)) {

                    //ViewerActivityへ値を渡す処理
                    Intent viewer = new Intent(activity.getApplicationContext(), ViewerActivity.class);
                    viewer.putExtra("TITLE", title);
                    viewer.putExtra("MEMO", cursor.getString(0) + ".gs");
                    viewer.putExtra("_ID", cursor.getInt(1));
                    viewer.putExtra("Notifi", cursor.getInt(2));

                    //カーソルのクローズ
                    cursor.close();
                    read_db.close();

                    //Activity開始
                    activity.startActivity(viewer);

                }else{

                    //EditActivityへ値を渡す処理
                    Intent editor = new Intent(activity.getApplicationContext(), EditActivity.class);
                    editor.putExtra("TITLE", title);
                    editor.putExtra("MEMO", MemoFileManager.readFile(cursor.getString(0) + ".gs", activity));
                    editor.putExtra("_ID", cursor.getInt(1));
                    editor.putExtra("flag", true);
                    editor.putExtra("Notifi", cursor.getInt(2));

                    //カーソルのクローズ
                    cursor.close();
                    read_db.close();

                    activity.startActivity(editor);

                }
            }
        });

        activity.listView.setEmptyView(activity.findViewById(R.id.EmptyText));
        activity.listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        activity.registerForContextMenu(activity.listView);

        if(activity.getIntent().getBooleanExtra("FLAG",false)){
            int id = activity.getIntent().getIntExtra("ID",-1);

            //データベースの取得・クエリ実行
            MemoDBHelper databaseHelper = new MemoDBHelper(activity);
            SQLiteDatabase read_db = databaseHelper.getReadableDatabase();
            Cursor cursor = read_db.query("memo",new String[] {"filepath","title","notifi_enabled"},"_id = '" + id + "'",null,null,null,null);

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                //EditActivityへ値を渡す処理
                Intent editer = new Intent(activity.getApplicationContext(), EditActivity.class);
                editer.putExtra("TITLE", cursor.getString(1));
                editer.putExtra("MEMO", MemoFileManager.readFile(cursor.getString(0) + ".gs",activity));
                editer.putExtra("_ID", id);
                editer.putExtra("flag", true);
                editer.putExtra("Notifi", cursor.getInt(2));

                //カーソルのクローズ
                cursor.close();
                read_db.close();

                activity.startActivity(editer);
                activity.overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_under);

            }else
                Toast.makeText(activity, activity.getResources().getString(R.string.toast_delete), Toast.LENGTH_SHORT).show();
        }
    }

    public static void syncList(MainActivity activity){
        MemoDBHelper Helper = new MemoDBHelper(activity);
        SQLiteDatabase read_db = Helper.getReadableDatabase();

        Cursor cursor = read_db.query("memo",new String[] {"title","data_modified","icon_img","icon_color","_id"},null,null,null,null,"data_modified desc" );
        cursor.moveToFirst();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        if(pref.getBoolean("list_style",false)){
            List<ShuttleListItem> listItems = new ArrayList<>();
            for(int i=0; i<cursor.getCount(); i++){
                int icon = activity.getResources().getIdentifier(cursor.getString(2), "drawable",activity.getPackageName());
                ShuttleListItem item = new ShuttleListItem(icon,cursor.getString(0),activity.getResources().getString(R.string.cd) + cursor.getString(1),cursor.getString(3),cursor.getInt(4));
                listItems.add(item);
                cursor.moveToNext();
            }

            ShuttleListAdapter adapter = new ShuttleListAdapter(activity, R.layout.shuttle_listitem, listItems);
            activity.listView.setAdapter(adapter);
        }else{
            List<SimpleListItem> listItems = new ArrayList<>();
            for(int i=0; i<cursor.getCount(); i++){
                SimpleListItem item = new SimpleListItem(cursor.getString(0),cursor.getInt(4));
                listItems.add(item);
                cursor.moveToNext();
            }

            SimpleListAdapter adapter = new SimpleListAdapter(activity,R.layout.simple_listitem,listItems);
            activity.listView.setAdapter(adapter);
        }

        cursor.close();
        read_db.close();

    }
}
