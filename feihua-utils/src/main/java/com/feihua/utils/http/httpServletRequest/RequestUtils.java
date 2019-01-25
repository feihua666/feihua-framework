package com.feihua.utils.http.httpServletRequest;

import com.feihua.utils.io.FileUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import com.feihua.utils.json.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.feihua.utils.io.FileUtils.*;

/**
 * getContextPath = /context // 部署到ROOT为空""
 * getPathInfo = null
 * getQueryString = null
 * getRequestURI = /context/news/main/111.html
 * getServletPath = /news/main/111.html
 * getRemoteAddr = 0:0:0:0:0:0:0:1
 * getRemoteAddr1 = 0:0:0:0:0:0:0:1
 * getLocalAddr = 0:0:0:0:0:0:0:1
 * getRequestURL = http://localhost:8080/context/news/main/111.html
 * protocol = HTTP/1.1
 * getServerPort = 8080
 * getScheme = http
 * getServerName = localhost
 * getServletContext = org.apache.catalina.core.ApplicationContextFacade@4f43c2d3
 * <p>
 * <p>
 * Created by yw on 2016/2/21.
 */
public class RequestUtils {
    private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    /**
     * 获取用户代理对象
     *
     * @param request
     *
     * @return
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    /**
     * 获取设备类型
     *
     * @param request
     *
     * @return
     */
    public static DeviceType getDeviceType(HttpServletRequest request) {
        return getUserAgent(request).getOperatingSystem().getDeviceType();
    }

    /**
     * 是否是PC
     *
     * @param request
     *
     * @return
     */
    public static boolean isComputer(HttpServletRequest request) {
        return DeviceType.COMPUTER.equals(getDeviceType(request));
    }

    /**
     * 是否是手机
     *
     * @param request
     *
     * @return
     */
    public static boolean isMobile(HttpServletRequest request) {
        return DeviceType.MOBILE.equals(getDeviceType(request));
    }

    /**
     * 是否是平板
     *
     * @param request
     *
     * @return
     */
    public static boolean isTablet(HttpServletRequest request) {
        return DeviceType.TABLET.equals(getDeviceType(request));
    }

    /**
     * 是否是手机和平板
     *
     * @param request
     *
     * @return
     */
    public static boolean isMobileOrTablet(HttpServletRequest request) {
        DeviceType deviceType = getDeviceType(request);
        return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
    }

    /**
     * 获取浏览类型
     *
     * @param request
     *
     * @return
     */
    public static Browser getBrowser(HttpServletRequest request) {
        return getUserAgent(request).getBrowser();
    }

    /**
     * 是否IE版本是否小于等于IE8
     *
     * @param request
     *
     * @return
     */
    public static boolean isLteIE8(HttpServletRequest request) {
        Browser browser = getBrowser(request);
        return Browser.IE5.equals(browser) || Browser.IE6.equals(browser)
                || Browser.IE7.equals(browser) || Browser.IE8.equals(browser);
    }

    /**
     * 获得用户远程地址,ip地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (!StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (!StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 获取取cookie
     *
     * @param request
     *
     * @return
     */
    public static Cookie[] getCookies(HttpServletRequest request) {
        return request.getCookies();
    }

    /**
     * 根据cookie名获取
     *
     * @param name
     * @param request
     *
     * @return
     */
    public static Cookie getCookieByName(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        // 如果是异步请求或是手机端，则直接返回信息
        return ((accept != null && accept.indexOf("application/json") != -1
                || (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
        ));
    }

    /**
     * spring环境中获取当前请求对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获得站点url
     *
     * @return
     */
    public static String getWebUrl() {
        final HttpServletRequest request = getRequest();
        String url = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80) {
            url += ":" + request.getServerPort();
        }
        url += request.getContextPath();
        return url;
    }

    /**
     * 获取headers
     *
     * @param request
     *
     * @return
     */
    public static String getHeaderText(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        try {
            return JSONUtils.obj2json(map);
        } catch (Exception e) {
            logger.error("getHeaderText", e);
        }
        return null;

    }

    public static boolean isStaticFile(HttpServletRequest request) {
        //线程中调用获取不到，作不是静态文件处理
        if (request == null) {
            return false;
        }
        String uri = request.getRequestURI();
        String staticFiles = ".css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk";

        if (StringUtils.endsWithAny(uri, staticFiles.split(","))) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前访问的域名
     *
     * @param request
     * @param includeScheme 是否包含scheme
     * @param includePort   是否包含接口
     *
     * @return http://localhost:8080
     */
    public static String getDomain(HttpServletRequest request, boolean includeScheme, boolean includePort) {
        String _scheme = request.getScheme();
        String _serverName = request.getServerName();
        int _port = request.getServerPort();
        String r = _serverName;
        if (includeScheme) {
            r = _scheme + "://" + r;
        }
        if (includePort) {
            r = r + ":" + _port;
        }
        return r;
    }

    /**
     * @param request
     * @param includeContextPath
     *
     * @return
     */
    public static String[] resolveRequestURI(HttpServletRequest request, boolean includeContextPath) {
        String[] r = null;
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        String siteRequestUri = requestURI;
        if (!includeContextPath && !StringUtils.isEmpty(contextPath)) {
            siteRequestUri = siteRequestUri.substring(contextPath.length());
        }
        r = siteRequestUri.split("/");
        return r;
    }

    /**
     * 获取当前项目的实际地址存放路径
     *
     * @param request
     *
     * @return
     */
    public static String getWebappRealPath(HttpServletRequest request) {
        return RequestUtils.getRequest().getSession().getServletContext().getRealPath("");
    }


    /**
     * 将路径中的路径分隔符校正为斜杠分隔符
     *
     * @param path
     *
     * @return
     */
    public static String convertToSlash(String path) {
        String r = path;
        while (r.contains(slash_double) || r.contains(backslash_double)) {
            r = r.replace(slash_double, slash).replace(backslash_double, backslash);
        }
        r = r.replace(backslash, slash);

        return r;
    }

    /**
     * 将path以斜杠分隔符开始
     *
     * @param path
     *
     * @return
     */
    public static String wrapStartSlash(String path) {
        String r = path;
        while (r.startsWith(slash_double) || r.startsWith(backslash_double)) {
            r = slash + r.substring(2);
        }
        if (r.startsWith(slash) || r.startsWith(backslash)) {
            r = slash + r.substring(1);
        } else {
            r = slash + r;
        }
        return r;
    }

    /**
     * 将path以斜杠分隔符结尾
     *
     * @param path
     *
     * @return
     */
    public static String wrapEndSlash(String path) {
        String r = path;
        while (r.endsWith(slash_double) || r.endsWith(backslash_double)) {
            r = r.substring(0, r.length() - 2) + slash;
        }
        if (r.endsWith(slash) || r.endsWith(backslash)) {
            r = r.substring(0, r.length() - 1) + slash;
        } else {
            r = r + slash;
        }
        return r;
    }

    public static String unwrapStartSlash(String path) {
        String r = wrapStartSlash(path);
        return r.substring(1);
    }

    public static String unwrapEndSlash(String path) {
        String r = wrapEndSlash(path);
        return r.substring(0, r.length() - 1);
    }
}
