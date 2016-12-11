myApp.controller('matchCardController', function ($scope, $log, $http, dataFactory, $routeParams, $rootScope) {
	$log.debug("Getting match card for "+$routeParams.fixtureId);
	dataFactory.getMatchCard($routeParams.fixtureId).success(function(data) {
		$log.debug("Data received for match card", data);
		$scope.matchCard = data;
	});

	$scope.hideNames = function() {
		$rootScope.name1 = undefined;
		$rootScope.name2 = undefined;
	};
});

myApp.directive('matchCard6Row', function($log, $rootScope) {
	return {
		restrict : 'AE',
		replace : true,
		templateUrl: 'partials/matchcard/matchCard6Row.html',
		scope: {
			matchCard: "=",
			rowNum: "=",
		},
		controller: function($scope) {
			$scope.rubber = function(colNum) {
				if($scope.matchCard) {
					return $scope.matchCard.rubbers[($scope.rowNum*3)+colNum];
				}
			}
			
			$scope.showNames = function(name1, name2) {
				$rootScope.name1 = name1;
				$rootScope.name2 = name2;
			};
		},
		link: function($scope) {
			$log.debug('Rubber', $scope.rubber());
		}
	}
});

myApp.directive('matchCard4Row', function($log, $rootScope) {
	return {
		restrict : 'AE',
		replace : true,
		templateUrl: 'partials/matchcard/matchCard4Row.html',
		scope: {
			matchCard: "=",
			rowNum: "=",
		},
		controller: function($scope) {
			$scope.rubber = function(colNum) {
				if($scope.matchCard) {
					return $scope.matchCard.rubbers[($scope.rowNum*3)+colNum];
				}
			}
			
			$scope.showNames = function(name1, name2) {
				$rootScope.name1 = name1;
				$rootScope.name2 = name2;
			};
		},
		link: function($scope) {
			$log.debug('Rubber', $scope.rubber());
		}
	}
});
