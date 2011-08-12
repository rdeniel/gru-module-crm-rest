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

            function onGetUserGuidView(  ) {
                var id_demand = document.formGetUserGuid.id_demand.value;
                document.location= 'rest/crm/demand/' + id_demand + '/user_guid';
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
	                <h2>Get User GUID</h2>
	                <form name="formGetUserGuid">
	                    <label for="id_demand">ID demand * : </label>
	                    <input type="text" name="id_demand" />
	                    <br/>
	                    <input class="button" type="button" value="View" onclick="javascript:onGetUserGuidView(  )"/>
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
        	</div>
        </div>
    </body>
</html>
