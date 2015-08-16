package in.ac.skit.skitech;

import in.ac.skit.skitech.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class WebPageFragment extends Fragment {

	WebView webView;
	protected ProgressBar progressBar;
	private LinearLayout wLayout;
	private MyBrowser mb=new MyBrowser();
	String url;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bd = getArguments();
		url = bd.getString(StartApp.URL_CHOSEN);

		webView = (WebView) getView().findViewById(R.id.webview);
		webView.setWebViewClient(new MyBrowser());
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				progressBar.setProgress(progress+10);
				if (progress >= 100) {
					if (wLayout != null) {
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								progressBar.setVisibility(View.GONE);
							}
						}, 350);
					}
				}
			}
		});
		webView.setWebViewClient(mb);
		webView.loadUrl(url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.page_view, container, false);
		wLayout = (LinearLayout) view.findViewById(R.id.web_layout);
		progressBar = (ProgressBar) view.findViewById(R.id.pb);
		return view;
	}

	private class MyBrowser extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			progressBar.setVisibility(View.VISIBLE);
			return super.shouldOverrideUrlLoading(webView, url);
		}

		@Override
		public void onReceivedError(WebView webView, int errorCode,
				String description, String failingUrl) {
			try {
				webView.stopLoading();
			} catch (Exception e) {
			}

			if (webView.canGoBack()) {
				webView.goBack();
			}
			webView.loadUrl("file:///android_asset/error.html");
			super.onReceivedError(webView, errorCode, description, failingUrl);
		}
	}

}
