/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.tools.javafx.ui;

import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.location.BooleanLocation;
import com.sun.javafx.runtime.location.DoubleLocation;
import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.sequence.Sequence;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * A Bean utill class to support FX clases
 * 
 * @author jclarke
 */
public class FXBean {
    Class beanClass;
    PropertyDescriptor[] descriptors;
    Map<String, PropertyDescriptor> propMap = new HashMap<String,PropertyDescriptor>();
    
    /**
     * Creates an empty FXBean
     */
    public FXBean() {
        
    }
    /**
     * Creates an FXBean for the specified class name
     * 
     * @param className
     * @throws java.lang.ClassNotFoundException
     * @throws java.beans.IntrospectionException
     * @throws java.lang.IllegalArgumentException if the class does not extend FXObject
     */
    public FXBean(String className) throws ClassNotFoundException, IntrospectionException {
        Class lbeanClass = this.getClass().getClassLoader().loadClass(className);
        setBeanClass(lbeanClass);
    }
    
    /**
     * Creates an FXBean for the specified class
     * 
     * @param beanClass the bean class
     * @throws java.lang.ClassNotFoundException
     * @throws java.beans.IntrospectionException
     * @throws java.lang.IllegalArgumentException if the class does not extend FXObject
     */
    public FXBean(Class beanClass) throws IntrospectionException, ClassNotFoundException {
        setBeanClass(beanClass);
    }

    public Class getBeanClass() {
        return beanClass;
    }

    /**
     * Sets FXBean class, if the class is not an interface, then the interface class
     * will be substituted. Interface classes are necessary to support inheritance.
     * 
     * @param beanClass the bean class
     * @throws java.beans.IntrospectionException
     * @throws java.lang.IllegalArgumentException if the class does not extend FXObject
     */
    public void setBeanClass(Class beanClass) throws IntrospectionException, ClassNotFoundException{
        if(!beanClass.isInterface()) { // need to get the interface to support inheritence
            try {
                String intfName = beanClass.getName() + "$Intf";
                if(beanClass.getClassLoader() != null) {
                    this.beanClass = beanClass.getClassLoader().loadClass(intfName);
                }else {
                    this.beanClass = beanClass;
                }
            }catch(ClassNotFoundException ignore) {
                this.beanClass = beanClass;
            }
        }else {
            this.beanClass = beanClass;
        }
        BeanInfo bi = Introspector.getBeanInfo(beanClass);
        descriptors = bi.getPropertyDescriptors();
        for(int i = 0; i < descriptors.length; i++) {
            String name = descriptors[i].getName();
            if(name.startsWith("$")) {
                name = name.substring(1);
            }
            propMap.put(name, descriptors[i]);
        }
    }
    
    /**
     * Get the Property descriptor for the named attribute. 
     * @param attribute the attribute name with optional '$' prefix
     * @return the Property descriptor for the named attribute. 
     * @throws java.lang.IllegalArgumentException if the attribute is not 
     *         a member of the bean class
     */
    public PropertyDescriptor getDescriptor(String attribute) {
        if(attribute.startsWith("$")) {
            attribute = attribute.substring(1);
        }
        PropertyDescriptor pd = propMap.get(attribute);
        if(pd == null)
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a valid attribute");
        return pd;
    }    
    
