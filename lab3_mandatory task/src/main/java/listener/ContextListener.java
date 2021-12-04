package listener;

import unilt.DBHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    private ServletContext context = null;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //get servlet context
        context = sce.getServletContext();
        DBHandler dbh = new DBHandler();
        context.setAttribute("dbh",dbh);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //remove database
        DBHandler dbh = (DBHandler) sce.getServletContext().getAttribute("dbh");
        dbh.close();
        context.removeAttribute("dbh");

    }
}
