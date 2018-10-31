package jp.ghostserver.ghostshuttle.EditActivityRepository;

import android.preference.PreferenceManager;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.MemoDatabaseAccessor;

public class EditActivityFunctions {

    static String getEditingMemoTitle(EditActivity activity) {
        String title;

        if (activity.titleField.length() != 0) {
            //タイトルになんか書かれてるとき
            title = activity.titleField.getText().toString();
        } else {
            //タイトル欄未記入
            title = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString("default_title", "");
        }

        //重複の確認
        int overlapNum = MemoDatabaseAccessor.checkOverlapTitle(activity, title);

        if (overlapNum >= 1) {
            title = title + "(" + overlapNum + ")";
        }

        return title;
    }
}
