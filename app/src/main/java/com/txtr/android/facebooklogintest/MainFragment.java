package com.txtr.android.facebooklogintest;

import android.os.Bundle;
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
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.txtr.android.flh.lib.FacebookLoginFragment;


public class MainFragment extends FacebookLoginFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
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



    @Override
    public void onSessionOpen(Session session, SessionState sessionState, Exception exception) {
        Request.newMeRequest(session, graphUserCallback).executeAsync();
    }

    @Override
    public void onSessionClose(Session session, SessionState sessionState, Exception exception) {
        updateUi(false);
    }

    @Override
    public void onSessionEvent(Session session, SessionState sessionState, Exception exception) {

    }

    @Override
    public void onSessionNullResume() {
        updateUi(false);
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

    private void loginWithFacebook() {
        login();
        updateUi(true);
    }

    private void logoutFromFacebook() {
        logout();
        updateUi(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.checkbox_consent && mFacebookConsentLayout.getVisibility() == View.VISIBLE) {
            mFacebookButton.setEnabled(isChecked);
        }
    }
}
