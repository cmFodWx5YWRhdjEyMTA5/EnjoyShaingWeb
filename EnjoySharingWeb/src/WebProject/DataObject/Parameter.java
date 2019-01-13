package WebProject.DataObject;

public class Parameter {

	public String Name;
	public Object Value;
	public String Type;
	
	public Parameter(String Name,Object Value)
	{
		this.Name = Name;
		this.Value = Value;
	}
	
	public Parameter(String Name,Object Value,String Type)
	{
		this.Name = Name;
		this.Value = Value;
		this.Type = Type;
	}
	
	public String GetName()
	{
		return this.Name;
	}
	
	public Object GetValue()
	{
		if(Value.getClass().toString().contains("Ljava.lang.String"))
			return GetStringValue();
		return this.Value;
	}
	
	private String GetStringValue()
	{
		try
		{
			String[] lStr = (String[])this.Value;
			String ret="";
			for(String item : lStr)
				ret += item;
			return ret;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
}