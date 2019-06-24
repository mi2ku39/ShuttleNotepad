package jp.ghostserver.ghostshuttle.domain.viewer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.example.denpa.ghostshuttle.R
import jp.ghostserver.ghostshuttle.entities.memo.MemoDBHelper
import jp.ghostserver.ghostshuttle.domain.editor.EditActivity
import kotlinx.android.synthetic.main.activity_viewer.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class ViewerActivity : AppCompatActivity() {

    var memo_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        setSupportActionBar(toolbar)
        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayHomeAsUpEnabled(true)

        //変数にIntentの値を代入
        memo_id = intent.getIntExtra("_ID",-1)

        fab.setOnClickListener { view ->
            //FAB押された時の挙動
            val memo_field = findViewById<TextView>(R.id.Memo_field)
            val edit_intent = Intent(applicationContext, EditActivity::class.java)

            val read_db = MemoDBHelper(this).readableDatabase
            val cursor = read_db.query("memo", arrayOf("filepath", "title", "notifi_enabled"), "_id = '$memo_id'", null, null, null, null)
            cursor.moveToFirst()

            edit_intent.putExtra("TITLE",cursor.getString(1))
            edit_intent.putExtra("MEMO",memo_field.text)
            edit_intent.putExtra("_ID",memo_id)
            edit_intent.putExtra("flag",true)
            edit_intent.putExtra("Notifi",cursor.getInt(2))

            read_db.close()
            cursor.close()

            startActivity(edit_intent)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        val read_db = MemoDBHelper(this).readableDatabase
        val cursor = read_db.query("memo", arrayOf("filepath", "title"), "_id = '$memo_id'", null, null, null, null)
        cursor.moveToFirst()

        title = cursor.getString(1)

        val memo_field = findViewById<TextView>(R.id.Memo_field)
        memo_field.text = readFile(cursor.getString(0) + ".gs")

        read_db.close()
        cursor.close()

    }

    private fun readFile(path:String):String{

        var str = ""
        var memo_tmp: String?

        try{
            val fis = openFileInput(path)
            val reader = BufferedReader(InputStreamReader(fis,"UTF-8") )
            while(true){
                memo_tmp = reader.readLine()

                if(memo_tmp != null)
                    str = str + memo_tmp + "\n"
                else{
                    break
                }

            }
        }catch (e: IOException){
            e.printStackTrace()
        }

        return str
    }

}
