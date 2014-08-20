package com.txtr.android.flh.lib;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public abstract class FacebookLoginFragment extends Fragment {
    protected UiLifecycleHelper mUiHelper;


    public FacebookLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiHelper = new UiLifecycleHelper(getActivity(), mStatusCallback);
        mUiHelper.onCreate(savedInstanceState);
    }

    protected Session.StatusCallback mStatusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    protected void onSessionStateChange(Session session, SessionState sessionState, Exception exception) {
        if (session.isOpened()) {
            onSessionOpen(session, sessionState, exception);
        } else if (session.isClosed()) {
            onSessionClose(session, sessionState, exception);
        } else {
            onSessionEvent(session, sessionState, exception);
        }
    }

    public abstract void onSessionOpen(Session session, SessionState sessionState, Exception exception);
    public abstract void onSessionClose(Session session, SessionState sessionState, Exception exception);
    public abstract void onSessionEvent(Session session, SessionState sessionState, Exception exception);
    public abstract void onSessionNullResume();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        } else {
            onSessionNullResume();
        }
        mUiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiHelper.onDestroy();
    }

    public Session login() {
        return new FacebookHelper(getActivity(), this, mStatusCallback).fragmentLogin("public_profile", "email");
    }

    public void logout() {
        new FacebookHelper(getActivity()).fragmentLogout();
    }
}
