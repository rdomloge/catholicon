myApp.directive('recentMatch', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
			match: '='
		},
		templateUrl : 'partials/recentMatch.html',
	}
});