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
//	$scope.toggleNews = function() {
//		if($scope.news) {
//			$scope.news = undefined;
//		}
//		else {
//			newsFactory.getNewsList().then(function(page) {
//				$log.debug("Data received for news", page.data);
//				$scope.news = page.data;
//			});			
//		}
//	}
	
	newsFactory.getNewsList().then(function(page) {
		$log.debug("Data received for news", page.data);
		$scope.news = page.data;
	});
	
}]);