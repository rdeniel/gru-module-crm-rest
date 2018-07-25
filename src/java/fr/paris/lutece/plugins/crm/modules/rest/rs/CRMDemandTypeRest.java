/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.crm.modules.rest.rs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.crm.business.demand.DemandType;
import fr.paris.lutece.plugins.crm.business.demand.DemandTypeHome;
import fr.paris.lutece.plugins.crm.modules.rest.util.constants.CRMRestConstants;
import fr.paris.lutece.plugins.crm.service.CRMPlugin;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * CRM Demand Type Rest Service
 */
@Path( RestConstants.BASE_PATH + CRMPlugin.PLUGIN_NAME + CRMRestConstants.PATH_DEMAND_TYPES )
public class CRMDemandTypeRest
{
    private ObjectMapper _objectMapper = new ObjectMapper( );

    /**
     * Get all the demand types
     * 
     * @return a JSON of the list of the demand types
     */
    @POST
    @Path( CRMRestConstants.PATH_GET_DEMAND + CRMRestConstants.PATH_GET_ALL_DEMANDS )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getDemandTypes( )
    {

        List<DemandType> listDemandTypes = DemandTypeHome.findAll( );
        try
        {
            String strResult = _objectMapper.writeValueAsString( listDemandTypes );
            return Response.ok( strResult ).build( );
        }
        catch( JsonProcessingException e )
        {
            return Response.serverError( ).build( );
        }

    }
}
