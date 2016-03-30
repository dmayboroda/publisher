package net.publisher.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKApiVideo;
import com.vk.sdk.api.model.VKList;

import net.publisher.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Video attach
 * Created by Mayboroda on 10/6/15.
 */
public class VideoAttach extends RelativeLayout implements View.OnClickListener {

    @Bind(R.id.video_count) TextView count;
    @Bind(R.id.video_prev)  ImageView prev;
    @Bind(R.id.video_name)  TextView name;
    @Bind(R.id.video_icon)  ImageView icon;

    private VKList<VKApiVideo> videos = new VKList<>();
    private VKApiPhotoAlbum album = new VKApiPhotoAlbum();

    public VideoAttach(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    public void bind(final VKList<VKApiVideo> videos) {
        this.videos = videos;
        icon.setVisibility(VISIBLE);
        if (videos.size() > 1) {
            count.setVisibility(VISIBLE);
            count.setText((videos.size() - 1) + "+");
        } else {
            count.setVisibility(GONE);
        }

        VKApiVideo video = videos.get(0);
        if (!TextUtils.isEmpty(video.title)) {
            name.setVisibility(VISIBLE);
            name.setText(video.title);
        } else {
            name.setVisibility(GONE);
        }

        String url = !TextUtils.isEmpty(video.photo_130) ?
                video.photo_320 : video.photo_130;
        if (TextUtils.isEmpty(url)) { return; }

        Picasso.with(getContext())
                .load(url)
                .into(prev);
    }

    private void browse(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getContext().startActivity(intent);
    }

    public void bind(VKApiPhotoAlbum album){
        icon.setVisibility(GONE);
        if (album.size > 0) {
            count.setVisibility(VISIBLE);
            count.setText(album.size + "");
        } else {
            count.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(album.title)) {
            name.setVisibility(VISIBLE);
            name.setText(album.title);
        } else {
            name.setVisibility(GONE);
        }

        String url = !TextUtils.isEmpty(album.thumb_src)
                ? album.thumb_src
                : VKApiPhotoAlbum.COVER_M;

        Picasso.with(getContext())
                .load(url)
                .into(prev);
    }

    @Override
    public void onClick(View v) {

    }
}
