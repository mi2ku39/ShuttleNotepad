package jp.ghostserver.ghostshuttle.memofileaccessor;

import android.content.Context;

import java.io.*;

import static android.content.Context.MODE_PRIVATE;

public class MemoFileManager {
    public static String readFile(Context context, String file) {
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

    public void saveFile(Context context, String filepath, String memo) {
        try {
            FileOutputStream out = context.openFileOutput(filepath + ".gs", MODE_PRIVATE);
            out.write(memo.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
