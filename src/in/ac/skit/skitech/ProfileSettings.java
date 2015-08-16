package in.ac.skit.skitech;

import in.ac.skit.skitech.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileSettings extends Activity implements OnClickListener {

	LinearLayout profile_layout;
	FrameLayout profileImageLayout;
	ImageView profileImage, editImage, crossImage;

	public static boolean flag = false;

	final static int FROM_GALLERY = 0, FROM_GALLERY_CROP = 1;
	int imageSize, count;
	Uri imageUri;
	Bitmap bmp;
	TextView profileName, profileBranch;
	
	public void initialize() {
		editImage = (ImageView) findViewById(R.id.edit_profile_pic);		
		profile_layout = (LinearLayout) findViewById(R.id.profile_layout2);
		profileImageLayout = (FrameLayout) findViewById(R.id.profile_image_layout);
		profileImage = (ImageView) findViewById(R.id.settings_profile_image);
		profileBranch=(TextView) findViewById(R.id.profileBranch);
		profileName=(TextView) findViewById(R.id.profileName);
		crossImage = (ImageView) findViewById(R.id.pic_default);
		
		SharedPreferences sp = getSharedPreferences("userdata", 0);
		int branch = sp.getInt("branch", 0);
		int year = sp.getInt("year", 0);
		String name = sp.getString("name", null);
		
		profileName.setText(name);
		profileBranch.setText(StartApp.BRANCH[branch-1]+", "+StartApp.YEAR[year-1]+" year");
		editImage.setOnClickListener(this);
		crossImage.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);

		initialize();		
		
		final ViewTreeObserver observer = profile_layout.getViewTreeObserver();

		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				imageSize = profile_layout.getWidth()
						- (2 * profile_layout.getPaddingLeft());
				profile_layout.getViewTreeObserver().removeOnPreDrawListener(
						this);
				profileImageLayout
						.setLayoutParams(new LinearLayout.LayoutParams(
								imageSize, imageSize));

				if (profileImage != null) {
					profileImage.setLayoutParams(new FrameLayout.LayoutParams(imageSize,
							imageSize));
					if (loadImage(Details.FILE_NAME) != null)
						profileImage.setImageBitmap(loadImage(Details.FILE_NAME));
				}
				return true;

			}

		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public String saveToInternal(Bitmap bitmapImage) {

		File directory = new File(this.getBaseContext().getFilesDir(),
				Details.FILE_NAME);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(directory);
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("saved Paths", "directory = " + directory + "\nabsolutePath = "
				+ directory.getAbsolutePath());
		return directory.getAbsolutePath();
	}

	public Bitmap loadImage(String fileName) {
		try {
			File f = new File(this.getBaseContext().getFilesDir(), fileName);
			if (f != null) {
				Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
				return b;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.edit_profile_pic:
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, "Choose Image"),
					FROM_GALLERY);

			break;
		case R.id.pic_default:
			profileImage.setImageResource(R.drawable.profile_pic);
			File f = new File(this.getBaseContext().getFilesDir(), Details.FILE_NAME);
			boolean result = f.delete();
			if(result){
				flag = true;
				Toast.makeText(this, "Profile Pic removed...", Toast.LENGTH_LONG)
				.show();
			}
			break;
		}

	}

	@Override
	protected void onActivityResult(int request, int result, Intent data) {
		super.onActivityResult(request, result, data);
		if (result == RESULT_OK && request == FROM_GALLERY && data != null) {
			imageUri = data.getData();
			cropImage(imageUri);
		}
		if (result == RESULT_OK && request == FROM_GALLERY_CROP && data != null) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				bmp = extras.getParcelable("data");

				saveToInternal(bmp);
				if (profileImage != null) {
					profileImage.setLayoutParams(new FrameLayout.LayoutParams(imageSize,
							imageSize));
					profileImage.setImageBitmap(loadImage(Details.FILE_NAME));
				}
				Toast.makeText(this, "Profile photo updated", Toast.LENGTH_LONG)
				.show();
				flag = true;
			}
		} else if (result == RESULT_CANCELED && request == FROM_GALLERY_CROP) {
			if (imageUri != null && count < 1) {
				Toast.makeText(this, "Please Crop the Image, else press back one more time", Toast.LENGTH_LONG)
						.show();

				cropImage(imageUri);
			}
			count++;
		}
	}

	public void cropImage(Uri path) {
		Intent i = new Intent("com.android.camera.action.CROP");
		i.setDataAndType(path, "image/*");

		i.putExtra("crop", "true");
		i.putExtra("aspectX", 1);
		i.putExtra("aspectY", 1);
		i.putExtra("outputX", 200);
		i.putExtra("outputY", 200);

		try {
			i.putExtra("return-data", true);
			startActivityForResult(Intent.createChooser(i, "Crop Image"),
					FROM_GALLERY_CROP);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "no such crop system here", Toast.LENGTH_LONG)
					.show();
		}
	}

}
