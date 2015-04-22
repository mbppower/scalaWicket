package com.mbppower

import org.apache.wicket.protocol.http.WebApplication

object WicketApplication {
  
}

class WicketApplication extends WebApplication {
  
  override def init: Unit = {
    super.init()
		getRequestCycleListeners().add(JpaRequestCycle)
    mountPage("/home", classOf[HomePageScala])
  }
  
	override def onDestroy(){
		super.onDestroy();
		JpaRequestCycle.destroy();
	}
	
  override def getHomePage = classOf[HomePageScala]  
}