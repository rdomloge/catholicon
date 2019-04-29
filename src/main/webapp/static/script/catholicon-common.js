var myApp = angular.module('app', ['ngRoute', 'ngCookies', 'pascalprecht.translate']);

var Config = {
	BASE_URL: '',
	MS_SEASONS_BASE: window.location.protocol+'://'+window.location.hostname+':81/seasons',
	MS_LEAGUES_BASE: window.location.protocol+'://'+window.location.hostname+':82/leagues',
	MS_RESULTS_BASE: window.location.protocol+'://'+window.location.hostname+':83'
};