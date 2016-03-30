package net.publisher.tool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import net.publisher.R;
import net.publisher.api.Post;
import net.publisher.api.Wall;
import net.publisher.view.PostView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Wall adapter.
 * Created by Mayboroda on 10/15/15.
 */
public class WallAdapter extends BaseAdapter<Wall>{

    private VKList<VKApiPost>       posts   = new VKList<>();
    private VKList<VKApiCommunity>  groups  = new VKList<>();
    private VKList<VKApiUser>       users   = new VKList<>();

    private boolean clear = false;

    /** Post card view holder. */
    public static class WallPostsHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.post_view) PostView postView;

        public WallPostsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(VKApiPost post, VKList<VKApiCommunity> groups, VKList<VKApiUser> users) {
            Post toPost = new Post();
            toPost.dateAbout        = post.date;
            toPost.sourceId         = post.from_id;
            toPost.textAbout        = post.text;
            toPost.attachments      = post.attachments;
            toPost.likes_count      = post.likes_count;
            toPost.reposts_count    = post.reposts_count;
            toPost.copy_history     = post.copy_history;
            postView.bind(toPost, groups, users);
        }
    }

    public WallAdapter() {
        /* nothing */
    }

    @Override
    public void setData(Wall model) {
        if (clear) {
            clear = false;
            posts.clear();
            groups.clear();
            users.clear();
        }
        posts.addAll(model.getItems());
        groups.addAll(model.getGroups());
        users.addAll(model.getProfiles());
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        clear = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);
        return new WallPostsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WallPostsHolder postsHolder = (WallPostsHolder) holder;
        VKApiPost post = posts.get(position);
        if (post != null) {
            postsHolder.bind(post, groups, users);
        }
    }

    @Override
    public int getItemCount() { return posts.size(); }
}
