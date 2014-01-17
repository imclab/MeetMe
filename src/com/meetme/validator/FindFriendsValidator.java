package com.meetme.validator;

import static com.meetme.store.MessageStore.FRIEND_IS_YOURSELF;
import static com.meetme.store.MessageStore.INVALID_EMAIL;
import android.content.Context;
import android.widget.EditText;

import com.meetme.core.SessionManager;

public class FindFriendsValidator extends Validator {
	
	private EditText searchFriendEdit;

	/*
	 * Constructors
	 */
	public FindFriendsValidator(Context context, EditText searchFriendEdit) {
		super(context);
		this.searchFriendEdit = searchFriendEdit;
	}
	
	/*
	 * private methods
	 */
	private boolean validateSearchFriend() {
		boolean isSearchFriendValid = true;
		String searchFriend = searchFriendEdit.getText().toString();
		
		// Friend search must be an email
		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(searchFriend).matches()) {
			searchFriendEdit.setError(getString(INVALID_EMAIL));
			isSearchFriendValid = false;
		}
		
		if (SessionManager.getInstance().getEmail().equals(searchFriend)) {
			searchFriendEdit.setError(getString(FRIEND_IS_YOURSELF));
			isSearchFriendValid = false;
		}
		
		return isSearchFriendValid;
	}
	
	/*
	 * Methods
	 */
	@Override
	public boolean validate() {
		return validateSearchFriend();
	}
}
