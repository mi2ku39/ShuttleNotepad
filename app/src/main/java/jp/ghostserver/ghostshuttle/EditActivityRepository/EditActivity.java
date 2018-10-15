package jp.ghostserver.ghostshuttle.EditActivityRepository;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.NotifyDataBaseAccessor;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.NotifyDateBaseRecord;
import jp.ghostserver.ghostshuttle.notifyRepository.NotifyManager;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //変数宣言
    EditText titleField, memoField;

    boolean isEdited;
    int memoID;
    boolean isNotifyEnabled;

    String _memoBeforeEditing, _titleBeforeEditing;
    private NotifyDateBaseRecord _notifyRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //intentの受け取り
        setViews.parseIntent(this);

        //画面上部の「戻るボタン」設定
        setViews.setActionBar(this);

        //初期設定系の関数
        setViews.findIDs(this);

        //レコードの初期化
        _notifyRecord = new NotifyDateBaseRecord();

        //EditTextへデフォルトのテキストを入れる
        setViews.setDefaultTexts(this);

        //編集前の状態を控える
        checkValues.setBeforeEditing(this);

        //編集状態であれば通知の有無を確認する
        if (isEdited) {
            _notifyRecord = NotifyDataBaseAccessor.getRecordByMemoID(this, memoID);

            //nullだったらレコードが見つからなかった→通知なし
            isNotifyEnabled = !(_notifyRecord == null);

            //通知が過去の日付だったら削除
            if (isNotifyEnabled && !checkValues.checkNotifyDate(_notifyRecord)) {
                NotifyManager.notifyDisableByMemoID(this, memoID);
                isNotifyEnabled = false;
            }
        }

        //通知の状態をToolBarに反映
        invalidateOptionsMenu();
    }

    //ActionBarのメニューを設定
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();

        //統合バックキーだったら保存ボタンを非表示にする
        if (PreferenceManager.getDefaultSharedPreferences(EditActivity.this).getBoolean("backKey_move", false)) {
            inflater.inflate(R.menu.edit_menu_unit, menu);
        } else {
            inflater.inflate(R.menu.edit_menu, menu);
        }

        //通知の有無によってベルマークを差し替える
        MenuItem Notify_item = menu.findItem(R.id.notifi);
        if (isNotifyEnabled) {
            Notify_item.setIcon(R.mipmap.notifi_b);
        } else {
            Notify_item.setIcon(R.mipmap.notifi_a);
        }

        return super.onCreateOptionsMenu(menu);
    }

    //ボタンに日付表示をする関数
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Button button = findViewById(R.id.date_b);
        button.setText(String.valueOf(year) + "/ " + String.valueOf(month + 1) + "/ " + String.valueOf(day));

        _notifyRecord.year = year;
        _notifyRecord.month = month + 1;
        _notifyRecord.date = day;
    }

    //ボタンに時刻表示をする関数
    @Override
    public void onTimeSet(TimePicker view, int hour, int min) {
        Button button = findViewById(R.id.time_b);
        button.setText(setViews.hour_convert(this, hour) + ":" + String.format("%02d", min));

        _notifyRecord.hour = hour;
        _notifyRecord.min = min;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean result;
        switch (id) {

            //「戻るボタン」のクリックイベント
            case android.R.id.home:
                if (PreferenceManager.getDefaultSharedPreferences(EditActivity.this).getBoolean("backKey_move", false)) {

                    //統合戻るキーの挙動
                    if (db_save()) {
                        if (isNotifyEnabled) {
                            setNotify();
                        } else {
                            NotifyManager.notifyDisableByMemoID(this, memoID);
                        }
                        finish();
                    }

                } else {
                    //旧バージョン挙動・確認ダイアログの表示
                    backDialog();
                }
                break;

            case R.id.save:
                if (db_save()) {
                    if (isNotifyEnabled) {
                        setNotify();
                    } else {
                        NotifyManager.notifyDisableByMemoID(this, memoID);
                    }
                    finish();
                }
                break;

            case R.id.now_save:
                db_save();
                checkValues.setBeforeEditing(this);
                isEdited = true;
                break;

            case R.id.notifi:
                if (isNotifyEnabled) {
                    //通知解除確認ダイアログの表示
                    setViews.showCheckingDisableNotifyDialog(this);
                } else {
                    //通知設定ダイアログの表示
                    setViews.showNotifySettingDialog(this);
                }
                break;

            case R.id.edit_cancel:
                finish();
                break;

        }
        result = super.onOptionsItemSelected(item);
        return result;

    }

    //戻るキーのクリックイベント
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                if (PreferenceManager.getDefaultSharedPreferences(EditActivity.this).getBoolean("backKey_move", false)) {
                    //統合戻るキーの挙動
                    if (titleField.length() <= 0 && memoField.length() <= 0) {
                        //タイトルかメモが空白の時
                        finish();

                    } else {

                        if (db_save()) {
                            if (isNotifyEnabled) {
                                setNotify();
                            } else {
                                NotifyManager.notifyDisableByMemoID(this, memoID);
                            }
                            finish();
                        }
                    }

                } else {
                    //旧バージョン挙動・確認ダイアログの表示
                    backDialog();
                }

                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void backDialog() {

        if (_titleBeforeEditing.equals(titleField.getText().toString()) && _memoBeforeEditing.equals(memoField.getText().toString())) {

            finish();

        } else {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.edit_cancel));
            alertDialogBuilder.setMessage(getResources().getString(R.string.cancel));
            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.save),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (db_save()) {
                                if (isNotifyEnabled) {
                                    setNotify();
                                } else {
                                    NotifyManager.notifyDisableByMemoID(this, memoID);
                                }
                                finish();
                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.edit_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    }

    private boolean db_save() {
        //データベースオブジェクトの取得（書き込み可能）
        SQLiteDatabase memo_db = DBHelper.getWritableDatabase();

        //メモデータをEditTextから取得
        String memo_raw = memoField.getText().toString();

        String title_raw;
        boolean title_not = true;
        int count = 0;
        Random rand = new Random();
        long filepath = rand.nextLong();

        if (titleField.length() != 0) {
            // タイトルの取得
            title_raw = titleField.getText().toString();
        } else {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            title_raw = pref.getString("default_title", "");
            title_not = false;
        }

        //データベースに保存するレコードの用意
        ContentValues values = new ContentValues();
        values.put("titleField", title_raw);
        values.put("filepath", String.valueOf(filepath));

        if (isNotifyEnabled) {
            values.put("notifi_enabled", true);
        } else {
            values.put("notifi_enabled", false);
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        String timestamp = String.format("%d-%02d-%02d %02d:%02d:%02d", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

        values.put("data_modified", timestamp);

        //編集か、新規作成かの分岐
        //true=編集 false=新規作成
        if (isEdited) {
            //編集Mode

            String where_words = "_id = " + db_id;

            while (true) {
                try {
                    memo_db.update("memo", values, where_words, null);
                    break;
                } catch (Exception e) {
                    if (title_not) {
                        //データベースへ追加に失敗したときの処理
                        ConstraintLayout cl = findViewById(R.id.cl);
                        Snackbar.make(cl, getResources().getString(R.string.DB_failed), Snackbar.LENGTH_SHORT).show();
                        return false;
                    } else {
                        count++;
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                        title_raw = pref.getString("default_title", "") + "(" + count + ")";
                        values.put("titleField", title_raw);
                        titleField.setText(title_raw);
                    }
                }
            }

        } else {
            //else（新規作成されていた場合。）

            values.put("icon_img", "paper");
            values.put("icon_color", "#ffffff");

            //データベースへ保存する記述
            long db_id = memo_db.insert("memo", null, values);

            if (db_id == -1) {
                if (title_not) {
                    //データベースへ追加に失敗したときの処理
                    ConstraintLayout cl = findViewById(R.id.cl);
                    Snackbar.make(cl, getResources().getString(R.string.DB_failed), Snackbar.LENGTH_SHORT).show();
                    return false;
                } else {
                    while (db_id == -1) {
                        count++;
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                        title_raw = pref.getString("default_title", "") + "(" + count + ")";
                        values.put("titleField", title_raw);
                        filepath = rand.nextLong();
                        values.put("filepath", String.valueOf(filepath));
                        db_id = memo_db.insert("memo", null, values);
                    }
                    titleField.setText(title_raw);
                }
            }

            Log.d("test", String.valueOf(db_id));
            this.db_id = (int) db_id;
        }

        memo_db.close();

        saveFile(String.valueOf(filepath), memo_raw);
        return true;
    }

    private void setNotify() {
        //BDへぶち込む
        NotifyDataBaseAccessor.insertRecord(this, _notifyRecord);

        //通知の発行
        NotifyManager.setNotify(this, _notifyRecord, titleField.getText().toString());
    }


}