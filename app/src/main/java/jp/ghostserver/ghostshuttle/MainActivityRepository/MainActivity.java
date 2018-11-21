package jp.ghostserver.ghostshuttle.MainActivityRepository;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.AppDetail;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDataBaseRecord;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDatabaseAccessor;
import jp.ghostserver.ghostshuttle.DeleteActivity;
import jp.ghostserver.ghostshuttle.EditActivityRepository.EditActivity;
import jp.ghostserver.ghostshuttle.ListViewClasses.BaseShuttleListItem;
import jp.ghostserver.ghostshuttle.SettingActivity;
import jp.ghostserver.ghostshuttle.iconActivity;

public class MainActivity extends AppCompatActivity {
    private ListView listView = findViewById(R.id.listview);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        MainActivityMethods.setPreferenceScreen(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(getResources().getString(R.string.app_name));

        findViewById(R.id.fab).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //FABが押されたときの動作
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(intent);
                    }
                }
        );
        MainActivityMethods.initListView(this, listView, (TextView) findViewById(R.id.EmptyText));
        registerForContextMenu(listView);
        MainActivityMethods.checkStartAppByNotify(this, getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo Info) {
        super.onCreateContextMenu(menu, v, Info);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) Info;

        BaseShuttleListItem item = (BaseShuttleListItem) listView.getItemAtPosition(info.position);
        String title = item.getTitle();
        menu.setHeaderTitle(title);

        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo Info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final BaseShuttleListItem listItem = (BaseShuttleListItem) listView.getItemAtPosition(Info.position);

        switch (item.getItemId()) {

            case R.id.delete_memo:

                //ダイアログの表示
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(MainActivity.this);
                alertDlg.setTitle(listItem.getTitle());
                alertDlg.setMessage(getResources().getString(R.string.delete_question));
                alertDlg.setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // OK ボタンクリック処理
                                MemoDatabaseAccessor.DeleteMemoById(MainActivity.this, listItem.getID());
                                MainActivityMethods.syncList(MainActivity.this, listView);
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
                MemoDataBaseRecord record = MemoDatabaseAccessor.getRecordById(this, listItem.getID());

                if (record == null) {
                    return false;
                }

                Intent iconIntent = new Intent(getApplicationContext(), iconActivity.class);

                iconIntent.putExtra("memo_id", record.getID());
                iconIntent.putExtra("memo_title", record.getMemoTitle());
                iconIntent.putExtra("icon_img", record.getIconImg());
                iconIntent.putExtra("icon_color", record.getIconColor());

                startActivity(iconIntent);
                overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_under);

                break;
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_under);
        MainActivityMethods.syncList(this, listView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail:
                startActivity(new Intent(this, AppDetail.class));
                break;

            case R.id.mode:
                if (listView.getCount() != 0) {
                    Intent delete_intent = new Intent(this, DeleteActivity.class);
                    delete_intent.putExtra("item", listView.getCount());
                    startActivity(delete_intent);
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout), getResources().getString(R.string.error_d), Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
        return true;
    }

}
