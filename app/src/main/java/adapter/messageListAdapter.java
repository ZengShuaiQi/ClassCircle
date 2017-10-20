package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;

import java.util.List;

import widgets.receiveMessageItemView;
import widgets.sendMessageItemView;

/**
 * Created by 我 on 2017/1/3.
 */

public class messageListAdapter extends RecyclerView.Adapter {
    public static final int ITEM_TYPE_SEND = 0;
    public static final int ITEM_TYPE_RECEIVE = 1;

    private Context mContext;
    private List<EMMessage> emMessages;


    public messageListAdapter(Context context, List<EMMessage> messages) {
        mContext = context;
        emMessages = messages;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_SEND) {
            return new sendMessageListItemViewHolder(new sendMessageItemView(mContext));
        }else {
            return new receiveMessageListItemViewHolder(new receiveMessageItemView(mContext));
        }



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof sendMessageListItemViewHolder) {
            ((sendMessageListItemViewHolder) holder).mSendMessageItemView.bindVIew(emMessages.get(position));
        } else if (holder instanceof receiveMessageListItemViewHolder) {
            ((receiveMessageListItemViewHolder) holder).mReceiveMessageItemView.bindView(emMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
            return emMessages.size();

    }

    @Override
    public int getItemViewType(int position) {

        EMMessage emMessage = emMessages.get(position);
        return emMessage.direct() == EMMessage.Direct.SEND ? ITEM_TYPE_SEND : ITEM_TYPE_RECEIVE;

    }

    public void addMessage(EMMessage emMessage) {
        emMessages.add(emMessage);
        notifyDataSetChanged();
    }


    public class sendMessageListItemViewHolder extends RecyclerView.ViewHolder {
        private sendMessageItemView mSendMessageItemView;

        public sendMessageListItemViewHolder(sendMessageItemView itemView) {
            super(itemView);
            mSendMessageItemView = itemView;
        }
    }

    public class receiveMessageListItemViewHolder extends RecyclerView.ViewHolder {
        private receiveMessageItemView mReceiveMessageItemView;

        public receiveMessageListItemViewHolder(receiveMessageItemView itemView) {
            super(itemView);
            mReceiveMessageItemView = itemView;
        }
    }

}
