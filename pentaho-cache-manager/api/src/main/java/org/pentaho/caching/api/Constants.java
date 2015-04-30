package org.pentaho.caching.api;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author nhudak
 */
public class Constants {
  public static final String PENTAHO_CACHE_PROVIDER = "pentaho.cache.provider";

  public static Map<String, String> convertDictionary( Dictionary<String, ?> dictionary ) {
    Map<String, String> properties = Maps.newHashMapWithExpectedSize( dictionary.size() );
    for ( Enumeration<String> keys = dictionary.keys(); keys.hasMoreElements(); ) {
      String key = keys.nextElement();
      properties.put( key, String.valueOf( dictionary.get( key ) ) );
    }
    return properties;
  }

  public static <T> T unwrap( Object object, Class<T> clazz ) {
    Preconditions.checkArgument( clazz.isInstance( object ), "%s cannot be cast to %s", object, clazz );
    return clazz.cast( object );
  }

  public enum SystemTemplate {
    DEFAULT( "default", new Function<Map<String, String>, List<String>>() {
      @Override public List<String> apply( Map<String, String> input ) {
        return Collections.emptyList();
      }
    } );

    private final String id;
    private final Function<Map<String, String>, List<String>> minimumRequirements;

    SystemTemplate( String id, Function<Map<String, String>, List<String>> minimumRequirements ) {
      this.id = id;
      this.minimumRequirements = minimumRequirements;
    }

    public String getId() {
      return id;
    }

    public List<String> checkMinimumRequirements( Map<String, String> template ) {
      return minimumRequirements.apply( template );
    }
  }
}
