# minimal-web-framework-with-restcontroller

This is a light weight webframework consisting of annotaions like GetMapping and RestController with usage similar to how we have in springboot but not equally flexible as its just for educational purpose.

At this point as of commit on 30th December there are only 5 files that are part of the framework:

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/4393147e-32b8-43c1-a548-cdb3b25ad59a)

And to use this we can create controller class like this(which is using the @RestController and @GetMapping annotations):

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/78f320ac-8f53-4550-b7e4-189a7211a6ae)

## Notice, how instead of having @QueryParam we can directly use the params using the $ notation

And it also needs a main class to scan the package containing the controllers and to start the server at a particular port:

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/39e622d9-fc58-46f7-85de-2fd31fc7fe3e)

Added support for @RequestParam annotation and injectable HttpHeader

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/beed0d0e-5d9d-4f1a-be4c-1adba3b2a50a)



