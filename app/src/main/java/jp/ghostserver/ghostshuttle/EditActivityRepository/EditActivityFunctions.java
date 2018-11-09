package jp.ghostserver.ghostshuttle.EditActivityRepository;

import android.content.Context;
import android.preference.PreferenceManager;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDatabaseAccessor;

public class EditActivityFunctions {

    static String memoTitleValidation(Context context, String title, boolean isEdited) {

        if (title.length() == 0) {
            //タイトル欄未記入
            title = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("default_title", "");
        }

        //重複の確認
        int overlapNum = MemoDatabaseAccessor.checkOverlapTitle(context, title);

        if (isEdited) {
            //編集のとき
            if (overlapNum <= 1) {
                return title;
            } else {
                return title + "(" + (overlapNum - 1) + ")";
            }
        } else {
            //新規作成のとき
            if (overlapNum == 0) {
                return title;
            } else {
                return title + "(" + overlapNum + ")";
            }
        }

    }
}
