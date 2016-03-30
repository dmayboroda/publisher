package net.publisher.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.publisher.MainActivity;
import net.publisher.R;
import net.publisher.api.Feed;
import net.publisher.api.method.AbsRequestMethod;
import net.publisher.api.method.FeedRequestMethod;
import net.publisher.api.method.RequestMethod;
import net.publisher.tool.BaseAdapter;
import net.publisher.tool.PostsAdapter;

/**
 * News feed fragment.
 * Created by Mayboroda on 6/8/15.
 */
public class FeedFragment extends RecyclerFragment<MainActivity, Feed> {

    public static final String TAG = FeedFragment.class.getSimpleName();

    public static FeedFragment getInstance() {
        FeedFragment feedFragment = new FeedFragment();
        return feedFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getOwner().setTitle(R.string.news_title);
        getOwner().setToolbarVisible(true);
        getOwner().disableDrawer(false);
    }

    @Override
    public RequestMethod<Feed> createRequestMethod(Context context, AbsRequestMethod.OnFetchUpdate<Feed> onFetchUpdate) {
        return new FeedRequestMethod(context, onFetchUpdate);
    }

    @Override
    public BaseAdapter<Feed> createAdapter() { return new PostsAdapter(); }

}
