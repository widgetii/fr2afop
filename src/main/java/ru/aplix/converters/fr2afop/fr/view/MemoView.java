package ru.aplix.converters.fr2afop.fr.view;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ru.aplix.converters.fr2afop.fr.type.FontStyle;
import ru.aplix.converters.fr2afop.fr.type.FontWeight;
import ru.aplix.converters.fr2afop.fr.type.TextAlign;
import ru.aplix.converters.fr2afop.fr.type.TextDecoration;
import ru.aplix.converters.fr2afop.fr.type.VerticalAlign;
import ru.aplix.converters.fr2afop.utils.ColorAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MemoView")
public class MemoView extends View {

	@XmlAttribute(name = "FontName")
	protected String fontName;
	@XmlJavaTypeAdapter(ColorAdapter.class)
	@XmlAttribute(name = "FontColor")
	protected Color fontColor;
	@XmlAttribute(name = "FontSize")
	protected float fontSize;
	@XmlAttribute(name = "FontWeight")
	protected FontWeight fontWeight;
	@XmlAttribute(name = "FontStyle")
	protected FontStyle fontStyle;
	@XmlAttribute(name = "TextDecoration")
	protected TextDecoration textDecoration;
	@XmlAttribute(name = "TextAlign")
	protected TextAlign textAlign;
	@XmlAttribute(name = "VerticalAlign")
	protected VerticalAlign verticalAlign;
	@XmlAttribute(name = "Rotate")
	protected int rotate;
	// @XmlElement(name = "Highlight", type = Highlight.class)
	protected transient Highlight highlight;

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String value) {
		this.fontName = value;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color value) {
		this.fontColor = value;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float value) {
		this.fontSize = value;
	}

	public FontWeight getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(FontWeight value) {
		this.fontWeight = value;
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyle value) {
		this.fontStyle = value;
	}

	public TextDecoration getTextDecoration() {
		return textDecoration;
	}

	public void setTextDecoration(TextDecoration value) {
		this.textDecoration = value;
	}

	public TextAlign getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(TextAlign value) {
		this.textAlign = value;
	}

	public VerticalAlign getVerticalAlign() {
		return verticalAlign;
	}

	public void setVerticalAlign(VerticalAlign value) {
		this.verticalAlign = value;
	}

	public int getRotate() {
		return rotate;
	}

	public void setRotate(int value) {
		this.rotate = value;
	}

	public Highlight getHighlight() {
		if (highlight == null) {
			highlight = new Highlight();
		}
		return highlight;
	}

	public void setHighlight(Highlight value) {
		this.highlight = value;
	}
}
