package servlets;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.EstructuraProyectoDAO;
import dao.HitoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubComponenteDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Hito;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subcomponente;
import pojo.Subproducto;
import utilities.CLogger;
import utilities.CProject;
import utilities.Utils;


@WebServlet("/SGantt")
public class SGantt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int OBJETO_ID_HITO = -2;
	private static int OBJETO_ID_PROYECTO = 0;
	private static int OBJETO_ID_COMPONENTE = 1;
	private static int OBJETO_ID_SUBCOMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;


    public SGantt() {
        super();

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		String items = "";
		String accion = "";
		Integer proyectoId = null;
		boolean multiproyecto = false;
		Integer programaId = null;
		boolean marcarCargado = false;
		Integer prestamoId = null;
		Map<String, String> map = null;

		HashMap<Integer,List<Integer>> predecesores;
		try {
			List<FileItem> parametros = null;
			if (request.getContentType() != null && request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 ) {


				parametros = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem parametro : parametros)
			    {
					if (parametro.isFormField()){

						if (parametro.getFieldName().compareTo("accion")==0 && parametro.getString().length()>0){
							accion = parametro.getString();
						}else if (parametro.getFieldName().compareTo("proyecto_id")==0 && parametro.getString().length()>0){
							proyectoId = Integer.parseInt(parametro.getString());
						}else if (parametro.getFieldName().compareTo("multiproyecto")==0 && parametro.getString().length()>0){
							multiproyecto = parametro.getString().equals("1");
						}else if (parametro.getFieldName().compareTo("programa_id")==0 && parametro.getString().length()>0){
							programaId = Integer.parseInt(parametro.getString());
						}else if (parametro.getFieldName().compareTo("marcarCargado")==0 && parametro.getString().length()>0){
							marcarCargado = parametro.getString().equals("1");
						}else if (parametro.getFieldName().compareTo("prestamoId")==0 && parametro.getString().length()>0){
							prestamoId  = Integer.parseInt(parametro.getString());
						}
					}
					
				}
			}else {
				request.setCharacterEncoding("UTF-8");

				Gson gson = new Gson();
				Type type = new TypeToken<Map<String, String>>(){}.getType();
				StringBuilder sb = new StringBuilder();
				BufferedReader br = request.getReader();
				String str;
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				;
				map = gson.fromJson(sb.toString(), type);
				accion = map.get("accion");
				proyectoId = Utils.String2Int(map.get("proyecto_id"));
			}

		if(accion.equals("getProyecto")){
			predecesores = new HashMap<>();
			
			items = cargarProyecto(proyectoId,usuario,predecesores);
			

			String estructruaPredecesores = getEstructuraPredecesores(predecesores);
			items = String.join("","{\"items\" : [", items,"]"
					,estructruaPredecesores!=null && estructruaPredecesores.length()>0 ? "," : ""
					,estructruaPredecesores,"}");

			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");


	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(items.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}else if(accion.equals("getPrograma")){
			predecesores = new HashMap<>();
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			items = "";

			List<Proyecto> proyectos = ProyectoDAO.getProyectosPorPrograma(programaId);
			for (Proyecto proyecto : proyectos){
				items = String.join(items.length()> 0 ? "," : "", items, getProyecto(proyecto.getId(),usuario,predecesores));
			}

			String estructruaPredecesores = getEstructuraPredecesores(predecesores);

			items = String.join("","{\"items\" : [", items,"]"
					,estructruaPredecesores!=null && estructruaPredecesores.length()>0 ? "," : ""
					,estructruaPredecesores,"}");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(items.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}else if(accion.equals("importar")){

				String directorioTemporal = "/SIPRO/archivos/temporales";
				ArrayList<FileItem> fileItems=new ArrayList<FileItem>();

				Long time=  0L;
				File file = null;

				for (FileItem parametro : parametros)
			    {
					if (!parametro.isFormField()){
						fileItems.add(parametro);

						File directorio = new File(directorioTemporal);
						if (directorio.exists()){
							directorio.mkdirs();
							time = new Date().getTime();
							file = new File(directorioTemporal,"temp_" + time.toString());
							while (file.exists()){
								time = new Date().getTime();
								file = new File(directorioTemporal,"temp_" + time.toString());
							}
							parametro.write(file);
						}
					}
			    }

				CProject project = new CProject(directorioTemporal + "/temp_"+ time.toString());

				boolean creado = project.imporatarArchivo(project.getProject(),usuario,multiproyecto,proyectoId,marcarCargado,prestamoId);

				if (file.exists())
				{
					file.delete();
				}
				if (creado){
				    items = "{ \"success\": true }";
				}else{
					items = "{ \"success\": false }";
				}

				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");


		        OutputStream output = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(output);
		        gz.write(items.getBytes("UTF-8"));
		        gz.close();
		        output.close();


		}
		else if(accion.equals("exportar")){
			try{
				CProject project = new CProject("");
				//TODO: lineaBase
				String path = project.exportarProject(proyectoId, null, usuario);

				File xml=new File(path);
				if(xml.exists()){
				ServletOutputStream	stream = response.getOutputStream();
				     
				      response.setContentType("text/xml");
				      
				      response.addHeader(
				        "Content-Disposition","attachment;" );

				      response.setContentLength( (int) xml.length() );
				      
				     FileInputStream input = new FileInputStream(xml);
				     BufferedInputStream buf = new BufferedInputStream(input);
				     int readBytes = 0;

				     //read from the file; write to the ServletOutputStream
				     while((readBytes = buf.read()) != -1)
				        stream.write(readBytes);

				     if(stream != null)
				         stream.close();
				      if(buf != null)
				          buf.close();
				          }
				}

			catch(Exception e){
				e.printStackTrace();
			}
		}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String construirItem(Integer idItem,Integer objetoId, Integer objetoTipo, String content,Integer identation,
			Boolean isExpanded,Date start,Date finish, Date actualStart, Date actualFinish, boolean isMilestone,Integer duracion,BigDecimal costo,
			BigDecimal metaPlanificada, BigDecimal metaReal){

		String f_inicio = Utils.formatDate(start);
		String f_fin = Utils.formatDate(finish);
		String f_actualInicio = Utils.formatDate(actualStart);
		String f_actualFin = Utils.formatDate(actualFinish);

		content = content.replace("\"", "");
		String cadena = String.join("", "{\"id\" :",idItem!=null ? idItem.toString() : "0",","
				,"\"content\" :\"",content,"\","
				,"\"objetoId\" :\"",objetoId.toString(),"\"," ,"\"objetoTipo\" :\"",objetoTipo.toString(),"\",",
				identation!=null ? "\"indentation\" :" : "", identation!=null ? identation.toString() :"",
				identation!=null ? "," : "",
				isExpanded!=null ? "\"isExpanded\" :\"":"" ,isExpanded!=null ? (isExpanded ? "true" : "false"):"",
				isExpanded!=null ?"\",":"",
				start !=null ? "\"start\" :\"" : "", start!=null ? f_inicio + " 00:00:00"  :"", start!=null ? "\"" : "",
			    start!=null ? "," : "",
				finish!=null ? "\"finish\" :\"" : "",finish!=null ? f_fin + " 00:00:00" : "",finish!=null ?"\"":"",
				finish!=null ?	"," : "",
				actualStart !=null ? "\"actualStart\" :\"" : "", actualStart!=null ? f_actualInicio + " 00:00:00"  :"", actualStart!=null ? "\"" : "",
					    actualStart!=null ? "," : "",
	    		actualFinish!=null ? "\"actualFinish\" :\"" : "",actualFinish!=null ? f_actualFin + " 00:00:00" : "", actualFinish!=null ?"\"":"",
	    				actualFinish!=null ?	"," : "",
				duracion!=null ? "\"duracion\" :\"" : "",duracion!=null ? duracion.toString() : "",
				duracion!=null ?"\",":"",
				"\"isMilestone\":",isMilestone ? "\"true\"" : "\"false\"",
			    costo!=null ? ",\"costo\" :" : "",costo!=null ? costo.toString() : "",
			    metaPlanificada!=null ? ",\"metaPlanificada\" :" : "",metaPlanificada!=null ?
			    		metaPlanificada.floatValue() + "" : "",
				metaReal!=null ? ",\"metaReal\" :" : "",metaReal!=null ?
						metaReal.floatValue() + "" : "",metaReal!=null ?"":"",

				"}"
			);
		
		return cadena.replaceAll(",,", ",");
	}

	private String obtenerItemsActividades(int objetoId,int objetoTipo,int nivelObjeto,HashMap<Integer,List<Integer>> predecesores){
		String ret = "";

		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, objetoId, objetoTipo
				, null, null, null, null, null, null);
		if (!actividades.isEmpty()){
			for (Actividad actividad : actividades){

				String retRec = obtenerItemsActividadesRecursivas(actividad.getId(), OBJETO_ID_ACTIVIDAD, nivelObjeto + 1, predecesores);

				List<Integer> idPredecesores = new ArrayList<>();
				if (actividad.getPredObjetoId()!=null && retRec!=null && retRec.isEmpty()){
					idPredecesores.add(actividad.getPredObjetoId());
					predecesores.put(actividad.getId(), idPredecesores);
				}

				ret = String.join(ret.trim().length()>0 ? "," : "",ret,
						construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(),
								nivelObjeto, true, actividad.getFechaInicio(), actividad.getFechaFin(), actividad.getFechaInicioReal(), actividad.getFechaFinReal(),
								false,actividad.getDuracion(),actividad.getCosto(),null,null));


				if (retRec!=null && retRec.length()>0){
					ret = String.join(",", ret,retRec);
				}
			}
		}
		return ret;
	}

	private String obtenerItemsActividadesRecursivas(int objetoId,int objetoTipo,Integer nivelObjeto,HashMap<Integer,List<Integer>> predecesores){
		String ret = "";

		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, objetoId, objetoTipo
				, null, null, null, null, null, null);
		if (!actividades.isEmpty()){
			for (Actividad actividad : actividades){

				List<Integer> idPredecesores = new ArrayList<>();
				if (actividad.getPredObjetoId()!=null){
					idPredecesores.add(actividad.getPredObjetoId());
					predecesores.put(actividad.getId(), idPredecesores);
				}
				ret = String.join(ret.trim().length()>0 ? "," : "",ret,
						construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(),
								nivelObjeto, true, actividad.getFechaInicio(), actividad.getFechaFin(),actividad.getFechaInicioReal(), actividad.getFechaFinReal(), false,
								actividad.getDuracion(),actividad.getCosto(),null,null));

				String retRec = obtenerItemsActividadesRecursivas(actividad.getId(), OBJETO_ID_ACTIVIDAD, nivelObjeto + 1, predecesores);

				if (retRec!=null && retRec.length()>0){
					ret = String.join(",", ret,retRec);
				}
			}
		}
		return ret;
	}

	private String getEstructuraPredecesores(HashMap<Integer,List<Integer>> predecesores){
		String ret = "";
		for (Entry<Integer, List<Integer>> predecesor : predecesores.entrySet()) {
			String itemPred = "";
			if (predecesor.getValue()!=null && predecesor.getValue().size()>0){
				 itemPred = String.join("", "{\"id\":",predecesor.getKey().toString(),",",
						"\"idPredecesor\":", predecesor.getValue().get(0).toString(),"}");
			}
		    ret = itemPred.length()> 0 ?  String.join(ret.length()>0 ? ",": "", ret,itemPred) : ret;
		}


		if (ret.length()>0){
			ret = String.join("","\"predecesores\": [",ret,"]");
		}
		return ret;
	}

	private String getProyecto(Integer proyectoId,String usuario, HashMap<Integer,List<Integer>> predecesores){
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		String items_actividad="";
		String items_subproducto="";
		String items_producto="";
		String items_componente="";
		String items_subcomponente="";
		String items="";

		//predecesores = new HashMap<>();
		if (proyecto !=null){
			Date fechaPrimeraActividad = null;
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyecto.getId(),
					null, null, null, null, null, usuario);
			items_componente="";
			for (Componente componente :componentes){
				
				List<Subcomponente> subcomponentes = SubComponenteDAO.getSubComponentesPaginaPorComponente(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				items_subcomponente="";
				for(Subcomponente subcomponente : subcomponentes){
					List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, null, subcomponente.getId(),
							null, null, null, null, null, usuario);
					items_producto="";
					for (Producto producto : productos){
						List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(), null, null, null, null, null, usuario);

						items_subproducto="";
						for (Subproducto subproducto : subproductos){

							List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
									null,null, null, null, null, usuario);
							items_actividad="";
							if (!actividades.isEmpty()){

								for (Actividad actividad : actividades){
									if (fechaPrimeraActividad==null) {
										fechaPrimeraActividad = actividad.getFechaInicio();
									}
									List<Integer> idPredecesores = new ArrayList<>();
									if (actividad.getPredObjetoId()!=null){
										idPredecesores.add(actividad.getPredObjetoId());
										predecesores.put(actividad.getId(), idPredecesores);
									}

									items_actividad = String.join(items_actividad.trim().length()>0 ? "," : "",items_actividad,
											construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(), OBJETO_ID_ACTIVIDAD,
													true, actividad.getFechaInicio(), actividad.getFechaFin(), actividad.getFechaInicioReal(), actividad.getFechaFinReal(),
													false,actividad.getDuracion(),actividad.getCosto(),null,null));

									String items_actividad_recursiva = obtenerItemsActividades(actividad.getId(),OBJETO_ID_ACTIVIDAD,OBJETO_ID_ACTIVIDAD,predecesores);

									items_actividad = String.join(items_actividad_recursiva !=null && items_actividad_recursiva.trim().length()>0 ? "," : "", items_actividad,items_actividad_recursiva!=null && items_actividad_recursiva.length() > 0 ?
											items_actividad_recursiva : "");

								}
							}
							items_subproducto = String.join(items_subproducto.trim().length()>0 ? ",":"", items_subproducto,
									construirItem(subproducto.getId(),subproducto.getId(),OBJETO_ID_SUBPRODUCTO, subproducto.getNombre(),OBJETO_ID_SUBPRODUCTO, true,
											fechaPrimeraActividad, null,subproducto.getFechaInicioReal(), subproducto.getFechaFinReal(), false,subproducto.getDuracion(),subproducto.getCosto(),null,null));
							items_subproducto = items_actividad.trim().length() > 0 ? String.join(",", items_subproducto,items_actividad) : items_subproducto;
						}
//						BigDecimal metaPlanificada = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(2, producto.getId(), OBJETO_ID_PRODUCTO);
//						BigDecimal metaReal = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(1, producto.getId(), OBJETO_ID_PRODUCTO);

						items_producto = String.join(items_producto.trim().length()>0 ? "," : "",items_producto,
								construirItem(producto.getId(),producto.getId(),OBJETO_ID_PRODUCTO, producto.getNombre(),OBJETO_ID_PRODUCTO, true, fechaPrimeraActividad,
										null, producto.getFechaInicioReal(), producto.getFechaFinReal(),false,producto.getDuracion(),producto.getCosto(),null,null));
						items_producto = items_subproducto.trim().length() > 0 ? String.join(",",items_producto, items_subproducto) : items_producto;

						items_actividad = obtenerItemsActividades(producto.getId(),OBJETO_ID_PRODUCTO,OBJETO_ID_PRODUCTO,predecesores);
						items_producto = (items_actividad.length()>0 ? String.join(",", items_producto,items_actividad):items_producto);

					}
					items_subcomponente = String.join(items_subcomponente.trim().length()>0 ? "," : "",items_subcomponente,
							construirItem(subcomponente.getId(),subcomponente.getId(),OBJETO_ID_SUBCOMPONENTE,subcomponente.getNombre(),OBJETO_ID_SUBCOMPONENTE, true, fechaPrimeraActividad,
									null,subcomponente.getFechaInicioReal(), subcomponente.getFechaFinReal(), false,subcomponente.getDuracion(),subcomponente.getCosto(),null,null));
					items_subcomponente = items_producto.trim().length() > 0 ? String.join(",", items_subcomponente,items_producto) : items_subcomponente;

					items_actividad = obtenerItemsActividades(subcomponente.getId(),OBJETO_ID_SUBCOMPONENTE,OBJETO_ID_SUBCOMPONENTE,predecesores);
					items_subcomponente = (items_actividad.length()>0 ? String.join(",", items_subcomponente,items_actividad):items_subcomponente);
				}
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(), null,
						null, null, null, null, null, usuario);
				items_producto="";
				for (Producto producto : productos){
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(), null, null, null, null, null, usuario);

					items_subproducto="";
					for (Subproducto subproducto : subproductos){

						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
								null,null, null, null, null, usuario);
						items_actividad="";
						if (!actividades.isEmpty()){

							for (Actividad actividad : actividades){
								if (fechaPrimeraActividad==null) {
									fechaPrimeraActividad = actividad.getFechaInicio();
								}
								List<Integer> idPredecesores = new ArrayList<>();
								if (actividad.getPredObjetoId()!=null){
									idPredecesores.add(actividad.getPredObjetoId());
									predecesores.put(actividad.getId(), idPredecesores);
								}

								items_actividad = String.join(items_actividad.trim().length()>0 ? "," : "",items_actividad,
										construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(), OBJETO_ID_ACTIVIDAD-1,
												true, actividad.getFechaInicio(), actividad.getFechaFin(), actividad.getFechaInicioReal(), actividad.getFechaFinReal(),
												false,actividad.getDuracion(),actividad.getCosto(),null,null));

								String items_actividad_recursiva = obtenerItemsActividades(actividad.getId(),OBJETO_ID_ACTIVIDAD,OBJETO_ID_ACTIVIDAD-1,predecesores);

								items_actividad = String.join(items_actividad_recursiva !=null && items_actividad_recursiva.trim().length()>0 ? "," : "", items_actividad,items_actividad_recursiva!=null && items_actividad_recursiva.length() > 0 ?
										items_actividad_recursiva : "");

							}
						}
						items_subproducto = String.join(items_subproducto.trim().length()>0 ? ",":"", items_subproducto,
								construirItem(subproducto.getId(),subproducto.getId(),OBJETO_ID_SUBPRODUCTO, subproducto.getNombre(),OBJETO_ID_SUBPRODUCTO-1, true,
										fechaPrimeraActividad, null,subproducto.getFechaInicioReal(), subproducto.getFechaFinReal(), false,subproducto.getDuracion(),subproducto.getCosto(),null,null));
						items_subproducto = items_actividad.trim().length() > 0 ? String.join(",", items_subproducto,items_actividad) : items_subproducto;
					}
