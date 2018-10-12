package jp.ghostserver.ghostshuttle.ListViewClasses.SimpleListView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import jp.ghostserver.ghostshuttle.ListViewClasses.BaseShuttleListAdapter;

import java.util.List;

/**
 * Created by denpa on 2018/01/22.
 */

public class SimpleListAdapter extends BaseShuttleListAdapter {

    public SimpleListAdapter(Context context, int resource, List<SimpleListItem> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        view = super.getView(position, view, parent);
        return view;
    }

}
