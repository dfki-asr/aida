/**
 * This file has been automatically generated using Grover (https://github.com/rmrschub/grover).
 * It contains static constants for the terms in the MATHS vocabulary.
 */
package de.dfki.resc28.aida.vocabularies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;

public final class MATHS 
{
  public static final String PREFIX = "MATHS";
  public static final PrefixMapping NAMESPACE = PrefixMapping.Factory.create().setNsPrefix(PREFIX, CONSTANTS.NS);

  /** 
   * Classes as org.apache.jena.rdf.model.Resource
   */
  public static final Resource Matrix3D = resource(CONSTANTS.CLASS_Matrix3D);
  public static final Resource Vector3D = resource(CONSTANTS.CLASS_Vector3D);
  public static final Resource CoordinateSystem = resource(CONSTANTS.CLASS_CoordinateSystem);

  /** 
   * Properties as org.apache.jena.rdf.model.Property
   */
  public static final Property a23 = property(CONSTANTS.PROP_a23);
  public static final Property a33 = property(CONSTANTS.PROP_a33);
  public static final Property a11 = property(CONSTANTS.PROP_a11);
  public static final Property a13 = property(CONSTANTS.PROP_a13);
  public static final Property y = property(CONSTANTS.PROP_y);
  public static final Property a21 = property(CONSTANTS.PROP_a21);
  public static final Property a31 = property(CONSTANTS.PROP_a31);
  public static final Property a12 = property(CONSTANTS.PROP_a12);
  public static final Property a22 = property(CONSTANTS.PROP_a22);
  public static final Property x = property(CONSTANTS.PROP_x);
  public static final Property a32 = property(CONSTANTS.PROP_a32);
  public static final Property z = property(CONSTANTS.PROP_z);


  /**
   * Returns a Jena resource for the given namespace name 
   * @param nsName  the full namespace name of a vocabulary element as a string
   * @return the vocabulary element with given namespace name as a org.apache.jena.rdf.model.Resource
   */
  private static final Resource resource(String nsName)
  {
    return ResourceFactory.createResource(nsName); 
  }

  /**
   * Returns a Jena property for the given namespace name
   * @param nsName  the full namespace name of a vocabulary element as a string
   * @return the vocabulary element with given namespace name as a org.apache.jena.rdf.model.Property
   */
  private static final Property property(String nsName)
  { 
    return ResourceFactory.createProperty(nsName);
  }

  private static final class CONSTANTS 
  {
    /**
     * Vocabulary namespace URI as string 
     */
    private static final String NS = "http://vocab.arvida.de/ns/maths#";

    /**
     * Local and namespace names of RDF(S) classes as strings 
     */
    private static final String CLASS_LNAME_Matrix3D = "Matrix3D";
    private static final String CLASS_Matrix3D = nsName(CLASS_LNAME_Matrix3D);
    private static final String CLASS_LNAME_Vector3D = "Vector3D";
    private static final String CLASS_Vector3D = nsName(CLASS_LNAME_Vector3D);
    private static final String CLASS_LNAME_CoordinateSystem = "CoordinateSystem";
    private static final String CLASS_CoordinateSystem = nsName(CLASS_LNAME_CoordinateSystem);

    /**
     * Local and namespace names of RDF(S) properties as strings 
     */
    private static final String PROP_LNAME_a23 = "a23";
    private static final String PROP_a23 = nsName(PROP_LNAME_a23);
    private static final String PROP_LNAME_a33 = "a33";
    private static final String PROP_a33 = nsName(PROP_LNAME_a33);
    private static final String PROP_LNAME_a11 = "a11";
    private static final String PROP_a11 = nsName(PROP_LNAME_a11);
    private static final String PROP_LNAME_a13 = "a13";
    private static final String PROP_a13 = nsName(PROP_LNAME_a13);
    private static final String PROP_LNAME_y = "y";
    private static final String PROP_y = nsName(PROP_LNAME_y);
    private static final String PROP_LNAME_a21 = "a21";
    private static final String PROP_a21 = nsName(PROP_LNAME_a21);
    private static final String PROP_LNAME_a31 = "a31";
    private static final String PROP_a31 = nsName(PROP_LNAME_a31);
    private static final String PROP_LNAME_a12 = "a12";
    private static final String PROP_a12 = nsName(PROP_LNAME_a12);
    private static final String PROP_LNAME_a22 = "a22";
    private static final String PROP_a22 = nsName(PROP_LNAME_a22);
    private static final String PROP_LNAME_x = "x";
    private static final String PROP_x = nsName(PROP_LNAME_x);
    private static final String PROP_LNAME_a32 = "a32";
    private static final String PROP_a32 = nsName(PROP_LNAME_a32);
    private static final String PROP_LNAME_z = "z";
    private static final String PROP_z = nsName(PROP_LNAME_z);

 
    /**
     * Returns the full namespace name of a vocabulary element as a string
     * @param localName  the local name of a vocabulary element as a string
     * @return the full namespace name of a vocabulary element as a string
     */
    private static String nsName(String localName) 
    {
      return NS + localName;
    }
  }
}