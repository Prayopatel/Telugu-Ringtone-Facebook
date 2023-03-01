package com.telugu.ringtoneapp.best.songs.ringtones.ui.list;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.telugu.ringtoneapp.best.songs.ringtones.R;
import java.util.ArrayList;

public class ListCardAdapter extends RecyclerView.Adapter<ListCardAdapter.ViewHolder> {
    private OnItemsClickListener listener;
    private ArrayList<ListCardModel> mantraModelArrayList;
    private OnItemsClickListener mediaPlayerListener;
    private static final String TAG = "ListCard";

    public interface OnItemsClickListener {
        void onItemClick(ListCardModel listCardModel);
    }

    public ListCardAdapter(ArrayList<ListCardModel> arrayList) {
        this.mantraModelArrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewholder, int i) {
        final ListCardModel listCardModel = this.mantraModelArrayList.get(i);
        viewholder.textView.setText(listCardModel.getRingtoneName());
        if (listCardModel.isPlayingState()) {
            viewholder.btnPlayPause.setImageResource(R.drawable.ic_audio_pause);
        } else {
            viewholder.btnPlayPause.setImageResource(R.drawable.ic_audio_play);
        }
        viewholder.cardView.setOnClickListener(view ->
                ListCardAdapter.this.onRingtoneCardClick(listCardModel));
        viewholder.btnPlayPause.setOnClickListener(view ->
                ListCardAdapter.this.onPlayPause(listCardModel, viewholder));
    }

    public  void onRingtoneCardClick(ListCardModel listCardModel) {
        OnItemsClickListener onItemsClickListener = this.listener;
        if (onItemsClickListener != null) {
            onItemsClickListener.onItemClick(listCardModel);
        }
    }

    //play and pause click
    public void onPlayPause(ListCardModel listCardModel, ViewHolder viewholder) {
        if (listCardModel.isPlayingState()) {
            viewholder.btnPlayPause.setImageResource(R.drawable.ic_audio_pause);
            listCardModel.setPlayingState(false);
        } else {
            viewholder.btnPlayPause.setImageResource(R.drawable.ic_audio_play);
            listCardModel.setPlayingState(true);
        }
        OnItemsClickListener onItemsClickListener = this.mediaPlayerListener;
        if (onItemsClickListener != null) {
            onItemsClickListener.onItemClick(listCardModel);
        }
    }

    public void setWhenClickListener(OnItemsClickListener onItemsClickListener) {
        Log.d(TAG, "setWhenClickListener: ");
        this.listener = onItemsClickListener;
    }

    public void setWhenPlayClickListener(OnItemsClickListener onItemsClickListener) {
        Log.d(TAG, "setWhenPlayClickListener: ");
        this.mediaPlayerListener = onItemsClickListener;
    }

    @Override
    public int getItemCount() {
        return this.mantraModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView btnPlayPause;
        CardView cardView;
        private final TextView textView;

        ViewHolder(View view) {
            super(view);
            this.cardView =  view.findViewById(R.id.idCardList);
            this.textView =  view.findViewById(R.id.idListCardText);
            this.btnPlayPause = view.findViewById(R.id.idMideaPlayer);
        }
    }
}
