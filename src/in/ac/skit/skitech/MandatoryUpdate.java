package in.ac.skit.skitech;

import in.ac.skit.skitech.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MandatoryUpdate extends Activity {
	
	TextView msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		
		msg=(TextView) findViewById(R.id.updatemsg);
		msg.setText("A new update is available. Please update your app.");
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	

}
