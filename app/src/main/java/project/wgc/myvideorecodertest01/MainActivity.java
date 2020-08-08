package project.wgc.myvideorecodertest01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static int REQUST_VIDEO = 1;
    private Button btn;
    private Button btn_start;
    private FrameLayout fl;
    private CustomVideoView video;
    private ImageView iv;
    private String sdCard;
    private String videoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        btn = ((Button) findViewById(R.id.btn_recoder));
        btn_start = ((Button) findViewById(R.id.btn_start));
        fl = ((FrameLayout) findViewById(R.id.fl_video));
        video = ((CustomVideoView) findViewById(R.id.video));
        iv = ((ImageView) findViewById(R.id.iv));
        sdCard = Environment.getExternalStorageDirectory().getPath();
        String currenTimeMillis = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        videoPath = sdCard + "/" +  "0000.mp4";
        MediaController controller = new MediaController(this);
        video.setMediaController(controller);
        if (video.isPlaying()){
            iv.setVisibility(View.INVISIBLE);
        }
        video.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                Toast.makeText(MainActivity.this,"播放",Toast.LENGTH_SHORT).show();
                iv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPause() {
                Toast.makeText(MainActivity.this,"暂停",Toast.LENGTH_SHORT).show();
                iv.setVisibility(View.VISIBLE);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,videoPath);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(intent,REQUST_VIDEO);
            }
        });
    }
    private Bitmap getVideoBitmap(String videoPath){
        MediaMetadataRetriever retriever = null;
        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoPath);
            Bitmap bitmap = retriever.getFrameAtTime();
            return bitmap;
        }finally {
            retriever.release();
        }
    }
    private Bitmap getVideoBitmap2(Uri uri){
        MediaMetadataRetriever retriever = null;
        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this,uri);
            Bitmap bitmap = retriever.getFrameAtTime();
            return bitmap;
        }finally {
            retriever.release();
        }
    }
    public void start(View view){
        iv.setVisibility(View.INVISIBLE);
        video.start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUST_VIDEO){
                fl.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
                video.setVideoURI(uri);
//                Bitmap bitmap = getVideoBitmap(videoPath);
                Bitmap bitmap = getVideoBitmap2(uri);
                iv.setImageBitmap(bitmap);
            }
        }
    }
}
