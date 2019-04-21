package dmcs.ignite.session.conf;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class IgniteConfigurationBeans {

    @Bean
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setPeerClassLoadingEnabled(false);

        igniteConfiguration.setIncludeEventTypes(
                org.apache.ignite.events.EventType.EVT_TASK_STARTED,
                org.apache.ignite.events.EventType.EVT_TASK_FINISHED,
                org.apache.ignite.events.EventType.EVT_TASK_FAILED,
                org.apache.ignite.events.EventType.EVT_TASK_TIMEDOUT,
                org.apache.ignite.events.EventType.EVT_TASK_SESSION_ATTR_SET,
                org.apache.ignite.events.EventType.EVT_TASK_REDUCED,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_PUT,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_READ,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_REMOVED);

        TcpDiscoveryMulticastIpFinder tcpDiscoveryMulticastIpFinder = new TcpDiscoveryMulticastIpFinder();
        tcpDiscoveryMulticastIpFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryMulticastIpFinder);
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);

        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("session-cache");
        cacheConfiguration.setCacheMode(CacheMode.REPLICATED);

        igniteConfiguration.setCacheConfiguration(cacheConfiguration);

        return igniteConfiguration;
    }

    @Bean("igniteInstance")
    public Ignite igniteInstance(IgniteConfiguration igniteConfiguration) {
        Ignite start = Ignition.start(igniteConfiguration);
        start.active(true);
        return start;
    }
}
