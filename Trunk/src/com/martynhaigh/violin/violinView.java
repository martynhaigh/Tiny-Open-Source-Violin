package com.martynhaigh.violin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class violinView extends View {

	public violinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}

	Bitmap violin = BitmapFactory.decodeResource(getResources(),
			R.drawable.violin);
	Bitmap bow = BitmapFactory.decodeResource(getResources(), R.drawable.bow);
	Float lastX, lastY;
	Boolean bowDown = false;
	MediaPlayer mp;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		lastX = event.getX();
		lastY = event.getY();
		if (action == MotionEvent.ACTION_UP) {
			bowDown = false;
			mp.release();
			Log.i("violin", "finger up on violin");
		} else if (action == MotionEvent.ACTION_MOVE) {
			Log.i("violin", "finger move on violin");
		} else if (action == MotionEvent.ACTION_DOWN) {
			bowDown = true;
			mp = MediaPlayer.create(getContext(), R.raw.music);
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
				}
			});			
			mp.start();

		}
		this.invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint violinColour = new Paint();
		violinColour.setColor(getResources().getColor(R.color.background));
		canvas.drawBitmap(violin, (getMeasuredWidth() - violin.getWidth()) / 2,
				(getMeasuredHeight() - violin.getHeight()) / 2, violinColour);
		if (bowDown)
			canvas.drawBitmap(bow, lastX - (bow.getWidth() / 2), lastY
					- (bow.getHeight() / 2), violinColour);

	}

}
