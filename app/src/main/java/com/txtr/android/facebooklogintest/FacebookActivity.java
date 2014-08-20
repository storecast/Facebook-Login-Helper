package com.txtr.android.facebooklogintest;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.txtr.android.flh.lib.FacebookLoginActivity;



public class FacebookActivity extends FacebookLoginActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private RelativeLayout mFacebookDataLayout;
    private RelativeLayout mFacebookConsentLayout;
    private ProfilePictureView mFacebookProfilePicture;
    private TextView mFacebookRealNameTextView;
    private TextView mFacebookMailAddressTextView;
    private Button mFacebookButton;
    private CheckBox mFacebookConsentCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mFacebookDataLayout = (RelativeLayout) findViewById(R.id.profileView);
        mFacebookConsentLayout = (RelativeLayout) findViewById(R.id.consentLayout);
        mFacebookProfilePicture = (ProfilePictureView) findViewById(R.id.facebook_profile_pic);
        mFacebookRealNameTextView = (TextView) findViewById(R.id.txt_facebook_real_name);
        mFacebookMailAddressTextView = (TextView) findViewById(R.id.txt_facebook_mail_address);
        mFacebookConsentCheckbox = (CheckBox) findViewById(R.id.checkbox_consent);
        mFacebookConsentCheckbox.setOnCheckedChangeListener(this);
        mFacebookButton = (Button) findViewById(R.id.facebook_button);
        mFacebookButton.setOnClickListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.facebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        login("public_profile", "email");
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

    @Override
    public void onSessionOpen(Session session, SessionState sessionState, Exception exception) {
        Request.newMeRequest(session, graphUserCallback).executeAsync();
    }

    @Override
    public void onSessionClose(Session session, SessionState sessionState, Exception exception) {

    }

    @Override
    public void onSessionEvent(Session session, SessionState sessionState, Exception exception) {

    }

    @Override
    public void onSessionNullResume() {
        updateUi(false);
    }
}
