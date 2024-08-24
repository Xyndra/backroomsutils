Note: This doesn't work for enum classes (and probably sealed classes and other stuff too) because their decompile will 
be an enum with functions and fields, which is not valid Java code. You will have to manually convert them. There also
isn't really a way to represent some of these things in Java, so the component might have to be redesigned.

If you are really against using Kotlin and are using IntelliJ IDEA, here is how you can transform a Kotlin file into a Java file:

1. Open the Kotlin file in IntelliJ IDEA.
2. Go to `Tools` -> `Kotlin` -> `Show Kotlin Bytecode` or a window with the Kotlin icon in the right bar.
3. Click on `Decompile`. This will open a .decompiled.java file with the Java code.
4. Since there is a lot of bad stuff in that code, go to [My tool](https://codepen.io/xyndra/full/QWXrrYN) and paste the code there.
5. Now delete the original Kotlin file(make sure it still accessible on the Git repository though!) and create Java files from the names in the comments (be careful to put it in the correct package!).