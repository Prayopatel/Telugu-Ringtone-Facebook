package com.telugu.ringtoneapp.best.songs.ringtones.ui.list;

public class ListCardModel {
    private boolean playingState;
    private int position;
    private String ringtoneName;

    
    public ListCardModel(int i, String str, boolean isPlaying) {
        this.position = i;
        this.ringtoneName = str;
        this.playingState = isPlaying;
    }

    public int getPosition() {
        return this.position;
    }

    public String getRingtoneName() {
        return this.ringtoneName;
    }

    public boolean isPlayingState() {
        return this.playingState;
    }

    public void setPlayingState(boolean z) {
        this.playingState = z;
    }
}
