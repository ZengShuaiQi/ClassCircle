package ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by clever boy on 2017/10/16.
 */

public class ImageDetailActivity extends BaseActivity {

    private String messageId;
    private String loaclPath;
    private String remoteUrl;
    private TextView comment1;
    private TextView comment2;
    private Button send;
    private EditText text;
    private ImageView image;
    private TextView allComment;


    @Override
    protected int getLayouResID() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void init() {
        super.init();
        comment1 = (TextView) findViewById(R.id.comment1);
        comment2 = (TextView) findViewById(R.id.comment2);
        send = (Button) findViewById(R.id.send);
        text = (EditText) findViewById(R.id.message);
        image = (ImageView) findViewById(R.id.big_image);
        allComment= (TextView) findViewById(R.id.all_comment);

        remoteUrl = getIntent().getStringExtra("remoteUrl");
        loaclPath = getIntent().getStringExtra("loaclPath");
        messageId = getIntent().getStringExtra("messageId");
        initData();
    }

    private void initData() {
        EMMessage message = EMClient.getInstance().chatManager().getMessage(messageId);
        try {
            FileInputStream fis = new FileInputStream(loaclPath);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            image.setImageBitmap(bitmap);

            String stringAttribute0 = message.getStringAttribute("comment0");
            String stringAttribute1 = message.getStringAttribute("comment1");
            comment1.setText(stringAttribute0);
            comment2.setText(stringAttribute1);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

}
