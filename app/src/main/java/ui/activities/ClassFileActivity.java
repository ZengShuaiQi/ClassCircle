package ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.FileUtils;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.util.List;

import adapter.FileListAdapter;
import utils.threadUtils;

/**
 * Created by clever boy on 2017/10/17.
 */

public class ClassFileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView add;
    private TextView title;
    private String mGroupId;
    private boolean isGroupManager;
    private ListView listView;
    private FileListAdapter adapter;
    private List<EMMucSharedFile> emMucSharedFiles;

    @Override
    protected int getLayouResID() {
        return R.layout.activity_class_file;
    }

    @Override
    protected void init() {
        super.init();
        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        title = (TextView) findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.file_list);
        title.setText("班级文件");
        isGroupManager = getIntent().getBooleanExtra("isGroupManager", false);
        if (isGroupManager) {
            add.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.INVISIBLE);
        }
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        mGroupId = getIntent().getStringExtra("groupId");

        uploadFileList();

        adapter = new FileListAdapter(this,ClassFileActivity.this);
        adapter.setList(emMucSharedFiles);
        listView.setAdapter(adapter);

//        title.setText(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath().toString());
    }

    private void uploadFileList() {
        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {
                try {
                    emMucSharedFiles = EMClient.getInstance().groupManager().fetchGroupSharedFileList(mGroupId, 0, 0);
                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ClassFileActivity.this, emMucSharedFiles.size()+"", Toast.LENGTH_SHORT).show();
                            adapter.setList(emMucSharedFiles);
                            adapter.notifyDataSetChanged();
                           }

                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Intent intent = new Intent(this,UploadFileActivity.class);
                intent.putExtra("groupId",mGroupId);
                startActivity(intent);
                break;
        }
    }
    /**
     * If local file doesn't exits, download it first.
     * else show file directly
     * @param file
     */
    public void showFile(EMMucSharedFile file){
        final File localFile = new File(PathUtil.getInstance().getFilePath(), file.getFileName());

        if(localFile.exists()){
            openFile(localFile);
            return;
        }

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Downloading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        EMClient.getInstance().groupManager().asyncDownloadGroupSharedFile(
                mGroupId,
                file.getFileId(),
                localFile.getAbsolutePath(),
                new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                openFile(localFile);
                            }
                        });
                    }

                    @Override
                    public void onError(int code, final String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(ClassFileActivity.this, "Download file fails, " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }
                }
        );
    }

    public void openFile(File file){
        if(file != null && file.exists()){
            FileUtils.openFile(file,this);
        }
    }


    public void deleteGroupFile(final int position) {
        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().groupManager().asyncDeleteGroupSharedFile(mGroupId, adapter.getItem(position).getFileId(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        uploadFileList();

                    }

                    @Override
                    public void onError(int i, final String s) {
                        threadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ClassFileActivity.this, "删除失败"+s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }
    public boolean getIsGroupManager(){
        return isGroupManager;
    }
}
