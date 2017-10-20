package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ui.activities.addFriendActivity;

import com.example.myqq.R;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clever boy on 2017/9/25.
 */

public class addClassListAdapter extends RecyclerView.Adapter<addClassListAdapter.MyViewHolder> implements View.OnClickListener {

    private List<EMGroup> searchGroup;
    private Context context;
    private OnItemClickListener mOnItemClickListener = null;

    public void setSearchGroup(List<EMGroup> searchGroup) {
        this.searchGroup = searchGroup;
    }

    public addClassListAdapter(Context context) {

        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_search_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.groupName.setText(searchGroup.get(position).getGroupName());
        holder.groupDesc.setText(searchGroup.get(position).getDescription());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (searchGroup == null) {
            return 0;
        }
        return searchGroup.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        TextView groupDesc;

        public MyViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.group_name);
            groupDesc = (TextView) itemView.findViewById(R.id.group_desc);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
