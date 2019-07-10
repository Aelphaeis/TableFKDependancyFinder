package com.cruat.tools.aide.database.pojo;

import javax.xml.bind.JAXBException;

import jmo.serialization.Serializer;

public class TableDependencyInfo {
	
	String tableName;
	String columnName;
	String constantName;
	String referencedTableName;
	String referencedColumnName;
	String constraintSchema;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getConstantName() {
		return constantName;
	}
	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}
	public String getReferencedTableName() {
		return referencedTableName;
	}
	public void setReferencedTableName(String referencedTableName) {
		this.referencedTableName = referencedTableName;
	}
	public String getReferencedColumnName() {
		return referencedColumnName;
	}
	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}
	public String getConstraintSchema() {
		return constraintSchema;
	}
	public void setConstraintSchema(String constraintSchema) {
		this.constraintSchema = constraintSchema;
	}
	
	@Override
	public String toString(){
		try {
			return Serializer.serialize(this);
		} 
		catch (JAXBException e) {
			return super.toString();
		}
		
	}
}
