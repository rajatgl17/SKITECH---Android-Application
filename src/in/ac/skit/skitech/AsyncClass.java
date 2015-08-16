package in.ac.skit.skitech;

import in.ac.skit.skitech.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

public class AsyncClass extends AsyncTask<Void, Void, Void> {

	String url;
	Document doc;

	List<String> noticesid = new ArrayList<String>();
	List<String> noticestitle = new ArrayList<String>();
	List<String> noticestime = new ArrayList<String>();
	String noticesids[];
	String noticestitles[];
	String noticestimes[];

	Activity myClass;
	Fragment mFrag;
	LinearLayout mLayout;

	public AsyncClass(String url, Activity obj) {
		this.url = url;
		myClass = obj;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			doc = Jsoup.connect(url).get();

			if (myClass instanceof LoadingScreen || myClass instanceof StartApp) {
				Elements links = doc.getElementsByTag("a");

				for (Element e : links) {
					noticesid.add(e.attr("id"));
					noticestitle.add(e.text());
					noticestime.add(e.attr("time"));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		if (myClass instanceof StartApp) {
			if (((StartApp) myClass).nlfc == null) {
				((StartApp) myClass).mProgress.setVisibility(View.VISIBLE);
			}else{
				((StartApp) myClass).nlfc.mProgress.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		noticesids = noticesid.toArray(new String[noticesid.size()]);
		noticestitles = noticestitle.toArray(new String[noticestitle.size()]);
		noticestimes = noticestime.toArray(new String[noticestime.size()]);

		if (myClass instanceof LoadingScreen) {
			Intent i = new Intent(myClass, StartApp.class);

			Bundle bdl = new Bundle();
			bdl.putStringArray(LoadingScreen.NOTICE_ID, noticesids);
			bdl.putStringArray(LoadingScreen.NOTICE_TITLES, noticestitles);
			bdl.putStringArray(LoadingScreen.NOTICE_TIMES, noticestimes);
			i.putExtras(bdl);
			myClass.startActivity(i);
		} else if (myClass instanceof StartApp) {
			((StartApp) myClass).noticesIds = noticesids;
			((StartApp) myClass).noticesTimes = noticestimes;
			((StartApp) myClass).noticesTitles = noticestimes;
			
			if (((StartApp) myClass).nlfc == null) {
				((StartApp) myClass).nlfc = new NoticeListFrag();
				FragmentTransaction transaction = ((StartApp) myClass)
						.getSupportFragmentManager().beginTransaction();
				Bundle bd = new Bundle();
				bd.putStringArray(LoadingScreen.NOTICE_ID, noticesids);
				bd.putStringArray(LoadingScreen.NOTICE_TITLES, noticestitles);
				bd.putStringArray(LoadingScreen.NOTICE_TIMES, noticestimes);
				((StartApp) myClass).nlfc.setArguments(bd);

				transaction.add(R.id.df, ((StartApp) myClass).nlfc, "nlfc");
				transaction.commit();
				
				((StartApp) myClass).mProgress.setVisibility(View.GONE);
			} else {
				((StartApp) myClass).nlfc.setNoticetimes(noticestimes);
				((StartApp) myClass).nlfc.setNoticetitles(noticestitles);
				((StartApp) myClass).nlfc.mProgress.setVisibility(View.GONE);
				((StartApp) myClass).nlfc.onPostRefresh();
			}
			
			StartApp.refresh = true;

		} else if (myClass instanceof WebPageView) {
		}

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
