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

myApp.controller('matchListController', ['$routeParams', 'matchListFactory', '$log', '$scope', '$timeout', '$rootScope', function($routeParams, matchListFactory, $log, $scope, $timeout, $rootScope) {
	matchListFactory.getMatches($routeParams.teamId, $routeParams.season).success(function(data) {
		$log.debug("Data received for matches", data);
		$scope.matches = data;
	});
	
	$scope.showFixture = function(fixtureId) {
		$scope.showFixtureDetailsDialogue = true;
		matchListFactory.getFixtureDetail(fixtureId).success(function(data) {
			$log.debug("Data received for fixture", data);
			$scope.fixtureDetails = data;
		});
	}
}]);