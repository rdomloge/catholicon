myApp.factory('fixtureDetailFactory', function($http, $log) {
	
	var fixtureDetailFactory = {};
	
	fixtureDetailFactory.getFixtureDetail = function(fixtureId) {
		$log.info("Loading fixture detail for "+fixtureId);
		return $http.get(Config.BASE_URL+'/fixture/'+fixtureId);
	};
	
	return fixtureDetailFactory;
});

myApp.directive('fixtureDetailsDirective', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
		},
		templateUrl : 'partials/fixtureDetails.html',
		controller : 'fixtureDetailsController',
		link: function() {
			$log.debug('Fixture details directive link');
		}
	}
});

myApp.controller('fixtureDetailsController', ['$scope', '$log', 'fixtureDetailFactory', function($scope, $log, fixtureDetailFactory) {
	
	$scope.$on('fixture-details', function(event, args) {
		$log.debug('Request to view fixture details', args);
		$scope.showFixtureDetailsDialogue = true;
		fixtureDetailFactory.getFixtureDetail(args.id).then(function(page) {
			$log.debug("Data received for fixture", page.data);
			$scope.fixtureDetails = page.data;
		});
	});
	
	$scope.hideFixtureDetails = function() {
		$scope.showFixtureDetailsDialogue = false;
		$scope.fixtureDetails = null;
	}
}]);