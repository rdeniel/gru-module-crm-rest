/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
import fr.paris.lutece.plugins.crm.business.demand.DemandStatusCRM;
import fr.paris.lutece.plugins.crm.business.demand.DemandType;
import fr.paris.lutece.plugins.crm.service.CRMPlugin;
import fr.paris.lutece.plugins.crm.service.CRMService;
import fr.paris.lutece.plugins.crm.service.demand.DemandService;
import fr.paris.lutece.plugins.crm.service.demand.DemandStatusCRMService;
import fr.paris.lutece.plugins.crm.service.demand.DemandTypeService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.rest.util.json.JSONUtil;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

import freemarker.template.utility.StringUtil;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
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


/**
 *
 * CRMRest
 *
 */
@Path( RestConstants.BASE_PATH + CRMPlugin.PLUGIN_NAME )
public class CRMRest
{
    // CONSTANTS
    private static final String SLASH = "/";

    // PARAMETERS
    private static final String PARAMETER_ID_DEMAND_TYPE = "id_demand_type";
    private static final String PARAMETER_USER_GUID = "user_guid";
    private static final String PARAMETER_DEMAND_DATA = "demand_data";
    private static final String PARAMETER_STATUS_TEXT = "status_text";
    private static final String PARAMETER_ID_STATUS_CRM = "id_status_crm";
    private static final String PARAMETER_ID_DEMAND = "id_demand";
    private static final String PARAMETER_NOTIFICATION_OBJECT = "notification_object";
    private static final String PARAMETER_NOTIFICATION_MESSAGE = "notification_message";
    private static final String PARAMETER_NOTIFICATION_SENDER = "notification_sender";

    // TAGS
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
    private static final String TAG_DEMAND = "demand";
    private static final String TAG_ID_DEMAND = "id-demand";
    private static final String TAG_ID_DEMAND_TYPE = "id-demand-type";
    private static final String TAG_STATUS_TEXT = "status-text";
    private static final String TAG_ID_STATUS_CRM = "id-status-crm";
    private static final String TAG_DATA = "data";
    private static final String TAG_USER_GUID = "user-guid";
    private static final String TAG_DATE_MODIFICATION = "date-modification";
    private static final String TAG_NB_NOTIFICATIONS = "nb-notifications";

    // MARKS
    private static final String MARK_BASE_URL = "base_url";

    // TEMPLATES
    private static final String TEMPLATE_WADL = "admin/plugins/crm/modules/rest/wadl.xml";

    // PATHS
    private static final String PATH_WADL = "wadl";
    private static final String PATH_CREATE_DEMAND = "demand/create";
    private static final String PATH_UPDATE_DEMAND = "demand/update";
    private static final String PATH_VIEW_DEMAND = "demand/{id_demand}";
    private static final String PATH_NOTIFY = "demand/notify";

    // MESSAGES
    private static final String MESSAGE_CRM_REST = "CRM Rest - ";
    private static final String MESSAGE_MANDATORY_FIELDS = "Every mandatory fields are not filled.";
    private static final String MESSAGE_INVALID_DEMAND_TYPE = "Invalid ID demand type.";
    private static final String MESSAGE_INVALID_DEMAND = "Invalid ID demand.";
    private static final String MESSAGE_INVALID_USER_GUID = "Invalid User GUID.";
    private static final String MESSAGE_DEMAND_NOT_FOUND = "Demand not found";
    private static final String MESSAGE_INVALID_ID_STATUS_CRM = "Invalid ID status CRM";

    /**
     * Get the wadl.xml content
     * @param request {@link HttpServletRequest}
     * @return the content of wadl.xml
     */
    @GET
    @Path( PATH_WADL )
    @Produces( MediaType.APPLICATION_XML )
    public String getWADL( @Context
    HttpServletRequest request )
    {
        StringBuilder sbBase = new StringBuilder( AppPathService.getBaseUrl( request ) );

        if ( sbBase.toString(  ).endsWith( SLASH ) )
        {
            sbBase.deleteCharAt( sbBase.length(  ) - 1 );
        }

        sbBase.append( RestConstants.BASE_PATH + CRMPlugin.PLUGIN_NAME );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_BASE_URL, sbBase.toString(  ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_WADL, request.getLocale(  ), model );

        return t.getHtml(  );
    }

