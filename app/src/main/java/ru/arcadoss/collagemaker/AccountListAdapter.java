package ru.arcadoss.collagemaker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import ru.arcadoss.collagemaker.json.User;

import java.util.List;

/**
 * Created by arcturus at 02.07.14
 */
@EBean
public class AccountListAdapter extends BaseAdapter {
	@RootContext Context rootContext;
	private List<User> userAccounts;

	public void setUserAccounts(List<User> userAccounts) {
		if (this.userAccounts != userAccounts) {
			this.userAccounts = userAccounts;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return userAccounts == null ? 0 : userAccounts.size();
	}

	@Override
	public User getItem(int position) {
		return userAccounts == null ? null : userAccounts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserAccountView view;

		if (convertView instanceof UserAccountView) {
			view = (UserAccountView) convertView;
		} else {
			view = UserAccountView_.build(rootContext);
		}

		view.bind(getItem(position));

		return view;
	}
}
