package com.jwattsuk.sample.main;

import com.jwattsuk.sample.exception.ServiceInitialiseException;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.server.Server;

public class SampleServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(SampleServiceApplication.class);

    public static void main(String[] args) throws Exception {

        LOG.info("Starting Sample Service");

        final var server = createFromXml();
        server.start();

    }

    private static Server createFromXml() {
        // To Do - Add logging config
        LOG.info("Reading Jetty Configuration from jetty-sample-all.xml");
        try (var resource = Resource.newResource(
                SampleServiceApplication.class.getClassLoader().getResource("jetty-sample-all.xml"))) {
            var config = new XmlConfiguration(resource);
            return (Server) config.configure();
        } catch (Exception e) {
            throw new ServiceInitialiseException(e);
        }
    }
}