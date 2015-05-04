package com.mbppower

import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Button
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.Model
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior
import org.apache.wicket.ajax.markup.html.AjaxLink
import java.util.Date
import javax.persistence.EntityManager
import com.mbppower.model.UserData
import com.mbppower.model.UserRole
import org.apache.wicket.ajax.AjaxEventBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import scala.collection.JavaConversions._

class HomePageScala(parameters: PageParameters) extends WebPage {

  var em = JpaRequestCycle.getEntityManager()
  val user = Model.of(getUserById(if (parameters.get("id").isEmpty()) 0 else parameters.get("id").toInt()));
	
	//user list
	val userListModel = Model.ofList(em.createQuery("From UserData", classOf[UserData]).getResultList());
	
  //form
  val form = new Form("form", new CompoundPropertyModel[UserData](user)) {
    override def onSubmit() {
      super.onSubmit()
			
			//set role
      val model : UserData = getDefaultModelObject().asInstanceOf[UserData];
			//save
			var em: EntityManager = JpaRequestCycle.getEntityManager()
			JpaRequestCycle.begin();
			em.merge(model)
			JpaRequestCycle.commit();
			
			refreshUserList();
    }
  }
	form.setOutputMarkupId(true)
	//roles
	val rolesListModel = Model.ofList(em.createQuery("From UserRole", classOf[UserRole]).getResultList())
	val rolesDropdown = new DropDownChoice[UserRole]("userRole", rolesListModel).setChoiceRenderer(new ChoiceRenderer("name", "id")).setOutputMarkupId(true)
	
	//add components
	form.add(rolesDropdown)
	form.add(new TextField[String]("name"))
	add(form)
	
	var countModel = new Model[Integer]{
		override def getObject():Integer = {
			return userListModel.getObject().length
		}
	}

  val label = new Label("users", countModel).setOutputMarkupId(true);
	add(label)
	
	//user list
	val userListContainer = new WebMarkupContainer("userListContainer");
	userListContainer.setOutputMarkupId(true)
	val userList = new ListView[UserData]("userList", userListModel){
		
		override def populateItem(item: ListItem[UserData]){
			val _this = this;
			val userData = item.getModel().getObject()
			
			//labels
			item.add(new Label("name", Model.of(userData.name)))
			item.add(new Label("role", Model.of(Option[UserRole](userData.userRole).map(_.name))))
			
			//delete
			item.add(new Button("deleteButton").add(new AjaxEventBehavior("click"){
				override def onEvent(target: AjaxRequestTarget) {
					
					//delete item
					var em: EntityManager = JpaRequestCycle.getEntityManager()
					JpaRequestCycle.begin();
					val userData = item.getModelObject();
					em.remove(if(em.contains(userData)) userData else em.merge(userData));
					JpaRequestCycle.commit();
					
					//update view
					refreshUserList();	
					_this.setList(userListModel.getObject())
					target.add(userListContainer)
					target.add(label)
				}
			})).add(new AjaxEventBehavior("click"){
				override def onEvent(target: AjaxRequestTarget) {
					user.setObject(getUserById(item.getModelObject().id));
					target.add(form)
				}
			})
		}
	}

	add(userListContainer.add(userList));
	
	add(new Button("submitButton").add(new AjaxFormSubmitBehavior(form, "click"){
		override def onSubmit(target: AjaxRequestTarget) {
			
      target.add(userListContainer)
      target.add(label)
    }
	}))
	
  add(new AjaxLink("addRoleButton") {
    override def onClick(target: AjaxRequestTarget) {
			var em: EntityManager = JpaRequestCycle.getEntityManager()
			JpaRequestCycle.begin();
			em.persist(new UserRole("Farewell " + new Date().toString()));
			JpaRequestCycle.commit();
			
			//update role list
			rolesListModel.setObject(em.createQuery("From UserRole", classOf[UserRole]).getResultList())
			target.add(rolesDropdown);
    }
  })

	def getUserById(id:Int): UserData = {
		return Option(JpaRequestCycle.getEntityManager().find[UserData](classOf[UserData], id)).getOrElse(new UserData())
  }
  def refreshUserList(): Unit = {
		userListModel.setObject(JpaRequestCycle.getEntityManager().createQuery("From UserData ORDER BY id", classOf[UserData]).getResultList());
  }
}