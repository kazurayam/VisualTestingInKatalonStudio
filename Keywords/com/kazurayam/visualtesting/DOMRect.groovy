package com.kazurayam.visualtesting

/**
 * represents the x-y postion and the widht,height of a web element in a DOM
 * 
 * @author kazurayam
 */
public class DOMRect {

	int x_ = 0
	int y_ = 0
	int width_ = 0
	int height_ = 0

	DOMRect(int x, int y, int width, int height) {
		x_ = x
		y_ = y
		width_ = width
		height_ = height
	}

	int x() {
		return x_
	}

	int y() {
		return y_
	}

	int width() {
		return width_
	}

	int height() {
		return height_
	}

	int top() {
		if (this.height() < 0) {
			return this.y() + this.height()
		} else {
			return this.y()
		}
	}

	int right() {
		if (this.width() < 0) {
			return this.x()
		} else {
			return this.x() + this.width()
		}
	}

	int bottom() {
		if (this.height() < 0) {
			return this.y()
		} else {
			return this.y() + this.height()
		}
	}

	int left() {
		if (this.width() < 0) {
			return this.x() + this.width()
		} else {
			return this.x
		}
	}

	@Override
	String toString() {
		StringBuilder sb = new StringBuilder()
		sb.append('{')
		sb.append('\"x\":' + this.x() + ',')
		sb.append('\"y\":' + this.y() + ',')
		sb.append('\"width\":' + this.width() + ',')
		sb.append('\"height\":' + this.height())
		sb.append('}')
		return sb.toString()
	}

	@Override
	boolean equals(Object obj) {
		if (obj == null) return false
		if (!(obj instanceof DOMRect)) return false
		DOMRect other = (DOMRect)obj
		return this.x() == other.x() &&
				this.y() == other.y() &&
				this.width() == other.width() &&
				this.height() == other.height()
	}

	@Override
	int hashCode() {
		int result = 17;
		result = 31 * result + this.x();
		result = 31 * result + this.y();
		result = 31 * result + this.width();
		result = 31 * result + this.height();
		return result;
	}
}
