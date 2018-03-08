<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <head>
        <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" />
        <link href="/css/main.css" rel="stylesheet" />
    </head>
    <body>
        <div class="group">
            <h1> ${requestedGroupName} </h1>
            <c:choose>
                <c:when test="${empty group}">
                    <div class="noGroups">
                        No such group exists
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row groupClass" ng-app="editGroupApp" ng-controller="editGroupCtrl">
                         <div class="groupData col-sm-6 tableAlignedForm">
                             <h3> Group Details : </h3>
                             <form>
                                 <input type="hidden" ng-model="serverSideGroupName">
                                 <p>
                                    <label> Group name </label>
                                    <input type="text" required="required" ng-model="groupName"/>
                                 </p>
                                 <button ng-click="changeGroupName()"> UPDATE </button>
                                 <button ng-click="deleteGroup()"> DELETE </button>
                             </form>
                         </div>
                         <div class="groupUsers col-sm-6">
                             <h3> My Users : </h3>
                             <div class="groupUser" ng-repeat="userName in groupUserNames">
                                <a ng-href="/users/{{userName}}"> {{userName}} </a>
                                <button ng-click="deleteFromGroup(userName)"> DELETE </button>
                                <br>
                             </div>
                         </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <script>
                var app3 = angular.module('editGroupApp', []);
                app3.controller('editGroupCtrl',
                    function($scope) {
                        $scope.groupName = "${group.groupName}";
                        $scope.serverSideGroupName = "${group.groupName}"
                        $scope.groupUserNames = ${groupUsersJSON};

                        $scope.deleteFromGroup = function(userName){
                            var data = {};
                            submit("/users/"  + userName + "/" + $scope.serverSideGroupName, data, "DELETE",
                                function(){
                                    $scope.groupUserNames = $scope.groupUserNames.filter( x => x !== userName);
                                    $scope.$apply();
                                }
                            );
                        };

                        $scope.changeGroupName = function(){
                            var data = {groupName : $scope.groupName };
                            var jqXHR = submit("/groups/" + $scope.serverSideGroupName, data, "POST",
                                function() {
                                    /* If you change group name, we have to update the current URL to match */
                                    if($scope.serverSideGroupName != $scope.groupName){
                                        location.href = "/groups/" + $scope.groupName;
                                    }
                                }
                            );
                        };

                        $scope.deleteGroup = function(){
                            var data = {};
                            var jqXHR = submit("/groups/" + $scope.serverSideGroupName, data, "DELETE",
                                                function() {location.reload();} );
                        };

                        var submit = function(uri,data,method, onSuccess){
                            var request = {
                                url: uri,
                                type: method,
                                contentType: "application/json",
                                cache: false,
                                data: JSON.stringify(data),
                                beforeSend: function (xhr) {
                                },
                                error: function(jqXHR) {
                                    alert ("Request Failed " +  jqXHR.status);
                                    console.log("ajax error " + jqXHR.status);
                                },
                                success: onSuccess
                            };
                            return $.ajax(request);
                        }
                    }
                );
        </script>
    </body>
</html>