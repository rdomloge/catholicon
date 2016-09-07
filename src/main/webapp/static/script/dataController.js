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
		$log.info("Loading division "+divisionId+" for "+leagueTypeId);
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

myApp.controller('leagueListController', ['$scope', '$log', 'dataFactory', '$routeParams', '$timeout', '$rootScope', function($scope, $log, dataFactory, $routeParams, $timeout, $rootScope) {
	$timeout(function(){
		$rootScope.$broadcast('started-thinking', 'leagueListController');	
	});
	
	dataFactory.getLeagues().success(function(data) {
		$log.debug("Data received for leagues", data);
		$scope.leagues = data;
		$timeout(function(){
			$rootScope.$broadcast('finished-thinking', 'leagueListController');
		}, 3000);
	});
}]);

myApp.controller('thinkingController', ['$scope', '$log', function($scope, $log) {
	$scope.$on('started-thinking', function(event, sourceController) {
		$log.debug('Started thinking', sourceController);
		$scope.contentLoading = true;
	});
	
	$scope.$on('finished-thinking', function(event, sourceController) {
		$log.debug('Finished thinking', sourceController);
		$scope.contentLoading = false;
	});
}]);

myApp.controller('divisionController', ['$routeParams', 'dataFactory', '$log', '$scope', function($routeParams, dataFactory, $log, $scope) {
	dataFactory.getDivision($routeParams.leagueTypeId, $routeParams.divisionId).success(function(data) {
		$log.debug("Data received for division "+$routeParams.divisionId, data);
		$scope.division = data;
	});
}]);

myApp.controller('matchListController', ['$routeParams', 'dataFactory', '$log', '$scope', function($routeParams, dataFactory, $log, $scope) {
	dataFactory.getMatches($routeParams.teamId).success(function(data) {
		$log.debug("Data received for matches", data);
		$scope.matches = data;
	});
}]);

myApp.controller('matchCardController', function ($scope, $log, $http, dataFactory) {
	$log.debug('Data controller initiated');
	
	$scope.getMatchCard = function(fixtureId) {
		$log.debug("Getting match card for "+fixtureId);
		dataFactory.getMatchCard(fixtureId).success(function(data) {
			$log.debug("Data received for match card", data);
			$scope.matchCard = data;
		});
		
	}
});
