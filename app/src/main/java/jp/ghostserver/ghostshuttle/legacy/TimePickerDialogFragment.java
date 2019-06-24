package jp.ghostserver.ghostshuttle.legacy;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import jp.ghostserver.ghostshuttle.domain.editor.EditActivity;

import java.util.Calendar;

/**
 * Created by denpa on 2017/07/28.
 */

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),(EditActivity)getActivity(),hour,min,false);
    }

    @Override
        public void onTimeSet(TimePicker view,int hour,int min){

        }

}
