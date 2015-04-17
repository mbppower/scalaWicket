package com.mbppower

import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.PropertyModel
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
import javax.persistence.EntityManager
import com.mbppower.model.UserData
import com.mbppower.model.UserRole
import org.apache.wicket.ajax.AjaxEventBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import scala.collection.JavaConversions._

class HomePageScala(parameters: PageParameters) extends WebPage {
	var userListModel:java.util.List[UserData] = _
	
  var em = JpaRequestCycle.getEntityManager()

  val id = if (parameters.get("id").isEmpty()) 0 else parameters.get("id").toInt()
  var user = em.find[UserData](classOf[UserData], id)
	if(user == null) user = new UserData()
	
	//user list
	refreshUserList();
	
  //form
  val form = new Form("form", new CompoundPropertyModel[UserData](user)) {
    override def onSubmit() {
      super.onSubmit()
      val model : UserData = getDefaultModelObject().asInstanceOf[UserData];
			var em: EntityManager = JpaRequestCycle.getEntityManager()
			em.getTransaction().begin();
			em.merge(model)
			em.getTransaction().commit();
			
			refreshUserList();
			userList.setList(userListModel)
    }
  }

  form.add(new TextField[String]("name"))
	
	val roles = em.createQuery("From UserRole", classOf[UserRole]).getResultList()
  form.add(new DropDownChoice[UserRole]("roles").setChoices(roles))//.setChoiceRenderer(new ChoiceRenderer("id", "name")))
	add(form)
	
	var countModel = new Model[Integer]{
		override def getObject():Integer = {
			return userListModel.length
		}
	}

  val label = new Label("users", countModel)
	label.setOutputMarkupId(true);
	add(label)
	val userListContainer = new WebMarkupContainer("userListContainer");
	userListContainer.setOutputMarkupId(true)
	val userList = new ListView[UserData]("userList", Model.ofList(userListModel)){
		
		override def populateItem(item: ListItem[UserData]){
			val _this = this;
			item.add(new Label("name", new PropertyModel(item.getModel(), "name")))
			item.add(new Button("deleteButton")).add(new AjaxEventBehavior("click"){
				override def onEvent(target: AjaxRequestTarget) {
					
					//delete item
					var em: EntityManager = JpaRequestCycle.getEntityManager()
					em.getTransaction().begin();
					val userData = item.getModelObject();
					em.remove(if(em.contains(userData)) userData else em.merge(userData));
					em.getTransaction().commit();
					
					//update view
					refreshUserList();	
					_this.setList(userListModel)
					target.add(userListContainer)
					target.add(label)
				}
			})
		}
	}

	userListContainer.add(userList);
	add(userListContainer);
	
	add(new Button("submitButton").add(new AjaxFormSubmitBehavior(form, "click"){
		override def onSubmit(target: AjaxRequestTarget) {
			
      target.add(userListContainer)
      target.add(label)
    }
	}))
	
  add(new AjaxLink("addRoleButton", Model.of("")) {
    override def onClick(target: AjaxRequestTarget) {
			var em: EntityManager = JpaRequestCycle.getEntityManager()
			em.getTransaction().begin();
			em.persist(new UserRole("Farewell"));
			em.getTransaction().commit();
    }
  })

  def refreshUserList(): Unit = {
		var em: EntityManager = JpaRequestCycle.getEntityManager()
		userListModel = em.createQuery("From UserData", classOf[UserData]).getResultList()
  }
	
	def showUsers(): String = {

    var em: EntityManager = JpaRequestCycle.getEntityManager()
    val userList = em.createQuery("From UserData", classOf[UserData]).getResultList()

    var result: String = userList.size() + " - "
    userList.foreach((u: UserData) => {
      result.concat(u.name + ", ")
    })

    return result
  }
}