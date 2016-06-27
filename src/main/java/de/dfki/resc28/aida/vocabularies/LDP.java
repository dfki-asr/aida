/**
 * This file has been automatically generated using Grover (https://github.com/rmrschub/grover).
 * It contains static constants for the terms in the  vocabulary.
 */
package de.dfki.resc28.aida.vocabularies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;

public final class LDP
{
  public static final String PREFIX = "LDP";
  public static final PrefixMapping NAMESPACE = PrefixMapping.Factory.create().setNsPrefix(PREFIX, CONSTANTS.NS);

  /** 
   * Classes as org.apache.jena.rdf.model.Resource
   */
  public static final Resource BasicContainer = resource(CONSTANTS.CLASS_BasicContainer);
  public static final Resource RDFSource = resource(CONSTANTS.CLASS_RDFSource);
  public static final Resource Page = resource(CONSTANTS.CLASS_Page);
  public static final Resource Container = resource(CONSTANTS.CLASS_Container);
  public static final Resource IndirectContainer = resource(CONSTANTS.CLASS_IndirectContainer);
  public static final Resource NonRDFSource = resource(CONSTANTS.CLASS_NonRDFSource);
  public static final Resource DirectContainer = resource(CONSTANTS.CLASS_DirectContainer);
  public static final Resource PageSortCriterion = resource(CONSTANTS.CLASS_PageSortCriterion);
  public static final Resource Resource = resource(CONSTANTS.CLASS_Resource);

  /** 
   * Properties as org.apache.jena.rdf.model.Property
   */
  public static final Property constrainedBy = property(CONSTANTS.PROP_constrainedBy);
  public static final Property pageSortCriteria = property(CONSTANTS.PROP_pageSortCriteria);
  public static final Property hasMemberRelation = property(CONSTANTS.PROP_hasMemberRelation);
  public static final Property isMemberOfRelation = property(CONSTANTS.PROP_isMemberOfRelation);
  public static final Property pageSequence = property(CONSTANTS.PROP_pageSequence);
  public static final Property pageSortOrder = property(CONSTANTS.PROP_pageSortOrder);
  public static final Property pageSortPredicate = property(CONSTANTS.PROP_pageSortPredicate);
  public static final Property pageSortCollation = property(CONSTANTS.PROP_pageSortCollation);
  public static final Property contains = property(CONSTANTS.PROP_contains);
  public static final Property membershipResource = property(CONSTANTS.PROP_membershipResource);
  public static final Property member = property(CONSTANTS.PROP_member);
  public static final Property insertedContentRelation = property(CONSTANTS.PROP_insertedContentRelation);


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
    private static final String NS = "http://www.w3.org/ns/ldp#";

    /**
     * Local and namespace names of RDF(S) classes as strings 
     */
    private static final String CLASS_LNAME_BasicContainer = "BasicContainer";
    private static final String CLASS_BasicContainer = nsName(CLASS_LNAME_BasicContainer);
    private static final String CLASS_LNAME_RDFSource = "RDFSource";
    private static final String CLASS_RDFSource = nsName(CLASS_LNAME_RDFSource);
    private static final String CLASS_LNAME_Page = "Page";
    private static final String CLASS_Page = nsName(CLASS_LNAME_Page);
    private static final String CLASS_LNAME_Container = "Container";
    private static final String CLASS_Container = nsName(CLASS_LNAME_Container);
    private static final String CLASS_LNAME_IndirectContainer = "IndirectContainer";
    private static final String CLASS_IndirectContainer = nsName(CLASS_LNAME_IndirectContainer);
    private static final String CLASS_LNAME_NonRDFSource = "NonRDFSource";
    private static final String CLASS_NonRDFSource = nsName(CLASS_LNAME_NonRDFSource);
    private static final String CLASS_LNAME_DirectContainer = "DirectContainer";
    private static final String CLASS_DirectContainer = nsName(CLASS_LNAME_DirectContainer);
    private static final String CLASS_LNAME_PageSortCriterion = "PageSortCriterion";
    private static final String CLASS_PageSortCriterion = nsName(CLASS_LNAME_PageSortCriterion);
    private static final String CLASS_LNAME_Resource = "Resource";
    private static final String CLASS_Resource = nsName(CLASS_LNAME_Resource);

    /**
     * Local and namespace names of RDF(S) properties as strings 
     */
    private static final String PROP_LNAME_constrainedBy = "constrainedBy";
    private static final String PROP_constrainedBy = nsName(PROP_LNAME_constrainedBy);
    private static final String PROP_LNAME_pageSortCriteria = "pageSortCriteria";
    private static final String PROP_pageSortCriteria = nsName(PROP_LNAME_pageSortCriteria);
    private static final String PROP_LNAME_hasMemberRelation = "hasMemberRelation";
    private static final String PROP_hasMemberRelation = nsName(PROP_LNAME_hasMemberRelation);
    private static final String PROP_LNAME_isMemberOfRelation = "isMemberOfRelation";
    private static final String PROP_isMemberOfRelation = nsName(PROP_LNAME_isMemberOfRelation);
    private static final String PROP_LNAME_pageSequence = "pageSequence";
    private static final String PROP_pageSequence = nsName(PROP_LNAME_pageSequence);
    private static final String PROP_LNAME_pageSortOrder = "pageSortOrder";
    private static final String PROP_pageSortOrder = nsName(PROP_LNAME_pageSortOrder);
    private static final String PROP_LNAME_pageSortPredicate = "pageSortPredicate";
    private static final String PROP_pageSortPredicate = nsName(PROP_LNAME_pageSortPredicate);
    private static final String PROP_LNAME_pageSortCollation = "pageSortCollation";
    private static final String PROP_pageSortCollation = nsName(PROP_LNAME_pageSortCollation);
    private static final String PROP_LNAME_contains = "contains";
    private static final String PROP_contains = nsName(PROP_LNAME_contains);
    private static final String PROP_LNAME_membershipResource = "membershipResource";
    private static final String PROP_membershipResource = nsName(PROP_LNAME_membershipResource);
    private static final String PROP_LNAME_member = "member";
    private static final String PROP_member = nsName(PROP_LNAME_member);
    private static final String PROP_LNAME_insertedContentRelation = "insertedContentRelation";
    private static final String PROP_insertedContentRelation = nsName(PROP_LNAME_insertedContentRelation);

 
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