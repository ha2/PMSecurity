package com.example.arpad.pmsecurity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by arpad on 2015.01.27..
 */
public class Adapter extends BaseAdapter {
    private static final String TAG = "TAG:Adapter: ";
    private Context mContext;
    private List<Item> mItemList;

    public Adapter( Context context, List<Item> itemList){
        mContext = context;
        mItemList = itemList;
    }

    @Override
    public int getCount() {
        //return 0;
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        //return null;
        return mItemList.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvApplicationLabel;
        TextView tvPkgDescription;
        ImageView ivIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return null;
        View v = convertView;
        if ( v==null ){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate( R.layout.single_item, null);
            ViewHolder holder = new ViewHolder();
            holder.tvApplicationLabel = (TextView) v.findViewById( R.id.tvPackageName );
            holder.tvPkgDescription = (TextView) v.findViewById( R.id.tvPackageDescription );
            v.setTag( holder );
        }

        TextView tvPkgName = (TextView) v.findViewById( R.id.tvPackageName );
        TextView tvPkgDescription = (TextView) v.findViewById( R.id.tvPackageDescription );
        ImageView ivPkgIcon = (ImageView) v.findViewById( R.id.ivAppIcon );

        Item item = mItemList.get( position );
        if ( item != null ) {
            ViewHolder holder = (ViewHolder) v.getTag();
            //Log.d(TAG, "getView(): holder használat");
            holder.tvApplicationLabel.setText( item.getPackageName() );
            holder.tvPkgDescription.setText( item.getPackageDescription() );
        }
        //tvPkgName.setText( item.getPackageName() );
        tvPkgName.setText( item.getApplicationLabel() );
        tvPkgDescription.setText( item.getPackageDescription() );
        ivPkgIcon.setImageDrawable(item.getPkgIcon());
        return v;
    }

    public void removeItem ( int index ){
        mItemList.remove( index );
        return;
    }
}
