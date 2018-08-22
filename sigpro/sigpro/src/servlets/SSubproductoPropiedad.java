package servlets;

import java.io.IOException;
import java.util.Map;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.SubproductoPropiedadDAO;
import utilities.Utils;

@WebServlet("/SSubproductoPropiedad")
public class SSubproductoPropiedad extends HttpServlet {
	private static final long serialVersionUID = -6537014370076177564L;
	
	public SSubproductoPropiedad() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;

		Map<String, String> parametro = Utils.getParams(request);

		if (parametro.get("accion").compareTo("cargar") == 0) {
			listar(parametro, response);
		} else if (parametro.get("accion").compareTo("crear") == 0) {
			crear(parametro, response,usuario);
		} else if (parametro.get("accion").compareTo("actualizar") == 0) {
			actualizar(parametro, response,usuario);
		} else if (parametro.get("accion").compareTo("borrar") == 0) {
			eliminar(parametro, response);
		} else if (parametro.get("accion").compareTo("totalElementos") == 0) {
			total(parametro,response);
		} else if (parametro.get("accion").compareTo("getSubproductoPropiedadPorTipo") == 0) {
			listarSubproductoPropiedadPorTipo(parametro,response);
		}
	}

	private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		String columna_ordenada = parametro.get("columna_ordenada");
		String orden_direccion = parametro.get("orden_direccion");

		String resultadoJson = "";

		resultadoJson = SubproductoPropiedadDAO.getJson(pagina, registros,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}

	private void crear(Map<String, String> parametro, HttpServletResponse response,String usuario) throws IOException {
		String nombre = parametro.get("nombre");
		String descripcion = parametro.get("descripcion");
		

		int tipo = Utils.String2Int(parametro.get("tipo"), -1);

		Integer creado = SubproductoPropiedadDAO.guardar(-1, nombre, descripcion, usuario , tipo);
		
		String resultadoJson = String.join("","{ \"success\": ",(creado!=null ? "true" : "false"),", "
				+ "\"id\": " + creado +" }");
		 

		Utils.writeJSon(response, resultadoJson);
	}

	private void actualizar(Map<String, String> parametro, HttpServletResponse response,String usuario) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"), -1);
		String nombre = parametro.get("nombre");
		String descripcion = parametro.get("descripcion");
		int tipo = Utils.String2Int(parametro.get("tipo"), -1);
		boolean actualizado = SubproductoPropiedadDAO.actualizar(codigo, nombre, descripcion, usuario, tipo);
		if (actualizado) {
			listar(parametro, response);
		}
	}

	private void eliminar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"), -1);
		String usuario = parametro.get("usuario");

		boolean eliminado = SubproductoPropiedadDAO.eliminar(codigo, usuario);
		if (eliminado) {
			listar(parametro, response);
		}
	}

	private void total(Map<String, String> parametro,HttpServletResponse response) throws IOException {
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		Long total = SubproductoPropiedadDAO.getTotal(filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion);

		String resultadoJson = "{\"success\":true, \"total\":" + total + "}";

		Utils.writeJSon(response, resultadoJson);
	}
	
	private void listarSubproductoPropiedadPorTipo (Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int idSubproducto = Utils.String2Int(parametro.get("idsubproducto"), -1);
		int idSubproductoTio = Utils.String2Int(parametro.get("idsubproductotipo"), -1);
		
		String subproductoPropiedades = SubproductoPropiedadDAO.getJsonPorTipo(idSubproductoTio,idSubproducto);
		
		if (Utils.isNullOrEmpty(subproductoPropiedades)) {
			subproductoPropiedades = "{\"success\":false}";
		}
		Utils.writeJSon(response, subproductoPropiedades);
	}

}
