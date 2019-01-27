package WebProject.Business;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import WebProject.DataObject.DataTable;
import WebProject.DataObject.Parameter;
import WebProject.DataObject.ParameterCollection;

public class BusinessBase {
	
	public Date GetNow()
	{
		try 
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			return dateFormat.parse(dateFormat.format(date));
		} 
		catch (Exception e) 
		{ }
		return null;
	}
	
	public Date GetDate(String str)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date;
        try 
        {
            date = formatter.parse(str);
            return date;
        } 
        catch (Exception e) 
        {
        	System.out.println("Date converter error\n");
        	e.printStackTrace();
        }
        return null;
	}
	
	public String encrypt(String str)
	{
	    String ret=null;
	    if(str!=null&&!str.equals(""))
	    {
		    try 
	        {
		    	ret = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
	        }
	        catch(Exception e) 
	        {
				return null;
	        }
	    }
	    return ret;
	}
	
	public String decrypt(String str)
	{
		String ret=null;
	    if(str!=null&&!str.equals(""))
	    {
		    try 
	        {
		    	ret = new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
	        }
	        catch(Exception e) 
	        {
				return null;
	        }
	    }
	    return ret;
	}
	
	public String ListToJson(List<?> list)
	{
		Field[] fields;
		Field field;
		Object obj;
		JsonArray retArray = new JsonArray();
		JsonObject rowObject;
		try
		{
			for(int i=0;i<list.size();i++)
			{
				rowObject = new JsonObject();
				obj = list.get(i);
				fields = obj.getClass().getDeclaredFields();
				for (int j=0;j<fields.length;j++) {
					field = fields[j];
					field.setAccessible(true);
					rowObject.addProperty(field.getName(), field.get(obj).toString());
			    }
				retArray.add(rowObject);
			}
		}
		catch(Exception e)
		{
			return null;
		}
		return retArray.toString();
	}
	
	public String CreateJSONObject(ParameterCollection params)
	{
		JsonArray retArray = new JsonArray();
		JsonObject rowObject;
		try
		{
			rowObject = new JsonObject();
			for(Parameter param : params.InputParameterList)
			{
				rowObject.addProperty(param.Name, param.GetValue().toString());
			}
			retArray.add(rowObject);
		}
		catch(Exception e)
		{
			return null;
		}
		return retArray.toString();
	}
	
	public String CreateJSONObject(DataTable dataTable)
	{
		Object[] row;
		JsonArray retArray = new JsonArray();
		JsonObject rowObject;
		try
		{
			for(int i=0;i<dataTable.GetRowCount();i++)
			{
				rowObject = new JsonObject();
				row = dataTable.GetRow(i);
				for (int j=0;j<dataTable.GetColumns().length;j++) {
					String columnName = dataTable.GetColumns()[j];
					rowObject.addProperty(columnName, row[j] == null ? "" : row[j].toString());
			    }
				retArray.add(rowObject);
			}
		}
		catch(Exception e)
		{ }
		return retArray.toString();
	}

	public String CreateJSONObject(Hibernate.DataObjectClass.DataTable dataTable) {
		Object[] row;
		JsonArray retArray = new JsonArray();
		JsonObject rowObject;
		try
		{
			for(int i=0;i<dataTable.GetRowCount();i++)
			{
				rowObject = new JsonObject();
				row = dataTable.GetRow(i);
				for (int j=0;j<dataTable.GetColumns().length;j++) {
					String columnName = dataTable.GetColumns()[j];
					rowObject.addProperty(columnName, row[j] == null ? "" : row[j].toString());
			    }
				retArray.add(rowObject);
			}
		}
		catch(Exception e)
		{ }
		return retArray.toString();
	}
}
