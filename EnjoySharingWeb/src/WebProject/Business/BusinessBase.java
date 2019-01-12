package WebProject.Business;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

}
