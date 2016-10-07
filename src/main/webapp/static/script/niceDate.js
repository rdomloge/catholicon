myApp.directive('niceDate', function($log) {
	return {
		restrict : 'AE',
		replace : true,
		scope: {
			date: '='
		},
		templateUrl : 'partials/niceDate.html',
		controller : 'niceDateController'
	}
});

myApp.controller('niceDateController', ['$scope', function($scope) {
	var date = new Date($scope.date);
	var d = new Date();
	var now = new Date(d.getFullYear()+"-"+(dd(d.getMonth()+1))+"-"+(dd(d.getDate()))); //2016-10-06
	
	$scope.daysDifference = Math.abs(dayOfYear(now) - dayOfYear(date)); // dates are 1 based, rather than zero-based
	$scope.future = date.getTime() > now.getTime();
	
	function dd(digit) {
		return ("0"+digit).slice(-2);
	}
	
	function dayOfYear(d) {
		var start = new Date(d.getFullYear(), 0, 0);
		var diff = d - start;
		var oneDay = 1000 * 60 * 60 * 24;
		var day = Math.ceil(diff / oneDay);
		return day;
	}
	
}]);