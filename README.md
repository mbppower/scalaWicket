# ScalaWicket

<p>Simple Web App using Scala, Maven, Hibernate and Wicket</p>

#NashornResourceReference

<p>
	This class allows you to use javascript files as pages.
	This means you can write a page entirely in Javascript and it will be processed by JVM Nashorn engine.
</p>

<p>Page Mount:</p>

<code>mountResource("/view", new NashornResourceReference("/js/view/index.js"))</code>

<p>index.js contents:</p>

<code>
	var outputs = "";
	var i = 0;
	while(i++ < 100 ){
		outputs += " " + i;
	}

	var name = context.getParameters().get("name").toString();

	output.write(outputs + " param: " + name);
</code>

<p>Browser output calling url http://localhost:8080/view?name=12</p>

<code>
1 2 3 4 5 6 7 8 9 10 param: 12
</code>
