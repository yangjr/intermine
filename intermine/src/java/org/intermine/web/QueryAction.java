package org.flymine.web;

/*
 * Copyright (C) 2002-2003 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import org.flymine.metadata.Model;
import org.flymine.metadata.ClassDescriptor;
import org.flymine.metadata.presentation.DisplayModel;
import org.flymine.metadata.presentation.DisplayClassDescriptor;
import org.flymine.objectstore.query.*;
import org.flymine.objectstore.query.presentation.QueryCreator;


/**
 * Implementation of <strong>Action</strong> that runs a Query
 *
 * @author Andrew Varley
 */

public class QueryAction extends LookupDispatchAction
{
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return an ActionForward object defining where control goes next
     *
     * @exception Exception if the application business logic throws
     *  an exception
     */
    public ActionForward submit(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {

        // Extract attributes we will need
        MessageResources messages = getResources(request);
        HttpSession session = request.getSession();

        QueryForm queryForm = (QueryForm) form;
        ClassDescriptor cld = ((DisplayClassDescriptor) session.getAttribute("cld")).getClassDescriptor();

        Query query = (Query) session.getAttribute("query");
        if (query == null) {
            query = new Query();
        }

        try {
            QueryCreator qc = new QueryCreator(query);
            qc.generateConstraints(cld.getName(), queryForm.getFields());
            session.setAttribute("query", query);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            request.setAttribute("exception", e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("buildquery"));
    }


    /**
     * Distributes the actions to the necessary methods, by providing a Map from action to
     * the name of a method.
     *
     * @return a Map
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("button.submit", "submit");
        map.put("button.view", "view");
        return map;
    }

}

