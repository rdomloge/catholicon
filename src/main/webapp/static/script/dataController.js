var myApp = angular.module('app', ['ngRoute']);

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
	
	factory.getUpcomingFixtures = function() {
		$log.info("Loading upcoming fixtures");
		return $http.get('/catholicon/frontpage/upcoming');
	};
	
	return factory;
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

myApp.controller('matchCardController', function ($scope, $log, $http, dataFactory, $routeParams) {
	$log.debug("Getting match card for "+$routeParams.fixtureId);
	dataFactory.getMatchCard($routeParams.fixtureId).success(function(data) {
		$log.debug("Data received for match card", data);
		$scope.matchCard = data;
	});
});

myApp.controller('seasonListController', function($scope, $log, dataFactory) {
	dataFactory.getSeasonList().success(function(data) {
		$log.debug("Data received for seasons", data);
		$scope.seasons = data;
	});
});

myApp.controller('frontPageController', function($scope, $log, dataFactory) {
	dataFactory.getUpcomingFixtures().success(function(data) {
		$log.debug("Data received for upcoming fixtures", data);
		$scope.upcomingFixtures = data;
	});
});

myApp.config([ "$httpProvider", function($httpProvider) {
	$httpProvider.interceptors.push(function($q, $log, $rootScope) {
		return {
			'request' : function(config) {
				$log.debug('Request started');
				$rootScope.$broadcast('started-thinking');
				return config;
			},

			'response' : function(response) {
				$log.debug('Response received');
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
		$log.debug('Started thinking', thinkers);
		$scope.contentLoading = true;
	});
	
	$scope.$on('finished-thinking', function(event) {
		thinkers--;
		$log.debug('Finished thinking', thinkers);
		if(thinkers < 1) {
			$scope.contentLoading = false;
			$log.debug('Hiding throbber');
		}
	});
}]);

myApp.controller('breadcrumbController', function($scope, $log) {
	
});
