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
package fr.paris.lutece.plugins.crm.modules.rest.util.constants;

/**
 *
 * CRMRestConstants
 *
 */
public final class CRMRestConstants
{
    // CONSTANTS
    public static final int INVALID_ID_INT = -1;
    public static final int TRUE = 1;
    public static final String SLASH = "/";
    public static final String INVALID_ID = "-1";
    public static final String MEDIA_TYPE_JSON = "application/json";
    public static final String MEDIA_TYPE_XML = "application/xml";
    public static final String API_VERSION = "version";
    public static final int VERSION_2 = 2;
    public static final int VERSION_3 = 3;
    public static final String USER_GUID = "user_guid";
    // PATHS
    public static final String PATH_WADL = "wadl";
    public static final String PATH_USER = "/user/";
    public static final String PATH_NOTIFY = "/notify/";
    public static final String PATH_DEMAND = "demand";
    public static final String PATH_USER_NOTIFICATIONS = "userNotification";
    public static final String PATH_USER_NOTIFICATIONS_BY_GUID = "userNotificationbyguid" + "/{" + CRMRestConstants.USER_GUID + "}";

    public static final String PATH_CRM_USER_ATTRIBUTES = "{user_guid}";
    public static final String PATH_CRM_USER_ATTRIBUTE = "{user_guid}/{attribute}";
    public static final String PATH_ID_CRM_USER = "{id_crm_user}";
    public static final String PATH_CREATE_DEMAND_BY_USER_GUID = PATH_DEMAND + "/createByUserGuid";
    public static final String PATH_CREATE_DEMAND_BY_ID_CRM_USER = PATH_DEMAND + "/createByIdCRMUser";
    public static final String PATH_UPDATE_DEMAND = PATH_DEMAND + "/update";
    public static final String PATH_DELETE_DEMAND = PATH_DEMAND + "/delete";
    public static final String PATH_VIEW_DEMAND = PATH_DEMAND + "/{id_demand}";
    public static final String PATH_GET_USER_GUID_FROM_ID_CRM_USER = PATH_ID_CRM_USER + "/user_guid";
    public static final String PATH_GET_USER_GUID_FROM_ID_DEMAND = PATH_DEMAND + "/{id_demand}/user_guid";
    public static final String PATH_CREATE_DEMAND_BY_USER_GUID_V2 = "/{" + API_VERSION + "}/" + PATH_CREATE_DEMAND_BY_USER_GUID;
    public static final String PATH_UPDATE_DEMAND_BY_USER_GUID_V2 = "/{" + API_VERSION + "}/" + PATH_UPDATE_DEMAND;
    public static final String PATH_DELETE_DEMAND_V2 = "/{" + API_VERSION + "}/" + PATH_DELETE_DEMAND;
    public static final String PATH_NOTIFY_DEMAND_V2 = "/{" + API_VERSION + "}/" + PATH_DEMAND;
    public static final String PATH_DEMAND_TYPES = "/demand_types";
    public static final String PATH_GET_DEMAND = "get/";
    public static final String PATH_GET_ALL_DEMANDS = "all/";
    public static final String PATH_VIEW_DEMAND_V2 = "/{" + API_VERSION + "}/" + PATH_DEMAND + "/{id_demand_type}/{remote_id}";

    // TAGS
    public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
    public static final String TAG_USER_ATTRIBUTES = "user-attributes";
    public static final String TAG_USER_ATTRIBUTE = "user-attribute";
    public static final String TAG_USER_ATTRIBUTE_KEY = "user-attribute-key";
    public static final String TAG_USER_ATTRIBUTE_VALUE = "user-attribute-value";
    public static final String TAG_DEMAND = "demand";
    public static final String TAG_ID_DEMAND = "id-demand";
    public static final String TAG_ID_DEMAND_TYPE = "id-demand-type";
    public static final String TAG_STATUS_TEXT = "status-text";
    public static final String TAG_ID_STATUS_CRM = "id-status-crm";
    public static final String TAG_DATA = "data";
    public static final String TAG_USER_GUID = "user-guid";
    public static final String TAG_DATE_MODIFICATION = "date-modification";
    public static final String TAG_NB_NOTIFICATIONS = "nb-notifications";
    public static final String TAG_NB_NOTIFICATIONS_UNREAD = "nb-notifications-unread";
    public static final String TAG_NOTIFICATION = "notification";
    // PARAMETERS
    public static final String PARAMETER_USER_GUID = "user_guid";
    public static final String PARAMETER_ATTRIBUTE = "attribute";
    public static final String PARAMETER_NOTIFICATION_OBJECT = "notification_object";
    public static final String PARAMETER_NOTIFICATION_MESSAGE = "notification_message";
    public static final String PARAMETER_NOTIFICATION_SENDER = "notification_sender";
    public static final String PARAMETER_ID_DEMAND = "id_demand";
    public static final String PARAMETER_REMOTE_ID = "remote_id";
    public static final String TAG_DEMNAD_TYPE = "demand_type";
    public static final String PARAMETER_ID_DEMAND_TYPE = "id_demand_type";
    public static final String PARAMETER_LABEL_DEMAND_TYPE = "label_demand_type";
    public static final String PARAMETER_DEMAND_DATA = "demand_data";
    public static final String PARAMETER_STATUS_TEXT = "status_text";
    public static final String PARAMETER_ID_STATUS_CRM = "id_status_crm";
    public static final String PARAMETER_ID_CRM_USER = "id_crm_user";
    public static final String PARAMETER_MEDIA_TYPE = "media_type";

    // PROPERTIES
    public static final String PROPERTY_ENCODING_ENABLE = "crm-rest.encoding.enable";
    public static final String PROPERTY_ENCODING_FROM = "crm-rest.encoding.from";
    public static final String PROPERTY_ENCODING_TO = "crm-rest.encoding.to";

    // MESSAGES
    public static final String MESSAGE_CRM_REST = "CRM Rest - ";
    public static final String MESSAGE_INVALID_USER = "Invalid User.";
    public static final String MESSAGE_INVALID_API_VERSION = "Invalid API Version";
    public static final String MESSAGE_MANDATORY_FIELDS = "Every mandatory fields are not filled.";
    public static final String MESSAGE_INVALID_DEMAND = "Invalid ID demand.";
    public static final String MESSAGE_INVALID_DEMAND_TYPE = "Invalid ID demand type.";
    public static final String MESSAGE_DEMAND_NOT_FOUND = "Demand not found";
    public static final String MESSAGE_INVALID_ID_STATUS_CRM = "Invalid ID status CRM";

    // MARKS
    public static final String MARK_BASE_URL = "base_url";

    // TEMPLATES
    public static final String TEMPLATE_WADL = "admin/plugins/crm/modules/rest/wadl.xml";

    /**
     * Private constructor
     */
    private CRMRestConstants( )
    {
    }
}
