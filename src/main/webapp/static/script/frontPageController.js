myApp.controller('frontPageController', 
		['$scope', '$log', 'dataFactory', 'errorHandlerFactory', '$cookies', '$rootScope', '$timeout',
        function($scope, $log, dataFactory, errorHandlerFactory, $cookies, $rootScope, $timeout) {

	$rootScope.frontPageFilter = $cookies.get('front-page-search');
	if(null != $rootScope.frontPageFilter) {
		$rootScope.showFilteringMsg = true;
		$timeout(function() {
			$rootScope.showFilteringMsg = false;	
		}, 3000);
	}
	
	dataFactory.getUpcomingFixtures().success(function(data) {
		$log.debug("Data received for upcoming fixtures", data);
		$scope.upcomingFixtures = data;
	}).error(errorHandlerFactory.getHandler());
	
	$scope.$watch('frontPageFilter', function() {
		$log.debug('frontPageFilter has just changed', $scope.frontPageFilter);
		$cookies.put('front-page-search', $scope.frontPageFilter);
	});
	
	$scope.showFixture = function(fixtureId) {
		$rootScope.showFixtureDetailsDialogue = true;
		dataFactory.getFixtureDetail(fixtureId).success(function(data) {
			$log.debug("Data received for fixture", data);
			$rootScope.fixtureDetails = data;
		});
	}
	
	$rootScope.hideFixtureDetails = function() {
		$rootScope.showFixtureDetailsDialogue = false;
		$rootScope.fixtureDetails = undefined;
	}
}]);

