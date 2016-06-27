/**
 * This file has been automatically generated using Grover (https://github.com/rmrschub/grover).
 * It contains static constants for the terms in the SPATIAL vocabulary.
 */
package de.dfki.resc28.aida.vocabularies;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;

public final class SPATIAL 
{
  public static final String PREFIX = "SPATIAL";
  public static final PrefixMapping NAMESPACE = PrefixMapping.Factory.create().setNsPrefix(PREFIX, CONSTANTS.NS);

  /** 
   * Classes as org.apache.jena.rdf.model.Resource
   */
  public static final Resource Translation = resource(CONSTANTS.CLASS_Translation);
  public static final Resource SpatialService = resource(CONSTANTS.CLASS_SpatialService);
  public static final Resource Rotation = resource(CONSTANTS.CLASS_Rotation);
  public static final Resource SpatialRelationship = resource(CONSTANTS.CLASS_SpatialRelationship);
  public static final Resource Rotation2D = resource(CONSTANTS.CLASS_Rotation2D);
  public static final Resource Rotation3D = resource(CONSTANTS.CLASS_Rotation3D);
  public static final Resource Translation2D = resource(CONSTANTS.CLASS_Translation2D);
  public static final Resource Translation3D = resource(CONSTANTS.CLASS_Translation3D);

  /** 
   * Properties as org.apache.jena.rdf.model.Property
   */
  public static final Property rotation = property(CONSTANTS.PROP_rotation);
  public static final Property sourceCoordinateSystem = property(CONSTANTS.PROP_sourceCoordinateSystem);
  public static final Property pose = property(CONSTANTS.PROP_pose);
  public static final Property spatialProperty = property(CONSTANTS.PROP_spatialProperty);
  public static final Property projection = property(CONSTANTS.PROP_projection);
  public static final Property translation = property(CONSTANTS.PROP_translation);
  public static final Property targetCoordinateSystem = property(CONSTANTS.PROP_targetCoordinateSystem);
  public static final Property scaling = property(CONSTANTS.PROP_scaling);
  public static final Property coordinateSystem = property(CONSTANTS.PROP_coordinateSystem);
  public static final Property spatialRelationship = property(CONSTANTS.PROP_spatialRelationship);


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
    private static final String NS = "http://vocab.arvida.de/ns/spatial#";

    /**
     * Local and namespace names of RDF(S) classes as strings 
     */
    private static final String CLASS_LNAME_Translation = "Translation";
    private static final String CLASS_Translation = nsName(CLASS_LNAME_Translation);
    private static final String CLASS_LNAME_SpatialService = "SpatialService";
    private static final String CLASS_SpatialService = nsName(CLASS_LNAME_SpatialService);
    private static final String CLASS_LNAME_Rotation = "Rotation";
    private static final String CLASS_Rotation = nsName(CLASS_LNAME_Rotation);
    private static final String CLASS_LNAME_SpatialRelationship = "SpatialRelationship";
    private static final String CLASS_SpatialRelationship = nsName(CLASS_LNAME_SpatialRelationship);
    private static final String CLASS_LNAME_Rotation2D = "Rotation2D";
    private static final String CLASS_Rotation2D = nsName(CLASS_LNAME_Rotation2D);
    private static final String CLASS_LNAME_Rotation3D = "Rotation3D";
    private static final String CLASS_Rotation3D = nsName(CLASS_LNAME_Rotation3D);
    private static final String CLASS_LNAME_Translation2D = "Translation2D";
    private static final String CLASS_Translation2D = nsName(CLASS_LNAME_Translation2D);
    private static final String CLASS_LNAME_Translation3D = "Translation3D";
    private static final String CLASS_Translation3D = nsName(CLASS_LNAME_Translation3D);

    /**
     * Local and namespace names of RDF(S) properties as strings 
     */
    private static final String PROP_LNAME_rotation = "rotation";
    private static final String PROP_rotation = nsName(PROP_LNAME_rotation);
    private static final String PROP_LNAME_sourceCoordinateSystem = "sourceCoordinateSystem";
    private static final String PROP_sourceCoordinateSystem = nsName(PROP_LNAME_sourceCoordinateSystem);
    private static final String PROP_LNAME_pose = "pose";
    private static final String PROP_pose = nsName(PROP_LNAME_pose);
    private static final String PROP_LNAME_spatialProperty = "spatialProperty";
    private static final String PROP_spatialProperty = nsName(PROP_LNAME_spatialProperty);
    private static final String PROP_LNAME_projection = "projection";
    private static final String PROP_projection = nsName(PROP_LNAME_projection);
    private static final String PROP_LNAME_translation = "translation";
    private static final String PROP_translation = nsName(PROP_LNAME_translation);
    private static final String PROP_LNAME_targetCoordinateSystem = "targetCoordinateSystem";
    private static final String PROP_targetCoordinateSystem = nsName(PROP_LNAME_targetCoordinateSystem);
    private static final String PROP_LNAME_scaling = "scaling";
    private static final String PROP_scaling = nsName(PROP_LNAME_scaling);
    private static final String PROP_LNAME_coordinateSystem = "coordinateSystem";
    private static final String PROP_coordinateSystem = nsName(PROP_LNAME_coordinateSystem);
    private static final String PROP_LNAME_spatialRelationship = "spatialRelationship";
    private static final String PROP_spatialRelationship = nsName(PROP_LNAME_spatialRelationship);

 
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