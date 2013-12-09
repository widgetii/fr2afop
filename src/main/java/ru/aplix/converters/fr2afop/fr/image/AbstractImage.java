package ru.aplix.converters.fr2afop.fr.image;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.type.MimeType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Image")
public abstract class AbstractImage<T> {

	@XmlAttribute(name = "Mime")
	public abstract MimeType getMimeType();

	public abstract T getImage();

	public abstract void setImage(T image);
}
