/****
 * This Adapter is still unused yet
 * */

package com.sigtech.panicbutton.data;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sigtech.panicbutton.R;

class FollowersAdapter extends BaseAdapter {
	Context c;
	List<People> followers;

	public FollowersAdapter(Context c, List<People> followers) {
		this.followers = followers;
		this.c = c;
	}

	@Override
	public int getCount() {
		return followers.size();
	}

	@Override
	public Object getItem(int position) {
		return followers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return followers.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// com.umranium.ebook.model.Book book = books.get(position);

		if (convertView == null) {
			convertView = View.inflate(c,R.layout.follower,
					null);
		}

		// setupView(convertView, book);

		return convertView;
	}

}
