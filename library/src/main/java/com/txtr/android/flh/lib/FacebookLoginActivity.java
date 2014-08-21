/**

 The MIT License (MIT)

 Copyright (c) 2014 'txtr GmbH

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 */
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
    protected FacebookHelper mFacebookHelper;

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
        mFacebookHelper = FacebookHelper.getInstance();
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

    /**
     * This method will perform the Facebook login
     * @param profiles is a variable number of permissions (or profiles) you can ask Facebook.
     * @return the {@link com.facebook.Session} object related to this login
     * @throws {@link com.txtr.android.flh.lib.FacebookHelperException} in case one of the parameters is null.
     */
    public Session login(String... profiles) throws FacebookHelperException {
        return mFacebookHelper.login(this, mStatusCallback, profiles);
    }

    /**
     * This method will perform the Facebook logout
     * @throws {@link com.txtr.android.flh.lib.FacebookHelperException} in case the {@link android.app.Activity} is null.
     */
    public void logout() throws FacebookHelperException {
        mFacebookHelper.logout(this);
    }

}
