package ru.aplix.converters.fr2afop.fr.view;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ru.aplix.converters.fr2afop.fr.type.FontStyle;
import ru.aplix.converters.fr2afop.fr.type.FontWeight;
import ru.aplix.converters.fr2afop.fr.type.TextDecoration;
import ru.aplix.converters.fr2afop.utils.ColorAdapter;

/**
 * Highlight subclass.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Highlight")
public class Highlight {

	@XmlAttribute(name = "FontWeight")
	protected FontWeight fontWeight;
	@XmlAttribute(name = "FontStyle")
	protected FontStyle fontStyle;
	@XmlAttribute(name = "TextDecoration")
	protected TextDecoration textDecoration;
	@XmlJavaTypeAdapter(ColorAdapter.class)
	@XmlAttribute(name = "FontColor")
	private Color fontColor;
	@XmlJavaTypeAdapter(ColorAdapter.class)
	@XmlAttribute(name = "FillColor")
	private Color fillColor;
	@XmlElement(name = "Text")
	private String text;

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

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color value) {
		this.fontColor = value;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color value) {
		this.fillColor = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String value) {
		this.text = value;
	}
}
