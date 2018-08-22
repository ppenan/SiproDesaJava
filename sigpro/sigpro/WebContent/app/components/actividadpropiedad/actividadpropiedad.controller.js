var app = angular.module('actividadpropiedadController', []);

app.controller('actividadpropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Propiedades de Actividad';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.actividadpropiedades = [];
			mi.actividadpropiedad;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalActividadPropiedades = 0;
			mi.datotipoid = "";
			mi.datotiponombre = "";
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.tipodatos = [];
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			

			mi.editarElemento = function (event) {
		        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
		        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
		        mi.editar();
		    };
		    
			mi.filtros = [];
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.actividadpropiedadc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				    useExternalSorting: true,
					columnDefs : [ 
						{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
							filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.actividadpropiedadc.filtros[\'nombre\']" ng-keypress="grid.appScope.actividadpropiedadc.filtrar($event)"></input></div>'
					    },
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'datotiponombre', displayName: 'Tipo dato', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación', cellClass: 'grid-align-left',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.actividadpropiedadc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.actividadpropiedadc.filtrar($event)"></input></div>'
					    },
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.actividadpropiedadc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.actividadpropiedadc.filtrar($event)"></input></div>'
					    }
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.actividadpropiedad = row.entity;
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.actividadpropiedadc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.actividadpropiedadc.ordenDireccion = sortColumns[0].sort.direction;
								for(var i = 0; i<sortColumns.length-1; i++)
									sortColumns[i].unsort();
								grid.appScope.actividadpropiedadc.cargarTabla(grid.appScope.actividadpropiedadc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.actividadpropiedadc.columnaOrdenada!=null){
									grid.appScope.actividadpropiedadc.columnaOrdenada=null;
									grid.appScope.actividadpropiedadc.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							mi.obtenerTotalActividadPropiedades();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'actividadpropiedades', t: (new Date()).getTime()}).then(function(response){
							      if(response.data.success && response.data.estado!=''){
							    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
							    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
								      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
								      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							      }
							      mi.obtenerTotalActividadPropiedades();
							  });
					    	 
					    }
					}
				};
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SActividadPropiedad', { accion: 'getActividadPropiedadPagina', pagina: pagina, numeroactividadpropiedad: $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: new Date().getTime()
				}).success(
				
						function(response) {
							mi.actividadpropiedades = response.actividadpropiedades;
							mi.gridOptions.data = mi.actividadpropiedades;
							mi.mostrarcargando = false;
							mi.paginaActual = pagina;
						});
			}
			
			mi.guardar=function(){
				if(mi.actividadpropiedad!=null && mi.actividadpropiedad.nombre!=''){
					$http.post('/SActividadPropiedad', {
						accion: 'guardarActividadPropiedad',
						esnuevo: mi.esnuevo,
						id: mi.actividadpropiedad.id,
						nombre: mi.actividadpropiedad.nombre,
						descripcion: mi.actividadpropiedad.descripcion,
						datoTipoId: mi.actividadpropiedad.datotipoid.id, 
						t: new Date().getTime()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad de Actividad '+(mi.esnuevo ? 'creada' : 'guardada')+' con éxito');
							mi.actividadpropiedad.id = response.id;
							mi.actividadpropiedad.usuarioCreo=response.usuarioCreo;
							mi.actividadpropiedad.fechaCreacion=response.fechaCreacion;
							mi.actividadpropiedad.usuarioActualizo=response.usuarioactualizo;
							mi.actividadpropiedad.fechaActualizacion=response.fechaactualizacion;
							mi.esnuevo = false;
							mi.obtenerTotalActividadPropiedades();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' la Propiedad de Actividad');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};
			
			mi.borrar = function(ev) {
				if(mi.actividadpropiedad!=null && mi.actividadpropiedad.id!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar la Prppiedad de Actividad "'+mi.actividadpropiedad.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SActividadPropiedad', {
								accion: 'borrarActividadPropiedad',
								id: mi.actividadpropiedad.id, 
								t: new Date().getTime()
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Propiedad de Actividad borrado con éxito');
									mi.actividadpropiedad = null;
									mi.obtenerTotalActividadPropiedades();
								}
								else
									$utilidades.mensaje('danger','Error al borrar la Propiedad de Actividad');
							});
						}
					}, function(){
						
					});						
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad de Actividad que desea borrar');
			};

			mi.nuevo = function() {
				mi.datotipoid = "";
				mi.datotiponombre = "";
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.actividadpropiedad = {};
				mi.gridApi.selection.clearSelectedRows();
				$utilidades.setFocus(document.getElementById("nombre"));
			};

			mi.editar = function() {
				if(mi.actividadpropiedad!=null && mi.actividadpropiedad.id!=null){
					mi.datotipoid = mi.actividadpropiedad.datotipoid;
					mi.datotiponombre = mi.actividadpropiedad.datotiponombre;
					mi.mostraringreso = true;
					mi.esnuevo = false;
					mi.actividadpropiedad.datotipoid = {
							"id" : mi.actividadpropiedad.datotipoid,
							"nombre" : mi.actividadpropiedad.datotiponombre
					}
					$utilidades.setFocus(document.getElementById("nombre"));
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad de Actividad que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'actividadpropiedad', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/actividadpropiedad/rv')
					$route.reload();
				else
					$location.path('/actividadpropiedad/rv');
			}
			
			mi.filtrar = function(evt,tipo){
				if(evt.keyCode==13){
					mi.obtenerTotalActividadPropiedades();
					mi.gridApi.selection.clearSelectedRows();
					mi.actividadpropiedad = null;
				}
			}
			
			mi.obtenerTotalActividadPropiedades = function(){
				$http.post('/SActividadPropiedad', { accion: 'numeroActividadPropiedades', filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'], t: new Date().getTime() }).success(
						function(response) {
							mi.totalActividadPropiedades = response.totalactividadpropiedades;
							mi.cargarTabla(1);
				});
			}
			
			$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
					function(response) {
						mi.tipodatos = response.datoTipos;
			});
			
		} ]);