package com.xiegeixiong.Gesturete;

import android.app.Activity;

import android.os.Bundle;

import android.view.GestureDetector;

import android.view.MotionEvent;

import android.widget.Toast;

public class GesturetestActivity extends Activity {

	/** Called when the activity is first created. */

	private GestureDetector gestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	//	setContentView(R.layout.activity_gesturetest);

		gestureDetector = new BuileGestureExt(this,
				new BuileGestureExt.OnGestureResult() {

					@Override
					public void onGestureResult(int direction) {

						show(Integer.toString(direction));

					}

				}

		).Buile();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event);

	}

	private void show(String value) {

//		Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

	}

}