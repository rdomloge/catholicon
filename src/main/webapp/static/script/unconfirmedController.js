myApp.factory('unconfirmedFactory', function($http, $log) {
	
	var unconfirmedFactory = {};
	
	unconfirmedFactory.getUnconfirmed = function() {
		$log.info("Loading unconfirmed results");
		return $http.get(Config.MS_RESULTS_BASE+'/fixtures/search/findUnconfirmedMatchcardsBySeason?season=0');
	};
	
	return unconfirmedFactory;
});


myApp.controller('unconfirmedController', 
		['$scope', '$log', 'unconfirmedFactory', 'errorHandlerFactory', '$cookies', '$timeout', 'matchCardFactory',
        function($scope, $log, unconfirmedFactory, errorHandlerFactory, $cookies, $timeout, matchCardFactory) {
	
			$scope.details = {};
			
			unconfirmedFactory.getUnconfirmed().then(function(page) {
				var results = page.data._embedded.fixtures;
				$log.debug("Data received for unconfirmed results", results);
				$scope.unconfirmed = results;
			}, errorHandlerFactory.getHandler());
			
			$scope.mergeFixtureDetails = function(result) {
				matchCardFactory.getMatchResult(result.externalFixtureId).then(function(page) {
					angular.extend(result, page.data);
					result.merged = true;
				});
			}
			
			$scope.homeTeamToAction = function(result) {
				if(result.merged) {
					return result.homeTeamId == result.teamToActionId;
				}
			}
	
}]);
