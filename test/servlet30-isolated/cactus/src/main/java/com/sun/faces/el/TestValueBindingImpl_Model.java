/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

// TestValueBindingImpl_Model.java

package com.sun.faces.el;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.cactus.TestBean.Inner2Bean;
import com.sun.faces.cactus.TestBean.InnerBean;

import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;

/**
 * <B>TestValueBindingImpl_Model</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 */

public class TestValueBindingImpl_Model extends ServletFacesTestCase {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables
    ValueBinding valueBinding = null;
    
//
// Constructors and Initializers    
//

    public TestValueBindingImpl_Model() {
        super("TestValueBindingImpl");
    }


    public TestValueBindingImpl_Model(String name) {
        super(name);
    }
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//
    public ValueBinding create(String ref) throws Exception {
    	return (getFacesContext().getApplication().createValueBinding("#{" + ref + "}"));
    }
    
    public void setUp() {
        super.setUp();
        valueBinding = null;
    }


    public void tearDown() {
        valueBinding = null;
        super.tearDown();
    }


    public void testSet() throws Exception {
        FacesContext facesContext = getFacesContext();
        System.out.println("Testing setValue() with model bean in session ");
        TestBean testBean = new TestBean();
        InnerBean inner = new InnerBean();
        Inner2Bean innerInner = new Inner2Bean();
        Object result = null;

        getFacesContext().getExternalContext().getSessionMap().put("TestBean",
                                                                   testBean);
        boolean exceptionThrown = false;
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("TestBean.one");
        valueBinding.setValue(getFacesContext(), "one");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        InnerBean newInner = new InnerBean();
        valueBinding = this.create("TestBean.inner");
        valueBinding.setValue(getFacesContext(), newInner);
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result == newInner);
        
        // Test two levels of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("sessionScope.TestBean.inner.two");
        valueBinding.setValue(getFacesContext(), "two");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        Inner2Bean newInner2 = new Inner2Bean();
        valueBinding = this.create("TestBean.inner.inner2");
        valueBinding.setValue(getFacesContext(), newInner2);
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result == newInner2);
        
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("sessionScope.TestBean.inner.inner2");
        valueBinding.setValue(getFacesContext(), innerInner);
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        
        // Test three levels of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("sessionScope.TestBean.inner.inner2.three");
        valueBinding.setValue(getFacesContext(), "three");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
    }

    public void testSetWithNoCurlyBraces() throws Exception {
        FacesContext facesContext = getFacesContext();
        System.out.println("Testing setValue() with model bean in request ");
        TestBean testBean = new TestBean();
        InnerBean inner = new InnerBean();
        Inner2Bean innerInner = new Inner2Bean();

        facesContext.getExternalContext().getSessionMap().remove("TestBean");
        facesContext.getExternalContext().getRequestMap().put("TestBean",
                                                              testBean);

        // Test implicit scopes direct access to some scope objects should
        // throw an illegalArgumentException
        boolean gotException = false;
        try {
            valueBinding = this.create("header.header-one");
            valueBinding.setValue(getFacesContext(), testBean);
        } catch (javax.faces.el.EvaluationException pnf) {
            gotException = true;
        }
        assertTrue(gotException);

        // Test one level of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("TestBean.one");
        valueBinding.setValue(getFacesContext(), "one");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("requestScope.TestBean.inner");
        valueBinding.setValue(getFacesContext(), inner);
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        // Test two levels of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("requestScope.TestBean.inner.two");
        valueBinding.setValue(getFacesContext(), "two");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("requestScope.TestBean.inner.inner2");
        valueBinding.setValue(getFacesContext(), innerInner);
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        // Test three levels of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueBinding = this.create("requestScope.TestBean.inner.inner2.three");
        valueBinding.setValue(getFacesContext(), "three");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
    }


    public void testGet() throws Exception {
        FacesContext facesContext = getFacesContext();
        System.out.println("Testing getValue() with model bean in context");
        assertTrue(facesContext != null);
        TestBean testBeanResult = null, testBean = new TestBean();
        InnerBean inner = new InnerBean();
        Inner2Bean inner2 = new Inner2Bean();
        String result;

        // Init the beans
        testBean.setOne("one");
        inner.setTwo("two");
        inner2.setThree("three");
        inner.setInner2(inner2);
        testBean.setInner(inner);

        assertTrue(facesContext != null);
        assertTrue(facesContext.getExternalContext().getSession(false) != null);

        facesContext.getExternalContext().getRequestMap().remove("TestBean");
        facesContext.getExternalContext().getSessionMap().remove("TestBean");
        facesContext.getExternalContext().getApplicationMap().put("TestBean",
                                                                  testBean);

        // Test zero levels of nesting
        valueBinding = this.create("applicationScope.TestBean");
        testBeanResult = (TestBean) valueBinding.getValue(getFacesContext());
        assertTrue(testBeanResult != null);
        assertTrue(testBeanResult == testBean);

        // Test one level of nesting
        valueBinding = this.create("applicationScope.TestBean.one");
        result = (String) valueBinding.getValue(getFacesContext());
        assertTrue(result.equals("one"));

        valueBinding = this.create("applicationScope.TestBean.inner");
        inner = (InnerBean) valueBinding.getValue(getFacesContext());
        assertTrue(null != inner);

        // Test two levels of nesting
        valueBinding = this.create("applicationScope.TestBean.inner.two");
        result = (String) valueBinding.getValue(getFacesContext());
        assertTrue(result.equals("two"));

        valueBinding = this.create("applicationScope.TestBean.inner.inner2");
        inner2 = (Inner2Bean)
            valueBinding.getValue(getFacesContext());
        assertTrue(null != inner2);

        // Test three levels of nesting
        valueBinding = this.create("applicationScope.TestBean.inner.inner2.three");
        result = (String) valueBinding.getValue(getFacesContext());
        assertTrue(result.equals("three"));

    }


    public void testModelType() {
        /***************** PENDING(edburns):

         // Test model type
         System.out.println("Testing getModelType()");
         Class classType = null;
         String className = null;

         // Test zero levels of nesting
         classType = facesContext.getModelType("applicationScope.TestBean");
         assertTrue(classType != null);
         className = classType.getName();
         assertTrue(className.equals(testBean.getClass().getName()));

         classType = facesContext.getModelType("applicationScope.TestBean.inner.pin");
         assertTrue(classType != null);
         className = classType.getName();
         assertTrue(className.equals("java.lang.Integer"));

         classType = facesContext.getModelType("applicationScope.TestBean.inner.result");
         assertTrue(classType != null);
         className = classType.getName();
         assertTrue(className.equals("java.lang.Boolean"));

         classType = facesContext.getModelType("applicationScope.TestBean.one");
         assertTrue(classType != null);
         className = classType.getName();
         assertTrue(className.equals("java.lang.String"));
         *********************/
    }

} // end of class TestValueBindingImpl_Model
