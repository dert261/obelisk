$(document).ready(function() {
	
		$(function () {
			$('[data-toggle="tooltip"]').tooltip()
		})
		
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
		
		
	