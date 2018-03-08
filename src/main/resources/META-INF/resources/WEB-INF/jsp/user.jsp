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
	<div class="user">
        <h1> ${requestedUserName} </h1>
        <c:choose>
            <c:when test="${empty user}">
                <div class="basicText noUsers">
                    No such user exists
                </div>
            </c:when>
            <c:otherwise>
                <div class="row userClass" ng-app="editUserApp" ng-controller="editUserCtrl">
                     <div class="userData col-sm-6 tableAlignedForm">
                         <h3> User details : </h3>
                         <form>
                             <input type="hidden" ng-model="serverSideUserName">
                             <p>
                                <label> Username: </label>
                                <input type="text" required="required" ng-model="userName"/>
                             </p>
                             <p>
                                <label> First name: </label>
                                <input type="text" required="required" ng-model="firstName"/>
                             </p>
                             <p>
                                <label> Last name: </label>
                                <input type="text" required="required" ng-model="lastName"/>
                             </p>
                             <p>
                                <label> Date of Birth: </label>
                                <input type="date" required="required" ng-model="dateOfBirth"/>
                             </p>
                             <p>
                                <label> Password: </label>
                                <input type="password" required="required" ng-model="password"/>
                             </p>
                             <p>
                                <label> New Password: </label>
                                <input type="password"  ng-model="newPassword"/>
                             </p>
                             <button ng-click="updateUser()"> SUBMIT </button>
                             <button ng-click="deleteUser()"> DELETE </button>
                         </form>
                     </div>
                     <div class="userGroups col-sm-6">
                         <h3> My Groups :</h3>
                         <div class="userGroup" ng-repeat="groupName in userGroupNames">
                            <a ng-href="/groups/{{groupName}}"> {{groupName}} </a>
                            <button ng-click="deleteFromGroup(groupName)"> DELETE </button>
                            <br>
                         </div>
                         <span class="newGroup"> New Group: <input type="text" ng-model="newGroupName"/> <button ng-click="addToGroup(newGroupName)"> SUBMIT </button> </span>
                         <span class="glyphicon glyphicon-search"></span></p>
                     </div>
                </div>
            </c:otherwise>
        </c:choose>
	</div>
    <script>
            var app3 = angular.module('editUserApp', []);
            app3.controller('editUserCtrl',
                function($scope) {
                    $scope.userName = "${user.userName}";
                    $scope.serverSideUserName = "${user.userName}"
                    $scope.lastName = "${user.lastName}";
                    $scope.firstName = "${user.firstName}";
                    $scope.dateOfBirth = new Date("${user.getDateOfBirth().toLocalDate()}");
                    $scope.dateOfBirth2 = new Date("${user.dateOfBirth}");
                    $scope.userGroupNames = ${userGroupsJSON};

                    $scope.deleteFromGroup = function(groupName){
                        var data = {};
                        submit("/users/"  + $scope.serverSideUserName + "/" + groupName, data, "DELETE",
                            function(){
                                $scope.userGroupNames = $scope.userGroupNames.filter( x => x !== groupName);
                                $scope.$apply();
                            }
                        );
                    };
                    $scope.addToGroup = function(newGroupName){
                        var data = {};
                        submit("/users/" + $scope.serverSideUserName + "/" + newGroupName, data, "POST",
                            function() {
                                $scope.userGroupNames.push(newGroupName);
                                $scope.$apply();
                            }
                        );
                    };

                    $scope.updateUser = function(){
                        var data = {userName : $scope.userName, firstName : $scope.firstName,
                                    lastName : $scope.lastName, password : $scope.password,
                                    dateOfBirth: $scope.dateOfBirth, newPassword : $scope.newPassword
                                    };
                        var jqXHR = submit("/users/" + $scope.serverSideUserName, data, "POST",
                            function() {
                                /* If you change user name, we have to update the current URL to match your name */
                                if($scope.serverSideUserName != $scope.userName){
                                    location.href = "/users/" + $scope.userName;
                                }
                            }
                        );
                          console.log("JxQHR status = ");
                                                debugger;


                    };

                    $scope.deleteUser = function(){
                        var data = {userName : null, firstName : null,
                                    lastName : null, password : $scope.password,
                                    newPassword : null, dateOfBirth : null
                                    };

                        var jqXHR = submit("/users/" + $scope.serverSideUserName, data, "DELETE",
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
                                /* xhr.setRequestHeader("Authorization",
                                    "Basic " + btoa(self.username + ":" + self.password)); */
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