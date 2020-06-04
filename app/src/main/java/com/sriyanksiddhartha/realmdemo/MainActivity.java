package com.sriyanksiddhartha.realmdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sriyanksiddhartha.realmdemo.model.SocialAccount;
import com.sriyanksiddhartha.realmdemo.model.User;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * 	Author: Sriyank Siddhartha
 *
 * 	Module 6: Exploring Miscellaneous Concepts
 *
 * 			"AFTER" Project
 * */
public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private EditText etPersonName, etAge, etSocialAccountName, etStatus;

	private Realm myRealm;
	private RealmAsyncTask realmAsyncTask;
	private RealmResults<User> userList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etPersonName 		= (EditText) findViewById(R.id.etPersonName);
		etAge 				= (EditText) findViewById(R.id.etAge);
		etSocialAccountName = (EditText) findViewById(R.id.etSocialAccount);
		etStatus 			= (EditText) findViewById(R.id.etStatus);

		myRealm = Realm.getDefaultInstance();

//		Realm myAnotherRealm = MyApplication.getAnotherRealm();

		Log.i(TAG, "Current Version: " + myRealm.getVersion());
	}

	public void addUserToRealm_Synchronously(View view) {

		final String id = UUID.randomUUID().toString();
		final String name 				= etPersonName.getText().toString();
		final int age 					= Integer.valueOf(etAge.getText().toString());
		final String socialAccountName 	= etSocialAccountName.getText().toString();
		final String status 			= etStatus.getText().toString();

//		try {
//			myRealm.beginTransaction();
//			myRealm.commitTransaction();
//		} catch (Exception e) {
//			myRealm.cancelTransaction();
//		}

		myRealm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				SocialAccount socialAccount = realm.createObject(SocialAccount.class);
				socialAccount.setName(socialAccountName);
				socialAccount.setStatus(status);

				User user = realm.createObject(User.class, id);
				user.setName(name);
				user.setAge(age);
				user.setSocialAccount(socialAccount);
			}
		});
	} // Add data to Realm using Main UI Thread. Be Careful: As it may BLOCK the UI.

	public void addUserToRealm_ASynchronously(View view) {

		final String id = UUID.randomUUID().toString();
		final String name 				= etPersonName.getText().toString();
		final int age 					= Integer.valueOf(etAge.getText().toString());
		final String socialAccountName 	= etSocialAccountName.getText().toString();
		final String status 			= etStatus.getText().toString();

		realmAsyncTask = myRealm.executeTransactionAsync(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				SocialAccount socialAccount = realm.createObject(SocialAccount.class);
				socialAccount.setName(socialAccountName);
				socialAccount.setStatus(status);

				User user = realm.createObject(User.class, id);
				user.setName(name);
				user.setAge(age);
				user.setSocialAccount(socialAccount);
			}
		}, new Realm.Transaction.OnSuccess() {
			@Override
			public void onSuccess() {
				Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
			}
		}, new Realm.Transaction.OnError() {
			@Override
			public void onError(Throwable error) {
				Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
			}
		});
	} // Add Data to Realm in the Background Thread.

	public void openDisplayActivity(View view) {

		Intent intent = new Intent(this, DisplayActivity.class);
		startActivity(intent);
	}

	public void displayAllUsers(View view) {

		RealmResults<User> userList = myRealm.where(User.class).findAll();
		displayQueriedUsers(userList);
	}

	private void displayQueriedUsers(RealmResults<User> userList) {

		StringBuilder builder = new StringBuilder();

		for (User user : userList) {
			builder.append("ID: ").append(user.getId());
			builder.append("\nName: ").append(user.getName());
			builder.append(", Age: ").append(user.getAge());

			SocialAccount socialAccount = user.getSocialAccount();
			builder.append("\nS'Account: ").append(socialAccount.getName());
			builder.append(", Status: ").append(socialAccount.getStatus()).append(" .\n\n");
		}

		Log.i(TAG + " UserList", builder.toString());
	}

	public void exploreMiscConcepts(View view) {

		userList = myRealm.where(User.class).findAllAsync();
		userList.addChangeListener(userListListener);


//		if (userList.isLoaded())
//			userList.deleteFirstFromRealm();
	}

	RealmChangeListener<RealmResults<User>> userListListener = new RealmChangeListener<RealmResults<User>>() {
		@Override
		public void onChange(RealmResults<User> userList) {
			displayQueriedUsers(userList);
		}
	};

	@Override
	protected void onStop() {
		super.onStop();

		if (userList != null)
			userList.removeChangeListener(userListListener); // Removes a particular Listener
		// Or userList.removeAllChangeListeners();			 // Remove all Registered Listeners

		if (realmAsyncTask != null && !realmAsyncTask.isCancelled()) {
			realmAsyncTask.cancel();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		myRealm.close();
	}
}
