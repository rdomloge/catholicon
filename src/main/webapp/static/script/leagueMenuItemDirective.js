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
		leagueFactory.getLeagues($scope.season).success(
			function(data) {
				$log.debug("Data received for leagues", data);
				$scope.leagues = data;
			}
		);		
	}
	
	$scope.$on('menu_item_selected', function() {
		$scope.show = false;
	});
}]);

