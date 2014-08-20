package com.txtr.android.facebooklogintest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import java.util.Arrays;


public class MainFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private UiLifecycleHelper uiHelper;
    private RelativeLayout mFacebookDataLayout;
    private RelativeLayout mFacebookConsentLayout;
    private ProfilePictureView mFacebookProfilePicture;
    private TextView mFacebookRealNameTextView;
    private TextView mFacebookMailAddressTextView;
    private Button mFacebookButton;
    private CheckBox mFacebookConsentCheckbox;

    public MainFragment() {
        // Required empty public constructor
    }

    /*
     uiHelper requires a few methods to be overridden in order to properly manage
     its lifecycle.
     Those methods are:
     - onCreate()
     - onActivityResult()
     - onSaveInstanceState()
     - onResume()
     - onPause()
     - onDestroy()
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), facebookStatusCallback);
        uiHelper.onCreate(savedInstanceState);
    }

    /*
    This is a simple callback we need when we instantiate the uiHelper.
    We used the private method as suggested by Facebook guidelines in order to be able to refresh our UI
    even when the callback itself it's not called.
     */
    private final Session.StatusCallback facebookStatusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState sessionState, Exception exception) {
        if (session.isOpened()) {
            Request.newMeRequest(session, graphUserCallback).executeAsync();
        } else if (session.isClosed()) {
            updateUi(false);
        }
    }

    private Request.GraphUserCallback graphUserCallback = new Request.GraphUserCallback() {
        @Override
        public void onCompleted(GraphUser user, Response response) {
            if (user != null) {
                mFacebookProfilePicture.setProfileId(user.getId());
                mFacebookRealNameTextView.setText(user.getName());
                /*
                Getting user email is not as straight forward as for the rest, but we can achieve the same
                results by directly accessing "email" key in the user structure.
                 */
                mFacebookMailAddressTextView.setText(user.asMap().get("email").toString());
                updateUi(true);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.main, container, false);

        mFacebookDataLayout = (RelativeLayout) parentView.findViewById(R.id.profileView);
        mFacebookConsentLayout = (RelativeLayout) parentView.findViewById(R.id.consentLayout);
        mFacebookProfilePicture = (ProfilePictureView) parentView.findViewById(R.id.facebook_profile_pic);
        mFacebookRealNameTextView = (TextView) parentView.findViewById(R.id.txt_facebook_real_name);
        mFacebookMailAddressTextView = (TextView) parentView.findViewById(R.id.txt_facebook_mail_address);
        mFacebookConsentCheckbox = (CheckBox) parentView.findViewById(R.id.checkbox_consent);
        mFacebookConsentCheckbox.setOnCheckedChangeListener(this);
        mFacebookButton = (Button) parentView.findViewById(R.id.facebook_button);
        mFacebookButton.setOnClickListener(this);
        return parentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //this code will resume the session when the app is launched from somewhere else than from the launcher
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        } else {
            updateUi(false);
        }
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void updateUi(boolean isUserLoggedIn) {
        if (isUserLoggedIn) {
            mFacebookButton.setText(R.string.facebook_button_logout);
            mFacebookDataLayout.setVisibility(View.VISIBLE);
            mFacebookConsentLayout.setVisibility(View.GONE);
            mFacebookButton.setEnabled(true);
        } else {
            mFacebookButton.setText(R.string.facebook_button_login);
            mFacebookDataLayout.setVisibility(View.GONE);
            mFacebookConsentLayout.setVisibility(View.VISIBLE);
            mFacebookConsentCheckbox.setChecked(false);
            mFacebookButton.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.facebook_button) {
            if (mFacebookDataLayout.getVisibility() == View.GONE) {
                loginWithFacebook();
            } else {
                logoutFromFacebook();
            }
        }
    }

    /*
    This method will begin Facebook login.
    For the purpose of this example, we needed the permissions for
    public_profile and email.
    If you need to store the Token, it will be enough to call
    session.getAccessToken()
     */
    private void loginWithFacebook() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setPermissions(Arrays.asList("public_profile", "email"))
                    .setCallback(facebookStatusCallback));
        } else {
            Session.openActiveSession(getActivity(), this, true, facebookStatusCallback);
        }
        updateUi(true);
    }

    /*
    Here we programmatically request a logout from the social network.
    Thanks to StackOverflow post: http://goo.gl/LMer41
     */
    private void logoutFromFacebook() {
        Session session = Session.getActiveSession();

        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
            session = new Session(getActivity());
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }
        updateUi(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.checkbox_consent && mFacebookConsentLayout.getVisibility() == View.VISIBLE) {
            mFacebookButton.setEnabled(isChecked);
        }
    }
}
