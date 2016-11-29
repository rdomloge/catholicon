myApp.config(function ($routeProvider) {
	$routeProvider
		.when('/secure/welcome', 
			{
				templateUrl: 'partials/secure/welcome.html',
				controller: 'WelcomeController'
			});
});

myApp.factory('welcomeFactory', function($log, $http){
	var factory = {};
	factory.getWelcomeItems = function() {
		$log.info("Loading welcome page items");
		return $http.get(Config.BASE_URL+'/secure/welcome');
	};
	return factory;
});

myApp.controller('WelcomeController', ['$scope', '$log', 'welcomeFactory', function($scope, $log, $welcomeFactory) {
	$welcomeFactory.getWelcomeItems().success(function(data) {
		$log.debug('Data received for welcome page items', data);
		$scope.welcomePageItems = data;
	});
}]);

myApp.controller('LoginController', ['$scope', '$http', '$log', '$timeout', '$location', 
	function($scope, $http, $log, $timeout, $location) {
	
	$scope.login = function() {
		document.getElementById('id01').style.display='block';
		
		$http.post('../../secure/doLogin', $scope.user).success(function(data) {
			document.getElementById('id01').style.display='none';
			if("true" == data.IsLoggedIn) {
				$log.info('Login successful');
				document.getElementById('loginSuccess').style.display='block';
				$timeout(function() {
					document.getElementById('loginSuccess').style.display='none';
					$location.path('/secure/welcome');
				}, 1000);
			}
			else {
				$log.info('Login failed');
				document.getElementById('loginFailed').style.display='block';
				$timeout(function() {
					document.getElementById('loginFailed').style.display='none';
				}, 2000);
			}
		}).error(function(data) {
			$log.debug('Login error', data);
			document.getElementById('id01').style.display='none';
		});
	}
}]);