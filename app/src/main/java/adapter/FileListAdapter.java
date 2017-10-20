package adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;

import java.io.File;
import java.util.List;
import com.hyphenate.util.FileUtils;
import com.hyphenate.util.PathUtil;

import ui.activities.ClassFileActivity;


/**
 * Created by clever boy on 2017/10/17.
 */

public class FileListAdapter extends BaseAdapter {
    private List<EMMucSharedFile> emMucSharedFiles;
    private Context context;
    private ClassFileActivity classFileActivity;

    public FileListAdapter(Context context,ClassFileActivity classFileActivity) {
        this.context = context;
        this.classFileActivity = classFileActivity;
    }

    public void setList(List<EMMucSharedFile> emMucSharedFiles) {
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
    public EMMucSharedFile getItem(int position) {
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
            convertView = View.inflate(context, R.layout.file_list_item, null);
            holder = new ViewHolder();
            holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
            holder.download = (Button) convertView.findViewById(R.id.download_file);
            holder.delete = (Button) convertView.findViewById(R.id.delete_file);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final EMMucSharedFile item = getItem(position);
        final File localFile = new File(PathUtil.getInstance().getFilePath(), item.getFileName());
        if(localFile.exists()){
            holder.download.setText("打开");
        }else{
            holder.download.setText("下载");
        }
        holder.fileName.setText(item.getFileName());
        if(classFileActivity.getIsGroupManager()){
            holder.delete.setVisibility(View.VISIBLE);
        }else{
            holder.delete.setVisibility(View.INVISIBLE);
        }

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    classFileActivity.showFile(item);
                    if(localFile.exists()){
                        holder.download.setText("打开");
                    }else{
                        holder.download.setText("下载");
                    }
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               classFileActivity.deleteGroupFile(position);
            }
        });



        return convertView;
    }





    class ViewHolder {
        TextView fileName;
        Button download;
        Button delete;
    }
}
