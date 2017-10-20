package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myqq.R;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import ui.activities.ImageDetailActivity;
import ui.activities.ShowBigImage;

/**
 * Created by clever boy on 2017/10/4.
 */

public class PhotoListAdapter extends BaseAdapter {
    private List<EMMessage> msgList;
    private Context context;

    public PhotoListAdapter(Context context, List<EMMessage> msgList) {
        this.msgList = msgList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (msgList == null){
            return 0;
        }
        return msgList.size();
    }


    @Override
    public EMMessage getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageViewHolder imageViewHolder;
        if (convertView==null){
            convertView = View.inflate(context, R.layout.gdv_item,null);
            imageViewHolder = new ImageViewHolder();
            imageViewHolder.image = (ImageView) convertView.findViewById(R.id.gdv_item_image);
            convertView.setTag(imageViewHolder);
        }else{
            imageViewHolder = (ImageViewHolder) convertView.getTag();
        }
        try {
            final EMMessage item = getItem(position);
            final EMImageMessageBody body = ((EMImageMessageBody) getItem(position).getBody());
            final String localUrl = body.thumbnailLocalPath();
            FileInputStream fis = new FileInputStream(localUrl);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            imageViewHolder.image.setImageBitmap(bitmap);
            imageViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String remoteUrl = body.getRemoteUrl();
                    Intent intent = new Intent(context, ShowBigImage.class);
                    intent.putExtra("remoteUrl",remoteUrl);
                    intent.putExtra("loaclPath",localUrl);
                    intent.putExtra("messageId",item.getMsgId());
                    context.startActivity(intent);
                }
            });

        }

        catch (Exception e){

        }

        return convertView;
    }

    class ImageViewHolder{
        public ImageView image;
    }

}
