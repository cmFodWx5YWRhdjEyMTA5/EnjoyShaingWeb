package WebProject.Business;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import WebProject.DataObject.Parameter;
import WebProject.DataObject.ParameterCollection;

public class BusinessBase {
	
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
				rowObject.addProperty(param.Name, (String) param.GetValue());
			}
			retArray.add(rowObject);
		}
		catch(Exception e)
		{
			return null;
		}
		return retArray.toString();
	}

}
