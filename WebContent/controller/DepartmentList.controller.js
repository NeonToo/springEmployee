sap.ui.define([ 'jquery.sap.global', 'sap/ui/core/mvc/Controller',
		'sap/ui/model/json/JSONModel', 'sap/ui/core/UIComponent' ],
		function(jQuery, Controller, JSONModel, UIComponent) {
			"use strict";

			var TableController = Controller.extend("ems.controller.DepartmentList", {

				onInit : function() {
					// set explored app's demo model on this sample
					var model = new sap.ui.model.json.JSONModel();

//					model.loadData("data/departments.json");
					model.loadData("/springEmployee/api/department");
					this.getView().setModel(model);
				},

				onPress : function(oEvent) {
					 this._showDetail(oEvent.getSource());
				},

				_showDetail : function(oItem) {
					console.log(oItem.getBindingContext().getProperty("id"));
					var id = oItem.getBindingContext().getProperty("id");
					window.location.href = "departmentDetail.html?id=" + id;
//					UIComponent.getRouterFor(this).navTo("departmentDetail", {
//						id: oItem.getBindingContext().getProperty("id")
//					});
				}
			});

			return TableController;

		});