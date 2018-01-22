package com.example.denpa.ghostshuttle;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by denpa on 2018/01/22.
 */

public class ShuttlePreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
    }

}
