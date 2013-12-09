package ru.aplix.converters.fr2afop.fr.image;

import java.awt.Image;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.type.MimeType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "JPEGImage")
public class JPEGImage extends AbstractImage<Image> {

	@XmlElement(name = "Content")
	@XmlMimeType(value = "image/jpeg")
	private Image image;

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public MimeType getMimeType() {
		return MimeType.JPEG;
	}
}
