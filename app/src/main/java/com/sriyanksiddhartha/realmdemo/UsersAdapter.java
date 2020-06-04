package com.sriyanksiddhartha.realmdemo;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sriyanksiddhartha.realmdemo.model.SocialAccount;
import com.sriyanksiddhartha.realmdemo.model.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

	private static final String TAG = UsersAdapter.class.getSimpleName();
	private Context 			mContext;
	private RealmResults<User> 	mRealmUserList;
	private LayoutInflater 		mInflater;
	private Realm 				mRealm;

	public UsersAdapter(Context context, Realm realm, RealmResults<User> realmUserList) {

		this.mContext = context;
		this.mRealm = realm;
		this.mRealmUserList = realmUserList;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = mInflater.inflate(R.layout.list_item, parent, false);
		MyViewHolder viewHolder = new MyViewHolder(itemView);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {

		User user = mRealmUserList.get(position);
		holder.setData(user, position);
		holder.setListeners();
	}

	@Override
	public int getItemCount() {
		return mRealmUserList.size();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {

		private int position;
		private TextView txvNameAge, txvSocialAccount;
		private ImageView imgDelete, imgEdit;

		public MyViewHolder(View itemView) {
			super(itemView);
			txvNameAge 			= (TextView) itemView.findViewById(R.id.txvNameAge);
			txvSocialAccount 	= (TextView) itemView.findViewById(R.id.txvSocialAccount);
			imgDelete 			= (ImageView) itemView.findViewById(R.id.ivRowDelete);
			imgEdit 			= (ImageView) itemView.findViewById(R.id.ivRowEdit);
		}

		public void setData(User user, int position) {
			this.position = position;

			String userDetail = user.getName() + " : " + user.getAge() + " years old";
			txvNameAge.setText(userDetail);

			SocialAccount account = user.getSocialAccount();
			if (account != null) {
				String accountDetail = account.getName() + " : " + account.getStatus();
				txvSocialAccount.setText(accountDetail);
			}
		}

		public void setListeners() {

			imgDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					mRealm.executeTransaction(new Realm.Transaction() {
						@Override
						public void execute(Realm realm) {
							mRealmUserList.deleteFromRealm(position);
							notifyItemRemoved(position);
							notifyItemRangeChanged(position, mRealmUserList.size());
						}
					});
				}
			});

			imgEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, EditActivity.class);
					intent.putExtra("position", position);
					mContext.startActivity(intent);
				}
			});
		}
	}
}
