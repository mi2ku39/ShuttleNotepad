package jp.ghostserver.ghostshuttle.domain.comopnents.EnhancedListView;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.denpa.ghostshuttle.R;
import com.makeramen.roundedimageview.RoundedImageView;
import jp.ghostserver.ghostshuttle.domain.comopnents.BaseShuttleListAdapter;

import java.util.List;

/**
 * Created by denpa on 2018/01/10.
 */

public class ShuttleListAdapter extends BaseShuttleListAdapter {

    public ShuttleListAdapter(Context context, int resource, List<ShuttleListItem> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        view = super.getView(position, view, parent);

        // リストビューに表示する要素を取得
        ShuttleListItem item = (ShuttleListItem) ItemList.get(position);

        // サムネイル画像を設定
        RoundedImageView thumbnail = view.findViewById(R.id.icon);
        thumbnail.setImageResource(item.getThumbnail());
        thumbnail.setBackgroundColor(Color.parseColor(item.getColor()));

        TextView detail = view.findViewById(R.id.detail);
        detail.setText(item.getDetail());

        return view;

    }

}
