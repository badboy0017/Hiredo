/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package nzo;


import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nzo.entity.Postule;
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
import nzo.entity.Cv;
import nzo.entity.Job;
import nzo.entity.Resume;
import nzo.entity.Users;
import nzo.entity.Video;


@Stateless
@LocalBean
@Path("postule")
public class RestPostule {
    
    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves representation of an instance of helloWorld.RestUsers
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("{id}")
    public Postule getPostule (@PathParam("id") Integer id) {
        return em.find(Postule.class, id);
    }
    
    @GET
    @Produces("application/json")
    @Path("/findall")
    public List<Postule> getAllPostule () {
        return em.createNamedQuery("Postule.findAll").getResultList();
    }
    
    @GET
    @Produces("application/json")
    @Path("/users/{id}")
    public List<Postule> getUsersByPostule(@PathParam("id") Integer id) {
         return em.createNamedQuery("Postule.findByIdJob").setParameter("idJob", id).getResultList();
    }
    
    @GET
    @Produces("application/json")
    @Path("/detail/{id}")
    public PostuleDetail getPostuleDetails(@PathParam("id") Integer id) {
        PostuleDetail pd = new PostuleDetail();
        Postule pos = (Postule) em.createNamedQuery("Postule.findById").setParameter("id", id).getSingleResult();
        
        pd.setUser((Users) em.createNamedQuery("Users.findById").setParameter("id", pos.getIdUser()).getSingleResult());
        pd.setCv((Cv) em.createNamedQuery("Cv.findById").setParameter("id", Integer.parseInt(pos.getCv())).getSingleResult());
        pd.setLm((Resume) em.createNamedQuery("Resume.findById").setParameter("id", Integer.parseInt(pos.getResume())).getSingleResult());
        pd.setVideo((Video) em.createNamedQuery("Video.findById").setParameter("id", Integer.parseInt(pos.getVideo())).getSingleResult());
        pd.setPostuleDate((Date) pos.getDatePostule());
        pd.setJob((Job)em.createNamedQuery("Job.findById").setParameter("id", pos.getIdJob()).getSingleResult());
        return pd;
    }
    
    @DELETE
    @Path("{id}")
    public void deletePostule (@PathParam("id") String id) {
        try {
            em.remove(em.find(Postule.class, id));
            
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
    
    @PUT
    @Consumes("application/json")
    @Produces("text/plain")
    public Response UpdatePostule(Postule val) {
        
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
    public Response CreatePostule (Postule val) {
        
        try {
            em.persist(val);
        } catch (Exception ex) {
            //throw new EJBException(ex);
            return Response.status(500).entity("Error\nClass: " + ex.getClass() + "\nCause: " + ex.getCause() + "\nMessage: " + ex.getMessage()).build();
        }
        return Response.status(201).entity("ok").build();
    }
}
