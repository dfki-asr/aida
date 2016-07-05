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
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.aida.vocabularies.LDP;
import de.dfki.resc28.aida.vocabularies.MATHS;
import de.dfki.resc28.aida.vocabularies.SPATIAL;
import de.dfki.resc28.aida.vocabularies.VOM;
import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.art4j.targets.Body;
import de.dfki.resc28.flapjack.resources.IResource;
import de.dfki.resc28.flapjack.resources.Resource;
import de.dfki.resc28.igraphstore.IGraphStore;

public class TreeTarget extends Resource implements IResource 
{

	public TreeTarget(String resourceURI, IGraphStore graphStore) 
	{
		super(resourceURI, graphStore);
		this.fRDFType = ART.TreeTarget ;
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
		final Model treeTargetModel = fGraphStore.getNamedGraph(fURI);
		org.apache.jena.rdf.model.Resource treeTarget = treeTargetModel.getResource(fURI);
		int bodyID = treeTargetModel.listObjectsOfProperty(ART.bodyID).next().asLiteral().getInt();
		
		DTrackSDK tracker = DTrackSDK.getInstance();
		
		while (!tracker.receive())
			System.out.println("Waiting for data frames!");
		
		Body artBody = tracker.getBody(bodyID);
		
		org.apache.jena.rdf.model.Resource sr = treeTargetModel.createResource();
		org.apache.jena.rdf.model.Resource sourceCS = treeTargetModel.createResource();
		org.apache.jena.rdf.model.Resource targetCS = treeTargetModel.createResource();
		org.apache.jena.rdf.model.Resource translation = treeTargetModel.createResource();
		org.apache.jena.rdf.model.Resource vec3 = treeTargetModel.createResource();
		org.apache.jena.rdf.model.Resource rotation = treeTargetModel.createResource();
		org.apache.jena.rdf.model.Resource mat3 = treeTargetModel.createResource();
		
		treeTargetModel.add(sourceCS, RDF.type, MATHS.CoordinateSystem);
		treeTargetModel.add(targetCS, RDF.type, MATHS.CoordinateSystem);
		
		treeTargetModel.add(treeTarget, RDF.type, ART.Target);
		treeTargetModel.add(treeTarget, SPATIAL.spatialRelationship, sr);
		
		treeTargetModel.add(sr, RDF.type,  SPATIAL.SpatialRelationship);
		treeTargetModel.add(sr, SPATIAL.sourceCoordinateSystem, sourceCS);
		treeTargetModel.add(sr, SPATIAL.targetCoordinateSystem, targetCS);
		treeTargetModel.add(sr, SPATIAL.translation, translation);
		treeTargetModel.add(sr, SPATIAL.rotation, rotation);
		
		treeTargetModel.add(translation, RDF.type, SPATIAL.Translation3D);
		treeTargetModel.add(translation, VOM.quantityValue, vec3);

		treeTargetModel.add(vec3, RDF.type, MATHS.Vector3D);
		treeTargetModel.add(vec3, MATHS.x, treeTargetModel.createTypedLiteral(artBody.getLocation()[0]));
		treeTargetModel.add(vec3, MATHS.y, treeTargetModel.createTypedLiteral(artBody.getLocation()[1]));
		treeTargetModel.add(vec3, MATHS.z, treeTargetModel.createTypedLiteral(artBody.getLocation()[2]));
		
		treeTargetModel.add(rotation, RDF.type, SPATIAL.Rotation3D);
		treeTargetModel.add(rotation, VOM.quantityValue, mat3);
		
		treeTargetModel.add(mat3, RDF.type, MATHS.Matrix3D);
		treeTargetModel.add(mat3, MATHS.a11, treeTargetModel.createTypedLiteral(artBody.getRotation()[0]));
		treeTargetModel.add(mat3, MATHS.a12, treeTargetModel.createTypedLiteral(artBody.getRotation()[1]));
		treeTargetModel.add(mat3, MATHS.a13, treeTargetModel.createTypedLiteral(artBody.getRotation()[2]));
		treeTargetModel.add(mat3, MATHS.a21, treeTargetModel.createTypedLiteral(artBody.getRotation()[3]));
		treeTargetModel.add(mat3, MATHS.a22, treeTargetModel.createTypedLiteral(artBody.getRotation()[4]));
		treeTargetModel.add(mat3, MATHS.a23, treeTargetModel.createTypedLiteral(artBody.getRotation()[5]));
		treeTargetModel.add(mat3, MATHS.a31, treeTargetModel.createTypedLiteral(artBody.getRotation()[6]));
		treeTargetModel.add(mat3, MATHS.a32, treeTargetModel.createTypedLiteral(artBody.getRotation()[7]));
		treeTargetModel.add(mat3, MATHS.a33, treeTargetModel.createTypedLiteral(artBody.getRotation()[8]));
		
		StreamingOutput out = new StreamingOutput() 
		{
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				RDFDataMgr.write(output, treeTargetModel, RDFDataMgr.determineLang(null, contentType, null)) ;
			}
		};
		
		return Response.ok(out)
					   .type(contentType)
					   .build();
	}
}
