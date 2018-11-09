package jp.ghostserver.ghostshuttle.EditActivityRepository;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDataBaseRecord;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDatabaseAccessor;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.NotifyDataBaseAccessor;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.NotifyDateBaseRecord;
import jp.ghostserver.ghostshuttle.memofileaccessor.MemoFileManager;
import jp.ghostserver.ghostshuttle.notifyRepository.NotifyManager;

import java.util.Objects;
import java.util.Random;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //変数宣言
    EditText titleField, memoField;

    private boolean isEdited;
    boolean isNotifyEnabled;

    String _memoBeforeEdit, _titleBeforeEdit;
    private NotifyDateBaseRecord _notifyRecord;
    MemoDataBaseRecord memoRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //intentの受け取り
        Intent intent = getIntent();
        isEdited = intent.getBooleanExtra("isEditMode", false);
        if (isEdited) {
            memoRecord = MemoDatabaseAccessor.getRecordById(this, intent.getLongExtra("_ID", -1));
        }

        //画面上部の「戻るボタン」設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //画面上部のタイトル設定
        setTitle(getResources().getString(R.string.edit_title));

        //初期設定系の関数
        titleField = findViewById(R.id.editText);
        memoField = findViewById(R.id.editmemo);

        //EditTextへデフォルトのテキストを入れる
        String[] texts = setViews.getDefaultTexts(this, isEdited, memoRecord);
        titleField.setText(texts[0]);
        memoField.setText(texts[1]);

        //編集前の状態を控える
        _memoBeforeEdit = memoField.getText().toString();
        _titleBeforeEdit = titleField.getText().toString();

        //レコードの初期化
        _notifyRecord = new NotifyDateBaseRecord();

        //編集状態であれば通知の有無を確認する
        if (isEdited) {
            _notifyRecord = NotifyDataBaseAccessor.getRecordByMemoID(this, (int) memoRecord.getID());

            //nullだったらレコードが見つからなかった→通知なし
            isNotifyEnabled = !(_notifyRecord == null);

            //通知が過去の日付だったら削除
            if (isNotifyEnabled && !checkValues.checkNotifyDate(_notifyRecord)) {
                NotifyManager.notifyDisableByMemoID(this, (int) memoRecord.getID());
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
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isEnableIntegratedBackKey", false)) {
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
                    if (saveMemo()) {
                        if (isNotifyEnabled) {
                            setNotify();
                        } else {
                            NotifyManager.notifyDisableByMemoID(this, (int) memoRecord.getID());
                        }
                        finish();
                    }

                } else {
                    //旧バージョン挙動・確認ダイアログの表示
                    setViews.backDialog(this);
                }
                break;

            case R.id.save:
                if (saveMemo()) {
                    if (isNotifyEnabled) {
                        setNotify();
                    } else {
                        if (memoRecord != null) {
                            NotifyManager.notifyDisableByMemoID(this, (int) memoRecord.getID());
                        }
                    }
                    finish();
                }
                break;

            case R.id.now_save:
                saveMemo();
                _memoBeforeEdit = memoField.getText().toString();
                _titleBeforeEdit = titleField.getText().toString();
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

                if (PreferenceManager.getDefaultSharedPreferences(EditActivity.this).getBoolean("isEnableIntegratedBackKey", false)) {
                    //戻るキーに統合しているときの挙動
                    if (titleField.length() <= 0 && memoField.length() <= 0) {
                        //タイトルとメモが空白の時
                        finish();

                    } else {
                        //保存が成功したら（trueが返って来たら）activityを閉じる
                        if (saveMemo()) {
                            //通知系の確認とセット
                            if (isNotifyEnabled) {
                                setNotify();
                            } else {
                                if (isEdited) {
                                    NotifyManager.notifyDisableByMemoID(this, (int) memoRecord.getID());
                                }
                            }
                            finish();
                        }
                    }
                } else {
                    //戻るキーを統合してない時の動作。
                    setViews.backDialog(this);
                }

                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    boolean saveMemo() {
        // タイトルの取得
        String title = titleField.getText().toString();
        title = EditActivityFunctions.memoTitleValidation(this, title, isEdited);

        //メモデータをEditTextから取得
        String memoString = memoField.getText().toString();

        String filepath;

        //編集か、新規作成かの分岐
        //true=編集 false=新規作成
        if (isEdited) {
            //編集Mode
            MemoDataBaseRecord record = new MemoDataBaseRecord(
                    memoRecord.getID(),
                    title,
                    null,
                    null,
                    isNotifyEnabled,
                    null,
                    null
            );
            if (MemoDatabaseAccessor.updateRecord(this, memoRecord.getID(), record) == -1) {
                setViews.showDatabaseErrorSnackBar(this);
                return false;
            }

            filepath = memoRecord.getFilePath();

        } else {
            //else（新規作成されていた場合。）

            //ファイルパスの取得
            Random rand = new Random();
            do {
                filepath = String.valueOf(Math.abs(rand.nextLong())) + ".gs";
            } while (!MemoDatabaseAccessor.checkOverlapFilepath(this, filepath));

            //レコード形式に詰める
            memoRecord = new MemoDataBaseRecord(
                    -1,
                    title,
                    filepath,
                    null,
                    isNotifyEnabled,
                    "paper",
                    "#ffffff"
            );

            if (MemoDatabaseAccessor.insertMemoRecord(this, memoRecord) == -1) {
                setViews.showDatabaseErrorSnackBar(this);
                return false;
            }
        }

        //メモをファイルへ保存
        MemoFileManager.saveFile(this, filepath, memoString);
        return true;
    }

    void setNotify() {
        //BDへぶち込む
        NotifyDataBaseAccessor.insertRecord(this, _notifyRecord);

        //通知の発行
        NotifyManager.setNotify(this, _notifyRecord, titleField.getText().toString());
    }


}