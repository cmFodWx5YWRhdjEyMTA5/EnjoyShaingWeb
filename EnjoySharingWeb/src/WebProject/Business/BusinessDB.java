package WebProject.Business;

import java.util.List;

import Hibernate.HibernateOperation;
import Hibernate.Tables.Parameter;
import WebProject.DataObject.ParameterCollection;

public class BusinessDB extends BusinessBase {
	
	@SuppressWarnings("unchecked")
	public String GetParameter(String parameterCode)
	{
		String parameterValue = null;
		ParameterCollection whereParams = new ParameterCollection();
		whereParams.Add("parameterCode", parameterCode);
		List<Parameter> listParameter = (List<Parameter>) new HibernateOperation().GetTableData("Parameter",whereParams);
		if(listParameter != null)
		{
			for(Parameter P : listParameter)
			{
				parameterValue = P.getParameterValue();
			}
		}
		return parameterValue;
	}
	
}
