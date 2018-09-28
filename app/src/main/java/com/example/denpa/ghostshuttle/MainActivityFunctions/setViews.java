package com.example.denpa.ghostshuttle.MainActivityFunctions;

import com.example.denpa.ghostshuttle.MainActivity;
import com.example.denpa.ghostshuttle.R;

public class setViews {
    public static void findIDs(MainActivity targetActivity){
        targetActivity.fab = targetActivity.findViewById(R.id.fab);
        targetActivity.listview = targetActivity.findViewById(R.id.listview);
        targetActivity.cl = targetActivity.findViewById(R.id.coordinatorLayout);
        targetActivity.fab.setOnClickListener(targetActivity);
    }
}
