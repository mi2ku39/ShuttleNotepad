package jp.ghostserver.ghostshuttle.ListViewClasses.SimpleListView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.denpa.ghostshuttle.R;
import jp.ghostserver.ghostshuttle.ListViewClasses.BaseShuttleListAdapter;
import jp.ghostserver.ghostshuttle.ListViewClasses.BaseShuttleListItem;

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

        // リストビューに表示する要素を取得
        SimpleListItem item = (SimpleListItem)ItemList.get(position);

        TextView title = view.findViewById(R.id.title);
        title.setText(item.getTitle());

        return view;
    }

}
