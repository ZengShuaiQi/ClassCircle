package adapter;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import ui.activities.ClassFileActivity;
import ui.activities.PostPollActivity;


/**
 * Created by clever boy on 2017/10/17.
 */

public class OptionListAdapter extends BaseAdapter {
    private List<EMMessage> emMucSharedFiles;
    private Context context;
//    private PostPollActivity postPollActivity;

    public OptionListAdapter(Context context) {
        this.context = context;
//        this.postPollActivity = postPollActivity;
    }

    public void setList(List<EMMessage> emMucSharedFiles) {
        this.emMucSharedFiles = emMucSharedFiles;
    }

    @Override
    public int getCount() {
        if (emMucSharedFiles == null) {
            return 0;
        }
        return emMucSharedFiles.size();
    }

    @Override
    public EMMessage getItem(int position) {
        return emMucSharedFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.option_list_item, null);
            holder = new ViewHolder();
            holder.fileName = (TextView) convertView.findViewById(R.id.options);
            holder.time = (TextView) convertView.findViewById(R.id.vote_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EMMessage item = getItem(position);
        EMTextMessageBody emTextMessageBody = (EMTextMessageBody) item.getBody();
        String s = emTextMessageBody.getMessage().toString();
        holder.fileName.setText(s);
        long msgTime = item.getMsgTime();
        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd hh:mm");
        //进行格式化
        String strs=sdf.format(msgTime);
        holder.time.setText(strs);

        return convertView;
    }

    public String[] getCount(int position){
        EMMessage item = getItem(position);
        String[] count = new String[5];

        for (int i =1;i<=5;i++){
            try {
                String option = item.getStringAttribute("option"+i);
                if(!TextUtils.isEmpty(option)){
                   count[i-1] = option;
                }
            } catch (HyphenateException e) {
                break;
            }
        }
        return count;
    }
    public String getType(int position){
        EMMessage item = getItem(position);
        try {
            String type = item.getStringAttribute("type");
            return type;
        } catch (HyphenateException e) {

        }
        return null;
    }



    class ViewHolder {
        TextView fileName;
        TextView time;
    }
}
