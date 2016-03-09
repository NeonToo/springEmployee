sap.ui.define([ 'jquery.sap.global', 'sap/ui/core/mvc/Controller',
		'sap/ui/model/json/JSONModel', "sap/m/MessageToast" ], function(jQuery,
		Controller, JSONModel, MessageToast) {
	"use strict";

	var DepartmentController = Controller.extend("ems.controller.AddDepartment", {

		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf dataform.Page
		 */
		onInit : function() {
			var model = new JSONModel();

			this.getView().setModel(model);
		},

		handleCancelPress : function() {
			// Restore the data
			var oModel = this.getView().getModel();
			var oData = oModel.getData();

			oData = this._oInfo;
			oModel.setData(oData);
			window.close();
		},

		handleSavePress : function() {
			this._submitInfo();
		},

		_formFragments : {},

		_submitInfo : function() {
			var department = this.getView().getModel().oData;
			if(this.byId("manager").getValue() == ""){
				department["manager"] = "undefined";
			}
			console.log(department);
			
			$.ajax({
				url : "/springEmployee/api/department",
				type : "POST",
				dataType : 'text',
				contentType : "application/json",
				data : JSON.stringify(department),
				success : function(res) {
					MessageToast.show(res);
				}
			});
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

	return DepartmentController;

});