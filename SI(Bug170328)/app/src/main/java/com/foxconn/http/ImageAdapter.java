package com.foxconn.http;

import java.util.List;

import com.bgxt.gallerydemo.R;
import com.foxconn.main.MainActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	
	//public Context mContext;
	//public String [] strFaceType = new String[]{"Back","Bottom","Front","Left","Right","Top"};
	public static final BaseAdapter Adapter = null;
	private List<String> imageUrls; // ?????list
	private Context context;
	int mGalleryItemBackground;

	public ImageAdapter(List<String> imageUrls, Context context) {
		
		//private String[] str={"??????","??????","?????????"};
		

		this.imageUrls = imageUrls;
		this.context = context;
		// /*
		// * ?????res/values/attrs.xml???<declare-styleable>???? ??Gallery????.
		// */
		TypedArray a = context.obtainStyledAttributes(R.styleable.Gallery1);
		/* ???Gallery?????Index id */
		mGalleryItemBackground = a.getResourceId(
				R.styleable.Gallery1_android_galleryItemBackground, 0);
		/* ??????styleable?????????????? */
		a.recycle();
	}

	public int getCount() {
		return imageUrls.size();
	}

	public Object getItem(int position) {
		return imageUrls.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Bitmap image;
		ImageView view = new ImageView(context);
		image = MainActivity.imagesCache.get(imageUrls.get(position));
		// ???????????
		if (image == null) {
			image = MainActivity.imagesCache.get("background_non_load");
		}
		
		// ??????????????????
		view.setImageBitmap(image);
		view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		view.setLayoutParams(new Gallery.LayoutParams(200, 200));
		view.setBackgroundResource(mGalleryItemBackground);
		
		/* ????Gallery????? */
		return view;
	}

}

