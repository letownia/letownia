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

    <title> UserLibrary demo application </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <body>
        <div class="row"  ng-app="frontPageApp" ng-controller="frontPageCtrl">
            <div class = "col-sm-6 usersContainer">
                <h1> User List </h1>
                <li ng-show="!users.length">No users exist</li>
                <ol type="I" class="userList">
                    <li ng-repeat="userName in users"> <a ng-href="/users/{{userName}}"> {{userName}} </a> </li>
                </ol>
                <div class="newUser tableAlignedForm">
                    <form>
                        <p> <label> Username: </label> <input type="text" required="required" ng-model="userName"/> </p>
                        <p> <label> First name: </label> <input type="text" required="required" ng-model="firstName"/> </p>
                        <p> <label> Last name: </label> <input type="text" required="required" ng-model="lastName"/> </p>
                        <p> <label> Date of Birth: </label> <input type="date" required="required" ng-model="dateOfBirth"/> </p>
                        <p> <label> Password: </label> <input type="password" required="required" ng-model="password"/> </p>
                        <button ng-click="resetUser()">RESET</button>
                        <button ng-click="submitUser()">SUBMIT</button>
                    </form>
                </div>
            </div>
            <div class = "col-sm-6 groupsContainer">
                <h1> Group List </h1>
                <li ng-show="!groups.length">No groups exist</li>
                <ol type="I" class="groupList">
                    <li ng-repeat="groupName in groups"> <a ng-href="/groups/{{groupName}}"> {{groupName}} </a> </li>
                </ol>
                <div class="newGroup tableAlignedForm" >
                    <form> <p>
                        <label> GroupName: </label> <input type="text" required="required" ng-model="newGroupName"/></p>
                        <button ng-click="submitGroup()">SUBMIT</button>
                    </form>
                </div>
            </div>
        </div>

        <script>

            var app = angular.module('frontPageApp', []);
            app.controller('frontPageCtrl',
                function($scope) {
                    $scope.users = ${usersJSON};
                    $scope.groups = ${groupsJSON};
                    $scope.resetUser = function(){
                        $scope.user.userName = $scope.user.firstName = $scope.user.lastName = $scope.user.password = "";
                    }
                    $scope.submitGroup = function(){
                         var url = "/groups";
                         var data = { groupName : $scope.newGroupName};
                         submit(url,data,
                            function() {
                                $scope.groups.push($scope.newGroupName);
                                $scope.$apply();
                            }
                         );
                    };
                    $scope.submitUser = function(){
                         var url = "/users";
                         var data = { userName : $scope.userName, firstName : $scope.firstName,
                                      lastName : $scope.lastName, password : $scope.password,
                                      dateOfBirth : $scope.dateOfBirth};
                         submit(url,data,
                            function() {
                                $scope.users.push($scope.userName);
                                $scope.$apply();
                            }
                         );
                    };

                   submit = function(url, data, onSuccess){
                        var method = "PUT";
                        var request = {
                            url: url,
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
                            success:onSuccess
                        };
                        $.ajax(request);
                    }
                }
            );
        </script>

    </body>

</html>