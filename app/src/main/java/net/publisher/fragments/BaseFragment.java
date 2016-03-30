package net.publisher.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Base fragment with owner activity typo support.
 * Created by Mayboroda on 6/8/15.
 */
public class BaseFragment<T extends AppCompatActivity> extends Fragment{

    protected T getOwner() { return (T)getActivity(); }
}
