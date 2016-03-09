sap.ui.define([ 'jquery.sap.global', 'sap/ui/core/mvc/Controller',
		'sap/ui/model/json/JSONModel', "sap/m/MessageToast" ], function(jQuery,
		Controller, JSONModel, MessageToast) {
	"use strict";

	var AddController = Controller.extend("ems.controller.AddEmployee", {

		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf dataform.Page
		 */
		onInit : function() {
			var employee = new JSONModel();
			var departments = new JSONModel();

			// employee.loadData("data/person.json");
			// department.loadData("data/departments.json");
			departments.loadData("/springEmployee/api/department/departments");
			// console.log(department.length);
			this.getView().setModel(employee);
			this.byId("selectDepartment").setModel(departments);
			console.log(departments);
		},

		handleCancelPress : function() {

			// Restore the data
			var oModel = this.getView().getModel();
			var oData = oModel.getData();

			oData = this._oInfo;
			oModel.setData(oData);
		},

		handleSavePress : function() {
			this._submitInfo();
		},

		handleUploadComplete : function(oEvent) {
			MessageToast.show("Image Upload Success!");
		},

		handleUploadPress : function(oEvent) {
			var oFileUploader = this.getView().byId("fileUploader");
			oFileUploader.upload();
		},

		_formFragments : {},

		_submitInfo : function() {
			var oPosition = this.byId("selectPosition");
			var oDepartment = this.byId("selectDepartment");
			var employee = this.getView().getModel().getData();
			var department = oDepartment.getModel().getData();
			var imgId = $.cookie("imgId");
			console.log("imgId: " + imgId);
			
			if(isNaN(employee["age"])){
				alert("The Input For 'Age' Field Must Be a Number!");
			} else {
				if (imgId != "") {
					employee["imgId"] = imgId;
					employee["position"] = "Officer";
					if (department.length == 0) {
						employee["department"] = "undefined";
					} else {
						employee["department"] = oDepartment.getSelectedItem()
								.getText();
					}

					console.log(employee);

					$.ajax({
						url : "/springEmployee/api/employee",
						type : "POST",
						dataType : 'text',
						contentType : "application/json",
						data : JSON.stringify(employee),
						success : function(id) {
							MessageToast.show("Add Employee Success!");
							setTimeout(function () { 
								window.location.href = "index.html#/EmployeeDetail/" + id;
						    }, 500);
						}
					});
				} else {
					MessageToast.show("Image Already Exist!");
				}
			}
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

	return AddController;

});