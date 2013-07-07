package ru.aplix.converters.fr2afop.utils;

public class Rectangle {

	public float x;
	public float y;
	public float width;
	public float height;

	public Rectangle() {
		this(0, 0, 0, 0);
	}

	public Rectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public boolean contains(Rectangle r) {
		return contains(r.x, r.y, r.width, r.height);
	}

	public boolean contains(float X, float Y, float W, float H) {
		float w = this.width;
		float h = this.height;
		if (w < 0f || h < 0f || W < 0f || H < 0f) {
			// At least one of the dimensions is negative...
			return false;
		}
		// Note: if any dimension is zero, tests below must return false...
		float x = this.x;
		float y = this.y;
		if (X < x || Y < y) {
			return false;
		}
		w += x;
		W += X;
		if (W <= X) {
			// X+W overflowed or W was zero, return false if...
			// either original w or W was zero or
			// x+w did not overflow or
			// the overflowed x+w is smaller than the overflowed X+W
			if (w >= x || W > w)
				return false;
		} else {
			// X+W did not overflow and W was not zero, return false if...
			// original w was zero or
			// x+w did not overflow and x+w is smaller than X+W
			if (w >= x && W > w)
				return false;
		}
		h += y;
		H += Y;
		if (H <= Y) {
			if (h >= y || H > h)
				return false;
		} else {
			if (h >= y && H > h)
				return false;
		}
		return true;
	}

	public boolean contains(float X, float Y) {
		float w = this.width;
		float h = this.height;
		if (w < 0f || h < 0f) {
			// At least one of the dimensions is negative...
			return false;
		}
		// Note: if either dimension is zero, tests below must return false...
		float x = this.x;
		float y = this.y;
		if (X < x || Y < y) {
			return false;
		}
		w += x;
		h += y;
		// overflow || intersect
		return ((w < x || w > X) && (h < y || h > Y));
	}

	public boolean equals(Object obj) {
		if (obj instanceof Rectangle) {
			Rectangle r = (Rectangle) obj;
			return ((x == r.x) && (y == r.y) && (width == r.width) && (height == r.height));
		}
		return super.equals(obj);
	}

	public String toString() {
		return getClass().getName() + "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
	}
}
