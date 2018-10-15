package jp.ghostserver.ghostshuttle.EditActivityRepository;

import jp.ghostserver.ghostshuttle.DataBaseAccesser.NotifyDateBaseRecord;

import java.util.Calendar;

public class checkValues {
    //編集されてるかどうかを判断する変数の設定
    static void setBeforeEditing(EditActivity activity) {
        activity._memoBeforeEditing = activity.memoField.getText().toString();
        activity._titleBeforeEditing = activity.titleField.getText().toString();
    }

    //通知時間の確認（過去だったらFalse）
    static Boolean checkNotifyDate(NotifyDateBaseRecord record) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) - record.year > 0) {
            return false;
        } else if (calendar.get(Calendar.MONTH) - record.month > 0) {
            return false;
        } else if (calendar.get(Calendar.DAY_OF_MONTH) - record.date > 0) {
            return false;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) - record.hour > 0) {
            return false;
        } else if (calendar.get(Calendar.MINUTE) - record.min >= 0) {
            return false;
        }
        return true;
    }



}
