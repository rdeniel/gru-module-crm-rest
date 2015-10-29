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

import fr.paris.lutece.plugins.crm.business.demand.Demand;
import fr.paris.lutece.plugins.crm.modules.rest.util.StringUtil;
import fr.paris.lutece.plugins.crm.modules.rest.util.constants.CRMRestConstants;
import fr.paris.lutece.plugins.crm.service.CRMPlugin;
import fr.paris.lutece.plugins.crm.service.CRMService;
import fr.paris.lutece.plugins.crm.service.demand.DemandService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.StringUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


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
    * @param strIdDemand the id demand
    * @param strNotificationObject the notification object
    * @param strNotificationMessage the notification message
    * @param strNotificationSender the sender
    * @return the id demand
    */
    @POST
    @Path( CRMRestConstants.PATH_DEMAND )
    @Produces( MediaType.TEXT_PLAIN )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doNotify( @FormParam( CRMRestConstants.PARAMETER_ID_DEMAND )
    String strIdDemand, @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_OBJECT )
    String strNotificationObject,
        @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_MESSAGE )
    String strNotificationMessage,
        @FormParam( CRMRestConstants.PARAMETER_NOTIFICATION_SENDER )
    String strNotificationSender )
    {
        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) &&
                StringUtils.isNotBlank( strNotificationObject ) )
        {
            String strObject = StringUtil.convertString( strNotificationObject );
            String strMessage = StringUtil.convertString( strNotificationMessage );
            String strSender = StringUtil.convertString( strNotificationSender );

            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService(  ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                CRMService.getService(  ).notify( nIdDemand, strObject, strMessage, strSender );
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
}
