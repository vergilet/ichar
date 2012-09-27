package com.googlecode.viewsource;

import android.content.Context;
import android.content.res.TypedArray;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: Vergil
 * Date: 06.09.12
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
public class ImageAndTextAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater;

    private String[] mStrings;
    private TypedArray mIcons;

    private int mViewResourceId;

    public ImageAndTextAdapter(Context ctx, int viewResourceId,
                               String[] strings, TypedArray icons) {
        super(ctx, viewResourceId, strings);

        mInflater = (LayoutInflater)ctx.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mStrings = strings;
        mIcons = icons;

        //mVriewResourceId = viewResourceId;
    }

    @Override
    public int getCount() {
        return mStrings.length;
    }

    @Override
    public String getItem(int position) {
        return mStrings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.vkmenulist, null);


        ImageView iv = (ImageView)convertView.findViewById(R.id.row_logo);
        //iv.setImageResource((mIcons[position]));
        iv.setImageDrawable(mIcons.getDrawable(position));

        TextView tv = (TextView)convertView.findViewById(R.id.row_name);
        tv.setText(mStrings[position]);

        return convertView;
    }
}
