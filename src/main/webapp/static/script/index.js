
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
	
var phaseNumber = 0;;
var nextPhaseCallBack = function() { 
	phaseNumber++;
	if(phaseNumber < scriptPhases.length) {
		phase = scriptPhases[phaseNumber];
		loadPhase(phase, nextPhaseCallBack);
	}
};
loadPhase(scriptPhases[phaseNumber], nextPhaseCallBack);


function loadPhase(phase, nextPhaseCallBack) {
	var remaining = phase.length;
	var phaseCountDownCallBack = function() {
		remaining--;
		if(remaining <= 0) nextPhaseCallBack();
	}
	for(var i=0; i < phase.length; i++) {
		loadScript(phase[i], phaseCountDownCallBack);
	}
}

function loadScript(scriptUrl, callBack) {
	var script = document.createElement('script');
	script.onload = callBack;
	script.src = scriptUrl;
	document.body.appendChild(script);
}