/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package nzo;


import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nzo.entity.Video;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


@Stateless
@LocalBean
@Path("video")
public class RestVideo {
    
    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves representation of an instance of helloWorld.RestUsers
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("{id}")
    public Video getVideo (@PathParam("id") Integer id) {
        return em.find(Video.class, id);
    }
    
    @GET
    @Produces("application/json")
    @Path("/findall")
    public List<Video> getAllVideo () {
        return em.createNamedQuery("Video.findAll").getResultList();
    }
    
    @GET
    @Produces("application/json")
    @Path("/myvideos/{id}")
    public List<Video> getMyVideos (@PathParam("id") Integer id) {
        return em.createNamedQuery("Video.findByIdUser").setParameter("idUser", id).getResultList();
    }
    
    @DELETE
    @Path("{id}")
    public void deleteVideo (@PathParam("id") String id) {
        try {
            em.remove(em.find(Video.class, id));
            
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
    
    @PUT
    @Consumes("application/json")
    @Produces("text/plain")
    public Response UpdateVideo (Video val) {
        
        try {
            em.merge(val);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        return Response.status(201).entity("ok").build();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public Response CreateVideo (Video val) {
        
        try {
            em.persist(val);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        return Response.status(201).entity("ok").build();
    }
    
}
