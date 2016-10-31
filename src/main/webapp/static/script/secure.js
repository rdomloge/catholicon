myApp.controller('LoginController', function($scope, $http) {
	$scope.login = function() {
		$http.post('../../secure/doLogin', $scope.user);
	}
});