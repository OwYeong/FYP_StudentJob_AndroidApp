package com.oybx.fyp_application.employer_section.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.infomation_classes.ChatMessage;
import com.oybx.fyp_application.student_section.adapter.StudentChatMessageRecyclerViewAdapter;

import java.util.ArrayList;

public class EmployerChatMessageRecyclerViewAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<ChatMessage> chatMessageArrayList = new ArrayList<>();//Array list For storing Employer Post info
    private Bitmap oppenentProfilePic;


    public EmployerChatMessageRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<ChatMessage> chatMessageArrayList, Bitmap oppenentProfilePic) {
        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.chatMessageArrayList = chatMessageArrayList;
        this.oppenentProfilePic =oppenentProfilePic;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ChatMessage currentChatMessage = chatMessageArrayList.get(position);

        if (currentChatMessage.getFrom().equals(ApplicationManager.getMyEmployerAccountInfo().getUsername())) {
            // If message is sent from me
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If message is sent from other user
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_message_send, parent, false);
            return new EmployerChatMessageRecyclerViewAdapter.SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_message_receive, parent, false);
            return new EmployerChatMessageRecyclerViewAdapter.ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int position) {
        final ChatMessage currentMessage = chatMessageArrayList.get(position);

        switch (myViewHolder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((EmployerChatMessageRecyclerViewAdapter.SentMessageHolder) myViewHolder).bind(currentMessage);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((EmployerChatMessageRecyclerViewAdapter.ReceivedMessageHolder) myViewHolder).bind(currentMessage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageArrayList.size();
    }

    public void setNewArrayList(ArrayList<ChatMessage> chatMessageArrayList){

        this.chatMessageArrayList = chatMessageArrayList;
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageSended_textView;
        TextView messageSendedDateTime_textView;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageSended_textView = (TextView) itemView.findViewById(R.id.layout_message_send_message_sended);
            messageSendedDateTime_textView = (TextView) itemView.findViewById(R.id.layout_message_send_message_sended_date_time);
        }

        void bind(ChatMessage message) {
            messageSended_textView.setText(message.getMessageContent());


            messageSendedDateTime_textView.setText(message.getMessageDateTime());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageReceived_textView;
        TextView messageReceivedDateTime_textView;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageReceived_textView = (TextView) itemView.findViewById(R.id.layout_message_received_message_received);
            messageReceivedDateTime_textView = (TextView) itemView.findViewById(R.id.layout_message_received_message_received_date_time);
            profileImage = (ImageView) itemView.findViewById(R.id.layout_message_received_headerContainer_profilePic);
        }

        void bind(ChatMessage message) {
            messageReceived_textView.setText(message.getMessageContent());

            // Format the stored timestamp into a readable String using method.
            messageReceivedDateTime_textView.setText(message.getMessageDateTime());

            profileImage.setImageBitmap(oppenentProfilePic);

        }
    }
}
