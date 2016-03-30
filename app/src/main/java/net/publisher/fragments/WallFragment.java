package net.publisher.fragments;

import android.content.Context;
import android.os.Bundle;

import net.publisher.WallActivity;
import net.publisher.api.Wall;
import net.publisher.api.method.AbsRequestMethod;
import net.publisher.api.method.RequestMethod;
import net.publisher.api.method.WallRequestMethod;
import net.publisher.tool.BaseAdapter;
import net.publisher.tool.WallAdapter;

/**
 * Wall represented fragment.
 * Created by Mayboroda on 10/6/15.
 */
public class WallFragment extends RecyclerFragment<WallActivity, Wall> {

    public static final String TAG = WallFragment.class.getSimpleName();

    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_ID   = "group_id";

    public static WallFragment getInstance(int groupId) {
        WallFragment fragment = new WallFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public RequestMethod<Wall> createRequestMethod(Context context, AbsRequestMethod.OnFetchUpdate<Wall> onFetchUpdate) {
        Bundle args = getArguments();
        int groupId = 0;
        if (args != null && args.containsKey(GROUP_ID)) {
            groupId = args.getInt(GROUP_ID);
        }
        return new WallRequestMethod(context, onFetchUpdate, groupId);
    }

    @Override
    public BaseAdapter<Wall> createAdapter() { return new WallAdapter(); }

}
