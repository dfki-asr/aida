/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.dfki.resc28.aida.services.DTrackActionProvider;
import de.dfki.resc28.aida.services.DTrackService;
import de.dfki.resc28.igraphstore.jena.FusekiGraphStore;


/**
 * @author resc01
 *
 */
@ApplicationPath("/")
public class Server extends Application 
{
	public static String dataEndpoint = "http://localhost:3030/DTrack2/data";
	public static String queryEndpoint = "http://localhost:3030/DTrack2/sparql";
	
	@Override
    public Set<Object> getSingletons() 
    {
		DTrackActionProvider foo = new DTrackActionProvider(new FusekiGraphStore(dataEndpoint, queryEndpoint));
		DTrackService bar = new DTrackService(new FusekiGraphStore(dataEndpoint, queryEndpoint));
        return new HashSet<Object>(Arrays.asList(foo, bar));
    }

}
