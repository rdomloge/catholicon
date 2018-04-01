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
			
	matchListFactory.getMatches($routeParams.teamId, $routeParams.season).then(function(page) {
		$log.debug("Data received for matches", page.data);
		
		decorateWinningTeam(page.data);
		decorateFirstUnplayed(page.data);
		
		$scope.matches = page.data;
		$scope.teamName = 
			$scope.matches[0].homeTeam.id == $routeParams.teamId 
			? $scope.matches[0].homeTeam.name 
			: $scope.matches[0].awayTeam.name;
	});
	
	$scope.selectUrlText = function() {
		var copyTextarea = document.querySelector('.js-copytextarea');
		copyTextarea.select();
		try {
		    var successful = document.execCommand('copy');
		    var msg = successful ? 'successful' : 'unsuccessful';
		    $log.debug('Copying text command was ' + msg);
		    $scope.urlCopySuccessMsg = successful ? 'Copied' : 'Copy failed';
		    $timeout(function() {
		    	$scope.urlCopySuccessMsg = '';
		    	$log.debug("Reset variable");
		    }, 2000);
		  } catch (err) {
		    console.log('Oops, unable to copy');
		  }
	}
	
	$scope.getTeamId = function() {
		return $routeParams.teamId;
	}
	
	$scope.getSeason = function() {
		return $routeParams.season;
	}
	
	$scope.getOpposition = function(match) {
		return match.homeTeam.id == $routeParams.teamId ? match.awayTeam : match.homeTeam;
	}
	
	$scope.isHome = function(match) {
		return match.homeTeam.id == $routeParams.teamId;
	}
	
	$scope.isWin = function(match) {
		var isHome = match.homeTeam.id == $routeParams.teamId;
		if(isHome)
			return match.homeTeam.score > match.awayTeam.score;
		else
			return match.awayTeam.score > match.homeTeam.score;
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

	$scope.openWebCal = function() {
		return "webcal://" + $location.host() + ":" + $location.port() + '/season/' + $scope.season
		 + "/matches/" + $scope.teamId + "/webcal";
	}

	$scope.scrollTo = function(element) {
		
	}
}]);
