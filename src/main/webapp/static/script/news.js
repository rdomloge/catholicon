myApp.factory('newsFactory', function($http, $log) {
	var factory = {};
	
	factory.getNewsList = function() {
		$log.info("Loading news");
		return $http.get(Config.BASE_URL+'/newsitems');
	}
	
	return factory;
});

myApp.controller('newsController', ['$routeParams', 'newsFactory', '$log', '$scope', function($routeParams, newsFactory, $log, $scope) {
	$log.debug("Fetching news");
	
	newsFactory.getNewsList().then(function(page) {
		$log.debug("Data received for news", page.data);
		$scope.news = page.data;
	});
	
}]);

myApp.directive('newsItem', function($log) {
	return {
		restrict : 'E',
		replace : true,
		scope: {
			data: '='
		},
		templateUrl : 'partials/newsItem.html'
	}
});