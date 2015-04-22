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
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener
import org.apache.wicket.request.cycle.RequestCycle

object JpaRequestCycle extends AbstractRequestCycleListener {
	
	var emf: EntityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit")
  var emThreadLocal: ThreadLocal[EntityManager] = new ThreadLocal[EntityManager]
	 
  def getEntityManager(): EntityManager = {
    if(emThreadLocal.get() == null) {
      emThreadLocal.set(emf.createEntityManager())
    }
    return emThreadLocal.get()
  }

  override def onEndRequest(r: RequestCycle) = {
		JpaRequestCycle.close()
  }

  override def onException(r: RequestCycle, e: Exception): IRequestHandler  = {
		JpaRequestCycle.close()
		return null
  }
	
	def commit(){
		emThreadLocal.get().getTransaction().commit()
	}
	
	def begin(){
		emThreadLocal.get().getTransaction().begin()
	}
	
	def rollback(){
		val em:EntityManager = emThreadLocal.get()
		if (em.getTransaction().isActive())
			em.getTransaction().rollback()
	}
	
	def close(){
		if(emThreadLocal != null){
			val em:EntityManager = emThreadLocal.get()
			if (em != null) {
				em.close()
			}
			emThreadLocal.set(null);
		}
	}
	
	def destroy(){
		if(emThreadLocal != null){
			close();
			emThreadLocal.remove();
		}
	}
}