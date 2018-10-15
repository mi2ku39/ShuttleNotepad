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

    static void findIDs(final MainActivity targetActivity) {
        targetActivity.findViewById(R.id.fab).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //FABが押されたときの動作
                        Intent intent = new Intent(targetActivity, EditActivity.class);
                        targetActivity.startActivity(intent);
                    }
                }
        );
        targetActivity.listView = targetActivity.findViewById(R.id.listview);
        targetActivity.coordinatorLayout = targetActivity.findViewById(R.id.coordinatorLayout);
    }

    public static void setToolbar(MainActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.setTitle(activity.getResources().getString(R.string.app_name));
    }

    static void setPreferenceScreen(MainActivity activity) {
        PreferenceManager.getDefaultSharedPreferences(activity);
        PreferenceManager.setDefaultValues(activity, R.xml.preference_setting, true);
    }

    static void initListView(final MainActivity activity) {
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

    static void checkStartAppFromNotify(MainActivity activity) {
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

    static void syncList(MainActivity activity) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        MemoDataBaseRecord[] records = MemoDatabaseAccessor.getAllMemoRecordsArray(activity);

        if (pref.getBoolean(activity.getResources().getString(R.string.isEnableEnhancedList), false)) {

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

    private static void wakeupMemoViewerById(MainActivity mainActivity, int id) {
        MemoDataBaseRecord record = MemoDatabaseAccessor.getRecordById(
                mainActivity, id
        );

        if (record == null) {
            return;
        }

        Class nextActivity;
        if (PreferenceManager.getDefaultSharedPreferences(mainActivity).getBoolean(mainActivity.getResources().getString(R.string.isUsingViewer), false)) {
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
