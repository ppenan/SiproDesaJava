<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="metavalorController as metavc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="17010">
			<p ng-init="metavc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Valores de Meta</h3></div>
		</div>
		<div class="subtitulo">
			{{metavc.nombreMeta}}
		</div>
				
		<div class="row" align="center" ng-hide="metavc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="17040">
			       		<label class="btn btn-primary" ng-click="metavc.nuevoValor()" uib-tooltip="Nuevo">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="17020">
			       		<label class="btn btn-primary" ng-click="metavc.editar()" uib-tooltip="Editar">
						<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			       </shiro:hasPermission>	
			       <shiro:hasPermission name="17030">
			       		<label class="btn btn-danger" ng-click="metavc.borrar()">
			       		<span class="glyphicon glyphicon-trash"></span>Borrar
			       		</label>
			       </shiro:hasPermission>  		        
			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="17010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="metavc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				
				<div id="maingrid" ui-grid="metavc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!metavc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">
				  Total de {{  metavc.totalMetaValores + (metavc.totalMetaValores == 1 ? " valor de meta" : " valores de metas" ) }}
				</div>
				<ul uib-pagination total-items="metavc.totalMetaValores" 
						ng-model="metavc.paginaActual" 
						max-size="metavc.numeroMaximoPaginas" 
						items-per-page="metavc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="metavc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row second-main-form" ng-show="metavc.mostraringreso">
			<div class="page-header">
			    <h2 ng-hide="!metavc.nuevo"><small>Nuevo Valor</small></h2>
			    <h2 ng-hide="metavc.nuevo"><small>Editar Valor</small></h2>
			</div>
		
		<div class="operation_buttons">
				<div class="btn-group"  style="float: right;">
					<shiro:hasPermission name="17020">
			        	<label class="btn btn-success" ng-click="form.$valid ? metavc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="metavc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label id_class">Meta ID {{ metavc.metavalorId  }}</label>
							<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText"  ng-model="metavc.metavalor.datotiponombre" 
    						ng-value = "metavc.datoTipoNombre"
    						id = "nombre" ng-disabled="true">
    						<label  class="floating-label">Tipo de Dato</label>
						</div>
						<div class="form-group">
							<input type="text" id="fecha" class="inputText" uib-datepicker-popup="{{metavc.formatofecha}}" alt-input-formats="{{metavc.altformatofecha}}"
												ng-model="metavc.metavalor.fecha" is-open="metavc.fc_abierto"
												datepicker-options="metavc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metavc.abrirPopupFecha(1000)"
												ng-value="metavc.metavalor.fecha" onblur="this.setAttribute('value', this.value);"  ng-required="true" ng-readonly="true"/>
												<span class="label-icon" ng-click="metavc.abrirPopupFecha(1000)">
													<i class="glyphicon glyphicon-calendar"></i>
												</span>
							<label class="floating-label">* Fecha de Meta</label>
						</div>
						
						<div class="form-group" ng-if="metavc.datoTipoId>0">
							
							<div ng-switch="metavc.datoTipoId" class="form-group">
								<input ng-switch-when="1" type="text"  ng-model="metavc.metavalor.valor" class="inputText" 
										ng-value="metavc.metavalor.valor" onblur="this.setAttribute('value', this.value);"/>	
								<input ng-switch-when="2" type="number"  numbers-Only ng-model="metavc.metavalor.valor" class="inputText" 
										ng-value="metavc.metavalor.valor" onblur="this.setAttribute('value', this.value);"/>	
								<input ng-switch-when="3" type="number"   ng-model="metavc.metavalor.valor" class="inputText" 
										ng-value="metavc.metavalor.valor" onblur="this.setAttribute('value', this.value);"/>
								<input ng-switch-when="4" type="checkbox"  ng-model="hitoc.hitoresultado"/>
								
								<input ng-switch-when="5" type="text"  class="inputText" uib-datepicker-popup="{{metavc.formatofecha}}" alt-input-formats="{{metavc.altformatofecha}}"
										ng-model="metavc.metavalor.valor" is-open="metavc.popupfecharesultado.abierto"
										datepicker-options="metavc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" 
										ng-value="metavc.metavalor.valor" onblur="this.setAttribute('value', this.value);"
										ng-click="metavc.abirpopupreultado()" ng-readonly="true"/>
								<span ng-switch="5" class="label-icon" ng-click="metavc.abirpopupreultado()">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								
								<label  class="floating-label" >* Valor</label>
							</div>
							
						</div>
						
						
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form">Usuario que creo</label> 
									<p class=""> {{ metavc.metavalor.usuario }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label class="label-form">Fecha de creación</label>
									<p > {{ metavc.metavalor.fechaIngreso }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
					
				</form>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
				
    		</div>
    		<div class="operation_buttons" align="right">
					<div class="btn-group">
						<shiro:hasPermission name="17020">
				        	<label class="btn btn-success" ng-click="form.$valid ? metavc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				        </shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="metavc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
		</div>
	</div>
