var myApp = angular.module('app', ['ngRoute']);

myApp.factory('dataFactory', function($http, $log) {
	var factory = {};
	
	factory.getLeagues = function() {
		$log.info("Fetching list of leagues");
		return $http.get('/catholicon/league/list');
	}
	
	factory.getMatches = function(team) {
		$log.info("Loading matches for "+team);
		return $http.get('/catholicon/matches/'+team+'/list');
	}
	
	factory.getDivisions = function(leagueTypeId) {
		$log.info("Loading divisions for league " + leagueTypeId);
		return $http.get('/catholicon/league/'+leagueTypeId+'/divisions');
	}

	factory.getMatchCard = function(fixtureId) {
		$log.info("Loading match card for "+fixtureId);
		return $http.get('/catholicon/matchcard/'+fixtureId);
	}
	
	factory.getDivision = function(leagueTypeId, divisionId) {
		$log.info("Loading division '+divisionId+' for "+leagueTypeId);
		return $http.get('/catholicon/league/'+leagueTypeId+"/division/"+divisionId);
	};
	
	return factory;
});

myApp.controller('leagueDivisionListController', ['$routeParams', 'dataFactory', '$log', '$scope', function($routeParams, dataFactory, $log, $scope) {
	$log.debug("Fetching league "+$routeParams.leagueTypeId +" divisions");
	dataFactory.getDivisions($routeParams.leagueTypeId).success(function(data) {
		$log.debug("Data received for league "+$routeParams.leagueTypeId+" divisions", data);
		$scope.divisions = data;
	});
}]);

myApp.controller('leagueController', ['$routeParams', 'dataFactory', '$log', '$scope', function($routeParams, dataFactory, $log, $scope) {
	$log.debug("Fetching league "+$routeParams.leagueTypeId);
	dataFactory.getLeague($routeParams.leagueTypeId).success(function (data){
		$log.debug("Data received for league", data);
		$scope.league = data;
	});
}]);

myApp.controller('leagueListController', function($scope, $log, dataFactory, $routeParams) {
	dataFactory.getLeagues().success(function(data) {
		$log.debug("Data received for leagues", data);
		$scope.leagues = data;
	});
});

myApp.controller('divisionController', ['$routeParams', 'dataFactory', '$log', '$scope', function($routeParams, dataFactory, $log, $scope) {
	dataFactory.getDivision($routeParams.leagueTypeId, $routeParams.divisionId).success(function(data) {
		$log.debug("Data received for division "+$routeParams.divisionId, data);
		$scope.division = data;
	});
}]);


myApp.controller('dataController', function ($scope, $log, $http, dataFactory) {
	$log.debug('Data controller initiated');
	
	$scope.getMatchCard = function(fixtureId) {
		$log.debug("Getting match card for "+fixtureId);
		dataFactory.getMatchCard(fixtureId).success(function(data) {
			$log.debug("Data received for match card", data);
			$scope.matchCard = data;
		});
		
	}
});
