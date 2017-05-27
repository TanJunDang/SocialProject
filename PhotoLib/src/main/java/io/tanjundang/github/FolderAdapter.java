package io.tanjundang.github;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import io.tanjundang.github.photolib.R;


/**
 * Creator: KevinSu kevinsu917@126.com
 * Date 2015-07-21-16:23
 * Description:
 */
public class FolderAdapter extends BaseAdapter {

    ArrayList<ImageInfoExtra> mFolderData = new ArrayList<>();
    private String mSelect = "";

    public FolderAdapter(ArrayList<ImageInfoExtra> mFolderData) {
        this.mFolderData = mFolderData;
    }

    public String getSelect() {
        return mSelect;
    }

    public void setSelect(int pos) {
        if (pos >= getCount()) {
            return;
        }

        mSelect = mFolderData.get(pos).getmName();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolderData.size();
    }

    @Override
    public Object getItem(int position) {
        return mFolderData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.photopick_list_item, parent, false);
            holder = new ViewHolder();
            holder.foldIcon = (ImageView) convertView.findViewById(R.id.foldIcon);
            holder.foldName = (TextView) convertView.findViewById(R.id.foldName);
            holder.photoCount = (TextView) convertView.findViewById(R.id.photoCount);
            holder.check = convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageInfoExtra data = mFolderData.get(position);
        String uri = data.getPath();
        int count = data.getCount();

        holder.foldName.setText(data.getmName());

        String format = parent.getContext().getResources().getString(R.string.photo_count_format_text);
        holder.photoCount.setText(String.format(format, count));
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(parent.getContext()));
        ImageLoader.getInstance().displayImage(ImageInfo.pathAddPreFix(uri), holder.foldIcon, PhotoPickActivity.optionsImage, null);

        if (data.getmName().equals(mSelect)) {
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView foldIcon;
        TextView foldName;
        TextView photoCount;
        View check;
    }
}
