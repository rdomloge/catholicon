myApp.controller('frontPageController', 
		['$scope', '$log', 'dataFactory', 'errorHandlerFactory', '$cookies', '$rootScope', '$timeout',
        function($scope, $log, dataFactory, errorHandlerFactory, $cookies, $rootScope, $timeout) {

	$rootScope.search = $cookies.get('front-page-search');
	if(null != $rootScope.search != null) {
		$rootScope.showFilteringMsg = true;
		$timeout(function() {
			$rootScope.showFilteringMsg = false;	
		}, 3000);
	}
	
	dataFactory.getUpcomingFixtures().success(function(data) {
		$log.debug("Data received for upcoming fixtures", data);
		$scope.upcomingFixtures = data;
	}).error(errorHandlerFactory.getHandler());
	
	$scope.$watch('search', function() {
		$log.debug('Search has just changed', $scope.search);
		$cookies.put('front-page-search', $scope.search);
	});
}]);

