
	function customReload() {
		   // Some custom code...
		   oTable_myTableId.fnReloadAjax();
		   // Some custom code...
		}

	$(document).ready(function() {
		$(function () {
			$('[data-toggle="tooltip"]').tooltip()
		})
		
		
		
		
		
		
	/*    var myTable = $('#userstable').DataTable({
	    	"ajax": '/users/ajax/userdata.json',
	    	"columns": [
	    	            { "data": "name" },
	    	            { "data": "login" },
	    	            { "data": "role" },
	    	            { "data": "status" },
	    	            { "data": "lastLogin" },
	    	            { "data": "signinDate" },
	    	            { "data": "blockDate" },
	    	            { "data": "id" }
	    	        ],
	    	
	    	
			"language": {
	        	"url": "/static/assets/data-tables-1.10.6/i18n/russian.lang"
	        },
	        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "Все"]],
	        "order": [[ 1, 'asc' ]],
	        "columnDefs": [
				{
	    			"searchable": false,
	    			"orderable": false,
	    			"targets": 0
				},
	            {
		         	"targets": -1,
		         	"searchable": false,
	    			"orderable": false,
		            "data": "Действия",
		            "render": function (data, type, full, meta) {
		            	var updateField = '<a class="update" href="/users/update/'+data+'" data-toggle="tooltip" title="" data-original-title="Редактировать"> \
		     	 								<i class="glyphicon glyphicon-pencil"></i></a>';
		     	 		var deleteField = '<a class="delete" href="/users/delete/'+data+'" data-toggle="tooltip" title="" data-original-title="Удалить"> \
		         								<i class="glyphicon glyphicon-trash"></i> </a>'; 	 
			            return "<center>"+updateField+"&nbsp;&nbsp;&nbsp;"+deleteField+"</center>";
	           	}
	        }]
	    });
		
		/*myTable.on( 'order.dt search.dt', function () {
			myTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
	        } );
	    } ).draw();*/
		
		
		
	});
	