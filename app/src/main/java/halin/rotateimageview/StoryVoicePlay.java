package halin.rotateimageview;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/17.
 * 播放
 */
public class StoryVoicePlay {

    private MediaPlayer mediaPlayer = null;

    private static StoryVoicePlay instance;

    public static StoryVoicePlay getInstance() {
        if (instance == null) {
            instance = new StoryVoicePlay();
        }
        return instance;
    }


    public void play_pause() {
        Log.v("bolin", "play_pause");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }


    }

    public void playsound(Context ctx, Uri uri) {  //
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.reset();
                    return false;
                }
            });
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(ctx, uri);
            mediaPlayer.prepare();//缓冲
            mediaPlayer.start();//开始或恢复播放
        } catch (IOException e) {
            e.printStackTrace();

        }
        setTimer();
    }


    public void release() {
        //取消定时器
        if (timer != null) {
            task.cancel();
            timer.cancel();
            timer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }

    private Timer timer = null;
    private TimerTask task;

    private void setTimer() {
        if (timer == null) {
            timer = new Timer();
            task = new TimerTask() {
                public void run() {
                    if (mediaPlayer != null) {

                        ObserverManage.getObserverManage().setMessage(
                                new playinfo(mediaPlayer.getCurrentPosition(),
                                        mediaPlayer.getDuration(), mediaPlayer.isPlaying()));
                    }


                }
            };
            //延迟每次延迟10 毫秒 隔0.1秒执行一次
            timer.schedule(task, 5, 500);
        }
    }


    public static class playinfo {
        int Duration;
        int CurrentPosition;
        boolean isplaying;

        public playinfo(int currentPosition, int duration, boolean isplaying) {
            CurrentPosition = currentPosition;
            Duration = duration;
            this.isplaying = isplaying;
        }

        public int getCurrentPosition() {
            return CurrentPosition;
        }

        public void setCurrentPosition(int currentPosition) {
            CurrentPosition = currentPosition;
        }

        public int getDuration() {
            return Duration;
        }

        public void setDuration(int duration) {
            Duration = duration;
        }

        public boolean isplaying() {
            return isplaying;
        }

        public void setIsplaying(boolean isplaying) {
            this.isplaying = isplaying;
        }
    }


}
