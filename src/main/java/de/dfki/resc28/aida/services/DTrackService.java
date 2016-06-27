/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.services;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.aida.vocabularies.LDP;
import de.dfki.resc28.aida.vocabularies.MATHS;
import de.dfki.resc28.aida.vocabularies.SPATIAL;
import de.dfki.resc28.aida.vocabularies.VOM;
import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.art4j.targets.Body;

/**
 * @author resc01
 *
 */
@Path("")
public class DTrackService 
{
	@GET
	@Path("bodies")
	@Produces("text/turtle")
	public Response getBodies()
	{
		DTrackSDK tracker = DTrackSDK.getInstance();
		
		while (!tracker.receive())
			System.out.println("Waiting for data frames!");
		
		final Model bodyContainerModel =  ModelFactory.createDefaultModel();
		bodyContainerModel.setNsPrefixes(ART.NAMESPACE);
		bodyContainerModel.setNsPrefixes(SPATIAL.NAMESPACE);
		bodyContainerModel.setNsPrefixes(VOM.NAMESPACE);
		bodyContainerModel.setNsPrefixes(MATHS.NAMESPACE);		
		
		Resource bodyContainer = bodyContainerModel.createResource(fRequestUriInfo.getRequestUri().toString());
		bodyContainerModel.add(bodyContainer, RDF.type, LDP.Container);
		bodyContainerModel.add(bodyContainer, LDP.hasMemberRelation, SPATIAL.coordinateSystem);
		
		Resource body; 
		for (int i=0; i< tracker.getNumBody(); i++)
		{
			body = bodyContainerModel.createResource(fRequestUriInfo.getRequestUri().toString() + "/" + i);
			bodyContainerModel.add(body, RDF.type, ART.TreeTarget);
			bodyContainerModel.add(bodyContainer, LDP.contains, body);
		}
		
		StreamingOutput out = new StreamingOutput() 
		{
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				RDFDataMgr.write(output, bodyContainerModel, Lang.TURTLE) ;
			}
		};
		
		return Response.ok(out)
					   .build();
	}

	@GET
	@Path("bodies/{bodyID: \\d+}")
	@Produces("text/turtle")
	public Response getBody( @PathParam("id") int bodyID,
							 @HeaderParam("accept") final String accept)
	{
		
		DTrackSDK tracker = DTrackSDK.getInstance();
		
		while (!tracker.receive())
			System.out.println("Waiting for data frames!");
		
		if (bodyID > (tracker.getNumBody() - 1))
			throw new WebApplicationException("Body not available.", Status.NOT_FOUND);
		
		final Model body = getBodyModel(fRequestUriInfo.getRequestUri().toString(), tracker.getBody(bodyID));
			
		StreamingOutput out = new StreamingOutput() 
		{
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				RDFDataMgr.write(output, body, Lang.TURTLE) ;
			}
		};
		
		return Response.ok(out)
					   .build();
	}
	


	private Model getBodyModel(String bodyURI, Body artBody)
	{
		Model bodyModel = ModelFactory.createDefaultModel();
		
		bodyModel.setNsPrefixes(ART.NAMESPACE);
		bodyModel.setNsPrefixes(SPATIAL.NAMESPACE);
		bodyModel.setNsPrefixes(VOM.NAMESPACE);
		bodyModel.setNsPrefixes(MATHS.NAMESPACE);
		
		Resource body = bodyModel.createResource(bodyURI);
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
		
		return bodyModel;
	}
	
	
	
	
	@Context HttpServletRequest fRequest;
	@Context protected ServletContext fContext;
	@Context protected HttpHeaders fRequestHeaders;
	@Context protected UriInfo fRequestUriInfo;	
}
