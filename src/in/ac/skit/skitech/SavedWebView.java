package in.ac.skit.skitech;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class SavedWebView extends Fragment{

	String text;
	WebView webview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
        View view = inflater.inflate(R.layout.savedwebview, container, false);
		webview = (WebView) view.findViewById(R.id.savedwebview);
		Bundle bundle=getArguments(); 
        text=bundle.getString("text"); 
		return view;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
        
        webview.loadData(text, "text/html", "UTF-8");
        
	}

	
}
