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
    .then(response => response.json())
    .then(responseCountries)
    .catch(function(error){
        console.log(error);
    });
}

function responseCountries(data){
    data.sort((a, b) => a.name.localeCompare(b.name));
    for(country of data){
        addCountryToTable(country);
    }
}

function addCountryToTable(country){
    let table = document.querySelector("table");

    let tr = document.createElement("tr");
    tr.addEventListener("click", function(){
        requestWeather(country.lat, country.lon, country.capital, country.code);
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

    table.appendChild(tr);
}