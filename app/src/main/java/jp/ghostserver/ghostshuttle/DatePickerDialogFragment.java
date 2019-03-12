package jp.ghostserver.ghostshuttle;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import jp.ghostserver.ghostshuttle.EditActivityRepository.EditActivity;

import java.util.Calendar;

/**
 * Created by denpa on 2017/07/28.
 */

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (EditActivity)getActivity(),  year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //日付が選択されたときの処理

    }

}
