//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.08 at 01:40:32 PM IST 
//


package com.tools.dependencyinjection.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeprecatedDependencyDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeprecatedDependencyDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DeprecatedJarName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeprecatedJarVersion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeprecatedDependencyDetail", namespace = "http://com/homedepot/ta/cu/ccs/xmlschema", propOrder = {
    "deprecatedJarName",
    "deprecatedJarVersion"
})
public class DeprecatedDependencyDetail {

    @XmlElement(name = "DeprecatedJarName", namespace = "http://com/homedepot/ta/cu/ccs/xmlschema", required = true)
    protected String deprecatedJarName;
    @XmlElement(name = "DeprecatedJarVersion", namespace = "http://com/homedepot/ta/cu/ccs/xmlschema", required = true)
    protected String deprecatedJarVersion;

    /**
     * Gets the value of the deprecatedJarName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeprecatedJarName() {
        return deprecatedJarName;
    }

    /**
     * Sets the value of the deprecatedJarName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeprecatedJarName(String value) {
        this.deprecatedJarName = value;
    }

    /**
     * Gets the value of the deprecatedJarVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeprecatedJarVersion() {
        return deprecatedJarVersion;
    }

    /**
     * Sets the value of the deprecatedJarVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeprecatedJarVersion(String value) {
        this.deprecatedJarVersion = value;
    }

}