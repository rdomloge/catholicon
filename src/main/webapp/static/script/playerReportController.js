myApp.factory('playerReportFactory', function($http, $log) {
	var factory = {};
	
	factory.getPlayerReport = function(season, league) {
		$log.info("Loading player report");
		return $http.get(Config.BASE_URL+'/season/'+season+'/league/'+league+'/report');
	};
	
	return factory;
});

myApp.controller('playerReportController', function($scope, $log, playerReportFactory, $routeParams) {
	playerReportFactory.getPlayerReport($routeParams.season, $routeParams.league).success(function(data) {
		$log.debug("Data received for playerReport", data);
		$scope.playerReport = data;
		addUniqueListOfDivisionsToScope(data);
	});
	
	$scope.setDivisionFilter = function(division) {
		$scope.divisionFilter = division;
		$scope.show = false;
	}
	
	function addUniqueListOfDivisionsToScope(playerReports) {
		var divisions = [];
		
		for(var i=0; i < playerReports.length; i++) {
			var division = playerReports[i].division;
			if( ! contains(division, divisions)) {
				divisions.push(division);
			}
		}
		
		divisions.sort();
		$scope.divisions = divisions;
	}
	
	function contains(item, array) {
		for(var i=0; i < array.length; i++) {
			if(array[i] == item) return true;
		}
		return false;
	}
});