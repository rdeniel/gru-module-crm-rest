<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>

<html>
    <head>
        <title>CRM - REST webservices test page</title>
        <base href="<%= AppPathService.getBaseUrl( request ) %>" />
        <link rel="stylesheet" type="text/css" href="css/portal_admin.css" title="lutece_admin" />
        <script type="text/javascript">
            function onDemandView(  ) {
                var id = document.formGetDemand.id_demand.value;
                var format = document.formGetDemand.format.value;
                document.location= 'rest/crm/demand/' + id + format;
            }

            function onUserAttributesView(  ) {
                var user_guid = document.formGetUserAttributes.user_guid.value;
                var format = document.formGetUserAttributes.format.value;
                document.location= 'rest/crm/user/' + user_guid + format;
            }

            function onUserAttributeView(  ) {
                var user_guid = document.formGetUserAttribute.user_guid.value;
                var attribute = document.formGetUserAttribute.attribute.value;
                document.location= 'rest/crm/user/' + user_guid + '/' + attribute;
            }
            
        </script>
    </head>
    <body>
        <div id="content" >
            <h1>CRM - REST webservices test page </h1>
            <div class="content-box">
	            <div class="highlight-box">
	                <h2>View WADL</h2>
	                <form action="rest/crm/wadl">
	                    <br/>
	                    <input class="button" type="submit" value="View WADL" />
	                </form>
	            </div>
	            
	            <div class="highlight-box">
	                <h2>View demand data</h2>
	                <form name="formGetDemand">
	                    <label for="id_demand">ID demand : </label>
	                    <input type="text" name="id_demand" size="10" maxlength="255" />
	                    <br/>
	                    <label for="format">Format :</label>
	                    <select name="format">
	                        <option value=".xml">XML</option>
	                        <option value=".json">JSON</option>
	                    </select>
	                    <br/>
	                    <input class="button" type="button" value="View" onclick="javascript:onDemandView(  )"/>
	                </form>
	            </div>
	            
	            <div class="highlight-box">
	                <h2>Create a demand with the user guid</h2>
	                <form action="rest/crm/demand/createByUserGuid" method="post">
	                    <label for="id_demand_type">ID demand type * : </label>
	                    <input type="text" name="id_demand_type" />
	                    <br />
	                    <label for="user_guid">User GUID * :</label>
	                    <input type="text" name="user_guid" />
	                    <br />
	                    <label for="id_status_crm">ID Status CRM * : </label>
	                    <input type="text" name="id_status_crm" size="1" maxlength="1" />
	                    <br />
	                    <label for="status_text">Status text: </label>
	                    <input type="text" name="status_text" />
	                    <br />
	                    <label for="data">Data : </label>
	                    <input type="text" name="data" />
	                    <br />
	                    <input class="button" type="submit" value="Create" />
	                </form>
	            </div>
	            
	            <div class="highlight-box">
	                <h2>Create a demand with the ID CRM User</h2>
	                <form action="rest/crm/demand/createByIdCRMUser" method="post">
	                    <label for="id_demand_type">ID demand type * : </label>
	                    <input type="text" name="id_demand_type" />
	                    <br />
	                    <label for="id_crm_user">ID CRM user * :</label>
	                    <input type="text" name="id_crm_user" />
	                    <br />
	                    <label for="id_status_crm">ID Status CRM * : </label>
	                    <input type="text" name="id_status_crm" size="1" maxlength="1" />
	                    <br />
	                    <label for="status_text">Status text: </label>
	                    <input type="text" name="status_text" />
	                    <br />
	                    <label for="data">Data : </label>
	                    <input type="text" name="data" />
	                    <br />
	                    <input class="button" type="submit" value="Create" />
	                </form>
	            </div>
	
	            <div class="highlight-box">
	                <h2>Update a demand</h2>
	                <form action="rest/crm/demand/update" method="post">
	                    <label for="id_demand">ID demand * : </label>
	                    <input type="text" name="id_demand" />
	                    <br />
	                    <label for="id_status_crm">ID Status CRM * : </label>
	                    <input type="text" name="id_status_crm" size="1" maxlength="1" />
	                    <br />
	                    <label for="status_text">Status text: </label>
	                    <input type="text" name="status_text" />
	                    <br />
	                    <label for="data">Data : </label>
	                    <input type="text" name="data" />
	                    <br />
	                    <input class="button" type="submit" value="Update" />
	                </form>
	            </div>
	            
	            <div class="highlight-box">
	                <h2>Delete a demand</h2>
	                <form action="rest/crm/demand/delete" method="post">
	                    <label for="id_demand">ID demand * : </label>
	                    <input type="text" name="id_demand" />
	                    <br />
	                    <input class="button" type="submit" value="Delete" />
	                </form>
	            </div>
	            
	            <div class="highlight-box">
	                <h2>Notify</h2>
	                <form action="rest/crm/demand/notify" method="post">
	                    <label for="id_demand">ID demand * : </label>
	                    <input type="text" name="id_demand" />
	                    <br />
	                    <label for="notification_object">Object * : </label>
	                    <input type="text" name="notification_object" size="50" maxlength="255" />
	                    <br />
	                    <label for="notification_message">Message : </label>
	                    <textarea name="notification_message"></textarea>
	                    <br />
	                    <label for="notification_sender">Sender : </label>
	                    <input type="text" name="notification_sender" size="50" maxlength="255" />
	                    <br />
	                    <input class="button" type="submit" value="Send" />
	                </form>
	            </div>
	            
	            <div class="highlight-box">
	                <h2>View CRM user attributes</h2>
	                <form name="formGetUserAttributes">
	                    <label for="user_guid">User GUID * : </label>
	                    <input type="text" name="user_guid" />
	                    <br/>
	                    <label for="format">Format :</label>
	                    <select name="format">
	                        <option value=".xml">XML</option>
	                        <option value=".json">JSON</option>
	                    </select>
	                    <br/>
	                    <input class="button" type="button" value="View" onclick="javascript:onUserAttributesView(  )"/>
	                </form>
	            </div>
	            
	            <div class="highlight-box">
	                <h2>View CRM user attribute</h2>
	                <form name="formGetUserAttribute">
	                    <label for="user_guid">User GUID * : </label>
	                    <input type="text" name="user_guid" />
	                    <br/>
	                    <label for="attribute">Attribute :</label>
	                    <input type="text" name="attribute" />
	                    <br/>
	                    <input class="button" type="button" value="View" onclick="javascript:onUserAttributeView(  )"/>
	                </form>
	            </div>
        	</div>
        </div>
    </body>
</html>
