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
	
	public class Constants
	{
		public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
		public static final String dateFormatFromRequest = "yyyy-MM-dd HH:mm";
	}
	
	public Date GetNow()
	{
		try 
		{
			DateFormat dateFormat = new SimpleDateFormat(Constants.dateFormat);
			Date date = new Date();
			return dateFormat.parse(dateFormat.format(date));
		} 
		catch (Exception e) 
		{ }
		return null;
	}
	
	public Date GetDate(String str)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.dateFormat);
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
	
	public Date GetDateFromRequest(String str)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.dateFormatFromRequest);
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
	        	e.printStackTrace();
				return null;
	        }
	    }
	    return ret;
	}
	
	public byte[] StringBase64ToByteArray(String str)
	{
		try 
        {
			String strBase64 = Base64.getEncoder().encodeToString(str.getBytes());
			return strBase64.getBytes();
        }
        catch(Exception e) 
        {
        	e.printStackTrace();
			return null;
        }
	}
	
	public String ArrayToBase64(byte[] byteArray)
	{
		try 
        {

			return new String(Base64.getDecoder().decode(new String(byteArray))).replaceAll("\\r|\\n", "").replaceAll(" ", "+");//Base64.getEncoder().encodeToString(byteArray);
        }
        catch(Exception e) 
        {
        	e.printStackTrace();
			return null;
        }
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
		String value;
		try
		{
			rowObject = new JsonObject();
			for(Parameter param : params.InputParameterList)
			{
				value = param.GetValue() == null ? "" : param.GetValue().toString();
				rowObject.addProperty(param.Name, value);
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
