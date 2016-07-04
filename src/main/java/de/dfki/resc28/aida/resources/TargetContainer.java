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

public class TargetContainer extends Container implements IResource {

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
		
		final Model bodyContainerModel =  ModelFactory.createDefaultModel();
		bodyContainerModel.setNsPrefixes(ART.NAMESPACE);
		bodyContainerModel.setNsPrefixes(SPATIAL.NAMESPACE);
		bodyContainerModel.setNsPrefixes(VOM.NAMESPACE);
		bodyContainerModel.setNsPrefixes(MATHS.NAMESPACE);
		bodyContainerModel.setNsPrefixes(LDP.NAMESPACE);		
		
		Resource bodyContainer = bodyContainerModel.createResource(fURI);
		bodyContainerModel.add(bodyContainer, RDF.type, LDP.Container);
		bodyContainerModel.add(bodyContainer, LDP.hasMemberRelation, SPATIAL.coordinateSystem);
		
		for (int i=0; i< tracker.getNumBody(); i++)
		{
			Body artBody = tracker.getBody(i);
			
			Model bodyModel = ModelFactory.createDefaultModel();
			
			bodyModel.setNsPrefixes(ART.NAMESPACE);
			bodyModel.setNsPrefixes(SPATIAL.NAMESPACE);
			bodyModel.setNsPrefixes(VOM.NAMESPACE);
			bodyModel.setNsPrefixes(MATHS.NAMESPACE);
			
			Resource body = bodyModel.createResource(fURI + "/" + i);
			Resource sr = bodyModel.createResource();
			Resource sourceCS = bodyModel.createResource();
			Resource targetCS = bodyModel.createResource();
			Resource translation = bodyModel.createResource();
			Resource vec3 = bodyModel.createResource();
			Resource rotation = bodyModel.createResource();
			Resource mat3 = bodyModel.createResource();
			
			bodyModel.add(sourceCS, RDF.type, MATHS.CoordinateSystem);
			bodyModel.add(targetCS, RDF.type, MATHS.CoordinateSystem);
			
			bodyModel.add(body, RDF.type, ART.Target);
			bodyModel.add(body, SPATIAL.spatialRelationship, sr);
			
			bodyModel.add(sr, RDF.type,  SPATIAL.SpatialRelationship);
			bodyModel.add(sr, SPATIAL.sourceCoordinateSystem, sourceCS);
			bodyModel.add(sr, SPATIAL.targetCoordinateSystem, targetCS);
			bodyModel.add(sr, SPATIAL.translation, translation);
			bodyModel.add(sr, SPATIAL.rotation, rotation);
			
			bodyModel.add(translation, RDF.type, SPATIAL.Translation3D);
			bodyModel.add(translation, VOM.quantityValue, vec3);
	
			bodyModel.add(vec3, RDF.type, MATHS.Vector3D);
			bodyModel.add(vec3, MATHS.x, bodyModel.createTypedLiteral(artBody.getLocation()[0]));
			bodyModel.add(vec3, MATHS.y, bodyModel.createTypedLiteral(artBody.getLocation()[1]));
			bodyModel.add(vec3, MATHS.z, bodyModel.createTypedLiteral(artBody.getLocation()[2]));
			
			bodyModel.add(rotation, RDF.type, SPATIAL.Rotation3D);
			bodyModel.add(rotation, VOM.quantityValue, mat3);
			
			bodyModel.add(mat3, RDF.type, MATHS.Matrix3D);
			bodyModel.add(mat3, MATHS.a11, bodyModel.createTypedLiteral(artBody.getRotation()[0]));
			bodyModel.add(mat3, MATHS.a12, bodyModel.createTypedLiteral(artBody.getRotation()[1]));
			bodyModel.add(mat3, MATHS.a13, bodyModel.createTypedLiteral(artBody.getRotation()[2]));
			bodyModel.add(mat3, MATHS.a21, bodyModel.createTypedLiteral(artBody.getRotation()[3]));
			bodyModel.add(mat3, MATHS.a22, bodyModel.createTypedLiteral(artBody.getRotation()[4]));
			bodyModel.add(mat3, MATHS.a23, bodyModel.createTypedLiteral(artBody.getRotation()[5]));
			bodyModel.add(mat3, MATHS.a31, bodyModel.createTypedLiteral(artBody.getRotation()[6]));
			bodyModel.add(mat3, MATHS.a32, bodyModel.createTypedLiteral(artBody.getRotation()[7]));
			bodyModel.add(mat3, MATHS.a33, bodyModel.createTypedLiteral(artBody.getRotation()[8]));

					
			bodyContainerModel.add(bodyContainer, LDP.contains, sr);
			bodyContainerModel.add(bodyModel);
		}
			
		StreamingOutput out = new StreamingOutput() 
		{
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				RDFDataMgr.write(output, bodyContainerModel, RDFDataMgr.determineLang(null, contentType, null)) ;
			}
		};
		
		return Response.ok(out)
					   .type(contentType)
					   .build();
	}
}
