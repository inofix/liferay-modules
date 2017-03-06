AUI.add(
	'liferay-so-scroll',
	function(A) {
		var Lang = A.Lang;

		var	isNumber = Lang.isNumber;
		var	isString = Lang.isString;

		var AVAILABLE = '-available';

		var BOTTOM = 'bottom';

		var DELAY = 'delay';

		var DOC_EL = A.config.doc.documentElement;

		var DOWN = 'down';

		var EDGE = '-edge';

		var EDGE_PROXIMITY = 'edgeProximity';

		var LAST_STATE = 'lastState';

		var MAX_COORDINATE = 'maxCoordinate';

		var SCROLL = 'scroll';

		var START = '-start';

		var Scroll = A.Component.create(
			{
				ATTRS: {
					delay: {
						validator: isNumber,
						value: null
					},

					edgeProximity: {
						setter: function(val) {
							var value = 0;

							if (isNumber(val)) {
								value = val;
							}
							else if (isString(val)) {
								value = (Lang.toInt(val) / 100);
							}

							return value;
						},
						value: null
					},

					lastState: {
						value: {
							scrollTop: 0
						}
					},

					maxCoordinate: {
						value: null
					}
				},

				EXTENDS: A.Plugin.Base,

				NAME: SCROLL,

				NS: SCROLL,

				prototype: {
					initializer: function(config) {
						var instance = this;

						var host = A.one(config.host);

						instance._host = host;

						instance._resetFn();

						host.on(SCROLL, A.bind('_onScroll', instance));

						instance._createResetTask();

						instance.after('delayChange', instance._createResetTask);
					},

					_createResetTask: function() {
						var instance = this;

						instance._resetTask = A.debounce('_resetFn', instance.get(DELAY), instance);
					},

					_onScroll: function(event) {
						var instance = this;
						
						var edgeProximity = instance.get(EDGE_PROXIMITY);
						var lastState = instance.get(LAST_STATE);
						var maxCoordinate = instance.get(MAX_COORDINATE);
						
						var edgeProximityY = edgeProximity;

						var maxCoordinateY = maxCoordinate.y;

						var host = instance._host;

						var scrollTop = host.get('scrollTop') || host.get('scrollY') || 0;
						
						// console.log(scrollTop);

						if (edgeProximity % 1) {
							edgeProximityY *= maxCoordinateY;
						}
						

						var scrolledDown = (scrollTop > lastState.scrollTop);

						var availableScrollY = (scrollTop - maxCoordinateY);

						var state = {
							availableScrollY: availableScrollY,
							scrolledDown: scrolledDown,
							scrollTop: scrollTop
						};

						if (scrolledDown) {
							instance.fire(DOWN, state);
							
//							console.log('availableScrollY = ' + availableScrollY);
//							console.log('edgeProximityY = ' + edgeProximityY);
//							console.log(availableScrollY + edgeProximityY);

							if ((availableScrollY + edgeProximityY) >= 0) {
								instance.fire(BOTTOM + EDGE, state);
							}

							if (availableScrollY > 0) {
								instance.fire(DOWN + AVAILABLE, state);

								if (lastState.availableScrollY < 1) {
									instance.fire(DOWN + AVAILABLE + START, state);
								}
							}

							if (!lastState.scrolledDown) {
								instance.fire(DOWN + START, state);
							}
						}

						if ((availableScrollY > 0) || (scrollTop < 0)) {
							instance._resetFn();
						}

						instance.set(LAST_STATE, state);

						instance._resetTask();
					},

					_resetFn: function() {
						var instance = this;

						var lastState = instance.get(LAST_STATE);

						lastState.availableScrollY = 0;

						instance.set(LAST_STATE, lastState);

						var scrollY = instance._host._node.scrollHeight || DOC_EL.scrollHeight - DOC_EL.clientHeight;

						instance.set(
							MAX_COORDINATE,
							{
								y: scrollY
							}
						);
					}
				}
			}
		);

		Liferay.namespace('SO').Scroll = Scroll;
	},
	'',
	{
		requires: ['aui-base']
	}
);