sap.ui.define([ 'jquery.sap.global', 'sap/ui/core/Fragment',
		'sap/ui/core/mvc/Controller', 'sap/ui/model/json/JSONModel',
		"sap/m/MessageToast" ], function(jQuery, Fragment, Controller,
		JSONModel, MessageToast) {
	"use strict";

	var PageController = Controller.extend("ems.controller.DepartmentDetail", {
		
		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf dataform.Page
		 */
		onInit : function() {
			var model = new sap.ui.model.json.JSONModel();
//			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			
			var url = location.href;
			var id = parseInt(url.substr(url.lastIndexOf("=") + 1));

//			 model.loadData("data/department.json");
			model.loadData("/springEmployee/api/department/" + id);
			this.getView().setModel(model);
			// this.getView().bindElement("/");
			this._showFormFragment("DepartmentDisplay");
		},

		handleEditPress : function() {
			this._oInfo = jQuery
					.extend({}, this.getView().getModel().getData());
			this._toggleButtonsAndView(true);
		},

		handleCancelPress : function() {

			// Restore the data
			var oModel = this.getView().getModel();
			var oData = oModel.getData();

			oData = this._oInfo;

			oModel.setData(oData);
			this._toggleButtonsAndView(false);

		},

		handleSavePress : function() {
			this._submitInfo("PUT", "edit");
			setTimeout(function () { 
				window.location.reload();
		    }, 500);
		},

		handleDeletePress : function() {
			if(window.confirm("Are you sure to delete this department?")){
				this._submitInfo("DELETE", "delete");
				window.location.href = "departmentList.html";
			}
		},
		

		_formFragments : {},

		_submitInfo : function(method, op) {
			var department = this.getView().getModel().oData;
			var url = location.href;
			var id = parseInt(url.substr(url.lastIndexOf("=") + 1));
			
			if (method == "PUT") {
				department["manager"] = this.byId("selectManager").getSelectedItem().getText();
				console.log(department);
			}
			
			$.ajax({
				url : "/springEmployee/api/department/" + id,
				type : method,
				dataType : 'text',
				contentType : "application/json",
				data : JSON.stringify(department),
				success : function(res) {
					MessageToast.show(res);
				}
			});

		},

		_toggleButtonsAndView : function(bEdit) {
			var oView = this.getView();

			// Show the appropriate action buttons
			oView.byId("edit").setVisible(!bEdit);
			oView.byId("delete").setVisible(!bEdit);
			oView.byId("save").setVisible(bEdit);
			oView.byId("cancel").setVisible(bEdit);

			// Set the right form type
			this._showFormFragment(bEdit ? "DepartmentChange" : "DepartmentDisplay");
		},

		_getFormFragment : function(sFragmentName) {
			var oFormFragment = this._formFragments[sFragmentName];
			var url = location.href;
			var id = parseInt(url.substr(url.lastIndexOf("=") + 1));

			if (oFormFragment) {
				return oFormFragment;
			}

			oFormFragment = sap.ui.xmlfragment(this.getView().getId(), "ems.view."
					+ sFragmentName);
			
//			if(sFragmentName == "DepartmentChange"){
//				var employees = new JSONModel();
//				
//				employees.loadData("/springEmployee/api/employee/department/" + id);
//				this.byId("selectManager").setModel(employees);
//			}


			return this._formFragments[sFragmentName] = oFormFragment;
		},

		_showFormFragment : function(sFragmentName) {
			var oPage = this.getView().byId("detailPage");

			oPage.removeAllContent();
			oPage.insertContent(this._getFormFragment(sFragmentName));
		},

	/**
	 * Similar to onAfterRendering, but this hook is invoked before the
	 * controller's View is re-rendered (NOT before the first rendering!
	 * onInit() is used for that one!).
	 * 
	 * @memberOf dataform.Page
	 */
	// onBeforeRendering: function() {
	//
	// },
	/**
	 * Called when the View has been rendered (so its HTML is part of the
	 * document). Post-rendering manipulations of the HTML could be done here.
	 * This hook is the same one that SAPUI5 controls get after being rendered.
	 * 
	 * @memberOf dataform.Page
	 */
	// onAfterRendering: function() {
	//
	// },
	/**
	 * Called when the Controller is destroyed. Use this one to free resources
	 * and finalize activities.
	 * 
	 * @memberOf dataform.Page
	 */
	// onExit: function() {
	//
	// }
	});

	return PageController;

});