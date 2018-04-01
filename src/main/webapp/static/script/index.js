
var scriptPhases = [
	["../script/angular/1.5.8/angular.min.js"], 
	["../script/angular/1.5.8/angular-route.min.js",
	"../script/angular/1.5.8/angular-resource.js",
	"../script/angular/1.5.8/angular-cookies.js",
	"../libs/angular-translate.min.js"],
	["../libs/angular-translate-loader-url.min.js"],
	["../script/catholicon-common.js"],
	["../script/routing.js"],
	["../script/catholicon-configure.js",
	"../script/dataController.js",
	"../script/frontPageController.js"],
	["../script/leagueMenuItemDirective.js",
	"../script/divisionMenuItemDirective.js",
	"../script/upcomingFixtureDirective.js"],
	["../script/fixtureDetailsDirective.js",
	"../script/playerReportController.js",
	"../script/matchCard.js",
	"../script/matchListController.js",
	"../script/clubsController.js",
	"../script/committeeController.js",
	"../script/niceDate.js"],
	["../script/secure.js",
	"../script/jwtLogin.js"]];

var stylePhases = [
	["../style/w3.css",
		"../style/common.css"],
	["../style/loadingAnimation.css",
		"../style/frontPage.css",],
	["../style/match.css", 
		"../style/league.css", 
		"../style/matchCard.css", 
		"../style/niceDate.css",
		"../font/titillium.css"]
];
	

function loadPhasedResources(resources, type, onCompleteCallBack) {
	console.log('Started loading '+type);
	var phaseNumber = 0;
	var nextPhaseCallBack = function() { 
		phaseNumber++;
		if(phaseNumber < resources.length) {
			var phase = resources[phaseNumber];
			loadPhase(phase, nextPhaseCallBack, type);
		}
		else {
			console.log('Finished loading '+type);
			if(onCompleteCallBack) onCompleteCallBack();
		}
	};
	loadPhase(resources[phaseNumber], nextPhaseCallBack, type);
}

loadPhasedResources(scriptPhases, 'script');
loadPhasedResources(stylePhases, 'link');

function loadPhase(phase, nextPhaseCallBack, type) {
	var remaining = phase.length;
	var phaseCountDownCallBack = function() {
		remaining--;
		if(remaining <= 0) nextPhaseCallBack();
	}
	for(var i=0; i < phase.length; i++) {
		loadResource(phase[i], phaseCountDownCallBack, type);
	}
}

function loadResource(scriptUrl, callBack, type) {
	var element = document.createElement(type);
	element.onload = callBack;
	
	if('link' == type) {
		element.rel="stylesheet";
		element.type="text/css"
		element.href=scriptUrl;
	}
	else {
		element.src = scriptUrl;		
	}
	
	document.head.appendChild(element);
}