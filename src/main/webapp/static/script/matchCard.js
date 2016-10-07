myApp.controller('matchCardController', function ($scope, $log, $http, dataFactory, $routeParams) {
	$log.debug("Getting match card for "+$routeParams.fixtureId);
	dataFactory.getMatchCard($routeParams.fixtureId).success(function(data) {
		$log.debug("Data received for match card", data);
		$scope.matchCard = data;
	});
});

myApp.directive('matchCardRow', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		templateUrl: 'partials/matchCardRow.html',
		scope: {
			matchCard: "=",
			rowNum: "="
		},
		controller: function($scope) {
			$scope.rubber = function(colNum) {
				if($scope.matchCard) {
					return $scope.matchCard.rubbers[($scope.rowNum*3)+colNum];
				}
			}
			$scope.parp = function() {
				return 'blam';
			};
		},
		link: function($scope) {
			$log.debug('Rubber', $scope.rubber());
		}
	}
});
