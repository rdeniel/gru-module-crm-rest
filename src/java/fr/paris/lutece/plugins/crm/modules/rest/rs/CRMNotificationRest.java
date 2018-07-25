/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.crm.business.demand.Demand;
import fr.paris.lutece.plugins.crm.business.demand.DemandFilter;
import fr.paris.lutece.plugins.crm.business.demand.DemandType;
import fr.paris.lutece.plugins.crm.business.user.CRMUser;
import fr.paris.lutece.plugins.crm.modules.rest.util.StringUtil;
import fr.paris.lutece.plugins.crm.modules.rest.util.constants.CRMRestConstants;
import fr.paris.lutece.plugins.crm.service.CRMPlugin;
import fr.paris.lutece.plugins.crm.service.CRMService;
import fr.paris.lutece.plugins.crm.service.demand.DemandService;
import fr.paris.lutece.plugins.crm.service.demand.DemandTypeService;
import fr.paris.lutece.plugins.crm.service.user.CRMUserService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;
import net.sf.json.JSONObject;

/**
 *
 * CRMNotificationRest
 *
 */
@Path( RestConstants.BASE_PATH + CRMPlugin.PLUGIN_NAME + CRMRestConstants.PATH_NOTIFY )
public class CRMNotificationRest
{
    /**
     * Notification for a demand
     * 
     * @param strIdDemand
     *            the id demand
     * @param strNotificationObject
     *            the notification object
     * @param strNotificationMessage
     *            the notification message
     * @param strNotificationSender
     *            the sender
     * @return the id demand
     */
    @POST
    @Path( CRMRestConstants.PATH_DEMAND )
    @Produces( MediaType.TEXT_PLAIN )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doNotify( @FormParam( CRMRestConstants.PARAMETER_ID_DEMAND ) String strIdDemand,
            @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_OBJECT ) String strNotificationObject,
            @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_MESSAGE ) String strNotificationMessage,
            @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_SENDER ) String strNotificationSender )
    {
        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) && StringUtils.isNotBlank( strNotificationObject ) )
        {
            String strObject = StringUtil.convertString( strNotificationObject );
            String strMessage = StringUtil.convertString( strNotificationMessage );
            String strSender = StringUtil.convertString( strNotificationSender );

            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService( ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                CRMService.getService( ).notify( nIdDemand, strObject, strMessage, strSender );
            }
            else
            {
                AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_DEMAND );
            }
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_MANDATORY_FIELDS );
        }

        return strIdDemand;
    }

    /**
     * notify a demand using Remote id and id demand Type
     *
     * @param nVersion
     *            the API version
     * @param strRemoteId
     *            the Remote Id
     * @param strIdDemandType
     *            the demand type id
     * @param strNotificationObject
     *            the notification object
     * @param strNotificationMessage
     *            the notification message
     * @param strNotificationSender
     *            the sender
     * @return the id demand
     */
    @POST
    @Path( CRMRestConstants.PATH_NOTIFY_DEMAND_V2 )
    @Produces( MediaType.TEXT_PLAIN )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doNotifyV2( @PathParam( CRMRestConstants.API_VERSION ) Integer nVersion,
            @FormParam( CRMRestConstants.PARAMETER_REMOTE_ID ) String strRemoteId,
            @FormParam( CRMRestConstants.PARAMETER_ID_DEMAND_TYPE ) String strIdDemandType,
            @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_OBJECT ) String strNotificationObject,
            @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_MESSAGE ) String strNotificationMessage,
            @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_SENDER ) String strNotificationSender )
    {

        String strIdDemand = CRMRestConstants.INVALID_ID;
        if ( nVersion == CRMRestConstants.VERSION_2 )
        {
            if ( StringUtils.isNotBlank( strRemoteId ) && StringUtils.isNumeric( strIdDemandType ) && StringUtils.isNotBlank( strNotificationObject ) )
            {
                String strObject = StringUtil.convertString( strNotificationObject );
                String strMessage = StringUtil.convertString( strNotificationMessage );
                String strSender = StringUtil.convertString( strNotificationSender );

                int nIdDemandType = Integer.parseInt( strIdDemandType );
                Demand demand = DemandService.getService( ).findByRemoteKey( strRemoteId, nIdDemandType );

                if ( demand != null )
                {
                    strIdDemand = Integer.toString( demand.getIdDemand( ) );
                    CRMService.getService( ).notify( demand.getIdDemand( ), strObject, strMessage, strSender );
                }
                else
                {
                    AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_DEMAND );
                }
            }
            else
            {
                AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_MANDATORY_FIELDS );
            }
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_API_VERSION );
        }

        return strIdDemand;
    }

    /**
     * Get the Json of the number notifications not read by Demand Type
     * 
     * @param request
     * @return the JSON of the number notifications not read by Demand Type
     */
    @GET
    @Path( CRMRestConstants.PATH_USER_NOTIFICATIONS )
    @Produces( MediaType.APPLICATION_JSON )
    public String getNumberNotificationsJson( @Context HttpServletRequest request )
    {
        String strJSON = StringUtils.EMPTY;
        DemandFilter filter = new DemandFilter( );
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( user != null )
        {

            CRMUserService crmUserService = CRMUserService.getService( );
            CRMUser crmUser = crmUserService.findByUserGuid( user.getName( ) );

            if ( crmUser != null )
            {

                filter.setIdCRMUser( crmUser.getIdCRMUser( ) );
                List<Demand> listDemand = DemandService.getService( ).findByFilter( filter );
                strJSON = getNumberNotifications( listDemand );

            }
            else
            {

                return StringUtils.EMPTY;
            }
        }

        return strJSON;
    }

    /**
     * Get the Json of the number notifications not read by Demand Type by user Guid The Api must be protected by signed request
     * 
     * @param request
     *            http servlet request
     * @param strGuid
     *            the user Guid
     * @return the JSON of the number notifications not read by Demand Type
     */
    @GET
    @Path( CRMRestConstants.PATH_USER_NOTIFICATIONS_BY_GUID )
    @Produces( MediaType.APPLICATION_JSON )
    public String getNumberNotificationsByGuidJson( @Context HttpServletRequest request, @PathParam( CRMRestConstants.USER_GUID ) String strGuid )
    {
        String strJSON = StringUtils.EMPTY;
        DemandFilter filter = new DemandFilter( );

        if ( !StringUtils.isEmpty( strGuid ) )
        {

            CRMUserService crmUserService = CRMUserService.getService( );
            CRMUser crmUser = crmUserService.findByUserGuid( strGuid );

            if ( crmUser != null )
            {

                filter.setIdCRMUser( crmUser.getIdCRMUser( ) );
                List<Demand> listDemand = DemandService.getService( ).findByFilter( filter );
                strJSON = getNumberNotifications( listDemand );

            }
            else
            {

                return StringUtils.EMPTY;
            }
        }

        return strJSON;
    }

    /**
     * Get the Json of the number notifications not read
     * 
     * @param listDemand
     *            the list demand object
     * @return the Json of the notification
     */
    private String getNumberNotifications( List<Demand> listDemand )
    {
        String strJSON = StringUtils.EMPTY;
        JSONObject json = new JSONObject( );
        if ( listDemand == null || listDemand.isEmpty( ) )
        {

            return strJSON;
        }

        Map<Integer, Integer> map = new HashMap<Integer, Integer>( );
        int nBNotifTotal = 0;
        for ( DemandType demandType : DemandTypeService.getService( ).findAll( ) )
        {

            map.put( demandType.getIdDemandType( ), 0 );
            JSONObject jsonTypedemande = new JSONObject( );
            for ( Demand demand : listDemand )
            {
                if ( demand.getIdDemandType( ) == demandType.getIdDemandType( ) )
                {
                    map.put( demandType.getIdDemandType( ), map.get( demandType.getIdDemandType( ) ) + demand.getNumberUnreadNotifications( ) );
                    nBNotifTotal = nBNotifTotal + demand.getNumberUnreadNotifications( );
                }
            }
            jsonTypedemande.accumulate( CRMRestConstants.TAG_NB_NOTIFICATIONS_UNREAD, map.get( demandType.getIdDemandType( ) ) );
            jsonTypedemande.accumulate( CRMRestConstants.PARAMETER_LABEL_DEMAND_TYPE, demandType.getLabel( ) );
            json.accumulate( String.valueOf( demandType.getIdDemandType( ) ), jsonTypedemande );
            JSONObject jsonDemand = new JSONObject( );
            jsonDemand.accumulate( CRMRestConstants.TAG_DEMNAD_TYPE, json );
            jsonDemand.accumulate( CRMRestConstants.TAG_NB_NOTIFICATIONS_UNREAD, nBNotifTotal );
            strJSON = jsonDemand.toString( 4 );
        }

        return strJSON;
    }
}
