package org.acme.personaldata;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;


@Path("/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Timed(name = "ApplicationTimer", description = "A measure of each request.", unit = MetricUnits.MILLISECONDS)
@Traced
public class ApplicationResource {

    @POST
    public ApplicationEntity createApplication(CreateApplicationDTO applicationDTO)
    {
        ApplicationEntity applicationEntity = applicationDTO.createEntity();
        applicationEntity.persistAndFlush();
        return applicationEntity;
    }

    @PUT
    @Path("{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public ApplicationEntity updateApplication(@PathParam("applicationId") UUID applicationId, @Valid UpdateApplicationDTO dto)
    {
        ApplicationEntity applicationEntity = ApplicationEntity.findById(applicationId);
        if (null == applicationEntity)
            return null;

        dto.updateEntity(applicationEntity);
        applicationEntity.persistAndFlush();
        return applicationEntity;
    }

    @POST
    @Path("{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/results")
    public ResultEntity createResult(@Valid CreateResultDTO resultDTO, @PathParam("applicationId") UUID applicationId)
    {
        ApplicationEntity application = ApplicationEntity.findById(applicationId);
        ResultEntity resultEntity = resultDTO.createEntity(application);

        application.persistAndFlush();

        return resultEntity;
    }

    @PUT
    @Path("{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/results/{resultId: \\d+}")
    public ResultEntity updateResult(@PathParam("applicationId") UUID applicationId, @PathParam("resultId") Long resultId, UpdateResultDTO dto)
    {
        ResultEntity resultEntity = ResultEntity.findById(resultId);
        if (resultEntity == null || !resultEntity.application.id.equals(applicationId))

        dto.updateEntity(resultEntity);
        resultEntity.persist();

        return resultEntity;
    }

    @GET
    @Path("{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public ApplicationEntity getApplication(@PathParam("applicationId") UUID applicationId)
    {
        return ApplicationEntity.findById(applicationId);
    }

    @GET
    @Path("{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/results")
    public List<ResultEntity> getApplicationResults(@PathParam("applicationId") UUID applicationId)
    {
        ApplicationEntity entity = ApplicationEntity.findById(applicationId);
        if (entity == null)
            return null;

        return entity.results;
    }

    @GET
    @Path("{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/results/{resultId: \\d+}")
    public ResultEntity getApplicationResults(@PathParam("applicationId") UUID applicationId, @PathParam("resultId") Long resultId)
    {
        ResultEntity entity = ResultEntity.findById(resultId);
        if (entity == null || !entity.application.id.equals(applicationId))
            return null;

        return entity;
    }

    @DELETE
    @Path("{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public Boolean removeApplication(@PathParam("applicationId") UUID applicationId)
    {
        return ApplicationEntity.deleteById(applicationId);
    }

    @DELETE
    @Path("{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/results/{resultId: \\d+}")
    public boolean removeApplicationResult(@PathParam("applicationId") UUID applicationId, @PathParam("resultId") Long resultId)
    {
        ResultEntity entity = ResultEntity.findById(resultId);
        if (entity == null || !entity.application.id.equals(applicationId))
            return false;

        return ResultEntity.deleteById(entity.id);
    }

    @GET
    public List<ApplicationEntity> getApplications(
            @DefaultValue ("0") @QueryParam("page") Integer page,
            @DefaultValue ("25") @QueryParam("perPage") Integer perPage,
            @QueryParam("IDCardNumber") String IDCardNumber
    ) {
        return IDCardNumber == null || IDCardNumber.isEmpty() ? ApplicationEntity.findAll().page(page, perPage).list()
                : ApplicationEntity.getByIDCardNumber(IDCardNumber, page, perPage);
    }
}
