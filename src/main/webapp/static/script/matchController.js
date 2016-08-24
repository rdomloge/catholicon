var myApp = angular.module('app', ['ngRoute']);

myApp.factory('matchFactory', function($http, $log) {
	var factory = {};
	factory.getMatches = function(team) {
		$log.info("Loading matches for "+team);
		return $http.get('/catholicon/matches/'+team+'/list');
	}
	return factory;
});

myApp.factory('leagueFactory', function($http, $log) {
	var factory = {};
	factory.getLeague= function(league) {
		$log.info("Loading league for "+league);
		return $http.get('/catholicon/league/'+league);
	}
	return factory;
});

myApp.controller('dataController', function ($scope, $log, $http, matchFactory, leagueFactory) {
	$log.debug('Match controller initiated');
	$scope.getMatches = function(team){
		$log.debug('getMatches() called');
		matchFactory.getMatches(team).success(function(data) {
			$log.debug("Data received for matches", data);
			$scope.matches = data;
		});
	}
	
	$scope.getLeague = function(league) {
		$log.debug("getLeague() called for "+league);
		leagueFactory.getLeague(league).success(function(data) {
			$log.debug("Data received for league", data);
			$scope.league = data;
		});
	}
});
