myApp.factory('divisionReportFactory', function($http, $log) {
	
	var divisionReportFactory = {};
	
	divisionReportFactory.getDivisionReport = function(leagueTypeId, divisionId, season) {
		$log.info("Loading division "+divisionId+" for "+leagueTypeId);
		return $http.get(Config.BASE_URL+'/divisions/'+divisionId+'/report');
	};
	
	return divisionReportFactory;
});


myApp.controller('divisionReportController', ['$routeParams', 'divisionReportFactory', '$log', '$scope', '$timeout', '$rootScope', function($routeParams, divisionReportFactory, $log, $scope, $timeout, $rootScope) {
	divisionReportFactory.getDivisionReport($routeParams.leagueTypeId, $routeParams.divisionId, $routeParams.season)
			.then(function(page) {
		$log.debug("Data received for division report "+$routeParams.divisionId, page.data);
		$scope.division = page.data;
	});
}]);
