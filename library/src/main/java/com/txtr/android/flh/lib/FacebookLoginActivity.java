package com.txtr.android.flh.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;


public abstract class FacebookLoginActivity extends Activity implements SessionCallbacks{
    protected UiLifecycleHelper mUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiHelper = new UiLifecycleHelper(this, mStatusCallback);
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

    public Session login(String... profiles) {
        return new FacebookHelper(this, mStatusCallback).activityLogin(profiles);
    }

    public void logout() {
        new FacebookHelper(this).logout();
    }

}
