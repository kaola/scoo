package com.example.retrofitlibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import okhttp3.ResponseBody;
import okio.Buffer;

public class Utils {
   static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

   private Utils() {

   }

   static RuntimeException methodError(Method method, String message, Object... args) {
      return methodError(method, null, message, args);
   }

   static RuntimeException methodError(
           Method method, @Nullable Throwable cause, String message, Object... args) {
      message = String.format(message, args);
      return new IllegalArgumentException(
              message
                      + "\n    for method "
                      + method.getDeclaringClass().getSimpleName()
                      + "."
                      + method.getName(),
              cause);
   }

   static RuntimeException parameterError(
           Method method, Throwable cause, int p, String message, Object... args) {
      return methodError(method, cause, message + " (parameter #" + (p + 1) + ")", args);
   }

   static RuntimeException parameterError(Method method, int p, String message, Object... args) {
      return methodError(method, message + " (parameter #" + (p + 1) + ")", args);
   }

   static Class<?> getRawType(Type type) {
      Objects.requireNonNull(type,"type == null");

      if (type instanceof Class<?>) {
         return (Class<?>) type;
      }

      if (type instanceof ParameterizedType) {
         ParameterizedType parameterizedType = (ParameterizedType) type;

         Type rawType = parameterizedType.getRawType();
         if (!(rawType instanceof Class)) throw  new IllegalArgumentException();
          return (Class<?>) rawType;
      }

      if (type instanceof GenericArrayType) {
         Type componentType = ((GenericArrayType) type).getGenericComponentType();
         return Array.newInstance(getRawType(componentType),0).getClass();
      }

      if (type instanceof TypeVariable) {
         return Object.class;
      }

      if (type instanceof WildcardType) {
         return getRawType(((WildcardType)type).getUpperBounds()[0]);
      }

      throw new IllegalArgumentException(
              "Expected a Class, ParameterizedType, or "
                      + "GenericArrayType, but <"
                      + type
                      + "> is of type "
                      + type.getClass().getName());
   }

