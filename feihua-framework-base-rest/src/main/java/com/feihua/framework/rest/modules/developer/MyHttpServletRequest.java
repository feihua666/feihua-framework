package com.feihua.framework.rest.modules.developer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by yangwei
 * Created at 2018/3/22 13:29
 */
public class MyHttpServletRequest extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public MyHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    private String myMethod = "GET";
    private String myRequestURI;
    private String myRequestURL;

    @Override
    public String getMethod() {
        return this.getMyMethod();
    }

    @Override
    public String getRequestURI() {
        return this.getMyRequestURI();
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(this.getMyRequestURL());
    }

    @Override
    public String getServletPath() {
        return getRequestURI();
    }

    public String getMyMethod() {
        return myMethod;
    }

    public void setMyMethod(String myMethod) {
        this.myMethod = myMethod;
    }

    public String getMyRequestURI() {
        return myRequestURI;
    }

    public void setMyRequestURI(String myRequestURI) {
        this.myRequestURI = myRequestURI;
    }

    public String getMyRequestURL() {
        return myRequestURL;
    }

    public void setMyRequestURL(String myRequestURL) {
        this.myRequestURL = myRequestURL;
    }
}
