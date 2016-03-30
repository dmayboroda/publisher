package net.publisher.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vk.sdk.api.model.VKApiCommunity;

import net.publisher.MainActivity;
import net.publisher.R;
import net.publisher.WallActivity;
import net.publisher.api.Groups;
import net.publisher.api.method.AbsRequestMethod;
import net.publisher.api.method.GroupsRequestMethod;
import net.publisher.api.method.RequestMethod;
import net.publisher.tool.BaseAdapter;
import net.publisher.tool.GroupsAdapter;

/**
 * Groups representation fragment.
 * Created by Mayboroda on 9/23/15.
 */
public class GroupsFragment extends RecyclerFragment<MainActivity, Groups> implements GroupsAdapter.OnGroupClick {

    public static final String TAG = GroupsFragment.class.getSimpleName();

    public static GroupsFragment getInstance(String userId) {
        GroupsFragment groupsFragment = new GroupsFragment();
        groupsFragment.setParams(userId);
        return groupsFragment;
    }

    public final static int SPANS_COUNT = 2;

    private String userId;

    public void setParams(String userId) {
        this.userId = userId;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getOwner().setTitle(R.string.groups_title);
        getOwner().setToolbarVisible(true);
        getOwner().disableDrawer(false);
    }

    @Override
    public RequestMethod<Groups> createRequestMethod(Context context, AbsRequestMethod.OnFetchUpdate<Groups> onFetchUpdate) {
        return new GroupsRequestMethod(context, onFetchUpdate, userId);
    }

    @Override
    public BaseAdapter<Groups> createAdapter() { return new GroupsAdapter(this); }

    @Override
    public LinearLayoutManager createLayoutManager(Context context) {
        return new GridLayoutManager(context, SPANS_COUNT) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) { return 600; }
        };
    }

    @Override
    public void onGroupClick(VKApiCommunity community) {
        int id = community.id > 0 ? community.id * -1 : community.id;
        Intent intent = new Intent(getOwner(), WallActivity.class);
        intent.putExtra(WallFragment.GROUP_NAME, community.name);
        intent.putExtra(WallFragment.GROUP_ID, id);
        getOwner().startActivity(intent);
    }
}
