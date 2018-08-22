<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Componente</h3>
 </div>
<div class="modal-body" id="modal-body">
	<div class="row">
	<div class="col-sm-12">
		<form name="form">
				
			<div class="form-group">
				<label for="id" class="floating-label">ID {{ componentec.componente.id }}</label>
				<br/><br/>
			</div>
				
			<div class="form-group">
			   <input type="text" class="inputText" 
			     ng-model="componentec.componente.nombre" ng-value="componentec.componente.nombre"   
			     onblur="this.setAttribute('value', this.value);" ng-required="true" >
			   <label class="floating-label">* Nombre</label>
			</div>
			
			<div class="form-group">
	            <input type="text" class="inputText"  ng-model="componentec.unidadejecutoranombre" ng-readonly="true" ng-required="true" 
	            	ng-click="componentec.buscarUnidadEjecutora()" ng-value="componentec.unidadejecutoranombre" onblur="this.setAttribute('value', this.value);"/>
	            <span class="label-icon" ng-click="componentec.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
	          	<label for="campo3" class="floating-label">* Unidad Ejecutora</label>
			</div>
			<div class="form-group">
	            <input type="text" class="inputText"  ng-model="componentec.componentetiponombre" ng-readonly="true" ng-required="true" 
	            	ng-click="componentec.buscarComponenteTipo()" ng-value="componentec.componentetiponombre" onblur="this.setAttribute('value', this.value);"/>
	            <span class="label-icon" ng-click="componentec.buscarComponenteTipo()"><i class="glyphicon glyphicon-search"></i></span>
	          	<label for="campo3" class="floating-label">* Tipo de Componente</label>
			</div>
			<div class = "row">
				<div class="col-sm-6">
					<div class="form-group">
						<select class="inputText" ng-model="componentec.duracionDimension"
							ng-options="dim as dim.nombre for dim in componentec.dimensiones track by dim.value"
							 ng-required="true">
						</select>
						<label for="nombre" class="floating-label">* Dimension</label>
					</div>
				</div>
				
				<div class="col-sm-6">
					<div class="form-group">
					   <input class="inputText"  type="number"
					     ng-model="componentec.duracion" ng-value="componentec.duracion"   
					     onblur="this.setAttribute('value', this.value);"  min="1" max="500" ng-required="true" 
					     ng-readonly="componentec.duracionDimension.value != 0 ? false : true"
					     ng-change="componentec.fechaInicio != null && componentec.duracionDimension.value != 0 ? componentec.cambioDuracion(componentec.duracionDimension) : ''">
					   <label class="floating-label">* Duración</label>
					</div>	
				</div>
				
				<div class="col-sm-6">
					<div class="form-group" >
					  <input type="text"  class="inputText" uib-datepicker-popup="{{componentec.formatofecha}}" alt-input-formats="{{componentec.altformatofecha}}"
							  	ng-model="componentec.fechaInicio" is-open="componentec.fi_abierto"
					            datepicker-options="componentec.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="componentec.cambioDuracion(componentec.duracionDimension);" ng-required="true"  
					            ng-click="componentec.abrirPopupFecha(1000)" ng-value="componentec.fechaInicio" onblur="this.setAttribute('value', this.value);">
					            <span class="label-icon" ng-click="componentec.abrirPopupFecha(1000)">
					              <i class="glyphicon glyphicon-calendar"></i>
					            </span>
					  <label for="campo.id" class="floating-label">* Fecha de Inicio</label>
					</div>
				</div>
			
				<div class="col-sm-6">
					<div class="form-group" >
					  <input type="text"  class="inputText" uib-datepicker-popup="{{componentec.formatofecha}}" alt-input-formats="{{componentec.altformatofecha}}"
							  	ng-model="componentec.fechaFin" is-open="componentec.ff_abierto"
					            datepicker-options="componentec.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true" ng-click=""
					            ng-value="componentec.fechaFin" onblur="this.setAttribute('value', this.value);"
					            ng-readonly="true"/>
					            <span class="label-icon" ng-click="componentec.abrirPopupFecha(1001)">
					              <i class="glyphicon glyphicon-calendar"></i>
					            </span>
					  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
					</div>
				</div>
			</div>
			
		</form>
		<br/>
		<div class="row">
		    <div class="col-sm-12 operation_buttons" align="right">
			    <div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? componentec.ok() : ''" ng-disabled="!form.$valid"> Guardar</label>
					<label class="btn btn-primary" ng-click="componentec.cancel()">Cancelar</label>
					<label class="btn btn-danger" ng-click="componentec.borrar()" ng-disabled="componentec.esnuevo">Borrar</label>
		    	</div>
		      
		    </div>
  		</div>
	</div>
	</div>
</div>