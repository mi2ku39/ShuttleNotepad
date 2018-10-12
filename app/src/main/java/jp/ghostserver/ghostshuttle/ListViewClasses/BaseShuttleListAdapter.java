package jp.ghostserver.ghostshuttle.ListViewClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BaseShuttleListAdapter extends ArrayAdapter {

    protected int Resource;
    protected List<? extends BaseShuttleListItem> ItemList;
    protected LayoutInflater Inflater;

    public BaseShuttleListAdapter(
            Context context,
            int resource,
            List<? extends BaseShuttleListItem> itemList) {
        super(context, resource);

        Resource = resource;
        ItemList = itemList;
        Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

}
