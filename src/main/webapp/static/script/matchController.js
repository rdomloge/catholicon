var myApp = angular.module('app', ['ngRoute']);

myApp.factory('matchFactory', function($http, $log) {
	var factory = {};
	factory.getMatches = function(team) {
		$log.info("Loading matches for "+team);
		return $http.get('/catholicon/matches/list?team='+team);
	}
	return factory;
});

myApp.factory('leagueFactory', function($http, $log) {
	var factory = {};
	factory.getLeague= function(league) {
		$log.info("Loading league for "+league);
		return $http.get('/catholicon/league?league='+league);
	}
	return factory;
});

myApp.controller('openAController', function ($scope, $log, $http, matchFactory) {
	matchFactory.getMatches("opena").success(function(data){
			$scope.matches = data;
		});
	}			
);

myApp.controller('mixedAController',  function ($scope, $log, $http, matchFactory) {
	matchFactory.getMatches("mixeda").success(function(data){
			$scope.matches = data;
		});
 	}			
);

myApp.controller('open1Controller',  function ($scope, $log, $http, leagueFactory) {
	leagueFactory.getLeague("open1").success(function(data){
			$scope.league = data;
		});
	}			
);

myApp.controller('open2Controller',  function ($scope, $log, $http, leagueFactory) {
	leagueFactory.getLeague("open2").success(function(data){
			$scope.league = data;
		});
	}			
);

myApp.controller('open3Controller',  function ($scope, $log, $http, leagueFactory) {
	leagueFactory.getLeague("open3").success(function(data){
			$scope.league = data;
		});
	}			
);

myApp.controller('mixed1Controller',  function ($scope, $log, $http, leagueFactory) {
	leagueFactory.getLeague("mixed1").success(function(data){
			$scope.league = data;
		});
	}			
);

myApp.controller('mixed2Controller',  function ($scope, $log, $http, leagueFactory) {
	leagueFactory.getLeague("mixed2").success(function(data){
			$scope.league = data;
		});
	}			
);