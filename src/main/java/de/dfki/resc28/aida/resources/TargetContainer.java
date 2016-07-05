/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.aida.vocabularies.LDP;
import de.dfki.resc28.aida.vocabularies.MATHS;
import de.dfki.resc28.aida.vocabularies.SPATIAL;
import de.dfki.resc28.aida.vocabularies.VOM;
import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.art4j.targets.Body;
import de.dfki.resc28.flapjack.resources.Container;
import de.dfki.resc28.flapjack.resources.IResource;
import de.dfki.resc28.igraphstore.IGraphStore;

public class TargetContainer extends Container implements IResource 
{

	public TargetContainer(String resourceURI, IGraphStore graphStore) 
	{
		super(resourceURI, graphStore);
		this.fRDFType = ART.TargetContainer ;
	}
	
	public Set<String> getAllowedMethods() 
    {
		Set<String> allow = super.getAllowedMethods();
		allow.clear();
		allow.add(HttpMethod.GET);
	    return allow;
    }
	
	public Response read(final String contentType)
	{
		DTrackSDK tracker = DTrackSDK.getInstance();
		
		while (!tracker.receive())
			System.out.println("Waiting for data frames!");
		
		final Model targetContainerModel =  ModelFactory.createDefaultModel();
		targetContainerModel.setNsPrefixes(ART.NAMESPACE);
		targetContainerModel.setNsPrefixes(LDP.NAMESPACE);
		
		
		Resource targetContainer = targetContainerModel.createResource(fURI);
		targetContainerModel.add(targetContainer, RDF.type, LDP.Container);

		
		for (int i=0; i< tracker.getNumBody(); i++)
		{
			Body artBody = tracker.getBody(i);

			Model bodyModel = ModelFactory.createDefaultModel();
			bodyModel.setNsPrefixes(ART.NAMESPACE);
			bodyModel.setNsPrefixes(SPATIAL.NAMESPACE);
			bodyModel.setNsPrefixes(VOM.NAMESPACE);
			bodyModel.setNsPrefixes(MATHS.NAMESPACE);
			bodyModel.setNsPrefix("xsd", XSD.NS);
			Resource body = bodyModel.createResource(fURI + "/" + artBody.getID());
			bodyModel.add(body, RDF.type, ART.TreeTarget);
			bodyModel.add(body, ART.bodyID, bodyModel.createTypedLiteral(artBody.getID()));
			fGraphStore.createNamedGraph(body.getURI().toString(), bodyModel);

			targetContainerModel.add(targetContainer, LDP.contains, body);
			targetContainerModel.add(body, RDF.type, ART.TreeTarget);
		}
			
		StreamingOutput out = new StreamingOutput() 
		{
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				RDFDataMgr.write(output, targetContainerModel, RDFDataMgr.determineLang(null, contentType, null)) ;
			}
		};
		
		return Response.ok(out)
					   .type(contentType)
					   .build();
	}
}
