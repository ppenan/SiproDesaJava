<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	

	<style>
		.tamanoDiv{
			height: calc(100% - 100px);
		}
		
		
	</style>

	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="porcentajeactividadesController as porcentajeactividadesc" class="maincontainer all_page" id="title">
	    
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Cartelera de Actividades</h3></div>
		</div>
		
		<div class="grid_loading" ng-hide="!porcentajeactividadesc.mostrarcargando">
		  	<div class="msg">
		      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
				  <br /><br />
				  <b>Cargando, por favor espere...</b>
			  </span>
			</div>
		</div>
		
		
		
		<div class="subtitulo">
			{{ porcentajeactividadesc.objetoTipoNombre }} {{ porcentajeactividadesc.proyectoNombre }}
		</div>
		<div class="col-sm-12 tamanoDiv ">
			<div class="row" align="center" >
				
				<div class="form-group col-sm-5" >
					<select  class="inputText" ng-model="porcentajeactividadesc.tObjeto"
						ng-options="a.text for a in porcentajeactividadesc.tObjetos" ng-change="porcentajeactividadesc.displayObjeto(porcentajeactividadesc.tObjeto.value)"></select>
					<label for="tObjeto" class="floating-label">Tipo Objeto</label>
				</div>
			</div>
			<div class="row" align="center" >
				<div class="form-group col-sm-5" ng-hide="!porcentajeactividadesc.componenteHide">
				<select  class="inputText" ng-model="porcentajeactividadesc.componente"
					ng-options="a.text for a in porcentajeactividadesc.componentes" 
					ng-change="porcentajeactividadesc.getProductos(porcentajeactividadesc.componente.value);"></select>
				<label for="tObjeto" class="floating-label">Componente</label>
				</div>
			</div>
			<div class="row" align="center" >
				<div class="form-group col-sm-5" ng-hide="!porcentajeactividadesc.productoHide">
					<select  class="inputText" ng-model="porcentajeactividadesc.producto"
						ng-options="a.text for a in porcentajeactividadesc.productos" ng-change="porcentajeactividadesc.refrescar()"></select>
					<label for="tObjeto" class="floating-label">Producto</label>
				</div>
			</div>
			<div class="row" align="center"  > 
				
				<div ds:kanban-board items="itemsKanban"  states="states" ng-if="mostrarKanban"
	                     on-adding-new-item="initializeNewItem(item)" on-editing-item="deleteItem(item)"
	                     edit-item-button-text="'?'" edit-item-button-tool-tip="'Delete item'"
	                     on-item-state-changed="onItemStateChanged(item, state)" >
	    		</div ds:kanban-board>
	    		
			</div>
	    </div>
	</div>
