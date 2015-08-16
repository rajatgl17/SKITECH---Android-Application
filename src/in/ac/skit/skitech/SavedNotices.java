package in.ac.skit.skitech;

import java.io.FileInputStream;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SavedNotices extends Fragment {

	String[] files;
	String text;
	int flag = 0;
	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.savednotices, container, false);
		ListView listView = (ListView) view.findViewById(R.id.list);
		registerForContextMenu(listView);
		context = container.getContext();
		files = getActivity().getApplicationContext().fileList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
				.getApplicationContext(), R.layout.custom_simple_list_item_1,
				android.R.id.text1, files);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v,
					int position, long arg3) {

				try {
					FileInputStream fin = getActivity().getApplicationContext()
							.openFileInput(files[position]);
					int c;
					text = "";
					while ((c = fin.read()) != -1) {
						text = text + Character.toString((char) c);
						flag = 1;
					}

				} catch (Exception e) {
					Toast.makeText(getActivity().getApplicationContext(),
							"There has been some error.", Toast.LENGTH_LONG)
							.show();
					flag = 0;
				}

				if (flag == 1) {

					SavedWebView svw = new SavedWebView();
					FragmentTransaction transaction = getActivity()
							.getSupportFragmentManager().beginTransaction();
					Bundle bundle = new Bundle();
					bundle.putString("text", text);
					svw.setArguments(bundle);
					transaction.replace(R.id.df, svw, "svw");
					transaction.addToBackStack("svw");
					transaction.commit();
				}
			}
		});

		return view;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getTitle().equals("Delete")) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			int i=info.position;
			getActivity().getApplicationContext().deleteFile(files[i]);
			SavedNotices sn = new SavedNotices();
			FragmentTransaction transaction2 = getActivity().getSupportFragmentManager()
					.beginTransaction();
			transaction2.replace(R.id.df, sn,"sn");
			transaction2.addToBackStack("sn");
			transaction2.commit();
        }
        return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, 1, 0, "Delete");
        super.onCreateContextMenu(menu, v, menuInfo);
	}

}
