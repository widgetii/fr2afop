package ru.aplix.converters.fr2afop.fr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.type.PageOrientation;
import ru.aplix.converters.fr2afop.fr.view.BandView;
import ru.aplix.converters.fr2afop.fr.view.BarCodeView;
import ru.aplix.converters.fr2afop.fr.view.LineView;
import ru.aplix.converters.fr2afop.fr.view.MemoView;
import ru.aplix.converters.fr2afop.fr.view.PictureView;
import ru.aplix.converters.fr2afop.fr.view.SubReportView;
import ru.aplix.converters.fr2afop.fr.view.View;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Page")
public class Page {

	@XmlElementWrapper(name = "Views")
	@XmlElementRefs({ @XmlElementRef(type = MemoView.class), @XmlElementRef(type = PictureView.class), @XmlElementRef(type = BandView.class),
			@XmlElementRef(type = SubReportView.class), @XmlElementRef(type = LineView.class), @XmlElementRef(type = BarCodeView.class) })
	private List<View> views;

	@XmlAttribute(name = "Size")
	private int size;
	@XmlAttribute(name = "Width")
	private float width;
	@XmlAttribute(name = "Height")
	private float height;
	//@XmlAttribute(name = "Columns")
	private transient int columns;
	//@XmlAttribute(name = "ColumnGap")
	private transient int columnGap;
	@XmlAttribute(name = "MarginLeft")
	private float marginLeft;
	@XmlAttribute(name = "MarginTop")
	private float marginTop;
	@XmlAttribute(name = "MarginRight")
	private float marginRight;
	@XmlAttribute(name = "MarginBottom")
	private float marginBottom;
	@XmlAttribute(name = "Orientation")
	private PageOrientation orientation;
	@XmlAttribute(name = "UseMargins")
	private boolean useMargins;
	//@XmlAttribute(name = "PrintToPrevPage")
	private transient boolean printToPrevPage;

	public List<View> getViews() {
		if (views == null) {
			views = new ArrayList<View>();
		}

		return views;
	}

	public void setViews(List<View> value) {
		this.views = value;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getColumnGap() {
		return columnGap;
	}

	public void setColumnGap(int columnGap) {
		this.columnGap = columnGap;
	}

	public float getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

	public float getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}

	public float getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}

	public float getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}

	public PageOrientation getOrientation() {
		return orientation;
	}

	public void setOrientation(PageOrientation value) {
		this.orientation = value;
	}

	public boolean isUseMargins() {
		return useMargins;
	}

	public void setUseMargins(boolean useMargins) {
		this.useMargins = useMargins;
	}

	public boolean isPrintToPrevPage() {
		return printToPrevPage;
	}

	public void setPrintToPrevPage(boolean printToPrevPage) {
		this.printToPrevPage = printToPrevPage;
	}
}
