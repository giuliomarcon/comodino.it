  $(document).ready(function() {

    $('.dispute').clone().appendTo('#disputeRows');
    $('.dispute').clone().appendTo('#disputeRows');

  	$(".dispute").each(function(index) {
  		$(this).on("click", function() {
		  	var disBody = $(this).find(".disBody");
		  	disBody.animate({ height: 'toggle', opacity: 'toggle'}, 'slow');
		  	if($(this).hasClass("disOpen")) {
		  		//chiusura
		  		$(this).addClass('disClosed');
				$(this).removeClass('disOpen');

				$(this).find("i").removeClass('fa-caret-down');
				$(this).find("i").addClass('fa-caret-left');

				$(this).find(".disHeader").animate({ borderBottomLeftRadius : '8px', borderBottomRightRadius : '8px'}, 'slow');
		  	}
		  	else{
		  		//apertura
		  		$(this).addClass('disOpen');
				$(this).removeClass('disClosed');

				$(this).find("i").removeClass('fa-caret-left');
				$(this).find("i").addClass('fa-caret-down');

				$(this).find(".disHeader").animate({ borderBottomLeftRadius : '0px', borderBottomRightRadius : '0px'}, 'slow');
		  	}
		});
	});

  });