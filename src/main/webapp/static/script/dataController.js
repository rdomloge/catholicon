
myApp.factory('dataFactory', function($http, $log) {
	var factory = {};
	
	factory.getLeagues = function(season) {
		$log.info("Fetching list of leagues");
		return $http.get(Config.BASE_URL+'/season/'+season+'/league/list');	
	}
	
	factory.getSeasonList = function() {
		$log.info("Loading seasons list");
		return $http.get(Config.BASE_URL+'/seasons');
	}
	
	factory.getDivisions = function(leagueTypeId, season) {
		$log.info("Loading divisions for league " + leagueTypeId);
		return $http.get(Config.BASE_URL+'/season/'+season+'/league/'+leagueTypeId+'/divisions');	
	}

	factory.getMatchCard = function(fixtureId) {
		$log.info("Loading match card for "+fixtureId);
		return $http.get(Config.BASE_URL+'/matchcard/'+fixtureId);
	}
	
	factory.getDivision = function(leagueTypeId, divisionId, season) {
		$log.info("Loading division "+divisionId+" for "+leagueTypeId);
		return $http.get(Config.BASE_URL+'/season/'+season+'/league/'+leagueTypeId+"/division/"+divisionId);
	};
	
	factory.getPlayerReport = function(season, league) {
		$log.info("Loading player report");
		return $http.get(Config.BASE_URL+'/season/'+season+'/league/'+league+'/report');
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
