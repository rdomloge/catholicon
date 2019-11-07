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

myApp.controller('divisionMenuItemController', function($scope, seasonFactory, $log, $rootScope, $q) {
	$scope.load = function() {
		
		$q.when(seasonFactory.getSeasonList()).then(function(page) {
			$scope.divisions = $scope.league.divisions;
		});
	}
	
	if($scope.season.apiIdentifier == 0) {
		$scope.load();
	}
	
	$scope.$on('menu_item_selected', function() {
		if($scope.season.apiIdentifier != 0) {
			$scope.show = false;
		}
	});
	
	$scope.menuItemSelected = function() {
		
		$rootScope.$broadcast('menu_item_selected');
		w3_close();
	}
});

