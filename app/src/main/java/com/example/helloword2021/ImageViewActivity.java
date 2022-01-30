package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView mv_net_view;
    private String img_src;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        img_src = "https://gitee.com/song_zhi_shu/my-image-host/raw/master/img/image-20220129203043346.png";

        mv_net_view = (ImageView) findViewById(R.id.imgview_net_view);
        Glide.with(this).load(img_src).into(mv_net_view);
        mv_net_view.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
