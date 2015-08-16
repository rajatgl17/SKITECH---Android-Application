package in.ac.skit.skitech;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WebPagerAdapter extends FragmentPagerAdapter {

	private String[] noticeTitles, ids;
	int position;
	
	public static final String CHOSEN_TITLE = "title";

	public WebPagerAdapter(FragmentManager fm, String[] titles, String[] ids, int pos) {
		super(fm);
		// TODO Auto-generated constructor stub

		noticeTitles = titles;
		this.ids = ids;
		position = pos;
	}

	@Override
	public Fragment getItem(int pos) {
		position = pos;
		String url = LoadingScreen.SITE_URL+"notice.php?id="+ids[position];
		
		Bundle arguments = new Bundle();
		arguments.putString(StartApp.URL_CHOSEN, url);

		// Create the fragment instance and pass the arguments
		WebPageFragment bookFragment = new WebPageFragment();
		bookFragment.setArguments(arguments);

		// return the fragment instance
		return bookFragment;
	}

	@Override
	public int getCount() {

		return noticeTitles.length;
	}
	
	@Override
	public CharSequence getPageTitle(int idx) {
		return noticeTitles[idx];
	}

}
