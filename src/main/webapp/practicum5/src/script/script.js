const wmKey = "68d3df80956e68999b9b1055baa5ba98";

window.addEventListener('load', init);

function init(e){
    if(window.localStorage.getItem("weatherData")){
        let weatherData = JSON.parse(window.localStorage.getItem("weatherData"));
        let tempArray = [];
        let timestamp = Math.round(+new Date()/1000);
        for(weather of weatherData){
            if(weather.timeStamp < timestamp + 6000){
                tempArray.push(weather);
            }
        }
        weatherData = tempArray;
        window.localStorage.setItem("weatherData", JSON.stringify(weatherData));
    }else{
        window.localStorage.setItem("weatherData", JSON.stringify([]));
    }
    getIpInfo();
    requestCountries();
    document.querySelector("#addNew").addEventListener("click", function(e){
		addNewSpawner(e);
	});
	
    document.querySelector("#newCountryForm").addEventListener("submit", function(e){
    	e.preventDefault();
        addNewCountry();
    });
    
    document.querySelector("#closeNewCountry").addEventListener("click", function(e){
    	hideNewSpawner(e);
    });
}

function getIpInfo(){
    requestIpApi();
}

function requestIpApi(){
    fetch("https://ipapi.co/json")
    .then(response => response.json())
    .then(responseIpApi)
    .catch(function(error){
        console.log(error);
    });
}

function responseIpApi(data){
    AddKeyValue("#locationInfo","Landcode", data.country);
    AddKeyValue("#locationInfo","Land", data.country_name);
    AddKeyValue("#locationInfo","Regio", data.region);
    AddKeyValue("#locationInfo","Stad", data.city);
    AddKeyValue("#locationInfo","Postcode", data.postal);
    AddKeyValue("#locationInfo","Latitude", data.latitude);
    AddKeyValue("#locationInfo","Longitude", data.longitude);
    AddKeyValue("#locationInfo","IP", data.ip)
    document.querySelector("#weatherIn").innerHTML = data.city;
    requestWeather(data.latitude, data.longitude, data.city, data.country);
    document.querySelector("#locationFrame").addEventListener("click",function(){
        requestWeather(data.latitude, data.longitude, data.city, data.country);
    })
}

function AddKeyValue(addTo, key, value){
    let pairElement = document.querySelector(addTo);

    let kTextNode = document.createTextNode(key + ":");
    let vTextNode = document.createTextNode(value);

    let kElement = document.createElement("p");
    let vElement = document.createElement("p");

    kElement.classList.add("key");
    vElement.classList.add("value");

    kElement.appendChild(kTextNode);
    vElement.appendChild(vTextNode);
    
    pairElement.appendChild(kElement);
    pairElement.appendChild(vElement);
}

function requestWeather(lat, lon, city, cc){
    document.querySelector("#weatherIn").innerHTML = city;
    lat = +lat.toFixed(2);
    lon = +lon.toFixed(2);
    let weatherData = JSON.parse(window.localStorage.getItem("weatherData"))
    for(weather of weatherData){
        if(weather.sys.country === cc){
            formatWeatherData(weather);
            return;
        }
    }

    
    const url = "https://api.openweathermap.org/data/2.5/weather?"+
    "lat="+lat+"&"+
    "lon="+lon+"&"+
    "APPID="+wmKey+"&"+
    "units=metric&"+
    "lang=nl";

    console.log("making request to", url);

    document.querySelector("#weatherIn").innerHTML = city;

    fetch(url)
    .then(response => response.json())
    .then(responseWeather)
    .catch(function(error){
        console.log(error);
    });
}

function responseWeather(data){
    formatWeatherData(data);
    
    data.timeStamp = Math.round(+new Date()/1000);
    let weatherData = JSON.parse(window.localStorage.getItem("weatherData"))
    weatherData.push(data);
    window.localStorage.setItem("weatherData", JSON.stringify(weatherData));
}

function formatWeatherData(data){
    document.querySelector("#weatherInfo").innerHTML = "";
    AddKeyValue("#weatherInfo", "Temperatuur", data.main.temp + " °C");
    AddKeyValue("#weatherInfo", "Luchtvochtigheid", data.main.humidity);
    AddKeyValue("#weatherInfo", "Windsnelheid", data.wind.speed);
    AddKeyValue("#weatherInfo", "Windrichting", data.wind.deg);
    var sunrise = new Date(data.sys.sunrise*1000);
    AddKeyValue("#weatherInfo", "Zonsopgang", sunrise.getHours() + ":" + sunrise.getMinutes() + ":" + sunrise.getSeconds());
    var sunset =  new Date(data.sys.sunset*1000);
    AddKeyValue("#weatherInfo", "zonsondergang", sunset.getHours() + ":" + sunset.getMinutes() + ":" + sunset.getSeconds());
}

function requestCountries(){
    const url = "../restservices/countries";
    fetch(url)
    .then(response => Promise.all([response.status, response.json()]))
    .then(function([status, data]){
        if(status==200){
            responseCountries(data);
        }else{
            console.log(data.error);
        }
    })
    .catch(function(error){
        console.log(error);
    });
}

function responseCountries(data){
    for(country of data){
        addCountryToTable(country);
    }
}

