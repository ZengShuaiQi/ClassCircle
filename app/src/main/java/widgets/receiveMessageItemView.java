package widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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

public class receiveMessageItemView extends RelativeLayout {
    @BindView(R.id.time_stamp)
    TextView timeStamp;
    @BindView(R.id.receive_content)
    TextView receiveContent;

    public receiveMessageItemView(Context context) {
        this(context, null);
    }

    public receiveMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_receive_message, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage) {
        long msgTime = emMessage.getMsgTime();
        timeStamp.setText(DateUtils.getTimestampString(new Date(msgTime)));
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            receiveContent.setText(((EMTextMessageBody) body).getMessage());
        } else {
            receiveContent.setText(R.string.no_text_message);
        }
    }
}
