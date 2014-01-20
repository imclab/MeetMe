package com.meetme.presentation.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.presentation.FriendCheckable;

public class FriendListArrayAdapter extends ArrayAdapter<FriendCheckable> implements Filterable {
	
	private FriendListArrayAdapterFilter filter;
	private List<FriendCheckable> originalList;
	private List<FriendCheckable> list;
	private final Activity context;
	
	static class ViewHolder {
		protected TextView friendFullName;
	    protected CheckBox checkBox;
	}
	
	public FriendListArrayAdapter(Activity context, List<FriendCheckable> list) {
		super(context, R.layout.invite_friend_item, list);
	    this.context = context;
	    this.originalList = new ArrayList<FriendCheckable>(list);
	    this.list = new ArrayList<FriendCheckable>(list);
    }
	
	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public FriendCheckable getItem(int position) {
		return this.list.get(position);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.invite_friend_item, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.friendFullName = (TextView) view.findViewById(R.id.friendFullName);
			viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			
			viewHolder.checkBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
			    	public void onCheckedChanged(CompoundButton buttonView,
			    			boolean isChecked) {
						FriendCheckable element = (FriendCheckable) viewHolder.checkBox.getTag();
						element.setSelected(buttonView.isChecked());
	  				}
				});
			view.setTag(viewHolder);
			viewHolder.checkBox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkBox.setTag(list.get(position));
	    }
	    
		ViewHolder holder = (ViewHolder) view.getTag();
	    holder.friendFullName.setText(list.get(position).toString());
	    holder.checkBox.setChecked(list.get(position).isSelected());
	    return view;
	}
	
	@Override
	public Filter getFilter() {
		if (this.filter == null) {
			this.filter = new FriendListArrayAdapterFilter();
		}
		
		return this.filter;
	}

	/*
	 * Accessors
	 */
	public List<FriendCheckable> getFriendList() {
		return this.list;
	}
	
	/*
	 * Filter
	 */
	private class FriendListArrayAdapterFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			
			if (constraint == null || constraint.length() == 0) {
				// No constraint, we return the full list
				ArrayList<FriendCheckable> resultList = new ArrayList<FriendCheckable>(originalList);
				results.values = resultList; 
				results.count = resultList.size();
			} else {
				// Firstname or lastname must match the constraint
				// (case insensitive)
				List<FriendCheckable> resultList = new ArrayList<FriendCheckable>();
				Locale locale = Locale.getDefault();
				
				for (FriendCheckable friend : originalList) {
					String constraintString = constraint.toString().toLowerCase(locale);
					boolean firstnameMatches = 
							friend.getFirstname().toLowerCase(locale).startsWith(constraintString);
					boolean lastnameMatches = 
							friend.getLastname().toLowerCase(locale).startsWith(constraintString);
					
					if(firstnameMatches || lastnameMatches) {
						resultList.add(friend);
					}
				}
				
				results.values = resultList;
				results.count = resultList.size();
			}
			
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			list = (ArrayList<FriendCheckable>)results.values;
			notifyDataSetChanged();
		}
	}
}

