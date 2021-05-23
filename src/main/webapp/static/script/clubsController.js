myApp.factory('clubsFactory', function($http, $log) {
	
	var clubsFactory = {};
	
	clubsFactory.getClubDescriptors = function() {
		$log.info("Loading club descriptors");
		return $http.get(Config.BASE_URL+'/clubs/search/findClubBySeason?season=0');
	};
	
	clubsFactory.getClub = function(clubId, season) {
		$log.info("Loading club "+clubId+" for season "+season);
		return $http.get(Config.BASE_URL+'/clubs/search/findClubByClubIdAndSeasonId?clubId='+clubId+"&season="+season);
	};
	
	return clubsFactory;
});


myApp.controller('clubsController', 
		['$scope', '$log', 'clubsFactory', 'errorHandlerFactory', '$cookies', '$timeout', '$routeParams',
        function($scope, $log, clubsFactory, errorHandlerFactory, $cookies, $timeout, $routeParams) {
	
			clubsFactory.getClubDescriptors().then(function(page) {
				$log.debug("Data received for club descriptors", page.data);
				$scope.clubDescriptors = page.data;
				if(page.data.length) {
					var clubId;
					if($routeParams.clubId) {
						clubId = $routeParams.clubId;
					}
					else {
						clubId = page.data[0].clubId;						
					}
					
					clubsFactory.getClub(clubId, 0).then(function(page) {
						$log.debug("Data received for club "+clubId, page.data);
						$scope.selectedClub = page.data;
					}, errorHandlerFactory.getHandler());
				}
			}, errorHandlerFactory.getHandler());
			
}]);

