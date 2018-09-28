package com.example.denpa.ghostshuttle.memofileaccessor;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MemoFileManager {
    public static String readFile(String file, Context context){
        StringBuilder str = new StringBuilder();
        String tmp;
        try{
            FileInputStream in = context.openFileInput( file );
            BufferedReader reader = new BufferedReader( new InputStreamReader( in , "UTF-8") );
            while( (tmp = reader.readLine()) != null ){
                str.append(tmp).append("\n");
            }
            reader.close();
        }catch( IOException e ){
            e.printStackTrace();
        }

        return str.toString();
    }
}
