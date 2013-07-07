package ru.aplix.converters.fr2afop.fr.view;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.type.BandType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BandView")
public class BandView extends View {

	@XmlAttribute(name = "Type")
	private BandType type;

	@XmlAttribute(name = "Dataset")
	private String dataset;

	@XmlElementWrapper(name = "ChildViews")
	@XmlElementRefs({ @XmlElementRef(type = MemoView.class), @XmlElementRef(type = PictureView.class), @XmlElementRef(type = SubReportView.class),
			@XmlElementRef(type = LineView.class), @XmlElementRef(type = BarCodeView.class) })
	private List<View> childViews;

	public BandType getType() {
		return type;
	}

	public void setType(BandType type) {
		this.type = type;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public List<View> getChildViews() {
		if (childViews == null) {
			childViews = new ArrayList<View>();
		}

		return childViews;
	}

	public void setChildViews(List<View> value) {
		this.childViews = value;
	}
}
