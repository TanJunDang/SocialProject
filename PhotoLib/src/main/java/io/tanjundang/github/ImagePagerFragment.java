package io.tanjundang.github;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import io.tanjundang.github.photolib.R;
import io.tanjundang.github.utils.ScreenUtils;
import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ImagePagerFragment extends Fragment {

    private final View.OnClickListener onClickImageClose = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };
    private final PhotoViewAttacher.OnPhotoTapListener onPhotoTapClose = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float v, float v2) {
            getActivity().onBackPressed();
        }
    };
    private final PhotoViewAttacher.OnViewTapListener onViewTapListener = new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float v, float v1) {
            getActivity().onBackPressed();
        }
    };
    String uri;
    View image;
    private String TAG = this.getClass().getName();
    private View baseView;
    private DonutProgress circleLoading;
    private ViewGroup rootLayout;
    private View imageLoadFail;
    //大图
    public static DisplayImageOptions imageBigOptions = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.photopick_ic_default_image)
            .showImageForEmptyUri(R.drawable.photopick_ic_default_image)
            .showImageOnFail(R.drawable.photopick_ic_default_image)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    public void setData(String uriString) {
        uri = uriString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_photopick_image_pager, container, false);
        initView();
        showPhoto();
        return baseView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView() {
        circleLoading = (DonutProgress) baseView.findViewById(R.id.circleLoading);
        rootLayout = (ViewGroup) baseView.findViewById(R.id.rootLayout);
        imageLoadFail = baseView.findViewById(R.id.imageLoadFail);
    }

    private void showPhoto() {
        if (!isAdded()) {
            return;
        }
        //使用displayImage,而不使用loadImage,因为loadImage会导致当同时加载同一个url的时候出现task被取消的情况
        //详情见https://github.com/nostra13/Android-Universal-Image-Loader/issues/804
        ImageSize size = new ImageSize(ScreenUtils.getScreenWidth(getContext()), ScreenUtils.getScreenHeight(getContext()));
        NonViewAware imageAware = new NonViewAware(size, ViewScaleType.CROP);
        ImageLoader.getInstance().displayImage(uri, imageAware, imageBigOptions, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        circleLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        if (!isAdded()) {
                            return;
                        }

                        circleLoading.setVisibility(View.GONE);
                        imageLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
                        if (!isAdded()) {
                            return;
                        }

                        circleLoading.setVisibility(View.GONE);

                        File file;
                        if (ImageInfo.isLocalFile(uri)) {
                            file = ImageInfo.getLocalFile(uri);
                        } else {
                            file = ImageLoader.getInstance().getDiskCache().get(imageUri);
                        }
                        if (isGifByFile(file)) {
                            image = getActivity().getLayoutInflater().inflate(R.layout.photopick_imageview_gif, null);
                            rootLayout.addView(image);
                            image.setOnClickListener(onClickImageClose);
                        } else {
                            PhotoView photoView = (PhotoView) getActivity().getLayoutInflater().inflate(R.layout.photopick_imageview_touch, null);
                            image = photoView;
                            rootLayout.addView(image);
                            photoView.setOnPhotoTapListener(onPhotoTapClose);
                            photoView.setOnViewTapListener(onViewTapListener);
                        }

                        try {
                            if (image instanceof GifImageView) {
                                Uri uri1 = Uri.fromFile(file);
                                ((GifImageView) image).setImageURI(uri1);
                            } else if (image instanceof PhotoView) {
                                ((PhotoView) image).setImageBitmap(loadedImage);

                            }
                        } catch (Exception e) {
                            Log.v(TAG, e.getMessage());
                        }
                    }
                },
                new ImageLoadingProgressListener() {

                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        if (!isAdded()) {
                            return;
                        }

//                        可以用DonutProgress 框架设置进度
//                        int progress = current * 100 / total;
//                        circleLoading.setProgress(progress);
                    }
                });
    }

    public static boolean isGifByFile(File file) {
        try {
            int length = 10;
            InputStream is = new FileInputStream(file);
            byte[] data = new byte[length];
            is.read(data);
            String type = getType(data);
            is.close();

            if (type.equals("gif")) {
                return true;
            }
        } catch (Exception e) {
            Log.v("GIF", e.getMessage());
        }

        return false;
    }

    private static String getType(byte[] data) {
        String type = "";
        if (data[1] == 'P' && data[2] == 'N' && data[3] == 'G') {
            type = "png";
        } else if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
            type = "gif";
        } else if (data[6] == 'J' && data[7] == 'F' && data[8] == 'I'
                && data[9] == 'F') {
            type = "jpg";
        }
        return type;
    }
}
