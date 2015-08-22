/**
 * js for the main navigation, from 
 * https://gist.github.com/randombrad/b5ffb95519eee2c56d87
 * 
 * Created: 	2015-08-22 19:42 by Christian Berndt
 * Modified:	2015-08-22 19:42 by Christian Berndt
 * Version: 	1.0.0
 */

AUI().ready(
		'liferay-hudcrumbs', 'liferay-navigation-interaction', 'liferay-sign-in-modal',
	function(A) {
		var navigation = A.one('#navigation');

		var menu_toggle = navigation.one('#nav_toggle');


		if (navigation) {
			navigation.plug(Liferay.NavigationInteraction);
		}

		menu_toggle.on('click', function(event){
			navigation.one('.collapse.nav-collapse').toggleClass('open');
		});

		var siteBreadcrumbs = A.one('#breadcrumbs');

		if (siteBreadcrumbs) {
			siteBreadcrumbs.plug(A.Hudcrumbs);
		}

		var signIn = A.one('li.sign-in a');

		if (signIn && signIn.getData('redirect') !== 'true') {
			signIn.plug(Liferay.SignInModal);
		}
	}
);