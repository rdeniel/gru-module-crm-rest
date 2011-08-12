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

import fr.paris.lutece.plugins.crm.business.user.CRMUser;
import fr.paris.lutece.plugins.crm.modules.rest.util.constants.CRMRestConstants;
import fr.paris.lutece.plugins.crm.service.CRMPlugin;
import fr.paris.lutece.plugins.crm.service.user.CRMUserAttributesService;
import fr.paris.lutece.plugins.crm.service.user.CRMUserService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.rest.util.json.JSONUtil;
import fr.paris.lutece.plugins.rest.util.xml.XMLUtil;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.xml.XmlUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 *
 * CRMUserAttributeRest
 *
 */
@Path( RestConstants.BASE_PATH + CRMPlugin.PLUGIN_NAME + CRMRestConstants.PATH_USER )
public class CRMUserAttributesRest
{
    /**
     * Get the CRMUser attributes in XML
     * @param strUserGuid the user guid
     * @return the CRMUser attributes
     */
    @GET
    @Path( CRMRestConstants.PATH_CRM_USER_ATTRIBUTES )
    @Produces( MediaType.APPLICATION_XML )
    public String getCRMUserAttributesXml( @PathParam( CRMRestConstants.PARAMETER_USER_GUID )
    String strUserGuid )
    {
        StringBuffer sbXML = new StringBuffer(  );

        if ( StringUtils.isNotBlank( strUserGuid ) )
        {
            // sbXML.append( XmlUtil.getXmlHeader(  ) );
            sbXML.append( CRMRestConstants.XML_HEADER );
            XmlUtil.beginElement( sbXML, CRMRestConstants.TAG_USER_ATTRIBUTES );

            Map<String, String> listAttributes = CRMUserAttributesService.getService(  ).getAttributes( strUserGuid );

            for ( Entry<String, String> attribute : listAttributes.entrySet(  ) )
            {
                XmlUtil.beginElement( sbXML, CRMRestConstants.TAG_USER_ATTRIBUTE );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_USER_ATTRIBUTE_KEY, attribute.getKey(  ) );
                XmlUtil.addElement( sbXML, CRMRestConstants.TAG_USER_ATTRIBUTE_VALUE, attribute.getValue(  ) );
                XmlUtil.endElement( sbXML, CRMRestConstants.TAG_USER_ATTRIBUTE );
            }

            XmlUtil.endElement( sbXML, CRMRestConstants.TAG_USER_ATTRIBUTES );
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_USER );
            sbXML.append( XMLUtil.formatError( CRMRestConstants.MESSAGE_INVALID_USER, 3 ) );
        }

        return sbXML.toString(  );
    }

    /**
     * Get the CRMUser attributes in JSON
     * @param strUserGuid the user guid
     * @return the attributes
     */
    @GET
    @Path( CRMRestConstants.PATH_CRM_USER_ATTRIBUTES )
    @Produces( MediaType.APPLICATION_JSON )
    public String getCRMUserAttributesJson( @PathParam( CRMRestConstants.PARAMETER_USER_GUID )
    String strUserGuid )
    {
        String strJSON = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( strUserGuid ) )
        {
            JSONObject jsonAttributes = new JSONObject(  );
            JSONArray jsonArray = new JSONArray(  );

            Map<String, String> listAttributes = CRMUserAttributesService.getService(  ).getAttributes( strUserGuid );

            for ( Entry<String, String> attribute : listAttributes.entrySet(  ) )
            {
                JSONObject jsonAttribute = new JSONObject(  );
                jsonAttribute.accumulate( CRMRestConstants.TAG_USER_ATTRIBUTE_KEY, attribute.getKey(  ) );
                jsonAttribute.accumulate( CRMRestConstants.TAG_USER_ATTRIBUTE_VALUE, attribute.getValue(  ) );
                jsonArray.add( jsonAttribute );
            }

            jsonAttributes.accumulate( CRMRestConstants.TAG_USER_ATTRIBUTES, jsonArray );
            strJSON = jsonAttributes.toString( 4 );
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_USER );
            strJSON = JSONUtil.formatError( CRMRestConstants.MESSAGE_INVALID_USER, 3 );
        }

        return strJSON;
    }

    /**
     * Get the CRMUser attribute value
     * @param strUserGuid the user guid
     * @param strAttribute the attribute
     * @return the attribute value
     */
    @GET
    @Path( CRMRestConstants.PATH_CRM_USER_ATTRIBUTE )
    @Produces( MediaType.TEXT_PLAIN )
    public String getCRMUserAttribute( @PathParam( CRMRestConstants.PARAMETER_USER_GUID )
    String strUserGuid, @PathParam( CRMRestConstants.PARAMETER_ATTRIBUTE )
    String strAttribute )
    {
        String strAttributeValue = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( strUserGuid ) )
        {
            strAttributeValue = CRMUserAttributesService.getService(  ).getAttribute( strUserGuid, strAttribute );
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_USER );
        }

        return strAttributeValue;
    }

    /**
     * Get the user guid from a given id crm user
     * @param strIdCRMUser the id crm user
     * @return the user guid
     */
    @GET
    @Path( CRMRestConstants.PATH_GET_USER_GUID )
    @Produces( MediaType.TEXT_PLAIN )
    public String getUserGuidFromIdCRMUser( @PathParam( CRMRestConstants.PARAMETER_ID_CRM_USER )
    String strIdCRMUser )
    {
        String strUserGuid = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( strIdCRMUser ) && StringUtils.isNumeric( strIdCRMUser ) )
        {
            int nIdCRMUser = Integer.parseInt( strIdCRMUser );
            CRMUser user = CRMUserService.getService(  ).findByPrimaryKey( nIdCRMUser );

            if ( user != null )
            {
                strUserGuid = user.getUserGuid(  );
            }
        }
        else
        {
            AppLogService.error( CRMRestConstants.MESSAGE_CRM_REST + CRMRestConstants.MESSAGE_INVALID_USER );
        }

        return strUserGuid;
    }
}
