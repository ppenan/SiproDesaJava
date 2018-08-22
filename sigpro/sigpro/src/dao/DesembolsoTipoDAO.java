package dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.DesembolsoTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class DesembolsoTipoDAO {
	
	public static Long getTotalDesembolsoTipo(String filtro_nombre){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(d.id) FROM DesembolsoTipo d WHERE d.estado=1";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query = String.join("",query, " AND d.nombre LIKE '%",filtro_nombre,"%'");
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}catch(Throwable e){
			CLogger.write("1", DesembolsoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static DesembolsoTipo getDesembolosTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		DesembolsoTipo ret = null;
		List<DesembolsoTipo> listRet = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<DesembolsoTipo> criteria = builder.createQuery(DesembolsoTipo.class);
			Root<DesembolsoTipo> root = criteria.from(DesembolsoTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			listRet = session.createQuery( criteria ).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("2", DesembolsoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarDesembolsoTipo(DesembolsoTipo desembolsoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(desembolsoTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", DesembolsoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarDesembolsoTipo(DesembolsoTipo desembolsoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			desembolsoTipo.setEstado(0);
			session.beginTransaction();
			session.update(desembolsoTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", DesembolsoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<DesembolsoTipo> geDesembolsoTiposPagina(int pagina, int numeroDesembolsoTipos, String filtro_nombre, String columna_ordenada, String orden_direccion){
		List<DesembolsoTipo> ret = new ArrayList<DesembolsoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT d FROM DesembolsoTipo d WHERE estado = 1 ";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query = String.join("",query, " AND d.nombre LIKE '%",filtro_nombre,"%'");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query," ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<DesembolsoTipo> criteria = session.createQuery(query,DesembolsoTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroDesembolsoTipos)));
			criteria.setMaxResults(numeroDesembolsoTipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", DesembolsoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
