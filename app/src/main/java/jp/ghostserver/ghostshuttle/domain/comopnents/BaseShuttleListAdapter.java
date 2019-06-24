package jp.ghostserver.ghostshuttle.domain.comopnents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.denpa.ghostshuttle.R;

import java.util.List;

public abstract class BaseShuttleListAdapter extends ArrayAdapter {

    private int Resource;
    protected List<? extends BaseShuttleListItem> ItemList;
    private LayoutInflater Inflater;

    public BaseShuttleListAdapter(
            Context context,
            int resource,
            List<? extends BaseShuttleListItem> itemList) {
        super(context, resource, itemList);

        Resource = resource;
        ItemList = itemList;
        Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.d("Adapter", "List Count : " + itemList.size());
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = Inflater.inflate(Resource, null);
        }

        BaseShuttleListItem item = ItemList.get(position);

        // タイトルを設定
        TextView title = view.findViewById(R.id.title);
        title.setText(item.getTitle());

        return view;
    }
}
