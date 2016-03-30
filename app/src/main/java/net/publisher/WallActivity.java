package net.publisher;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import net.publisher.fragments.WallFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Wall activity.
 * Created by Mayboroda on 10/6/15.
 */
public class WallActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)   Toolbar toolbar;

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
        setContentView(R.layout.activity_wall);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        if (intent != null) {
            int groupId = intent.getIntExtra(WallFragment.GROUP_ID, 0);
            String name = intent.getStringExtra(WallFragment.GROUP_NAME);
            getSupportActionBar().setTitle(name);
            WallFragment wallFragment = WallFragment.getInstance(groupId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, wallFragment, WallFragment.TAG)
                    .commit();
        }
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

}
