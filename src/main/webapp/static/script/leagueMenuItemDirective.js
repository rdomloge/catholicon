myApp.directive('leagueMenuItemDirective', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
			season: '='
		},
		templateUrl : 'leagueTemplate.html',
		controller : 'leagueMenuItemController'
	}
});

myApp.controller('leagueMenuItemController', function($scope, seasonFactory, $log, $q) {
	$scope.load = function() {
		$q.when(seasonFactory.getSeasonList()).then(function(page) {
			$scope.leagues = $scope.season.leagues;
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
});

