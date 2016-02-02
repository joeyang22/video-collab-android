package me.joeyang.videocollab.Models;

/**
 * Created by joe on 15-12-26.
 */
public class Video {
    public String title;
    public String channel;
    public String videoId;
    public String thumbnailUrl;
    public int votes;
    public Video(String title, String videoId, String thumbnailUrl){
        this.title = title;
        this.videoId = videoId;
        this.thumbnailUrl = thumbnailUrl;
        this.votes=0;
    }
}
