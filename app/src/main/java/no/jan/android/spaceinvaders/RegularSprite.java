package no.jan.android.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class RegularSprite {
	private Bitmap bitmap;
	private int x;
	private int y;
	private int initialX;
	private int initialY;
	private int deltaX;
	private int deltaY;
	private int maxLeftPos;
	private int maxRightPos;
	private boolean animationBoundHorizontal;
	private boolean lifespanned = false;
	private int lifespan = 0;
	private Rect spriteRect = new Rect();
	
	// Create a standard sprite without velocity
	public RegularSprite(Bitmap bitmap, Point pos) {
		this.bitmap = bitmap;
		this.x = pos.x;
		this.y = pos.y;
		this.initialX = pos.x;
		this.initialY = pos.y;
		this.deltaX = 0;
		this.deltaY = 0;
		animationBoundHorizontal = false;
	}
	
	// Create a standard sprite with velocity
	public RegularSprite(Bitmap bitmap, Point pos, int deltaX, int deltaY) {
		this.bitmap = bitmap;
		this.x = pos.x;
		this.y = pos.y;
		this.initialX = pos.x;
		this.initialY = pos.y;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		animationBoundHorizontal = false;
	}
	
	// Create a standard sprite with specified lifetime
	public RegularSprite(Bitmap bitmap, Point pos, int deltaX, int deltaY, int lifetimeInTicks) {
		this.bitmap = bitmap;
		this.x = pos.x;
		this.y = pos.y;
		this.initialX = pos.x;
		this.initialY = pos.y;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		animationBoundHorizontal = false;
		lifespanned = true;
		lifespan = lifetimeInTicks;
	}
	
	// Create a standard sprite bound between max X-axis movement
	public RegularSprite(Bitmap bitmap, Point pos, int deltaX, int deltaY, int leftBound, int rightBound) {
		this.bitmap = bitmap;
		this.x = pos.x;
		this.y = pos.y;
		this.initialX = pos.x;
		this.initialY = pos.y;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.maxLeftPos = leftBound;
		this.maxRightPos = rightBound;
		animationBoundHorizontal = true;
	}
	

	public Rect getRect() {
		spriteRect.set(x-bitmap.getWidth()/2,
				y-bitmap.getHeight()/2, 
				x+bitmap.getWidth()/2, 
				y+bitmap.getHeight()/2);
		return spriteRect;
	}
	
	public boolean collide(Rect otherRect) {
		return getRect().intersect(otherRect);
	}
	
	public boolean update() {
		x += deltaX;
		y += deltaY;
		if (animationBoundHorizontal) {
			if (x < maxLeftPos) {
				x = maxLeftPos;
			} else if (x > maxRightPos) {
				x = maxRightPos;
			}
		}
		if (lifespanned) {
			lifespan--;
			if (lifespan <= 0) {
				return false;
			}
		}
		return true;
	}
	
	public void draw(Canvas canvas) {
		int offsetX = bitmap.getWidth()/2;
		int offsetY = bitmap.getHeight()/2;
		canvas.drawBitmap(bitmap, x - offsetX , y - offsetY, null);
	}
	
	public void resetSprite() {
		x = initialX;
		y = initialY;
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

	public int getInitialX() {
		return initialX;
	}

	public void setInitialX(int initialX) {
		this.initialX = initialX;
	}

	public int getInitialY() {
		return initialY;
	}

	public void setInitialY(int initialY) {
		this.initialY = initialY;
	}

	public int getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(int deltaY) {
		this.deltaY = deltaY;
	}

	public int getMaxLeftPos() {
		return maxLeftPos;
	}

	public void setMaxLeftPos(int maxLeftPos) {
		this.maxLeftPos = maxLeftPos;
	}

	public int getMaxRightPos() {
		return maxRightPos;
	}

	public void setMaxRightPos(int maxRightPos) {
		this.maxRightPos = maxRightPos;
	}

	public boolean isAnimationBoundHorizontal() {
		return animationBoundHorizontal;
	}

	public void setAnimationBoundHorizontal(boolean animationBoundHorizontal) {
		this.animationBoundHorizontal = animationBoundHorizontal;
	}
	
	public int getLifespan() {
		return lifespan;
	}

	public void setLifespan(int lifespan) {
		this.lifespanned = true;
		this.lifespan = lifespan;
	}

	public int getSpriteWidth() {
		return bitmap.getWidth();
	}
}
