<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>

<html>
    <head>
        <title>CRM - REST webservices test page</title>
        <base href="<%= AppPathService.getBaseUrl( request ) %>" />
        <link rel="stylesheet" type="text/css" href="css/portal_admin.css" title="lutece_admin" />
    </head>
    <body>
        <div id="content" >
            <h1>CRM Notification - REST webservices test page </h1>
            <div class="content-box">
	            <div class="highlight-box">
	                <h2>Notify</h2>
	                <form action="rest/crm/notify/demand" method="post">
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
        	</div>
        </div>
    </body>
</html>
