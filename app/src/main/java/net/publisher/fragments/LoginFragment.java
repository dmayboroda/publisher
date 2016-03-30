package net.publisher.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.publisher.MainActivity;
import net.publisher.R;
import net.publisher.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Login form will be shown when user firstly log in.
 * Also when user in log out state.
 * Created by Mayboroda on 6/7/15.
 */
public class LoginFragment extends BaseFragment<MainActivity> {

    public static final String TAG = LoginFragment.class.getSimpleName();

    public static LoginFragment getInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Bind(R.id.logo_text)  TextView logo;
    @Bind(R.id.login_text) TextView login;

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_view, container, false);
        ButterKnife.bind(this, view);
        login.setTypeface(Utils.typeface(getOwner(), Utils.ROBOTO_REGULAR));
        logo.setTypeface(Utils.typeface(getOwner(), Utils.ROBOTO_THIN));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getOwner().setToolbarVisible(false);
        getOwner().disableDrawer(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logo = null;
        login = null;
    }

    @OnClick(R.id.login_button)
    public void onLogin() {
        if (Utils.checkConnection(getOwner())) {
            getOwner().login();
        }
    }

}
