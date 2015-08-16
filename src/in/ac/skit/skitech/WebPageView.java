package in.ac.skit.skitech;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import in.ac.skit.skitech.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Toast;

public class WebPageView extends ActionBarActivity implements
		OnPageChangeListener, OnTouchListener {

	String url;
	String[] noticeTitles, noticeIds;
	int id, count;

	WebPagerAdapter mWebPage;
	ViewPager mViewPager;
	AsyncSave b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.web_layout);

		Bundle bdl = getIntent().getExtras();
		id = bdl.getInt(StartApp.URL_ID);
		noticeTitles = bdl.getStringArray(LoadingScreen.NOTICE_TITLES);
		noticeIds = bdl.getStringArray(LoadingScreen.NOTICE_ID);

		mWebPage = new WebPagerAdapter(getSupportFragmentManager(),
				noticeTitles, noticeIds, id);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mWebPage);
		mViewPager.setCurrentItem(id);
		mViewPager.setPageTransformer(true, new ZoomPageTransformer());
		count = mWebPage.getCount();
		setTitle("Notice");

		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.start_app2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		String urlSave;
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, StartApp.class));
		case R.id.action_refresh:
			WebPageFragment current = (WebPageFragment) mWebPage.instantiateItem(mViewPager, mViewPager.getCurrentItem());
			current.progressBar.setVisibility(View.VISIBLE);
			current.webView.loadUrl(current.url);
			break;
		case R.id.action_save:
			WebPageFragment current2 = (WebPageFragment) mWebPage.instantiateItem(mViewPager, mViewPager.getCurrentItem());
			urlSave=current2.url;
			b=new AsyncSave(urlSave);
			b.execute();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	@Override
	public boolean onTouch(View v, MotionEvent motion) {
		WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
        
        Log.i("hit", "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
		return false;
	}
	
	class AsyncSave extends AsyncTask<Void, Void, Void> {
		
		String url;
		String text;
		String title;
		int flag=0;
		public AsyncSave(String url) {
			this.url = url;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Document doc = Jsoup.connect(url).get();
				text = doc.toString();
				Element link = doc.select("h2").first();
				title = link.text();	
			} catch (IOException e) {
				flag=1;
				Toast.makeText(getApplicationContext(),"Unable to save.",
						Toast.LENGTH_SHORT).show();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			/*FileOutputStream outputStream;

			try {
			  outputStream = openFileOutput(title+".txt", Context.MODE_PRIVATE);
			  outputStream.write(text.getBytes());
			  outputStream.close();
			} catch (Exception e) {
				flag=1;
				Toast.makeText(getApplicationContext(),"Unable to save.",
						Toast.LENGTH_SHORT).show();
			}*/
			
			try{
				FileOutputStream fOut = openFileOutput(title,MODE_WORLD_READABLE);
		         fOut.write(text.getBytes());
		         fOut.close();
			}catch(Exception e){
				flag=1;
				Toast.makeText(getApplicationContext(),"Unable to save.",
						Toast.LENGTH_SHORT).show();
			}
			if(flag==0)
				Toast.makeText(getApplicationContext(),"Notice saved successfully.",
					Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}

}
