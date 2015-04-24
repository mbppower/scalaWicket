package com.mbppower

import com.mbppower.nashorn.NashornResourceReference
import org.apache.wicket.protocol.http.WebApplication

object WicketApplication {
  
}

class WicketApplication extends WebApplication {
  
  override def init: Unit = {
    super.init()
		getRequestCycleListeners().add(JpaRequestCycle)
		
		//mount js file
		mountResource("/view", new NashornResourceReference("/js/view/index.js"))
		
		//mount page
    mountPage("/home", classOf[HomePageScala])
  }
  
	override def onDestroy(){
		super.onDestroy();
		JpaRequestCycle.destroy();
	}
	
  override def getHomePage = classOf[HomePageScala]  
}


