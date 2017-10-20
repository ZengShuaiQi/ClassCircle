package ui.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myqq.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;


import utils.threadUtils;


/**
 * Created by clever boy on 2017/10/5.
 */

public class ShowBigImage extends BaseActivity {

    private String remoteUrl;
    private String fileName;
    private ImageView image;
    BitmapUtils bitmapUtils ;
    @Override
    protected int getLayouResID() {
        return R.layout.show_big_image;
    }

    @Override
    protected void init() {
        super.init();
        image = (ImageView) findViewById(R.id.big_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        remoteUrl = getIntent().getStringExtra("remoteUrl");
        fileName = getIntent().getStringExtra("fileName");
        bitmapUtils = new BitmapUtils(this);
        showProgressDialog("请稍等。。。。");
        downloadImage();
    }

    private void downloadImage() {
        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {

                bitmapUtils.display(image, remoteUrl, new BitmapLoadCallBack<ImageView>() {
                    @Override
                    public void onLoadCompleted(ImageView container, final String uri, final Bitmap bitmap, final BitmapDisplayConfig config, final BitmapLoadFrom from) {

                        threadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                image.setImageBitmap(bitmap);
                                hideProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onLoadFailed(ImageView container, String uri, Drawable drawable) {

                    }
                });

            }
        });
    }
}
