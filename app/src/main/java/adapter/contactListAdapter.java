package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import model.contactListItem;
import ui.activities.chatActivity;
import widgets.contactListItemView;

/**
 * Created by 我是小丑逼 on 2017/1/2.
 */

public class contactListAdapter extends RecyclerView.Adapter<contactListAdapter.ContactListItemVIewHolder> {
    private Context context;
    private List<contactListItem> mContactListItems;

    public contactListAdapter(Context context,List<contactListItem> listItems){
        this.context = context;
        mContactListItems = listItems;
    }
    @Override
    public  ContactListItemVIewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactListItemVIewHolder(new contactListItemView(context));
    }

    @Override
    public void onBindViewHolder(ContactListItemVIewHolder holder, final int position) {
        holder.contactListItemView.bindView(mContactListItems.get(position));
        holder.contactListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,chatActivity.class);
                intent.putExtra("contact",mContactListItems.get(position).contact);
                intent.putExtra("groupId",mContactListItems.get(position).groupId);
                intent.putExtra("isGroupManager",mContactListItems.get(position).isGroupManager);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactListItems.size();
    }


    public class ContactListItemVIewHolder extends RecyclerView.ViewHolder{
        public contactListItemView contactListItemView;
        public ContactListItemVIewHolder(contactListItemView itemView) {
            super(itemView);
            contactListItemView = itemView;
        }
    }

}
