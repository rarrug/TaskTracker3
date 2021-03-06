package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Actions interface
 */
public interface IAction {

    /* Message types consts */
    int SUCCESS_MESS = 1;
    int FAIL_MESS = 2;

    /**
     * Process some request and do some actions
     * @param request Request from page
     * @param response Response to page
     * @return Redirected page adress
     */
    public String perform(HttpServletRequest request, HttpServletResponse response);
}
