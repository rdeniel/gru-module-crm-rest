<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>

<html>
    <head>
        <title>CRM - REST webservices test page</title>
        <base href="<%= AppPathService.getBaseUrl( request ) %>" />
        <link rel="stylesheet" type="text/css" href="css/portal_admin.css" title="lutece_admin" />
        <script type="text/javascript">
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
            <h1>CRM User Attribute - REST webservices test page </h1>
            <div class="content-box">
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
