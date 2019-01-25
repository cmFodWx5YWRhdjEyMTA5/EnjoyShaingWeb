package Hibernate.DataObjectClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DataTable {
    public String[] columnNames;
    public Object[][] data;

    public DataTable(List<?> list)
    {
    	Field[] fields;
		Field field;
		Object obj;
		try
		{
			FillColumnNames(list);
			if(list != null && !list.isEmpty())
			{
	            data = new Object[list.size()][columnNames.length];
				for(int i=0;i<list.size();i++)
				{
					obj = list.get(i);
					fields = obj.getClass().getDeclaredFields();
					for (int j=0;j<fields.length;j++) {
						field = fields[j];
						field.setAccessible(true);
						data[i][j]=FillValue(field,obj);
				    }
				}
			}
		}
        catch(Exception e)
        {
        	System.out.println("Error on DataTable create\n");
        	e.printStackTrace();
        }
    }
    
    protected Object FillValue(Field field, Object obj)
    {
    	Object value;
		try 
		{
			value = field.get(obj);
		} 
		catch (Exception e) {
			value = null;
		}
		if(value == null)
		{
	    	if(field.getType() == java.math.BigInteger.class)
	    		return java.math.BigInteger.valueOf((long)0);
	    	if(field.getType() == java.lang.Integer.class)
	    		return 0;
	    	return "";
		}
		if(field.getType() == String.class)
    		return value.toString();
    	return value;
    }
    
    protected void FillColumnNames(List<?> list)
    {
    	Field[] fields;
		Field field;
    	if(!list.isEmpty())
    	{
        	columnNames = new String[list.get(0).getClass().getDeclaredFields().length];
    		fields = list.get(0).getClass().getDeclaredFields();
    	}
    	else
    	{
    		columnNames = new String[list.getClass().getDeclaredFields().length];
    		fields = list.getClass().getDeclaredFields();
    	}
		for (int j=0;j<fields.length;j++) {
			field = fields[j];
			field.setAccessible(true);
			columnNames[j]=field.getName();
	    }
    }

    public boolean isVisible(int r) { return (boolean) GetCell(r, "Visible"); }

    public boolean isEmpty()
    {
        return !(data.length>0);
    }

    public boolean ColumnExists(String columnName)
    {
    	return Arrays.asList(columnNames).contains(columnName);
    }

    public String[] GetColumns()
    {
        return columnNames;
    }

    public int GetColumnIndex(String columnName)
    {
        String column;
        for(int i=0;i<columnNames.length;i++)
        {
            column = columnNames[i];
            if(column.equals(columnName))
                return i;
        }
        return -1;
    }

    public String GetColumnName(int i)
    {
        return columnNames[i];
    }

    public int GetRowCount()
    {
        return data.length;
    }

    public Object[] GetRows()
    {
        return data;
    }

    public Object[] GetRow(int i)
    {
        return data[i];
    }

    public Object[] GetRow(String column, String value)
    {
        if(!ColumnExists(column)) return null;
        int columnIndex = GetColumnIndex(column);
        if(columnIndex == -1) return null;
        for(Object[] row : data)
            if(row[columnIndex].toString().equals(value))
                return row;
        return null;
    }

    public Object GetCell(int row, int column)
    {
        return data[row][column];
    }
    
    public String GetCellValue(int row, int column)
    {
    	Object o = GetCell(row,column);
    	return o == null ? "" : o.toString();
    }

    public Object GetCell(int r, String column)
    {
        int colIndex = GetColumnIndex(column);
        if(colIndex == -1)
            return null;
        return GetCell(r,colIndex);
    }
    
    public Object GetRowNumberCellValue(String rn, String column)
    {
        int colIndex = -1;
        for(int i=0;i<columnNames.length;i++)
        {
            if(columnNames[i].equals(column))
                colIndex = i;
        }
        if(colIndex == -1)
            return null;
        Object[] row = GetRow("RowNumber", rn);
        return row[colIndex];
    }
}