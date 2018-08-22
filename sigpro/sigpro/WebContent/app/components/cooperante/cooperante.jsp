<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="cooperanteController as cooperantec" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="8010">
			<p ng-init="cooperantec.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<script type="text/ng-template" id="tipoAdquisicion.jsp">
    	<%@ include file="/app/components/cooperante/tipoAdquisicion.jsp"%>
  		</script>
		
		<div class="panel panel-default">
		    <div class="panel-heading"><h3>Cooperantes</h3></div>
		</div>
		<div class="row" align="center" ng-hide="cooperantec.mostraringreso">
			
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="8040">
			    <label class="btn btn-primary" ng-click="cooperantec.nuevo()" uib-tooltip="Nuevo">
			    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="8020">
			    <label class="btn btn-primary" ng-click="cooperantec.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="8030">
			    <label class="btn btn-danger" ng-click="cooperantec.borrar()" uib-tooltip="Borrar">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
    		<shiro:hasPermission name="8010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="cooperantec.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="cooperantec.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!cooperantec.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br>
				<div class="total-rows">
				  Total de {{  cooperantec.totalCooperantes + (cooperantec.totalCooperantes == 1 ? " Cooperante" : " Cooperantes" ) }}
				</div>
				<ul uib-pagination total-items="cooperantec.totalCooperantes" 
						ng-model="cooperantec.paginaActual" 
						max-size="cooperantec.numeroMaximoPaginas" 
						items-per-page="cooperantec.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="cooperantec.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row second-main-form" ng-show="cooperantec.mostraringreso">
			<div class="page-header">
			    <h2 ng-hide="!cooperantec.esnuevo"><small>Nuevo cooperante</small></h2>
			    <h2 ng-hide="cooperantec.esnuevo"><small>Edición de cooperante</small></h2>
			</div>
    		<div class="operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="8020">
			      <label class="btn btn-success" ng-click="form.$valid ? cooperantec.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="cooperantec.irATabla()" uib-tooltip="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
						  <label for="id" class="floating-label id_class">ID {{ cooperantec.cooperante.id }}</label>
						  <br/><br/>
						</div>
						<div class="form-group">
						   <input type="number" name="codigo"  class="inputText" id="codigo" 
						     ng-model="cooperantec.cooperante.codigo" ng-value="cooperantec.cooperante.codigo"   
						     onblur="this.setAttribute('value', this.value);" ng-required="true">
						   <label class="floating-label">* Código</label>
						</div>
						<div class="form-group">
						   <input type="text" name="nombre"  class="inputText" id="nombre" 
						     ng-model="cooperantec.cooperante.nombre" ng-value="cooperantec.cooperante.nombre"   
						     onblur="this.setAttribute('value', this.value);" ng-required="true" >
						   <label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
						   <input type="text" name="siglas"  class="inputText" id="siglas" 
						     ng-model="cooperantec.cooperante.siglas" ng-value="cooperantec.cooperante.siglas"   
						     onblur="this.setAttribute('value', this.value);">
						   <label class="floating-label">Siglas</label>
						</div>
						<div class="form-group">
						   <textarea class="inputText" rows="4"
						   ng-model="cooperantec.cooperante.descripcion" ng-value="cooperantec.cooperante.descripcion"   
						   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
						   <label class="floating-label">Descripción</label>
						</div>
						<br/>
						<div align="center">
							<h5 class="label-form">Tipo de Adquisición</h5>
							<br/>
							<table style="width: 75%;"
							st-table="cooperantec.cooperanteTipoAdquisiciones"
							class="table table-striped  table-bordered">
								<thead >
									<tr>
										<th class="label-form">ID</th>
										<th class="label-form">Nombre</th>
									</tr>
								</thead>
								<tbody>
									<tr st-select-row="row"
										ng-repeat="row in cooperantec.cooperanteTipoAdquisiciones">
										<td>{{row.id}}</td>
										<td>{{row.nombre}}</td>
									</tr>
								</tbody>
							</table>
						</div>

						<div class="panel panel-default">
							<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6" >
										<div class="form-group" style="text-align: right">
											<label class="label-form" for="usuarioCreo">Usuario que creo</label>
				    						<p  id="usuarioCreo"> {{ cooperantec.cooperante.usuarioCreo }}</p>
										</div>
									</div>
									<div class="col-sm-6" >
										<div class="form-group">
											<label for="fechaCreacion" class="label-form">Fecha de creación</label>
				    						<p id="fechaCreacion"> {{ cooperantec.cooperante.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6" >
										<div class="form-group" style="text-align: right">
											<label class="label-form" for="usuarioActualizo">Usuario que actualizo</label>
				    						<p  id="usuarioCreo"> {{ cooperantec.cooperante.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6" >
										<div class="form-group">
											<label class="label-form" for="fechaActualizacion">Fecha de actualizacion</label>
				    						<p  id="usuarioCreo" > {{ cooperantec.cooperante.fechaActualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
				</form>
			</div>
			<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
    		<div class=" col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="8020">
			      <label class="btn btn-success" ng-click="form.$valid ? cooperantec.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="cooperantec.irATabla()" uib-tooltip="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>
		</div>
	</div>
