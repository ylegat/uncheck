# Description #
**uncheck** is a very simple Java library aiming to ease the transformation of checked exceptions into runtime exceptions using lambda.

## Without `uncheck` ##
Wrapping a checked exception inside a runtime exception usually requires some boilerplate code:
```java
try {
    unsafeMethod();
} catch(Exception e) {
    throw new RuntimeException(e);
}
```

This can even be *more* verbose when the unsafe call returns a value to process:
```java
Object result;
try {
    result = unsafeMethod();
} catch(Exception e) {
    throw new RuntimeException(e);
}

process(result);
```

## With `uncheck` ##
Using **uncheck**, you can improve the readability of your code by removing the boilerplate part of it:
```java
uncheck(this::unsafeMethod);
```

It also nicely handles methods returning values:
```java
Object result = uncheck(this::unsafeMethod);
process(result);
```

**Note**

Caught exceptions are processed this way:
- RuntimeException are just propagated as they are
- IOException are wrapped inside UncheckedIOException
- other exceptions are wrapped inside RuntimeException

## Download ##
Just add the following dependency in your `pom.xml`:
```xml
<dependency>
    <groupId>com.github.ylegat</groupId>
    <artifactId>uncheck</artifactId>
    <version>1.0</version>
</dependency>
```