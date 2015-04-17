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

package com.mbppower

import javax.persistence.EntityManager
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener
import org.apache.wicket.request.cycle.RequestCycle

object JpaRequestCycle extends AbstractRequestCycleListener {

  var em: EntityManager = null

  def getEntityManager(): EntityManager = {
    if(em == null || !em.isOpen()) {
      val emf = WicketApplication.getEntityManager
      em = emf.createEntityManager()
    }
    return em
  }

  override def onEndRequest(r: RequestCycle) = {
    if (em != null && em.isOpen()) {
      if (em.getTransaction().isActive()) em.getTransaction().rollback()
      em.close()
    }
  }

  override def onException(r: RequestCycle, e: Exception): IRequestHandler  = {
    if (em != null && em.isOpen()) {
	  if (em.getTransaction().isActive()) em.getTransaction().rollback()
	  em.close()
    }
	return null
  }
}