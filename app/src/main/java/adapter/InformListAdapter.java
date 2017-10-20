package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.io.FileInputStream;
import java.util.List;

import ui.activities.ShowBigImage;

/**
 * Created by clever boy on 2017/10/4.
 */

public class InformListAdapter extends BaseAdapter {
    private List<EMMessage> msgList;
    private Context context;
    private final int IMAGE= 1;
    private final int TEXT= 0;

    public InformListAdapter(Context context,List<EMMessage> msgList) {
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(msgList.get(position).getType() == EMMessage.Type.TXT){
            return TEXT;
        }else{
            return IMAGE;
        }

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
        switch (getItemViewType(position)){
            case TEXT:
                TextViewHolder textHolder;
                if (convertView==null){
                    convertView = View.inflate(context, R.layout.inform_list_text,null);
                    textHolder = new TextViewHolder();
                    textHolder.informText = (TextView) convertView.findViewById(R.id.tv_inform);
                    convertView.setTag(textHolder);
                }else{
                    textHolder = (TextViewHolder) convertView.getTag();
                }
                textHolder.informText.setText(((EMTextMessageBody)getItem(position).getBody()).getMessage().toString());
                break;
            case IMAGE:
                ImageViewHolder imageViewHolder;
                if (convertView==null){
                    convertView = View.inflate(context, R.layout.inform_list_image,null);
                    imageViewHolder = new ImageViewHolder();
                    imageViewHolder.informText = (TextView) convertView.findViewById(R.id.tv_inform);
                    imageViewHolder.image = (ImageView) convertView.findViewById(R.id.inform_photo);
                    convertView.setTag(imageViewHolder);
                }else{
                    imageViewHolder = (ImageViewHolder) convertView.getTag();
                }
                try {
                    EMMessage item = getItem(position);
                    String inform = item.getStringAttribute("inform");
                    if(inform!=null){
                        imageViewHolder.informText.setText(inform);
                    }
                    String localUrl = ((EMImageMessageBody) item.getBody()).thumbnailLocalPath();
                    FileInputStream fis = new FileInputStream(localUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    imageViewHolder.image.setImageBitmap(bitmap);
                    imageViewHolder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EMImageMessageBody body = (EMImageMessageBody) getItem(position).getBody();
                            String remoteUrl = body.getRemoteUrl();
                            String fileName = body.getFileName();
                            Intent intent = new Intent(context, ShowBigImage.class);
                            intent.putExtra("remoteUrl",remoteUrl);
                            intent.putExtra("fileName",fileName);
                            context.startActivity(intent);

                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }catch (Exception e){

                }
                break;
        }
        return convertView;
    }

    class ImageViewHolder{
        public ImageView image;
        public TextView informText;
    }
    class TextViewHolder{
        public TextView informText;
    }
}
