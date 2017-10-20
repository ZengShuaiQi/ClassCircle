package ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;

import presenter.CreateClassPresenter;
import presenter.impl.CreateClassPresenterImpl;
import view.CreateClassView;

/**
 * Created by clever boy on 2017/9/21.
 */

public class CreateClassActivity extends BaseActivity implements CreateClassView, View.OnClickListener {

    private CreateClassPresenter mCreateClassPresenter;
    private EditText className;
    private EditText classDesc;
    private EditText classCount;
    private Button create;
    private TextView title;
    private ImageView back;


    @Override
    protected int getLayouResID() {
        return R.layout.activity_create_class;
    }

    @Override
    protected void init() {
        super.init();
        mCreateClassPresenter = new CreateClassPresenterImpl(this);
        className = (EditText) findViewById(R.id.class_name);
        classDesc = (EditText) findViewById(R.id.class_desc);
        classCount = (EditText) findViewById(R.id.class_count);
        create = (Button) findViewById(R.id.create);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText(getString(R.string.create_class));
        create.setOnClickListener(this);
    }

    @Override
    public void CreateClassSuccess() {
        Toast.makeText(this, "创建班级成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void CreateClassfailed() {
        Toast.makeText(this, "创建班级失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void classExited() {
        Toast.makeText(this, "班级已存在", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void classOutnumber() {
        Toast.makeText(this, "班级人数不符合要求", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                String groupName = className.getText().toString().trim();
                String desc = classDesc.getText().toString().trim();
                int count = Integer.parseInt(classCount.getText().toString().trim());
                mCreateClassPresenter.createClass(groupName, desc, count);
                break;
            case R.id.back:
                finish();
                break;

        }

    }
}
