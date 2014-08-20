package com.txtr.android.flh.lib;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.facebook.Session;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Created by roberto on 20/08/14.
 */
public class FacebookHelper {
    private WeakReference<Context> mContext;
    private WeakReference<Activity> mActivity;
    private WeakReference<Session.StatusCallback> mStatusCallback;
    private WeakReference<Fragment> mSupportFragment;

    public FacebookHelper(Context context, Activity activity, Session.StatusCallback statusCallback) {
        setActivity(activity);
        setStatusCallback(statusCallback);
        setContext(context);
    }

    public FacebookHelper(Activity activity, Fragment fragment, Session.StatusCallback statusCallback) {
        setActivity(activity);
        setStatusCallback(statusCallback);
        setFragment(fragment);
    }

    public FacebookHelper(Activity activity, Session.StatusCallback statusCallback) {
        setActivity(activity);
        setStatusCallback(statusCallback);
    }

    public FacebookHelper(Activity activity) {
        setActivity(activity);
    }

    public FacebookHelper setFragment(Fragment hostFragment) {
        mSupportFragment = new WeakReference<Fragment>(hostFragment);
        return this;
    }

    public FacebookHelper setContext(Context context) {
        mContext = new WeakReference<Context>(context);
        return this;
    }

    public FacebookHelper setStatusCallback(Session.StatusCallback statusCallback) {
        mStatusCallback = new WeakReference<Session.StatusCallback>(statusCallback);
        return this;
    }

    public FacebookHelper setActivity(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
        return this;
    }

    public Session fragmentLogin(String... permissions) {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(mSupportFragment.get())
                    .setPermissions(Arrays.asList(permissions))
                    .setCallback(mStatusCallback.get()));
        } else {
            session = Session.openActiveSession(mActivity.get(), mSupportFragment.get(), true, mStatusCallback.get());
        }
        return session;
    }

    public void fragmentLogout() {
        Session session = Session.getActiveSession();

        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
            session = new Session(mActivity.get());
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }
    }

}
