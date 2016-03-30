package net.publisher.tool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import net.publisher.R;
import net.publisher.api.Feed;
import net.publisher.api.Post;
import net.publisher.view.PostView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter for posts recycler view.
 * Created by Mayboroda on 6/24/15.
 */
public class PostsAdapter extends BaseAdapter<Feed> {

    /** Post card view holder. */
    public static class PostsHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.post_view) PostView postView;

        public PostsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Post post, VKList<VKApiCommunity> groups, VKList<VKApiUser> users) {
            postView.bind(post, groups, users);
        }
    }

    private VKList<Post>            posts   = new VKList<>();
    private VKList<VKApiCommunity>  groups  = new VKList<>();
    private VKList<VKApiUser>       users   = new VKList<>();

    private boolean clear = false;

    public PostsAdapter() {
        /* empty */
    }

    public void setData(Feed feed) {
        if (clear) {
            clear = false;
            posts.clear();
            groups.clear();
            users.clear();
        }
        posts.addAll(feed.getItems());
        groups.addAll(feed.getGroups());
        users.addAll(feed.getProfiles());
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        clear = true;
    }

    @Override
    public PostsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);
        return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Post post = posts.get(position);
        PostsHolder postsHolder = (PostsHolder) holder;
        if (post != null) {
            postsHolder.bind(post, groups, users);
        }
    }


    @Override
    public int getItemCount() { return posts.size(); }

}
