package io.tanjundang.github;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import io.tanjundang.github.photolib.R;
import io.tanjundang.github.utils.ScreenUtils;


/**
 * Creator: KevinSu kevinsu917@126.com
 * Date 2015-07-21-16:23
 * Description:
 */
class GridPhotoAdapter extends CursorAdapter {

    final int itemWidth;
    LayoutInflater mInflater;
    PhotoPickActivity mActivity;

    View.OnClickListener mClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mActivity.clickPhotoItem(v);
        }
    };

    GridPhotoAdapter(Context context, Cursor c, boolean autoRequery, PhotoPickActivity activity) {
        super(context, c, autoRequery);
        mInflater = LayoutInflater.from(context);
        mActivity = activity;
        int spacePix = context.getResources().getDimensionPixelSize(R.dimen.pickimage_gridlist_item_space);
        itemWidth = (int) ((ScreenUtils.getScreenWidth(context) - spacePix * 4) / 3);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = mInflater.inflate(R.layout.photopick_gridlist_item, parent, false);
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height = itemWidth;
        layoutParams.width = itemWidth;
        convertView.setLayoutParams(layoutParams);


        GridViewHolder holder = new GridViewHolder();
        holder.icon = (ImageView) convertView.findViewById(R.id.ivAvatar);
        holder.iconFore = (ImageView) convertView.findViewById(R.id.iconFore);
        holder.check = (CheckBox) convertView.findViewById(R.id.check);
        GridViewCheckTag checkTag = new GridViewCheckTag(holder.iconFore);
        holder.check.setTag(checkTag);
        holder.check.setOnClickListener(mClickItem);
        convertView.setTag(holder);

        ViewGroup.LayoutParams iconParam = holder.icon.getLayoutParams();
        iconParam.width = itemWidth;
        iconParam.height = itemWidth;
        holder.icon.setLayoutParams(iconParam);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        GridViewHolder holder;
        holder = (GridViewHolder) view.getTag();

        String path = ImageInfo.pathAddPreFix(cursor.getString(1));
        ImageLoader.getInstance().displayImage(path, holder.icon, PhotoPickActivity.optionsImage, null);

        ((GridViewCheckTag) holder.check.getTag()).path = path;

        boolean picked = mActivity.isPicked(path);
        holder.check.setChecked(picked);
        holder.iconFore.setVisibility(picked ? View.VISIBLE : View.INVISIBLE);
    }

    static class GridViewHolder {
        ImageView icon;
        ImageView iconFore;
        CheckBox check;
    }

    static class GridViewCheckTag {
        View iconFore;
        String path = "";

        GridViewCheckTag(View iconFore) {
            this.iconFore = iconFore;
        }
    }
}
