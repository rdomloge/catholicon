

myApp.factory('matchCardFactory', function($http, $log) {
	var factory = {};
	
	factory.getMatchCard = function(fixtureId) {
		$log.info("Loading match card for "+fixtureId);
		return $http.get(Config.BASE_URL+'/fixtures/search/findByExternalFixtureId?externalFixtureId='+fixtureId);
	}
	
	factory.getMatchResult = function(fixtureId) {
		$log.info("Loading result for "+fixtureId);
		return $http.get(Config.BASE_URL+'/fixtures/search/findResultByExternalFixtureId?externalFixtureId='+fixtureId);
	}
	
	return factory;
});

myApp.controller('matchCardController', function ($scope, $log, $http, matchCardFactory, $routeParams, $rootScope) {
	$log.debug("Getting match card for "+$routeParams.fixtureId);
	matchCardFactory.getMatchCard($routeParams.fixtureId).then(function(page) {
		$log.debug("Data received for match card", page.data);
		$scope.matchCard = page.data.matchCard;
	});

	$scope.hideNames = function() {
		$rootScope.name1 = undefined;
		$rootScope.name2 = undefined;
	};
});

var team4PlayerMap = {
		0: {
			home: [0,1],
			away: [0,1]
		},
		1: {
			home: [2,3],
			away: [0,1]
		},
		2: {
			home: [2,3],
			away: [2,3]
		},
		3: {
			home: [0,3],
			away: [0,3]
		},
		4: {
			home: [0,1],
			away: [2,3]
		},
		5: {
			home: [1,2],
			away: [1,2]
		}
}

myApp.directive('matchCardSix', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		templateUrl: 'partials/matchcard/matchCard6.html',
		scope: {
			matchCard: "="
		}
	}
});

myApp.directive('matchCardFour', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		templateUrl: 'partials/matchcard/matchCard4.html',
		scope: {
			matchCard: "="
		}
	}
});

myApp.directive('matchCard4Cell', function() {
	return {
		restrict : 'AE',
		replace : true,
		templateUrl: 'partials/matchcard/matchCard4Cell.html',
		scope: {
			matchCard: "=",
			rubber: "@"
		},
		controller: function($scope) {
			$scope.playerName = function(rubber, home, number) {
				if(home) {
					return $scope.matchCard.homePlayers[team4PlayerMap[rubber].home[number]];	
				}
				else {
					return $scope.matchCard.awayPlayers[team4PlayerMap[rubber].home[number]];
				}
			}
		}
	}
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
	}
});
