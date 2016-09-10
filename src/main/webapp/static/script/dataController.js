var myApp = angular.module('app', ['ngRoute']);

myApp.factory('dataFactory', function($http, $log) {
	var factory = {};
	
	factory.getLeagues = function() {
		$log.info("Fetching list of leagues");
		return $http.get('/catholicon/season/{SEASON}/league/list');	
	}
	
	factory.getSeasonList = function() {
		return $http.get('/catholicon/season/list');
	}
	
	factory.getMatches = function(team) {
		$log.info("Loading matches for "+team);
		return $http.get('/catholicon/season/{SEASON}/matches/'+team+'/list');
	}
	
	factory.getDivisions = function(leagueTypeId) {
		$log.info("Loading divisions for league " + leagueTypeId);
		return $http.get('/catholicon/season/{SEASON}/league/'+leagueTypeId+'/divisions');	
	}

	factory.getMatchCard = function(fixtureId) {
		$log.info("Loading match card for "+fixtureId);
		return $http.get('/catholicon/matchcard/'+fixtureId);
	}
	
	factory.getDivision = function(leagueTypeId, divisionId) {
		$log.info("Loading division "+divisionId+" for "+leagueTypeId);
		return $http.get('/catholicon/season/{SEASON}/league/'+leagueTypeId+"/division/"+divisionId);
	};
	
	return factory;
});

myApp.controller('leagueDivisionListController', ['$routeParams', 'dataFactory', '$log', '$scope', '$timeout', '$rootScope', function($routeParams, dataFactory, $log, $scope, $timeout, $rootScope) {
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

myApp.controller('leagueListController', ['$scope', '$log', 'dataFactory', '$routeParams', '$timeout', '$rootScope', '$location', function($scope, $log, dataFactory, $routeParams, $timeout, $rootScope, $location) {
	dataFactory.getLeagues().success(function(data) {
		$log.debug("Data received for leagues", data);
		$scope.leagues = data;
	});
}]);

function getQueryParams(qs) {
    qs = qs.split('+').join(' ');

    var params = {},
        tokens,
        re = /[?&]?([^=]+)=([^&]*)/g;

    while (tokens = re.exec(qs)) {
        params[decodeURIComponent(tokens[1])] = decodeURIComponent(tokens[2]);
    }

    return params;
}

myApp.controller('divisionController', ['$routeParams', 'dataFactory', '$log', '$scope', '$timeout', '$rootScope', function($routeParams, dataFactory, $log, $scope, $timeout, $rootScope) {
	dataFactory.getDivision($routeParams.leagueTypeId, $routeParams.divisionId).success(function(data) {
		$log.debug("Data received for division "+$routeParams.divisionId, data);
		$scope.division = data;
		$rootScope.$broadcast('breadcrumb');
	});
}]);

myApp.controller('matchListController', ['$routeParams', 'dataFactory', '$log', '$scope', '$timeout', '$rootScope', function($routeParams, dataFactory, $log, $scope, $timeout, $rootScope) {
	dataFactory.getMatches($routeParams.teamId).success(function(data) {
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

myApp.config([ "$httpProvider", function($httpProvider) {
	$httpProvider.interceptors.push(function($q, $log, $rootScope) {
		return {
			'request' : function(config) {
				$log.debug('Request started');
				$rootScope.$broadcast('started-thinking');
				var query = getQueryParams(document.location.search);
				var seasonStartYear = query.season ? query.season : 0;
				if(config.url.includes('{SEASON}')) {
					config.url = config.url.replace('{SEASON}', seasonStartYear);
				}
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
	
	$scope.trail = [ ];
	
	$scope.$on('breadcrumb', function(event) {
		$log.debug('New breadcrumb', event);
		$scope.trail.push("New:");
	});
});
