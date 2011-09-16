package com.martynhaigh.violin;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ViolinView extends View {

	public ViolinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		violin = BitmapFactory
				.decodeResource(getResources(), R.drawable.violin);
		bow = BitmapFactory.decodeResource(getResources(), R.drawable.bow);
		
		scaledViolin = Bitmap.createBitmap(violin);
		highlightColour = new Paint();
		highlightColour.setColor(getResources().getColor(R.color.highlight));
		violinColour = new Paint();
		violinColour.setColor(getResources().getColor(R.color.background));
		subtleColour = new Paint();
		subtleColour.setColor(getResources().getColor(R.color.subtle));
		
	}

	Bitmap violin, bow, scaledViolin;
	Float lastX, lastY;
	Boolean bowDown = false, resize = false;
	MediaPlayer mp;
	double xOff, counter = 0;
	boolean top = false, left = false, calculateCentre = true;
	float vX1 = -100, vX2, vY1, vY2, bitmapRatio = 0;
	Paint highlightColour, violinColour, subtleColour;
	int touchArea = 20;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		lastX = event.getX();
		lastY = event.getY();
		if (action == MotionEvent.ACTION_UP) {
			if(resize)
				resize = false;
			else {
				bowDown = false;
				mp.release();
				Log.i("violin", "finger up on violin");
			}
		} else if (action == MotionEvent.ACTION_MOVE) {
			
			if(resize) {


					int width = violin.getWidth();
			        int height = violin.getHeight();
			        
			        int newHeight;
			        if(top) {
			        	newHeight =  (int) (getMeasuredHeight() - 2*lastY);
			        } else {
			        	newHeight =  (int) (getMeasuredHeight() - 2*(getMeasuredHeight() - lastY));
			        }
			       
			        if(newHeight < 10) newHeight = 10;
			        if(newHeight > getMeasuredHeight()-20) newHeight = getMeasuredHeight()-20;
			        
			        int newWidth = (int) (newHeight/bitmapRatio);
			        
			        Log.i("viold resize", "width = " + newWidth + " - ratio = " + bitmapRatio);
			        
					float scaleWidth = ((float) newWidth) / width;
					float scaleHeight = ((float) newHeight) / height;
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleHeight);
					scaledViolin = Bitmap.createBitmap(violin, 0, 0,
	                          violin.getWidth(), violin.getHeight(), matrix, true);
					calculateCentre = true;
					

			} else {
				double xNoise = lastX * 0.01;
				double yNoise = lastY * 0.01;
				double counterNoise = counter++ * 0.02;
	
				xOff = Noise.createNoise(xNoise, yNoise, counterNoise);
				}
		} else if (action == MotionEvent.ACTION_DOWN) {
			
			if (((lastX > vX1 - touchArea && lastX < vX1 + touchArea) || (lastX > vX2 - touchArea && lastX < vX2 + touchArea))
					&& ((lastY > vY1 - touchArea && lastY < vY1 + touchArea) || (lastY > vY2 - touchArea && lastY < vY2 + touchArea))) {
				resize = true;
				if (lastX > vX1 - touchArea && lastX < vX1 + touchArea) {
					left = true;
				} else {
					left = false;
				}
				if(lastY > vY1 - touchArea && lastY < vY1 + touchArea) {
					top = true;
				} else {
					top = false;
				}
			} else {
				bowDown = true;
				Random random = new Random();
				int randNum = random.nextInt(31);
				if (randNum > 20) {
					mp = MediaPlayer.create(getContext(), R.raw.violinmusic1);
				} else if (randNum < 10) {
					mp = MediaPlayer.create(getContext(), R.raw.violinmusic2);
				} else {
					mp = MediaPlayer.create(getContext(), R.raw.violinmusic3);
				}
				mp.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						mp.release();
					}
				});
				mp.start();
			}

		}
		this.invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(bitmapRatio == 0) {	
			bitmapRatio = (float)scaledViolin.getHeight() / (float)scaledViolin.getWidth();
			Log.i("bitmapRatio",  scaledViolin.getHeight() +"/"+ scaledViolin.getWidth() + "="+ bitmapRatio);
		}
		if(calculateCentre) {
			calculateCentre = false;
			vX1 = (getMeasuredWidth() - scaledViolin.getWidth()) / 2;
			vY1 = (getMeasuredHeight() - scaledViolin.getHeight()) / 2;
			vX2 = vX1 + scaledViolin.getWidth();
			vY2 = vY1 + scaledViolin.getHeight();
		}
		Log.i("violin", "vX1 = " + vX1 + "  - vY1 = " + vY1);		
		canvas.drawBitmap(scaledViolin, vX1, vY1, violinColour);

		if (bowDown)
			canvas.drawBitmap(bow, (float) ((lastX + (xOff * 80)) - (bow
					.getWidth() / 2)), lastY - (bow.getHeight() / 2),
					violinColour);
		if (resize) {

			drawDottedLine(canvas, vX1, vY1, vX2, vY1, subtleColour);
			drawDottedLine(canvas, vX1, vY1, vX1, vY2, subtleColour);

			drawDottedLine(canvas, vX1, vY2, vX2, vY2, subtleColour);
			drawDottedLine(canvas, vX2, vY1, vX2, vY2, subtleColour);

			
			
		} else {
			canvas.drawLine(vX1, vY1, vX1+touchArea, vY1, subtleColour);
			canvas.drawLine(vX1, vY1, vX1, vY1+touchArea, subtleColour);
			
			canvas.drawLine(vX1, vY2, vX1+touchArea, vY2, subtleColour);
			canvas.drawLine(vX1, vY2, vX1, vY2-touchArea, subtleColour);

			canvas.drawLine(vX2, vY1, vX2-touchArea, vY1, subtleColour);
			canvas.drawLine(vX2, vY1, vX2, vY1+touchArea, subtleColour);

			canvas.drawLine(vX2, vY2, vX2-touchArea, vY2, subtleColour);
			canvas.drawLine(vX2, vY2, vX2, vY2-touchArea, subtleColour);

		}

	}
	
	private void drawDottedLine(Canvas canvas, float startX, float startY, float stopX, float stopY, Paint paint) {
		int stop = 10;
		if(startX-stopX == 0) {
			for(float y = startY; y < stopY; y+=stop) {
				canvas.drawLine(startX, y, stopX, (float)(y+(.5*stop)), subtleColour);
			}			
		} else {
			for(float x = startX; x < stopX; x+=stop) {
				canvas.drawLine(x, startY, (float)(x+(.5*stop)), stopY, subtleColour);
			}				
		}
		

		
	}

}
