package net.publisher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;

import net.publisher.api.User;
import net.publisher.fragments.BaseFragment;
import net.publisher.fragments.FeedFragment;
import net.publisher.fragments.GroupsFragment;
import net.publisher.fragments.LoginFragment;
import net.publisher.tool.CircleTransform;
import net.publisher.tool.GrayscaleTransform;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Main activity.
 * Holds login fragment and fragment for news feed.
 * Also will contain navigation drawer.
 */
public class MainActivity extends AppCompatActivity {

    /** Scope of permissions for vkontakte authorization. */
    public static final String[] SCOPE = new String[] {
            VKScope.WALL,
            VKScope.GROUPS,
            VKScope.FRIENDS
    };

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String PREF_NAME        = "FLAG_PREFS";

    private static final String USER            = "users.get";
    private static final String FIELDS_USER     = "first_name,last_name,photo_50,photo_100";

    private static final Map<String, Object> USERS_VK_PARAMS = new HashMap<>(2);

    static {
        USERS_VK_PARAMS.put("fields", FIELDS_USER);
    }

    @Bind(R.id.toolbar)         Toolbar toolbar;
    @Bind(R.id.name)            TextView name;
    @Bind(R.id.surname)         TextView surname;
    @Bind(R.id.header_bg)       ImageView background;
    @Bind(R.id.avatar)          ImageView avatar;
    @Bind(R.id.drawer)          DrawerLayout drawer;
    @Bind(R.id.navigation)      NavigationView navigation;

    private FeedFragment feedFragment;
    private LoginFragment loginFragment;

    private boolean isResumed = false;
    private boolean onResult  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        name.setTypeface(Utils.typeface(this, Utils.ROBOTO_MEDIUM));
        surname.setTypeface(Utils.typeface(this, Utils.ROBOTO_MEDIUM));
        feedFragment = FeedFragment.getInstance();
        loginFragment = LoginFragment.getInstance();
        setupNavigation();
        VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {

            @Override
            public void onResult(VKSdk.LoginState loginState) {
                if (!isResumed) { return; }
                switch (loginState) {
                    case LoggedOut:
                        showFragment(loginFragment, LoginFragment.TAG);
                        break;
                    case LoggedIn:
                        showFragment(feedFragment, FeedFragment.TAG);
                        request(VKAccessToken.currentToken());
                        setCheckedMenu(R.id.menu_news);
                        break;
                }
            }

            @Override
            public void onError(VKError vkError) {
                Utils.errorReport(vkError, feedFragment.getActivity());
            }
        });
    }


    private void setCheckedMenu(int id) {
        if (navigation != null) {
            navigation.getMenu().findItem(id).setChecked(true);
        }
    }

    private void setupNavigation() {
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                closeDrawer();
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.menu_news:
                        showFragment(feedFragment, FeedFragment.TAG);
                        break;
                    case R.id.menu_groups:
                        String userId = VKAccessToken.currentToken().userId;
                        showFragment(GroupsFragment.getInstance(userId), GroupsFragment.TAG);
                        break;
                    case R.id.menu_logout:
                        logout();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!Utils.checkConnection(this)) {
            AppCompatDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.error_network_settings))
                    .setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (!VKSdk.isLoggedIn()) {
            showFragment(loginFragment, LoginFragment.TAG);
        } else if (onResult){
            onResult = false;
            setFlag();
            launchFeed();
        } else if (!getFlag()) {
            setFlag();
            launchFeed();
        }
    }

    private void setFlag() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putBoolean(PREF_NAME, true);
        editor.apply();
    }

    private boolean getFlag(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        return preferences.contains(PREF_NAME);
    }

    private void launchFeed() {
        setCheckedMenu(R.id.menu_news);
        showFragment(feedFragment, FeedFragment.TAG);
        request(VKAccessToken.currentToken());
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        name = null;
        surname = null;
        drawer = null;
        feedFragment = null;
        loginFragment = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onResult = true;
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken vkAccessToken) { request(vkAccessToken); }

            @Override
            public void onError(VKError vkError) { Utils.errorReport(vkError, feedFragment.getActivity()); }
        };
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            onResult = false;
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void login() {
        VKSdk.login(this, SCOPE);
    }

    public void logout() {
        VKSdk.logout();
        if (!VKSdk.isLoggedIn()) {
            showFragment(loginFragment, LoginFragment.TAG);
        }
    }

    private void showFragment(BaseFragment fragment, String tag) {
        if (isVisible(fragment)) { return; }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment, tag)
                .commit();
    }

    private boolean isVisible(BaseFragment fragment) {
        return fragment != null && fragment.isVisible();
    }

    public void request(VKAccessToken token) {
        if (token != null && !TextUtils.isEmpty(token.userId)) {
            USERS_VK_PARAMS.put("user_ids", token.userId);
        }
        VKParameters usersParams = new VKParameters(USERS_VK_PARAMS);
        VKRequest usersRequest = new VKRequest(USER, usersParams);
        usersRequest.setModelClass(User.class);
        usersRequest.executeWithListener(new VkCallback(this));
    }

    private void setupUserView(VKApiUser user) {
        if (!TextUtils.isEmpty(user.first_name)) {
            name.setText(user.first_name);
        }

        if (!TextUtils.isEmpty(user.last_name)) {
            surname.setText(user.last_name);
        }

        if (!TextUtils.isEmpty(user.photo_100)) {
            Picasso.with(this)
                    .load(user.photo_100)
                    .transform(new CircleTransform())
                    .into(avatar);

            Picasso.with(this)
                    .load(user.photo_100)
                    .transform(new GrayscaleTransform())
                    .into(background);
        }
    }

    public void closeDrawer() {
        if (drawer != null) { drawer.closeDrawers(); }
    }

    public void setToolbarVisible(boolean isVisible) {
        if (getSupportActionBar() != null) {
            if (isVisible) {
                getSupportActionBar().show();
            } else {
                getSupportActionBar().hide();
            }
        }
    }

    public void setTitle(int id) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(id);
        }
    }

    public void disableDrawer(boolean disable) {
        if (drawer != null) {
            if (disable) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }
    }

    private class VkCallback extends VKRequest.VKRequestListener {

        private Context context;

        public VkCallback(Context context) {
            this.context = context;
        }

        @Override
        public void onComplete(VKResponse response) {
            if (response != null && response.parsedModel != null) {
                Object model = response.parsedModel;
                if (model instanceof User) {
                    User user = (User) model;
                    if (!user.getUsers().isEmpty()) {
                        setupUserView(user.getUsers().get(0));
                    }
                }
            }
        }

        @Override
        public void onError(VKError error) { Utils.errorReport(error, context); }
    }
}
