myApp.controller('matchCardController', function ($scope, $log, $http, dataFactory, $routeParams) {
	$log.debug("Getting match card for "+$routeParams.fixtureId);
	dataFactory.getMatchCard($routeParams.fixtureId).success(function(data) {
		$log.debug("Data received for match card", data);
		$scope.matchCard = data;
	});

	$scope.showNames = function(name1, name2) {
		$scope.name1 = name1;
		$scope.name2 = name2;
	};
	
	$scope.hideNames = function() {
		$scope.name1 = undefined;
		$scope.name2 = undefined;
	};
});

myApp.directive('matchCardRow', function($log, $rootScope) {
	return {
		restrict : 'AE',
		replace : true,
		templateUrl: 'partials/matchCardRow.html',
		scope: {
			matchCard: "=",
			rowNum: "=",
			showNames: "&"
		},
		controller: function($scope) {
			$scope.rubber = function(colNum) {
				if($scope.matchCard) {
					return $scope.matchCard.rubbers[($scope.rowNum*3)+colNum];
				}
			}
			
			$scope.test = function() {
				$log.debug($scope.name1);
				$scope.showNames('this', 'that');
			}
		},
		link: function($scope) {
			$log.debug('Rubber', $scope.rubber());
		}
	}
});
