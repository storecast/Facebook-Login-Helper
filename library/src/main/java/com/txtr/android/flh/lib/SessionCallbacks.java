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

import com.facebook.Session;
import com.facebook.SessionState;

public interface SessionCallbacks {
    /**
     * This method is called every time a <b>Session Opening is completed</b>, including during the {@code onResume()} method
     * of your {@code Fragment} or {@code Activity} if your app is called from links.
     * <i>Note</i>: the number of times this method is invoked may vary depending on Facebook SDK behaviour
     */
    public void onSessionOpen(Session session, SessionState sessionState, Exception exception);

    /**
     * This method is called every time a <b>Session Closing is completed</b>, including during the {@code onResume()} method
     * of your {@code Fragment} or {@code Activity} if your app is called from links.
     * <i>Note</i>: the number of times this method is invoked may vary depending on Facebook SDK behaviour
     */
    public void onSessionClose(Session session, SessionState sessionState, Exception exception);

    /**
     * This method is called every time a <b>Session Change is completed</b> but the Session is not yet opened or closed,
     * including during the {@code onResume()} method of your {@code Fragment} or {@code Activity} if your app is called from links.
     * <i>Note</i>: the number of times this method is invoked may vary depending on Facebook SDK behaviour
     */
    public void onSessionEvent(Session session, SessionState sessionState, Exception exception);

    /**
     * This method is called during the {@code onResume()} phase of your {@code Fragment} or {@code Activity}
     * if your app is called from links and the Facebook Session is null.
     * <i>Note</i>: one case in which this method can be invoked is when your app is launched from a link
     * but the user did not logged in for the first time yet.
     */
    public void onSessionNullResume();
}
