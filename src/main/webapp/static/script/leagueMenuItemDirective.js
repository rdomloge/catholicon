myApp.factory('leagueFactory', function($http, $log) {
	var factory = {};
	
	factory.getLeagues = function(season) {
		$log.info("Fetching list of leagues");
		return $http.get(Config.BASE_URL+'/season/'+season+'/league/list');	
	};
	
	return factory;
});

myApp.directive('leagueMenuItemDirective', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
			season: '=',
			current: '='
		},
		templateUrl : 'leagueTemplate.html',
		controller : 'leagueMenuItemController'
	}
});

myApp.controller('leagueMenuItemController', ['$scope', 'leagueFactory', '$log', function($scope, leagueFactory, $log) {
	leagueFactory.getLeagues($scope.current ? 0 : $scope.season.seasonStartYear).success(
		function(data) {
			$log.debug("Data received for leagues", data);
			$scope.leagues = data;
		}
	);
}]);

