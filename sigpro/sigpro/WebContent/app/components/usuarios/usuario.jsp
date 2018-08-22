<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	
	<script type="text/ng-template" id="buscarPermiso.jsp">
   	<%@ include file="/app/components/usuarios/buscarPermiso.jsp"%>
	</script>
	<script type="text/ng-template" id="cambiarPassword.jsp">
   	<%@ include file="/app/components/usuarios/cambiarPassword.jsp"%>
	</script>
	<script type="text/ng-template" id="buscarColaborador.jsp">
   	<%@ include file="/app/components/usuarios/buscarColaborador.jsp"%>
	</script>
	
	<div ng-controller="usuarioController as usuarioc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="34010">
			<p ng-init="usuarioc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Usuarios</h3></div>
		</div>
		
		<div class="row" align="center" ng-hide="usuarioc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="34040">
						<label class="btn btn-primary" ng-click="usuarioc.nuevoUsuario()" uib-tooltip="Nuevo">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="34020">
						<label class="btn btn-primary" ng-click="usuarioc.editarUsuario()" uib-tooltip="Editar">
						<span class="glyphicon glyphicon-pencil"></span> Editar</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="34030">
						<label class="btn btn-danger" ng-click="usuarioc.eliminarUsuario()" uib-tooltip="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
					</shiro:hasPermission>
    			</div>
    		</div>
    		
    		<shiro:hasPermission name="34010">
    			<div class="col-sm-12" align="center">
	    			<div style="height: 35px;">
						<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="usuarioc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
						</div>
						</div>
					</div>
					<br/>
					<div id="grid1" ui-grid="usuarioc.gridOptions" ui-grid-save-state
							ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="usuarioc.myGrid">
							<div class="grid_loading" ng-hide="!usuarioc.mostrarcargando">
							  	<div class="msg">
							      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
									  <br /><br />
									  <b>Cargando, por favor espere...</b>
								  </span>
								</div>
							  </div>
					</div>
					<div class="total-rows">
							  Total de {{  usuarioc.totalUsuarios + (usuarioc.totalUsuarios == 1 ? " Usuario" : " Usuarios" ) }}
						   </div>
					<ul uib-pagination total-items="usuarioc.totalUsuarios"
							ng-model="usuarioc.paginaActual"
							max-size="usuarioc.numeroMaximoPaginas"
							items-per-page="usuarioc.elementosPorPagina"
							first-text="Primero"
							last-text="Último"
							next-text="Siguiente"
							previous-text="Anterior"
							class="pagination-sm" boundary-links="true" force-ellipses="true"
							ng-change="usuarioc.cambiarPagina()"
					></ul>
				</div>
    		</shiro:hasPermission>
		</div>
		<div class="row second-main-form" ng-hide="!usuarioc.isCollapsed">
			<div class="page-header">
				<h2 ng-hide="!usuarioc.esNuevo"><small>Nuevo Usuario</small></h2>
				<h2 ng-hide="usuarioc.esNuevo"><small>Edición de Usuario</small></h2>
			</div>
			
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="34020">
						<label class="btn btn-success" ng-click="usuarioc.mForm.$valid && usuarioc.botones ? usuarioc.guardarUsuario() : ''" 
							ng-disabled="!usuarioc.mForm.$valid || !usuarioc.botones" uib-tooltip="Guardar">
						<span class="glyphicon glyphicon-floppy-saved"></span>Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="usuarioc.mForm.$valid && usuarioc.botones ? usuarioc.cancelar() : ''"  uib-tooltip="Ir a Tabla" ng-disabled="!usuarioc.mForm.$valid || !usuarioc.botones">
					<span class="glyphicon glyphicon-list-alt"></span>Ir a Tabla</label>
    			</div>
    		</div>
    		
    		<div class="col-sm-12">
    			<form name="usuarioc.mForm">
    				<div class="row">
    					<div class="form-group">
			 				<input type="text" class="inputText" ng-model="usuarioc.usuariosSelected.usuario" ng-required="true"
			 					ng-value="usuarioc.usuariosSelected.usuario" onblur="this.setAttribute('value', this.value);" ng-readonly="!usuarioc.esNuevo">
			 				<label class="floating-label">* Usuario</label>
						</div>
						
						<div class="form-group">
		 					<input type="text" class="inputText" ng-model="usuarioc.usuariosSelected.email" ng-required="true"
		  						ng-value="usuarioc.usuariosSelected.email" onblur="this.setAttribute('value', this.value);">
		  					<label class="floating-label">* Correo electrónico</label>
						</div>
						
						<div class="form-group">   			
			    			<select class="inputText" ng-model="usuarioc.sistemaSeleccionado" 
			    			ng-options="tipo as tipo.claseNombre for tipo in usuarioc.sistemasUsuario track by tipo.id" 
			    			ng-required="true">
								<option disabled selected value>Seleccione Tipo</option>
							</select>
					        <label for="campo2" class="floating-label">* Sistema</label>  
			    		 </div>
						
						<div class="form-group">
		  					<input type="password" class="inputText" ng-model="usuarioc.usuariosSelected.password" ng-required="usuarioc.esNuevo"
		  						ng-value="usuarioc.usuariosSelected.password" onblur="this.setAttribute('value', this.value);">
		  					<label class="floating-label">* Contraseña</label>
						</div>
						
						<div class="form-group">
	   						<input type="password" class="inputText" ng-model="usuarioc.usuariosSelected.password2" ng-required="usuarioc.esNuevo"
	   							ng-value="usuarioc.usuariosSelected.password2" onblur="this.setAttribute('value', this.value);">
	   						<label class="floating-label">* Vuelva a ingresar la contraseña</label>
						</div>
    				</div>
    				<br/>
	   				<div class="row">
	   					<div class="col-sm-6">
							<h3 ng-show="!usuarioc.esNuevo">Estructuras</h3>
						</div>
						<div class="col-sm-6" align="right">
							<div class="btn-group">
						    	<label class="btn btn-default" ng-click="usuarioc.seleccionarTodo()">Todos</label>
						    	<label class="btn btn-default" ng-click="usuarioc.deseleccionarTodo()">Ninguno</label>		       
					    	</div>
						</div>
						<div style="width: 100%; height:300px; overflow-y: scroll;" class="panel panel-default">
						    <div treecontrol="" class="tree-light" tree-model="usuarioc.treedata" options="usuarioc.tree_options" 
							expanded-nodes="usuarioc.expanded" on-selection="usuarioc.showSelected(node)" style="width: 1000px; margin: 10px 0 0 0;">
	     				  	  <span ng-switch="" on="node.objeto_tipo">
						             <span ng-switch-when="1" class="glyphicon glyphicon-record" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="2" class="glyphicon glyphicon-th" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="3" class="glyphicon glyphicon-certificate" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="4" class="glyphicon glyphicon-link" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="5" class="glyphicon glyphicon-time" aria-hidden="true" style="color: #4169E1;"></span>
						        </span><input type="checkbox" ng-model='node.estado' ng-change='usuarioc.onChange(node)' indeterminate id="{{ node.objeto_tipo + '_' +node.id}}" />{{node.nombre}}
							</div>
					    </div>
					    <div class="grid_loading" ng-hide="usuarioc.esNuevo" ng-if="!usuarioc.cargarArbol" style="width: 100%; height: 310px; margin-top:17%;">
							<div class="msg">
								<span><i class="fa fa-spinner fa-spin fa-4x"></i>
									<br><br>
									<b>Cargando, por favor espere... </b>
								</span>
							</div>
						</div>
	   				</div>
	   				<br/>
	   				<div class="row">
						<h3 style="margin-left: 15px;" ng-show="usuarioc.isCollapsed">Permisos</h3>
						<div class="col-sm-12 operation_buttons" align="right"  ng-if="usuarioc.esNuevo">
							<div class="btn-group">
								<label class="btn btn-default" ng-click="usuarioc.buscarPermiso(0)"
									uib-tooltip="Agregar permiso" ng-disabled="usuarioc.cargandoPermisos">
									<span class="glyphicon glyphicon-plus"></span>
									Agregar permiso.
								</label>
							</div>
						</div>
						<div class="col-sm-12 operation_buttons" align="right"  ng-if="!usuarioc.esNuevo">
							<div class="btn-group">
								<label class="btn btn-default" ng-click="usuarioc.buscarPermiso(0)"
														ng-disabled="" 
													uib-tooltip="Agregar permiso" ng-disabled="usuarioc.cargandoPermisos">
									<span class="glyphicon glyphicon-plus"></span>
									Agregar permiso.
								</label>
							</div>
						</div>
					</div>
					<br>
					<div class="row">		
						<div style="max-height: 375px; overflow: auto">
							<table style="width: 100%; margin-bottom: 20px;" st-table="usuarioc.permisosAsignados"
								class="table table-striped  table-bordered table-hover table-propiedades">
								<thead>
									<tr>
										<th style="white-space: nowrap;">Nombre</th>
										<th style="white-space: nowrap;">Descripción</th>
										<th style="width: 30px;">Quitar</th>
				
									</tr>
								</thead>
								<tbody>
									<tr st-select-row="row"
										ng-repeat="row in usuarioc.permisosAsignados">
										<td style="white-space: nowrap;">{{row.nombre}}</td>
										<td style="white-space: nowrap;">{{row.descripcion}}</td>
										<td>
											<button type="button"
												ng-click="usuarioc.eliminarPermiso(row)"
												class="btn btn-sm btn-danger">
												<i class="glyphicon glyphicon-minus-sign"> </i>
											</button>
										</td>
									</tr>
								</tbody>
							</table>
							<div class="grid_loading" ng-if="usuarioc.cargandoPermisos" style="margin-top:80px; width: 96%; margin-left: 1%;">
								<div class="msg">
									<span><i class="fa fa-spinner fa-spin fa-4x"></i>
										<br><br>
										<b>Cargando, por favor espere...</b>
									</span>
								</div>
							</div> 	
						</div>							
	   				</div>
    			</form>
    		</div>
		</div>
	</div>