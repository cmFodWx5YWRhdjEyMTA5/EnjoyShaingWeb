package WebProject.DataObject;

import java.util.ArrayList;
import java.util.List;

public class ParameterCollection {

	public List<Parameter> InputParameterList;
	
	public ParameterCollection()
	{
		InputParameterList = new ArrayList<Parameter>();
	}
	
	public int size()
	{
		return InputParameterList.size();
	}
	
	public void setInputParameterList(List<Parameter> params)
	{
		this.InputParameterList = params;
	}
	
	public void Add(Parameter param)
	{
		InputParameterList.add(param);
	}
	
	public void Add(String Name,Object Value)
	{
		InputParameterList.add(new Parameter(Name,Value));
	}
	
	public void Add(String Name,Object Value, String Type)
	{
		InputParameterList.add(new Parameter(Name,Value,Type));
	}
	
	public void Update(String Name,Object Value)
	{
		Drop(Name);
		Add(Name,Value);			
	}
	
	public void Drop(String Name)
	{
		if(Contains(Name))
			InputParameterList.remove(IndexOfName(Name));
	}
	
	public int IndexOfName(String Name)
	{
		int ret = -1;
		for(int i = 0;i<InputParameterList.size();i++)
			if(InputParameterList.get(i).Name.equals(Name))
				ret = i;
		return ret;
	}
	
	public boolean Contains(String Name)
	{
		boolean IsContain = false;
		for(Parameter param : InputParameterList)
			if(param.Name.toUpperCase().equals(Name.toUpperCase()))  // Ignoro il CaseSensitive per Update se no viene un casino!
				IsContain=true;
		return IsContain;
	}
	
	public Object Get(int index)
	{
		if(InputParameterList.size() > index) return null;
		return InputParameterList.get(index);
	}
	
	public Object Get(String Name)
	{
		for(Parameter param : InputParameterList)
			if(param.Name.toUpperCase().equals(Name.toUpperCase()))  // Ignoro il case sensitive se no è un casino per l'Update!
				return param.GetValue();
		return null;
	}
	
	public Parameter GetParameter(String Name)
	{
		for(Parameter param : InputParameterList)
			if(param.Name.equals(Name))
				return param;
		return null;
	}
	
	public Object[] GetParametersListObject()
	{
		Object[] parameters = new Object[InputParameterList.size()];
		for(int i=0;i<InputParameterList.size();i++)
			parameters[i] = ((Parameter)InputParameterList.get(i)).Value;
		return parameters;
	}
	
	public List<Parameter> GetParametersList()
	{
		return InputParameterList;
	}
	
	public String[] GetParametersNames()
	{
		String[] ret = new String[InputParameterList.size()];
		for(int i=0;i<InputParameterList.size();i++)
			ret[i] = InputParameterList.get(i).Name;
		return ret;
	}
	
	public String[] GetParametersValues()
	{
		String[] ret = new String[InputParameterList.size()];
		for(int i=0;i<InputParameterList.size();i++)
			ret[i] = InputParameterList.get(i).Value.toString();
		return ret;
	}
	
}