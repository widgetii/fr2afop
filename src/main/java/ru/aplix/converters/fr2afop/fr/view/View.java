package ru.aplix.converters.fr2afop.fr.view;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ru.aplix.converters.fr2afop.fr.type.FrameStyle;
import ru.aplix.converters.fr2afop.fr.type.FrameType;
import ru.aplix.converters.fr2afop.utils.ColorAdapter;
import ru.aplix.converters.fr2afop.utils.FrameTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "View")
public abstract class View {

	@XmlAttribute(name = "Name")
	protected String name;
	@XmlAttribute(name = "Left")
	protected float left;
	@XmlAttribute(name = "Top")
	protected float top;
	@XmlAttribute(name = "Width")
	protected float width;
	@XmlAttribute(name = "Height")
	protected float height;
	@XmlAttribute(name = "BoundLeft")
	protected float boundLeft;
	@XmlAttribute(name = "BoundTop")
	protected float boundTop;
	@XmlAttribute(name = "BoundWidth")
	protected float boundWidth;
	@XmlAttribute(name = "BoundHeight")
	protected float boundHeight;
	// @XmlAttribute(name = "Flags")
	protected transient int flags;
	@XmlAttribute(name = "Stretched")
	protected boolean stretched;
	@XmlJavaTypeAdapter(FrameTypeAdapter.class)
	@XmlAttribute(name = "FrameType")
	protected FrameType frameType;
	@XmlAttribute(name = "FrameStyle")
	protected FrameStyle frameStyle;
	@XmlAttribute(name = "FrameWidth")
	protected float frameWidth;
	@XmlJavaTypeAdapter(ColorAdapter.class)
	@XmlAttribute(name = "FrameColor")
	protected Color frameColor;
	@XmlJavaTypeAdapter(ColorAdapter.class)
	@XmlAttribute(name = "FillColor")
	protected Color fillColor;
	// @XmlAttribute(name = "Format")
	protected transient Integer format;
	// @XmlAttribute(name = "FormatString")
	protected transient String formatString;
	@XmlAttribute(name = "Visible")
	protected boolean visible = true;
	@XmlElement(name = "Memo")
	protected String memo;
	// @XmlElement(name = "Script")
	protected transient String script;

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public float getLeft() {
		return left;
	}

	public void setLeft(float value) {
		this.left = value;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float value) {
		this.top = value;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float value) {
		this.width = value;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float value) {
		this.height = value;
	}

	public float getBoundLeft() {
		return boundLeft;
	}

	public void setBoundLeft(float boundLeft) {
		this.boundLeft = boundLeft;
	}

	public float getBoundTop() {
		return boundTop;
	}

	public void setBoundTop(float boundTop) {
		this.boundTop = boundTop;
	}

	public float getBoundWidth() {
		return boundWidth;
	}

	public void setBoundWidth(float boundWidth) {
		this.boundWidth = boundWidth;
	}

	public float getBoundHeight() {
		return boundHeight;
	}

	public void setBoundHeight(float boundHeight) {
		this.boundHeight = boundHeight;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public boolean isStretched() {
		return stretched;
	}

	public void setStretched(boolean stretched) {
		this.stretched = stretched;
	}

	public FrameType getFrameType() {
		if (frameType == null) {
			frameType = new FrameType();
		}
		return frameType;
	}

	public void setFrameType(FrameType value) {
		this.frameType = value;
	}

	public FrameStyle getFrameStyle() {
		return frameStyle;
	}

	public void setFrameStyle(FrameStyle value) {
		this.frameStyle = value;
	}

	public float getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(float value) {
		this.frameWidth = value;
	}

	public Color getFrameColor() {
		return frameColor;
	}

	public void setFrameColor(Color value) {
		this.frameColor = value;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color value) {
		this.fillColor = value;
	}

	public Integer getFormat() {
		return format;
	}

	public void setFormat(Integer value) {
		this.format = value;
	}

	public String getFormatString() {
		return formatString;
	}

	public void setFormatString(String value) {
		this.formatString = value;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean value) {
		this.visible = value;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String value) {
		this.memo = value;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String value) {
		this.script = value;
	}
}
