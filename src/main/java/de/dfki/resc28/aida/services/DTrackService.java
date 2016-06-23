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
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.art4j.targets.Body;

/**
 * @author resc01
 *
 */
@Path("")
public class DTrackService {
	
	

	@GET
	@Path("bodies")
	@Produces("text/turtle")
	public Response getBodies( @HeaderParam("accept") final String accept)
	{
		
		DTrackSDK tracker = DTrackSDK.getInstance();
		
		while (!tracker.receive())
			System.out.println("Waiting for data frames!");
		
		final Model body = tracker.getBody(0).getRDF(fRequestUriInfo.getRequestUri().toString());
		
//		String output = new StringBuilder().append("#Bodies: ")
//										   .append(tracker.getNumBody())
//										   .append(" , Timestamp: ")
//										   .append(tracker.getTimeStamp())
//										   .append(" , Framecounter: ")
//										   .append(tracker.getFrameCounter())
//										   .append("\nLoc: " + java.util.Arrays.toString(tt0.getLocation()))
//										   .append("\nRot: " + java.util.Arrays.toString(tt0.getRotation()))
//										   .toString();
		
		
		
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
	
	
	
	@Context HttpServletRequest fRequest;
	@Context protected ServletContext fContext;
	@Context protected HttpHeaders fRequestHeaders;
	@Context protected UriInfo fRequestUriInfo;	
}
