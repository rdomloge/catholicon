myApp.controller('JwtLoginController', ['mainService','$scope','$http',
        function(mainService, $scope, $http) {
            $scope.greeting = 'Welcome to the JSON Web Token / AngularJR / Spring example!';
            $scope.token = null;
            $scope.error = null;
            $scope.roleUser = false;
            $scope.roleAdmin = false;
            $scope.roleFoo = false;

            $scope.login = function() {
                $scope.error = null;
                mainService.login($scope.user.name).then(function(token) {
                    $scope.token = token;
                    $http.defaults.headers.common.Authorization = 'Bearer ' + token;
                    $scope.makeSecureCall();
                },
                function(error){
                    $scope.error = error
                    $scope.userName = '';
                });
            }

            $scope.logout = function() {
                $scope.userName = '';
                $scope.token = null;
                $http.defaults.headers.common.Authorization = '';
            }

            $scope.loggedIn = function() {
                return $scope.token !== null;
            }
            
            $scope.makeCallThatNeedsSecurity = function() {
            	$http.get(Config.BASE_URL+'/api/claims').then(function(response){
                    console.log(response);
                    return response.data;
                });
            };
            
            $scope.makeSecureCall = function() {
            	mainService.makeSecureCall();
            };
        } ]);

myApp.service('mainService', function($http) {
    return {
        login : function(username) {
            return $http.post(Config.BASE_URL+'/user/login', {name: username}).then(function(response) {
                return response.data.token;
            });
        },

        makeSecureCall : function() {
            return $http.get(Config.BASE_URL+'/api/claims').then(function(response){
                console.log(response.data);
                return response.data;
            });
        }
    };
});