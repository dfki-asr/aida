/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.services;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import de.dfki.resc28.aida.resources.DTrackResourceManager;
import de.dfki.resc28.flapjack.resources.IResource;
import de.dfki.resc28.igraphstore.Constants;
import de.dfki.resc28.igraphstore.IGraphStore;

/**
 * @author resc01
 *
 */
@Path("{path:.*}")
public class DTrackService 
{
	public DTrackService(IGraphStore graphStore)
	{
		this.fGraphStore = graphStore;
	}
	
	@GET
	@Produces({ Constants.CT_TEXT_TURTLE, Constants.CT_APPLICATION_RDFXML, Constants.CT_APPLICATION_XTURTLE, Constants.CT_APPLICATION_JSON, Constants.CT_APPLICATION_LD_JSON })
	public Response get(@HeaderParam(HttpHeaders.ACCEPT) final String acceptType)
	{	
		IResource r = getResourceManager().get(getCanonicalURL(fRequestUrl.getRequestUri()));
		
		if (r == null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
    	else if (!(r instanceof IResource))
    	{
    		return Response.status(Status.BAD_REQUEST).build();
    	}
		else if (!(r.getAllowedMethods().contains(HttpMethod.GET)))
		{
			return Response.status(Status.METHOD_NOT_ALLOWED).build();
		}

		return r.read(acceptType);
	}
	

	protected DTrackResourceManager getResourceManager() 
	{
		return new DTrackResourceManager(fGraphStore);
	}
	
    protected String getCanonicalURL (URI url)
    {
    	// @TODO: remove trailing '/'
    	return url.toString();
    }
	
	
    protected IGraphStore fGraphStore;
	@Context protected UriInfo fRequestUrl;	
}
