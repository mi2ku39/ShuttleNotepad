package jp.ghostserver.ghostshuttle.ListViewClasses.EnhancedListView;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.denpa.ghostshuttle.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by denpa on 2018/01/10.
 */

public class ShuttleListAdapter extends ArrayAdapter<ShuttleListItem> {

    private int _resource;
    private List<ShuttleListItem> _items;
    private LayoutInflater _inflater;

    public ShuttleListAdapter(Context context, int resource, List<ShuttleListItem> items) {
        super(context, resource, items);
        _resource = resource;
        _items = items;
        _inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = _inflater.inflate(_resource, null);
        }

        // リストビューに表示する要素を取得
        ShuttleListItem item = _items.get(position);

        // サムネイル画像を設定
        RoundedImageView thumbnail = view.findViewById(R.id.icon);
        thumbnail.setImageResource(item.getThumbnail());
        thumbnail.setBackgroundColor(Color.parseColor(item.getColor()));

        // タイトルを設定
        TextView title = view.findViewById(R.id.title);
        title.setText(item.getmTitle());

        TextView detail = view.findViewById(R.id.detail);
        detail.setText(item.getDetail());

        return view;

    }

}
