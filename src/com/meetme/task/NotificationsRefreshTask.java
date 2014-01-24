package com.meetme.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.task.listener.NotificationsRefreshTaskListener;

public class NotificationsRefreshTask extends AsyncTask<Void, Void, Void> {
	
	private SessionManager session;
	private Context context;
	private ProgressDialog progressDialog;
	private NotificationsRefreshTaskListener listener;
	
	public NotificationsRefreshTask(
			Context context) {
		this.session = SessionManager.getInstance();
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(
				context, 
				context.getString(R.string.pleaseWait), 
				context.getString(R.string.refreshingNotifications), 
				true
			);
		progressDialog.setCancelable(true);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		session.updateFriendNotificationSet();
		session.updateMeetingNotificationSet();
		
		progressDialog.dismiss();
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		listener.onNotificationsRefreshTaskComplete();
	}

	public void setOnCompleteListener(NotificationsRefreshTaskListener listener) {
		this.listener = listener;
	}
}
