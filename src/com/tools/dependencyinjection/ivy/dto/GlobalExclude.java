//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.25 at 06:39:58 PM IST 
//

package com.tools.dependencyinjection.ivy.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

/**
 * <p>
 * Java class for global-exclude complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="global-exclude">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="conf" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="org" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="module" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="artifact" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ext" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="matcher" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "global-exclude", propOrder = { "conf" })
public class GlobalExclude {

	protected List<GlobalExclude.Conf> conf;
	@XmlAttribute
	protected String org;
	@XmlAttribute
	protected String module;
	@XmlAttribute
	protected String artifact;
	@XmlAttribute
	protected String type;
	@XmlAttribute
	protected String ext;
	@XmlAttribute
	protected String matcher;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	/**
	 * Gets the value of the conf property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the conf property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getConf().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link GlobalExclude.Conf }
	 * 
	 * 
	 */
	public List<GlobalExclude.Conf> getConf() {
		if (conf == null) {
			conf = new ArrayList<GlobalExclude.Conf>();
		}
		return this.conf;
	}

	/**
	 * Gets the value of the org property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrg() {
		return org;
	}

	/**
	 * Sets the value of the org property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrg(String value) {
		this.org = value;
	}

	/**
	 * Gets the value of the module property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Sets the value of the module property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setModule(String value) {
		this.module = value;
	}

	/**
	 * Gets the value of the artifact property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getArtifact() {
		return artifact;
	}

	/**
	 * Sets the value of the artifact property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setArtifact(String value) {
		this.artifact = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the ext property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * Sets the value of the ext property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setExt(String value) {
		this.ext = value;
	}

	/**
	 * Gets the value of the matcher property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMatcher() {
		return matcher;
	}

	/**
	 * Sets the value of the matcher property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMatcher(String value) {
		this.matcher = value;
	}

	/**
	 * Gets a map that contains attributes that aren't bound to any typed
	 * property on this class.
	 * 
	 * <p>
	 * the map is keyed by the name of the attribute and the value is the string
	 * value of the attribute.
	 * 
	 * the map returned by this method is live, and you can add new attribute by
	 * updating the map directly. Because of this design, there's no setter.
	 * 
	 * 
	 * @return always non-null
	 */
	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Conf {

		@XmlAttribute(required = true)
		protected String name;

		/**
		 * Gets the value of the name property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the value of the name property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setName(String value) {
			this.name = value;
		}

	}

}