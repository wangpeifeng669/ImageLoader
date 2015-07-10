package com.cancai.demo.imageloaderdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cancai.demo.imageloadlib.core.ImageLoader;


public class ImageListViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list_view);

        ListView lv = (ListView) this.findViewById(R.id.lv_image);
        ImageAdapter imageAdapter = new ImageAdapter(this, Constants.IMAGES);
        lv.setAdapter(imageAdapter);
    }

    private static class ViewHolder {
        TextView text;
        ImageView image;
    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private String[] mImageUrl;

        public ImageAdapter(Context context, String[] imageUrl) {
            this.mContext = context;
            this.mImageUrl = imageUrl;
        }

        @Override
        public int getCount() {
            return mImageUrl.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = View.inflate(mContext, R.layout.list_item_image, null);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.tv_text);
                holder.image = (ImageView) view.findViewById(R.id.iv_image);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.text.setText("Item " + (position + 1));

            ImageLoader.getInstance().displayImage(holder.image, mImageUrl[position]);

            return view;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
