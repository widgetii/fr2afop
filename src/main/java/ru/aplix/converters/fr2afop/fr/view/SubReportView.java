package ru.aplix.converters.fr2afop.fr.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SubReportView")
public class SubReportView extends View {

	@XmlAttribute(name = "SubPage")
	protected int subPage;

	public int getSubPage() {
		return subPage;
	}

	public void setSubPage(int value) {
		this.subPage = value;
	}
}
