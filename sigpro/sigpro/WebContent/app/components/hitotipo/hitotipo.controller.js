var app = angular.module('hitotipoController', []);

app.controller('hitotipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
		var mi=this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Tipo Hito';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.hitotipo = [];
		mi.hitotipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalHitotipo = 0;
		mi.paginaActual = 1;
		mi.datoTipos = [];
		mi.datoTipoSeleccionado = null;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		
		mi.filtros = [];
		
		$http.post('/SDatoTipo', {
			accion : 'cargarCombo',
			t: (new Date()).getTime()
		}).success(function(response) {
			mi.datoTipos = response.datoTipos;
		});
		
		mi.editarElemento = function (event) {
	        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
	        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
	        mi.editar();
	    };
	    
		mi.gridOptions = {
				enableRowSelection : true,
				enableRowHeaderSelection : false,
				multiSelect: false,
				modifierKeysToMultiSelect: false,
				noUnselect: true,
				enableFiltering: true,
				enablePaginationControls: false,
			    paginationPageSize: $utilidades.elementosPorPagina,
			    useExternalFiltering: true,
			    useExternalSorting: true,
			    rowTemplate: '<div ng-dblclick="grid.appScope.hitotipoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					{ name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.hitotipoc.filtros[\'nombre\']" ng-keypress="grid.appScope.hitotipoc.filtrar($event)" style="width:175px;"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación', cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"style="width: 90%;" ng-model="grid.appScope.hitotipoc.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.hitotipoc.filtrar($event)" style="width:90px;"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.hitotipoc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.hitotipoc.filtrar($event)" style="width:80px;" ></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.hitotipo = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.hitotipoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.hitotipoc.ordenDireccion = sortColumns[0].sort.direction;

							grid.appScope.hitotipoc.cargarTabla(grid.appScope.hitotipoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.hitotipoc.columnaOrdenada!=null){
								grid.appScope.hitotipoc.columnaOrdenada=null;
								grid.appScope.hitotipoc.ordenDireccion=null;
							}
						}
					} );
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalHitoTipos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'hitotipos', t: (new Date()).getTime()}).then(function(response){
					      if(response.data.success && response.data.estado!='')
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalHitoTipos();
						  });
				    }
				}
			};
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SHitoTipo', { accion: 'getHitoTiposPagina', pagina: pagina, numerohitotipos: $utilidades.elementosPorPagina,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'] ,
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion,t: (new Date()).getTime() }).success(
					function(response) {
						mi.hitotipos = response.hitotipos;
						mi.gridOptions.data = mi.hitotipos;
						mi.mostrarcargando = false;
					});
		}
		
		mi.guardar=function(){
			if(mi.hitotipo!=null  && mi.hitotipo.nombre!=''){
				$http.post('/SHitoTipo', {
					accion: 'guardarHitoTipo',
					esnuevo: mi.esnuevo,
					id: mi.hitotipo.id,
					nombre: mi.hitotipo.nombre,
					dato_tipo : mi.datoTipoSeleccionado.id,
					descripcion: mi.hitotipo.descripcion
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo Hito '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.hitotipo.id = response.id;
						mi.hitotipo.usuarioCreo = response.usuarioCreo;
						mi.hitotipo.fechaCreacion = response.fechaCreacion;
						mi.hitotipo.usuarioActualizo = response.usuarioactualizo;
						mi.hitotipo.fechaActualizacion = response.fechaactualizacion;
						mi.obtenerTotalHitoTipos(1);
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Tipo Hito');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
	
		mi.borrar = function(ev) {
			if(mi.hitotipo!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el Tipo Hito "'+mi.hitotipo.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SHitoTipo', {
							accion: 'borrarHitoTipo',
							id: mi.hitotipo.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Tipo Hito borrado con éxito');
								mi.hitotipo = null;
								mi.obtenerTotalHitoTipos(1);
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Tipo Hito');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo Hito que desea borrar');
		};
	
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.hitotipo = null;
			mi.datoTipoSeleccionado = null;
			mi.gridApi.selection.clearSelectedRows();
			$utilidades.setFocus(document.getElementById("nombre"));
		};
	
		mi.editar = function() {
			if(mi.hitotipo!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.datoTipoSeleccionado = {
						"id" : mi.hitotipo.idTipo,
						"nombre" : mi.hitotipo.tipo
				}
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo Hito que desea editar');
			$utilidades.setFocus(document.getElementById("nombre"));
		}
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
			mi.esnuevo=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'hitotipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/hitotipo/rv')
				$route.reload();
			else
				$location.path('/hitotipo/rv');
		}
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalHitoTipos();
				mi.gridApi.selection.clearSelectedRows();
				mi.hitotipo = null;
			}
		};

		mi.obtenerTotalHitoTipos=function(){
			$http.post('/SHitoTipo', { accion: 'numeroHitoTipos',
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],t: (new Date()).getTime() }).success(
					function(response) {
						mi.totalHitotipo = response.totalhitotipos;
						mi.cargarTabla(1);
			});
		};
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
	} ]);

