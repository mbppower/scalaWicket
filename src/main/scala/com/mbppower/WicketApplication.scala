package com.mbppower

import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import org.apache.wicket.protocol.http.WebApplication

object WicketApplication {
  private var emf:EntityManagerFactory = null
  def getEntityManager = emf
}

class WicketApplication extends WebApplication {
  
  override def init: Unit = {
    super.init()
    WicketApplication.emf = Persistence.createEntityManagerFactory("persistenceUnit")
	getRequestCycleListeners().add(JpaRequestCycle)
    mountPage("/home", classOf[HomePageScala])
  }
  
  override def getHomePage = classOf[HomePageScala]  
}