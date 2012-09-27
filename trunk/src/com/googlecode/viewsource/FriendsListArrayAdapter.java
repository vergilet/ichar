package com.googlecode.viewsource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 21.08.12
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */
public class FriendsListArrayAdapter extends ArrayAdapter<Result> {

    private LayoutInflater mInflater;
    List<Result> container;
    //private AndroidExtender container;

    public FriendsListArrayAdapter(Context context, int textViewResourceId, List<Result> myFriendsList) {
        super(context, textViewResourceId,  myFriendsList);
        container = myFriendsList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    private class ViewHolder {
        TextView fr_name;
        TextView fr_online;
        ImageView fr_photo;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if ( convertView == null ) {
            convertView = mInflater.inflate(R.layout.row, null);

            holder = new ViewHolder();
            holder.fr_name = (TextView) convertView.findViewById(R.id.row_name);
            holder.fr_online = (TextView) convertView.findViewById(R.id.row_online);
            holder.fr_photo = (ImageView) convertView.findViewById(R.id.row_photo);
            holder.fr_photo.setFocusable(false);
            holder.fr_photo.setClickable(false);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Result android =container.get(position);
        holder.fr_name.setText(android.first_name);
        holder.fr_online.setText(android.online);
        ImageManager.fetchImage(android.photo, holder.fr_photo);
        return convertView;
    }
}
