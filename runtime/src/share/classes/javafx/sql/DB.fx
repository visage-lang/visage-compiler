/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package javafx.sql;

import java.sql.*;
import java.lang.Object;
import javax.sql.*;
import java.lang.System;
//TODO import javafx.reflect.*; 
import java.util.Map;
import java.util.HashMap;

/**
 * JavaFX Framework for access to Relational Databases via JDBC
 * @author jclarke
 */

public class DB {
    /**
     * holds the Database connection
     */
    attribute connection:Connection;
    
    /**
     * holds a resuable statement
     */
    public attribute statement:Statement ;
    
    /**
     * indicates if the connection is in autoCommit mode or not.
     */
    public attribute autoCommit:Boolean = true on replace {
        if(connection <> null) {
            connection.setAutoCommit(autoCommit);
        }
    }
    
    /**
     * optional connection password
     */
    public attribute password:String;   
    
    /**
     * optional connection user id
     */    
    public attribute user:String;
    
    /**
     * optional database connection URL
     * If a dataSource is provided, it will be used to establish the
     * connection. Otherwise, the dbURL and user/password attributes will
     * be used.     
     */     
    public attribute dbUrl:String on replace {
        if(connection <> null and not connection.isClosed()) {
            connection.close();
        }
    };

    /**
     * the DataSource to use to establish the database connection.
     * If a dataSource is provided, it will be used to establish the
     * connection. Otherwise, the dbURL and user/password attributes will
     * be used.
     */
    public attribute dataSource:DataSource on replace {
        if(connection <> null and not connection.isClosed()) {
            connection.close();
        }
    };
    
    /**
     * holds the last set of warnings from an sql execution
     */
    public attribute warnings:Warning[];
    
    /**
     * Convenience method to get a statement
     * @return the statement
     */
    public function getStatement():Statement {
       getConnection().createStatement();
    }
    
    
    /**
     * Convenience method to get a reusable statement
     * @return the statement
     */
    private function getInternalStatement():Statement {
       if(statement == null or statement.isClosed()) {
            statement = getConnection().createStatement();
       }
       statement;
    }    
    
    /**
     * Convenience method to get a PreparedStatement
     * @param sql the sql statement
     * @return the prepared statement
     */
    public function getPreparedStatement(sql:String):PreparedStatement {
        connection.prepareStatement(sql);
    }
    
    /**
     * get the database connection
     * @return the connection
     */
    public function getConnection():Connection { 
        if(connection == null or connection.isClosed()) {
            try {
                if(dataSource <> null) {
                    connection = dataSource.getConnection();
                }else if(user == null) {
                    connection = DriverManager.getConnection(dbUrl);
                } else {
                    connection = DriverManager.getConnection(dbUrl, user, password);
                }
                connection.setAutoCommit(autoCommit);
            }catch(e:SQLException) {
                e.printStackTrace();
            }        
        }
        return connection; 
    }
    
    /**
     * close the database connection
     */
    public function closeConnection():Void { 
        if(connection <> null and not connection.isClosed()) {
            connection.close();
        }
    }
    
    /**
     * set the warnings list
     * @param sqlWarnings the SQLWarning object
     */
    private function setWarnings(sqlWarnings:SQLWarning):Void {
        delete warnings;
        var sqlWarn = sqlWarnings;
        while(sqlWarn <> null) {
            var ww = Warning {
                message: sqlWarn.getMessage()
                SQLState: sqlWarn.getSQLState()
                errorCode: sqlWarn.getErrorCode()
            };
            insert ww into warnings;
            sqlWarn = sqlWarn.getNextWarning();
        }
    }
    
    /**
     * convenience method to commit a transaction
     */
    public function commit(): Void {
         getConnection().commit();
    }
    
    /**
     * convenience method to abort a transaction
     */    
    public function rollback():Void {
        getConnection().rollback();
    }
    
    /**
     * convenience method to rollback to a save point
     * @param savepoint the save point
     */       
    public function rollback(savepoint:Savepoint):Void {
        getConnection().rollback(savepoint);
    }   
    
