$(document).ready(function () {
console.log("scripts.js Document ready");
    // Invoke the corresponding URL to update the dynamic fields section using Ajax
    $('.dynamic-update-rows').on('click', 'button[data-dynamic-update-rows-url]', function () {
    	//event event.preventDefault();
        let url = $(this).data('dynamic-update-rows-url');
console.log("Button clicked - url = " + url);
console.log("Button id: " + this.id);
console.log("Button name: " + this.name);
        // adding the row index, needed when deleting a dynamic row
        let formData = $('form').serializeArray();
console.log("Form data before pushing params = " + JSON.stringify(formData));
console.log("attr('name') = " + $(this).attr('name'));
        let param = {};
        param["name"] = $(this).attr('name');
        param["value"] = $(this).val();
console.log("param[name] = " + param["name"]);
console.log("param[value]" + param["value"]);
        formData.push(param);
console.log("Form data after pushing params = " + JSON.stringify(formData));

			console.log("Form data.length: " + JSON.stringify(formData.length));
			console.log("Form data devServers: " + JSON.stringify(formData.devServers));
			console.log("Form data testServers: " + JSON.stringify(formData.testServers));
			console.log("Before pushing date, formdata = " + JSON.stringify(formData));
//			let devServers = $("#devServers");
//			console.log("Dev servers: " + JSON.stringify(devServers));
//			console.log("Dev servers: " + devServers.length);
			//console.log("Dev servers as json: " + JSON.stringify(devServers));
//			let testServers = $("#testServers");
//			console.log("Test servers: " + testServers);
//			console.log("Test servers: " + testServers.length);
			//console.log("Test servers as json: " + JSON.stringify(testServers));
	        // updating the dynamic section

			let buttonName = this.name;
			if (buttonName === 'addDevUser') {
				console.log("Adding dev user");
				let devUsers = $("#devUsers");
				console.log("Dev users: " + JSON.stringify(devUsers));
			    $('#dynamicTableContentsDevUsers').load(url, formData);
			} else if (buttonName === 'addDevServer') {
				console.log("Adding dev server");
				let devServers = $("#devServers");
				console.log("DevServers: " + JSON.stringify(devServers));
			    $('#dynamicTableContentsDevServers').load(url, formData);
			} else if (buttonName === 'addTestUser') {
				console.log("Adding test user");
			    $('#dynamicTableContentsTestUsers').load(url, formData);
let testUsers = $("testUsers");
			} else if (buttonName === 'addTestServer') {
				console.log("Adding test server");
			    $('#dynamicTableContentsTestServers').load(url, formData);
			} else {
				console.log("found another name: '" + buttonName + "'");
			}
			console.log("After pushing data, formData=" + JSON.stringify(formData));
    });

    // autodismiss alerts
    window.setTimeout(function() {
        $(".alert").fadeTo(500, 0).slideUp(500, function(){
            $(this).remove();
        });
    }, 4000);
});