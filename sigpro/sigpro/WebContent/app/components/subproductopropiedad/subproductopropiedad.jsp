<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="controlSubproductoPropiedad as subproductoPropiedad" class="maincontainer all_page">
	<shiro:lacksPermission name="41010">
		<p ng-init="subproductoPropiedad.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	
	<div class="panel panel-default">
			<div class="panel-heading"><h3>Propiedad de Subproducto</h3></div>
		</div>

  <div align="center" ng-hide="subproductoPropiedad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="41040">
        	<label class="btn btn-primary" ng-click="subproductoPropiedad.nuevo()" uib-tooltip="Nuevo">
			<span class="glyphicon glyphicon-plus"></span> Nuevo</label> 
      	</shiro:hasPermission>
      	<shiro:hasPermission name="41020">
        	<label class="btn btn-primary" ng-click="subproductoPropiedad.editar()" uib-tooltip="Editar">
			<span class="glyphicon glyphicon-pencil"></span> Editar</label>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="41030">
        	<label class="btn btn-danger" ng-click="subproductoPropiedad.borrar()" uib-tooltip="Borrar">
			<span class="glyphicon glyphicon-trash"></span> Borrar</label>
      	</shiro:hasPermission>
      </div>
    </div>
    <shiro:hasPermission name="41010">
     <div class="col-sm-12" align="center">
      <div style="height: 35px;">
		<div style="text-align: right;">
			<div class="btn-group" role="group" aria-label="">
				<a class="btn btn-default" href ng-click="subproductoPropiedad.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
			</div>
		</div>
	  </div>
	  <br/>
      <div id="grid1" ui-grid="subproductoPropiedad.opcionesGrid" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
        <div class="grid_loading" ng-hide="!subproductoPropiedad.mostrarCargando">
          <div class="msg">
            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
          </div>
        </div>
      </div>
      <br/>
      <div class="total-rows">Total de {{  subproductoPropiedad.totalElementos + (subproductoPropiedad.totalElementos == 1 ? " Propiedad de Subproducto" : " Propiedades de Subproducto" ) }}</div>
      <ul uib-pagination 
      	total-items="subproductoPropiedad.totalElementos" 
      	ng-model="subproductoPropiedad.paginaActual" 
      	max-size="subproductoPropiedad.numeroMaximoPaginas" 
      	items-per-page="subproductoPropiedad.elementosPorPagina" 
      	first-text="Primero"
        last-text="Último" 
        next-text="Siguiente" 
        previous-text="Anterior" 
        class="pagination-sm" 
        boundary-links="true" 
        force-ellipses="true" 
        ng-change="subproductoPropiedad.cambioPagina()"
      ></ul>
    </div>
    </shiro:hasPermission>
   
  </div>

  <div class="row second-main-form" ng-show="subproductoPropiedad.esForma">
	<div class="page-header">
		<h2 ng-hide="!subproductoPropiedad.esNuevo"><small>Nueva propiedad de subproducto</small></h2>
	  	<h2 ng-hide="subproductoPropiedad.esNuevo"><small>Edición de propiedad de subproducto</small></h2>
	</div>
	
    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
      	<shiro:hasPermission name="41020">
        	<label class="btn btn-success" ng-click="form.$valid ? subproductoPropiedad.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
			<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="subproductoPropiedad.cancelar()" uib-tooltip="Ir a Tabla">
		<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
      </div>
    </div>
    
    <div class="col-sm-12">
	    <form name="form">
	      <div class="form-group" >
		        <label for="id" class="floating-label">ID {{ subproductoPropiedad.codigo }}</label>
		        <br/><br/>
	      </div>

	      <div class="form-group">
	        <input type="text" class="inputText" ng-model="subproductoPropiedad.nombre" ng-required="true"  ng-value="subproductoPropiedad.nombre" onblur="this.setAttribute('value', this.value);"/>
	        <label class="floating-label">* Nombre</label>
	      </div>

	      <div class="form-group">   			
   			<select class="inputText" ng-model="subproductoPropiedad.datoTipoSeleccionado" ng-options="tipo as tipo.nombre for tipo in subproductoPropiedad.datoTipos track by tipo.id" ng-required="true"
   			ng-readonly="true" ng-disabled="!subproductoPropiedad.esNuevo" ng-required="true">
				<option disabled selected value>Seleccione Tipo</option>
			</select>
			<label for="campo2" class="floating-label">* Tipo</label>
   		 </div>
	
	      <div class="form-group">
			   <textarea class="inputText" rows="4"
			   ng-model="subproductoPropiedad.descripcion" ng-value="subproductoPropiedad.descripcion"   
			   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
			   <label class="floating-label">Descripción</label>
			</div>
	      <div class="panel panel-default">
				<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" style="text-align: right">
								<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
								<p class=""> {{ subproductoPropiedad.auditoria.usuarioCreo }}</p>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" >
								<label for="fechaCreacion" class="label-form">Fecha de creación</label>
								<p class="" id="fechaCreacion"> {{ subproductoPropiedad.auditoria.fechaCreacion }} </p>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" style="text-align: right">
								<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
								<p class="" id="usuarioCreo">{{ subproductoPropiedad.auditoria.usuarioActualizo }} </p>
							</div>	
						</div>
						<div class="col-sm-6">		
							<div class="form-group">
								<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
								<p class="" id="usuarioCreo">{{ subproductoPropiedad.auditoria.fechaActualizacion }} </p>
							</div>
						</div>
					</div>
				</div>
			</div>
	    </form>
    </div>
  
    <div align="center" class="label-form">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      <shiro:hasPermission name="41020">
        	<label class="btn btn-success" ng-click="form.$valid ? subproductoPropiedad.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
			<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="subproductoPropiedad.cancelar()" uib-tooltip="Ir a Tabla">
		<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
      </div>
    </div>
  </div>

</div>