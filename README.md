# ScalaWicket

<p>Simple Web App using Scala, Maven, Hibernate and Wicket</p>

#NashornResourceReference

<p>
	This class allows you to use javascript files as pages.
	This means you can write a page entirely in Javascript and it will be processed by JVM Nashorn engine.
</p>

<p>Page Mount:</p>

```
mountResource("/view", new NashornResourceReference("/js/view/index.js"))
```

<p>index.js contents:</p>

```
var outputs = "";
var i = 0;
while(i++ < 100 ){
	outputs += " " + i;
}

var name = context.getParameters().get("name").toString();

output.write(outputs + " param: " + name);
```

<p>Browser output calling url http://localhost:8080/view?name=12</p>

```
1 2 3 4 5 6 7 8 9 10 param: 12
```
