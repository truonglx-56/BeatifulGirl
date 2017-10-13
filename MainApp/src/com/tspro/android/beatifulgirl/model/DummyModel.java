package com.tspro.android.beatifulgirl.model;

/*
{
full_picture: "https://scontent.xx.fbcdn.net/v/t1.0-9/s720x720/21317839_529746830750534_1326429534462585372_n.jpg?_nc_ad=z-m&_nc_cid=0&oh=ca3492aa69a7353520b7dd763153626a&oe=5A3C7A17",
message: "Đố mng biết HỌC SINH hay SINH VIÊN ? =))) 🙈 IG : _quynhzin #vsbg",
updated_time: "2017-10-02T08:40:22+0000",
from: {
name: "Nguyen Vu Quynh Anh",
id: "100011456644998"
},
id: "1173636692750000_1455804564533210"
}
 */

public class DummyModel {
	
	private long mId;
	private String mImageURL;
	private String mText;
	private int mIconRes;

	public DummyModel() {
	}

	public DummyModel(long id, String imageURL, String text, int iconRes) {
		mId = id;
		mImageURL = imageURL;
		mText = text;
		mIconRes = iconRes;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public String getImageURL() {
		return mImageURL;
	}

	public void setImageURL(String imageURL) {
		mImageURL = imageURL;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public int getIconRes() {
		return mIconRes;
	}

	public void setIconRes(int iconRes) {
		mIconRes = iconRes;
	}

	@Override
	public String toString() {
		return mText;
	}
}
