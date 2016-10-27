myApp.factory('frontPageFactory', function($http, $log) {
	
	var frontPageFactory = {};
	
	frontPageFactory.getUpcomingFixtures = function() {
		$log.info("Loading upcoming fixtures");
		return $http.get('/catholicon/frontpage/upcoming');
	};
	
	frontPageFactory.getFixtureDetail = function(fixtureId) {
		$log.info("Loading fixture detail for "+fixtureId);
		return $http.get('/catholicon/fixture/'+fixtureId);
	};
	
	return frontPageFactory;
});


myApp.controller('frontPageController', 
		['$scope', '$log', 'frontPageFactory', 'errorHandlerFactory', '$cookies', '$timeout',
        function($scope, $log, frontPageFactory, errorHandlerFactory, $cookies, $timeout) {

	$scope.frontPageFilter = $cookies.get('front-page-search');
	if(null != $scope.frontPageFilter) {
		$scope.showFilteringMsg = true;
		$timeout(function() {
			$scope.showFilteringMsg = false;	
		}, 3000);
	}
	
	frontPageFactory.getUpcomingFixtures().success(function(data) {
		$log.debug("Data received for upcoming fixtures", data);
		$scope.upcomingFixtures = data;
	}).error(errorHandlerFactory.getHandler());
	
	$scope.$watch('frontPageFilter', function() {
		$log.debug('frontPageFilter has just changed', $scope.frontPageFilter);
		$cookies.put('front-page-search', $scope.frontPageFilter);
	});
	
	$scope.showFixture = function(fixtureId) {
		$scope.showFixtureDetailsDialogue = true;
		frontPageFactory.getFixtureDetail(fixtureId).success(function(data) {
			$log.debug("Data received for fixture", data);
			$scope.fixtureDetails = data;
		});
	}
	
	$scope.hideFixtureDetails = function() {
		$scope.showFixtureDetailsDialogue = false;
	}
}]);

