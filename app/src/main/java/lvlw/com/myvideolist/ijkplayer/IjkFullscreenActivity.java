package lvlw.com.myvideolist.ijkplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.dl7.player.media.IjkPlayerView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.entity.QFileInfo;

public class IjkFullscreenActivity extends AppCompatActivity {


//    private static final String IMAGE_URL = "http://vimg3.ws.126.net/image/snapshot/2016/11/C/T/VC628QHCT.jpg";
    IjkPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson=new Gson();
        String playvideo=getIntent().getExtras().getString("playlist");
        QFileInfo qFileInfo=gson.fromJson(playvideo,QFileInfo.class);
        String VIDEO_URL = qFileInfo.get_filePath();
        mPlayerView = new IjkPlayerView(this);
        setContentView(mPlayerView);
//        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
//        mediaMetadataRetriever.setDataSource(VIDEO_URL);
//        Bitmap bitmap=mediaMetadataRetriever.getFrameAtTime();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] bytes=baos.toByteArray();
        Glide.with(this).load(R.mipmap.logo_yunpan_home).fitCenter().into(mPlayerView.mPlayerThumb);
        if (qFileInfo.get_fileExt().equals(".blv")){
                File file=new File(qFileInfo.get_filePath()).getParentFile().getParentFile();
            InputStream in = null;
            try {
                in = new FileInputStream(new File(file.getPath()+"/danmaku.xml"));
                mPlayerView.init()
                        .alwaysFullScreen()
                        .enableOrientation()
                        .setVideoPath(VIDEO_URL)
                        .enableDanmaku()
                        .setDanmakuSource(in)
                        .setTitle(qFileInfo.get_fileName())
                        .start();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                mPlayerView.init()
                        .alwaysFullScreen()
                        .enableOrientation()
                        .setVideoPath(VIDEO_URL)
                        .enableDanmaku()
                        .setDanmakuSource(getResources().openRawResource(R.raw.bili))
                        .setTitle(qFileInfo.get_fileName())
                        .start();
            }
        }else {
            mPlayerView.init()
                    .alwaysFullScreen()
                    .enableOrientation()
                    .setVideoPath(VIDEO_URL)
                    .enableDanmaku()
                    .setDanmakuSource(getResources().openRawResource(R.raw.bili))
                    .setTitle(qFileInfo.get_fileName())
                    .start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
