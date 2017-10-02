myApp.factory('divisionFactory', function($http, $log) {
	var factory = {};
	
	factory.getDivisions = function(season, leagueTypeId) {
		$log.info("Loading divisions for league " + leagueTypeId);
		return $http.get(Config.BASE_URL+'/season/'+season+'/league/'+leagueTypeId+'/divisions');		
	};
	
	return factory;
});

myApp.directive('divisionMenuItemDirective', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
			season: '=',
			league: '='
		},
		templateUrl : 'divisionTemplate.html',
		controller : 'divisionMenuItemController'
	}
});

myApp.controller('divisionMenuItemController', ['$scope', 'divisionFactory', '$log', function($scope, divisionFactory, $log) {
	divisionFactory.getDivisions($scope.season, $scope.league.leagueTypeId).success(
		function(data) {
			$log.debug("Data received for divisions", data);
			$scope.divisions = data;
		}
	);
}]);

