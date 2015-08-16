package in.ac.skit.skitech;

import in.ac.skit.skitech.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CustomDialog extends DialogFragment {

	AlertDialog.Builder builder = null;
	String name;
	int branch, year;
	
	public CustomDialog(String name, int branch, int year){
		this.name = name;
		this.branch = branch;
		this.year = year;
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.dialog_start, null);
		
		TextView t_welcome = (TextView) v.findViewById(R.id.welcome);
		TextView t_branch = (TextView) v.findViewById(R.id.branch);
		TextView t_year = (TextView) v.findViewById(R.id.year);
		
		t_welcome.setText("Hey!\n"+ this.name);
		t_branch.setText("BRANCH : " + StartApp.BRANCH[branch-1]);
		t_year.setText("YEAR : " + StartApp.YEAR[year-1]);
		builder.setView(v);
		return builder.create();
		
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		
		Thread timer= new Thread(){
			public void run(){
				try{
					sleep(2000);
				}catch(InterruptedException e){
					
				}finally{
					dismiss();
				}
			}
		};
		timer.start();
	}
	
}
