package ru.aplix.converters.fr2afop.fr.type;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FrameType class.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "FrameType")
public class FrameType {

	private static final String ALL = "all";
	private static final String NONE = "none";
	private static final String SEPARATOR = "|";

	private Set<FrameTypeEnum> set;

	public FrameType() {
		set = new TreeSet<FrameTypeEnum>();
	}

	public void setEdge(FrameTypeEnum edge) {
		set.add(edge);
	}

	public boolean hasEdge(FrameTypeEnum edge) {
		return set.contains(edge);
	}

	public void setNone() {
		set.clear();
	}

	public boolean isNone() {
		return (set.size() == 0);
	}

	public void setAll() {
		for (FrameTypeEnum entry : FrameTypeEnum.values()) {
			setEdge(entry);
		}
	}

	public boolean isAll() {
		boolean result = true;
		for (FrameTypeEnum entry : FrameTypeEnum.values()) {
			result &= hasEdge(entry);
		}
		return result;
	}

	@Override
	public String toString() {
		if (isNone()) {
			return NONE;
		} else if (isAll()) {
			return ALL;
		} else {
			StringBuffer sb = new StringBuffer();
			Iterator<FrameTypeEnum> i = set.iterator();
			while (i.hasNext()) {
				FrameTypeEnum key = i.next();
				if (sb.length() > 0) {
					sb.append(SEPARATOR);
				}
				sb.append(key.value());
			}
			return sb.toString();
		}
	}

	public static FrameType fromValue(String v) {
		if (NONE.equals(v)) {
			return new FrameType();
		} else if (ALL.equals(v)) {
			FrameType ft = new FrameType();
			for (FrameTypeEnum entry : FrameTypeEnum.values()) {
				ft.setEdge(entry);
			}
			return ft;
		} else if (v != null && v.length() > 0) {
			FrameType ft = new FrameType();
			String[] parts = v.split("\\" + SEPARATOR);
			for (String part : parts) {
				FrameTypeEnum entry = FrameTypeEnum.fromValue(part);
				ft.setEdge(entry);
			}
			return ft;
		}

		throw new IllegalArgumentException(v);
	}
}
