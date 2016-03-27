/**
 * 
 * Created: 	2015-08-22 19:42 by Christian Berndt
 * Modified:	2016-03-27 18:26 by Christian Berndt
 * Version: 	1.0.6
 */
$(document).ready(function() {

    $('.projects-min.flexslider.slider').flexslider({
        animation : "fade",
        controlNav : true,
        directionNav : false,
        animationLoop : true,
        slideshow : true
    });

    // The slider being synced must be initialized first
    $('.projects.flexslider.carousel').flexslider({
        animation : "slide",
        controlNav : false,
        directionNav : false,
        animationLoop : false,
        slideshow : false,
        itemWidth : 390,
        asNavFor : '.projects.flexslider.slider'
    });

    $('.projects.flexslider.slider').flexslider({
        animation : "fade",
        controlNav : true,
        directionNav : false,
        animationLoop : true,
        slideshow : true,
        sync : ".projects.flexslider.carousel"
    });

    /**
     * Affix the toc of articles
     */
    $('.article .toc').affix({
        offset : {
            top : 0,
            bottom : 100
        }
    });

    /**
     * Add the target="_blank" attribute dynamically.
     */
    $('a[rel="external"]').attr('target', '_blank');

    /**
     * Smooth scrolling for the TOC - targets (from:
     * http://stackoverflow.com/questions/14804941/how-to-add-smooth-scrolling-to-bootstraps-scroll-spy-function)
     */
    $(".toc ul li a[href^='#']").on('click', function(e) {

        // prevent default anchor click behavior
        e.preventDefault();

        // store hash
        var hash = this.hash;

        // use offset because of fixed top navigation
        var offsetTop = 100;

        // animate
        $('html, body').animate({
            scrollTop : $(hash).offset().top - offsetTop
        }, 300, function() {

            // when done, add hash to url
            // (default click behaviour)
            // does not work this way on firefox.
            // window.location.hash = hash;
        });
    });
});
