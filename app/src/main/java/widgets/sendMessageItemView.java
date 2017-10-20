package widgets;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myqq.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 我是小丑逼 on 2017/1/3.
 */

public class sendMessageItemView extends RelativeLayout {
    @BindView(R.id.time_stamp)
    TextView timeStamp;
    @BindView(R.id.send_content)
    TextView sendContent;
    @BindView(R.id.send_message_progress)
    ImageView mProgressImageVIew;

    public sendMessageItemView(Context context) {
        this(context, null);
    }

    public sendMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_message, this);
        ButterKnife.bind(this,this);
    }

    public void bindVIew(EMMessage emMessage) {
        long msgTime = emMessage.getMsgTime();
        timeStamp.setText(DateUtils.getTimestampString(new Date(msgTime)));
        EMMessageBody body = emMessage.getBody();
        if(body instanceof EMTextMessageBody){
            sendContent.setText(((EMTextMessageBody) body).getMessage());
        }else{
            sendContent.setText(R.string.no_text_message);
        }
        switch(emMessage.status()){
            case INPROGRESS:
                mProgressImageVIew.setVisibility(VISIBLE);
                mProgressImageVIew.setImageResource(R.drawable.send_message_progress);
                AnimationDrawable drawable = (AnimationDrawable) mProgressImageVIew.getDrawable();
                drawable.start();
                break;
            case SUCCESS:
                mProgressImageVIew.setVisibility(GONE);
                break;
            case FAIL:
                mProgressImageVIew.setImageResource(R.mipmap.msg_error);
                break;
        }

    }
}
