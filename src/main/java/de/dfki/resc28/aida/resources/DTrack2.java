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
			
		StreamingOutput out = new StreamingOutput() 
		{
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				RDFDataMgr.write(output, description, RDFDataMgr.determineLang(null, contentType, null)) ;
			}
		};
		
		return Response.ok(out)
					   .type(contentType)
					   .build();
	}
}
