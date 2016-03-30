package net.publisher.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKApiModel;

import net.publisher.R;
import net.publisher.Utils;
import net.publisher.api.method.AbsRequestMethod;
import net.publisher.api.method.RequestMethod;
import net.publisher.tool.BaseAdapter;
import net.publisher.tool.EndlessScroll;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Recycler fragment.
 * A - activity owner.
 * M - model to fetch.
 * Created by Mayboroda on 9/25/15.
 */
public abstract class RecyclerFragment<A extends AppCompatActivity, M extends VKApiModel>
        extends BaseFragment<A>
        implements AbsRequestMethod.OnFetchUpdate<M>,
        SwipeRefreshLayout.OnRefreshListener{

    public static final int ERROR_VIEW      = 0;
    public static final int CONTENT_VIEW    = 1;

    @Bind(R.id.content)   ViewAnimator content;
    @Bind(R.id.refresher) SwipeRefreshLayout refresh;
    @Bind(R.id.recycler)  RecyclerView recycler;
    @Bind(R.id.error)     TextView message;

    private RequestMethod<M> requestMethod;
    private BaseAdapter<M> adapter;
    private EndlessScroll endlessScroll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMethod = createRequestMethod(getOwner(), this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reset();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);
        refresh.setColorSchemeResources(R.color.color_primary);
        refresh.setOnRefreshListener(this);
        adapter = createAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                content.setDisplayedChild(
                        adapter.getItemCount() == 0 ? ERROR_VIEW : CONTENT_VIEW);
                setRefresh(false);
            }
        });
        LinearLayoutManager layoutManager = createLayoutManager(getOwner());
        endlessScroll = new EndlessScroll(layoutManager) {
            @Override
            public void onLoad(int loadingTimes) { requestMethod.fetch(); }
        };
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(false);
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(endlessScroll);
        message.setTypeface(Utils.typeface(getOwner(), Utils.ROBOTO_LIGHT));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        content.setDisplayedChild(CONTENT_VIEW);
        setRefresh(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        content = null;
        refresh = null;
        recycler = null;
        message = null;
    }

    private void setRefresh(final boolean flag) {
        if (refresh == null) { return; }
        refresh.post(new Runnable() {
            @Override
            public void run() {
                if (refresh == null) { return; }
                refresh.setRefreshing(flag);
            }
        });
    }

    private void reset() {
        if (endlessScroll != null) {
            endlessScroll.resetTotal();
        }
        if (adapter != null) {
            adapter.clear();
        }
        if (requestMethod != null) {
            requestMethod.cancel();
            requestMethod.reset();
            requestMethod.fetch();
        }
    }

    @Override
    public void onRefresh() {
        if (requestMethod != null) {
            requestMethod.cancel();
        }
        reset();
    }

    @Override
    public void onDestroy() {
        if (requestMethod != null) {
            requestMethod.cancel();
            requestMethod = null;
        }
        super.onDestroy();
    }

    @Override
    public void onError(VKError error) {
        if (!isVisible()) { return; }
        setRefresh(false);
        if (error.errorCode == 0) {
            Utils.errorReport(error, getActivity());
            return;
        }
        content.setDisplayedChild(ERROR_VIEW);
        int errorId =  Utils.getErrorStringId(error.errorCode);
        message.setText(getString(errorId));
    }

    @Override
    public void onSuccess(M model) {
        adapter.setData(model);
        setRefresh(false);
    }

    @Override
    public void onFetch() {
        setRefresh(true);
    }

    public abstract RequestMethod<M> createRequestMethod(Context context,
                                                         AbsRequestMethod.OnFetchUpdate<M> onFetchUpdate);

    public abstract BaseAdapter<M> createAdapter();

    public LinearLayoutManager createLayoutManager(Context context) {
        return new LinearLayoutManager(context) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) { return 600; }
        };
    }
}