   static boolean equals(Type a, Type b) {
      if (a == b) {
         return true;
      } else if (a instanceof  Class) {
         return a.equals(b);
      } else if (a instanceof ParameterizedType) {
         if (!(b instanceof ParameterizedType)) return false;
         ParameterizedType pa = (ParameterizedType) a;
         ParameterizedType pb = (ParameterizedType) b;
         Object ownerA = pa.getOwnerType();
         Object ownerB = pb.getOwnerType();
         return (ownerA == ownerB || (ownerA != null && ownerA.equals(ownerB)
                 && pa.getRawType().equals(pb.getRawType())
                 && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments())));
      }else if (a instanceof GenericArrayType) {
         if (!(b instanceof WildcardType))  {
            return false;
         }
         WildcardType wa = (WildcardType) a;
         WildcardType wb = (WildcardType) b;
         return Arrays.equals(wa.getLowerBounds(),wb.getLowerBounds())
                 && Arrays.equals(wa.getLowerBounds(),wb.getLowerBounds());
      }else if (a instanceof TypeVariable) {
         if (!(b instanceof TypeVariable)) return false;
         TypeVariable<?> va = (TypeVariable<?>) a;
         TypeVariable<?> vb = (TypeVariable<?>) b;
         return va.getGenericDeclaration() == vb.getGenericDeclaration()
                 && va.getName().equals(vb.getName());
      }else {
         return false;
      }
   }

   static Type getGenericSupertype(Type context,Class<?> rawType,Class<?> toResolve) {
       if (toResolve == rawType) return context;

       if (toResolve.isInterface()) {
          Class<?>[] interfaces = rawType.getInterfaces();
          for (int i = 0 , length = interfaces.length; i<length; i++) {
             if (interfaces[i] == toResolve) {
                return rawType.getGenericInterfaces()[i];
             }else if (toResolve.isAssignableFrom(interfaces[i])) {
                return getGenericSupertype(rawType.getGenericInterfaces()[i],interfaces[i],toResolve);
             }
          }
       }

       if (!rawType.isInterface()) {
          while (rawType != Object.class) {
             Class<?> rawSupertype = rawType.getSuperclass();
             if (rawSupertype == toResolve) {
                return rawType.getGenericSuperclass();
             }else if (toResolve.isAssignableFrom(rawSupertype)) {
                return getGenericSupertype(rawType.getGenericSuperclass(),rawSupertype,toResolve);
             }
             rawType = rawSupertype;
          }
       }
       return toResolve;
   }

   private static int indexOf(Object[] array, Object toFind){
     for (int i = 0; i< array.length; i++) {
        if (toFind.equals(array[i])) return i;
     }
     throw  new NoSuchElementException();
   }

   static String typeToString(Type type) {
      return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
   }

   static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
      if (!supertype.isAssignableFrom(contextRawType)) throw new IllegalArgumentException();
      return resolve(context, contextRawType, getGenericSupertype(context, contextRawType, supertype));
   }

   static Type resolve(Type context, Class<?> contextRawType, Type toResolve){
      while (true) {
         if (toResolve instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) toResolve;
            toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
            if (toResolve == typeVariable) {
               return toResolve;
            }
         }else if (toResolve instanceof Class && ((Class<?>) toResolve).isArray()) {
          Class<?> original = (Class<?>) toResolve;
          Type componentType = original.getComponentType();
          Type newComponentType = resolve(context, contextRawType, componentType);
          return componentType == newComponentType
                  ? original
                  : new GenericArrayTypeImpl(newComponentType);
         }else if (toResolve instanceof GenericArrayType) {
            GenericArrayType original = (GenericArrayType) toResolve;
            Type componentType = original.getGenericComponentType();
            Type newComponentType = resolve(context,contextRawType,componentType);
            return componentType == newComponentType
                    ? original
                    :new GenericArrayTypeImpl(newComponentType);
         }else if (toResolve instanceof ParameterizedType) {
             ParameterizedType original = (ParameterizedType) toResolve;
             Type ownerType = original.getOwnerType();
             Type newOwnerType = resolve(context, contextRawType, ownerType);
             boolean changed = newOwnerType != ownerType;

             Type[] args = original.getActualTypeArguments();
             for (int t = 0, lenght = args.length; t < lenght; t++) {
                 Type resolvedTypeArgument = resolve(context,contextRawType,args[t]);
                 if (resolvedTypeArgument != args[t]) {
                     if (!changed) {
                         args = args.clone();
                         changed = true;
                     }
                     args[t] = resolvedTypeArgument;
                 }
             }

             return changed ? new ParameterizedTypeImpl(newOwnerType, original.getRawType(), args) :original;
         } else if (toResolve instanceof WildcardType) {
           WildcardType original = (WildcardType) toResolve;
           Type[] originalLowerBound = original.getLowerBounds();
           Type[] originalUpperBound = original.getUpperBounds();

           if (originalLowerBound.length == 1) {
               Type lowerBound = resolve(context, contextRawType, originalLowerBound[0]);
               if (lowerBound != originalLowerBound[0]) {
                   return new WildcardTypeImpl(new Type[]{Object.class}, new Type[] {lowerBound});
               }
           }else if (originalUpperBound.length == 1) {
               Type upperBound = resolve(context, contextRawType, originalUpperBound[0]);
               if (upperBound != originalUpperBound[0]) {
                   return new WildcardTypeImpl(new Type[] {upperBound}, EMPTY_TYPE_ARRAY);
               }
           }
           return original;
         } else {
             return toResolve;
         }
      }
   }

   private static Type resolveTypeVariable(
           Type context, Class<?> contextRawTyep, TypeVariable<?> unknown) {
         Class<?> declareByRaw = declaringClassOf(unknown);

         if (declareByRaw == null) return unknown;

         Type declaredBy = getGenericSupertype(context,contextRawTyep,declareByRaw);
         if (declaredBy instanceof ParameterizedType) {
            int index = indexOf(declareByRaw.getTypeParameters(),unknown);
            return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
         }

         return unknown;
   }

   private static @Nullable Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
      GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
      return genericDeclaration instanceof Class ? (Class<?>) genericDeclaration : null;
   }

   private static final class GenericArrayTypeImpl implements GenericArrayType {
     private final Type componentType;

     GenericArrayTypeImpl(Type componentType) {
        this.componentType = componentType;
     }
      @NonNull
      @Override
      public Type getGenericComponentType() {
         return componentType;
      }

      @Override
      public boolean equals(Object o) {
         return o instanceof GenericArrayType && Utils.equals(this, (GenericArrayType) o);
      }

      @Override
      public int hashCode() {
         return Objects.hash(componentType);
      }

      @Override
      public String toString() {
         return typeToString(componentType) + "[]";
      }
   }

   static void checkNotPrimitive(Type type) {
       if (type instanceof Class<?> && ((Class<?>) type).isPrimitive()) {
           throw new IllegalArgumentException();
       }
   }

   static boolean isAnnotationPresent(Annotation[] annotations, Class<? extends Annotation> cls) {
       for (Annotation annotation : annotations) {
           if (cls.isInstance(annotation)) {
               return true;
           }
       }
       return false;
   }

   static ResponseBody buffer(final ResponseBody body) throws IOException {
       Buffer buffer = new Buffer();
       body.source().readAll(buffer);
       return ResponseBody.create(body.contentType(), body.contentLength(), buffer);
   }

   static Type getParameterUpperBound(int index, ParameterizedType type) {
       Type[] types = type.getActualTypeArguments();
       if (index < 0 || index >= types.length) {
          throw new IllegalArgumentException("Index " + index + " not in range [0," + types.length + ") for " + type);
       }
       Type paramType = types[index];
       if (paramType instanceof WildcardType) {
           return ((WildcardType) paramType).getUpperBounds()[0];
       }
       return paramType;
   }

