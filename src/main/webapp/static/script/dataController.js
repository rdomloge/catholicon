
myApp.factory('seasonFactory', function($http, $log) {
	var factory = {};
	var seasons;
	
	$log.info("Loading seasons list");
	seasons = $http.get(Config.MS_SEASONS_BASE+'?sort=seasonStartYear,desc');
	
	factory.getSeasonList = function() {
		return seasons;
	}
	
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

myApp.controller('seasonListController', function($scope, $log, seasonFactory, $q) {
	$q.when(seasonFactory.getSeasonList()).then(function(page) {
		$log.debug("Data received for seasons", page.data._embedded.seasons);
		$scope.seasons = page.data._embedded.seasons;
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