    /**
     *  sets a boolean value for an attribute
     * 
     * @param instance the object instance 
     * @param attribute the attribute name
     * @param value the boolean value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setBoolean(Object instance, String attribute, boolean value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        
        if(desc.getPropertyType() == Boolean.TYPE || desc.getPropertyType() == Boolean.class) {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                Boolean b = Boolean.valueOf(value);
                meth.invoke(instance,b);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        }else if(desc.getPropertyType() == BooleanLocation.class) {
            Method meth = desc.getReadMethod();
            if(meth != null) {
                BooleanLocation location = (BooleanLocation) meth.invoke(instance);
                location.set(value);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        }else {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Boolean type");
        }        
    }
    
    /**
     * gets the boolean value for an attribute
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the boolean value 
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public boolean getBoolean(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        Method meth = desc.getReadMethod();
        if(meth == null) {
            throw new IllegalArgumentException("Attribute: " + attribute + " is not gettable");
        }
        if(desc.getPropertyType() == Boolean.TYPE || desc.getPropertyType() == Boolean.class) {
            Boolean b = (Boolean)meth.invoke(instance);
            return b.booleanValue();
        }else if(desc.getPropertyType() == BooleanLocation.class) {
            BooleanLocation location = (BooleanLocation) meth.invoke(instance);
            return location.get();
        }else {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Boolean type");
        }
    }
    
    /**
     * sets the value of a Number attribute
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the number value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Number type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setNumber(Object instance, String attribute, double value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() == Double.TYPE || desc.getPropertyType() == Double.class) {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                Double d = new Double(value);
                meth.invoke(instance, d);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        }else if(desc.getPropertyType() == Float.TYPE || desc.getPropertyType() == Float.class) {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                Float d = new Float(value);
                meth.invoke(instance, d);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        } else if(desc.getPropertyType() == DoubleLocation.class) {
            Method meth = desc.getReadMethod();
            if(meth != null) {
                DoubleLocation location = (DoubleLocation) meth.invoke(instance);
                location.set(value);  
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }                
        } else {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }

    }
    
    /**
     * gets the value of a Number attribute
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Number type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public double getNumber(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        Method meth = desc.getReadMethod();
        if (meth == null) {
            throw new IllegalArgumentException("Attribute: " + attribute + " is not gettable");
        }
        if (desc.getPropertyType() == Double.TYPE || desc.getPropertyType() == Double.class) {
            Double d = (Double) meth.invoke(instance);
            return d.doubleValue();
        } else if (desc.getPropertyType() == Float.TYPE || desc.getPropertyType() == Float.class) {
            Float d = (Float) meth.invoke(instance);
            return d.doubleValue();
        } else if (desc.getPropertyType() == DoubleLocation.class) {
            DoubleLocation location = (DoubleLocation) meth.invoke(instance);
            return location.get();
        } else {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
    }    
    
    /**
     * sets the value of an Integer attribute
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not an Integer type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setInteger(Object instance, String attribute, int value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() == Byte.TYPE || desc.getPropertyType() == Byte.class) {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                Byte b = new Byte((byte)value);
                meth.invoke(instance, b);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        } else if(desc.getPropertyType() == Character.TYPE || desc.getPropertyType() == Character.class) {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                Character b = Character.valueOf((char)value);
                meth.invoke(instance, b);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        } else if(desc.getPropertyType() == Short.TYPE || desc.getPropertyType() == Short.class) {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                Short b = Short.valueOf((short)value);
                meth.invoke(instance, b);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        } else if(desc.getPropertyType() == Integer.TYPE || desc.getPropertyType() == Integer.class) {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                Integer b = Integer.valueOf(value);
                meth.invoke(instance, b);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        } else if(desc.getPropertyType() == Long.TYPE || desc.getPropertyType() == Long.class) {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                Long b = Long.valueOf(value);
                meth.invoke(instance, b);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
        }else if(desc.getPropertyType() == IntLocation.class) {
            Method meth = desc.getReadMethod();
            IntLocation location = (IntLocation) meth.invoke(instance);
            location.set(value);
        } else {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }

    }
    
    /**
     * gets the value of an Integer attribute
     * @param instance the object instance
     * @param attribute the attribute name
     * @return teh value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not an Integer type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public int getInteger(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        Method meth = desc.getReadMethod();
        if (meth == null) {
            throw new IllegalArgumentException("Attribute: " + attribute + " is not gettable");
        }
        if (desc.getPropertyType() == Byte.TYPE || desc.getPropertyType() == Byte.class) {
            Byte b = (Byte) meth.invoke(instance);
            return b.intValue();
        } else if (desc.getPropertyType() == Character.TYPE || desc.getPropertyType() == Character.class) {
            Character b = (Character) meth.invoke(instance);
            return (int) b.charValue();
        } else if (desc.getPropertyType() == Short.TYPE || desc.getPropertyType() == Short.class) {
            Short b = (Short) meth.invoke(instance);
            return b.intValue();
        } else if (desc.getPropertyType() == Integer.TYPE || desc.getPropertyType() == Integer.class) {
            Integer b = (Integer) meth.invoke(instance);
            return b.intValue();
        } else if (desc.getPropertyType() == Long.TYPE || desc.getPropertyType() == Long.class) {
            Long b = (Long) meth.invoke(instance);
            return b.intValue();
        } else if (desc.getPropertyType() == IntLocation.class) {
            IntLocation location = (IntLocation) meth.invoke(instance);
            return location.get();
        } else {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
    }    
    
    /**
     * sets the value of an Object attribute
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the object value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setObject(Object instance, String attribute, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        Class propertyType = desc.getPropertyType();
        if(propertyType == DoubleLocation.class) {
            Method meth = desc.getReadMethod();
            if(meth == null) {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }            
            DoubleLocation location = (DoubleLocation)meth.invoke(instance);
            double v = 0.0;
            if(instance instanceof Number) {
                v = ((Number)value).doubleValue();
            }else {
                v = Double.valueOf(value.toString());
            }
            location.set(v);
        }else if (propertyType == IntLocation.class) {
            Method meth = desc.getReadMethod();
            if(meth == null) {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }            
            IntLocation location = (IntLocation)meth.invoke(instance);
            int v = 0;
            if(instance instanceof Number) {
                v = ((Number)value).intValue();
            }else {
                v = Integer.valueOf(value.toString());
            }
            location.set(v);
        }else if (propertyType == BooleanLocation.class) {
            Method meth = desc.getReadMethod();
            if(meth == null) {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
            BooleanLocation location = (BooleanLocation)meth.invoke(instance);
            boolean v = false;
            if(instance instanceof Boolean) {
                v = ((Boolean)value).booleanValue();
            }else {
                v = Boolean.valueOf(value.toString());
            }
            location.set(v);
        }else if(desc.getPropertyType() == ObjectLocation.class) {
            Method meth = desc.getReadMethod();
            if(meth == null) {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }
            ObjectLocation location = (ObjectLocation) meth.invoke(instance);
            location.set(value);
        } else {
            Method meth = desc.getWriteMethod();
            if(meth != null) {
                meth.invoke(instance, value);
            }else {
                throw new IllegalArgumentException("Attribute: " + attribute + " is not settable");
            }   
        }
    }
    
    /**
     * gets the value of an Object attribute
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown
     * @throws java.lang.reflect.InvocationTargetException
     */
    public Object getObject(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        Method meth = desc.getReadMethod();
        if(meth == null) {
            throw new IllegalArgumentException("Attribute: " + attribute + " is not gettable");
        }
        Class propertyType = desc.getPropertyType();
        if(propertyType == DoubleLocation.class) {
            DoubleLocation location = (DoubleLocation)meth.invoke(instance);
            return new Double(location.get());
        }else if (propertyType == IntLocation.class) {
            IntLocation location = (IntLocation)meth.invoke(instance);
            return new Integer(location.get());
        }else if (propertyType == BooleanLocation.class) {
            BooleanLocation location = (BooleanLocation)meth.invoke(instance);
            return(Boolean.valueOf(location.get()));
        }else if(desc.getPropertyType() == ObjectLocation.class) {
            ObjectLocation location = (ObjectLocation) meth.invoke(instance);
            return location.get();
        } else {
            return meth.invoke(instance);
        }
    }  
    
