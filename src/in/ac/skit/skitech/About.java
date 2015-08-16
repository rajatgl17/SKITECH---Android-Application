package in.ac.skit.skitech;


import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class About extends Fragment {

	private WebView webView1;
	private TextView coders;
	String s[]={"Rajat Goel  &  Subroto Biswas", "Subroto Biswas  &  Rajat Goel"};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Random rand = new Random();
		int i=rand.nextInt((1-0)+1);
		
		String htmlText = "<html><body style=\"text-align:justify; color:#555\"> %s </body></html>";
		String myData = "This is the official app of SKIT, Jaipur developed by SKIT Web Marines. The app will facilitate the students by sending them the official notices, public announcements, event reminders and exam calendars right in the notification menu of their Android smartphones.";
		
		View v = inflater.inflate(R.layout.about, null);
		getActivity().setTitle("About");
		webView1 = (WebView)v.findViewById(R.id.description2);
		coders=(TextView) v.findViewById(R.id.coders);
		
		coders.setText(s[i]);
		webView1.loadData(String.format(htmlText, myData), "text/html", "utf-8");
		return v;
	}

}