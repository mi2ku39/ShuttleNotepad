package jp.ghostserver.ghostshuttle

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.example.denpa.ghostshuttle.EditActivity
import com.example.denpa.ghostshuttle.R
import kotlinx.android.synthetic.main.activity_viewer.*
import java.util.*

class ViewerActivity : AppCompatActivity() {

    var memo_title = ""
    var memo_text = ""
    var memo_id = 0
    var Notify_flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        setSupportActionBar(toolbar)
        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayHomeAsUpEnabled(true)

        //変数にIntentの値を代入
        memo_title = intent.getStringExtra("TITLE")
        memo_text = intent.getStringExtra("MEMO")
        memo_id = intent.getIntExtra("_ID",-1)
        Notify_flag = intent.getBooleanExtra("Notifi",false)

        title = memo_title

        val memo_field = findViewById<TextView>(R.id.Memo_field)
        memo_field.text = memo_text

        fab.setOnClickListener { view ->
            //FAB押された時の挙動

            val edit_intent = Intent(applicationContext, EditActivity::class.java)
            edit_intent.putExtra("TITLE",memo_title)
            edit_intent.putExtra("MEMO",memo_text)
            edit_intent.putExtra("_ID",memo_id)
            edit_intent.putExtra("flag",true)
            edit_intent.putExtra("Notifi",Notify_flag)

            startActivity(edit_intent)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

}