    /**
     * sets the value of an element in Sequence attribute
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the value
     * @param index the index in the sequence
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequence(Object instance, String attribute, Object value, int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        location.set(index,value);
    }
    
    /**
     * Add an item to the sequence
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void addToSequence(Object instance, String attribute, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        location.insert(value);
    }
    
    /**
     * delete an item from a sequence
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void deleteFromSequence(Object instance, String attribute, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        location.deleteValue(value);
    }     
    
    /**
     * delete an item from a sequence
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param index the index in the sequence
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void deleteFromSequence(Object instance, String attribute, int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        location.delete(index);
    }   
    
    /**
     * delete all items from a sequence
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void deleteAllFromSequence(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        location.deleteAll();
    }    
    
    /**
     * insert an item before the specified index
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the value
     * @param index the index in the sequence
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void insertBeforeSequence(Object instance, String attribute, Object value, int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        location.insertBefore(value, index);
    }    
    
    /**
     * insert an item after the specified index
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the value
     * @param index the index in the sequence
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void insertAfterSequence(Object instance, String attribute, Object value, int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        location.insertAfter(value, index);
    }  
    
    /**
     * get the size of a sequence 
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the size of a sequence
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public int getSequenceSize(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a Number type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        return location.get().size();
    }  
    
    
    /**
     * gets the value of an element in Sequence attribute
     * @param instance the object instance
     * @param attribute the attribute name
     * @param index the index within the sequence
     * @return the value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public Object getSequence(Object instance, String attribute, int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not an Object type: " + desc.getPropertyType());
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        return location.get().flatten().get(index);
    }   
    
    /**
     * Set a sequence 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the sequence
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequence(Object instance, String attribute, Sequence value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a SequenceLocation type");
        }
        Method meth = desc.getReadMethod();
        SequenceLocation location = (SequenceLocation) meth.invoke(instance);
        location.set(value);
    }
    
    /**
     * get a sequence location for an attribute
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the sequence location for an attribute
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    private SequenceLocation getSequenceLocation(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor desc = getDescriptor(attribute);
        if(desc.getPropertyType() != SequenceLocation.class) {
            throw new IllegalArgumentException(beanClass + "." + attribute + " is not a SequenceLocation type");
        }
        Method meth = desc.getReadMethod();
        return (SequenceLocation) meth.invoke(instance);
    }     
    
    /**
     * get a Sequence for an attribute
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the Sequence
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Sequence type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public Sequence getSequence(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SequenceLocation location = getSequenceLocation(instance, attribute);
        return location.get(); 
    }    
    
    /**
     * set the value of a sequence with an Object array, this has the effect of 
     * appending the array to the sequence.
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequenceArray(Object instance, String attribute, Object[] value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SequenceLocation location = getSequenceLocation(instance, attribute);
        for(int i = 0;i < value.length; i++) {
            location.insert(value[i]);
        }
    }
    
    /**
     * set the value of a sequence with an byte array, this has the effect of 
     * appending the array to the sequence. The elements are converted to an Integer type.
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequenceArray(Object instance, String attribute, byte[] value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Integer[] array = new Integer[value.length];
        for(int i = 0;i < value.length; i++) {
            array[i] = new Integer(value[i]);
        }
        setSequenceArray(instance, attribute, array);
    }
    
    /**
     * set the value of a sequence with an char array, this has the effect of 
     * appending the array to the sequence. The elements are converted to an Integer type.
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequenceArray(Object instance, String attribute, char[] value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Integer[] array = new Integer[value.length];
        for(int i = 0;i < value.length; i++) {
            array[i] = new Integer(value[i]);
        }
        setSequenceArray(instance, attribute, array);
    }    
    
    /**
     * set the value of a sequence with an short array, this has the effect of 
     * appending the array to the sequence. The elements are converted to an Integer type.
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequenceArray(Object instance, String attribute, short[] value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Integer[] array = new Integer[value.length];
        for(int i = 0;i < value.length; i++) {
            array[i] = new Integer(value[i]);
        }
        setSequenceArray(instance, attribute, array);
    }
    
    /**
     * set the value of a sequence with an int array, this has the effect of 
     * appending the array to the sequence. The elements are converted to an Integer type.
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequenceArray(Object instance, String attribute, int[] value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Integer[] array = new Integer[value.length];
        for(int i = 0;i < value.length; i++) {
            array[i] = new Integer(value[i]);
        }
        setSequenceArray(instance, attribute, array);
    }
    
    /**
     * set the value of a sequence with an long array, this has the effect of 
     * appending the array to the sequence. The elements are converted to an Integer type
     * There may be loss of precision with this method.
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequenceArray(Object instance, String attribute, long[] value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Integer[] array = new Integer[value.length];
        for(int i = 0;i < value.length; i++) {
            array[i] = new Integer((int)value[i]);
        }
        setSequenceArray(instance, attribute, array);
    }   
    
    /**
     * set the value of a sequence with an float array, this has the effect of 
     * appending the array to the sequence. The elements are converted to an Number type.
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequenceArray(Object instance, String attribute, float[] value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Double[] array = new Double[value.length];
        for(int i = 0;i < value.length; i++) {
            array[i] = new Double(value[i]);
        }
        setSequenceArray(instance, attribute, array);
    }   
    
    /**
     * set the value of a sequence with an double array, this has the effect of 
     * appending the array to the sequence. The elements are converted to an Number type.
     * @param instance the object instance
     * @param attribute the attribute name
     * @param value the array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute is not a Boolean type
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void setSequenceArray(Object instance, String attribute, double[] value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Double[] array = new Double[value.length];
        for(int i = 0;i < value.length; i++) {
            array[i] = new Double(value[i]);
        }
        setSequenceArray(instance, attribute, array);
    }    
    
    /**
     * get the value of a sequence as an Object array
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the Object array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown 
     * @throws java.lang.reflect.InvocationTargetException
     */
    public Object[] getSequenceArray(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Sequence seq = getSequence(instance, attribute).flatten();
        Object[] result = new Object[seq.size()];
        seq.toArray(result, 0);
        return result;
    } 
    
