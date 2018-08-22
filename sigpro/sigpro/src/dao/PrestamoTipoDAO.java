package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Prestamo;
import pojo.PrestamoTipo;
import pojo.PrestamoTipoPrestamo;
import pojo.PrestamoTipoPrestamoId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PrestamoTipoDAO {
	public static List<PrestamoTipo> getPrestamoTipos(){
		List<PrestamoTipo> ret = new ArrayList<PrestamoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<PrestamoTipo> criteria = builder.createQuery(PrestamoTipo.class);
			Root<PrestamoTipo> root = criteria.from(PrestamoTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", PrestamoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PrestamoTipo getPrestamoTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		PrestamoTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<PrestamoTipo> criteria = builder.createQuery(PrestamoTipo.class);
			Root<PrestamoTipo> root = criteria.from(PrestamoTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", PrestamoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarPrestamoTipo(PrestamoTipo prestamotipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(prestamotipo);
			session.flush();
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", PrestamoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<PrestamoTipo> getPrestamosTipoPagina(int pagina, int numeroproyectotipos,
			String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String excluir){
		List<PrestamoTipo> ret = new ArrayList<PrestamoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT p FROM PrestamoTipo p WHERE estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = String.join(" ", query, (excluir!=null && excluir.length()>0 ? "and p.id not in (" + excluir + ")" : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<PrestamoTipo> criteria = session.createQuery(query,PrestamoTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyectotipos)));
			criteria.setMaxResults(numeroproyectotipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", PrestamoTipo.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalPrestamosTipos(String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM PrestamoTipo p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));			
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("5", PrestamoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarPrestamoTipo(PrestamoTipo prestamoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			prestamoTipo.setEstado(0);
			session.beginTransaction();
			session.update(prestamoTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", PrestamoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean desasignarTiposAPrestamo(Integer prestamoId){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query<?> criteria = session.createQuery("delete PrestamoTipoPrestamo ptp where ptp.prestamo.id=:prestamoId");
			criteria.setParameter("prestamoId", prestamoId);
			criteria.executeUpdate();
			session.getTransaction().commit();
			ret = true;
		}catch(Exception e){
			CLogger.write("7", PrestamoTipoDAO.class, e);
		}finally {
			session.close();
		}	
		return ret;
	}
	
	public static boolean asignarTiposAPrestamo(ArrayList<Integer> tipos, Prestamo prestamo, String usuario){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			for(int i=0; i<tipos.size();i++){
				PrestamoTipo prestamoTipo = getPrestamoTipoPorId(tipos.get(i));
				PrestamoTipoPrestamoId prestamotipoid = new PrestamoTipoPrestamoId(prestamo.getId(), tipos.get(i));;
				PrestamoTipoPrestamo prestamotipoprestamo = new PrestamoTipoPrestamo();
				prestamotipoprestamo.setId(prestamotipoid);
				prestamotipoprestamo.setPrestamo(prestamo);
				prestamotipoprestamo.setPrestamoTipo(prestamoTipo);
				prestamotipoprestamo.setFechaCreacion(new Date());
				prestamotipoprestamo.setEstado(1);
				prestamotipoprestamo.setUsuarioCreo(usuario);
				session.save(prestamotipoprestamo);
				if( i % 20 == 0 ){
					session.flush();
		            session.clear();
		        }
			}
			session.getTransaction().commit();
			ret = true;
		}catch(Exception e){
			ret = false;
			CLogger.write("8", PrestamoTipoDAO.class, e);
		}finally{
			session.close();
		}	
		return ret;
	}
	
	public static List <PrestamoTipoPrestamo> getPrestamoTiposPrestamo(Integer prestamoId){
		List <PrestamoTipoPrestamo> ret = new ArrayList <PrestamoTipoPrestamo> ();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<PrestamoTipoPrestamo> criteria = session.createQuery("FROM PrestamoTipoPrestamo where prestamoId=:prestamoId", PrestamoTipoPrestamo.class);
			criteria.setParameter("prestamoId", prestamoId);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("9", PrestamoTipoDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
}
