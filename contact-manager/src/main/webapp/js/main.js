/**
 * main.js: Functions used by the contact-manager portlet.
 * 
 * Created: 	2015-06-25 14:45 by Christian Berndt
 * Modified: 	2015-07-03 16:58 by Christian Berndt
 * Version: 	1.0.1
 */

/**
 * 
 * @param event
 * @since 1.0.0
 */
function restoreOriginalNames(event) {
	
    // liferay-auto-fields by default adds index numbers
    // to the cloned row's inputs which is here undone.
    var row = event.row;
    var guid = event.guid;

    var inputs = row.all('input, select, textarea');

    inputs.each(function(item) {
        var name = item.attr('name') || item.attr('id');
        var original = name.replace(guid, '');
        item.set('name', original);
        item.set('id', original);
    });

};

/** 
 * contact-manager-navigation - based on the model of 
 * .../portlet/journal/js/navigation.js. 
 * @since 1.0.1
 */
AUI.add(
	'contact-manager-navigation',
	function(A) {
		var AObject = A.Object;
		var Lang = A.Lang;
		var History = Liferay.HistoryManager;
		
		var DISPLAY_STYLE_TOOLBAR = 'displayStyleToolbar';
		
		var STR_CLICK = 'click';
		
		var WIN = A.config.win;
		
		var ContactManagerNavigation = A.Component.create(
				{
					AUGMENTS: [Liferay.PortletBase],

					EXTENDS: A.Base,

					NAME: 'contactmanagernavigation',
					
					
					prototype: {
						initializer: function(config) {
							var instance = this;

							var contactManagerContainer = instance.byId('contactManagerContainer');
							
							instance._contactManagerContainer = contactManagerContainer;
							
							instance._entriesContainer = instance.byId('entriesContainer');
							
							instance._eventPageLoaded = instance.ns('pageLoaded');
							
							var checkBoxesId = [instance.ns('rowIds')];
							
							var displayStyle = config.displayStyle;
							
							var displayStyleCSSClass = 'display-style';
							
							var displayStyleToolbar = instance.byId(DISPLAY_STYLE_TOOLBAR);

							var namespace = instance.NS;

							var portletContainerId = instance.ns('contactManagerContainer');
							
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

							instance._contactManagerContainer.purge(true);
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
			

		Liferay.Portlet.ContactManagerNavigation = ContactManagerNavigation;		
		
	},
	'',
	{
		requires: ['aui-loading-mask-deprecated', 'aui-parse-content', 'document-library-upload', 'event-simulate', 'liferay-app-view-folders', 'liferay-app-view-move', 'liferay-app-view-paginator', 'liferay-app-view-select', 'liferay-history-manager', 'liferay-message', 'liferay-portlet-base', 'querystring-parse-simple']
	}
);