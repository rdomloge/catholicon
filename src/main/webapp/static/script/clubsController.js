myApp.factory('clubsFactory', function($http, $log) {
	
	var clubsFactory = {};
	
	clubsFactory.getClubDescriptors = function() {
		$log.info("Loading club descriptors");
		return $http.get(Config.BASE_URL+'/clubs?fetch=clubId,clubName');
	};
	
	clubsFactory.getClub = function(clubId) {
		$log.info("Loading club "+clubId);
		return $http.get(Config.BASE_URL+'/clubs/'+clubId);
	};
	
	return clubsFactory;
});


myApp.controller('clubsController', 
		['$scope', '$log', 'clubsFactory', 'errorHandlerFactory', '$cookies', '$timeout',
        function($scope, $log, clubsFactory, errorHandlerFactory, $cookies, $timeout) {
	
			clubsFactory.getClubDescriptors().success(function(data) {
				$log.debug("Data received for club descriptors", data);
				$scope.clubDescriptors = data;
				if(data.length) {
					var clubId = data[0].clubId;
					clubsFactory.getClub(clubId).success(function(data) {
						$log.debug("Data received for club "+clubId, data);
						$scope.selectedClub = data;
					}).error(errorHandlerFactory.getHandler());
				}
			}).error(errorHandlerFactory.getHandler());
			
			$scope.hideDropDownAndShow = function(clubDescr) {
				$scope.showClubs = false;
				$scope.selectedClub = undefined;
				clubsFactory.getClub(clubDescr.clubId).success(function(data) {
					$log.debug("Data received for club "+clubDescr.clubId, data);
					$scope.selectedClub = data;
				}).error(errorHandlerFactory.getHandler());
			};
	
}]);

