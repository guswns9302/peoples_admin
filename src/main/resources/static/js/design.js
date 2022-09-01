jQuery(function($){
	//AOS.init();

	//tab
	$.fn.tabContainer = function(){
		return this.each(function(){
			if ($(this).hasClass('nojs')){
				return false;
			}
			var tabAnchor = $(this).find('> li');
			tabAnchor.each(function(){
				var menu = $(this);
				var target = $('#' + menu.find('>a').attr('href').split('#')[1]);
				var link = menu.find('>a');
				target.css('display', 'none');
				if (menu.hasClass('on')) {
					target.css('display', 'block');
				};
				link.click(function(){
					if (!$(this).parents('li').hasClass('on')) {
						tabAnchor.removeClass('on');
						tabAnchor.each(function(){
							$('#' + $(this).find('>a').attr('href').split('#')[1]).css('display', 'none');
						});
					menu.addClass('on');
					target.css('display', 'block');
					};
					return false;
				});
			});
			//tabAnchor.eq(0).trigger('click');
		});
	};
	$('.nav_tabs').tabContainer();

	//switch slide
	$.fn.switch_slide = function(){
		return this.each(function(){
			$(this).slick({
				draggable: false,
				dots: false,
				infinite: true,
				slidesToShow: 1,
				slidesToScroll: 1,
				autoplay: true,
				autoplaySpeed: 20000,
				pauseOnHover: true,
				fade: true,
				//adaptiveHeight: true
			});			
		});
	};
	$('.dash_sd .switch').switch_slide();
});
