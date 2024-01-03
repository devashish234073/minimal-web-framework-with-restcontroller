# minimal-web-framework-with-restcontroller

This is a light weight webframework consisting of annotaions like GetMapping and RestController with usage similar to how we have in springboot but not equally flexible as its just for educational purpose.

This started with 5 files framework, which further grew to support more annotations:

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/4393147e-32b8-43c1-a548-cdb3b25ad59a)

And to use this we can create controller class like this(which is using the @RestController and @GetMapping annotations):

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/78f320ac-8f53-4550-b7e4-189a7211a6ae)

## The $ notation is used in the response to replace the values from requestparam without even using the RequestParam annotation

And it also needs a main class to scan the package containing the controllers and to start the server at a particular port:

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/39e622d9-fc58-46f7-85de-2fd31fc7fe3e)

@RequestParam annotation and injectable HttpHeader are now supported as described below

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/beed0d0e-5d9d-4f1a-be4c-1adba3b2a50a)

## Update 1st Jan 2024:

@ResponseType is also supported now to return json and xml responses too:

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/70b29317-1d70-4974-8dcf-21060c698d5f)

## Update 3rd Jan 2024:

Added @PostMapping and @RequestBody annotaion support , also converted teh project into a maven one to use the fasterxml dependencies to parsing request xml or json into objects

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/448a1184-779c-4583-8ffa-6b8f36287673)

Added a ResponseDeparser class to simplify the code in the controller so that instead if creating the whole JSON or XML we can just return any Class type which will be converted into JSON or XML string by the framework. 

The conversion uses the ObjectMapper from fasterxml:

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/4f8a6a08-6ad1-4fc1-8098-f1dc830a76cf)

This is the simplified usage in controller after this:

![image](https://github.com/devashish234073/minimal-web-framework-with-restcontroller/assets/20777854/9fbb4e57-1daf-4d22-8d2e-a25fe8479745)





