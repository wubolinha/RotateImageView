package halin.rotateimageview;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements View.OnClickListener,Observer {

    private Button playbutton;
    private ProgressBar playprogress;
    private RotateImageView  rotateiv;
    private RelativeLayout storybackground;
    private int diskwidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storybackground=(RelativeLayout)findViewById(R.id.storybackground);
        playbutton= (Button) findViewById(R.id.booklistenplay);
        playprogress= (ProgressBar) findViewById(R.id.listenplayprogress);
        playbutton.setOnClickListener(this);
        rotateiv= new RotateImageView(this);
        rotateiv.setScaleType(ImageView.ScaleType.FIT_XY  );
        initview();
    }

    private void initview( ) {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (storybackground == null) {
                    return;
                }
                int width = storybackground.getWidth();
                int height = storybackground.getHeight();
                diskwidth=width<height?width:height;
                Message msg = new Message();
                msg.what = 100;
                handler.sendMessage(msg);
                if (width > 1) {
                    //取消定时器
                    timer.cancel();
                }
            }
        };
        //延迟每次延迟10 毫秒 隔1秒执行一次
        timer.schedule(task, 10, 100);
        //获取上一个fragment传递过来的数据

    }
    @Override
    protected void onResume() {
        super.onResume();
        ObserverManage.getObserverManage().addObserver(this);
        Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.voice);
        StoryVoicePlay.getInstance().playsound(this, mUri);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.booklistenplay:
            StoryVoicePlay.getInstance().play_pause();
                break;
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        ObserverManage.getObserverManage().deleteObserver(this);
        StoryVoicePlay.getInstance().release();

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 100:
                    storybackground.removeAllViews();
                    RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(diskwidth, diskwidth);
                    rl.addRule(RelativeLayout.CENTER_IN_PARENT);
                    storybackground.addView(rotateiv, rl);

                    rotateiv.setImageResource(R.mipmap.discimg);

                    break;
                case 500:
                    Log.e("bolin", duartion + "\t" + position);
                    playprogress.setProgress(100* position / duartion);
                    rotateiv.stopRotate(isplaying);
                    if(isplaying){
                        playbutton.setBackgroundResource(R.drawable.slt_btn_listen_pause);

                    }else {
                        playbutton.setBackgroundResource(R.drawable.slt_btn_listen_paly);
                    }
                    break;
            }
        }

    };

    int duartion;
    int position;
    boolean isplaying;
    @Override
    public void update(Observable observable, Object data) {
        StoryVoicePlay.playinfo  playdat= (StoryVoicePlay.playinfo) data;
        duartion=playdat.getDuration();
        position=playdat.getCurrentPosition();
        isplaying=playdat.isplaying();
        Message msg = new Message();
        msg.what = 500;
        handler.sendMessage(msg);

    }
}
