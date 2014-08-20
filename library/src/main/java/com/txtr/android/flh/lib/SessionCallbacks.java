package com.txtr.android.flh.lib;

import com.facebook.Session;
import com.facebook.SessionState;

/**
 * Created by roberto on 20/08/14.
 */
public interface SessionCallbacks {
    public void onSessionOpen(Session session, SessionState sessionState, Exception exception);
    public void onSessionClose(Session session, SessionState sessionState, Exception exception);
    public void onSessionEvent(Session session, SessionState sessionState, Exception exception);
    public void onSessionNullResume();
}
