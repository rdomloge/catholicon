myApp.factory('divisionFactory', function($http, $log) {
	var factory = {};
	
	factory.getDivisions = function(season, leagueTypeId) {
		$log.info("Loading divisions for league " + leagueTypeId);
		return $http.get(Config.BASE_URL+'/season/'+season.apiIdentifier+'/league/'+leagueTypeId+'/divisions');		
	};
	
	return factory;
});

myApp.directive('divisionMenuItemDirective', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
			season: '=',
			league: '='
		},
		templateUrl : 'divisionTemplate.html',
		controller : 'divisionMenuItemController'
	}
});

myApp.controller('divisionMenuItemController', ['$scope', 'divisionFactory', '$log', '$rootScope', function($scope, divisionFactory, $log, $rootScope) {
	$scope.load = function() {
		divisionFactory.getDivisions($scope.season, $scope.league.leagueTypeId).success(function(data) {
			$log.debug("Data received for divisions", data);
			$scope.divisions = data;
		});
	}
	
	if($scope.season.apiIdentifier == 0) {
		$scope.load();
	}
	
	$scope.$on('menu_item_selected', function() {
		$scope.show = false;
	});
	
	$scope.menuItemSelected = function() {
		
		$rootScope.$broadcast('menu_item_selected');
		w3_close();
	}
}]);

