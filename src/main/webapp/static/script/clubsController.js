myApp.factory('clubsFactory', function($http, $log) {
	
	var clubsFactory = {};
	
	clubsFactory.getClubs = function() {
		$log.info("Loading clubs");
		return $http.get(Config.BASE_URL+'/clubs');
	};
	
	return clubsFactory;
});


myApp.controller('clubsController', 
		['$scope', '$log', 'clubsFactory', 'errorHandlerFactory', '$cookies', '$timeout',
        function($scope, $log, clubsFactory, errorHandlerFactory, $cookies, $timeout) {
	
			clubsFactory.getClubs().success(function(data) {
				$log.debug("Data received for clubs", data);
				$scope.clubs = data;
				if(data.length) {
					$scope.selectedClub = data[0];
				}
			}).error(errorHandlerFactory.getHandler());
			
			$scope.hideDropDownAndShow = function(club) {
				$scope.showClubs = false;
				$scope.selectedClub= club;
			};
	
}]);

