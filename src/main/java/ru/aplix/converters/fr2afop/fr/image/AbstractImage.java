package ru.aplix.converters.fr2afop.fr.image;

import java.awt.Image;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.type.MimeType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Image")
public abstract class AbstractImage {

	@XmlAttribute(name = "Mime")
	public abstract MimeType getMimeType();

	public abstract Image getImage();

	public abstract void setImage(Image image);
}