    /**
     * get the value of a sequence as an byte array
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the byte array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute cannot be converted to a byte array 
     * @throws java.lang.reflect.InvocationTargetException
     * @see    java.lang.reflect.Array
     */
    public byte[] getSequenceArrayByte(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] array = getSequenceArray(instance, attribute);
        byte[] result = new byte[array.length];
        for(int i = 0;i < array.length; i++) {
            if(array[i] instanceof Number) {
                result[i] = ((Number)array[i]).byteValue();
            }else {
                throw new IllegalArgumentException("Array Elements cannot be converted to byte");
            }
        }
        return result;
    }   
    
    /**
     * get the value of a sequence as a char array
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the char array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute cannot be converted to a char array 
     * @throws java.lang.reflect.InvocationTargetException
     * @see    java.lang.reflect.Array
     */
    public char[] getSequenceArrayChar(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] array = getSequenceArray(instance, attribute);
        char[] result = new char[array.length];
        for(int i = 0;i < array.length; i++) {
            if(array[i] instanceof Number) {
                result[i] = (char) ((Number)array[i]).intValue();
            }else {
                throw new IllegalArgumentException("Array Elements cannot be converted to char");
            }
        }
        return result;
    }   
    
    /**
     * get the value of a sequence as a short array
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the short array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute cannot be converted to a short array 
     * @throws java.lang.reflect.InvocationTargetException
     * @see    java.lang.reflect.Array
     */
    public short[] getSequenceArrayShort(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] array = getSequenceArray(instance, attribute);
        short[] result = new short[array.length];
        for(int i = 0;i < array.length; i++) {
            if(array[i] instanceof Number) {
                result[i] = ((Number)array[i]).shortValue();
            }else {
                throw new IllegalArgumentException("Array Elements cannot be converted to short");
            }
        }
        return result;
    }      
    
    /**
     * get the value of a sequence as an int array
     * @param instance the object instance
     * @param attribute the attribute name
     * @return int array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute cannot be converted to an int array 
     * @throws java.lang.reflect.InvocationTargetException
     * @see    java.lang.reflect.Array
     */
    public int[] getSequenceArrayInt(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] array = getSequenceArray(instance, attribute);
        int[] result = new int[array.length];
        for(int i = 0;i < array.length; i++) {
            if(array[i] instanceof Number) {
                result[i] = ((Number)array[i]).intValue();
            }else {
                throw new IllegalArgumentException("Array Elements cannot be converted to int");
            }
        }
        return result;
    }  
    
    /**
     * get the value of a sequence as a long array
     * @param instance the object instance
     * @param attribute the attribute name
     * @return the long array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute cannot be converted to a long array 
     * @throws java.lang.reflect.InvocationTargetException
     * @see    java.lang.reflect.Array
     */
    public long[] getSequenceArrayLong(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] array = getSequenceArray(instance, attribute);
        long[] result = new long[array.length];
        for(int i = 0;i < array.length; i++) {
            if(array[i] instanceof Number) {
                result[i] = ((Number)array[i]).longValue();
            }else {
                throw new IllegalArgumentException("Array Elements cannot be converted to long");
            }
        }
        return result;
    }  
    
    /**
     * get the value of a sequence as a dobule array
     * @param instance the object instance
     * @param attribute the attribute name
     * @return a double array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute cannot be converted to a double array 
     * @throws java.lang.reflect.InvocationTargetException
     * @see    java.lang.reflect.Array
     */
    public double[] getSequenceArrayDouble(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] array = getSequenceArray(instance, attribute);
        double[] result = new double[array.length];
        for(int i = 0;i < array.length; i++) {
            if(array[i] instanceof Number) {
                result[i] = ((Number)array[i]).doubleValue();
            }else {
                throw new IllegalArgumentException("Array Elements cannot be converted to double");
            }
        }
        return result;
    }   
    
    /**
     * get the value of a sequence as a float array
     * 
     * @param instance the object instance
     * @param attribute the attribute name
     * @return a float array
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.IllegalArgumentException if the attribute is unknown or
     *         the attribute cannot be converted to a float array 
     * @throws java.lang.reflect.InvocationTargetException
     * @see    java.lang.reflect.Array
     */
    public float[] getSequenceArrayFloat(Object instance, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] array = getSequenceArray(instance, attribute);
        float[] result = new float[array.length];
        for(int i = 0;i < array.length; i++) {
            if(array[i] instanceof Number) {
                result[i] = ((Number)array[i]).floatValue();
            }else {
                throw new IllegalArgumentException("Array Elements cannot be converted to float");
            }
        }
        return result;
    }     
    
}
