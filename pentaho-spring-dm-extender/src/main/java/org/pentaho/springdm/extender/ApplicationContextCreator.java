package org.pentaho.springdm.extender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.context.DelegatedExecutionOsgiBundleApplicationContext;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;
import org.springframework.osgi.extender.OsgiApplicationContextCreator;
import org.springframework.osgi.extender.support.ApplicationContextConfiguration;
import org.springframework.osgi.extender.support.DefaultOsgiApplicationContextCreator;
import org.springframework.osgi.extender.support.scanning.ConfigurationScanner;
import org.springframework.osgi.extender.support.scanning.DefaultConfigurationScanner;
import org.springframework.osgi.util.OsgiStringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Created by nbaker on 7/20/16.
 */
public class ApplicationContextCreator implements OsgiApplicationContextCreator {

  private static final Log log;
  private ConfigurationScanner configurationScanner = new DefaultConfigurationScanner();

  public ApplicationContextCreator() {
  }

  public DelegatedExecutionOsgiBundleApplicationContext createApplicationContext( BundleContext bundleContext )
      throws Exception {
    Bundle bundle = bundleContext.getBundle();
    ApplicationContextConfiguration config = new ApplicationContextConfiguration( bundle, this.configurationScanner );
    if ( log.isTraceEnabled() ) {
      log.trace(
          "Created configuration " + config + " for bundle " + OsgiStringUtils.nullSafeNameAndSymName( bundle ) );
    }

    if ( !config.isSpringPoweredBundle() ) {
      return null;
    } else {
      log.info( "Discovered configurations " + ObjectUtils.nullSafeToString( config.getConfigurationLocations() )
          + " in bundle [" + OsgiStringUtils.nullSafeNameAndSymName( bundle ) + "]" );
      PentahoOsgiBundleXmlApplicationContext sdoac = new PentahoOsgiBundleXmlApplicationContext( config.getConfigurationLocations() );
      sdoac.setBundleContext( bundleContext );
      sdoac.setPublishContextAsService( config.isPublishContextAsService() );

      return sdoac;
    }
  }

  public void setConfigurationScanner( ConfigurationScanner configurationScanner ) {
    Assert.notNull( configurationScanner );
    this.configurationScanner = configurationScanner;
  }

  static {
    log = LogFactory.getLog( DefaultOsgiApplicationContextCreator.class );
  }
}
