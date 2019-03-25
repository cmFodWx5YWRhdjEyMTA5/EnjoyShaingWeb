package Hibernate;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import WebProject.DataObject.Parameter;
import WebProject.DataObject.ParameterCollection;

public class HibernateOperation {
	
	public Session session;
	
	public HibernateOperation()
	{
		super();
		session = HibernateUtil.getSessionFactory().openSession();
		//System.out.println("BaseDBOperations.BaseDBOperations sessione istanziata");
	}
	
	public List<?> ExecuteQuery(String query)
	{
		// Con questa funzione eseguo una query senza where
		List<?> retList = null;
		try
		{
			session.beginTransaction();
			retList = session.createQuery(query).list();
			session.flush();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
		return retList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<?> ExecuteQuery(String query,ParameterCollection params)
	{
		// Con questa funzione eseguo una query con delle clausole where customizzate all'interno (es. item like ...)
		List<?> retList = null;
		try
		{
			session.beginTransaction();
			Query q = session.createQuery(query);
			//System.out.println("BaseDBOperations.ExecuteQuery query "+query);
			for(Parameter param : params.InputParameterList)
			{
				//System.out.println("BaseDBOperations.ExecuteQuery parametro "+param.Name+" = "+param.Value+" "+param.Type);
				if(param.Value instanceof List)
				{
					q.setParameterList(param.Name, (Collection) param.Value);
				}
				else
				{
					q.setParameter(param.Name, param.Value);
				}
			}
			retList = q.list();
			session.flush();
		}
		catch(Exception e)
		{
			System.out.println("Error on ExecuteQuery\n");
			e.printStackTrace();
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
		return retList;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public List<?> ExecuteSPQuery(String storedName,ParameterCollection params,Class resultClass)
	{
		List<?> retList = null;
		try
		{
			session.beginTransaction();
			Query q = session.createSQLQuery(CreateSPString(storedName,params));
			//System.out.println("BaseDBOperations.ExecuteQuery query "+query);
			for(Parameter param : params.InputParameterList)
			{
				//System.out.println("BaseDBOperations.ExecuteQuery parametro "+param.Name+" = "+param.Value+" "+param.Type);
				if(param.Value instanceof List)
				{
					q.setParameterList(param.Name, (Collection) param.Value);
				}
				else
				{
					try
					{
						q.setParameter(param.Name, param.Value.toString().equals("")?null:param.GetValue());
					}
					catch(Exception e)
					{
						q.setParameter(param.Name, null);
					}
				}
			}
			retList = q.setResultTransformer(Transformers.aliasToBean(resultClass)).list();
			session.flush();
		}
		catch(Exception e)
		{
			System.out.println("Errore chiamata stored "+storedName);
			e.printStackTrace();
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
		return retList;
	}
	
	protected String CreateSPString(String storedName,ParameterCollection params)
	{
		String ret = "{call "+storedName+"(";
		for(Parameter param : params.InputParameterList)
		{
			ret += ":"+param.Name+",";
		}
		return ret.substring(0, ret.lastIndexOf(','))+")}";
	}
	
	public List<?> GetTableData(String tableName)
	{
		List<?> retList;
		String query = "from "+tableName;
		retList = ExecuteQuery(query);
		return retList;
	}
	
	public List<?> GetTableData(String tableName,ParameterCollection whereParams)
	{
		List<?> retList;
		String query = "from "+tableName;
		if(whereParams==null || whereParams.InputParameterList.isEmpty())
		{
			retList = ExecuteQuery(query);
		}
		else
		{
			query += GenerateTableWhereClauseFromParameters(whereParams);
			retList = ExecuteQuery(query,whereParams);
		}
		return retList;
	}
	
	public List<?> GetCompositeTableData(String tableName,ParameterCollection whereParams)
	{
		List<?> retList;
		String query = "from "+tableName+" T ";
		if(whereParams==null || whereParams.InputParameterList.isEmpty())
		{
			retList = ExecuteQuery(query);
		}
		else
		{
			query += GenerateTableIdWhereClauseFromParameters(whereParams);
			retList = ExecuteQuery(query,whereParams);
		}
		return retList;
	}
	
	public List<?> GetCompositeTableData(String tableName,ParameterCollection whereParams, ParameterCollection whereParamsTable)
	{
		List<?> retList;
		String query = "from "+tableName+" T ";
		if(whereParams==null || whereParams.InputParameterList.isEmpty())
		{
			retList = ExecuteQuery(query);
		}
		else
		{
			query += GenerateTableIdWhereClauseFromParameters(whereParams);
			query += GenerateTableWhereCompositeClauseFromParameters(whereParamsTable);
			ParameterCollection whereParamsAll = whereParams;
			for(Parameter param : whereParamsTable.InputParameterList)
				whereParamsAll.Add(param);
			retList = ExecuteQuery(query,whereParamsAll);
		}
		return retList;
	}
	
	private String GenerateTableWhereCompositeClauseFromParameters(ParameterCollection whereParams)
	{
		// Con questa funzione costruisco le where clause per la query
		String ret = "";
		if(whereParams.InputParameterList.size()==0)
			return "";
		for(Parameter param : whereParams.InputParameterList)
		{
			ret += " and T."+param.Name +"= :"+param.Name; 
		}
		return ret;  // Il substring mi toglie il primo "and "
	}
	
	private String GenerateTableWhereClauseFromParameters(ParameterCollection whereParams)
	{
		// Con questa funzione costruisco le where clause per la query
		String ret = " where ";
		if(whereParams.InputParameterList.size()==0)
			return "";
		for(Parameter param : whereParams.InputParameterList)
		{
			ret += param.Name +"= :"+param.Name+" and "; 
		}
		return ret.substring(0,ret.length()-4);  // Il substring mi toglie l'ultimo "and "
	}
	
	private String GenerateTableIdWhereClauseFromParameters(ParameterCollection whereParams)
	{
		// Con questa funzione costruisco le where clause per la query
		String ret = " where ";
		if(whereParams.InputParameterList.size()==0)
			return "";
		for(Parameter param : whereParams.InputParameterList)
		{
			ret += "T.id."+param.Name +"= :"+param.Name+" and "; 
		}
		return ret.substring(0,ret.length()-4);  // Il substring mi toglie l'ultimo "and "
	}
	
	public List<?> GetViewData(String viewName)
	{
		List<?> retList;
		String query = "select V.id from "+viewName+" V ";
		retList = ExecuteQuery(query);
		return retList;
	}
	
	public List<?> GetViewData(String viewName,ParameterCollection whereParams)
	{
		List<?> retList;
		String query = "select V.id from "+viewName+" V ";
		if(whereParams==null || whereParams.InputParameterList.isEmpty())
		{
			retList = ExecuteQuery(query);
		}
		else
		{
			query += GenerateViewWhereClauseFromParameters(whereParams);
			retList = ExecuteQuery(query,whereParams);
		}
		return retList;
	}
	
	private String GenerateViewWhereClauseFromParameters(ParameterCollection whereParams)
	{
		// Con questa funzione costruisco le where clause per la query
		String ret = " where ";
		if(whereParams.InputParameterList.isEmpty())
			return "";
		for(Parameter param : whereParams.InputParameterList)
		{
			ret += "V.id."+param.Name +"= :"+param.Name+" and "; 
		}
		return ret.substring(0,ret.length()-4);  // Il substring mi toglie l'ultimo "and "
	}
	
	@SuppressWarnings("unchecked")
	public Object GetObject(String tableName, ParameterCollection whereParams)
	{
		Object ret = null;
		try
		{
			List<Object> listObject = (List<Object>) GetTableData(tableName,whereParams);
			if(listObject != null && !listObject.isEmpty())
			{
				for(Object o : listObject)
				{
					ret = o;
				}
			}
		}
		catch(Exception e)
		{
			
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Object GetObjectView(String tableName, ParameterCollection whereParams)
	{
		Object ret = null;
		try
		{
			List<Object> listObject = (List<Object>) GetViewData(tableName,whereParams);
			if(listObject != null && !listObject.isEmpty())
			{
				for(Object o : listObject)
				{
					ret = o;
				}
			}
		}
		catch(Exception e)
		{
			
		}
		return ret;
	}
	
	public boolean Save(Object o)
	{
		// Questa funzione salva un nuovo record
		boolean ret = true;
		try
		{
			session.beginTransaction();
			session.save(o);
			session.flush();
		}
		catch(Exception e)
		{
			ret = false;
			
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
        return ret;
	}
	
	@SuppressWarnings("unchecked")
	public boolean Delete(String tableName, ParameterCollection whereParams)
	{
		// Questa funzione estrae una lista di oggetti da updatare ed esegue l'update
		// E' molto utile specialmente se voglio uploadare più oggetti insieme!
		boolean ret = false;
		try
		{
			List<Object> list = (List<Object>) GetTableData(tableName, whereParams);
			if(list != null)
			{
				session.beginTransaction();
				for(Object o : list)
				{
					session.delete(o);
					session.flush();
				}
			}
			//System.out.println("BaseDBOperations.Update nessun oggetto da uploadare");
			ret = true;
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
        return ret;
	}
	
	public boolean DeleteComposite(String tableName, ParameterCollection whereParams)
	{
		// Questa funzione estrae una lista di oggetti da updatare ed esegue l'update
		// E' molto utile specialmente se voglio uploadare più oggetti insieme!
		boolean ret = false;
		try
		{
			String query = "delete from "+tableName+" T ";
			if(whereParams==null)
			{
				ret = ExecuteUpdate(query);
			}
			else
			{
				query += GenerateTableIdWhereClauseFromParameters(whereParams);
				//System.out.println("BaseDBOperations.Update query "+query);
				ret = ExecuteUpdate(query, whereParams);
			}
		}
		catch(Exception e)
		{
			
		}
		//System.out.println("BaseDBOperations.Update ret "+ret);
        return ret;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean ExecuteUpdate(String query, ParameterCollection params)
	{
		boolean ret = false;
		int queryRet;
		try
		{
			session.beginTransaction();
			Query q = session.createQuery(query);
			//System.out.println("BaseDBOperations.ExecuteQuery query "+query);
			for(Parameter param : params.InputParameterList)
			{
				if(param.Value instanceof List)
				{
					q.setParameterList(param.Name, (Collection) param.Value);
				}
				else
				{
					q.setParameter(param.Name, param.Value);
				}
			}
			queryRet = q.executeUpdate();
			if(queryRet>0)
				ret = true;
			session.flush();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
		return ret;
	}
	
	public boolean ExecuteUpdate(String query)
	{
		boolean ret = false;
		int queryRet;
		try
		{
			session.beginTransaction();
			queryRet = session.createQuery(query).executeUpdate();
			if(queryRet>0)
				ret = true;
			session.flush();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
		return ret;
	}
	
	public boolean CheckIfExistsTable(String tableName, ParameterCollection whereParams)
	{
		return GetObject(tableName,whereParams) != null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean Update(String tableName, ParameterCollection params, ParameterCollection whereParams)
	{
		// Questa funzione estrae una lista di oggetti da updatare ed esegue l'update
		// E' molto utile specialmente se voglio uploadare più oggetti insieme!
		boolean ret = false;
		try
		{
			List<Object> list = (List<Object>) GetTableData(tableName, whereParams);
			if(list != null)
			{
				session.beginTransaction();
				for(Object o : list)
				{
					ret = true;
					o = SetUpdateFieldsFromParameters(o, params);
					session.update(o);
					session.flush();
				}
			}
			//System.out.println("BaseDBOperations.Update nessun oggetto da uploadare");
		}
		catch(Exception e)
		{
			System.out.println("Error on update\n");
			e.printStackTrace();
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
        return ret;
	}
	
	@SuppressWarnings("unchecked")
	public boolean UpdateComposite(String tableName, ParameterCollection params, ParameterCollection whereParams)
	{
		// Questa funzione estrae una lista di oggetti da updatare ed esegue l'update
		// E' molto utile specialmente se voglio uploadare più oggetti insieme!
		boolean ret = false;
		try
		{
			List<Object> list = (List<Object>) GetCompositeTableData(tableName, whereParams);
			if(list != null)
			{
				session.beginTransaction();
				for(Object o : list)
				{
					ret = true;
					o = SetUpdateFieldsFromParameters(o, params);
					session.update(o);
					session.flush();
				}
			}
			//System.out.println("BaseDBOperations.Update nessun oggetto da uploadare");
		}
		catch(Exception e)
		{
			System.out.println("Update Composite Error\n");
			e.printStackTrace();
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
        return ret;
	}
	
	@SuppressWarnings("unchecked")
	public boolean UpdateComposite(String tableName, ParameterCollection params, ParameterCollection whereParams, ParameterCollection whereParamsTable)
	{
		// Questa funzione estrae una lista di oggetti da updatare ed esegue l'update
		// E' molto utile specialmente se voglio uploadare più oggetti insieme!
		boolean ret = false;
		try
		{
			List<Object> list = (List<Object>) GetCompositeTableData(tableName, whereParams, whereParamsTable);
			if(list != null)
			{
				session.beginTransaction();
				for(Object o : list)
				{
					ret = true;
					o = SetUpdateFieldsFromParameters(o, params);
					session.update(o);
					session.flush();
				}
			}
			//System.out.println("BaseDBOperations.Update nessun oggetto da uploadare");
		}
		catch(Exception e)
		{
			System.out.println("Update Composite Error\n");
			e.printStackTrace();
		}
		finally
		{
			if(!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		}
        return ret;
	}
	
	private Object SetUpdateFieldsFromParameters(Object fromObj, ParameterCollection params)
	{
		// Con questa funzione eseguo l'upload dei fields in un oggetto
		for(Field f : fromObj.getClass().getDeclaredFields()) // il getDeclaredFields mi permette di estrarre i field private che sono i campi delle tabelle
		{
			if(params.Contains(f.getName()))
			{
				try 
				{
					//System.out.println("BaseDBOperations.SetUpdateFieldsFromParameters field "+f.getName()+" in "+params.Get(f.getName()));
					f.setAccessible(true);
					f.set(fromObj, params.Get(f.getName()));
					f.setAccessible(false);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
		return fromObj;
	}

}