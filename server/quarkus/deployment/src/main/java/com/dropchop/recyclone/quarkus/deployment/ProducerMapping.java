package com.dropchop.recyclone.quarkus.deployment;

@SuppressWarnings("unused")
public class ProducerMapping {

  private final String ifaceClass;
  private final String implClass;
  private final Class<?> selectorClass;
  private final Class<?> rootIface;

  public ProducerMapping(String ifaceClass, String implClass, Class<?> selectorClass, Class<?> rootIface) {
    this.ifaceClass = ifaceClass;
    this.implClass = implClass;
    this.selectorClass = selectorClass;
    this.rootIface = rootIface;
  }

  public String getIfaceClass() {
    return ifaceClass;
  }

  public String getImplClass() {
    return implClass;
  }

  public Class<?> getSelectorClass() {
    return selectorClass;
  }

  public Class<?> getRootIface() {
    return rootIface;
  }

  @Override
  public String toString() {
    return "ProducerMapping{" +
        "ifaceClass='" + ifaceClass + '\'' +
        ", implClass='" + implClass + '\'' +
        ", selectorClass=" + selectorClass +
        ", rootIface=" + rootIface +
        '}';
  }
}
