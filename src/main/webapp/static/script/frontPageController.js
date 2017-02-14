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
	
	return frontPageFactory;
});


myApp.controller('frontPageController', 
		['$scope', '$log', 'frontPageFactory', 'errorHandlerFactory', '$cookies', '$timeout',
        function($scope, $log, frontPageFactory, errorHandlerFactory, $cookies, $timeout) {
			
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
	
	frontPageFactory.getUpcomingFixtures().success(function(data) {
		$log.debug("Data received for upcoming fixtures", data);
		$scope.upcomingFixtures = data;
	}).error(errorHandlerFactory.getHandler());
	
	$scope.$watch('frontPageFilter', function() {
		$log.debug('frontPageFilter has just changed', $scope.frontPageFilter);
		var yearFromNow = new Date(new Date().setFullYear(new Date().getFullYear() + 1));
		$cookies.put('front-page-search', $scope.frontPageFilter, {expires: yearFromNow});
	});
}]);