    /**
     * Create a new demand
     * @param strIdDemandType the demand type id
     * @param strUserGuid the user guid
     * @param strIdStatusCRM the id status crm
     * @param strStatusText the status text
     * @param strData the data
     * @param request {@link HttpServletRequest}
     * @return the id of the newly created demand
     */
    @POST
    @Path( PATH_CREATE_DEMAND )
    @Produces( MediaType.TEXT_HTML )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doCreateDemand( @FormParam( PARAMETER_ID_DEMAND_TYPE )
    String strIdDemandType, @FormParam( PARAMETER_USER_GUID )
    String strUserGuid, @FormParam( PARAMETER_ID_STATUS_CRM )
    String strIdStatusCRM, @FormParam( PARAMETER_STATUS_TEXT )
    String strStatusText, @FormParam( PARAMETER_DEMAND_DATA )
    String strData, @Context
    HttpServletRequest request )
    {
        String strIdDemand = "-1";

        if ( StringUtils.isNotBlank( strIdDemandType ) && StringUtils.isNumeric( strIdDemandType ) &&
                StringUtils.isNotBlank( strUserGuid ) && StringUtils.isNotBlank( strIdStatusCRM ) &&
                StringUtils.isNumeric( strIdStatusCRM ) )
        {
            int nIdDemandType = Integer.parseInt( strIdDemandType );
            DemandType demandType = DemandTypeService.getService(  ).findByPrimaryKey( nIdDemandType );

            if ( demandType != null )
            {
                int nIdStatusCRM = Integer.parseInt( strIdStatusCRM );
                DemandStatusCRM statusCRM = DemandStatusCRMService.getService(  )
                                                                  .getStatusCRM( nIdStatusCRM, request.getLocale(  ) );

                if ( statusCRM != null )
                {
                    LuteceUser user = SecurityService.getInstance(  ).getUser( strUserGuid );

                    if ( user != null )
                    {
                        strIdDemand = Integer.toString( CRMService.getService(  )
                                                                  .registerDemand( nIdDemandType, strUserGuid, strData,
                                    strStatusText, nIdStatusCRM ) );
                    }
                    else
                    {
                        AppLogService.error( MESSAGE_CRM_REST + MESSAGE_INVALID_USER_GUID );
                    }
                }
                else
                {
                    AppLogService.error( MESSAGE_CRM_REST + MESSAGE_INVALID_ID_STATUS_CRM );
                }
            }
            else
            {
                AppLogService.error( MESSAGE_CRM_REST + MESSAGE_INVALID_DEMAND_TYPE );
            }
        }
        else
        {
            AppLogService.error( MESSAGE_MANDATORY_FIELDS );
        }

        return strIdDemand;
    }

