package ru.aplix.converters.fr2afop.fr.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.type.BarCodeType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BarCodeView")
public class BarCodeView extends View {

	@XmlAttribute(name = "CheckSum")
	private boolean checkSum;
	@XmlAttribute(name = "ShowText")
	private boolean showText;
	@XmlAttribute(name = "BarCodeType")
	private BarCodeType barCodeType;
	@XmlAttribute(name = "Angle")
	private int angle;
	@XmlAttribute(name = "DataMatrixEncoding")
	private String dataMatrixEncoding;
	@XmlAttribute(name = "ModuleWidth")
	private String moduleWidth;

	public boolean isCheckSum() {
		return checkSum;
	}

	public void setCheckSum(boolean checkSum) {
		this.checkSum = checkSum;
	}

	public boolean isShowText() {
		return showText;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public BarCodeType getBarCodeType() {
		return barCodeType;
	}

	public void setBarCodeType(BarCodeType barCodeType) {
		this.barCodeType = barCodeType;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public String getDataMatrixEncoding() {
		return dataMatrixEncoding;
	}

	public void setDataMatrixEncoding(String dataMatrixEncoding) {
		this.dataMatrixEncoding = dataMatrixEncoding;
	}

	public String getModuleWidth() {
		return moduleWidth;
	}

	public void setModuleWidth(String moduleWidth) {
		this.moduleWidth = moduleWidth;
	}
}
