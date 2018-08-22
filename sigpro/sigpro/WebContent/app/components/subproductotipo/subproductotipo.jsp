<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<div ng-controller="controlSubproductoTipo as subproductoTipo" class="maincontainer all_page">
	<script type="text/ng-template" id="buscarPropiedad.jsp">
	    <%@ include file="/app/components/subproductotipo/buscarPropiedad.jsp"%>
	</script>
	<shiro:lacksPermission name="42010">
		<p ng-init="subproductoTipo.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	
	<div class="panel panel-default">
			<div class="panel-heading"><h3>Tipo de Subproducto</h3></div>
		</div>

	<div align="center" ng-hide="subproductoTipo.esForma">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="42040">
					<label class="btn btn-primary" ng-click="subproductoTipo.nuevo()" uib-tooltip="Nuevo">
					<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="42020">
					<label class="btn btn-primary" ng-click="subproductoTipo.editar()" uib-tooltip="Editar">
					<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="42030">
					<label class="btn btn-danger" ng-click="subproductoTipo.borrar()" uib-tooltip="Borrar">
					<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="42010">
			<div class="col-sm-12" align="center">
				<div style="height: 35px;">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="subproductoTipo.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
								<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<br/>
				<div id="grid1" ui-grid="subproductoTipo.opcionesGrid"
					ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
					ui-grid-selection ui-grid-pinning ui-grid-pagination>
					<div class="grid_loading" ng-hide="!subproductoTipo.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  subproductoTipo.totalElementos + (subproductoTipo.totalElementos == 1 ? " Tipo de subproducto" : " Tipos de subproducto" ) }}</div>
				<ul uib-pagination total-items="subproductoTipo.totalElementos"
					ng-model="subproductoTipo.paginaActual"
					max-size="subproductoTipo.numeroMaximoPaginas"
					items-per-page="subproductoTipo.elementosPorPagina"
					first-text="Primero" last-text="Último" next-text="Siguiente"
					previous-text="Anterior" class="pagination-sm"
					boundary-links="true" force-ellipses="true"
					ng-change="subproductoTipo.cambioPagina()"></ul>
			</div>
		</shiro:hasPermission>

	</div>

	<div class="row second-main-form" ng-show="subproductoTipo.esForma">
		<div class="page-header">
			<h2 ng-hide="!subproductoTipo.esNuevo"><small>Nuevo tipo de subproducto</small></h2>
	  		<h2 ng-hide="subproductoTipo.esNuevo"><small>Edición de tipo de subproducto</small></h2>
		</div>

		<div class="col-sm-12 operation_buttons" align="right">

			<div class="btn-group">
				<shiro:hasPermission name="42020">
					<label class="btn btn-success" ng-click="form.$valid ? subproductoTipo.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="subproductoTipo.cancelar()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>

		</div>

		<div class="col-sm-12">
			<form name="form" >

				<div class="form-group">
					<div class="form-group">
						<label for="id" class="floating-label id_class">ID {{subproductoTipo.codigo}}</label>
						<br/><br/>
					</div>
				</div>

				<div class="form-group">
					<input type="text" id="nombre" class="inputText" ng-model="subproductoTipo.nombre" ng-required="true" ng-value="subproductoTipo.nombre" onblur="this.setAttribute('value', this.value);"/>
					<label for="nombre" class="floating-label">* Nombre</label>
				</div>
				<div class="form-group">
				   <textarea class="inputText" rows="4"
				   ng-model="subproductoTipo.descripcion" ng-value="subproductoTipo.descripcion"   
				   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
				   <label class="floating-label">Descripción</label>
				</div>
				
				<br />
	
				<h5 class="label-form" align="center">Propiedades</h5>
				<div align="center">
					
				<div style="height: 35px; width: 75%">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href
								ng-click="subproductoTipo.agregarPropiedad()" role="button"
								uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
								<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<br/>
				<table style="width: 75%;" st-table="subproductoTipo.propiedadesTipo" class="table table-striped  table-bordered">
					<thead >
						<tr>
							<th class="label-form">ID</th>
							<th class="label-form">Nombre</th>
							<th class="label-form">Descripicon</th>
							<th class="label-form">Tipo Dato</th>
							<th style="width: 30px;" class="label-form">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row" st-select-mode="single" ng-repeat="propiedad in subproductoTipo.propiedadesTipo | filter: { estado: '!E'} track by $index">
							<td>{{propiedad.idPropiedad}}</td>
							<td>{{propiedad.propiedad}}</td>
							<td>{{propiedad.descripcion}}</td>
							<td>{{propiedad.propiedadTipo}}</td>
							<td align="center">
								<button type="button"
									 ng-click="subproductoTipo.eliminarPropiedad($index)"
									class="btn btn-sm btn-danger">
									<i class="glyphicon glyphicon-minus-sign"> </i>
								</button>
						</tr>
					</tbody>
				</table>
			</div>
			<br/>
			<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p class=""> {{ subproductoTipo.auditoria.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p class="" id="fechaCreacion"> {{ subproductoTipo.auditoria.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p class="" id="usuarioCreo">{{ subproductoTipo.auditoria.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p class="" id="usuarioCreo">{{ subproductoTipo.auditoria.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>

			</form>
		</div>
		
		<div class="col-sm-12 operation_buttons" align="right">
		<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
			<div class="btn-group">
				<shiro:hasPermission name="42020">
					<label class="btn btn-success" ng-click="form.$valid ? subproductoTipo.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="subproductoTipo.cancelar()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>