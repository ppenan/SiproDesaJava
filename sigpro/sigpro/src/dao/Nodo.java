package dao;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class Nodo {
	@Expose
	public int id;
	@Expose
	public int objeto_tipo;
	@Expose
	public String nombre;
	@Expose
	public int nivel;
	@Expose
	public boolean estado;
	@Expose
	public ArrayList<Nodo> children;
	@Expose(serialize = false)
	public Nodo parent;
	@Expose(serialize = false)
	public Timestamp fecha_inicio;
	@Expose(serialize = false)
	public Timestamp fecha_fin;
	@Expose(serialize = false)
	public int duracion;
	@Expose(serialize = false)
	public Double costo;
	@Expose(serialize = false)
	public Object objeto;
	@Expose(serialize = false)
	public Timestamp fecha_inicio_real;
	@Expose(serialize = false)
	public Timestamp fecha_fin_real;
	
	public Nodo(int id, int objeto_tipo, String nombre, int nivel, ArrayList<Nodo> children, Nodo parent, boolean estado) {
		super();
		this.id = id;
		this.objeto_tipo = objeto_tipo;
		this.nombre = nombre;
		this.nivel = nivel;
		this.children = children;
		this.parent = parent;
		this.estado = estado;
	}
	
	public Nodo(int id, int objeto_tipo, String nombre, int nivel, ArrayList<Nodo> children, Nodo parent, boolean estado, Timestamp fecha_inicio,
			Timestamp fecha_fin, Double costo, int duracion, Timestamp fecha_inicio_real, Timestamp fecha_fin_real) {
		super();
		this.id = id;
		this.objeto_tipo = objeto_tipo;
		this.nombre = nombre;
		this.nivel = nivel;
		this.children = children;
		this.parent = parent;
		this.estado = estado;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.costo = costo;
		this.duracion = duracion;
		this.fecha_inicio_real = fecha_inicio_real;
		this.fecha_fin_real = fecha_fin_real;
	}
}
