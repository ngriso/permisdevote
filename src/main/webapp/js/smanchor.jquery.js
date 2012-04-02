/**
 * jQuery plugin
 * @author Stanislav Komarovsky aka Faceless
 * @version 0.1b
 * 
 * @param elem Element to apply function to. 
 * 				Can be any jQuery-like expression (selector, DOM element, jQuery object).
 * 				If not set, function will be applied to $('body').
 * 				Only used for standalone function ($.smAnchor). 
 * @param options Options
 * 					changeUrlHash	Change browser address bar url hash or not. Default is false.
 * 					duration 		Animation duration parameter. Default is "fast".
 * 					easing 			Animation easing parameter
 * 					complete 		Animation complete event handler 
 * 					step 			Animation step event handler
 * 					queue 			Animation queue parameter
 * 					specialEasing 	Animation special easing parameter
 * 					unbind			If set to true, plugin will be disconnected
 * 					on				Use jQuery "on" function to attach handlers to all elements filtered by selector. 
 * 									Default is "a[href^="#"]" for standalone function and false for jQuery object method.
 * 					live, delegate	Use deprecated function. 
 * 
 * Example:
 * $.smAnchor(); //processes all anchors with "href" attribute starts with "#" (hash) symbol
 * 
 * $.smAnchor({  //set animation duration to 1000ms and filter elements by "on" selector
 * 		duration: 1000,
 * 		on: 'span.smAnchor'
 * });
 * 
 * $('span.smAnchor').smAnchor({ //process elements filtered by selector and set animation duration to "slow"
 * 		duration: 'slow'
 * });
 * 
 * $('ul.anchors').smAnchor({ //it's a little bit tricky. Let's say it's just like this: $('ul.anchors').on('click', 'a[href^="#"]', smAnchorHandlerFunction);
 * 		on: 'a[href^="#"]'
 * });
 */
(function ($) {
	var methods = {
		'init': function (elem, options) {
			var opts = $.extend({
				'changeUrlHash': true,
				'duration': 'slow',
				'on': 'a[href^="#"]'
			}, options),
			animate_opts = {
				'duration': opts.duration,
				'easing': opts.easing,
				'complete': opts.complete,
				'step': opts.step,
				'queue': opts.queue || false,
				'specialEasing': opts.specialEasing
			},
			_this = elem?$(elem):$('html,body');
			
			$.smAnchor(elem, 'unbind');

			var clickHandler = function () {
				if ($(this).attr('href')) {
					var aName = $(this).attr('href').replace('#', ''),
						dest = $('a[name="' + aName + '"]:eq(0)'),
						container = $('html,body');
					dest.parents().each(function () {
						var obj = $(this);
						if (obj.css('overflow-x') != 'visible') {
							container = obj;
							return false;
						};
					}); console.log(container, (container.is('html,body')?dest.offset().top:'+=' + (dest.offset().top - container.offset().top)));
				    container
						.stop(true)
						.animate({
							'scrollTop': (container.is('html,body')?dest.offset().top:'+=' + (dest.offset().top - container.offset().top))
						}, animate_opts);

					if (opts.changeUrlHash) {
						window.location.hash = aName;
					}
				}

				return false;
			};
			
			if (opts.live) {
				$(opts.live).live('click.smAnchor', clickHandler);
			} else if (opts.delegate || (opts.on && !$().on)) {
				_this.delegate(opts.delegate || opts.on, 'click.smAnchor', clickHandler);
			} else if (opts.on) {
				_this.on('click.smAnchor', opts.on, clickHandler);
			} else {
				_this.bind('click.smAnchor', clickHandler);
			}
		},
		'unbind': function (elem) {
			(elem?$(elem):$('body')).unbind('.smAnchor')
				.off('.smAnchor')
				.die('.smAnchor')
				.undelegate('.smAnchor');
		}
	};
	
	$.smAnchor = function (elem, method, options) {
		if ( methods[method] ) {
			return methods[method].call( this, elem, options);
	    } else if ( typeof method === 'object' || ! method ) {
	    	return methods.init.call( this, elem, options );
	    } else {
	    	$.error( 'Method ' +  method + ' does not exist on jQuery.smAnchor' );
	    }   
	};

	$.fn.smAnchor = function (method, options) {
		$.smAnchor(this, method, $.extend({
			'on': false
		}, options));
		return this;
	};
})(jQuery);