    /**
     * Update a demand
     * @param strIdDemand the id demand
     * @param strIdStatusCRM the id status crm
     * @param strStatusText the status text
     * @param strData the data
     * @param request {@link HttpServletRequest}
     * @return the id of the demand
     */
    @POST
    @Path( PATH_UPDATE_DEMAND )
    @Produces( MediaType.TEXT_HTML )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doUpdateDemand( @FormParam( PARAMETER_ID_DEMAND )
    String strIdDemand, @FormParam( PARAMETER_ID_STATUS_CRM )
    String strIdStatusCRM, @FormParam( PARAMETER_STATUS_TEXT )
    String strStatusText, @FormParam( PARAMETER_DEMAND_DATA )
    String strData, @Context
    HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) &&
                StringUtils.isNotBlank( strIdStatusCRM ) && StringUtils.isNumeric( strIdStatusCRM ) )
        {
            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService(  ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                int nIdStatusCRM = Integer.parseInt( strIdStatusCRM );
                DemandStatusCRM statusCRM = DemandStatusCRMService.getService(  )
                                                                  .getStatusCRM( nIdStatusCRM, request.getLocale(  ) );

                if ( statusCRM != null )
                {
                    CRMService.getService(  ).setStatus( nIdDemand, strData, strStatusText, nIdStatusCRM );
                }
                else
                {
                    AppLogService.error( MESSAGE_CRM_REST + MESSAGE_INVALID_ID_STATUS_CRM );
                }
            }
            else
            {
                AppLogService.error( MESSAGE_CRM_REST + MESSAGE_INVALID_DEMAND );
            }
        }
        else
        {
            AppLogService.error( MESSAGE_CRM_REST + MESSAGE_MANDATORY_FIELDS );
        }

        return strIdDemand;
    }

    /**
     * Notification for a demand
     * @param strIdDemand the id demand
     * @param strNotificationObject the notification object
     * @param strNotificationMessage the notification message
     * @param strSender the sender
     * @return the id demand
     */
    @POST
    @Path( PATH_NOTIFY )
    @Produces( MediaType.TEXT_HTML )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doNotify( @FormParam( PARAMETER_ID_DEMAND )
    String strIdDemand, @FormParam( PARAMETER_NOTIFICATION_OBJECT )
    String strNotificationObject, @FormParam( PARAMETER_NOTIFICATION_MESSAGE )
    String strNotificationMessage, @FormParam( PARAMETER_NOTIFICATION_SENDER )
    String strNotificationSender )
    {
        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) &&
                StringUtils.isNotBlank( strNotificationObject ) )
        {
            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService(  ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                CRMService.getService(  )
                          .notify( nIdDemand, strNotificationObject, strNotificationMessage, strNotificationSender );
            }
            else
            {
                AppLogService.error( MESSAGE_CRM_REST + MESSAGE_INVALID_DEMAND );
            }
        }
        else
        {
            AppLogService.error( MESSAGE_CRM_REST + MESSAGE_MANDATORY_FIELDS );
        }

        return strIdDemand;
    }

    /**
     * Get the XML of the demand
     * @param strIdDemand the id demand
     * @return the XML of the demand
     */
    @GET
    @Path( PATH_VIEW_DEMAND )
    @Produces( MediaType.APPLICATION_XML )
    public String getDemandXML( @PathParam( PARAMETER_ID_DEMAND )
    String strIdDemand )
    {
        StringBuffer sbXML = new StringBuffer(  );

        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) )
        {
            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService(  ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                // sbXML.append( XmlUtil.getXmlHeader(  ) );
                sbXML.append( XML_HEADER );
                XmlUtil.beginElement( sbXML, TAG_DEMAND );

                XmlUtil.addElement( sbXML, TAG_ID_DEMAND, demand.getIdDemand(  ) );
                XmlUtil.addElement( sbXML, TAG_ID_DEMAND_TYPE, demand.getIdDemandType(  ) );
                XmlUtil.addElement( sbXML, TAG_STATUS_TEXT, StringUtil.XMLEnc( demand.getStatusText(  ) ) );
                XmlUtil.addElement( sbXML, TAG_ID_STATUS_CRM, demand.getIdStatusCRM(  ) );
                XmlUtil.addElement( sbXML, TAG_DATA, demand.getData(  ) );
                XmlUtil.addElement( sbXML, TAG_USER_GUID, demand.getUserGuid(  ) );

                String strDateModification = DateUtil.getDateString( demand.getDateModification(  ), null );
                XmlUtil.addElement( sbXML, TAG_DATE_MODIFICATION, strDateModification );
                XmlUtil.addElement( sbXML, TAG_NB_NOTIFICATIONS, demand.getNumberNotifications(  ) );

                XmlUtil.endElement( sbXML, TAG_DEMAND );
            }
            else
            {
                AppLogService.error( MESSAGE_CRM_REST + MESSAGE_DEMAND_NOT_FOUND );
            }
        }
        else
        {
            AppLogService.error( MESSAGE_CRM_REST + MESSAGE_INVALID_DEMAND );
        }

        return sbXML.toString(  );
    }

    /**
     * Get the Json of the demand
     * @param strIdDemand the id of the demand
     * @return the Json of the demand
     */
    @GET
    @Path( PATH_VIEW_DEMAND )
    @Produces( MediaType.APPLICATION_JSON )
    public String getDemandJson( @PathParam( PARAMETER_ID_DEMAND )
    String strIdDemand )
    {
        String strJSON = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) )
        {
            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService(  ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                JSONObject json = new JSONObject(  );
                json.accumulate( TAG_ID_DEMAND, demand.getIdDemand(  ) );
                json.accumulate( TAG_ID_DEMAND_TYPE, demand.getIdDemandType(  ) );
                json.accumulate( TAG_STATUS_TEXT, StringUtil.XMLEnc( demand.getStatusText(  ) ) );
                json.accumulate( TAG_ID_STATUS_CRM, demand.getIdStatusCRM(  ) );
                json.accumulate( TAG_DATA, demand.getData(  ) );
                json.accumulate( TAG_USER_GUID, demand.getUserGuid(  ) );

                String strDateModification = DateUtil.getDateString( demand.getDateModification(  ), null );
                json.accumulate( TAG_DATE_MODIFICATION, strDateModification );
                json.accumulate( TAG_NB_NOTIFICATIONS, demand.getNumberNotifications(  ) );

                JSONObject jsonDemand = new JSONObject(  );
                jsonDemand.accumulate( TAG_DEMAND, json );
                strJSON = jsonDemand.toString( 4 );
            }
            else
            {
                AppLogService.error( MESSAGE_CRM_REST + MESSAGE_DEMAND_NOT_FOUND );
                strJSON = JSONUtil.formatError( MESSAGE_DEMAND_NOT_FOUND, 3 );
            }
        }
        else
        {
            AppLogService.error( MESSAGE_CRM_REST + MESSAGE_INVALID_DEMAND );
            strJSON = JSONUtil.formatError( MESSAGE_INVALID_DEMAND, 3 );
        }

        return strJSON;
    }
}
