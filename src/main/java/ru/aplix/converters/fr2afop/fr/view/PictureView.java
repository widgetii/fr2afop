package ru.aplix.converters.fr2afop.fr.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.image.AbstractImage;
import ru.aplix.converters.fr2afop.fr.image.BMPImage;
import ru.aplix.converters.fr2afop.fr.image.JPEGImage;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PictureView")
public class PictureView extends View {

	@XmlElementRefs({ @XmlElementRef(type = JPEGImage.class), @XmlElementRef(type = BMPImage.class) })
	private AbstractImage image;
	@XmlAttribute(name = "Centered")
	private Boolean centered;
	@XmlAttribute(name = "KeepRatio")
	private Boolean keepRatio;

	public AbstractImage getImage() {
		return image;
	}

	public void setImage(AbstractImage image) {
		this.image = image;
	}

	public Boolean getCentered() {
		return centered;
	}

	public void setCentered(Boolean centered) {
		this.centered = centered;
	}

	public Boolean getKeepRatio() {
		return keepRatio;
	}

	public void setKeepRatio(Boolean keepRatio) {
		this.keepRatio = keepRatio;
	}
}
