/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
import fr.paris.lutece.plugins.crm.business.user.CRMUser;
import fr.paris.lutece.plugins.crm.modules.rest.util.StringUtil;
import fr.paris.lutece.plugins.crm.modules.rest.util.constants.CRMRestConstants;
import fr.paris.lutece.plugins.crm.service.CRMPlugin;
import fr.paris.lutece.plugins.crm.service.CRMService;
import fr.paris.lutece.plugins.crm.service.demand.DemandService;
import fr.paris.lutece.plugins.crm.service.demand.DemandStatusCRMService;
import fr.paris.lutece.plugins.crm.service.demand.DemandTypeService;
import fr.paris.lutece.plugins.crm.service.user.CRMUserService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.rest.util.json.JSONUtil;
import fr.paris.lutece.plugins.rest.util.xml.XMLUtil;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

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
    /**
     * Get the wadl.xml content
     * @param request {@link HttpServletRequest}
     * @return the content of wadl.xml
     */
    @GET
    @Path( CRMRestConstants.PATH_WADL )
    @Produces( MediaType.APPLICATION_XML )
    public String getWADL( @Context
    HttpServletRequest request )
    {
        StringBuilder sbBase = new StringBuilder( AppPathService.getBaseUrl( request ) );

        if ( sbBase.toString(  ).endsWith( CRMRestConstants.SLASH ) )
        {
            sbBase.deleteCharAt( sbBase.length(  ) - 1 );
        }

        sbBase.append( RestConstants.BASE_PATH + CRMPlugin.PLUGIN_NAME );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( CRMRestConstants.MARK_BASE_URL, sbBase.toString(  ) );

        HtmlTemplate t = AppTemplateService.getTemplate( CRMRestConstants.TEMPLATE_WADL, request.getLocale(  ), model );

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
    @Path( CRMRestConstants.PATH_CREATE_DEMAND_BY_USER_GUID )
    @Produces( MediaType.TEXT_HTML )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doCreateDemandByUserGuid( 
        @FormParam( CRMRestConstants.PARAMETER_ID_DEMAND_TYPE )
    String strIdDemandType, @FormParam( CRMRestConstants.PARAMETER_USER_GUID )
    String strUserGuid, @FormParam( CRMRestConstants.PARAMETER_ID_STATUS_CRM )
    String strIdStatusCRM, @FormParam( CRMRestConstants.PARAMETER_STATUS_TEXT )
    String strStatusText, @FormParam( CRMRestConstants.PARAMETER_DEMAND_DATA )
    String strData, @Context
    HttpServletRequest request )
    {
        String strIdDemand = CRMRestConstants.INVALID_ID;

        if ( StringUtils.isNotBlank( strUserGuid ) )
        {
            CRMUser crmUser = CRMUserService.getService(  ).findByUserGuid( strUserGuid );

            if ( crmUser != null )
            {
                String strConvertedStatusText = StringUtil.convertString( strStatusText );
                String strConvertedData = StringUtil.convertString( strData );
                strIdDemand = doCreateDemandByIdCRMUser( strIdDemandType, Integer.toString( crmUser.getIdCRMUser(  ) ),
                        strIdStatusCRM, strConvertedStatusText, strConvertedData, request );
            }
            else
            {
                AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_USER );
            }
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_MANDATORY_FIELDS );
        }

        return strIdDemand;
    }

    /**
     * Create a new demand
     * @param strIdDemandType the demand type id
     * @param strIdCRMUser the ID CRM user
     * @param strIdStatusCRM the id status crm
     * @param strStatusText the status text
     * @param strData the data
     * @param request {@link HttpServletRequest}
     * @return the id of the newly created demand
     */
    @POST
    @Path( CRMRestConstants.PATH_CREATE_DEMAND_BY_ID_CRM_USER )
    @Produces( MediaType.TEXT_HTML )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doCreateDemandByIdCRMUser( 
        @FormParam( CRMRestConstants.PARAMETER_ID_DEMAND_TYPE )
    String strIdDemandType, @FormParam( CRMRestConstants.PARAMETER_ID_CRM_USER )
    String strIdCRMUser, @FormParam( CRMRestConstants.PARAMETER_ID_STATUS_CRM )
    String strIdStatusCRM, @FormParam( CRMRestConstants.PARAMETER_STATUS_TEXT )
    String strStatusText, @FormParam( CRMRestConstants.PARAMETER_DEMAND_DATA )
    String strData, @Context
    HttpServletRequest request )
    {
        String strIdDemand = CRMRestConstants.INVALID_ID;

        if ( StringUtils.isNotBlank( strIdDemandType ) && StringUtils.isNumeric( strIdDemandType ) &&
                StringUtils.isNotBlank( strIdStatusCRM ) && StringUtils.isNumeric( strIdStatusCRM ) &&
                StringUtils.isNotBlank( strIdCRMUser ) && StringUtils.isNumeric( strIdCRMUser ) )
        {
            int nIdCRMUser = Integer.parseInt( strIdCRMUser );
            CRMUser crmUser = CRMUserService.getService(  ).findByPrimaryKey( nIdCRMUser );

            if ( crmUser != null )
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
                        String strConvertedStatusText = StringUtil.convertString( strStatusText );
                        String strConvertedData = StringUtil.convertString( strData );
                        strIdDemand = Integer.toString( CRMService.getService(  )
                                                                  .registerDemand( nIdDemandType, nIdCRMUser,
                                    strConvertedData, strConvertedStatusText, nIdStatusCRM ) );
                    }
                    else
                    {
                        AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST +
                            CRMRestConstants.MESSAGE_INVALID_ID_STATUS_CRM );
                    }
                }
                else
                {
                    AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST +
                        CRMRestConstants.MESSAGE_INVALID_DEMAND_TYPE );
                }
            }
            else
            {
                AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_USER );
            }
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_MANDATORY_FIELDS );
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
    @Path( CRMRestConstants.PATH_UPDATE_DEMAND )
    @Produces( MediaType.TEXT_HTML )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doUpdateDemand( @FormParam( CRMRestConstants.PARAMETER_ID_DEMAND )
    String strIdDemand, @FormParam( CRMRestConstants.PARAMETER_ID_STATUS_CRM )
    String strIdStatusCRM, @FormParam( CRMRestConstants.PARAMETER_STATUS_TEXT )
    String strStatusText, @FormParam( CRMRestConstants.PARAMETER_DEMAND_DATA )
    String strData, @Context
    HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) )
        {
            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService(  ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                int nIdStatusCRM = CRMRestConstants.INVALID_ID_INT;
                DemandStatusCRM statusCRM = null;

                if ( StringUtils.isNotBlank( strIdStatusCRM ) && StringUtils.isNumeric( strIdStatusCRM ) )
                {
                    nIdStatusCRM = Integer.parseInt( strIdStatusCRM );
                    statusCRM = DemandStatusCRMService.getService(  ).getStatusCRM( nIdStatusCRM, request.getLocale(  ) );
                }

                if ( ( statusCRM != null ) || ( nIdStatusCRM == CRMRestConstants.INVALID_ID_INT ) )
                {
                    String strConvertedStatusText = StringUtil.convertString( strStatusText );
                    String strConvertedData = StringUtil.convertString( strData );
                    CRMService.getService(  )
                              .setStatus( nIdDemand, strConvertedData, strConvertedStatusText, nIdStatusCRM );
                }
                else
                {
                    AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST +
                        CRMRestConstants.MESSAGE_INVALID_ID_STATUS_CRM );
                }
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
     * Delete a demand
     * @param strIdDemand the id demand
     * @return the id of the demand
     */
    @POST
    @Path( CRMRestConstants.PATH_DELETE_DEMAND )
    @Produces( MediaType.TEXT_HTML )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public String doDeleteDemand( @FormParam( CRMRestConstants.PARAMETER_ID_DEMAND )
    String strIdDemand )
    {
        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) )
        {
            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService(  ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                CRMService.getService(  ).deleteDemand( nIdDemand );
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
     * Get the XML of the demand
     * @param strIdDemand the id demand
     * @return the XML of the demand
     */
    @GET
    @Path( CRMRestConstants.PATH_VIEW_DEMAND )
    @Produces( MediaType.APPLICATION_XML )
    public String getDemandXML( @PathParam( CRMRestConstants.PARAMETER_ID_DEMAND )
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
                sbXML.append( CRMRestConstants.XML_HEADER );
                XmlUtil.beginElement( sbXML, CRMRestConstants.TAG_DEMAND );

                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_ID_DEMAND, demand.getIdDemand(  ) );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_ID_DEMAND_TYPE, demand.getIdDemandType(  ) );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_STATUS_TEXT, demand.getStatusText(  ) );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_ID_STATUS_CRM, demand.getIdStatusCRM(  ) );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_DATA, demand.getData(  ) );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_USER_GUID, demand.getIdCRMUser(  ) );

                String strDateModification = DateUtil.getDateString( demand.getDateModification(  ), null );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_DATE_MODIFICATION, strDateModification );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_NB_NOTIFICATIONS, demand.getNumberNotifications(  ) );

                XmlUtil.endElement( sbXML, CRMRestConstants.TAG_DEMAND );
            }
            else
            {
                AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_DEMAND_NOT_FOUND );
                sbXML.append( XMLUtil.formatError( CRMRestConstants.MESSAGE_DEMAND_NOT_FOUND, 3 ) );
            }
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_DEMAND );
            sbXML.append( XMLUtil.formatError( CRMRestConstants.MESSAGE_INVALID_DEMAND, 3 ) );
        }

        return sbXML.toString(  );
    }

    /**
     * Get the Json of the demand
     * @param strIdDemand the id of the demand
     * @return the Json of the demand
     */
    @GET
    @Path( CRMRestConstants.PATH_VIEW_DEMAND )
    @Produces( MediaType.APPLICATION_JSON )
    public String getDemandJson( @PathParam( CRMRestConstants.PARAMETER_ID_DEMAND )
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
                json.accumulate( CRMRestConstants.TAG_ID_DEMAND, demand.getIdDemand(  ) );
                json.accumulate( CRMRestConstants.TAG_ID_DEMAND_TYPE, demand.getIdDemandType(  ) );
                json.accumulate( CRMRestConstants.TAG_STATUS_TEXT, demand.getStatusText(  ) );
                json.accumulate( CRMRestConstants.TAG_ID_STATUS_CRM, demand.getIdStatusCRM(  ) );
                json.accumulate( CRMRestConstants.TAG_DATA, demand.getData(  ) );
                json.accumulate( CRMRestConstants.TAG_USER_GUID, demand.getIdCRMUser(  ) );

                String strDateModification = DateUtil.getDateString( demand.getDateModification(  ), null );
                json.accumulate( CRMRestConstants.TAG_DATE_MODIFICATION, strDateModification );
                json.accumulate( CRMRestConstants.TAG_NB_NOTIFICATIONS, demand.getNumberNotifications(  ) );

                JSONObject jsonDemand = new JSONObject(  );
                jsonDemand.accumulate( CRMRestConstants.TAG_DEMAND, json );
                strJSON = jsonDemand.toString( 4 );
            }
            else
            {
                AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_DEMAND_NOT_FOUND );
                strJSON = JSONUtil.formatError( CRMRestConstants.MESSAGE_DEMAND_NOT_FOUND, 3 );
            }
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_DEMAND );
            strJSON = JSONUtil.formatError( CRMRestConstants.MESSAGE_INVALID_DEMAND, 3 );
        }

        return strJSON;
    }

    /**
     * Get the user guid from a given id demand
     * @param strIdDemand the id demand
     * @return the user guid
     */
    @GET
    @Path( CRMRestConstants.PATH_GET_USER_GUID_FROM_ID_DEMAND )
    @Produces( MediaType.TEXT_PLAIN )
    public String getUserGuidFromIdDemand( @PathParam( CRMRestConstants.PARAMETER_ID_DEMAND )
    String strIdDemand )
    {
        String strUserGuid = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( strIdDemand ) && StringUtils.isNumeric( strIdDemand ) )
        {
            int nIdDemand = Integer.parseInt( strIdDemand );
            Demand demand = DemandService.getService(  ).findByPrimaryKey( nIdDemand );

            if ( demand != null )
            {
                int nIdCRMUser = demand.getIdCRMUser(  );
                CRMUser user = CRMUserService.getService(  ).findByPrimaryKey( nIdCRMUser );

                if ( user != null )
                {
                    strUserGuid = user.getUserGuid(  );
                }
            }
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_USER );
        }

        return strUserGuid;
    }
}
