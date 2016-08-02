/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.resources;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.flapjack.resources.IResource;
import de.dfki.resc28.flapjack.resources.Resource;
import de.dfki.resc28.igraphstore.IGraphStore;
import javax.ws.rs.core.Response.Status;
import org.apache.jena.riot.Lang;

// TODO: Implement ActionableResource subclass
public class DTrack2 extends Resource implements IResource
{

	public DTrack2(String resourceURI, IGraphStore graphStore) 
	{
		super(resourceURI, graphStore);
		this.fRDFType = ART.DTrack2;
		
		if (this.fGraphStore.getDefaultGraph().isEmpty())
		{
			fGraphStore.replaceDefaultGraph(fGraphStore.getNamedGraph(fURI + "/model/initial"));
		}
	}
	
	public Response read(final String contentType)
	{
		final Model description = fGraphStore.getDefaultGraph();
                final Lang lang = RDFDataMgr.determineLang(null, contentType, null);
                System.err.format("Content type is %s, lang is %s%n", contentType, lang);
                if (lang == null) {
                    throw new WebApplicationException("Could not convert content type "+contentType+" to RDF language", Status.BAD_REQUEST);
                }
		StreamingOutput out = new StreamingOutput()
		{
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				RDFDataMgr.write(output, description, lang);
			}
		};

		return Response.ok(out)
					   .type(contentType)
					   .build();
	}
}
