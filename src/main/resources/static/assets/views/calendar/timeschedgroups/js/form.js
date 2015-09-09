$(document).ready(function() {
	
		$(function () {
			$('[data-toggle="tooltip"]').tooltip()
		})
		
		
		/*$('#timePeriods').multiSelect({
			dblClick: true,
			keepRenderingSort: true,
									
			selectableHeader: "" +
					"<div class='panel-heading'>" +
						"<h3 class='panel-title'>Доступные периоды</h3>" +
					"</div>" +
					"<div class='input-group'>" +
						"<span class='input-group-addon'><i class='fa fa-search'></i></span>" +
						"<input type='text' class='search-input form-control' autocomplete='off' placeholder=\"Найти...\" ></input>" +
					"</div>",
			
			selectionHeader: "" +
					"<div class='panel-heading'>" +
						"<h3 class='panel-title'>Выбранные периоды</h3>" +
					"</div>" +
					"<div class='input-group'>" +
						"<span class='input-group-addon'><i class='fa fa-search'></i></span>" +
						"<input type='text' class='search-input form-control' autocomplete='off' placeholder=\"Найти...\" ></input>" +
					"</div>",		
						
			afterInit: function(ms){
			    var that = this,
			        $selectableSearch = that.$selectableUl.prev().children().next(),
			        $selectionSearch = that.$selectionUl.prev().children().next(),
			        selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
			        selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';
			    
			    that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
			    .on('keydown', function(e){
			    if (e.which === 40){
			        that.$selectableUl.focus();
			        return false;
			      }
			    });

			    that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
			    .on('keydown', function(e){
			      if (e.which == 40){
			        that.$selectionUl.focus();
			        return false;
			      }
			    });
			  },
			  afterSelect: function(){
			    this.qs1.cache();
			    this.qs2.cache();
			    
			  },
			  afterDeselect: function(){
			    this.qs1.cache();
			    this.qs2.cache();
			    
			  }
		});*/
		
		jQuery.fn.filterByText = function(textbox) {
			return this.each(function() {
				var select = this;
				var options = [];
				$(select).find('option').each(function() {
					options.push({value: $(this).val(), text: $(this).text()});
				});
				$(select).data('options', options);

				$(textbox).bind('change keyup', function() {
					var options = $(select).empty().data('options');
					var search = $.trim($(this).val());
					var regex = new RegExp(search,"gi");

					$.each(options, function(i) {
						var option = options[i];
						if(option.text.match(regex) !== null) {
							$(select).append(
									$('<option>').text(option.text).val(option.value)
							);
						}
					});
				});
			});
		};
							
		function addPeriod(ids) {	
			$.ajax({
				type: 'GET',
				url: "/calendar/timeschedgroups/ajax/serverside/addperiod",
				data: ({
					'ids': ids
				}),
				traditional: true,
			}).done(function(result) {
				updateLists(result);
			});
			return false;
		}
		
		function delPeriod(ids) {	
			$.ajax({
				type: 'GET',
				url: "/calendar/timeschedgroups/ajax/serverside/delperiod",
				data: ({
					'ids': ids
				}), 
				traditional: true,
			}).done(function(result) {
				updateLists(result);
			});
			return false;
		}
		
		function updateLists(data){
			var availableTimePeriods = data.availableTimePeriods;
			var selectedTimePeriods = data.selectedTimePeriods;
			$("#sel2").empty();
			var selectedOptions = selectedTimePeriods;
						
			$.each(selectedOptions, function(i) {
				var selectedOption = selectedOptions[i];
				$("#sel2").append(
					$('<option>').text(selectedOption.name).val(selectedOption.id)
				);
			});
			
			$("#sel1").empty();
			var availableOptions = availableTimePeriods;
						
			$.each(availableOptions, function(i) {
				var availableOption = availableOptions[i];
				$("#sel1").append(
						$('<option>').text(availableOption.name).val(availableOption.id)
				);
			});
			return false;
		}
			
		$("#sel1").filterByText($("#search1"));
		$("#sel2").filterByText($("#search2"));
		
		$("#keepRenderingSort").filterByText($("#search12"));
		$("#keepRenderingSort_to").filterByText($("#search22"));
		
		$("#addNewItems").click(function(){
			var selval = []; 
			$('#sel1 :selected').each(function(i, selected){ 
				selval.push($(selected).val());
			});
			if(selval.length>0) addPeriod(selval);
			return false;
		});
			
		$("#removeItems").click(function(){
			var selval = []; 
			$('#sel2 :selected').each(function(i, selected){ 
				selval.push($(selected).val());
			});
				
			if(selval.length>0) delPeriod(selval);
			return false;
		});
});
		
		
	