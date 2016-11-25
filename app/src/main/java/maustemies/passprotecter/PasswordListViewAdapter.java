package maustemies.passprotecter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maustemies on 25.11.2016.
 */

public class PasswordListViewAdapter extends ArrayAdapter<PasswordListViewItem> {

    private final Context context;
    private final List<PasswordListViewItem> passwordListViewItemList;

    static class ItemHolder {
        TextView textViewKey;
        TextView textViewValue;
    }

    public PasswordListViewAdapter(Context context, List<PasswordListViewItem> data) {
        super(context, R.layout.password_listview_item, data);
        this.context = context;
        this.passwordListViewItemList = data;
    }

    @Override
    public int getCount() {
        return passwordListViewItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder itemHolder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.password_listview_item, parent, false);

            itemHolder = new ItemHolder();
            itemHolder.textViewKey = (TextView) row.findViewById(R.id.textViewPasswordLayoutKey);
            itemHolder.textViewValue = (TextView) row.findViewById(R.id.textViewPasswordLayoutValue);

            row.setTag(itemHolder);
        }
        else itemHolder = (ItemHolder) row.getTag();

        PasswordListViewItem passwordListViewItem = passwordListViewItemList.get(position);
        itemHolder.textViewKey.setText(passwordListViewItem.Key);
        itemHolder.textViewValue.setText(passwordListViewItem.Value);

        return row;
    }
}
