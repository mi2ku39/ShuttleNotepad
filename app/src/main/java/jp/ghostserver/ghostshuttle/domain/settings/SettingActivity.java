package jp.ghostserver.ghostshuttle.domain.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.denpa.ghostshuttle.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getResources().getString(R.string.app_settings));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        boolean result;
        switch(id){

            //「戻るボタン」のクリックイベント
            case android.R.id.home:

                //ダイアログの表示
                finish();

                break;

            default:

                break;

        }
        result = super.onOptionsItemSelected(item);
        return result;

    }
}
