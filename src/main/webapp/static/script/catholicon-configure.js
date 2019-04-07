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
				//ga('send', 'pageview', config.url); // tell Google Analytics about what URL we are fetching
				var url = new URL(window.location.href);
				var test = url.searchParams.get("test");
				if(test) {
					if( ! config.params) {
						config.params = { };
					}
					config.params.test = "";
				}
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

myApp.config([ "$compileProvider", function($compileProvider) {   
	$compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|webcal):/);
	// Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
} ]);
