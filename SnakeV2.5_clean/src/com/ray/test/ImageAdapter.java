package com.ray.test;

import android.basic.snaketestxiong.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext; //define Context 

    private Integer[] mImageIds = { //picture source
            R.drawable.tongnian,
            R.drawable.saonian,
            R.drawable.qinnian,
            R.drawable.baba,
            R.drawable.yeye,
    };

    public ImageAdapter(Context c) { //define ImageAdapter
        mContext = c;
    }

    //get the picture number
    public int getCount() { 
        return mImageIds.length;
    }
    
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	
        ImageView i = new ImageView(mContext);
        i.setImageResource(mImageIds[position]);//set resource for the imageView
        i.setLayoutParams(new Gallery.LayoutParams(TestGallery.w - 150, TestGallery.w - 150));//layout
        i.setScaleType(ImageView.ScaleType.FIT_XY);//set scale type
        return i;
    }
}
