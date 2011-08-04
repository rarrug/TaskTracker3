package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * May be used for redefinition methods of some actions
 * @author parkhomchuk
 */
public abstract class TaskAction implements IAction {
    public abstract String perform(HttpServletRequest request, HttpServletResponse response);
}
