package jp.ghostserver.ghostshuttle.EditActivityRepository;

public class checkValues {
    //編集されてるかどうかを判断する変数の設定
    static void setBeforeEditing(EditActivity activity) {
        activity._memoBeforeEditing = activity.memoField.getText().toString();
        activity._titleBeforeEditing = activity.titleField.getText().toString();
    }
}
