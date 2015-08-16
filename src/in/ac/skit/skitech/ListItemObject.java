package in.ac.skit.skitech;

public class ListItemObject {

	private String mTitle, mDate;
	
	public ListItemObject(){
		mTitle = null;
		mDate = null;
	}

    public ListItemObject(String title, String date) {
        super();
        mTitle = title;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate(){
    	return mDate;
    }
    
	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
	public void setDate(String mDate) {
		this.mDate = mDate;
	}

}
