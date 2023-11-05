package dev.nucleoid.backend.web;

import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.server.session.*;

import javax.sql.DataSource;

public class SessionUtil {
    public static SessionHandler sqlSessionHandler(DataSource dataSource) {
        SessionHandler sessionHandler = new SessionHandler();
        SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        sessionCache.setSessionDataStore(
                jdbcDataStoreFactory(dataSource).getSessionDataStore(sessionHandler)
        );
        sessionHandler.setSessionCache(sessionCache);
        sessionHandler.setHttpOnly(true);
        sessionHandler.setSameSite(HttpCookie.SameSite.LAX);
        return sessionHandler;
    }

    private static JDBCSessionDataStoreFactory jdbcDataStoreFactory(DataSource dataSource) {
        DatabaseAdaptor databaseAdaptor = new DatabaseAdaptor();
         databaseAdaptor.setDatasource(dataSource);
        JDBCSessionDataStoreFactory jdbcSessionDataStoreFactory = new JDBCSessionDataStoreFactory();
        jdbcSessionDataStoreFactory.setDatabaseAdaptor(databaseAdaptor);
        return jdbcSessionDataStoreFactory;
    }
}
