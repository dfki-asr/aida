/**
 * This file has been automatically generated using Grover (https://github.com/rmrschub/grover).
 * It contains static constants for the terms in the ART vocabulary.
 */
package de.dfki.resc28.aida.vocabularies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;


public final class ART 
{
  public static final String PREFIX = "ART";
  public static final PrefixMapping NAMESPACE = PrefixMapping.Factory.create().setNsPrefix(PREFIX, CONSTANTS.NS);

  /** 
   * Classes as org.apache.jena.rdf.model.Resource
   */
  public static final Resource ConfigurationAction = resource(CONSTANTS.CLASS_ConfigurationAction);
  public static final Resource StartMeasurementAction = resource(CONSTANTS.CLASS_StartMeasurementAction);
  public static final Resource StopMeasurementAction = resource(CONSTANTS.CLASS_StopMeasurementAction);
  public static final Resource TearDownAction = resource(CONSTANTS.CLASS_TearDownAction);
  public static final Resource TreeTarget = resource(CONSTANTS.CLASS_TreeTarget);
  public static final Resource Target = resource(CONSTANTS.CLASS_Target);
  public static final Resource DTrack2 = resource(CONSTANTS.CLASS_DTrack2);
  public static final Resource TargetContainer = resource(CONSTANTS.CLASS_TargetContainer);
  public static final Resource CoordinateSystemContainer = resource(CONSTANTS.CLASS_CoordinateSystemContainer);

  /** 
   * Properties as org.apache.jena.rdf.model.Property
   */
  public static final Property deviceState = property(CONSTANTS.PROP_deviceState);
  public static final Property serverHost = property(CONSTANTS.PROP_serverHost);
  public static final Property serverPort = property(CONSTANTS.PROP_serverPort);
  public static final Property dataPort = property(CONSTANTS.PROP_dataPort);
  public static final Property bodyID = property(CONSTANTS.PROP_bodyID);
  
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
    private static final String NS = "http://www.ar-tracking.com/ns#";

    /**
     * Local and namespace names of RDF(S) classes as strings 
     */
    private static final String CLASS_LNAME_TargetContainer = "TargetContainer";
    private static final String CLASS_TargetContainer = nsName(CLASS_LNAME_TargetContainer);
    private static final String CLASS_LNAME_CoordinateSystemContainer = "CoordinateSystemContainer";
    private static final String CLASS_CoordinateSystemContainer = nsName(CLASS_LNAME_CoordinateSystemContainer);
    private static final String CLASS_LNAME_ConfigurationAction = "ConfigurationAction";
    private static final String CLASS_ConfigurationAction = nsName(CLASS_LNAME_ConfigurationAction);
    private static final String CLASS_LNAME_StartMeasurementAction = "StartMeasurementAction";
    private static final String CLASS_StartMeasurementAction = nsName(CLASS_LNAME_StartMeasurementAction);
    private static final String CLASS_LNAME_StopMeasurementAction = "StopMeasurementAction";
    private static final String CLASS_StopMeasurementAction = nsName(CLASS_LNAME_StopMeasurementAction);
    private static final String CLASS_LNAME_TearDownAction = "TearDownAction";
    private static final String CLASS_TearDownAction = nsName(CLASS_LNAME_TearDownAction);
    private static final String CLASS_LNAME_TreeTarget = "TreeTarget";
    private static final String CLASS_TreeTarget = nsName(CLASS_LNAME_TreeTarget);
    private static final String CLASS_LNAME_Target = "Target";
    private static final String CLASS_Target = nsName(CLASS_LNAME_Target);
    private static final String CLASS_LNAME_DTrack2 = "DTrack2";
    private static final String CLASS_DTrack2 = nsName(CLASS_LNAME_DTrack2);

    /**
     * Local and namespace names of RDF(S) properties as strings 
     */
    private static final String PROP_LNAME_deviceState = "deviceState";
    private static final String PROP_deviceState = nsName(PROP_LNAME_deviceState);
    private static final String PROP_LNAME_serverHost = "serverHost";
    private static final String PROP_serverHost = nsName(PROP_LNAME_serverHost);
    private static final String PROP_LNAME_serverPort = "serverPort";
    private static final String PROP_serverPort = nsName(PROP_LNAME_serverPort);
    private static final String PROP_LNAME_dataPort = "dataPort";
    private static final String PROP_dataPort = nsName(PROP_LNAME_dataPort);
    private static final String PROP_LNAME_bodyID = "bodyID";
    private static final String PROP_bodyID = nsName(PROP_LNAME_bodyID);
    
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