//   static Type getParameterLowerBound(int index, ParameterizedType type) {
//       Type[] types = type.getActualTypeArguments();
//       if (index < 0 |)
//   }

   private static final class WildcardTypeImpl implements WildcardType {
       private final Type upperBound;
       private final @Nullable Type lowerBound;

       public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
           if (lowerBounds.length > 1) throw new IllegalArgumentException();
           if (upperBounds.length != 1) throw new IllegalArgumentException();
           if (lowerBounds.length == 1) {
               if (lowerBounds[0] == null) throw new NullPointerException();
               checkNotPrimitive(lowerBounds[0]);
               if (upperBounds[0] != Object.class) throw new IllegalArgumentException();
               this.lowerBound = lowerBounds[0];
               this.upperBound = Object.class;
           }else {
               if (upperBounds[0] == null) throw new NullPointerException();
               checkNotPrimitive(upperBounds[0]);
               this.lowerBound = null;
               this.upperBound = upperBounds[0];
           }
       }

       @NonNull
       @Override
       public Type[] getUpperBounds() {
           return new Type[] {upperBound};
       }

       @NonNull
       @Override
       public Type[] getLowerBounds() {
           return lowerBound != null ? new Type[]{lowerBound} : EMPTY_TYPE_ARRAY;
       }

       @Override
       public boolean equals(Object other) {
       return other instanceof WildcardType && Utils.equals(this,(WildcardType) other);
       }

       @Override
       public int hashCode() {
           return (lowerBound != null ? 31 + lowerBound.hashCode() : 1) ^ (31 + upperBound.hashCode());
       }

       @Override
       public String toString() {
           if (lowerBound != null) return "? super " + typeToString(lowerBound);
           if (upperBound == Object.class) return "?";
           return "? extends " + typeToString(upperBound);
       }
   }

   static final class ParameterizedTypeImpl implements ParameterizedType {
       private final @Nullable Type ownerType;
       private final Type rawType;
       private final Type[] typeArguments;

       public ParameterizedTypeImpl(@Nullable Type ownerType, Type rawType, Type... typeArguments) {

           if (rawType instanceof Class<?> && (ownerType == null) != (((Class<?>) rawType).getEnclosingClass() == null)) {
               throw new IllegalArgumentException();
           }
           for (Type typeArgument : typeArguments) {
               Objects.requireNonNull(typeArgument,"typeArgument == null");
               checkNotPrimitive(typeArgument);
           }
           this.ownerType = ownerType;
           this.rawType = rawType;
           this.typeArguments = typeArguments;
       }

       @NonNull
       @Override
       public Type[] getActualTypeArguments() {
           return typeArguments.clone();
       }

       @NonNull
       @Override
       public Type getRawType() {
           return rawType;
       }

       @Nullable
       @Override
       public Type getOwnerType() {
           return ownerType;
       }

       @Override
       public boolean equals(Object other) {
           return other instanceof ParameterizedType && Utils.equals(this, (ParameterizedType) other);
       }

       @Override
       public int hashCode() {
           return Arrays.hashCode(typeArguments)^ rawType.hashCode()
                   ^ (ownerType != null ? ownerType.hashCode() : 0);
       }

       @Override
       public String toString() {
           if (typeArguments.length == 0) return typeToString(rawType);
           StringBuilder result = new StringBuilder(30 * (typeArguments.length + 1));
           result.append(typeToString(rawType));
           result.append("<").append(typeToString(typeArguments[0]));
           for (int i = 1; i < typeArguments.length; i++) {
               result.append(", ").append(typeToString(typeArguments[i]));
           }
           return result.append(">").toString();
       }
   }
}
