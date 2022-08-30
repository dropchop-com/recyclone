package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.filter.TokenFilter;

import java.io.IOException;

/**
 * Taken from <a href="https://github.com/HubSpot/jackson-jaxrs-propertyfiltering">jackson-jaxrs-propertyfiltering</a>
 *
 * @author Jonathan Haber
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 08. 22.
 */
public class PropertyFilteringTokenContext extends JsonStreamContext {

  /**
   * Parent context for this context; null for root context.
   */
  private final PropertyFilteringTokenContext _parent;

    /*
    /**********************************************************
    /* Simple instance reuse slots; speed up things
    /* a bit (10-15%) for docs with lots of small
    /* arrays/objects
    /**********************************************************
     */

  protected PropertyFilteringTokenContext _child;

    /*
    /**********************************************************
    /* Location/state information
    /**********************************************************
     */
  protected int _level;

  protected int _maxLevel;

  protected String _contentDetails;

  protected Object _subject;

  /**
   * Name of the field of which value is to be parsed; only
   * used for OBJECT contexts
   */
  protected String _currentName;

  /**
   * Filter to use for items in this state (for properties of Objects,
   * elements of Arrays, and root-level values of root context)
   */
  protected TokenFilter _filter;

  /**
   * Flag that indicates that start token has been read/written,
   * so that matching close token needs to be read/written as well
   * when context is getting closed.
   */
  protected boolean _startHandled;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

  protected PropertyFilteringTokenContext(int type, PropertyFilteringTokenContext parent,
                                          TokenFilter filter, boolean startHandled, Object subject,
                                          Integer maxLevel, String contentDetails) {
    _type = type;
    _parent = parent;
    _filter = filter;
    _index = -1;
    _contentDetails = contentDetails;
    _maxLevel = maxLevel;
    _subject = subject;
    _level = parent != null ? parent._level + 1 : 1;
    _startHandled = startHandled;
  }

  protected PropertyFilteringTokenContext reset(int type, TokenFilter filter, boolean startWritten) {
    _type = type;
    _filter = filter;
    _index = -1;
    _currentName = null;
    _startHandled = startWritten;
    return this;
  }

    /*
    /**********************************************************
    /* Factory methods
    /**********************************************************
     */

  public static PropertyFilteringTokenContext createRootContext(TokenFilter filter, Object subject,
                                                                Integer maxLevel, String contentDetails) {
    // true -> since we have no start/end marker, consider start handled
    return new PropertyFilteringTokenContext(TYPE_ROOT, null, filter, true, subject, maxLevel, contentDetails);
  }

  public PropertyFilteringTokenContext createChildArrayContext(TokenFilter filter, Object subject, boolean writeStart) {
    PropertyFilteringTokenContext ctxt = _child;
    if (ctxt == null) {
      _child = ctxt = new PropertyFilteringTokenContext(
        TYPE_ARRAY, this, filter, writeStart, subject, this._maxLevel, this._contentDetails
      );
      return ctxt;
    }
    return ctxt.reset(TYPE_ARRAY, filter, writeStart);
  }

  public PropertyFilteringTokenContext createChildObjectContext(TokenFilter filter, Object subject, boolean writeStart) {
    PropertyFilteringTokenContext ctxt = _child;
    if (ctxt == null) {
      _child = ctxt = new PropertyFilteringTokenContext(
        TYPE_ARRAY, this, filter, writeStart, subject, this._maxLevel, this._contentDetails
      );
      return ctxt;
    }
    return ctxt.reset(TYPE_OBJECT, filter, writeStart);
  }

    /*
    /**********************************************************
    /* State changes
    /**********************************************************
     */

  public TokenFilter setFieldName(String name) {
    _currentName = name;
    return _filter;
  }

  public PropertyFilteringTokenContext closeArray(JsonGenerator gen) throws IOException {
    if (_startHandled) {
      gen.writeEndArray();
    }
    return _parent;
  }

  public PropertyFilteringTokenContext closeObject(JsonGenerator gen) throws IOException {
    if (_startHandled) {
      gen.writeEndObject();
    }
    return _parent;
  }

    /*
    /**********************************************************
    /* Accessors, mutators
    /**********************************************************
     */

  @Override
  public Object getCurrentValue() {
    return null;
  }

  @Override
  public void setCurrentValue(Object v) {}

  @Override
  public final PropertyFilteringTokenContext getParent() {
    return _parent;
  }

  @Override
  public final String getCurrentName() {
    return _currentName;
  }

  // @since 2.9
  @Override
  public boolean hasCurrentName() {
    return _currentName != null;
  }

  public TokenFilter getFilter() {
    return _filter;
  }

  public int getLevel() {
    return _level;
  }

  // // // Internally used abstract methods

  protected void appendDesc(StringBuilder sb) {
    if (_parent != null) {
      _parent.appendDesc(sb);
    }
    if (_type == TYPE_OBJECT) {
      sb.append('{');
      if (_currentName != null) {
        sb.append('"');
        // !!! TODO: Name chars should be escaped?
        sb.append(_currentName);
        sb.append('"');
      } else {
        sb.append('?');
      }
      sb.append('}');
    } else if (_type == TYPE_ARRAY) {
      sb.append('[');
      sb.append(getCurrentIndex());
      sb.append(']');
    } else {
      // nah, ROOT:
      sb.append("/");
    }
  }

  // // // Overridden standard methods

  /**
   * Overridden to provide developer writeable "JsonPath" representation
   * of the context.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(64);
    appendDesc(sb);
    return sb.toString();
  }
}