function addCountryToTable(country){
    let table = document.querySelector("table");

    let tr = document.createElement("tr");
    tr.addEventListener("click", function(){
        requestWeather(country.latitude, country.longitude, country.capital, country.code);
    });
    
    let eName = document.createElement("td");
    let tName = document.createTextNode(country.name);
    eName.appendChild(tName);
    tr.appendChild(eName);

    let eCity = document.createElement("td");
    let tCity = document.createTextNode(country.capital);
    eCity.appendChild(tCity);
    tr.appendChild(eCity);

    let eRegion = document.createElement("td");
    let tRegion = document.createTextNode(country.region);
    eRegion.appendChild(tRegion);
    tr.appendChild(eRegion);

    let eSurface = document.createElement("td");
    let tSurface = document.createTextNode(country.surface);
    eSurface.appendChild(tSurface);
    tr.appendChild(eSurface);

    let ePopulation = document.createElement("td");
    let tPopulation = document.createTextNode(country.population);
    ePopulation.appendChild(tPopulation);
    tr.appendChild(ePopulation);

    let eEdit = document.createElement("td");
    let butEdit = document.createElement("button");
    butEdit.addEventListener("click", function(e){
        makeRowEditable(country.code, tr);
    });
    butEdit.classList.add("edit");
    eEdit.appendChild(butEdit);
    tr.appendChild(eEdit);

    let eRem = document.createElement("td");
    let butRem = document.createElement("button");
    butRem.addEventListener("click", function(){
        console.log(country.code);
        let success = deleteCountry(country.code, tr)
    });
    butRem.classList.add("remove");
    eRem.appendChild(butRem);
    tr.appendChild(eRem);

    table.appendChild(tr);
}

function deleteCountry(code, tableRow){
    const url = "../restservices/countries/"+code;
    fetch(url, {method: 'DELETE', headers: {
    	"Authorization": "Bearer " + window.sessionStorage.getItem("token")
    }})
    .then(function(response){
        if(response.ok){
            console.log("Country deleted");
            tableRow.remove();
        }else if(response.status===403){
        	throw new Error("Unauthorized user");
        }else{
            console.log(response.json().error);
        }
    }).catch(error => console.error(error))
}

function makeRowEditable(code, tableRow){
    for(let i = 0; i < 5; i++){
        let value = tableRow.children[i].innerText;
        let input = document.createElement("input");
        input.value = value;
        tableRow.children[i].innerHTML = "";
        tableRow.children[i].appendChild(input);
    }

    tableRow.children[5].innerHTML = "";
    let butEdit = document.createElement("button");
    butEdit.classList.add("confirm-edit");
    tableRow.children[5].appendChild(butEdit);
    butEdit.addEventListener("click", function(){
        makeChanges(code, tableRow);
    });
}

function makeChanges(code, tableRow){
    const url = "../restservices/countries/"+code;
    let formdata = new FormData();
    formdata.append("name", tableRow.children[0].firstElementChild.value);
    formdata.append("capital", tableRow.children[1].firstElementChild.value);
    formdata.append("region", tableRow.children[2].firstElementChild.value);
    formdata.append("surface", tableRow.children[3].firstElementChild.value);
    formdata.append("population", tableRow.children[4].firstElementChild.value);
    
    const data = new URLSearchParams();
    for (const pair of formdata) {
        data.append(pair[0], pair[1]);
    }
    
    fetch(url, {method: "PUT", body: data})
    .then(function(response){
        if(response.ok){
            console.log("Country changed");
            fixTableRow(tableRow, data, code);
        }else{
            console.error(response.json().error);
        }
    });
}

function fixTableRow(tableRow, data, code){
    for(let i = 0; i < 5; i++){
        tableRow.children[i].innerHTML="";
    }
    tableRow.children[0].appendChild(document.createTextNode(data.get("name")));
    tableRow.children[1].appendChild(document.createTextNode(data.get("capital")));
    tableRow.children[2].appendChild(document.createTextNode(data.get("region")));
    tableRow.children[3].appendChild(document.createTextNode(data.get("surface")));
    tableRow.children[4].appendChild(document.createTextNode(data.get("population")));
    tableRow.children[5].innerHTML = "";
    let butEdit = document.createElement("button");
    butEdit.addEventListener("click", function(e){
        makeRowEditable(code, tableRow);
    });
    butEdit.classList.add("edit");
    tableRow.children[5].appendChild(butEdit);
}

function addNewSpawner(e){
	document.querySelector("#newCountry").classList.remove("hidden");
}

function hideNewSpawner(e){
	document.querySelector("#newCountry").classList.add("hidden");
}

function addNewCountry(){
	const form = document.querySelector("#newCountryForm");
	const formData = new FormData(form);

	const data = new URLSearchParams();
	for (const pair of formData) {
	    data.append(pair[0], pair[1]);
	}
	
	const url = "../restservices/countries";
	fetch(url, {method: "post", body: data})
	.then((res) => {
        if (res.status === 400) {
            throw new Error('Bad request');
        }else if(res.status === 404){
        	console.log('Internal server error');
        }else if(res.ok){
        	console.log("added country");
        }
        return res.json();
    })
    .then(json => {
        console.log(json);
    })
    .catch(ex => {
    	console.error(ex, ex.stack);
    });
}