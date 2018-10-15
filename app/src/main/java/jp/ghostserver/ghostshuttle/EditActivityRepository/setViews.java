package jp.ghostserver.ghostshuttle.EditActivityRepository;

import android.support.v7.widget.Toolbar;
import com.example.denpa.ghostshuttle.R;

import java.util.Objects;

class setViews {
    static void findIDs(EditActivity activity) {
        activity.titleField = activity.findViewById(R.id.editText);
        activity.memoField = activity.findViewById(R.id.editmemo);
    }

    static void setActionBar(EditActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //画面上部のタイトル設定
        activity.setTitle(activity.getResources().getString(R.string.edit_title));
    }

    static String hour_convert(EditActivity activity, int hour) {
        String am_pm = activity.getResources().getString(R.string.am);

        if (hour >= 12) {
            am_pm = activity.getResources().getString(R.string.pm);
            hour = hour - 12;
        }
        return (am_pm + " " + hour);
    }
}
