package widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myqq.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.contactListItem;

/**
 * Created by 我是小丑逼 on 2017/1/2.
 */

public class contactListItemView extends RelativeLayout {
    @BindView(R.id.first_letter)
    TextView firstLetter;
    @BindView(R.id.contact)
    TextView contact;

    public contactListItemView(Context context) {
        this(context, null);
    }

    public contactListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_list, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(contactListItem contactListItem) {
        contact.setText(contactListItem.contact);
        if(contactListItem.showFirstLetter){
            firstLetter.setVisibility(VISIBLE);
            firstLetter.setText(contactListItem.getFirstLetter());
        }else{
            firstLetter.setVisibility(GONE);
        }

    }
}
