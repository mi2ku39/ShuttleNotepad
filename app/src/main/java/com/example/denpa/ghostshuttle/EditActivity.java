package com.example.denpa.ghostshuttle;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class EditActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    //変数宣言
    EditText title,editmemo;
    Button date_b,time_b;
    TextView debaglog;
    boolean Edit_flag;
    boolean Notifi_flag = false;
    int db_id ;
    int year,month,day,hour,min;

    String memo_before;
    String title_before;

    MemoDBHelper DBHelper = new MemoDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //画面上部の「戻るボタン」設定
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //画面上部のタイトル設定
        setTitle(getResources().getString(R.string.edit_title));

        //初期設定系の関数
        findid();

        debaglog.setText(getResources().getString(R.string.app_version)+"\nNotification : False");

        Intent ei = getIntent();
        Edit_flag = ei.getBooleanExtra("flag",false);

        if(Edit_flag) {
            title.setText(ei.getStringExtra("TITLE"));
            editmemo.setText(ei.getStringExtra("MEMO"));
            this.db_id = ei.getIntExtra("_ID",1);

            if(ei.getIntExtra("Notifi",0) == 1){

                //debaglog.setText("Early Access (Version : 0.0)\nNotification : True");
                Notifi_flag = true;

                //データベースの取得・クエリ実行
                SQLiteDatabase read_db = DBHelper.getReadableDatabase();
                Cursor cursor = read_db.query("NOTIFICATION",new String[] {"notifi_year","notifi_month","notifi_day","notifi_hour","notifi_min"},"_id = '" + db_id + "'",null,null,null,null,null);
                cursor.moveToFirst();
                Log.d("test",String.valueOf(db_id));
                debaglog.setText(getResources().getString(R.string.app_version)+"\nNotification : True\n" + cursor.getString(0)+"/"+ cursor.getString(1)+ "/"+cursor.getString(2)+" "+ cursor.getString(3)+":"+ cursor.getString(4));

                year = cursor.getInt(0);
                month = cursor.getInt(1);
                day = cursor.getInt(2);
                hour = cursor.getInt(3);
                min = cursor.getInt(4);

                cursor.close();
                read_db.close();

                //通知時間の確認（過去だったらFalse）
                Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.YEAR) - year > 0){
                    Notifi_flag = false;
                    debaglog.setText(getResources().getString(R.string.app_version)+"\nNotification : False");
                }else if(calendar.get(Calendar.MONTH) - month > 0){
                    Notifi_flag = false;
                    debaglog.setText(getResources().getString(R.string.app_version)+"\nNotification : False");
                }else if(calendar.get(Calendar.DAY_OF_MONTH) - day > 0){
                    Notifi_flag = false;
                    debaglog.setText(getResources().getString(R.string.app_version)+"\nNotification : False");
                }else if(calendar.get(Calendar.HOUR_OF_DAY) - hour > 0){
                    Notifi_flag = false;
                    debaglog.setText(getResources().getString(R.string.app_version)+"\nNotification : False");
                }else if(calendar.get(Calendar.MINUTE) - min >= 0){
                    Notifi_flag = false;
                    debaglog.setText(getResources().getString(R.string.app_version)+"\nNotification : False");
                }

            }else{
                debaglog.setText(getResources().getString(R.string.app_version)+"\nNotification : False");
            }

        }else{
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            if(pref.getString("title_template","").length() == 0){}else{
                title.setText(pref.getString("title_template",""));
            }
            if(pref.getString("memo_template","").length() == 0){}else{
                editmemo.setText(pref.getString("memo_template",""));
            }
        }

        search_before();
        invalidateOptionsMenu();

    }

    //編集されてるかどうかを判断する変数の設定
    private void search_before(){
        memo_before = editmemo.getText().toString();
        title_before = title.getText().toString();
    }

    //findViewByIdする関数
    private void findid(){
        title = findViewById(R.id.editText);
        editmemo = findViewById(R.id.editmemo);
        debaglog = findViewById(R.id.textView5);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){

            //日付を設定するやつ
            case R.id.date_b:

                DatePickerDialogFragment newDateFragment = new DatePickerDialogFragment();
                newDateFragment.show(getFragmentManager(), "datePicker");

                break;

            //時間設定するやつ
            case R.id.time_b:

                TimePickerFragment newTimeFragment = new TimePickerFragment();
                newTimeFragment.show(getSupportFragmentManager(), "timePicker");

                break;

            default:
                break;
        }

    }

    //ActionBarのメニューを設定
    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);

        if(Notifi_flag){

            MenuItem Notifi_item = menu.findItem(R.id.notifi);
            Notifi_item.setIcon(R.mipmap.notifi_b);

        }else{

            MenuItem Notifi_item = menu.findItem(R.id.notifi);
            Notifi_item.setIcon(R.mipmap.notifi_a);

        }

        return super.onCreateOptionsMenu(menu);
    }

    //日付・時刻設定ボタンの初期値設定
    private void setPrimary()
    {
        if(Notifi_flag){

        }else{
            Calendar calendar = Calendar.getInstance();
            int Month = calendar.get(Calendar.MONTH) + 1;
            date_b.setText( calendar.get(Calendar.YEAR) + "/ " + Month + "/ " + calendar.get(Calendar.DAY_OF_MONTH) );
            time_b.setText( hour_convert(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.format("%02d",calendar.get(Calendar.MINUTE)) );

            this.year = calendar.get(Calendar.YEAR);
            this.month = calendar.get(Calendar.MONTH);
            this.day=calendar.get(Calendar.DAY_OF_MONTH);

            this.hour=calendar.get(Calendar.HOUR_OF_DAY);
            this.min=calendar.get(Calendar.MINUTE);
        }

    }

    //ボタンに日付表示をする関数
    @Override
    public void onDateSet(DatePicker view,int year,int month,int day){
        date_b.setText( String.valueOf(year)  + "/ " + String.valueOf(month+1) + "/ " + String.valueOf(day) );

        this.year = year;
        this.month = month;
        this.day = day;
    }

    //ボタンに時刻表示をする関数
    @Override
    public void onTimeSet(TimePicker view, int hour, int min){
        time_b.setText( hour_convert(hour)+":"+String.format("%02d",min ));

        this.hour = hour;
        this.min = min;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        boolean result;
        switch(id){

            //「戻るボタン」のクリックイベント
            case android.R.id.home:

                //ダイアログの表示
               backdialog();

                break;

            case R.id.save:

                if(db_save()) {
                    if (Notifi_flag) {
                        setNotification();
                    }else{
                        Notify_cancel();
                    }
                    finish();
                }
                break;

            case R.id.now_save:

                db_save();
                search_before();

                Edit_flag = true;
                break;

            case R.id.notifi:

                if(Notifi_flag){

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getResources().getString(R.string.notify_disable));
                    builder.setPositiveButton(getResources().getString(R.string.disable), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // OK ボタンクリック処理
                            Notifi_flag = false;
                            invalidateOptionsMenu();
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel ボタンクリック処理
                        }
                    });
                    // 表示
                    builder.create().show();

                }else{
                    // アラートダイアログ を生成
                    LayoutInflater a_inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View notifi_dialog = a_inflater.inflate(R.layout.notifi_dialog, (ViewGroup) findViewById(R.id.notifidialog_cl));

                    date_b=notifi_dialog.findViewById(R.id.date_b);
                    time_b=notifi_dialog.findViewById(R.id.time_b);
                    date_b.setOnClickListener(this);
                    time_b.setOnClickListener(this);

                    setPrimary();

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("通知設定");
                    builder.setView(notifi_dialog);
                    builder.setPositiveButton("設定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // OK ボタンクリック処理
                            Notifi_flag = true;
                            invalidateOptionsMenu();
                        }
                    });
                    builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel ボタンクリック処理
                            invalidateOptionsMenu();
                        }
                    });
                    // 表示
                    builder.create().show();
                }

                break;

            default:

                break;

        }
        result = super.onOptionsItemSelected(item);
        return result;

    }

    //戻るキーのクリックイベント
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                backdialog();

                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void backdialog() {

        if(title_before.equals(title.getText().toString()) && memo_before.equals(editmemo.getText().toString())) {

                finish();

        }else{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("メモの作成/編集をキャンセル");
            alertDialogBuilder.setMessage("保存しないでいいの？");
            alertDialogBuilder.setPositiveButton("いいよ！",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("ダメです",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
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
        String memo_raw = editmemo.getText().toString();

        String title_raw;
        boolean title_not = true;
        int count = 0;
        Random rand = new Random();
        long filepath = rand.nextLong();

        if (title.length() != 0) {
            // タイトルの取得
            title_raw = title.getText().toString();
        } else {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            title_raw = pref.getString("default_title","");
            title_not = false;
        }

        //データベースに保存するレコードの用意
        ContentValues values = new ContentValues();
        values.put("title", title_raw);
        values.put("filepath", String.valueOf(filepath));

        if (Notifi_flag == true) {
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
        if (Edit_flag) {
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
                        Snackbar.make(cl, "データベースへ追加失敗 : タイトルが他のメモと重複しています。", Snackbar.LENGTH_SHORT).show();
                        return false;
                    } else {
                        count++;
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                        title_raw =  pref.getString("default_title","") + "(" + count + ")";
                        values.put("title", title_raw);
                        title.setText(title_raw);
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
                    Snackbar.make(cl, "データベースへ追加失敗 : タイトルが他のメモと重複しています。", Snackbar.LENGTH_SHORT).show();
                    return false;
                } else {
                    while (db_id == -1) {
                        count++;
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                        title_raw = pref.getString("default_title","") + "(" + count + ")";
                        values.put("title", title_raw);
                        filepath = rand.nextLong();
                        values.put("filepath", String.valueOf(filepath));
                        db_id = memo_db.insert("memo", null, values);
                    }
                    title.setText(title_raw);
                }
            }

            Log.d("test",String.valueOf(db_id));
            this.db_id = (int) db_id;
        }

        memo_db.close();

        saveFile(String.valueOf(filepath), memo_raw);
        return true;
    }

    private void setNotification(){

        //データベースオブジェクトの取得（書き込み可能）
        SQLiteDatabase Notifi_db = DBHelper.getWritableDatabase();

        //データベースに保存するレコードの用意
        ContentValues values = new ContentValues();

        values.put("_id",this.db_id);
        values.put("notifi_year", this.year);
        values.put("notifi_month", this.month);
        values.put("notifi_day", this.day);
        values.put("notifi_hour", this.hour);
        values.put("notifi_min", this.min);

        long test = Notifi_db.insert("NOTIFICATION", null, values);
        if(test == -1){
            String where_words= "_ID = '" + this.db_id + "'";
            Notifi_db.update("NOTIFICATION", values, where_words , null);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.year);
        calendar.set(Calendar.MONTH, this.month);// 7=>8月
        calendar.set(Calendar.DATE, this.day);
        calendar.set(Calendar.HOUR_OF_DAY, this.hour);
        calendar.set(Calendar.MINUTE, this.min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        intent.putExtra("ID",this.db_id);
        intent.putExtra("title", title.getText().toString());

        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), this.db_id, intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);

        Notifi_db.close();


    }

    private void Notify_cancel(){
        Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), this.db_id, intent, 0);

        // アラームを解除する
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        am.cancel(pending);

    }

    private String hour_convert(int hour){
        String am_pm="午前";

        if (hour >= 12){
            am_pm ="午後";
            hour = hour - 12;
        }
        return (am_pm+" "+hour);
    }

    public void saveFile(String filepath,String memo){
        try{
            String str = memo;
            FileOutputStream out = openFileOutput( filepath + ".gs", MODE_PRIVATE );
            out.write( str.getBytes()   );
        }catch( IOException e ){
            e.printStackTrace();
        }

    }

}