/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.resources;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.flapjack.resources.IResource;
import de.dfki.resc28.flapjack.resources.ResourceManager;
import de.dfki.resc28.igraphstore.IGraphStore;

public class DTrackResourceManager extends ResourceManager 
{

	public DTrackResourceManager(IGraphStore graphStore) 
	{
		super(graphStore);
	}

	public IResource get(String resourceURI) 
	{
		Model resourceModel = fGraphStore.getNamedGraph(resourceURI);
		
		if (resourceModel == null)
			return null;
		
		Resource r = resourceModel.getResource(resourceURI);
		
		if (r.hasProperty(RDF.type, ART.DTrack2))
		{
			return new DTrack2(resourceURI, fGraphStore);
		}
		else if (r.hasProperty(RDF.type, ART.TargetContainer))
		{
			return new TargetContainer(resourceURI, fGraphStore);
		}
		else if (r.hasProperty(RDF.type, ART.TreeTarget))
		{
			return new TreeTarget(resourceURI, fGraphStore);
		}
		else
		{
			return null;
		}
	}

}
