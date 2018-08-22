package servlets;

import static org.junit.Assert.*;

import org.junit.Test;
import utils.ClienteHttp;
import utils.DecodificadorJson;
public class SLoginTest {

	String direccionServlet = "http://localhost:8080/SLogin";
	@Test
	public void loginTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"username\":\"admin\", \"password\":\"etc\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
}