    /**
     * convenience method to release a save point
     * @param savepoint the save point
     */     
    public function release(savepoint:Savepoint):Void {
        getConnection().releaseSavepoint(savepoint);
    } 
    
    /**
     * convenience method to set a save point
     * @param savepoint the save point identifier
     * @return the save point
     */     
    public function setSavepoint(savepoint:String):Savepoint {
        getConnection().setSavepoint(savepoint)
    }
    
    /**
     * execute an sql statement
     * @param sql the sql statement
     * @return true if a result set is available
     */
    public function execute(sql:String):Boolean {
        //System.out.println("execute '{sql}'");
        var rc:Boolean;
        try {
            statement = getInternalStatement();
            rc = statement.execute(sql);
            setWarnings(statement.getWarnings());
            
        }catch(e:SQLException) {
            if(not connection.getAutoCommit()) {
                rollback();
            }
           e.printStackTrace();
        }       
        rc;
    }
    
    /**
     * execute an sql statement, using the values Sequence to populate the
     * parameters in the prepared statment. 
     * @param sql the sql statement
     * @param values the sequence containing the parameter values
     * @return true if a result set is available
     */     
    public function execute(sql:String, values:String[]):Boolean {
        //System.out.println("execute '{sql}' {for(v in values) '{v}' }");
        var rc:Boolean;
        try {
            var preparedStatement = getPreparedStatement(sql);
            rc = execute(preparedStatement, values);
            setWarnings(statement.getWarnings());
            preparedStatement.close();
        }catch(e:SQLException) {
           e.printStackTrace();
        }         
        rc;
    }    
    
    /**
     * execute an sql statement
     * @param sql the sql statement
     * @param statement the statement to use
     * @return true if a result set is available
     */    
    public function execute(sql:String, statement:Statement): Boolean {
         var rc:Boolean;
        try {
            statement = getInternalStatement();
            rc = statement.execute(sql);
            setWarnings(statement.getWarnings());
            statement.clearWarnings();
        }catch(e:SQLException) {
           e.printStackTrace();
        }         
        rc;       
    }
    
    
    /**
     * execute a prepared statement, using the values Sequence to populate the
     * parameters in the prepared statement. 
     * @param preparedStatement the prepared statement
     * @param values the sequence containing the parameter values
     * @return true if a result set is available
     */     
    public function execute(preparedStatement:PreparedStatement, values:String[]):Boolean {
        var rc:Boolean;
        try {
            preparedStatement.clearParameters();
            for(i in [0..<sizeof values]) {
                preparedStatement.setString(i+1, values[i]);
            }
            rc = preparedStatement.execute();
            setWarnings(statement.getWarnings());
        }catch(e:SQLException) {
           e.printStackTrace();
        }         
        rc;
    }    
    
    /**
     * execute an sql statement, this sql statement is always
     * committed 
     * @param sql the sql statement
     * @return the number of rows updated
     */    
    public function executeUpdate(sql:String):Integer {
        //System.out.println("executeUpdate '{sql}'");
        var rc:Integer;
        try {
            statement = getInternalStatement();
            rc = statement.executeUpdate(sql);
            setWarnings(statement.getWarnings());
        }catch(e:SQLException) {
            e.printStackTrace();
        }         
        rc;
    }
    
    /**
     * execute an sql statement, using the values Sequence to populate the
     * parameters in the prepared statment. This sql statement is always
     * committed 
     * @param sql the sql statement
     * @param values the sequence containing the parameter values
     * @return the number of rows updated
     */     
    public function executeUpdate(sql:String, values:String[]):Integer {
        //System.out.println("executeUpdate '{sql}' {for(v in values) '{v}' }");
        var rc:Integer;
        try {
            var preparedStatement = getPreparedStatement(sql);
            rc = executeUpdate(preparedStatement, values);
            preparedStatement.close();
        }catch(e:SQLException) {
            e.printStackTrace();
        }         
        rc;
    }    
    
