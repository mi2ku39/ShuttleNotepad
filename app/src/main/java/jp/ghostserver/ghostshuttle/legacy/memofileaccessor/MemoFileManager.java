package jp.ghostserver.ghostshuttle.legacy.memofileaccessor;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public class MemoFileManager {
    public static String readFile(Context context, String file) {
        Log.d("read File", file);

        StringBuilder str = new StringBuilder();
        String tmp;
        try {
            FileInputStream in = context.openFileInput(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((tmp = reader.readLine()) != null) {
                str.append(tmp).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str.toString();
    }

    public static void saveFile(Context context, String filepath, String memo) {
        Log.d("save File", filepath);

        try {
            FileOutputStream out = context.openFileOutput(filepath, MODE_PRIVATE);
            out.write(memo.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