//					BigDecimal metaPlanificada = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(2, producto.getId(), OBJETO_ID_PRODUCTO);
//					BigDecimal metaReal = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(1, producto.getId(), OBJETO_ID_PRODUCTO);

					items_producto = String.join(items_producto.trim().length()>0 ? "," : "",items_producto,
							construirItem(producto.getId(),producto.getId(),OBJETO_ID_PRODUCTO, producto.getNombre(),OBJETO_ID_PRODUCTO-1, true, fechaPrimeraActividad,
									null,producto.getFechaInicioReal(), producto.getFechaFinReal(),false,producto.getDuracion(),producto.getCosto(),null,null));
					items_producto = items_subproducto.trim().length() > 0 ? String.join(",",items_producto, items_subproducto) : items_producto;

					items_actividad = obtenerItemsActividades(producto.getId(),OBJETO_ID_PRODUCTO,OBJETO_ID_PRODUCTO-1,predecesores);
					items_producto = (items_actividad.length()>0 ? String.join(",", items_producto,items_actividad):items_producto);

				}
				items_componente = String.join(items_componente.trim().length()>0 ? "," : "",items_componente,
						construirItem(componente.getId(),componente.getId(),OBJETO_ID_COMPONENTE,componente.getNombre(),OBJETO_ID_COMPONENTE, true, fechaPrimeraActividad,
								null,componente.getFechaInicioReal(), componente.getFechaFinReal(), false,componente.getDuracion(),componente.getCosto(),null,null));
				items_componente = items_producto.trim().length() > 0 ? String.join(",", items_componente,items_producto) : items_componente;

				items_actividad = obtenerItemsActividades(componente.getId(),OBJETO_ID_COMPONENTE,OBJETO_ID_COMPONENTE,predecesores);
				items_componente = (items_actividad.length()>0 ? String.join(",", items_componente,items_actividad):items_componente);
			}


			items = String.join(",",construirItem(proyecto.getId(),proyecto.getId(),OBJETO_ID_PROYECTO,proyecto.getNombre()
					,null, true, fechaPrimeraActividad, null,proyecto.getFechaInicioReal(), proyecto.getFechaFinReal(),false,proyecto.getDuracion(),proyecto.getCosto(),null,null),items_componente);
			List<Hito> hitos = HitoDAO.getHitosPaginaPorProyecto(0, 0, proyectoId, null, null, null, null, null);

			for (Hito hito:hitos){
				items = String.join(",",items,
						construirItem(hito.getId(),hito.getId(),OBJETO_ID_HITO, hito.getNombre(), 1, null, hito.getFecha(),
								null,null, null, true,null,null,null,null));
			}
		}
		//String estructruaPredecesores = getEstructuraPredecesores(predecesores);



		return items;

	}


	
	private String cargarProyecto(Integer proyectoId,String usuario, HashMap<Integer,List<Integer>> predecesores){
		String items = "";
		try{
			//TODO: lineaBase
			List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(proyectoId, null);
			
			for(Object objeto : estructuraProyecto){
				String item="";
				Object[] obj = (Object[]) objeto;
				Integer tipoObjeto =((BigInteger)obj[2]).intValue();
				
				switch(tipoObjeto){
					case 0:
						item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_PROYECTO, (String)obj[1], 0, 
								true, (Date)obj[5], null, (Date)obj[16], (Date)obj[17],false,(Integer) obj[6], (BigDecimal) obj[8], null, null);
						
						break;
					case 1:
						item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_COMPONENTE, (String)obj[1], 1, 
								true, null, null, (Date)obj[16], (Date)obj[17],false,(Integer) obj[6], (BigDecimal) obj[8], null, null);
						break;
						
					case 2:
						item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_SUBCOMPONENTE, (String)obj[1], 2, 
								true, null, null, (Date)obj[16], (Date)obj[17],false,(Integer) obj[6], (BigDecimal) obj[8], null, null);
						break;
					case 3:
	//					BigDecimal metaPlanificada = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(2, (Integer)obj[0], OBJETO_ID_PRODUCTO);
	//					BigDecimal metaReal = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(1, (Integer)obj[0], OBJETO_ID_PRODUCTO);
						
						item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_PRODUCTO, (String)obj[1], (obj[3]!=null) ?  (((String)obj[3]).length()/8 - 1 ) : 0,  
								false, null, null, (Date)obj[16], (Date)obj[17],false,(Integer) obj[6], (BigDecimal) obj[8], null,null);
						break;
					case 4:
						item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_SUBPRODUCTO, (String)obj[1], (obj[3]!=null) ?  (((String)obj[3]).length()/8 - 1 ) : 0, 
								false, null, null, (Date)obj[16], (Date)obj[17],false,(Integer) obj[6], (BigDecimal) obj[8], null, null);
						break;
					case 5:
						item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_ACTIVIDAD, (String)obj[1], (obj[3]!=null) ?  (((String)obj[3]).length()/8 - 1 ) : 0, 
								false, (Date)obj[4], (Date)obj[5], (Date)obj[16], (Date)obj[17],false,(Integer) obj[6],(BigDecimal) obj[8], null, null);
						/*if (obj[10]!=null){
							List<Integer> idPredecesores = new ArrayList<>();
							idPredecesores.add(((BigInteger)obj[10]).intValue());
							predecesores.put((Integer)obj[0], idPredecesores);
						}*/
						break;
				}
				
				items = String.join(items.length() > 0 ? "," : "", items,item);
			}
		}catch(Exception e){
			CLogger.write("6", SGantt.class, e);
		}
		return items;
		
	}
	

}
