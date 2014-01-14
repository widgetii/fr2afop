package ru.aplix.converters.fr2afop.fr.image;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.type.MimeType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SVGImage")
public class SVGImage extends AbstractImage<String> {

	@XmlElement(name = "Content")
	private String image;

	@Override
	public String getImage() {
		return image;
	}

	@Override
	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public MimeType getMimeType() {
		return MimeType.SVG;
	}
}
