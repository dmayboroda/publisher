package net.publisher;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ViewAnimator;

import net.publisher.api.Post;
import net.publisher.tool.EditAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Post activity.
 * Created by Mayboroda on 10/15/15.
 */
public class PostActivity extends AppCompatActivity {

    public static final String POST_EXTRA = "parcelable_extra";

    public static final int ERROR_VIEW      = 0;
    public static final int CONTENT_VIEW    = 1;

    @Bind(R.id.toolbar)         Toolbar             toolbar;
    @Bind(R.id.content)         ViewAnimator        content;
    @Bind(R.id.refresher)       SwipeRefreshLayout  refresh;
    @Bind(R.id.recycler)        RecyclerView        recycler;
    @Bind(R.id.error)           TextView            message;
    @Bind(R.id.publish_button)  ViewAnimator        publish;

    private EditAdapter editAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int color = getResources().getColor(R.color.color_primary_dark);
            getWindow().setStatusBarColor(color);
        }
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.post_editor);
        }
        refresh.setColorSchemeResources(R.color.color_primary);
        message.setTypeface(Utils.typeface(this, Utils.ROBOTO_LIGHT));
        editAdapter = new EditAdapter();

        if (getIntent() != null && getIntent().hasExtra(POST_EXTRA)) {
            Post post = getIntent().getParcelableExtra(POST_EXTRA);
            editAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    content.setDisplayedChild(
                            editAdapter.getItemCount() == 0
                                    ? ERROR_VIEW
                                    : CONTENT_VIEW);
                    publish.setVisibility(
                            editAdapter.getItemCount() == 0
                                    ? View.GONE
                                    : View.VISIBLE);
                    setRefresh(false);
                }
            });
            LinearLayoutManager layoutManager = createLayoutManager(this);
            recycler.setLayoutManager(layoutManager);
            recycler.setHasFixedSize(true);
            editAdapter.setupPost(post);
            recycler.setAdapter(editAdapter);
        }
        publish.setVisibility(View.GONE);
        setRefresh(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        content = null;
        refresh = null;
        recycler = null;
        message = null;
        super.onDestroy();
    }

    private void setRefresh(final boolean flag) {
        if (refresh == null) { return; }
        refresh.post(new Runnable() {
            @Override
            public void run() {
                if (refresh == null) {
                    return;
                }
                refresh.setRefreshing(flag);
            }
        });
    }

    public LinearLayoutManager createLayoutManager(Context context) {
        return new LinearLayoutManager(context) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) { return 600; }
        };
    }
}
