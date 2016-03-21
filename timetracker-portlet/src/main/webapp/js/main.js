/**
 * main.js: Functions used by the timetracker portlet.
 * 
 * Created: 	2016-03-21 15:50 by Christian Berndt
 * Modified: 	2016-03-21 15:50 by Christian Berndt
 * Version: 	1.0.0
 */


/** 
 * timetracker-navigation - based on the model of 
 * .../portlet/journal/js/navigation.js. 
 * @since 1.0.1
 */
AUI.add(
	'timetracker-navigation',
	function(A) {
		var AObject = A.Object;
		var Lang = A.Lang;
		var History = Liferay.HistoryManager;
		
		var DISPLAY_STYLE_TOOLBAR = 'displayStyleToolbar';
		
		var STR_CLICK = 'click';
		
		var WIN = A.config.win;
		
		var TimetrackerNavigation = A.Component.create(
				{
					AUGMENTS: [Liferay.PortletBase],

					EXTENDS: A.Base,

					NAME: 'timetrackernavigation',
					
					
					prototype: {
						initializer: function(config) {
							var instance = this;

							var timetrackerContainer = instance.byId('timetrackerContainer');
							
							instance._timetrackerContainer = timetrackerContainer;
							
							instance._entriesContainer = instance.byId('entriesContainer');
							
							instance._eventPageLoaded = instance.ns('pageLoaded');
							
							var checkBoxesId = [instance.ns('rowIds')];
							
							var displayStyle = config.displayStyle;
							
							var displayStyleCSSClass = 'display-style';
							
							var displayStyleToolbar = instance.byId(DISPLAY_STYLE_TOOLBAR);

							var namespace = instance.NS;

							var portletContainerId = instance.ns('timetrackerContainer');
							
							var selectConfig = config.select;

							selectConfig.checkBoxesId = checkBoxesId;
							selectConfig.displayStyle = displayStyle;
							selectConfig.displayStyleCSSClass = displayStyleCSSClass;
							selectConfig.displayStyleToolbar = displayStyleToolbar;
							selectConfig.namespace = namespace;
							selectConfig.portletContainerId = portletContainerId;
							selectConfig.selector = 'entry-selector';

							instance._appViewSelect = new Liferay.AppViewSelect(selectConfig);
							
							var eventHandles = [
								Liferay.on(instance._eventPageLoaded, instance._onPageLoaded, instance),
								History.after('stateChange', instance._afterStateChange, instance),
							];

							instance._config = config;

							instance._eventHandles = eventHandles;
							
							eventHandles.push(Liferay.on(config.portletId + ':portletRefreshed', A.bind('destructor', instance)));
						},
						
						destructor: function() {
							var instance = this;

							A.Array.invoke(instance._eventHandles, 'detach');

							instance._appViewSelect.destroy();

							instance._timetrackerContainer.purge(true);
						},
						
						_afterStateChange: function(event) {
							var instance = this;

							var namespace = instance.NS;

							var requestParams = {};

							var state = History.get();

							AObject.each(
								state,
								function(item, index, collection) {
									if (index.indexOf(namespace) === 0) {
										requestParams[index] = item;
									}
								}
							);

							if (AObject.isEmpty(requestParams)) {
								requestParams = instance._getDefaultHistoryState();
							}

							Liferay.fire(
								instance._eventDataRequest,
								{
									requestParams: requestParams,
									src: SRC_HISTORY
								}
							);
						},
						
						_getDefaultHistoryState: function() {
							var instance = this;

							var initialState = History.get();

							return initialState;
						},
						
						_onPageLoaded: function(event) {
							
							var instance = this;

							var paginationData = event.pagination;

						}
					}

				});
			

		Liferay.Portlet.TimetrackerNavigation = TimetrackerNavigation;		
		
	},
	'',
	{
		requires: ['aui-loading-mask-deprecated', 'aui-parse-content', 'document-library-upload', 'event-simulate', 'liferay-app-view-folders', 'liferay-app-view-move', 'liferay-app-view-paginator', 'liferay-app-view-select', 'liferay-history-manager', 'liferay-message', 'liferay-portlet-base', 'querystring-parse-simple']
	}
);