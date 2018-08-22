<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="desembolsotipoController as desembolsotipoc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="35010">
			<p ng-init="desembolsotipoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>	
		
		<div class="panel panel-default">
		    <div class="panel-heading"><h3>Tipo Desembolso</h3></div>
		</div>
		
		<div class="row" align="center" ng-if="!desembolsotipoc.mostraringreso">
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="35040">
			    <label class="btn btn-primary" ng-click="desembolsotipoc.nuevo()" uib-tooltip="Nuevo">
			    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="35020">
			    <label class="btn btn-primary" ng-click="desembolsotipoc.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="35030">
			    <label class="btn btn-danger" ng-click="desembolsotipoc.borrar()" uib-tooltip="Borrar">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
    		<shiro:hasPermission name="35010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="desembolsotipoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="desembolsotipoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!desembolsotipoc.mostrarcargando">
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
				  Total de {{  desembolsotipoc.totalDesembolsoTipo + (desembolsotipoc.totalDesembolsoTipo == 1 ? " Tipo de Desembolso" : " Tipos de Desembolsos" ) }}
				</div>
				<ul uib-pagination total-items="desembolsotipoc.totalTiposDesembolso" 
						ng-model="desembolsotipoc.paginaActual" 
						max-size="desembolsotipoc.numeroMaximoPaginas" 
						items-per-page="desembolsotipoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="desembolsotipoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row second-main-form" ng-show="desembolsotipoc.mostraringreso">
			<div class="page-header">
			    <h2 ng-hide="!desembolsotipoc.esnuevo"><small>Nuevo Tipo de Desembolso</small></h2>
			    <h2 ng-hide="desembolsotipoc.esnuevo"><small>Edición de Tipo de Desembolso</small></h2>
			</div>
    		
		   <div class="operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="35020">
			      <label class="btn btn-success" ng-click="form.$valid ? desembolsotipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="desembolsotipoc.irATabla()" uib-tooltip="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
						  <label for="id" class="floating-label id_class">ID {{ desembolsotipoc.desembolsotipo.id }}</label>
						  <br/><br/>
						</div>
						
						<div class="form-group">
						   <input type="text" name="nombre"  class="inputText" id="nombre" 
						     ng-model="desembolsotipoc.desembolsotipo.nombre" ng-value="desembolsotipoc.desembolsotipo.nombre"   
						     onblur="this.setAttribute('value', this.value);" ng-required="true">
						   <label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
						   <textarea class="inputText" rows="4"
						   ng-model="desembolsotipoc.desembolsotipo.descripcion" ng-value="desembolsotipoc.desembolsotipo.descripcion"   
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
									<p  id="usuarioCreo"> {{ desembolsotipoc.desembolsotipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label class="label-form" for="fechaCreacion">Fecha de creación</label>
									<p id="fechaCreacion"> {{ desembolsotipoc.desembolsotipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form"for="usuarioActualizo">Usuario que actualizo</label> 
									<p id="usuarioCreo">{{ desembolsotipoc.desembolsotipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p  id="usuarioCreo">{{ desembolsotipoc.desembolsotipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<br />
			</form>
			</div>
			<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="35020">
			      <label class="btn btn-success" ng-click="form.$valid ? desembolsotipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="desembolsotipoc.irATabla()" uib-tooltip="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>
		</div>
	</div>