    /**
     * execute an sql statement
     * @param sql the sql statement
     * @param statement the statement to use
     * @return the number of rows updated
     */      
    public function executeUpdate(preparedStatement:PreparedStatement, values:String[]):Integer {
        var rc:Integer;
        try {
            preparedStatement.clearParameters();
            for(i in [0..<sizeof values]) {
                preparedStatement.setString(i+1, values[i]);
            }
            rc = preparedStatement.executeUpdate();
            setWarnings(statement.getWarnings());
        }catch(e:SQLException) {
            e.printStackTrace();
        }         
        rc;
    }
    
    
    /**
     * perform a query
     *
     * @param sql the sql query
     * @return the ResultSet
     */
    public function query(sql:String):ResultSet {
        //System.out.println("query '{sql}'");
        var rc:ResultSet;
        try {
            statement = getStatement();
            rc = statement.executeQuery(sql);
            setWarnings(statement.getWarnings());
            statement.clearWarnings();
        }catch(e:SQLException) {
            e.printStackTrace();
        }         
        rc;
    }
    
    /**
     * perform a query
     *
     * @param sql the sql query
     * @param process the function to call for each row in the ResultSet
     * @return the ResultSet
     */    
    public function query(sql:String, process:function(rs:ResultSet):Void) {
        var rs = query(sql);
        while(rs.next()) {
            process(rs);
        }
    }
    
    /**** TODO Reflection
    public function query( sql:String, process:function(obj:ObjectRef), resultClass:ClassRef):Void {
        var rs = query(sql);
        var rsmd = rs.getMetaData();
        var numColumns = rsmd.getColumnCount();
        var col2FXMap = new HashMap();
        for(i in [1..numColumns]) {
            col2FXMap.put(rsmd.getColumnName(i),Column2FX {
                    columnName: rsmd.getColumnName(i)
                    columnType: rsmd.getColumnType(i)
                    columnIndex: i                  
                });
        }
        var attrRefList = resultClass.getAttributes(true);
        var iter = attrRefList.iterator();
        while(iter.hasNext()) {
            var attr = iter.next() as AttributeRef;
            var attrName = attr.getName();
            var colMap = col2FXMap.get(attrName);
            if(colMap <> null) {
                colMap.fxName = attrName;
                colMap.fxAttribute = attr;
                colMap.fxClass = resultClass;
            }else {
                System.out.println("Warning: attribute {attrName} does not map to a column name in the result set");
            }
        }
        
        lquery(rs, resultClass, col2FXMap, process);
    }
    
     public function query( sql:String, process:function(obj:ObjectRef), col2FXMap:Map):Void {   
         var rs = query(sql);
         var resultClass =( col2FXMap.values().iterator().next() as Column2FX).fxClass;
         lquery(rs, resultClass, col2FXMap, process);
     }
    
    private function lquery(rs:ResultSet, resultClass:ClassRef, col2FXMap:Map, process:function(obj:ObjectRef) ) {
        var rsmd = rs.getMetaData();
        var numColumns = rsmd.getColumnCount();        
        while(rs.next()) {
            var obj = resultClass.newInstance();
            for(i in [1..numColumns]) {
                var colMap = col2FXMap.get(rsmd.getColumnName(i)) as Column2FX;
                // PER, I would like to get the type of the fx attribute here
                // Integer, Number, Boolean or String only.
                if(colMap <> null) {
                    var type = colMap.fxAttribute.getType();
                    if(type instanceof java.lang.Integer) {
                         fxAttribute.setValue(obj,rs,getInt(i));
                    }else if(type instanceof java.lang.Number) {
                        fxAttribute.setValue(obj,rs.getDouble(i));
                    }else if(type instanceof java.lang.Boolean) {
                        fxAttribute.setValue(obj,rs.getBoolean(i));
                    }else {
                        fxAttribute.setValue(obj,rs.getString(i));
                    }
                }
                fxAttribute.setValue(obj, value ?? ValueRef);
            }
            process(rs);
        }
    }
    
    *******/
    
}
