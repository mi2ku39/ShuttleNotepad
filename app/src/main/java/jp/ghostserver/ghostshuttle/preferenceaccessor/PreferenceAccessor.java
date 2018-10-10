package jp.ghostserver.ghostshuttle.preferenceaccessor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceAccessor {
    public static Boolean getListStyle(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean("list_style",false);
    }
}
