package com.example.movieapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.movieapp.R;

public class WatchActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        videoView = findViewById(R.id.videoView);

        videoPlay();
    }

    private void videoPlay() {
        // Đường dẫn đúng tới tài nguyên video
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.kong_skull_island;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Thêm MediaController để điều khiển video (phát, tạm dừng, tua)
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // Bắt đầu phát video
        videoView.start();

        // Lắng nghe sự kiện khi video kết thúc
        videoView.setOnCompletionListener(mp -> {
            // Xử lý khi video phát xong
        });

        // Lắng nghe sự kiện khi xảy ra lỗi
        videoView.setOnErrorListener((mp, what, extra) -> {
            // Xử lý khi có lỗi xảy ra
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
