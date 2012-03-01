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
package fr.paris.lutece.plugins.crm.modules.rest.util;

import fr.paris.lutece.plugins.crm.modules.rest.util.constants.CRMRestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.lang.StringUtils;


/**
 *
 * StringUtil
 *
 */
public final class StringUtil
{
    /**
     * Private constructor
     */
    private StringUtil(  )
    {
    }

    /**
     * Convert a string from the encoding defined in <code>crm-rest.encoding.from</code>
     * to the encoding defined in <code>crm-rest.encoding.to</code>.
     * <br />
     * If there is an error, then it will return the String given in the parameter.
     * @param strToConvert the String to convert
     * @return the convert String if there is no error, the String in the parameter otherwise
     */
    public static String convertString( String strToConvert )
    {
        int nConversionEnable = AppPropertiesService.getPropertyInt( CRMRestConstants.PROPERTY_ENCODING_ENABLE, 0 );
        boolean bConversionEnable = nConversionEnable == CRMRestConstants.TRUE;
        String strEncodingFrom = AppPropertiesService.getProperty( CRMRestConstants.PROPERTY_ENCODING_FROM );
        String strEncodingTo = AppPropertiesService.getProperty( CRMRestConstants.PROPERTY_ENCODING_TO );
        // The encoding from can be an empty string. Then it will get by the default computer encoding
        strEncodingFrom = StringUtils.isNotBlank( strEncodingFrom ) ? strEncodingFrom : StringUtils.EMPTY;

        String strConverted = null;

        if ( bConversionEnable && StringUtils.isNotBlank( strEncodingTo ) && ( strToConvert != null ) )
        {
            try
            {
                strConverted = new String( strToConvert.getBytes( strEncodingFrom ), strEncodingTo );
            }
            catch ( Exception e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        if ( StringUtils.isNotBlank( strConverted ) )
        {
            return strConverted;
        }

        return strToConvert;
    }
}
