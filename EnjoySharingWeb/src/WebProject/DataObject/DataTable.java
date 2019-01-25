package WebProject.DataObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DataTable {
	public String[] visibleColumns;
	public String[] editableColumns;
	public String[] editableOnCreateColumns;
	public String[] keysColumns;
    public String[] columnNames;
    public Object[][] data;
    public int colsPlus = 2;

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
					data[i][0] = i+1;  // RowNumber
					data[i][1] = true;  // Visible
					fields = obj.getClass().getDeclaredFields();
					for (int j=0;j<fields.length;j++) {
						field = fields[j];
						field.setAccessible(true);
						data[i][j+colsPlus]=field.get(obj).toString();
				    }
				}
			}
		}
        catch(Exception e)
        { }
    }
    
    protected void FillColumnNames(List<?> list)
    {
    	Field[] fields;
		Field field;
    	if(!list.isEmpty())
    	{
        	columnNames = new String[list.get(0).getClass().getDeclaredFields().length+colsPlus];
    		fields = list.get(0).getClass().getDeclaredFields();
    	}
    	else
    	{
    		columnNames = new String[list.getClass().getDeclaredFields().length+colsPlus];
    		fields = list.getClass().getDeclaredFields();
    	}
        columnNames[0] = "RowNumber";
        columnNames[1] = "Visible";
		for (int j=0;j<fields.length;j++) {
			field = fields[j];
			field.setAccessible(true);
			columnNames[j+colsPlus]=field.getName();
	    }
    }
    
    public void setVisibleColumns(String[] vCol) { visibleColumns = vCol == null ? new String[0] : vCol; }
    
    public void setEditableColumns(String[] eCol) { editableColumns = eCol == null ? new String[0] : eCol; }
    
    public void setEditableOnCreateColumns(String[] eCol) { editableOnCreateColumns = eCol == null ? new String[0] : eCol; }
    
    public void setKeysColumns(String[] kCol) { keysColumns = kCol == null ? new String[0] : kCol; }

    public boolean isColumnVisible(String col) { return Arrays.asList(visibleColumns).contains(col); }
    
    public boolean isColumnEditable(int c) { return Arrays.asList(editableColumns).contains(GetVisibleColumnName(c)); }
    
    public boolean isColumnEditable(String c) { return Arrays.asList(editableColumns).contains(c); }
    
    public boolean isColumnEditableOnCreate(String c) { return Arrays.asList(editableOnCreateColumns).contains(c); }
    
    public boolean isColumnKey(String c) { return Arrays.asList(keysColumns).contains(c); }

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

    public String[] GetVisibleColumns()
    {
    	return visibleColumns;
    }

    public String GetKeysColumns(int r)
    {
    	String ret = "";
    	String column;
    	for(String col : keysColumns)
    	{
    		column = col;
        	for(String c : columnNames)
        	{
        		if(c.equals(col+"Id"))
        			column = c;
        		if(c.equals(col+"Code"))
        			column = c;
        	}
    		ret += column +"="+GetCell(r,column)+"&";
    	}
        return ret.substring(0, ret.lastIndexOf("&"));
    }

    public Object[][] GetKeys(int r)
    {
    	Object[][] ret = new Object[keysColumns.length][2];
    	String column;
    	int index=0;
    	for(String col : keysColumns)
    	{
    		column = col;
        	for(String c : columnNames)
        	{
        		if(c.equals(col+"Id"))
        			column = c;
        		if(c.equals(col+"Code"))
        			column = c;
        	}
    		ret[index][0] = column;
    		ret[index][1] = GetCell(r,column);
    		index++;
    	}
        return ret;
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

    public String GetVisibleColumnName(int i)
    {
        return visibleColumns[i];
    }

    public String GetVisibleColumnNameDDL(int i)
    {
    	String col = visibleColumns[i];
    	if(isColumnKey(col))
    		return col;
    	for(String c : columnNames)
    	{
    		if(c.equals(col+"Id") && !isColumnKey(col+"Id"))
    			return c;
    		if(c.equals(col+"Code") && !isColumnKey(col+"Code"))
    			return c;
    	}
    	return col;
    }

    public String GetVisibleColumnNameDDL(String col)
    {
    	if(isColumnKey(col))
    		return col;
    	for(String c : columnNames)
    	{
    		if(c.equals(col+"Id") && !isColumnKey(col+"Id"))
    			return c;
    		if(c.equals(col+"Code") && !isColumnKey(col+"Code"))
    			return c;
    	}
    	return col;
    }

    public int GetRowCount()
    {
        return data.length;
    }

    public Object[] GetRows()
    {
        return data;
    }

    public Object[] GetRowsVisibleColumn()
    {
    	if(data == null) return new Object[0][0];
    	Object[][] ret = new Object[data.length][visibleColumns.length];
    	for(int i=0;i<data.length;i++)
    	{
    		for(int j=0;j<visibleColumns.length;j++)
    		{
    			ret[i][j] = data[i][GetColumnIndex(visibleColumns[j])];
    		}
    	}
        return ret;
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
    
    public boolean getRowVisibility(int r)
    {
        return (boolean) GetRow(r)[1];
    }

    public void SetRowVisibility(int r, boolean visible)
    {
        GetRow(r)[1] = visible;
    }

    public void SetRowsVisibility(boolean visible)
    {
        for(int i=0;i<data.length;i++)
            SetRowVisibility(i, visible);
    }

    public void ApplyFilters(ParameterCollection filters)
    {
        Object[] row;
        boolean visible;
        for(int i=0;i<GetRowCount();i++)
        {
            visible = false;
            row = GetRow(i);
            for(int j=0;j<columnNames.length;j++)
            {
                if(filters.Contains(columnNames[j]))
                    if(row[j].toString().startsWith(filters.Get(columnNames[j]).toString().toUpperCase()))
                        visible = true;
            }
            SetRowVisibility(i, visible);
        }
    }
}
