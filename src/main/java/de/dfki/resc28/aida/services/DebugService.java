/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.services;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Dmitri Rubinstein
 */
@Path("/debug")
public class DebugService {

    @GET
    @Produces("text/plain")
    public String get() {
        StringBuilder b = new StringBuilder();
        b.append("Servlet Request Server Name: ").append(fRequest.getServerName()).append('\n');
        b.append("Servlet Request Server Port: ").append(fRequest.getServerPort()).append('\n');

        b.append("Base URI: ").append(fRequestUrl.getBaseUri()).append('\n');
        b.append("Path: ").append(fRequestUrl.getPath()).append('\n');
        b.append("Request URI: ").append(fRequestUrl.getRequestUri()).append('\n');
        b.append("Headers:\n");
        MultivaluedMap<String, String> headers = fRequestHeaders.getRequestHeaders();
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            for (String value : header.getValue()) {
                b.append("  ").append(header.getKey()).append(": ").append(value).append('\n');
            }
        }
        return b.toString();
    }

    @Context
    protected HttpServletRequest fRequest;
    @Context
    protected ServletContext fContext;
    @Context
    protected HttpHeaders fRequestHeaders;
    @Context
    protected UriInfo fRequestUrl;
}
