package jp.ghostserver.ghostshuttle.domain.memo_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.denpa.ghostshuttle.R;

import jp.ghostserver.ghostshuttle.entities.memo.MemoRecord;
import jp.ghostserver.ghostshuttle.entities.memo.MemoDatabaseAccessor;
import jp.ghostserver.ghostshuttle.domain.app_detail.AppDetail;
import jp.ghostserver.ghostshuttle.domain.comopnents.BaseShuttleListItem;
import jp.ghostserver.ghostshuttle.domain.editor.EditActivity;
import jp.ghostserver.ghostshuttle.domain.icon_picker.iconActivity;
import jp.ghostserver.ghostshuttle.domain.selected_memo_delete.DeleteActivity;
import jp.ghostserver.ghostshuttle.domain.settings.SettingActivity;

public class MainActivity extends AppCompatActivity {

    private MemoListActivityContract.Presenter presenter;

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

        MainActivityMethods.initListView(this, (ListView) findViewById(R.id.listview), (TextView) findViewById(R.id.EmptyText));

        ((ListView) findViewById(R.id.listview)).setEmptyView(findViewById(R.id.EmptyText));
        ((ListView) findViewById(R.id.listview)).setChoiceMode(ListView.CHOICE_MODE_NONE);

        registerForContextMenu(findViewById(R.id.listview));
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

        BaseShuttleListItem item = (BaseShuttleListItem) ((ListView) findViewById(R.id.listview)).getItemAtPosition(info.position);
        String title = item.getTitle();
        menu.setHeaderTitle(title);

        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo Info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final BaseShuttleListItem listItem = (BaseShuttleListItem) ((ListView) findViewById(R.id.listview)).getItemAtPosition(Info.position);

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
                                MainActivityMethods.syncList(MainActivity.this, (ListView) findViewById(R.id.listview));
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
                MemoRecord record = MemoDatabaseAccessor.getRecordById(this, listItem.getID());

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
        MainActivityMethods.syncList(this, (ListView) findViewById(R.id.listview));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail:
                startActivity(new Intent(this, AppDetail.class));
                break;

            case R.id.mode:
                if (((ListView) findViewById(R.id.listview)).getCount() != 0) {
                    Intent delete_intent = new Intent(this, DeleteActivity.class);
                    delete_intent.putExtra("item", ((ListView) findViewById(R.id.listview)).getCount());
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
