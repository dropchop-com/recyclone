package com.dropchop.recyclone.quarkus.deployment.shiro;

import com.dropchop.shiro.cdi.DefaultShiroEnvironmentProvider;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import com.dropchop.shiro.cdi.ShiroEnvironment;
import com.dropchop.shiro.filter.*;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.MemberValue;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 03. 24.
 */
public class ShiroProcessor {

  private static final Logger LOG = Logger.getLogger(ShiroProcessor.class);

  private static final DotName OLD_LOGICAL = DotName.createSimple(
      "com.dropchop.recyclone.base.api.model.security.annotations.Logical"
  );

  private static final DotName NEW_LOGICAL = DotName.createSimple(
      "org.apache.shiro.authz.annotation.Logical"
  );

  private static final DotName DC_PERMISSION_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.base.api.model.security.annotations.RequiresPermissions"
  );

  private static final DotName SHIRO_PERMISSION_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresPermissions"
  );

  private static final DotName DC_ROLE_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.base.api.model.security.annotations.RequiresRoles"
  );

  private static final DotName SHIRO_ROLE_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresRoles"
  );

  private static final DotName DC_USER_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.base.api.model.security.annotations.RequiresUser"
  );

  private static final DotName SHIRO_USER_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresUser"
  );

  private static final DotName DC_GUEST_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.base.api.model.security.annotations.RequiresGuest"
  );

  private static final DotName SHIRO_GUEST_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresGuest"
  );

  private static final DotName DC_AUTH_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.base.api.model.security.annotations.RequiresAuthentication"
  );

  private static final DotName SHIRO_AUTH_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresAuthentication"
  );

  private static final Map<DotName, DotName> replacements = Map.of(
      DC_PERMISSION_ANNO, SHIRO_PERMISSION_ANNO,
      DC_ROLE_ANNO, SHIRO_ROLE_ANNO,
      DC_USER_ANNO, SHIRO_USER_ANNO,
      DC_GUEST_ANNO, SHIRO_GUEST_ANNO,
      DC_AUTH_ANNO, SHIRO_AUTH_ANNO
  );

  @BuildStep
  public void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(DefaultShiroEnvironmentProvider.class)
            .setUnremovable()
            .setDefaultScope(DotNames.APPLICATION_SCOPED).build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ShiroEnvironment.class)
            .setUnremovable()
            .setDefaultScope(DotNames.APPLICATION_SCOPED).build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ShiroAuthorizationService.class)
            .setUnremovable()
            .setDefaultScope(DotNames.APPLICATION_SCOPED).build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ApiKeyHttpAuthenticationFilter.class)
            .setDefaultScope(DotNames.APPLICATION_SCOPED)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(JwtAuthenticationFilter.class)
            .setDefaultScope(DotNames.APPLICATION_SCOPED)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(JwtEveryResponseFilter.class)
            .setDefaultScope(DotNames.APPLICATION_SCOPED)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(UuidAuthenticationFilter.class)
            .setDefaultScope(DotNames.APPLICATION_SCOPED)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(BasicHttpAuthenticationFilter.class)
            .setDefaultScope(DotNames.APPLICATION_SCOPED)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(BearerHttpAuthenticationFilter.class)
            .setDefaultScope(DotNames.APPLICATION_SCOPED)
            .setUnremovable()
            .build()
    );
  }

  private void transformAnnotation(ConstPool constPool, AnnotationsAttribute attr,
                                   String oldAnnoName, String newAnnoName) {
    if (attr != null) {
      Annotation[] annotations = attr.getAnnotations();
      for (int i = 0; i < annotations.length; i++) {
        if (annotations[i].getTypeName().equals(oldAnnoName)) {
          Annotation oldAnnotation = annotations[i];
          Annotation newAnnotation = new Annotation(newAnnoName, constPool);
          if (oldAnnotation.getMemberNames() != null) {
            for (String member : oldAnnotation.getMemberNames()) {
              MemberValue value = oldAnnotation.getMemberValue(member);
              if (value instanceof EnumMemberValue enumValue && enumValue.getType().equals(OLD_LOGICAL.toString())) {
                enumValue.setType(NEW_LOGICAL.toString());
              }
              newAnnotation.addMemberValue(member, value);
            }
          }
          annotations[i] = newAnnotation;
        }
      }
      attr.setAnnotations(annotations);
    }
  }

  /**
   * Replaces all Recyclone security Annotations with Apache Shiro's in class files
   */
  @BuildStep
  public void replaceRecycloneToShiroAnnotationsJavaassist(CombinedIndexBuildItem combinedIndexBuildItem,
                                                           BuildProducer<BytecodeTransformerBuildItem> transformers) {
    IndexView index = combinedIndexBuildItem.getIndex();

    // Find classes annotated with recyclone annos
    Set<DotName> classesToChange = new LinkedHashSet<>();
    for (DotName dcAnno : replacements.keySet()) {
      for (AnnotationInstance dcAnnoInstance : index.getAnnotations(dcAnno)) {
        if (dcAnnoInstance.target().kind() == AnnotationTarget.Kind.CLASS) {
          ClassInfo classInfo = dcAnnoInstance.target().asClass();
          classesToChange.add(classInfo.name());
        }
        if (dcAnnoInstance.target().kind() == AnnotationTarget.Kind.METHOD) {
          ClassInfo classInfo = dcAnnoInstance.target().asMethod().declaringClass();
          classesToChange.add(classInfo.name());
        }
      }
    }
    LOG.debugf("Will replace Recyclone security Annotations with Apache Shiro's in [%s]", classesToChange);
    for (DotName dcAnnoClass : classesToChange) {
      BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
          .setCacheable(true)
          .setPriority(1)
          .setClassToTransform(dcAnnoClass.toString())
          .setInputTransformer((className, bytes) -> {
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ByteArrayClassPath(className, bytes));
            CtClass ctClass;
            try {
              ctClass = pool.get(className);
              ConstPool constPool = ctClass.getClassFile().getConstPool();

              for(Map.Entry<DotName, DotName> dotNameDotNameEntry : replacements.entrySet()) {
                String dcAnno = dotNameDotNameEntry.getKey().toString();
                String shiroAnno = dotNameDotNameEntry.getValue().toString();
                AnnotationsAttribute attr = (AnnotationsAttribute) ctClass.getClassFile()
                    .getAttribute(AnnotationsAttribute.visibleTag);
                transformAnnotation(constPool, attr, dcAnno, shiroAnno);
                for (CtMethod method : ctClass.getDeclaredMethods()) {
                  attr = (AnnotationsAttribute) method.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
                  transformAnnotation(constPool, attr, dcAnno, shiroAnno);
                }
              }
              if (ctClass.isFrozen()) {
                ctClass.defrost();
              }
              byte[] classBytes = ctClass.toBytecode();
              /*FileOutputStream fos;
              try {
                fos = new FileOutputStream(
                    "%s/projects/recyclone/%s.class".formatted(System.getProperty("user.home"), className)
                );
                fos.write(classBytes);
                fos.close();
              } catch (IOException e) {
                throw new RuntimeException(e);
              }*/
              ctClass.detach();
              return classBytes;
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }).build();
      transformers.produce(item);
    }
  }

  ////************************************** ASM annotation replacement - not working

  /*
  private static final String DC_LOGICAL =
      "Lcom/dropchop/recyclone/model/api/security/annotations/Logical;";
  private static final String SHIRO_LOGICAL =
      "Lorg/apache/shiro/authz/annotation/Logical;";

  private static final String DC_PERM_DESCRIPTOR =
      "Lcom/dropchop/recyclone/model/api/security/annotations/RequiresPermissions;";
  private static final String SHIRO_PERM_DESCRIPTOR =
      "Lorg/apache/shiro/authz/annotation/RequiresPermissions;";

  //@BuildStep
  public void replaceRecycloneToShiroAnnotations(BuildProducer<BytecodeTransformerBuildItem> transformers) {
    BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
        .setCacheable(true)
        .setPriority(1)
        .setClassToTransform("com.dropchop.recyclone.quarkus.it.rest.api.DummyResource")
        .setInputTransformer((className, bytes) -> {
          ClassReader cr = new ClassReader(bytes);
          ClassWriter cw = new ClassWriter(cr, 0);

          // ClassVisitor that adds an annotation to a specific method
          ClassVisitor cv = new ClassVisitor(Gizmo.ASM_API_VERSION, cw) {

            private AnnotationVisitor transformAnnotation(String descriptor, boolean visible,
                                                          AnnotationVisitor av) {
              if (descriptor.equals(DC_PERM_DESCRIPTOR)) {
                return new AnnotationVisitor(Gizmo.ASM_API_VERSION,
                    super.visitAnnotation(SHIRO_PERM_DESCRIPTOR, visible)) {
                  @Override
                  public void visit(String name, Object value) {
                    super.visit(name, value);
                  }

                  @Override
                  public AnnotationVisitor visitAnnotation(String name, String descriptor) {
                    return super.visitAnnotation(name, descriptor);
                  }

                  @Override
                  public AnnotationVisitor visitArray(String name) {
                    return super.visitArray(name);
                  }

                  @Override
                  public void visitEnum(String name, String descriptor, String value) {
                    if (descriptor.equals(DC_LOGICAL)) {
                      super.visitEnum(name, SHIRO_LOGICAL, value);
                    } else {
                      super.visitEnum(name, descriptor, value);
                    }
                  }
                };
              }
              return av;
            }

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
              return transformAnnotation(descriptor, visible, super.visitAnnotation(descriptor, visible));
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
              MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
              return new MethodVisitor(Gizmo.ASM_API_VERSION, mv) {
                @Override
                public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                  return transformAnnotation(descriptor, visible, super.visitAnnotation(descriptor, visible));
                }
              };
            }
          };
          cr.accept(cv, 0);
          FileOutputStream fos;
          try {
            fos = new FileOutputStream("/home/nikola/projects/recyclone/DummyResource.class");
            fos.write(cw.toByteArray());
            fos.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          return cw.toByteArray();
      }).build();
    transformers.produce(item);
  }*/

  ////************************************** Index annotation replacement

  private AnnotationInstance copyAnnoValues(Declaration declaration, DotName oldAnno, DotName newAnno) {
    AnnotationInstance oldAnnotation = declaration.annotation(oldAnno);
    AnnotationValue[] annoValues = oldAnnotation.values().toArray(new AnnotationValue[]{});
    //LOG.infof("Replacing annotation new [%s -> %s] in [%s]", oldAnno, newAnno, declaration);
    return AnnotationInstance.create(
        newAnno,
        declaration,
        annoValues
    );
  }

  Consumer<AnnotationTransformation.TransformationContext> getTransformationContextConsumer() {
    return context -> {
      Declaration declaration = context.declaration();
      if (declaration.hasAnnotation(DC_PERMISSION_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_PERMISSION_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_PERMISSION_ANNO, SHIRO_PERMISSION_ANNO
        ));
      } else if (declaration.hasAnnotation(DC_ROLE_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_ROLE_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_ROLE_ANNO, SHIRO_ROLE_ANNO
        ));
      } else if (declaration.hasAnnotation(DC_USER_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_USER_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_USER_ANNO, SHIRO_USER_ANNO
        ));
      } else if (declaration.hasAnnotation(DC_GUEST_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_GUEST_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_GUEST_ANNO, SHIRO_GUEST_ANNO
        ));
      } else if (declaration.hasAnnotation(DC_AUTH_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_AUTH_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_AUTH_ANNO, SHIRO_AUTH_ANNO
        ));
      }
    };
  }


  /**
   * Replaces or fakes-up all Recyclone security Annotations with Apache Shiro's in Jandex indices
   */
  @BuildStep
  void transformShiroAnnotation(
      BuildProducer<io.quarkus.arc.deployment.AnnotationsTransformerBuildItem> arcTransformer,
      BuildProducer<io.quarkus.resteasy.reactive.server.spi.AnnotationsTransformerBuildItem> restTransformer) {
    restTransformer.produce(
        new io.quarkus.resteasy.reactive.server.spi.AnnotationsTransformerBuildItem(AnnotationTransformation.builder()
          .whenDeclaration(d -> d.hasAnnotation(DC_PERMISSION_ANNO)
              || d.hasAnnotation(DC_ROLE_ANNO) || d.hasAnnotation(DC_USER_ANNO)
              || d.hasAnnotation(DC_GUEST_ANNO) || d.hasAnnotation(DC_AUTH_ANNO))
          .transform(getTransformationContextConsumer())
        )
    );

    arcTransformer.produce(
        new io.quarkus.arc.deployment.AnnotationsTransformerBuildItem(AnnotationTransformation.builder()
          .whenDeclaration(d -> d.hasAnnotation(DC_PERMISSION_ANNO)
              || d.hasAnnotation(DC_ROLE_ANNO) || d.hasAnnotation(DC_USER_ANNO)
              || d.hasAnnotation(DC_GUEST_ANNO) || d.hasAnnotation(DC_AUTH_ANNO))
          .transform(getTransformationContextConsumer())
        )
    );
  }

  /* I wanted to implement shiro JaxRS filter binding to a specific filter and pass annos to it but,
  This would be far more complex than current DynamicFeature, although I have all the information in build-time.

  Problem is: Binding specific method to a specific filter instance preconfigured
              with java.lang.Annotation expected in org.apache.shiro.authz.aop.AuthorizingAnnotationHandler.

  private static MethodInfo findDirectImplementation(ClassInfo classInfo, MethodInfo methodToFind) {
    for (MethodInfo method : classInfo.methods()) {
      if (methodsMatch(method, methodToFind)) {
        return method;
      }
    }
    return null;
  }

  private static boolean methodsMatch(MethodInfo method1, MethodInfo method2) {
    // Check method names
    if (!method1.name().equals(method2.name())) {
      return false;
    }

    // Check return types
    if (!method1.returnType().name().equals(method2.returnType().name())) {
      return false;
    }

    // Check parameter count
    List<MethodParameterInfo> params1 = method1.parameters();
    List<MethodParameterInfo> params2 = method2.parameters();
    if (params1.size() != params2.size()) {
      return false;
    }

    // Check parameter types
    for (int i = 0; i < params1.size(); i++) {
      if (!params1.get(i).type().name().equals(params2.get(i).type().name())) {
        return false;
      }
    }

    return true;
  }

  public static MethodInfo findImplementingMethod(IndexView index, ClassInfo apiClass,
                                                  MethodInfo apiMethod, ClassInfo implClass) {
    // Check if the implClass directly implements the method
    MethodInfo directImpl = findDirectImplementation(implClass, apiMethod);
    if (directImpl != null) {
      return directImpl;
    }

    // Check superclasses
    Type superClassType = implClass.superClassType();
    while (superClassType != null) {
      ClassInfo superClass = index.getClassByName(superClassType.name());
      if (superClass == null) {
        break;
      }

      directImpl = findDirectImplementation(superClass, apiMethod);
      if (directImpl != null) {
        return directImpl;
      }

      superClassType = superClass.superClassType();
    }

    // Check implemented interfaces
    List<Type> interfaces = implClass.interfaceTypes();
    for (Type interfaceType : interfaces) {
      ClassInfo interfaceInfo = index.getClassByName(interfaceType.name());
      if (interfaceInfo != null) {
        if (interfaceInfo.equals(apiClass)) {
          // This is the interface we're looking for
          return findDirectImplementation(implClass, apiMethod);
        }
        // Check if this interface extends our target interface
        MethodInfo interfaceImpl = findImplementingMethod(index, apiClass, apiMethod, interfaceInfo);
        if (interfaceImpl != null) {
          return findDirectImplementation(implClass, interfaceImpl);
        }
      }
    }

    return null;
  }

  @BuildStep
  void addRestShiroSecurity(
      CombinedIndexBuildItem cibi,
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<ContainerRequestFilterBuildItem> containerRequestFilterProducer) {
    IndexView indexView = cibi.getIndex();
    for (Map.Entry<String, RestClass> entry : restMappingBuildItem.getMapping().getApiClasses().entrySet()) {
      RestClass mapping = entry.getValue();
      ClassInfo apiClass = indexView.getClassByName(mapping.getApiClass());
      ClassInfo implClass = indexView.getClassByName(mapping.getImplClass());
      if (!apiClass.name().equals(DotName.createSimple("com.dropchop.recyclone.quarkus.it.rest.api.DummyResource"))) {
        break;
      }
      AnnotationInstance permAnno = null;
      if (apiClass.hasDeclaredAnnotation(SHIRO_PERMISSION_ANNO)) {
        permAnno = apiClass.annotation(SHIRO_PERMISSION_ANNO);
      }
      if (implClass.hasDeclaredAnnotation(SHIRO_PERMISSION_ANNO)) {
        permAnno = apiClass.annotation(SHIRO_PERMISSION_ANNO);
      }
      Map<String, MethodInfo> apiMethodInfoMap = new LinkedHashMap<>();
      for (MethodInfo method : apiClass.methods()) {
        String methodRef = JandexUtil.createUniqueMethodReference(apiClass, method);
        apiMethodInfoMap.put(methodRef, method);
      }

      for(Map.Entry<String, RestMethod> mEntry : mapping.getApiMethodMappings().entrySet()) {
        RestMethod method = mEntry.getValue();
        MethodInfo apiMethodInfo = apiMethodInfoMap.get(method.getMethodRef());
        MethodInfo implMethodInfo = findImplementingMethod(indexView, apiClass, apiMethodInfo, implClass);
        if (apiMethodInfo.hasAnnotation(SHIRO_PERMISSION_ANNO)) {
          permAnno = apiMethodInfo.annotation(SHIRO_PERMISSION_ANNO);
        }
        if (implMethodInfo.hasAnnotation(SHIRO_PERMISSION_ANNO)) {
          permAnno = apiMethodInfo.annotation(SHIRO_PERMISSION_ANNO);
        }
        LOG.infof("Registering PERMISSIONS filter: %s -> %s", implClass.name(), implMethodInfo.name());
        containerRequestFilterProducer.produce(
            new ContainerRequestFilterBuildItem.Builder(ShiroAuthorizationFilter2.class.getName())
                .setFilterSourceMethod(implMethodInfo)
                .setRegisterAsBean(true)
                .setPriority(Priorities.AUTHORIZATION)
                .build()
        );

      }
    }
  }*/
}
