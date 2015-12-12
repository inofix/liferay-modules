/**
 * 
 * Created: 	2015-08-22 19:42 by Christian Berndt
 * Modified:	2015-12-12 13:43 by Christian Berndt
 * Version: 	1.0.4
 */
$(document).ready(function() {
	
	$('.projects-min.flexslider.slider').flexslider({
		animation: "fade",
		controlNav: true,
		directionNav: false,
		animationLoop: true,
		slideshow: true
	});	
	
	// The slider being synced must be initialized first
	$('.projects.flexslider.carousel').flexslider({
		animation: "slide",
		controlNav: false,
		directionNav: false,
		animationLoop: false,
		slideshow: false,
		itemWidth: 390,
		asNavFor: '.projects.flexslider.slider'
	});
	 
	$('.projects.flexslider.slider').flexslider({
		animation: "fade",
		controlNav: true,
		directionNav: false,
		animationLoop: true,
		slideshow: true,
		sync: ".projects.flexslider.carousel"
	});
});
