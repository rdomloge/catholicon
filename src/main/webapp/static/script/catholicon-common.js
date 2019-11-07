var myApp = angular.module('app', ['ngRoute', 'ngCookies', 'pascalprecht.translate']);

var Config = {
	BASE_URL: '',
	
	MS_SEASONS_BASE: '//' + window.location.hostname + (location.port ? ':'+location.port: '') + '/seasons',
	MS_RESULTS_BASE: '//' + window.location.hostname + (location.port ? ':'+location.port: '')
};