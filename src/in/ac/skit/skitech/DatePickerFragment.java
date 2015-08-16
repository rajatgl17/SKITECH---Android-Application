package in.ac.skit.skitech;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	
	public interface onClickDialogListener{
		public void changeText(String val);
	}
	
	onClickDialogListener oc;
	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			oc = (onClickDialogListener) activity;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		
		String dayx, monthx;
		
		if(String.valueOf(day).length() < 2){
			dayx = "0" + String.valueOf(day);
		}else{
			dayx = String.valueOf(day);
		}
		
		switch(month+1){
		case 1:monthx="Jan";break;
		case 2:monthx="Feb";break;
		case 3:monthx="Mar";break;
		case 4:monthx="Apr";break;
		case 5:monthx="May";break;
		case 6:monthx="Jun";break;
		case 7:monthx="Jul";break;
		case 8:monthx="Aug";break;
		case 9:monthx="Sep";break;
		case 10:monthx="Oct";break;
		case 11:monthx="Nov";break;
		case 12:monthx="Dec";break;
		default:monthx="";
		}
		
        String sdob =dayx+"-"+monthx+"-"+String.valueOf(year);
        oc.changeText(sdob);
	}
}
