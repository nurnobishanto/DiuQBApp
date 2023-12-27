package com.softitbd.diuquestionbank.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softitbd.diuquestionbank.Model.ListModel;
import com.softitbd.diuquestionbank.R;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ListModel> {

    public ListAdapter(Context context, List<ListModel> itemList) {
        super(context, 0, itemList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = convertView.findViewById(R.id.textViewName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ListModel item = getItem(position);

        if (item != null) {
            viewHolder.textViewName.setText(item.getName());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewName;
    }
}
