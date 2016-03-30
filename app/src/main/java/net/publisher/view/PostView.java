package net.publisher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiVideo;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKList;

import net.publisher.PostActivity;
import net.publisher.R;
import net.publisher.Utils;
import net.publisher.api.Post;
import net.publisher.tool.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Post item view.
 * Created by Mayboroda on 9/28/15.
 */
public class PostView extends RelativeLayout {

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM HH:mm");
    private Transformation   CIRCLE      = new CircleTransform();

    @Bind(R.id.post_avatar)   ImageView avatar;
    @Bind(R.id.post_likes)    TextView likes;
    @Bind(R.id.post_reposts)  TextView reposts;
    @Bind(R.id.post_author)   TextView author;
    @Bind(R.id.post_time)     TextView time;
    @Bind(R.id.post_text)     TextView text;
    @Bind(R.id.post_attach)   ViewGroup container;
    @Bind(R.id.post_history)  ViewGroup historyPost;
    @Bind(R.id.grab_button)   ViewAnimator grab;
    @Bind(R.id.grab_text)     TextView grabText;

    private Post post;

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        grabText.setTypeface(Utils.typeface(getContext(), Utils.ROBOTO_LIGHT));
    }

    public void bind(Post post, VKList<VKApiCommunity> groups, VKList<VKApiUser> users) {
        this.post = post;
        if (post.likes_count < 0) {
            likes.setVisibility(GONE);
        } else {
            likes.setText(Integer.toString(post.likes_count));
            likes.setVisibility(VISIBLE);
        }
        if (post.reposts_count < 0) {
            reposts.setVisibility(GONE);
        } else {
            reposts.setText(Integer.toString(post.reposts_count));
            reposts.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(post.textAbout)) {
            text.setVisibility(VISIBLE);
            text.setText(post.textAbout);
        } else {
            text.setVisibility(GONE);
        }
        if (post.dateAbout > 0) {
            time.setVisibility(VISIBLE);
            Date date = new Date(post.dateAbout);
            DATE_FORMAT.toLocalizedPattern();
            time.setText(DATE_FORMAT.format(date.getTime()));
        } else {
            time.setVisibility(GONE);
        }
        String name = null;
        String url = null;

        if (post.sourceId > 0 ){
            if (users.isEmpty()) { return; }
            VKApiUser user = users.getById(post.sourceId);
            if (user != null) {
                name = user.first_name + " " + user.last_name;
                url = TextUtils.isEmpty(user.photo_100) ?
                        user.photo_50 : user.photo_100;
            }
        } else {
            if (groups.isEmpty()) { return; }
            VKApiCommunity community = groups.getById(post.sourceId * -1);
            if (community != null) {
                name = community.name;
                url = TextUtils.isEmpty(community.photo_100) ?
                        community.photo_50 : community.photo_100;
            }
        }
        if (!TextUtils.isEmpty(name)) {
            author.setVisibility(VISIBLE);
            author.setText(name);
        } else {
            author.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(url)) {
            avatar.setVisibility(VISIBLE);
            Picasso.with(getContext())
                    .load(url)
                    .transform(CIRCLE)
                    .into(avatar);
        } else {
            avatar.setVisibility(GONE);
        }

        setAttachment(post.attachments);

        VKList<VKApiPost> reposted = post.copy_history;
        if (reposted != null && !reposted.isEmpty()) {
            historyPost.removeAllViews();
            PostView postView = (PostView) LayoutInflater.from(getContext())
                    .inflate(R.layout.post_item, this, false);
            postView.setGrabVisible(false);
            Post repost = new Post();
            VKApiPost history = reposted.get(0);
            repost.sourceId         = history.from_id;
            repost.dateAbout        = history.date;
            repost.textAbout        = history.text;
            repost.attachments      = history.attachments;
            repost.likes_count      = -1;
            repost.reposts_count    = -1;
            postView.bind(repost, groups, users);
            historyPost.addView(postView);
            historyPost.setVisibility(VISIBLE);
        } else {
            historyPost.setVisibility(GONE);
        }
    }

    private void setAttachment(VKAttachments attachments) {
        if (attachments != null && !attachments.isEmpty()) {
            container.removeAllViews();
            container.setVisibility(VISIBLE);
            VKList<VKApiPhoto> photos = new VKList<>();
            VKList<VKApiVideo> videos = new VKList<>();
            for (VKAttachments.VKApiAttachment attachment : attachments) {
                if (attachment.getType().equals(VKAttachments.TYPE_LINK)      ||
                        attachment.getType().equals(VKAttachments.TYPE_DOC)   ||
                        attachment.getType().equals(VKAttachments.TYPE_POLL)  ||
                        attachment.getType().equals(VKAttachments.TYPE_AUDIO) ||
                        attachment.getType().equals(VKAttachments.TYPE_WIKI_PAGE)) {
                    addSimpleAttach(attachment);
                } else if (attachment.getType().equals(VKAttachments.TYPE_PHOTO)) {
                    photos.add((VKApiPhoto)attachment);
                } else if (attachment.getType().equals(VKAttachments.TYPE_VIDEO)) {
                    videos.add((VKApiVideo)attachment);
                } else if (attachment.getType().equals(VKAttachments.TYPE_ALBUM)) {
                    addAlbumAttach((VKApiPhotoAlbum)attachment);
                }
            }

            if (!photos.isEmpty()) {
                addPhotoAttach(photos);
            }
            if (!videos.isEmpty()) {
                addVideoAttach(videos);
            }
        } else {
            container.setVisibility(GONE);
        }
    }

    private void addAlbumAttach(VKApiPhotoAlbum album) {
        VideoAttach albumAttach = (VideoAttach)LayoutInflater.from(getContext())
                .inflate(R.layout.video_attach, this, false);
        albumAttach.bind(album);
        container.addView(albumAttach);
    }

    private void addVideoAttach(VKList<VKApiVideo> videos) {
        VideoAttach videoAttach = (VideoAttach)LayoutInflater.from(getContext())
                .inflate(R.layout.video_attach, this, false);
        videoAttach.bind(videos);
        container.addView(videoAttach);
    }

    private void addPhotoAttach(VKList<VKApiPhoto> photos) {
        PhotoAttach photoAttach = (PhotoAttach)LayoutInflater.from(getContext())
                .inflate(R.layout.photo_attach, this, false);
        photoAttach.bind(photos);
        container.addView(photoAttach);
    }

    private void addSimpleAttach(VKAttachments.VKApiAttachment attachment) {
        SimpleAttach attach = (SimpleAttach)LayoutInflater.from(getContext())
                .inflate(R.layout.simple_attach, this, false);
        attach.bind(attachment);
        container.addView(attach);
    }

    public void setGrabVisible(boolean flag) {
        grab.setVisibility(flag ? VISIBLE : GONE);
    }

    @OnClick(R.id.grab_button)
    public void onPostGrab(){
        if (post != null) {
            Intent intent = new Intent(getContext(), PostActivity.class);
            intent.putExtra(PostActivity.POST_EXTRA, post);
            getContext().startActivity(intent);
        }
    }

}
