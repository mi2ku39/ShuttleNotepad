package jp.ghostserver.ghostshuttle.MainActivityRepository;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDataBaseRecord;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDatabaseAccessor;
import jp.ghostserver.ghostshuttle.EditActivityRepository.EditActivity;
import jp.ghostserver.ghostshuttle.ListViewClasses.BaseShuttleListItem;
import jp.ghostserver.ghostshuttle.ListViewClasses.EnhancedListView.ShuttleListAdapter;
import jp.ghostserver.ghostshuttle.ListViewClasses.EnhancedListView.ShuttleListItem;
import jp.ghostserver.ghostshuttle.ListViewClasses.SimpleListView.SimpleListAdapter;
import jp.ghostserver.ghostshuttle.ListViewClasses.SimpleListView.SimpleListItem;
import jp.ghostserver.ghostshuttle.ViewerActivity;
import jp.ghostserver.ghostshuttle.memofileaccessor.MemoFileManager;

import java.util.ArrayList;
import java.util.List;

public class SetViews {
    /**
     * ID見つけてくるやつ
     *
     * @param targetActivity
     */
    public static void findIDs(MainActivity targetActivity) {
        targetActivity.fab = targetActivity.findViewById(R.id.fab);
        targetActivity.listView = targetActivity.findViewById(R.id.listview);
        targetActivity.cl = targetActivity.findViewById(R.id.coordinatorLayout);
        targetActivity.fab.setOnClickListener(targetActivity);
    }

    /**
     * Toolbarをセットするやつ
     * @param activity
     */
    public static void setToolbar(MainActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.setTitle(activity.getResources().getString(R.string.app_name));
    }

    /**
     * Preference Screenを設定するやつ
     * @param activity
     */
    public static void setPreferenceScreen(MainActivity activity) {
        PreferenceManager.getDefaultSharedPreferences(activity);
        PreferenceManager.setDefaultValues(activity, R.xml.preference_setting, true);
    }

    /**
     * ListVIewを初期化する。
     * @param activity
     */
    public static void initListView(final MainActivity activity) {
        //ListViewのアイテムがタップされたときの処理
        activity.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BaseShuttleListItem item = (BaseShuttleListItem) activity.listView.getItemAtPosition(position);
                int itemID = item.getID();

                wakeupMemoViewerById(activity, itemID);

            }
        });

        activity.listView.setEmptyView(activity.findViewById(R.id.EmptyText));
        activity.listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        activity.registerForContextMenu(activity.listView);

    }


    /**
     * 通知からの起動かどうかチェックして、通知からの起動の場合はビュワーを起動する。
     * @param activity
     */
    public static void checkStartAppFromNotify(MainActivity activity) {
        activity.listView.setEmptyView(activity.findViewById(R.id.EmptyText));
        activity.listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        activity.registerForContextMenu(activity.listView);

        if (activity.getIntent().getBooleanExtra("FLAG", false)) {
            int id = activity.getIntent().getIntExtra("ID", -1);
            MemoDataBaseRecord record = MemoDatabaseAccessor.getRecordById(activity, id);

            if (record != null) {
                wakeupMemoViewerById(activity, record.getID());
                activity.overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_under);

            } else
                Toast.makeText(activity, activity.getResources().getString(R.string.toast_delete), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ListViewにメモ一覧を表示する
     * @param activity
     */
    public static void syncList(MainActivity activity) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        MemoDataBaseRecord[] records = MemoDatabaseAccessor.getAllMemoRecordsArray(activity);

        if (pref.getBoolean("list_style", false)) {

            List<ShuttleListItem> listItems = new ArrayList<>();
            for (MemoDataBaseRecord record : records) {
                int icon =
                        activity.getResources().getIdentifier(
                                record.getIconImg(),
                                "drawable",
                                activity.getPackageName()
                        );

                ShuttleListItem item =
                        new ShuttleListItem(
                                icon,
                                record.getMemoTitle(),
                                activity.getResources().getString(R.string.cd) + record.getTimestamp(),
                                record.getIconColor(),
                                record.getID()
                        );

                listItems.add(item);
            }

            ShuttleListAdapter adapter = new ShuttleListAdapter(activity, R.layout.shuttle_listitem, listItems);
            activity.listView.setAdapter(adapter);

        } else {

            List<SimpleListItem> listItems = new ArrayList<>();
            for (MemoDataBaseRecord record : records) {
                SimpleListItem item =
                        new SimpleListItem(
                                record.getMemoTitle(),
                                record.getID()
                        );

                listItems.add(item);
            }

            SimpleListAdapter adapter = new SimpleListAdapter(activity, R.layout.simple_listitem, listItems);
            activity.listView.setAdapter(adapter);

        }

    }

    /**
     * memoDBのidを元にビュワーを起動する
     * @param mainActivity
     * @param id
     */
    private static void wakeupMemoViewerById(MainActivity mainActivity, int id) {
        MemoDataBaseRecord record = MemoDatabaseAccessor.getRecordById(
                mainActivity, id
        );

        if (record == null) {
            return;
        }

        Class nextActivity;
        if (PreferenceManager.getDefaultSharedPreferences(mainActivity).getBoolean("viewer_used", false)) {
            nextActivity = ViewerActivity.class;
        } else {
            nextActivity = EditActivity.class;
        }

        Intent intent = new Intent(mainActivity.getApplicationContext(), nextActivity);
        intent.putExtra("TITLE", record.getMemoTitle());
        intent.putExtra("MEMO", MemoFileManager.readFile(record.getFilePath(), mainActivity));
        intent.putExtra("_ID", record.getID());
        intent.putExtra("flag", true);
        intent.putExtra("Notifi", record.getIsNotifyEnabled());

        mainActivity.startActivity(intent);
    }
}
