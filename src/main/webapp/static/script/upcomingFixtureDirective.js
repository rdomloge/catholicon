myApp.directive('upcomingFixture', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
			fixture: '='
		},
		templateUrl : 'partials/upcomingFixture.html',
		controller : 'upcomingFixtureController'
	}
});

myApp.controller('upcomingFixtureController', ['$scope', function($scope) {
	
	
}]);