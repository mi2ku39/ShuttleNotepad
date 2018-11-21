package jp.ghostserver.ghostshuttle.MainActivityRepository;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import jp.ghostserver.ghostshuttle.preferenceaccessor.PreferenceAccessor;

import java.util.ArrayList;
import java.util.List;

public class MainActivityMethods {

    /**
     * Preference Screenを初期化するメソッド
     *
     * @param context コンテキスト
     */
    static void setPreferenceScreen(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context);
        PreferenceManager.setDefaultValues(context, R.xml.preference_setting, true);
    }

    /**
     * ListViewを初期化するメソッド
     *
     * @param context   コンテキスト
     * @param listView  ListViewへの参照
     * @param emptyView emptyView
     */
    static void initListView(final Context context, final ListView listView, TextView emptyView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BaseShuttleListItem item = (BaseShuttleListItem) listView.getItemAtPosition(position);
                long itemID = item.getID();

                wakeupMemoViewerById(context, itemID);

            }
        });

        listView.setEmptyView(emptyView);
        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
    }

    /**
     * 通知による起動かどうかを確認するメソッド
     *
     * @param context コンテキスト
     * @param intent  getIntent()されたやつ
     */
    static void checkStartAppByNotify(Context context, Intent intent) {
        if (intent.getBooleanExtra("FLAG", false)) {
            int id = intent.getIntExtra("ID", -1);
            MemoDataBaseRecord record = MemoDatabaseAccessor.getRecordById(context, id);

            if (record != null) {
                wakeupMemoViewerById(context, record.getID());
            } else
                Toast.makeText(context, context.getResources().getString(R.string.toast_delete), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ListViewに表示する内容をDBから取得するメソッド
     *
     * @param context  コンテキスト
     * @param listView ListViewへの参照
     */
    static void syncList(Context context, ListView listView) {
        MemoDataBaseRecord[] records = MemoDatabaseAccessor.getAllMemoRecordsArray(context);

        if (PreferenceAccessor.isUsingEnhancedList(context)) {

            List<ShuttleListItem> listItems = new ArrayList<>();
            for (MemoDataBaseRecord record : records) {
                int icon =
                        context.getResources().getIdentifier(
                                record.getIconImg(),
                                "drawable",
                                context.getPackageName()
                        );

                ShuttleListItem item =
                        new ShuttleListItem(
                                icon,
                                record.getMemoTitle(),
                                context.getResources().getString(R.string.cd) + record.getTimestamp(),
                                record.getIconColor(),
                                record.getID()
                        );

                listItems.add(item);
            }

            ShuttleListAdapter adapter = new ShuttleListAdapter(context, R.layout.shuttle_listitem, listItems);
            listView.setAdapter(adapter);

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

            SimpleListAdapter adapter = new SimpleListAdapter(context, R.layout.simple_listitem, listItems);
            listView.setAdapter(adapter);

        }

    }

    /**
     * 編集画面か閲覧画面へ飛ばすメソッド
     *
     * @param context コンテキスト
     * @param id      レコードのid
     */
    static void wakeupMemoViewerById(Context context, long id) {

        Class nextActivity;
        if (PreferenceAccessor.isUsingViewer(context)) {
            nextActivity = ViewerActivity.class;
        } else {
            nextActivity = EditActivity.class;
        }

        Intent intent = new Intent(context.getApplicationContext(), nextActivity);
        intent.putExtra("_ID", id);
        intent.putExtra("isEditMode", true);

        context.startActivity(intent);
    }
}
