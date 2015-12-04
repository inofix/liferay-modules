/**
 * 
 * Created: 	2015-08-22 19:42 by Christian Berndt
 * Modified:	2015-12-04 10:39 by Christian Berndt
 * Version: 	1.0.3
 */
$(document).ready(function() {
	
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
