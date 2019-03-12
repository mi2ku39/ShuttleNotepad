package jp.ghostserver.ghostshuttle.preferenceaccessor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceAccessor {
    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isUsingEnhancedList(Context context) {
        return getSharedPreference(context).getBoolean("list_style", false);
    }

    public static boolean isIntegrateBackKey(Context context) {
        return getSharedPreference(context).getBoolean("isEnableIntegratedBackKey", false);
    }

    public static boolean isUsingViewer(Context context) {
        return getSharedPreference(context).getBoolean("isUsingViewer", false);
    }

    public static String getDefaultTitle(Context context) {
        return getSharedPreference(context).getString("defaultTitle", "");
    }

    public static String getTitleTemplate(Context context) {
        return getSharedPreference(context).getString("titleTemplate", "");
    }

    public static String getMemoTemplate(Context context) {
        return getSharedPreference(context).getString("memoTemplate", "");
    }
}
