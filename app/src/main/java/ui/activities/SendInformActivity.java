package ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import utils.PreUtils;
import utils.threadUtils;


/**
 * Created by clever boy on 2017/9/26.
 */

public class SendInformActivity extends BaseActivity {
    private EditText text;
    private Button sendInform;
    private ProgressBar progressBar;
    private ImageView back;
    private TextView title;
    String inform = null;
    String mGroupId;
    private ImageView photo;
    private String picturePath = null;
    private String type = "announcement";

    @Override
    protected int getLayouResID() {
        return R.layout.activity_send_inform;
    }

    @Override
    protected void init() {
        super.init();
        picturePath = null;
        text = (EditText) findViewById(R.id.send_inform_text);
        sendInform = (Button) findViewById(R.id.send_inform);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        photo = (ImageView) findViewById(R.id.send_inform_photo);

        title.setText(getString(R.string.send_inform));
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mGroupId = getIntent().getStringExtra("groupId");

        sendInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                sendInformation();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 设定结果返回
                startActivityForResult(i, 2);
            }
        });

    }

    private void sendInformation() {

        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {

                inform = text.getText().toString();
                if (inform != null && picturePath != null) {
                    EMMessage message = EMMessage.createImageSendMessage(picturePath, true, mGroupId);
                    message.setAttribute("inform", inform);
                    message.setAttribute("type", type);
                    message.setChatType(EMMessage.ChatType.GroupChat);
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    PreUtils.putBoolean(mGroupId,true,getApplicationContext());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(int i, String s) {
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                    EMClient.getInstance().chatManager().sendMessage(message);
                } else if (inform != null && picturePath == null) {
                    EMMessage message = EMMessage.createTxtSendMessage(inform, mGroupId);
                    message.setAttribute("type", type);
                    message.setChatType(EMMessage.ChatType.GroupChat);
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                                    PreUtils.putBoolean(mGroupId,true,getApplicationContext());
                                }
                            });

                        }
                        @Override
                        public void onError(int i, String s) {
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                    EMClient.getInstance().chatManager().sendMessage(message);

                } else {
                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SendInformActivity.this, "请输入通知内容", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                photo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }

    }
}
