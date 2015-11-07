package no.jan.android.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class ButtonSprite {
	
	private Bitmap neutralImg;
	private Bitmap pressedImg;
	private int x;
	private int y;
	private boolean pressed;
	private Rect buttonRect;
	private int halfButtonX;
	private int halfButtonY;
	
	public ButtonSprite(Bitmap neutralImg, Bitmap pressedImg, Point pos) {
		this.neutralImg = neutralImg;
		this.pressedImg = pressedImg;
		this.x = pos.x;
		this.y = pos.y;
		pressed = false;
		halfButtonX = neutralImg.getWidth()/2;
		halfButtonY = neutralImg.getHeight()/2;
		buttonRect = new Rect(x-halfButtonX, y-halfButtonY, x+halfButtonX, y+halfButtonY);
		Log.d("BUTTON_SPRITE", "Pos: X=" + x + ",y=" + y);
		Log.d("BUTTON_SPRITE", "Rect: " + buttonRect.toString());
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
	public boolean isInsideButton(int x, int y) {
		return buttonRect.contains(x, y);
	}

	public void draw(Canvas canvas) {
		if (pressed) {
			canvas.drawBitmap(pressedImg, x - pressedImg.getWidth()/2, y - pressedImg.getHeight()/2, null);
		} else {
			canvas.drawBitmap(neutralImg, x - neutralImg.getWidth()/2, y - neutralImg.getHeight()/2, null);
		}
	}
	
	public Point getPos() {
		return new Point(x, y);
	}
	
	public int getXDimension() {
		return neutralImg.getWidth();
	}
	
	public int getYDimension() {
		return neutralImg.getHeight();
	}
}
