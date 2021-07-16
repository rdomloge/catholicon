myApp.factory('frontPageFactory', function($http, $log) {
	
	var frontPageFactory = {};
	
	frontPageFactory.getUpcomingFixtures = function() {
		$log.info("Loading upcoming fixtures");
		return $http.get(Config.BASE_URL+'/frontpage/upcoming');
	};
	
	frontPageFactory.getFixtureDetail = function(fixtureId) {
		$log.info("Loading fixture detail for "+fixtureId);
		return $http.get(Config.BASE_URL+'/fixture/'+fixtureId);
	};
	
	frontPageFactory.getRecentMatches = function() {
		$log.info("Loading recent");
		var fromDate = new Date(new Date().setDate(new Date().getDate()-17)).toISOString();
		fromDate = '2020-03-10T00:00:00.000Z'; // temp hack until new season
		var toDate = new Date().toISOString();
		return $http.get(Config.BASE_URL+'/fixtures/search/findResultsByTimeRange?isoDateFrom='+fromDate+'&isoDateTo='+toDate);
	};
	
	return frontPageFactory;
});


myApp.controller('frontPageController', 
		['$scope', '$log', 'frontPageFactory', 'errorHandlerFactory', '$cookies', '$timeout',
        function($scope, $log, frontPageFactory, errorHandlerFactory, $cookies, $timeout) {
			
	$scope.recentMatchesToggle = false;
	
	$scope.frontPageFilter = $cookies.get('front-page-search');
	if(null != $scope.frontPageFilter) {
		$scope.showFilteringMsg = true;
		$scope.filteringMsgVisible = true;
		$timeout(function() {
			$scope.showFilteringMsg = false;
			$timeout(function() {
				$scope.filteringMsgVisible = false;
			}, 2000);
		}, 3000);
	}
	
	$scope.showFixture = function(fixtureId) {
		$scope.$broadcast('fixture-details', {id: fixtureId});
	}
	
	frontPageFactory.getUpcomingFixtures().then(function(page) {
		$log.debug("Data received for upcoming fixtures", page.data);
		$scope.upcomingFixtures = page.data;
	}, errorHandlerFactory.getHandler());
	
	frontPageFactory.getRecentMatches().then(function(page) {
		$log.debug("Data received for recent matches", page.data);
		$scope.recentMatches = page.data;
	}, errorHandlerFactory.getHandler());
	
	$scope.$watch('frontPageFilter', function() {
		$log.debug('frontPageFilter has just changed', $scope.frontPageFilter);
		var yearFromNow = new Date(new Date().setFullYear(new Date().getFullYear() + 1));
		$cookies.put('front-page-search', $scope.frontPageFilter, {expires: yearFromNow});
	});
	
	$scope.offSeason = function() {
		if($scope.upcomingFixtures &&
			$scope.upcomingFixtures.length > 1) {
			return false;
		}
		return true;
	}
}]);

