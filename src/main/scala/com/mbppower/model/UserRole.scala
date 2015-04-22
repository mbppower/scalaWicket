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


package com.mbppower.model

import scala.collection.JavaConversions._
import javax.persistence._

@Entity
class UserRole(roleName:String) extends java.io.Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id:Int = _
  var name:String = roleName
	
	//for hibernate
	def this() = this(null)
}
