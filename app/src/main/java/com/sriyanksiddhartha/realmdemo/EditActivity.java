package com.sriyanksiddhartha.realmdemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.sriyanksiddhartha.realmdemo.model.SocialAccount;
import com.sriyanksiddhartha.realmdemo.model.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditActivity extends AppCompatActivity {

	private EditText etPersonName, etAge, etSocialAccountName, etStatus;
	private Realm myRealm;

	Bundle bundle;
	int position;
	User user;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		bundle = getIntent().getExtras();
		if (bundle != null)
			position = bundle.getInt("position");

		etPersonName 		= (EditText) findViewById(R.id.etPersonName);
		etAge 				= (EditText) findViewById(R.id.etAge);
		etSocialAccountName = (EditText) findViewById(R.id.etSocialAccount);
		etStatus 			= (EditText) findViewById(R.id.etStatus);

		myRealm = Realm.getDefaultInstance();
		RealmResults<User> userList = myRealm.where(User.class).findAll();
		user = userList.get(position);

		setupViews(user);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setupViews(User user) {

		etPersonName.setText(user.getName());
		etAge.setText(String.valueOf(user.getAge()));

		SocialAccount socialAccount = user.getSocialAccount();
		if (socialAccount != null) {
			etSocialAccountName.setText(socialAccount.getName());
			etStatus.setText(socialAccount.getStatus());
		}
	}

	public void update(View view) {

		myRealm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				SocialAccount socialAccount = user.getSocialAccount();
				if (socialAccount != null) {
					socialAccount.setName(etSocialAccountName.getText().toString());
					socialAccount.setStatus(etStatus.getText().toString());
				}
				user.setName(etPersonName.getText().toString());
				user.setAge(Integer.valueOf(etAge.getText().toString()));
				user.setSocialAccount(socialAccount);
			}
		});
	}

	public void cancel(View view) {
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		myRealm.close();
	}
}
