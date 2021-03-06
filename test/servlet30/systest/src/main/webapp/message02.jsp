<%--

    Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ page contentType="text/html"
%><%@ page import="java.util.Locale"
%><%@ page import="jakarta.faces.FactoryFinder"
%><%@ page import="jakarta.faces.application.Application"
%><%@ page import="jakarta.faces.application.ApplicationFactory"
%><%@ page import="jakarta.faces.application.FacesMessage"
%><%@ page import="jakarta.faces.context.FacesContext"
%><%@ page import="com.sun.faces.util.MessageFactory"
%><%@ page import="jakarta.faces.component.UIViewRoot,jakarta.faces.render.RenderKitFactory"
%><%

    // Initialize list of message ids
    String list[] = {
          // PENDING(craigmcc) - put message ids here
    };

    // Acquire the FacesContext instance for this request
    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (facesContext == null) {
        out.println("/message02.jsp FAILED - No FacesContext returned");
        return;
    }

    ApplicationFactory afactory = (ApplicationFactory)
          FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    Application appl = afactory.getApplication();
    if (appl == null) {
        out.println("/message02.jsp FAILED - No Application returned");
        return;
    }

    UIViewRoot root = (UIViewRoot)
          appl.createComponent(UIViewRoot.COMPONENT_TYPE);
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    facesContext.setViewRoot(root);

    facesContext.setViewRoot(root);
    // Acquire our Application instance

    // Check message identifiers that should be present (en_US)
    facesContext.getViewRoot().setLocale(new Locale("en", "US"));
    for (int i = 0; i < list.length; i++) {
        FacesMessage message = MessageFactory.getMessage(facesContext, list[i]);
        if (message == null) {
            out.println("/message02.jsp FAILED - Missing en_US message '" +
                        list[i] + "'");
            return;
        }
    }

    // Check message identifiers that should be present (fr_FR)
    facesContext.getViewRoot().setLocale(new Locale("fr", "FR"));
    for (int i = 0; i < list.length; i++) {
        FacesMessage message = MessageFactory.getMessage(facesContext, list[i]);
        if (message == null) {
            out.println("/message02.jsp FAILED - Missing fr_FR message '" +
                        list[i] + "'");
            return;
        }
    }

    // All tests passed
    out.println("/message02.jsp PASSED");

%>
