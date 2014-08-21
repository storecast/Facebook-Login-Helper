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
import android.support.v4.app.Fragment;

import com.facebook.Session;

import java.util.Arrays;


public class FacebookHelper {
    protected static final FacebookHelper INSTANCE = new FacebookHelper();

    protected FacebookHelper() {

    }

    public static FacebookHelper getInstance() {
        return INSTANCE;
    }

    protected void checkFragment(Fragment fragment) throws FacebookHelperException {
        if (fragment == null) {
            throw new FacebookHelperException(FacebookHelperException.Code.FRAGMENT_IS_NULL);
        }
    }

    protected void checkActivity(Activity activity) throws FacebookHelperException {
        if (activity == null) {
            throw new FacebookHelperException(FacebookHelperException.Code.ACTIVITY_IS_NULL);
        }
    }

    protected void checkCallback(Session.StatusCallback callback) throws FacebookHelperException {
        if (callback == null) {
            throw new FacebookHelperException(FacebookHelperException.Code.CALLBACK_IS_NULL);
        }
    }

    public Session login(Fragment fragment, Activity activity, Session.StatusCallback callback, String... permissions) throws FacebookHelperException {
        checkFragment(fragment);
        checkActivity(activity);
        checkCallback(callback);

        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(fragment)
                    .setPermissions(Arrays.asList(permissions))
                    .setCallback(callback));
        } else {
                session = Session.openActiveSession(activity, fragment, true, callback);
        }
        return session;
    }

    public Session login(Activity activity, Session.StatusCallback callback, String... permissions) throws FacebookHelperException {
        checkActivity(activity);
        checkCallback(callback);

        Session session = Session.getActiveSession();
            if (!session.isOpened() && !session.isClosed()) {
                session.openForRead(new Session.OpenRequest(activity)
                        .setPermissions(Arrays.asList(permissions))
                        .setCallback(callback));
            } else {
                session = Session.openActiveSession(activity, true, callback);
            }
        return session;
    }

    public void logout(Activity activity) throws FacebookHelperException {
        checkActivity(activity);
        Session session = Session.getActiveSession();

        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
                session = new Session(activity);
                Session.setActiveSession(session);
                session.closeAndClearTokenInformation();
        }
    }

}