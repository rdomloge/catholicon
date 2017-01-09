myApp.config(function($translateProvider) {
	$translateProvider.useSanitizeValueStrategy('escape');
	$translateProvider.useUrlLoader('../../i18n');
	
	//$translateProvider.preferredLanguage('en');
	$translateProvider.determinePreferredLanguage();
});

myApp.config([ "$httpProvider", function($httpProvider) {
	$httpProvider.interceptors.push(function($q, $log, $rootScope) {
		return {
			'request' : function(config) {
				$rootScope.$broadcast('started-thinking');
				return config;
			},

			'response' : function(response) {
				$rootScope.$broadcast('finished-thinking');
				return response;
			},

			// optional method
			'responseError' : function(rejection) {
				$rootScope.$broadcast('finished-thinking');
				$rootScope.$broadcast('error-occurred');
				return $q.reject(rejection);
			}
		};
	});
} ]);