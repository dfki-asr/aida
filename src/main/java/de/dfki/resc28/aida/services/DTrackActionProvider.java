/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.services;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.dfki.resc28.aida.actions.DTrackActionController;
import de.dfki.resc28.aida.resources.DTrackResourceManager;
import de.dfki.resc28.flapjack.resources.IContainer;
import de.dfki.resc28.flapjack.resources.IResource;
import de.dfki.resc28.flapjack.resources.IResourceManager;
import de.dfki.resc28.igraphstore.Constants;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.sodalite.actions.IActionController;
import de.dfki.resc28.sodalite.services.ActionProvider;

/**
 * @author resc01
 *
 */
@Path("/")
public class DTrackActionProvider extends ActionProvider
{

	public DTrackActionProvider(IGraphStore graphStore) 
	{
		super(graphStore);
	}

	@Override
	protected IActionController getActionController() 
	{
		return new DTrackActionController(this.fGraphStore);
	}

	@Override
	protected IResourceManager getResourceManager() 
	{
		return new DTrackResourceManager(this.fGraphStore);
	}

	@Override
	protected IContainer getRootContainer() 
	{
		return null;
	}
	
	@Path("{a: targets|coordinateSystems}")
	@GET
	@Produces({ Constants.CT_TEXT_TURTLE, Constants.CT_APPLICATION_RDFXML, Constants.CT_APPLICATION_XTURTLE, Constants.CT_APPLICATION_JSON, Constants.CT_APPLICATION_JSON_LD })
	public Response getSubResource( @HeaderParam(HttpHeaders.ACCEPT) String acceptType )
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
	
	@Path("targets/{id}")
	@GET
	@Produces({ Constants.CT_TEXT_TURTLE, Constants.CT_APPLICATION_RDFXML, Constants.CT_APPLICATION_XTURTLE, Constants.CT_APPLICATION_JSON, Constants.CT_APPLICATION_JSON_LD })
	public Response getTargetSubResource( @HeaderParam(HttpHeaders.ACCEPT) String acceptType )
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
}
