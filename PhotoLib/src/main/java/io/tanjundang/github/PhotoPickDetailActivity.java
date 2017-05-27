package io.tanjundang.github;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import io.tanjundang.github.photolib.R;


/**
 * Creator: KevinSu kevinsu917@126.com
 * Date 2015-07-21-16:23
 * Description: 照片详情
 */
public class PhotoPickDetailActivity extends AppCompatActivity {

    public static final String PICK_DATA = "PICK_DATA";
    public static final String ALL_DATA = "ALL_DATA";
    public static final String FOLDER_NAME = "FOLDER_NAME";
    public static final String PHOTO_BEGIN = "PHOTO_BEGIN";
    public static final String EXTRA_MAX = "EXTRA_MAX";
    public static final String PICKER_TYPE = "PICKER_TYPE";

    private ArrayList<ImageInfo> mPickPhotos;
    private ArrayList<ImageInfo> mAllPhotos;

    private ViewPager mViewPager;
    private CheckBox mCheckBox;
    private LinearLayout llCheckLayout;

    private int mMaxPick = 6;
    private boolean isPickerType = true;//是否选择图片的类型
    private MenuItem mMenuSend;
    private final String actionbarTitle = "%d/%d";
    Cursor mCursor;

    public static void SkipToPhotoDetail(Context context, ArrayList<ImageInfo> mPickPhotos, int photoBegin) {
        Intent intent = new Intent(context, PhotoPickDetailActivity.class);
        intent.putExtra(PhotoPickDetailActivity.ALL_DATA, mPickPhotos);
        intent.putExtra(PhotoPickDetailActivity.PICKER_TYPE, false);
        intent.putExtra(PhotoPickDetailActivity.PHOTO_BEGIN, photoBegin);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pick_detail);
        initView();
        initData();
    }


    protected void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        llCheckLayout = (LinearLayout) findViewById(R.id.llCheckLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
    }


    protected void initData() {
        Bundle extras = getIntent().getExtras();
        mPickPhotos = (ArrayList<ImageInfo>) extras.getSerializable(PICK_DATA);
        mAllPhotos = (ArrayList<ImageInfo>) extras.getSerializable(ALL_DATA);
        isPickerType = extras.getBoolean(PICKER_TYPE, true);

        int mBegin = extras.getInt(PHOTO_BEGIN, 0);
        mMaxPick = extras.getInt(EXTRA_MAX, mMaxPick);
        if (mAllPhotos == null) {
            String folderName = extras.getString(FOLDER_NAME, "");
            String where = folderName;
            if (!folderName.isEmpty()) {
                where = String.format("%s='%s'",
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        folderName);
            }
            mCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.ImageColumns._ID,
                            MediaStore.Images.ImageColumns.DATA},
                    where,
                    null,
                    MediaStore.MediaColumns.DATE_ADDED + " DESC");
        }

        if (mPickPhotos == null) {
            mPickPhotos = new ArrayList<>();
        }


        ImagesAdapter adapter = new ImagesAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mBegin);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateDisplay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mViewPager.getCurrentItem();

                String uri = getImagePath(pos);

                if (((CheckBox) v).isChecked()) {
                    if (mPickPhotos.size() >= mMaxPick) {
                        ((CheckBox) v).setChecked(false);
                        String s = String.format(getString(R.string.photopick_total_limit_toast), mMaxPick);
                        Toast.makeText(PhotoPickDetailActivity.this, s, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addPicked(uri);
                } else {
                    removePicked(uri);
                }
                updateDataPickCount();
            }
        });

        updateDisplay(mBegin);

        if (!isPickerType) {
            llCheckLayout.setVisibility(View.GONE);
            mCheckBox.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        super.onDestroy();
    }

    private void updateDisplay(int pos) {
        String uri = getImagePath(pos);
        mCheckBox.setChecked(isPicked(uri));
        getSupportActionBar().setTitle(String.format(actionbarTitle, pos + 1, getImageCount()));
    }

    private boolean isPicked(String path) {
        for (ImageInfo item : mPickPhotos) {
            if (item.path.equals(path)) {
                return true;
            }
        }

        return false;
    }

    private void addPicked(String path) {
        if (!isPicked(path)) {
            mPickPhotos.add(new ImageInfo(path));
        }
    }

    private void removePicked(String path) {
        for (int i = 0; i < mPickPhotos.size(); ++i) {
            if (mPickPhotos.get(i).path.equals(path)) {
                mPickPhotos.remove(i);
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        selectAndSend(false);
    }

    private void selectAndSend(boolean send) {
        Intent intent = new Intent();
        intent.putExtra("data", mPickPhotos);
        intent.putExtra("send", send);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_pick_detail, menu);
        mMenuSend = menu.getItem(0);
        updateDataPickCount();

        if (!isPickerType) {
            mMenuSend.setVisible(false);
        }

        return true;
    }

    private void updateDataPickCount() {
        String send = String.format(getString(R.string.photopick_sure_text), mPickPhotos.size(), mMaxPick);
        mMenuSend.setTitle(send);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            selectAndSend(false);
            return true;
        } else if (id == R.id.action_send) {
            selectAndSend(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class ImagesAdapter extends FragmentStatePagerAdapter {

        ImagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            ImagePagerFragment fragment = new ImagePagerFragment();
            String path = getImagePath(position);
            fragment.setData(ImageInfo.pathAddPreFix(path));
            return fragment;
        }

        @Override
        public int getCount() {
            return getImageCount();
        }
    }

    String getImagePath(int pos) {
        if (mAllPhotos != null) {
            return mAllPhotos.get(pos).path;
        } else {
            String path = "";
            if (mCursor.moveToPosition(pos)) {
                path = ImageInfo.pathAddPreFix(mCursor.getString(1));
            }
            return path;
        }
    }

    int getImageCount() {
        if (mAllPhotos != null) {
            return mAllPhotos.size();
        } else {
            return mCursor == null ? 0 : mCursor.getCount();
        }
    }

}
