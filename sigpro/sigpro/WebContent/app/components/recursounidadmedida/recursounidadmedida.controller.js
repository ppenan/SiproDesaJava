var app = angular.module('recursounidadmedidaController', []);

app.controller('recursounidadmedidaController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Unidades de Medida de Recursos';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.medidas = [];
			mi.medida;
			mi.mostraringreso=false;
			mi.esnueva = false;
			mi.totalmedidas = 0;
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			mi.filtros = [];
			
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.recursounidadc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
					columnDefs : [ 
						{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
						{ name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
							filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursounidadc.filtros[\'nombre\']"  ng-keypress="grid.appScope.recursounidadc.filtrar($event)"></input></div>'
						},
						{ name: 'simbolo', width: 85, displayName: 'Símbolo', cellClass: 'grid-align-center', enableFiltering: false},
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursounidadc.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.recursounidadc.filtrar($event)"></input></div>'
					    },
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursounidadc.filtros[\'fecha_creacion\']"  ng-keypress="grid.appScope.recursounidadc.filtrar($event)"></input></div>'
						}
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.medida = row.entity;
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.recursounidadc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.recursounidadc.ordenDireccion = sortColumns[0].sort.direction;
								for(var i = 0; i<sortColumns.length-1; i++)
									sortColumns[i].unsort();
								grid.appScope.recursounidadc.cargarTabla(grid.appScope.recursounidadc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.recursounidadc.columnaOrdenada!=null){
									grid.appScope.recursounidadc.columnaOrdenada=null;
									grid.appScope.recursounidadc.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							mi.obtenerTotalUnidadesMedida();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'recursosunidadmedidas', t: (new Date()).getTime()}).then(function(response){
					    		  if(response.data.success && response.data.estado!='')
					    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							      mi.obtenerTotalUnidadesMedida();
							  });
					    }
					}
				};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SRecursoUnidadMedida', { accion: 'getRecursoUnidadMedidasPagina', pagina: pagina, numerorecursounidadmedidas: $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()
					}).success(
				
						function(response) {
							mi.medidas = response.RecursoUnidadMedidas;
							mi.gridOptions.data = mi.medidas;
							mi.mostrarcargando = false;
						});
			}
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			mi.guardar=function(){
				if(mi.medida!=null && mi.medida.nombre!=''){
					$http.post('/SRecursoUnidadMedida', {
						accion: 'guardarRecursoUnidadMedida',
						esnueva: mi.esnueva,
						id: mi.medida.id,
						codigo: mi.medida.codigo,
						nombre: mi.medida.nombre,
						descripcion: mi.medida.descripcion,
						simbolo: mi.medida.simbolo,
						t: (new Date()).getTime()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','medida '+(mi.esnueva ? 'creada' : 'guardada')+' con éxito');
							mi.esnueva = false;
							mi.medida.id = response.id;
							mi.medida.usuarioCreo = response.usuarioCreo;
							mi.medida.fechaCreacion = response.fechaCreacion;
							mi.medida.usuarioActualizo = response.usuarioactualizo;
							mi.medida.fechaActualizacion = response.fechaactualizacion;
							mi.obtenerTotalUnidadesMedida();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnueva ? 'crear' : 'guardar')+' la unidad de medida');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.medida!=null && mi.medida.id!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar la unidad de medida "'+mi.medida.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SRecursoUnidadMedida', {
								accion: 'borrarRecursoUnidadMedida',
								id: mi.medida.id,
								t: (new Date()).getTime()
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Unidad de medida borrada con éxito');
									mi.medida = null;
									mi.obtenerTotalUnidadesMedida();
								}
								else
									$utilidades.mensaje('danger','Error al borrar la unidad de medida');
							});
						}
					}, function(){
						
					});
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la unidad de medida que desea borrar');
			};

			mi.nueva = function() {
				mi.mostraringreso=true;
				mi.esnueva = true;
				mi.medida = {};
				mi.gridApi.selection.clearSelectedRows();
				$utilidades.setFocus(document.getElementById("nombre"));
			};

			mi.editar = function() {
				if(mi.medida!=null && mi.medida.id!=null){
					mi.mostraringreso = true;
					mi.esnueva = false;
					$utilidades.setFocus(document.getElementById("nombre"));
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la unidad de medida que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
				mi.esnueva=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'recursosunidadmedidas', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/recursounidadmedida/rv')
					$route.reload();
				else
					$location.path('/recursounidadmedida/rv');
			}
			mi.obtenerTotalUnidadesMedida = function(){
				$http.post('/SRecursoUnidadMedida', { accion: 'numeroRecursoUnidadMedidas',
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'], t: (new Date()).getTime()}).success(
				
						function(response) {
							mi.totalmedidas = response.totalRecursoUnidadMedidas;
							mi.cargarTabla(1);
						});
				
			}
			
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.obtenerTotalUnidadesMedida();
					mi.gridApi.selection.clearSelectedRows();
					mi.medida = null;
				}
			}
}]);