package ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import utils.FileUtils;

import java.io.File;
import java.net.URISyntaxException;

import utils.threadUtils;

/**
 * Created by clever boy on 2017/10/17.
 */

public class UploadFileActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private TextView file;
    private ImageView back;
    private Button upload;
    private ProgressBar progressBar;
    private String mGroupId;
    private String filePath;

    @Override
    protected int getLayouResID() {
        return R.layout.activity_upload_file;
    }

    @Override
    protected void init() {
        super.init();
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        file = (TextView) findViewById(R.id.file);
        upload = (Button) findViewById(R.id.upload_file);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        title.setText("上传文件");
        mGroupId = getIntent().getStringExtra("groupId");
        back.setOnClickListener(this);
        upload.setOnClickListener(this);
        file.setOnClickListener(this);
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.file:
                choosefile();
                break;
            case R.id.upload_file:
                progressBar.setVisibility(View.VISIBLE);
                uploadFile();
                break;
        }
    }

    private void uploadFile() {

        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {
                if (filePath != null) {
                    try {
                        EMClient.getInstance().groupManager().uploadGroupSharedFile(mGroupId, filePath, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                threadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UploadFileActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, String s) {
                                threadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UploadFileActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);

                                    }
                                });

                            }

                            @Override
                            public void onProgress(int i, String s) {
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void choosefile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    // Get the path
                    try {
                        filePath= FileUtils.getPath(this,uri);
                        String b = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                        this.file.setText(b);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;

        }
    }
}