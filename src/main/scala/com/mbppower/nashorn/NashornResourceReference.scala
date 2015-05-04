/*
 * Copyright 2015 Marcel.Barbosa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mbppower.nashorn

import com.mbppower.JpaRequestCycle
import java.io.FileReader
import java.io.OutputStreamWriter
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.resource.ResourceReference
import org.apache.wicket.request.resource.AbstractResource
import org.apache.wicket.request.resource.IResource
import org.apache.wicket.request.resource.IResource.Attributes
import javax.script.ScriptEngineManager

class NashornResourceReference(path:String) extends ResourceReference(path:String) {
	override def getResource():IResource = {
		return new NashornResource(path);
	}
}
class NashornResource(path:String) extends AbstractResource {
	
	val engine = new ScriptEngineManager().getEngineByName("nashorn")

	override def newResourceResponse(attributes:Attributes):AbstractResource.ResourceResponse = {
		val resourceResponse = new AbstractResource.ResourceResponse()
		resourceResponse.setContentType("text/html")
		resourceResponse.setTextEncoding("utf-8")
		resourceResponse.setWriteCallback(new AbstractResource.WriteCallback() {

			override def writeData(attributes : Attributes){
				
				val outputStream = attributes.getResponse().getOutputStream();
				val writer = new OutputStreamWriter(outputStream);
				val context = WebApplication.get().getServletContext();
				var jsBaseDir = context.getRealPath("/WEB-INF/classes")

				//expose java objects
				engine.put("jpa", JpaRequestCycle)
				engine.put("context", attributes)
				engine.put("jsBaseDir", jsBaseDir)
				engine.put("output", writer)

				//process
				engine.eval(new FileReader( jsBaseDir + path));
				writer.flush();
				writer.close();
			}      
		})
		return resourceResponse
	}
}
