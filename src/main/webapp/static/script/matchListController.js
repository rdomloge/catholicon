myApp.factory('matchListFactory', function($http, $log) {
	
	var factory = {};
	
	factory.getMatches = function(team, season) {
		$log.info("Loading matches for "+team);
		return $http.get(Config.BASE_URL+'/season/'+season+'/matches/'+team+'/list');
	}
	
	factory.getFixtureDetail = function(fixtureId) {
		$log.info("Loading fixture detail for "+fixtureId);
		return $http.get(Config.BASE_URL+'/fixture/'+fixtureId);
	};
	
	return factory;
});

myApp.controller('matchListController', 
		['$routeParams', 'matchListFactory', '$log', '$scope', '$timeout', '$rootScope', '$location', '$anchorScroll', 
			function($routeParams, matchListFactory, $log, $scope, $timeout, $rootScope, $location, $anchorScroll) {
			
	var firstUnplayed;
			
	matchListFactory.getMatches($routeParams.teamId, $routeParams.season).success(function(data) {
		$log.debug("Data received for matches", data);
		
		decorateWinningTeam(data);
		decorateFirstUnplayed(data);
		
		$scope.matches = data;
		$scope.teamName = 
			$scope.matches[0].homeTeamId == $routeParams.teamId 
			? $scope.matches[0].homeTeamName 
			: $scope.matches[0].awayTeamName;
	    
	    $timeout(function(){
	    	$anchorScroll();
	    }, 100);
	});
	
	$scope.getTeamId = function() {
		return $routeParams.teamId;
	}
	
	$scope.getSeason = function() {
		return $routeParams.season;
	}
	
	function decorateWinningTeam(data) {
		for(key in data) {
			var match = data[key];
			if(match.fixtureStatus == 5) {
				match.homeWin = match.homeTeamScore > match.awayTeamScore;
			}
		}
	}
	
	function decorateFirstUnplayed(data) {
		for(key in data) {
			var match = data[key];
			if(match.fixtureStatus == 5 || Date.parse(match.date) - new Date() < 0)
				continue;
			if(firstUnplayed) {
				if(match.date < firstUnplayed.date) 
					firstUnplayed = match;
			}
			else {
				firstUnplayed = match;
			}
		}
		
		if(firstUnplayed) {
			firstUnplayed.firstUnplayed = true;
		}
	}
	
	$scope.showFixture = function(fixtureId) {
		$scope.$broadcast('fixture-details', {id: fixtureId});
	}
	
	$scope.teamId = $routeParams.teamId;
	$scope.season = $routeParams.season;
	
	$scope.scrollTo = function(element) {
		
	}
}]);