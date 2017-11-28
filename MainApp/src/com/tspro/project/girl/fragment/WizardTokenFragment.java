package com.tspro.project.girl.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.tspro.project.girl.R;

public class WizardTokenFragment extends Fragment {

	private static final String ARG_POSITION = "position";

	private int position;
	private ImageView image;

	public static WizardTokenFragment newInstance(int position) {
		WizardTokenFragment f = new WizardTokenFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_wizard_travel,
				container, false);
		image = (ImageView) rootView
				.findViewById(R.id.fragment_wizard_travel_image);

		if (position == 0) {
			Glide.with(this).load(R.drawable.image0).into(image);
		} else if (position == 1) {
			Glide.with(this).load(R.drawable.image1).into(image);
		} else if(position==2){
			Glide.with(this).load(R.drawable.image0).into(image);
		} else if(position==3){
			Glide.with(this).load(R.drawable.image3).into(image);
		}
		else if(position==4){
			Glide.with(this).load(R.drawable.image4).into(image);
		}
		else if(position==5){
			Glide.with(this).load(R.drawable.image5).into(image);
		}
		else if(position==6){
			Glide.with(this).load(R.drawable.image6).into(image);
		}
		else if(position==7){
			Glide.with(this).load(R.drawable.image7).into(image);
		}
		else if(position==8){
			Glide.with(this).load(R.drawable.image8).into(image);
		}
		else if(position==9){
			Glide.with(this).load(R.drawable.image9_10).into(image);
		}
		else if(position==10){
			Glide.with(this).load(R.drawable.image9_10).into(image);
		}



		ViewCompat.setElevation(rootView, 50);
		return rootView;
	}

}