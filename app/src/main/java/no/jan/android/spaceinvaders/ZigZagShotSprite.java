package no.jan.android.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ZigZagShotSprite {
	private Bitmap[] bitmaps = new Bitmap[2];
	private int x;
	private int y;
	private int deltaX;
	private int deltaY;
	int currentBitmap;
	private int stepSize;
	private int stepInterval;
	private int stepCounter;
	private Rect spriteRect = new Rect();
	
	public ZigZagShotSprite(Bitmap bitmap1, Bitmap bitmap2, int x, int y, int stepSize, int stepInterval) {
		bitmaps[0] = bitmap1;
		bitmaps[1] = bitmap2;
		this.x = x;
		this.y = y;
		this.stepSize = stepSize;
		this.stepInterval = stepInterval;
		currentBitmap = 0;
		stepCounter = 0;
	}
	
	public Rect getRect() {
		spriteRect.set(x-bitmaps[0].getWidth()/2,
				y-bitmaps[0].getHeight()/2, 
				x+bitmaps[0].getWidth()/2, 
				y+bitmaps[0].getHeight()/2);
		return spriteRect;
	}
	
	public boolean collide(Rect otherRect) {
		return getRect().intersect(otherRect);
	}
	
	public boolean update() {
		stepCounter++;
		if (stepCounter > stepInterval) {
			stepCounter = 0;
			this.y += this.stepSize;	
			if (currentBitmap == 0) {
				currentBitmap = 1;
			} else {
				currentBitmap = 0;
			}
			return true;
		} 
		return false;
	}
	
	public void draw(Canvas canvas) {
		int offsetX = bitmaps[currentBitmap].getWidth()/2;
		int offsetY = bitmaps[currentBitmap].getHeight()/2;
		canvas.drawBitmap(bitmaps[currentBitmap], x - offsetX , y - offsetY, null);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getStepSize() {
		return stepSize;
	}

	public void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}

	public int getStepInterval() {
		return stepInterval;
	}

	public void setStepInterval(int stepInterval) {
		this.stepInterval = stepInterval;
	}
	
}
