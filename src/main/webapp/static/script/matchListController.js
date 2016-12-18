myApp.factory('matchListFactory', function($http, $log) {
	
	var factory = {};
	
	factory.getMatches = function(team, season) {
		$log.info("Loading matches for "+team);
		return $http.get(Config.BASE_URL+'/season/'+season+'/matches/'+team+'/list');
	}
	
	factory.getFixtureDetail = function(fixtureId) {
		$log.info("Loading fixture detail for "+fixtureId);
		return $http.get(Config.BASE_URL+'/fixture/'+fixtureId);
	};
	
	return factory;
});

myApp.factory('matchService', function() {
	var matchService = {};
	return matchService;
});

myApp.controller('matchListController', 
		['$routeParams', 'matchListFactory', '$log', '$scope', '$timeout', '$rootScope', 'matchService', 
			function($routeParams, matchListFactory, $log, $scope, $timeout, $rootScope, matchService) {
			
	matchListFactory.getMatches($routeParams.teamId, $routeParams.season).success(function(data) {
		$log.debug("Data received for matches", data);
		$scope.matches = data;
		$scope.teamName = 
			$scope.matches[0].homeTeamId == $routeParams.teamId 
			? $scope.matches[0].homeTeamName 
			: $scope.matches[0].awayTeamName;
			
		matchService.teamMatchesId = $routeParams.teamId;
		matchService.teamMatchesSeason = $routeParams.season;
	});
	
	$scope.showFixture = function(fixtureId) {
		$scope.$broadcast('fixture-details', {id: fixtureId});
	}
	
	$scope.teamId = $routeParams.teamId;
	$scope.season = $routeParams.season;
}]);