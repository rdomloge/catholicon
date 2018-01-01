myApp.factory('webCalFactory', function($http, $log) {
	
	var webCalFactory = {};
	
	webCalFactory.openWebCal = function(season, teamId) {
		$log.info("Fetching webCal for season: " + season + ", teamId: " + teamId);
		return $http.get(Config.BASE_URL+'/season/'+season+"/matches/" + teamId + "/webcal");
	};
	
	return webCalFactory;
});

myApp.directive('webCalDirective', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
		},
		// templateUrl : 'partials/fixtureDetails.html',
		controller : 'webCalController',
		link: function() {
			$log.debug('WebCal directive link');
		}
	}
});

myApp.controller('webCalController', ['$scope', '$log', 'webCalFactory', 
    function($scope, $log, webCalFactory) {
	
	$scope.$on('web-cal', function(event, args) {
		$log.debug('Request to open webcal', args);
		webCalFactory.openWebCal(args.season, args.teamId).success(function(data) {
			$log.debug("Data received for webcal", data);
			// $scope.fixtureDetails = data;
		});
	});
	
}]);