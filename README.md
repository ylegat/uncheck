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

doSomethingWith(result);
```

## With `uncheck` ##
Using **uncheck**, you can improve the readability of your code by reducing the boilerplate part of it:
```java
uncheck(() -> unsafeMethod());
```

It also handle nicely return values:
```java
object result = uncheck(() -> unsafeMethod());
doSomethingWith(result);
```