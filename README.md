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
load(jsBaseDir + '/js/view/helper.js');
load(jsBaseDir + '/js/lib/underscore-min.js');

//get request parameter
var name = context.getParameters().get("name").toString();

//parse undescore template
var compiled = _.template(Helper.readContents(jsBaseDir + "/js/view/template/index.html"));

//jpa entity manager instance
var em = jpa.getEntityManager();
var users = em.createQuery("From UserData").getResultList();

//render template and output it to the browser
output.write(
	compiled({
		name : name,
		help: "Underscore Template. User list:",
		users : users
	})
);
```

<p>Browser output calling url http://localhost:8080/view?name=12</p>

```
I am a template
RequestParam: Help:Underscore Template. User list:	

    Name: Marcel Role: Farewell Tue May 05 10:44:50 BRT 2015
    Name: Darwin Role: Farewell Tue May 05 10:44:50 BRT 2015
```
