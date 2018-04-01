myApp.factory('leagueFactory', function($http, $log) {
	var factory = {};
	
	factory.getLeagues = function(season) {
		$log.info("Fetching list of leagues");
		return $http.get(Config.BASE_URL+'/season/'+season.apiIdentifier+'/league/list');	
	};
	
	return factory;
});

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

myApp.controller('leagueMenuItemController', ['$scope', 'leagueFactory', '$log', function($scope, leagueFactory, $log) {
	$scope.load = function() {
		leagueFactory.getLeagues($scope.season).then(
			function(page) {
				$log.debug("Data received for leagues", page.data);
				$scope.leagues = page.data;
			}
		);		
	}
	
	if($scope.season.apiIdentifier == 0) {
		$scope.load();
	}
	
	$scope.$on('menu_item_selected', function() {
		if($scope.season.apiIdentifier != 0) {
			$scope.show = false;
		}
	});
}]);

