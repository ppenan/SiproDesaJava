<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="recursopropiedadController as recursopropiedadc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="27010">
			<p ng-init="recursopropiedadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
	  		<div class="panel-heading"><h3>Propiedades de Recurso</h3></div>
		</div>
	
		<div align="center" ng-hide="recursopropiedadc.mostraringreso">
		<br>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="27040">
			       		<label class="btn btn-primary" ng-click="recursopropiedadc.nueva()" uib-tooltip="Nueva">
				<span class="glyphicon glyphicon-plus"></span> Nueva</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="27010"><label class="btn btn-primary" ng-click="recursopropiedadc.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="27030">
			       		<label class="btn btn-danger" ng-click="recursopropiedadc.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>


    			</div>
    		</div>
    		<shiro:hasPermission name="27010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="recursopropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="recursopropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!recursopropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  recursopropiedadc.totalRecursoPropiedades + (recursopropiedadc.totalRecursoPropiedades == 1 ? " Propiedad" : " Propiedades" ) }}</div>
				<ul uib-pagination total-items="recursopropiedadc.totalRecursoPropiedades"
						ng-model="recursopropiedadc.paginaActual"
						max-size="recursopropiedadc.numeroMaximoPaginas"
						items-per-page="recursopropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="recursopropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="recursopropiedadc.mostraringreso">
			<div class="page-header">
			<h2 ng-hide="!recursopropiedadc.esnuevo"><small>Nueva Propiedad</small></h2>
			<h2 ng-hide="recursopropiedadc.esnuevo"><small>Edición de Propiedad</small></h2>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="27020">
			        	<label class="btn btn-success" ng-click="form.$valid ? recursopropiedadc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="recursopropiedadc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			<br>
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label id_class">ID {{ recursopropiedadc.recursopropiedad.id }}</label>
    						<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" id="nombre" class="inputText" ng-model="recursopropiedadc.recursopropiedad.nombre" ng-value="recursopropiedadc.recursopropiedad.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true">
							<label for="nombre" class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
							<select class="inputText" 
								ng-model="recursopropiedadc.datoTipoSeleccionado" 
								ng-options="opcion as opcion.nombre for opcion in recursopropiedadc.tipodatos" ng-required="true"
								ng-readonly="true" 
								ng-disabled="!recursopropiedadc.esnuevo">
								<option value="">Seleccione un tipo</option>
							</select>
							<label for="nombre" class="floating-label">* Tipo dato</label>
						</div>
						<div class="form-group">
						   <textarea class="inputText" rows="4"
						   ng-model="recursopropiedadc.recursopropiedad.descripcion" ng-value="recursopropiedadc.recursopropiedad.descripcion"   
						   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
						   <label class="floating-label">Descripción</label>
						</div>
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p class=""> {{ recursopropiedadc.recursopropiedad.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p class="" id="fechaCreacion"> {{ recursopropiedadc.recursopropiedad.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p class="" id="usuarioCreo">{{ recursopropiedadc.recursopropiedad.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p class="" id="usuarioCreo">{{ recursopropiedadc.recursopropiedad.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				</form>
			</div>
			<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
						<shiro:hasPermission name="27020">
				        	<label class="btn btn-success" ng-click="form.$valid ? recursopropiedadc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				        </shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="recursopropiedadc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
