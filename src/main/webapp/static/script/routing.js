myApp.config(function ($routeProvider) {
	$routeProvider
		.when('/season/:season', 
			{
				templateUrl: 'partials/leagueList.html',
				controller: 'leagueListController'
			})
		.when('/season/:season/matches/:teamId/list',
			{
				templateUrl: 'partials/matches.html'
			})
		.when('/season/:season/matches/:teamId/listcompact',
			{
				templateUrl: 'partials/matchesCompact.html'
			})
		.when('/season/:season/league/:leagueTypeId/divisions',
			{
				templateUrl: 'partials/divisionList.html',
				controller: 'leagueDivisionListController'
			})
		.when('/season/:season/league/:league/report',
			{
				templateUrl: 'partials/playerReport.html',
				controller: 'playerReportController'
			})
		.when('/season/:season/league/:leagueTypeId/division/:divisionId', 
			{
				templateUrl: 'partials/division.html',
				controller: 'divisionController'
			})
		.when('/matchcard/:fixtureId',
			{
				templateUrl: 'partials/matchcard/matchCard.html'
			})
		.when('/clubs',
			{
				templateUrl: 'partials/clubs.html',
				controller: 'clubsController'
			})
		.when('/clubs/:clubId',
			{
				templateUrl: 'partials/clubs.html',
				controller: 'clubsController'
			})
		.when('/committee',
			{
				templateUrl: 'partials/committee.html',
				controller: 'committeeController'
			})
		.when('/',
			{
				templateUrl: 'partials/frontPage.html'
			})
		.when('/unconfirmed',
			{
				templateUrl: 'partials/unconfirmed.html',
				controller: 'unconfirmedController'
			})
		.when('/secure/login', 
			{
				templateUrl: 'partials/secure/login.html'
			})
		.when('/secure/jwtlogin', 
			{
				templateUrl: 'partials/secure/jwtlogin.html'
			});
});