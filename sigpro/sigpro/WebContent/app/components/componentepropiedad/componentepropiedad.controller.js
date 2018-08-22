var app = angular.module('componentepropiedadController', []);

app.controller('componentepropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Componente Propiedad';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.componentepropiedades = [];
			mi.componentepropiedad;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalComponentePropiedades = 0;
			
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.tipodatos = [];
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			mi.filtros = [];
			mi.orden = null;
			
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.componentepropiedadc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				    useExternalSorting: true,
					columnDefs : [ 
						{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left', 
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentepropiedadc.filtros[\'nombre\']" ng-keypress="grid.appScope.componentepropiedadc.filtrar($event)"></input></div>',
					    },
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'datotiponombre', displayName: 'Tipo dato', cellClass: 'grid-align-left', enableFiltering: false, enableSorting: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación', cellClass: 'grid-align-left',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentepropiedadc.filtros[\'usuarioCreo\']" ng-keypress="grid.appScope.componentepropiedadc.filtrar($event)" ></input></div>'
					    },
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentepropiedadc.filtros[\'fechaCreacion\']" ng-keypress="grid.appScope.componentepropiedadc.filtrar($event)" style="width:80px;" ></input></div>'
					    }
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.componentepropiedad = row.entity;
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.componentepropiedadc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.componentepropiedadc.ordenDireccion = sortColumns[0].sort.direction;
								
								grid.appScope.componentepropiedadc.cargarTabla(grid.appScope.componentepropiedadc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.componentepropiedadc.columnaOrdenada!=null){
									grid.appScope.componentepropiedadc.columnaOrdenada=null;
									grid.appScope.componentepropiedadc.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							mi.obtenerTotalComponentePropiedades();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'componentepropiedades', t: (new Date()).getTime()}).then(function(response){
							      if(response.data.success && response.data.estado!=''){
							    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
							    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
								      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
								      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							      }
							      mi.obtenerTotalComponentePropiedades();
							  });
					    }
					}
				};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SComponentePropiedad', { accion: 'getComponentePropiedadPagina', pagina: pagina, numerocomponentepropiedades: $utilidades.elementosPorPagina,
					numeroproyecto:  $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()		
					}).success(
						function(response) {
							mi.componentepropiedades = response.componentepropiedades;
							mi.gridOptions.data = mi.componentepropiedades;
							mi.mostrarcargando = false;
							mi.paginaActual = pagina;
						});
			}
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			
			mi.guardar=function(){
				if(mi.componentepropiedad!=null && mi.componentepropiedad.nombre!=null){
					$http.post('/SComponentePropiedad', {
						accion: 'guardarComponentePropiedad',
						esnuevo: mi.esnuevo,
						id: mi.componentepropiedad.id,
						nombre: mi.componentepropiedad.nombre,
						descripcion: mi.componentepropiedad.descripcion,
						datoTipoId: mi.datotipo.id, t: (new Date()).getTime()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad Componente '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.componentepropiedad.id = response.id;
							mi.componentepropiedad.usuarioCreo=response.usuarioCreo;
							mi.componentepropiedad.fechaCreacion=response.fechaCreacion;
							mi.componentepropiedad.usuarioActualizo=response.usuarioactualizo;
							mi.componentepropiedad.fechaActualizacion=response.fechaactualizacion;
							mi.esnuevo = false;
							mi.obtenerTotalComponentePropiedades();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' la Propiedad Componente');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.componentepropiedad!=null && mi.componentepropiedad.id!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar la Prppiedad Componente "'+mi.componentepropiedad.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SComponentePropiedad', {
								accion: 'borrarComponentePropiedad',
								id: mi.componentepropiedad.id, t: (new Date()).getTime()
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Propiedad Componente borrado con éxito');
									mi.componentepropiedad = null;
									mi.cargarTabla();
								}
								else
									$utilidades.mensaje('danger','Error al borrar la Propiedad Componente');
							});
						}
					}, function(){
						
					});		
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Componente que desea borrar');
			};

			mi.nuevo = function() {
				mi.datotipo = null;
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.componentepropiedad = {};
				mi.gridApi.selection.clearSelectedRows();
				$utilidades.setFocus(document.getElementById("nombre"));
			};

			mi.editar = function() {
				if(mi.componentepropiedad!=null && mi.componentepropiedad.id!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
					mi.datotipo = {
							"id" : mi.componentepropiedad.datotipoid,
							"nombre" : mi.componentepropiedad.datotiponombre
					}
					
					$utilidades.setFocus(document.getElementById("nombre"));
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Componente que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'componentepropiedades', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/componentepropiedad/rv')
					$route.reload();
				else
					$location.path('/componentepropiedad/rv');
			}
			
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.obtenerTotalComponentePropiedades();
					mi.gridApi.selection.clearSelectedRows();
					mi.componentepropiedad = null;
				}
			}

			mi.obtenerTotalComponentePropiedades = function(){
				$http.post('/SComponentePropiedad', { accion: 'numeroComponentePropiedades',
					filtro_nombre: mi.filtros['nombre'],
					filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: (new Date()).getTime()  }).then(
						function(response) {
							mi.totalComponentePropiedades = response.data.totalcomponentepropiedades;
							mi.paginaActual = 1;
							mi.cargarTabla(mi.paginaActual);
				});
			}
			
			
			$http.post('/SDatoTipo', { accion: 'cargarCombo', t: (new Date()).getTime() }).success(
					function(response) {
						mi.tipodatos = response.datoTipos;
			});
			
		} ]);