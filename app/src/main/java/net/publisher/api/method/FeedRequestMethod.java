package net.publisher.api.method;

import android.content.Context;
import android.text.TextUtils;

import net.publisher.api.Feed;

import java.util.HashMap;
import java.util.Map;

/**
 * Feed request method.
 * Created by Mayboroda on 9/22/15.
 */
public class FeedRequestMethod extends AbsRequestMethod<Feed> {

    private static final String FEED            = "newsfeed.get";
    private static final String FILTER_FEED     = "post";
    private static final String START_FROM      = "start_from";
    private static final int NEWS_COUNT         = 20;

    private static final Map<String, Object> FEED_VK_PARAMS = new HashMap<>(3);

    static {
        FEED_VK_PARAMS.put("filters", FILTER_FEED);
        FEED_VK_PARAMS.put("count", NEWS_COUNT);
    }

    public FeedRequestMethod(Context context, OnFetchUpdate<Feed> listener) {
        super(context, listener);
        createRequest(FEED, FEED_VK_PARAMS, Feed.class);
    }

    @Override
    public void reset() {
        FEED_VK_PARAMS.remove(START_FROM);
        recreateRequest(FEED_VK_PARAMS);
    }

    @Override
    public void next(Feed model) {
        String next = model.getNext();
        if (!TextUtils.isEmpty(next)) {
            FEED_VK_PARAMS.put(START_FROM, next);
            recreateRequest(FEED_VK_PARAMS);
        } else {
            stop();
        }
    }
}
