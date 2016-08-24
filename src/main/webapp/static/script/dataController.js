var myApp = angular.module('app', ['ngRoute']);

myApp.factory('dataFactory', function($http, $log) {
	var factory = {};
	factory.getMatches = function(team) {
		$log.info("Loading matches for "+team);
		return $http.get('/catholicon/matches/'+team+'/list');
	}
	
	factory.getLeague = function(league) {
		$log.info("Loading league for "+league);
		return $http.get('/catholicon/league/'+league);
	}

	factory.getMatchCard = function(fixtureId) {
		$log.info("Loading match card for "+fixtureId);
		return $http.get('/catholicon/matchcard/'+fixtureId);
	}
	
	return factory;
});


myApp.controller('dataController', function ($scope, $log, $http, dataFactory) {
	$log.debug('Data controller initiated');
	$scope.getMatches = function(team){
		$log.debug('getMatches() called');
		dataFactory.getMatches(team).success(function(data) {
			$log.debug("Data received for matches", data);
			$scope.matches = data;
		});
	}
	
	$scope.getLeague = function(league) {
		$log.debug("getLeague() called for "+league);
		dataFactory.getLeague(league).success(function(data) {
			$log.debug("Data received for league", data);
			$scope.league = data;
		});
	}
	
	$scope.getMatchCard = function(fixtureId) {
		$log.debug("Getting match card for "+fixtureId);
		dataFactory.getMatchCard(fixtureId).success(function(data) {
			$log.debug("Data received for match card", data);
			$scope.matchCard = data;
		});
		
	}
});
