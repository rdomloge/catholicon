myApp.factory('committeeFactory', function($http, $log) {
	
	var committeeFactory = {};
	
	committeeFactory.getCommittee = function() {
		$log.info("Loading committee contacts");
		return $http.get(Config.BASE_URL+'/committee');
	};
	
	return committeeFactory;
});


myApp.controller('committeeController', 
		['$scope', '$log', 'committeeFactory', 'errorHandlerFactory', '$cookies', '$timeout',
        function($scope, $log, committeeFactory, errorHandlerFactory, $cookies, $timeout) {
	
			committeeFactory.getCommittee().then(function(page) {
				$log.debug("Data received for committee contacts", page.data);
				$scope.committee = page.data;
			}, errorHandlerFactory.getHandler());
	
}]);

