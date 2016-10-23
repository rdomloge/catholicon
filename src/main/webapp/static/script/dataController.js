var myApp = angular.module('app', ['ngRoute', 'ngCookies']);

myApp.factory('dataFactory', function($http, $log) {
	var factory = {};
	
	factory.getLeagues = function(season) {
		$log.info("Fetching list of leagues");
		return $http.get('/catholicon/season/'+season+'/league/list');	
	}
	
	factory.getSeasonList = function() {
		return $http.get('/catholicon/season/list');
	}
	
	factory.getMatches = function(team, season) {
		$log.info("Loading matches for "+team);
		return $http.get('/catholicon/season/'+season+'/matches/'+team+'/list');
	}
	
	factory.getDivisions = function(leagueTypeId, season) {
		$log.info("Loading divisions for league " + leagueTypeId);
		return $http.get('/catholicon/season/'+season+'/league/'+leagueTypeId+'/divisions');	
	}

	factory.getMatchCard = function(fixtureId) {
		$log.info("Loading match card for "+fixtureId);
		return $http.get('/catholicon/matchcard/'+fixtureId);
	}
	
	factory.getDivision = function(leagueTypeId, divisionId, season) {
		$log.info("Loading division "+divisionId+" for "+leagueTypeId);
		return $http.get('/catholicon/season/'+season+'/league/'+leagueTypeId+"/division/"+divisionId);
	};
	
	factory.getPlayerReport = function(season, league) {
		$log.info("Loading player report");
		return $http.get('/catholicon/season/'+season+'/league/'+league+'/report');
	};
	
	return factory;
});

myApp.factory('errorHandlerFactory', function($log, $rootScope) {
	var fac = {};
	fac.getHandler = function() {
		return function(error, status, headers, config, statusText) {
			$log.debug('Call failed ('+status+'): ', config.url);
			$rootScope.$broadcast('finished-thinking');
			$rootScope.$broadcast('error-occurred');
		};
	}	
	return fac;
});

myApp.controller('leagueDivisionListController', ['$routeParams', 'dataFactory', '$log', '$scope', '$timeout', '$rootScope', function($routeParams, dataFactory, $log, $scope, $timeout, $rootScope) {
	$log.debug("Fetching league "+$routeParams.leagueTypeId +" divisions");
	dataFactory.getDivisions($routeParams.leagueTypeId, $routeParams.season).success(function(data) {
		$log.debug("Data received for league "+$routeParams.leagueTypeId+" divisions", data);
		$scope.divisions = data;
	});
}]);

myApp.controller('leagueController', ['$routeParams', 'dataFactory', '$log', '$scope', function($routeParams, dataFactory, $log, $scope) {
	$log.debug("Fetching league "+$routeParams.leagueTypeId+' for season '+$routeParams.season);
	
	dataFactory.getLeague($routeParams.leagueTypeId, $routeParams.season).success(function (data){
		$log.debug("Data received for league", data);
		$scope.league = data;
	});
}]);

myApp.controller('leagueListController', ['$scope', '$log', 'dataFactory', '$routeParams', '$timeout', '$rootScope', '$location', function($scope, $log, dataFactory, $routeParams, $timeout, $rootScope, $location) {
	dataFactory.getLeagues($routeParams.season).success(function(data) {
		$log.debug("Data received for leagues", data);
		$scope.leagues = data;
	});
}]);

myApp.controller('divisionController', ['$routeParams', 'dataFactory', '$log', '$scope', '$timeout', '$rootScope', function($routeParams, dataFactory, $log, $scope, $timeout, $rootScope) {
	dataFactory.getDivision($routeParams.leagueTypeId, $routeParams.divisionId, $routeParams.season)
			.success(function(data) {
		$log.debug("Data received for division "+$routeParams.divisionId, data);
		$scope.division = data;
	});
}]);

myApp.controller('matchListController', ['$routeParams', 'dataFactory', '$log', '$scope', '$timeout', '$rootScope', function($routeParams, dataFactory, $log, $scope, $timeout, $rootScope) {
	dataFactory.getMatches($routeParams.teamId, $routeParams.season).success(function(data) {
		$log.debug("Data received for matches", data);
		$scope.matches = data;
	});
}]);

myApp.controller('seasonListController', function($scope, $log, dataFactory) {
	dataFactory.getSeasonList().success(function(data) {
		$log.debug("Data received for seasons", data);
		$scope.seasons = data;
	});
});

myApp.controller('playerReportController', function($scope, $log, dataFactory, $routeParams) {
	dataFactory.getPlayerReport($routeParams.season, $routeParams.league).success(function(data) {
		$log.debug("Data received for playerReport", data);
		$scope.playerReport = data;
	});
	
});

myApp.config([ "$httpProvider", function($httpProvider) {
	$httpProvider.interceptors.push(function($q, $log, $rootScope) {
		return {
			'request' : function(config) {
				$rootScope.$broadcast('started-thinking');
				return config;
			},

			'response' : function(response) {
				$rootScope.$broadcast('finished-thinking');
				return response;
			}
		};
	});
} ]);


myApp.controller('thinkingController', ['$scope', '$log', function($scope, $log) {
	
	var thinkers = 0;
	
	$scope.$on('started-thinking', function(event) {
		thinkers++;
		$scope.contentLoading = true;
	});
	
	$scope.$on('finished-thinking', function(event) {
		thinkers--;
		if(thinkers < 1) {
			$scope.contentLoading = false;
		}
	});
}]);

myApp.controller('errorController', ['$scope', '$log', function($scope, $log) {
	
	$scope.$on('error-occurred', function(event) {
		$log.debug('Showing error notification');
		$scope.errorOccurred = true;
	});
}]);
