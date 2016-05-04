<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:attribute name="head">
        <title>settings</title>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <t:main_panel>
            <jsp:attribute name="panel_title">Settings</jsp:attribute>
            <jsp:attribute name="panel_body">
                <div ng-controller="SettingsCtrl as settings" style="margin-top:-15px;">
                    <ul class="nav nav-tabs">
                        <li role="presentation" class="active"><a href="#">Profile</a></li>
                    </ul>
                    <div ng-show="profileSelected" style="margin-top:30px;">
                        <form class="ng-pristine ng-valid col-md-8 col-md-offset-2">
                            <div class="form-group input-group-sm">
                                <input type="text" id="inputName" class="form-control" placeholder="Name" required="" autofocus="">
                            </div>
                            <div class="form-group input-group-sm">
                                <input type="text" id="inputSurname" class="form-control" placeholder="Surname">
                            </div>
                            <div class="form-group input-group-sm">
                                <input type="number" id="inputAge" class="form-control" placeholder="Age">
                            </div>
                            <div class="form-group input-group-sm">
                                <input type="text" id="inputCountry" class="form-control" placeholder="Country">
                            </div>
                            <div class="form-group input-group-sm">
                                <input type="text" id="inputCity" class="form-control" placeholder="City">
                            </div>
                            <div class="form-group input-group-sm">
                                <input type="tel" id="inputPhone" class="form-control" placeholder="Phone">
                            </div>
                            <div class="form-group input-group-sm">
                                <input type="text" id="inputInfo" class="form-control" placeholder="Info">
                            </div>
                            <br>
                            <button class="btn btn-success col-md-4 pull-right" type="submit">Save</button>
                        </form>
                    </div>
                </div>
            </jsp:attribute>
        </t:main_panel>
    </jsp:body>
</t:template>